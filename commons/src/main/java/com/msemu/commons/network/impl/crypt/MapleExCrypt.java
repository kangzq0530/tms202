package com.msemu.commons.network.impl.crypt;

import java.nio.ByteBuffer;

/**
 * Created by Weber on 2018/3/14.
 */
public class MapleExCrypt extends MapleAESOFB{
    public MapleExCrypt(byte[] iv, short serverVersion, boolean isSend) {
        super(iv, serverVersion, isSend);
    }

    @Override
    public boolean crypt(ByteBuffer buff, int offset, int length) {
        byte key = this.getIV()[0];
        for(int i = offset; i < (offset+length); i++) {
            buff.put(i, (byte)(buff.get(i) + key));
        }
        super.updateIv();
        return true;
    }
}
