package com.msemu.commons.network.crypt.keys;

import com.msemu.commons.enums.GameServiceType;
import com.msemu.commons.utils.BitUtils;
import com.msemu.commons.utils.HexUtils;

/**
 * Created by Weber on 2018/3/28.
 */
public class MapleKeys {


    private enum  EncryptionKey {
        Default(GameServiceType.Taiwan, (short)999, "130000000800000006000000B40000001B0000000F0000003300000052000000");

        private final byte[] skey;
        private final short version;
        private GameServiceType serviceType;

        EncryptionKey(GameServiceType serviceType, short version, String skey) {
            this.skey = HexUtils.hex2Byte(skey);
            this.version = version;
            this.serviceType =serviceType;
        }

        public byte[] getKey() {
            return this.skey;
        }
    }


    public static byte[] getEncryptKey(GameServiceType serviceType, short version) {

        // 台版 176 之後
        if(serviceType.equals(GameServiceType.Taiwan) && version >= 176) {
            return MapleTWKeys.getEncryptionKey(version);
        }
        return EncryptionKey.Default.getKey();
}

}
