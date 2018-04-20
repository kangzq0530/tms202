package com.msemu.login.client.character;

import com.msemu.commons.database.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Weber on 2018/4/14.
 */
@Schema
@Entity
@Table(name = "characters")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private int id;

    @Column(name = "accId")
    @Getter
    @Setter
    private int accId;

    @JoinColumn(name = "avatarData")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private AvatarData avatarData;

    @Getter
    @Setter
    @Column(name = "characterPos")
    private int characterPos;


    // TODO:
    @Transient
    @Getter
    @Setter
    private Ranking ranking = null;

    public Character(int accId, String name, int keySettingType, int eventNewCharSaleJob, int job, short curSelectedSubJob,
                byte gender, byte skin, int[] items) {

        this.accId = accId;
        avatarData = new AvatarData();
        avatarData.setAvatarLook(new AvatarLook());
        AvatarLook avatarLook = avatarData.getAvatarLook();
        avatarLook.setGender(gender);
        avatarLook.setSkin(skin);
        avatarLook.setFace(items.length > 0 ? items[0] : 0);
        avatarLook.setHair(items.length > 1 ? items[1] : 0);

    }

}
