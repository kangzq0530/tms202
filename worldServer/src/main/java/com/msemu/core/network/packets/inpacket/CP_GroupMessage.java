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
import com.msemu.core.network.packets.outpacket.field.LP_GroupMessage;
import com.msemu.world.World;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.friends.FriendList;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.client.guild.Guild;
import com.msemu.world.enums.GroupMessageType;

import java.util.ArrayList;
import java.util.List;

public class CP_GroupMessage extends InPacket<GameClient> {

    private int typeValue;
    private List<Integer> targets = new ArrayList<>();
    private String text;


    public CP_GroupMessage(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

        typeValue = decodeByte();
        final int targetCount = decodeInt();
        for (int i = 0; i < targetCount; i++)
            targets.add(decodeInt());
        text = decodeString();

    }

    @Override
    public void runImpl() {

        final GroupMessageType type = GroupMessageType.getByValue(typeValue);
        final Character chr = getClient().getCharacter();
        final World world = getClient().getWorld();

        OutPacket<GameClient> packet = new LP_GroupMessage(type, chr.getName(), text);

        switch (type) {
            case FRIENDS:
                final FriendList friendList = chr.getFriendList();
                targets.forEach(targetId -> {
                    if (friendList.getByCharId(targetId) == null)
                        return;
                    Character target = world.getCharacterById(targetId);
                    if (target == null)
                        return;
                    target.write(packet);
                });
                break;
            case PARTY:
                final Party party = chr.getParty();
                party.broadcastPacket(packet);
                break;
            case GUILD:
                final Guild guild = chr.getGuild();
                guild.broadcast(packet);
                break;
            case ALLIANCE:
                // TODO
                break;
            case EXPEDITION:
                // TODO
                break;
        }

    }
}
