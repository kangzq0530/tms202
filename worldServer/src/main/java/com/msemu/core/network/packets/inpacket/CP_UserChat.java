package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.user.local.LP_ChatMsg;
import com.msemu.core.network.packets.outpacket.user.local.LP_UserChat;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.commands.CommandProcessor;
import com.msemu.world.enums.ChatMsgType;
import com.msemu.world.enums.Stat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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


        chr.addStat(Stat.MP, 50);

        if (text.charAt(0) == '!') {
            List<String> args = Arrays.stream(text.toLowerCase().split(" ")).collect(Collectors.toList());
            if(CommandProcessor.getCommands().containsKey(args.get(0).substring(1))) {
                try {
                    CommandProcessor.getCommands().get(args.get(0).substring(1)).newInstance().execute(getClient(), args);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        chr.getScriptManager().stopScript();
        OutPacket<GameClient> chatPacket = new LP_UserChat(chr.getId(), text, onlyBallon);
        chr.getField().broadcastPacket(chatPacket);
    }
}
