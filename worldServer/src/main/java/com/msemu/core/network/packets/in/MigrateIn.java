package com.msemu.core.network.packets.in;

import com.msemu.commons.enums.ClientState;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.DateUtils;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.out.Field.SetQuestClear;
import com.msemu.core.network.packets.out.WvsContext.EventNameTagInfo;
import com.msemu.core.network.packets.out.WvsContext.HourChanged;
import com.msemu.core.network.packets.out.WvsContext.RequestEventList;
import com.msemu.core.network.packets.out.WvsContext.SetTamingMobInfo;
import com.msemu.world.World;
import com.msemu.world.channel.Channel;
import com.msemu.world.client.Account;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;

/**
 * Created by Weber on 2018/4/22.
 */
public class MigrateIn extends InPacket<GameClient> {

    private int worldId;

    private int characterId;

    public MigrateIn(short opcode) {
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
        Character character = channel.getTransferAndRemoveByCharacterId(characterId);
        if (character == null) {
            getClient().close();
            return;
        }

        //TODO 確認登入伺服器跟現在的IP一樣

        if (getClient().compareAndSetState(ClientState.CONNECTED, ClientState.AUTHED)) {
            Account account = Account.findById(character.getAccId());
            getClient().setAccount(account);
            getClient().setChannel(channel);
            getClient().setCharacter(character);
            getClient().getChannelInstance().addCharacter(character);
            getClient().getCharacter().setOnline(true);
            getClient().write(new EventNameTagInfo());
            getClient().write(new RequestEventList(true));
            character.renewBulletIDForAttack();
            Field field = channel.getField(character.getFieldID() <= 0 ? 100000000 : character.getFieldID());
            character.warp(field);
            getClient().write(new SetQuestClear());
            getClient().write(new HourChanged(DateUtils.getCurrentDayOfWeek()));
            getClient().write(new SetTamingMobInfo(character, false));
            //0x020F:



        }


    }
}
