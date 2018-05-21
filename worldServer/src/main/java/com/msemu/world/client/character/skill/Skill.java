package com.msemu.world.client.character.skill;

import com.msemu.commons.database.Schema;
import com.msemu.commons.utils.types.FileTime;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Weber on 2018/4/13.
 */
@Schema
@Entity
@Table(name = "skills")
@Getter
@Setter
public class Skill {

    @JoinColumn(name = "dateExpire")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    protected FileTime dateExpire = FileTime.getFileTimeFromType(FileTime.Type.PERMANENT);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "charId")
    private int charId;
    @Column(name = "skillId")
    private int skillId;
    @Column(name = "rootId")
    private int rootId;
    @Column(name = "maxLevel")
    private int maxLevel;
    @Column(name = "currentLevel")
    private int currentLevel;
    @Column(name = "masterLevel")
    private int masterLevel;

    @Override
    public String toString() {
        return "id = " + getSkillId() + ", cur = " + getCurrentLevel() + ", master = " + getMasterLevel();
    }
}