package com.msemu.world.client.character;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.GameClient;

/**
 * Created by Weber on 2018/5/22.
 */
public class AndroidInfo {
    //TODO DB表
    private String name;
    private int skin;
    private int hair;
    private int face;
    private int drawElfEar;
    private FileTime dateExpire = FileTime.getFileTimeFromType(FileTime.Type.ZERO_TIME);

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeShort(skin);
        outPacket.encodeShort(hair);
        outPacket.encodeShort(face);
        outPacket.encodeString(name);
        outPacket.encodeInt(drawElfEar);
        outPacket.encodeFT(dateExpire);
    }
}
