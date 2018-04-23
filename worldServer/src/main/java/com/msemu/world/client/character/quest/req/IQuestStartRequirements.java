package com.msemu.world.client.character.quest.req;

import com.msemu.world.client.character.Character;

/**
 * Created by Weber on 2018/4/13.
 */
public interface IQuestStartRequirements extends IQuestRequirement {
    /**
     * Returns whether or not a given {@link com.msemu.world.client.character.Character} has a requirement.
     *
     * @param chr The Char that should be checked.
     * @return Whether or not the given Char has the req for this requirement.
     */
    boolean hasRequirements(Character chr);
}
