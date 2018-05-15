package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.lifes.Drop;
import com.msemu.world.client.field.lifes.DropInfo;
import com.msemu.world.enums.DropType;

/**
 * Created by Weber on 2018/5/16.
 */
public class CP_UserDropMoneyRequest extends InPacket<GameClient> {

    private int updateTick;
    private int money;

    public CP_UserDropMoneyRequest(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        updateTick = decodeInt();
        money = decodeInt();
    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        Field field = chr.getField();
        if(!chr.isAlive() || money < 10 || money > 50000 || money > chr.getMoney()) {
            chr.enableActions();
            return;
        }
        chr.addMoney(-money);
        Drop drop = new Drop(-1);
        drop.setMoney(money);
        drop.setDropType(DropType.MONEY);
        drop.setOwnerID(chr.getId());
        drop.setPosition(chr.getPosition());
        field.drop(drop, chr.getPosition(), chr.getPosition());
    }
}
