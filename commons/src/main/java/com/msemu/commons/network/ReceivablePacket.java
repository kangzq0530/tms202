package com.msemu.commons.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Weber on 2018/3/14.
 */
public abstract class ReceivablePacket<TClient extends Client<TClient>> implements Runnable, Cloneable {
    private static final Logger log = LoggerFactory.getLogger(ReceivablePacket.class);
    protected short opcode;
    private TClient client;
    private RecvByteBuffer recvByteBuffer;

    public ReceivablePacket(short opcode) {
        this.opcode = opcode;
    }

    protected abstract void read();

    public void run() {
        try {
            this.runImpl();
        } catch (Exception except) {
            log.error("Error while running {} opcodes", this.getClass().getSimpleName(), except);
        }
    }

    public abstract void runImpl();

    public TClient getClient() {
        return this.client;
    }

    public void setClient(TClient client) {
        this.client = client;
    }

    public short getOpcode() {
        return this.opcode;
    }

    public RecvByteBuffer getRecvByteBuffer() {
        return this.recvByteBuffer;
    }

    public void setRecvByteBuffer(RecvByteBuffer recvByteBuffer) {
        this.recvByteBuffer = recvByteBuffer;
    }

    protected void sendPacket(SendablePacket<TClient> packet) {
        this.client.getConnection().sendPacket(packet);
    }

    public ReceivablePacket<TClient> clonePacket() {
        try {
            return (ReceivablePacket<TClient>) super.clone();
        } catch (Exception var2) {
            log.error("Error while cloning ReceivablePacket: {}", this.getClass().getSimpleName(), var2);
            return null;
        }
    }

    @Override
    public String toString() {
        return "{" + this.getClass().getSimpleName() + " from " + this.getClient() + "}";
    }
}
