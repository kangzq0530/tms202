package com.msemu.world.client.character.quest.act;

import com.msemu.commons.data.dat.DatSerializable;
import com.msemu.commons.data.templates.quest.actions.QuestActData;
import com.msemu.commons.data.templates.quest.actions.QuestExpActionData;
import com.msemu.world.client.character.Character;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestExpAction implements IQuestAction {
    @Getter
    @Setter
    private long exp;
    @Override
    public void action(Character chr) {
        chr.addExp(getExp());
    }

    @Override
    public void load(QuestActData actData) {
        if(actData instanceof QuestExpActionData) {
            setExp(((QuestExpActionData)actData).getExp());
        }
    }

}
