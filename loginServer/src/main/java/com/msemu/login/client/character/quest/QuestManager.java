package com.msemu.login.client.character.quest;

import com.msemu.commons.database.Schema;
import com.msemu.login.client.character.Character;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weber on 2018/4/21.
 */
@Schema
@Entity
@Table(name = "questManagers")
public class QuestManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private long id;

    @Transient
    private Character character;

    public QuestManager() {
    }

    public QuestManager(Character chr) {
        this.character = chr;
    }

}