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

import com.msemu.commons.enums.ClientState;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.DateUtils;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.field.LP_SetQuestClear;
import com.msemu.core.network.packets.outpacket.funckey.LP_FuncKeyMappedInit;
import com.msemu.core.network.packets.outpacket.socket.LP_AuthenCodeChanged;
import com.msemu.core.network.packets.outpacket.wvscontext.*;
import com.msemu.world.Channel;
import com.msemu.world.World;
import com.msemu.world.client.Account;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.party.Party;
import com.msemu.world.client.field.Field;
import com.msemu.world.constants.MapleJob;
import com.msemu.world.service.PartyService;

/**
 * Created by Weber on 2018/4/22.
 */
public class CP_MigrateIn extends InPacket<GameClient> {

    private int worldId;

    private int characterId;

    public CP_MigrateIn(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {

        worldId = decodeInt();
        characterId = decodeInt();

    }

    @Override
    public void runImpl() {

        World world = World.getInstance();
        Channel channel = world.getChannels()
                .stream()
                .filter(ch -> ch.hasTransferByCharacterId(characterId))
                .findFirst().orElse(null);

        if (worldId != world.getWorldId() || channel == null) {
            getClient().close();
            return;
        }

        // TODO 這邊可能有雙登問題
        Character chr = channel.getTransferIdAndRemoveByCharacterId(characterId);

        if (chr == null) {
            getClient().close();
            return;
        }
        //TODO 確認登入伺服器跟現在的IP一樣

        if (getClient().compareAndSetState(ClientState.CONNECTED, ClientState.ENTERED)) {
            channel.addCharacter(chr);

            Account account = Account.findById(chr.getAccId());
            getClient().setAccount(account);
            getClient().setChannel(channel);
            getClient().setCharacter(chr);
            getClient().getChannelInstance().addCharacter(chr);
            getClient().write(new LP_EventNameTagInfo());
            getClient().write(new LP_RequestEventList(true));

            final Field field = channel.getField(chr.getFieldID() <= 0 ? 100000000 : chr.getFieldID());
            final Party party = PartyService.getInstance().findCharacterParty(chr.getId());

            chr.setClient(getClient());

            chr.warp(field, true);
            chr.renewCharacterStats();
            chr.setJob(chr.getJob());
            chr.setParty(party);
            chr.setOnline(true);
            chr.write(new LP_AuthenCodeChanged());
            chr.write(new LP_SetQuestClear());
            chr.write(new LP_HourChanged(DateUtils.getCurrentDayOfWeek()));
            chr.write(new LP_SetTamingMobInfo(chr, false));
            //0x020F:
            chr.write(new LP_ChangeSkillRecordResult(chr.getVisibleSkills(), true,
                    false, false, false));
            // TODO update VSkillRecord
            chr.write(new LP_ForcedStatReset());
            //TODO c.announce(CWvsContext.broadcastMsg(channelServer.getServerMessage(player.getWorld())));
            // GM hide
            /**
             *         if (player.getQuestNoAdd(MapleQuest.getInstance(GameConstants.墜飾欄)) != null
             && player.getQuestNoAdd(MapleQuest.getInstance(GameConstants.墜飾欄)).getCustomData() != null
             && "0".equals(player.getQuestNoAdd(MapleQuest.getInstance(GameConstants.墜飾欄)).getCustomData())) {//更新永久墜飾欄
             c.announce(CWvsContext.updatePendantSlot(0));
             }
             */
            if (MapleJob.is幻獸師(chr.getJob())) {
                // ??
                chr.write(new LP_FuncKeyMappedInit(chr.getFuncKeyMap()));
            } else {
                chr.write(new LP_FuncKeyMappedInit(chr.getFuncKeyMap()));
            }

            chr.write(new LP_MacroSysDataInit(chr.getSkillMacros()));


        }


    }
}
