package com.msemu.login.client.character;

import com.msemu.commons.database.Schema;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Weber on 2018/4/14.
 */
@Schema
@MappedSuperclass
@Table(name = "characters")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "accId")
    private int accId;

    @JoinColumn(name = "avatarData")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private AvatarData avatarData;


    // TODO:
    @Transient
    private Ranking ranking = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccId() {
        return accId;
    }

    public void setAccId(int accId) {
        this.accId = accId;
    }

    public AvatarData getAvatarData() {
        return avatarData;
    }

    public void setAvatarData(AvatarData avatarData) {
        this.avatarData = avatarData;
    }

    public Ranking getRanking() {
        return ranking;
    }

    public void setRanking(Ranking ranking) {
        this.ranking = ranking;
    }
}
