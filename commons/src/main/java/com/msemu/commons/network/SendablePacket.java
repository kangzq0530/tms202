package com.msemu.commons.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Weber on 2018/3/14.
 */

public abstract class SendablePacket<TClient extends Client<TClient>> {
    private static final Logger log = LoggerFactory.getLogger(SendablePacket.class);
    private boolean encrypt = true;
    private boolean hasOpcode = true;

    public SendablePacket() {
    }

    protected abstract void writeBody(SendByteBuffer byteBuffer);

    protected boolean write(TClient client, SendByteBuffer buffer) {
        try {
            if(hasOpcode) {
                this.writeOpCode(client, buffer);
            }
            this.writeBody(buffer);
            return true;
        } catch (Exception except) {
            log.error("Error while write opcodes [{}]", this.getClass().getSimpleName(), except);
            return false;
        }
    }

    protected void writeOpCode(TClient client, SendByteBuffer buffer) {
        buffer.writeShort(client.getPacketHandler().getServerPacketOpCode(this));
    }

    public boolean isEncrypt() {
        return this.encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public void setHasOpcode(boolean value) {
        this.hasOpcode = value;
    }

    public boolean hasOpcode() {
        return this.hasOpcode;
    }

}
