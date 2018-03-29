package com.msemu.commons.database.schema;

import com.msemu.commons.database.Schema;

import javax.persistence.*;

/**
 * Created by Weber on 2018/3/29.
 */
@Schema
@Entity
@Table(name = "characterstats")
public class CharacterStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "characterId")
    private int characterId;

}
