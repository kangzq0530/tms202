package com.msemu.commons.network.crypt;

import com.msemu.commons.enums.GameServiceType;

/**
 * Created by Weber on 2018/3/14.
 */
public class MapleExCrypt extends MapleCrypt {
    public MapleExCrypt(GameServiceType serviceType, short serverVersion) {
        super(serviceType, serverVersion);
    }

    @Override
    public void crypt(byte[] data, byte[] iv) {
        byte key = iv[0];
        for (int i = 0; i < data.length; i++)
            data[i] += key;
    }
}
