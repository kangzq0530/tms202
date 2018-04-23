package com.msemu.world.client.character.quest.req;

/**
 * Created by Weber on 2018/4/23.
 */
public interface IQuestProgressRequirement extends IQuestRequirement {

    /**
     * Returns whether this progress requirement has been completed by the player.
     *
     * @return Completeness.
     */
    boolean isComplete();
}
