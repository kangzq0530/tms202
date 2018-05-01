package com.msemu.world.client.character;


import com.msemu.commons.database.Schema;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.GameClient;
import com.msemu.world.constants.ItemConstants;
import com.msemu.world.constants.MapleJob;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Weber on 2018/3/29.
 */
@Schema
@Entity
@Table(name = "avatarlook")
@Getter
@Setter
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

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeByte(getGender());
        outPacket.encodeByte(getSkin());
        outPacket.encodeInt(getFace());
        outPacket.encodeInt(getJob());
        outPacket.encodeByte(isZeroBetaLook());
        outPacket.encodeInt(getHair());

        for (int i = 1; i < getHairEquips().size(); i++) {
            int itemId = getHairEquips().get(i);
            outPacket.encodeByte(ItemConstants.getBodyPartFromItem(itemId, getGender())); // body part
            outPacket.encodeInt(itemId); // item id
        }
        outPacket.encodeByte(-1);
        for (int i = 0; i < getUnseenEquips().size(); i++) {
            int itemId = getUnseenEquips().get(i);
            outPacket.encodeByte(ItemConstants.getBodyPartFromItem(itemId, getGender())); // body part
            outPacket.encodeInt(itemId);
        }
        outPacket.encodeByte(-1);
        for (int i = 0; i < getTotems().size(); i++) {
            int itemId = getTotems().get(i);
            outPacket.encodeByte(ItemConstants.getBodyPartFromItem(itemId, getGender()));
            outPacket.encodeInt(itemId);
        }
        outPacket.encodeByte(-1);
        outPacket.encodeInt(getWeaponStickerId());
        outPacket.encodeInt(getWeaponId());
        outPacket.encodeInt(getSubWeaponId());
        outPacket.encodeByte(isDrawElfEar());

        outPacket.encodeByte(0);

        getPetIDs().forEach(outPacket::encodeInt);

        if (MapleJob.is惡魔(getJob())) {
            outPacket.encodeInt(getDemonSlayerDefFaceAcc());
        } else if (MapleJob.is傑諾(getJob())) {
            outPacket.encodeInt(getXenonDefFaceAcc());
        } else if (MapleJob.is神之子(getJob())) {
            outPacket.encodeByte(isZeroBetaLook());
        } else if (MapleJob.is幻獸師(getJob())) {
            boolean hasEars = getEars() > 0;
            boolean hasTail = getTail() > 0;
            outPacket.encodeInt(getBeastTamerDefFaceAcc());
            outPacket.encodeByte(hasEars);
            outPacket.encodeInt(getEars());
            outPacket.encodeByte(hasTail);
            outPacket.encodeInt(getTail());
        }
        outPacket.encodeByte(getMixedHairColor());
        outPacket.encodeByte(getMixHairPercent());
        outPacket.encodeZeroBytes(5); // 186+ unknown
    }
}
