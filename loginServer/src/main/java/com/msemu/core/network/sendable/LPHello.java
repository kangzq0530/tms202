package com.msemu.core.network.sendable;

import com.msemu.commons.network.ICipher;
import com.msemu.commons.network.SendByteBuffer;
import com.msemu.commons.network.SendablePacket;
import com.msemu.commons.network.impl.crypt.MapleAESOFB;
import com.msemu.core.configs.CoreConfig;
import com.msemu.core.network.LoginClient;

/**
 * Created by Weber on 2018/3/16.
 */
public class LPHello extends SendablePacket<LoginClient> {

    final MapleAESOFB sendCipher;
    final MapleAESOFB recvCipher;

    public LPHello(final ICipher sendCipher, final ICipher recvCipher) {
        super();
        this.sendCipher = (MapleAESOFB) sendCipher;
        this.recvCipher = (MapleAESOFB) recvCipher;
        this.setEncrypt(false);
        this.setHasOpcode(false);
    }

    @Override
    protected void writeBody(SendByteBuffer buffer) {
        buffer.writeShort(0x2E);
        buffer.writeShort(CoreConfig.GAME_VERSION);
        buffer.writeMapleAsciiString(CoreConfig.GAME_PATCH_VERSION);
        buffer.writeBytes(recvCipher.getIV());
        buffer.writeBytes(sendCipher.getIV());
        buffer.write(CoreConfig.GAME_SERVICE_TYPE.getLocal());
        buffer.write(0);
        buffer.writeShort(CoreConfig.GAME_VERSION);
        buffer.writeShort(CoreConfig.GAME_VERSION);
        buffer.writeShort(0);
        buffer.writeBytes(recvCipher.getIV());
        buffer.writeBytes(sendCipher.getIV());
        buffer.write(CoreConfig.GAME_SERVICE_TYPE.getLocal());
        buffer.writeMapleAsciiString(CoreConfig.GAME_PATCH_VERSION);
        buffer.writeMapleAsciiString(CoreConfig.GAME_PATCH_VERSION);
        buffer.writeInt(0);
        buffer.writeShort(1);
    }
}
