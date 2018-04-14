package com.msemu.world.client.character.quest.rewards;

import com.msemu.commons.data.DatSerializable;
import com.msemu.world.client.character.Character;

/**
 * Created by Weber on 2018/4/13.
 */
public interface IQuestReward extends DatSerializable {
    /**
     * Gives the reward of this QuestReward to a {@link Character}
     * @param chr The Char to give the reward to.
     */
    void giveReward(Character chr);
}

