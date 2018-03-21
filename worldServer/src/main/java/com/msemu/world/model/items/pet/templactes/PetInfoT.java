package com.msemu.world.model.items.pet.templactes;

import com.msemu.commons.data.provider.interfaces.MapleData;

import java.util.HashMap;

/**
 * Created by Weber on 2018/3/21.
 */
public class PetInfoT {

    private final int itemID;

    private final int hungry;

    private final boolean isCash;

    private final int life;

    private final HashMap<Integer, PetCommandT> commands;

    public PetInfoT(MapleData source) {
        commands = new HashMap<>();
        itemID = Integer.parseInt(source.getName().replace(".img", ""));
        hungry = (Integer) source.getChildByPath("info/hungry").getData();
        isCash = (Integer) source.getChildByPath("info/cash").getData() > 0;
        life = (Integer) source.getChildByPath("info/life").getData();
        source.getChildByPath("interact").forEach(commandData->{
            PetCommandT petCommandT = new PetCommandT(commandData);
            commands.put(petCommandT.getIndex(), petCommandT);
        });
    }

    public int getItemID() {
        return itemID;
    }

    public int getHungry() {
        return hungry;
    }

    public boolean isCash() {
        return isCash;
    }

    public int getLife() {
        return life;
    }

    public PetCommandT getCommand(int skillID) {
        return commands.get(skillID);
    }
}
