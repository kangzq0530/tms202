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

import com.msemu.commons.data.templates.quest.reqs.QuestQuestReqData;
import com.msemu.commons.data.templates.quest.reqs.QuestReqData;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.character.quest.QuestManager;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Weber on 2018/4/13.
 */
public class QuestStartCompletionRequirement implements IQuestStartRequirements {
    private static final Logger log = LoggerFactory.getLogger(QuestStartCompletionRequirement.class);

    @Getter
    @Setter
    private int questID;
    @Getter
    @Setter
    private byte questStatus;

    @Override
    public boolean hasRequirements(Character chr) {
        QuestManager qm = chr.getQuestManager();
        switch (getQuestStatus()) {
            case 0: // Not started
                return !qm.hasQuestInProgress(getQuestID()) && !qm.hasQuestCompleted(getQuestID());
            case 1: // In progress
                return qm.hasQuestInProgress(getQuestID());
            case 2: // Completed
                return qm.hasQuestCompleted(getQuestID());
            default:
                log.error(String.format("Unknown status %d.", getQuestStatus()));
                return true;
        }
    }

    @Override
    public void load(QuestReqData reqData) {
        if (reqData instanceof QuestQuestReqData) {
            setQuestID(((QuestQuestReqData) reqData).getQuestId());
            setQuestStatus(((QuestQuestReqData) reqData).getState());
        }
    }
}

