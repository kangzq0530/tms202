package com.msemu.commons.network.netty;

import com.msemu.commons.network.InPacket;
import com.msemu.commons.network.Packet;
import com.msemu.commons.network.crypt.MapleCrypt;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.concurrent.locks.ReentrantLock;

public abstract class NettyClient<TClient extends NettyClient<TClient>> {

    /**
     * Attribute key for MapleCrypto related to this Client.
     */
    public static final AttributeKey<MapleCrypt> CRYPTO_KEY = AttributeKey.valueOf("A");
    /**
     * Attribute key for this NettyClient object.
     */
    public static final AttributeKey<NettyClient> CLIENT_KEY = AttributeKey.valueOf("C");

    /**
     * Send seed or IV for one of the cryptography stages.
     */
    private byte[] siv;
    /**
     * Receive seed or IV for one of the cryptography stages.
     */
    private byte[] riv;
    /**
     * Stored length used for packet decryption. This is used for
     * storing the packet length for the next packet that is readable.
     * Since TCP sessions ensure that all data arrives to the server in order,
     * we can decodeByte packet data in the correct order.
     */
    private int storedLength = -1;
    /**
     * Channel object associated with this specific client. Used for all
     * I/O operations regarding a MapleStory game session.
     */
    protected final Channel ch;

    /**
     * Lock regarding the encoding of packets to be sent to remote
     * sessions.
     */
    private final ReentrantLock lock;

    /**
     * InPacket object for this specific session since this can help
     * scaling compared to keeping OutPacket for each session.
     */
    private final InPacket r;

    /**
     * Empty constructor for child class implementation.
     */
    private NettyClient() {
        ch = null;
        lock = null;
        r = null;
    }

    /**
     * Construct a new NettyClient with the corresponding Channel that
     * will be used to write to as well as the send and recv seeds or IVs.
     * @param c the channel object associated with this client session.
     * @param siv the send seed or IV.
     * @param riv the recv seed or IV.
     */
    public NettyClient(Channel c, byte[] siv, byte[] riv) {
        ch = c;
        this.siv = siv;
        this.riv = riv;
        r = new InPacket();
        lock = new ReentrantLock(true); // note: lock is fair to ensure logical sequence is maintained server-side
    }

    /**
     * Gets the InPacket object associated with this NettyClient.
     * @return a packet reader.
     */
    public final InPacket getReader() {
        return r;
    }

    /**
     * Gets the stored length for the next packet to be read. Used as
     * a decoding state variable to determine when it is ok to proceed with
     * decoding a packet.
     * @return stored length for next packet.
     */
    public final int getStoredLength() {
        return storedLength;
    }

    /**
     * Sets the stored length for the next packet to be read.
     * @param val length of the next packet to be read.
     */
    public final void setStoredLength(int val) {
        storedLength = val;
    }

    /**
     * Gets the current send seed or IV.
     * @return send IV.
     */
    public final byte[] getSendIV() {
        return siv;
    }

    /**
     * Gets the current recv seed or IV.
     * @return recv IV.
     */
    public final byte[] getRecvIV() {
        return riv;
    }

    /**
     * Sets the send seed or IV for this session.
     * @param alpha the new send IV.
     */
    public final void setSendIV(byte[] alpha) {
        siv = alpha;
    }

    /**
     * Sets the recv seed or IV for this session.
     * @param delta  the new recv IV.
     */
    public final void setRecvIV(byte[] delta) {
        riv = delta;
    }

    /**
     * Writes a packet message to the channel. Gets encoded later in the
     * pipeline.
     * @param msg the packet message to be sent.
     */
    public void write(Packet msg) {
        ch.writeAndFlush(msg);
    }

    /**
     * Closes this channel and session.
     */
    public void close() {
        ch.close();
    }

    /**
     * Gets the remote IP address for this session.
     * @return the remote IP address.
     */
    public String getIP() {
        return ch.remoteAddress().toString().split(":")[0].substring(1);
    }

    /**
     * Acquires the encoding state for this specific send IV. This is to
     * prevent multiple encoding states to be possible at the same time. If
     * allowed, the send IV would mutate to an unusable IV and the session would
     * be dropped as a result.
     */
    public final void acquireEncoderState() {
        lock.lock();
    }

    /**
     * Releases the encoding state for this specific send IV.
     */
    public final void releaseEncodeState() {
        lock.unlock();
    }

    public abstract void onInit();

    public abstract void onOpen();

    public abstract void onClose();
}