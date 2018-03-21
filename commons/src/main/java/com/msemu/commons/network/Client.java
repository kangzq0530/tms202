package com.msemu.commons.network;

import com.msemu.commons.network.handler.AbstractPacketHandlerFactory;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/14.
 */
public abstract class Client<TClient extends Client<TClient>> {
    private static final Logger log = LoggerFactory.getLogger("Network");
    private final Connection<TClient> connection;
    private final AtomicReference<EClientState> state;

    public Client(Connection<TClient> connection) {
        this.state = new AtomicReference<>(EClientState.NULL);
        this.connection = connection;
    }


    public Connection<TClient> getConnection() {
        return this.connection;
    }

    public int getConnectionId() {
        return this.getConnection().getConnectionId();
    }

    public EClientState getState() {
        return this.state.get();
    }

    public boolean isConnected() {
        return !this.getConnection().isClosedOrPending();
    }

    protected void onOpen() {
        if (this.state.compareAndSet(EClientState.NULL, EClientState.CONNECTED)) {
            log.info("Client [{}] connected.", this);
        }

    }

    protected void onClose() {
        if (this.state.compareAndSet(EClientState.CONNECTED, EClientState.DISCONNECTED)
                || this.state.compareAndSet(EClientState.ENTERED, EClientState.DISCONNECTED)) {
            log.info("Client [{}] disconnected.", this);
        }

    }

    public void sendPacket(SendablePacket<TClient> packet) {
        if (this.isConnected() && packet != null) {
            this.getConnection().sendPacket(packet);
        }
    }

    public void close() {
        if (this.isConnected()) {
            this.getConnection().close();
        }
    }

    public boolean compareAndSetState(EClientState beforeState, EClientState afterState) {
        return this.state.compareAndSet(beforeState, afterState);
    }


    public String getHostAddress() {
        return this.isConnected() ? this.getConnection().getSocketAddress().getAddress().getHostAddress() : "not connected";
    }

    public AbstractPacketHandlerFactory<TClient> getPacketHandler() {
        return this.connection.getServer().getPacketHandler();
    }

    public String toString() {
        switch (state.get()) {
            case NULL:
                break;
            case CONNECTED:
            case AUTHED_GG:
            case AUTHED:
            case ENTERED:
            case DISCONNECTED:
                return "IP: " + this.getHostAddress() + " State: " + this.state.get();
        }
        return "";
    }
}
