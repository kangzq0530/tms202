package com.msemu.commons.network;

import com.msemu.core.configs.NetworkConfig;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Created by Weber on 2018/3/14.
 */
public class Connection<TClient extends Client<TClient>> extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(Connection.class);

    private ICipher sendCipher;
    private ICipher recvCipher;
    private TClient client;
    private Channel channel;
    private NetworkThread<TClient> server;
    private AtomicReference<EConnectionState> connectionState;
    private int decodePacketSize = -1;

    Connection(NetworkThread<TClient> server) {
        this.server = server;
        this.connectionState = new AtomicReference<>(EConnectionState.OPEN);
    }

    public ICipher getSendCipher() {
        return this.sendCipher;
    }

    public ICipher getRecvCipher() {
        return this.recvCipher;
    }

    public void setSendCipher(ICipher cipher) {
        this.sendCipher = cipher;
    }

    public void setRecvCipher(ICipher cipher) {
        this.recvCipher = cipher;
    }

    public TClient getClient() {
        return this.client;
    }

    public NetworkThread<TClient> getServer() {
        return this.server;
    }

    public InetSocketAddress getSocketAddress() {
        return this.channel != null ? (InetSocketAddress) this.channel.remoteAddress() : null;
    }

    public int getConnectionId() {
        return this.channel.id().hashCode();
    }

    public boolean isClosedOrPending() {
        switch (connectionState.get()) {
            case PENDING_CLOSE:
            case CLOSED:
                return true;
            default:
                return false;
        }
    }

    public void sendPacket(SendablePacket<TClient> packet) {
        SendByteBuffer byteBuffer = new SendByteBuffer();
        if (NetworkConfig.DEBUG) {
            log.warn("Server -> Client: {}", packet.getClass().getSimpleName());
        }

        int headerSize = packet.isEncrypt() ? 4 : 0;

        if (headerSize > 0) {
            byteBuffer.writeInt(0);
        }

        if (!packet.write(getClient(), byteBuffer)) {
            return;
        }

        int bodySize = byteBuffer.position() - headerSize;

        if (packet.isEncrypt()) {
            int frameSize = bodySize + headerSize;
            byteBuffer.limit(frameSize);
            getSendCipher().crypt(byteBuffer.getBuffer(), headerSize, bodySize);
            byteBuffer.position(0);
            byteBuffer.getBuffer().put(getSendCipher().encodeHeaderSize(bodySize));
            byte[] frame = new byte[frameSize];
            byteBuffer.position(0);
            byteBuffer.getBuffer().get(frame, 0, frameSize);
            this.channel.writeAndFlush(frame);
        } else {
            int frameSize = bodySize;
            byteBuffer.position(0);
            byteBuffer.limit(frameSize);
            byte[] frame = new byte[frameSize];
            byteBuffer.position(0);
            byteBuffer.limit(frameSize);
            byteBuffer.getBuffer().get(frame, 0, frameSize);
            this.channel.writeAndFlush(frame);
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        this.client = this.server.getClientFactory().createClient(this);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.channel = ctx.channel();
        this.client.onOpen();
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        this.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) {
        if (!(object instanceof ByteBuf)) {
            return;
        }
        ByteBuf byteBuf = (ByteBuf) object;
        if (decodePacketSize == -1 && byteBuf.readableBytes() < 4)
            return;
        if (decodePacketSize == -1) {
            final int header = byteBuf.readInt();
            if (!getRecvCipher().checkHeader(header)) {
                close();
                return;
            } else {
                decodePacketSize = getRecvCipher().decodeHeaderSize(header);
            }
        }
        if(byteBuf.readableBytes() < decodePacketSize) {
            return;
        }

        ByteBuffer buffer = ByteBuffer.allocateDirect(decodePacketSize).order(ByteOrder.LITTLE_ENDIAN);
        byteBuf.readBytes(buffer);
        buffer.position(0);

        getRecvCipher().crypt(buffer, 0, decodePacketSize);

        short opcode = buffer.getShort();
        ReceivablePacket<TClient> revPacket = this.getServer().getPacketHandler().handleClientPacket(opcode, client);
        if (revPacket != null) {
            if (NetworkConfig.DEBUG) {
                log.warn("[Client -> Server] {}", revPacket.getClass().getSimpleName());
                log.warn("Data: %s", revPacket.getRecvByteBuffer().toString());
            }

            RecvByteBuffer recvBuffer = new RecvByteBuffer(buffer);
            revPacket.setClient(client);
            revPacket.setRecvByteBuffer(recvBuffer);

            try {
                revPacket.read();
                revPacket.run();
            } catch (Exception except) {
                log.error("Created opcodes [{}] has not been read.", revPacket.getClass().getSimpleName(), except);
                this.close();
            }
        } else {
            log.error(String.format("[Client -> Server] Opcode: %02X not found",opcode));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext var1, Throwable exceptionCaught) {
        exceptionCaught.printStackTrace();
        if (!(exceptionCaught instanceof ReadTimeoutException) && !(exceptionCaught instanceof IOException)) {
            log.error("An exception occured when reading a opcodes.", new Exception(exceptionCaught));
            this.close();
        } else {
            if (exceptionCaught instanceof ReadTimeoutException) {
                log.error("User has been disconnected due ReadTimeoutException.");
            }

            this.close();
        }
    }

    protected synchronized void close() {
        if ((this.connectionState.compareAndSet(Connection.EConnectionState.OPEN, Connection.EConnectionState.CLOSED) || this.connectionState.compareAndSet(Connection.EConnectionState.PENDING_CLOSE, Connection.EConnectionState.CLOSED)) && this.client != null) {
            this.client.onClose();
        }
        if (this.channel != null) {
            this.channel.close();
        }
    }


    private enum EConnectionState {
        OPEN,
        PENDING_CLOSE,
        CLOSED;

        EConnectionState() {
        }
    }

}
