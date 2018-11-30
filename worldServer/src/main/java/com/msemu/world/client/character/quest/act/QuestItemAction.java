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

package com.msemu.world.client.character.quest.act;

import com.msemu.commons.data.templates.quest.actions.QuestActData;
import com.msemu.commons.data.templates.quest.actions.QuestItemActData;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.inventory.items.Item;
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
        Item item = ItemData.getInstance().createItem(getItemId());
        chr.giveItem(item);
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

