package com.msemu.commons.database.schema;

import com.msemu.commons.database.Schema;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Weber on 2018/3/29.
 */
@Schema
@Entity
@Table(name = "avatarlook")
public class AvatarLook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "gender")
    private int gender;
    @Column(name = "skin")
    private int skin;
    @Column(name = "face")
    private int face;
    @Column(name = "hair")
    private int hair;
    @Column(name = "weaponStickerId")
    private int weaponStickerId;
    @Column(name = "weaponId")
    private int weaponId;
    @Column(name = "subWeaponId")
    private int subWeaponId;
    @ElementCollection
    @CollectionTable(name = "hairEquips", joinColumns = @JoinColumn(name = "alId"))
    @Column(name = "equipId")
    private List<Integer> hairEquips;
    @ElementCollection
    @CollectionTable(name = "unseenEquips", joinColumns = @JoinColumn(name = "alId"))
    @Column(name = "equipId")
    private List<Integer> unseenEquips;
    @ElementCollection
    @CollectionTable(name = "petIDs", joinColumns = @JoinColumn(name = "alId"))
    @Column(name = "petId")
    private List<Integer> petIDs;
    @Column(name = "job")
    private int job;
    @Column(name = "drawElfEar")
    private boolean drawElfEar;
    @Column(name = "demonSlayerDefFaceAcc")
    private int demonSlayerDefFaceAcc;
    @Column(name = "xenonDefFaceAcc")
    private int xenonDefFaceAcc;
    @Column(name = "beastTamerDefFaceAcc")
    private int beastTamerDefFaceAcc;
    @Column(name = "isZeroBetaLook")
    private boolean isZeroBetaLook;
    @Column(name = "mixedHairColor")
    private int mixedHairColor;
    @Column(name = "mixHairPercent")
    private int mixHairPercent;
    @ElementCollection
    @CollectionTable(name = "totems", joinColumns = @JoinColumn(name = "alId"))
    @Column(name = "totemId")
    private List<Integer> totems;
    @Column(name = "ears")
    private int ears;
    @Column(name = "tail")
    private int tail;
    @Transient
    private int demonWingID;
    @Transient
    private int kaiserWingID;
    @Transient
    private int kaiserTailID;
}
