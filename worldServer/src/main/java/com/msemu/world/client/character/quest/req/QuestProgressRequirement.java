package com.msemu.world.client.character.quest.req;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Weber on 2018/4/13.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "questProgressRequirements")
@DiscriminatorColumn(name = "progressType")
public abstract class QuestProgressRequirement implements IQuestProgressRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private long id;
}
