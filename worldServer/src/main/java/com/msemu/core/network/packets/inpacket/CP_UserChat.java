/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.user.local.LP_UserChat;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.commands.CommandProcessor;
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
            if (CommandProcessor.getCommands().containsKey(args.get(0).substring(1))) {
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
