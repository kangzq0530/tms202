package com.msemu.world.client.character.quest.requirements;

import com.msemu.commons.data.dat.DatSerializable;

import javax.persistence.*;

/**
 * Created by Weber on 2018/4/13.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "questProgressRequirements")
@DiscriminatorColumn(name = "progressType")
public abstract class QuestProgressRequirement implements DatSerializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Returns whether this progress requirement has been completed by the player.
     * @return Completeness.
     */
    public abstract boolean isComplete();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
