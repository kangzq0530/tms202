package com.msemu.core.network.packets.in;

import com.msemu.commons.enums.ClientState;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.DateUtils;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.out.field.LP_SetQuestClear;
import com.msemu.core.network.packets.out.funckey.LP_FuncKeyMappedInit;
import com.msemu.core.network.packets.out.wvscontext.*;
import com.msemu.world.World;
import com.msemu.world.channel.Channel;
import com.msemu.world.client.Account;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;
import com.msemu.world.constants.MapleJob;

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
            chr.setClient(getClient());
            Account account = Account.findById(chr.getAccId());
            getClient().setAccount(account);
            getClient().setChannel(channel);
            getClient().setCharacter(chr);
            getClient().getChannelInstance().addCharacter(chr);
            getClient().getCharacter().setOnline(true);
            getClient().write(new LP_EventNameTagInfo());
            getClient().write(new LP_RequestEventList(true));
            chr.renewBulletIDForAttack();
            Field field = channel.getField(chr.getFieldID() <= 0 ? 100000000 : chr.getFieldID());
            chr.warp(field, true);
            // TODO  c.announce(CCashShop.onAuthenCodeChanged()); // Enable CashShop
            getClient().write(new LP_SetQuestClear());
            getClient().write(new LP_HourChanged(DateUtils.getCurrentDayOfWeek()));
            getClient().write(new LP_SetTamingMobInfo(chr, false));
            //0x020F:
            getClient().write(new LP_ChangeSkillRecordResult(chr.getSkills(), true,
                    false, false, false));

            // TODO update VSkillRecord
            getClient().write(new LP_ForcedStatReset());

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
                getClient().write(new LP_FuncKeyMappedInit(chr.getFuncKeyMap()));
            } else {
                getClient().write(new LP_FuncKeyMappedInit(chr.getFuncKeyMap()));
            }

        }


    }
}
