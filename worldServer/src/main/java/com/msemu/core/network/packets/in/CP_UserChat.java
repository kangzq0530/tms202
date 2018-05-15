package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.out.user.local.LP_ChatMsg;
import com.msemu.core.network.packets.out.user.local.LP_UserChat;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.lifes.DropInfo;

/**
 * Created by Weber on 2018/5/5.
 */
public class CP_UserChat extends InPacket<GameClient> {

    private int tick;
    private String text;
    private byte onlyBallon;

    public CP_UserChat(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        tick = decodeInt();
        text = decodeString();
        onlyBallon = decodeByte();
    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        // TODO 指令系統
        DropInfo info = new DropInfo();
        info.setItemID(0);
        info.setChance(1000000);
        info.setMaxQuantity(500);
        info.setMinQuantity(500);
        info.setMobID(0);
        for(int i = 0 ; i < 100; i++)
            chr.getField().drop(info, chr.getPosition(),chr.getPosition(), 0);
        chr.getScriptManager().stopScript();
        OutPacket<GameClient> chatPacket = new LP_UserChat(chr.getId(), text, onlyBallon);
        chr.getField().broadcastPacket(chatPacket);
    }
}
