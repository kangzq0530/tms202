package com.msemu.world.client.character.quest.act;

import com.msemu.commons.data.templates.quest.actions.QuestActData;
import com.msemu.commons.data.templates.quest.actions.QuestItemActData;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.constants.ItemConstants;
import com.msemu.world.data.ItemData;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestItemAction implements IQuestAction {
    @Getter
    @Setter
    private int itemId;
    @Getter
    @Setter
    private short quantity;
    @Getter
    @Setter
    private String potentialGrade;
    @Getter
    @Setter
    private int status;
    @Getter
    @Setter
    private int prop;
    @Getter
    @Setter
    private int gender;

    @Getter
    @Setter
    private List<Short> jobs = new ArrayList<>();


    @Override
    public void action(Character chr) {
        Item item;
        if (ItemConstants.isEquip(getItemId())) {
            item = ItemData.getInstance().getEquipFromTemplate(getItemId());
        } else {
            item = ItemData.getInstance().getItemFromTemplate(getItemId());
            item.setQuantity(getQuantity());
        }
        chr.addItemToInventory(item);
    }

    @Override
    public void load(QuestActData actData) {
        if (actData instanceof QuestItemActData) {
            setItemId(((QuestItemActData) actData).getItemId());
            setPotentialGrade(((QuestItemActData) actData).getPotentialGrade());
            setGender(((QuestItemActData) actData).getGender());
            setProp(((QuestItemActData) actData).getProp());
            setQuantity(((QuestItemActData) actData).getQuantity());
            jobs.addAll(((QuestItemActData) actData).getJobReqs());
        }
    }
}

