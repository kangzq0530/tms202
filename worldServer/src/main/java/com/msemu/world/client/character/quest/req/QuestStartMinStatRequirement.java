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

package com.msemu.world.client.character.quest.req;

import com.msemu.commons.data.templates.quest.reqs.QuestLevelMinReqData;
import com.msemu.commons.data.templates.quest.reqs.QuestPopReqData;
import com.msemu.commons.data.templates.quest.reqs.QuestReqData;
import com.msemu.world.client.character.Character;
import com.msemu.world.enums.Stat;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestStartMinStatRequirement implements IQuestStartRequirements {

    @Getter
    @Setter
    private Stat stat;

    @Getter
    @Setter
    private short reqAmount;


    @Override
    public boolean hasRequirements(Character chr) {
        return chr.getStat(getStat()) >= getReqAmount();
    }

    @Override
    public void load(QuestReqData reqData) {
        switch (reqData.getType()) {
            case charismaMin:
                setStat(Stat.CHARISMA);
                // TODO
            case craftMin:
                setStat(Stat.CRAFT);
                // TODO
                break;
            case lvmin:
                setStat(Stat.LEVEL);
                setReqAmount((short) ((QuestLevelMinReqData) reqData).getMinLevel());
                break;
            case insightMin:
                setStat(Stat.INSIGHT);
                // TODO
                break;
            case senseMin:
                setStat(Stat.SENSE);
                // TODO
                break;
            case charmMin:
                setStat(Stat.CHARM);
                // TODO
                break;
            case pop:
                setStat(Stat.POP);
                setReqAmount((short) ((QuestPopReqData) reqData).getPop());
                break;
        }
    }
}
