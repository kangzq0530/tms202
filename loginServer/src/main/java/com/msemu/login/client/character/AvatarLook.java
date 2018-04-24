package com.msemu.login.client.character;


import com.msemu.commons.database.Schema;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.LoginClient;
import com.msemu.login.constants.MapleJob;
import com.msemu.world.constants.ItemConstants;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Getter
    @Setter
    @Column(name = "id")
    private int id;
    @Getter
    @Setter
    @Column(name = "gender")
    private int gender;
    @Getter
    @Setter
    @Column(name = "skin")
    private int skin;
    @Getter
    @Setter
    @Column(name = "face")
    private int face;
    @Getter
    @Setter
    @Column(name = "hair")
    private int hair;
    @Getter
    @Setter
    @Column(name = "weaponStickerId")
    private int weaponStickerId;
    @Getter
    @Setter
    @Column(name = "weaponId")
    private int weaponId;
    @Getter
    @Setter
    @Column(name = "subWeaponId")
    private int subWeaponId;
    @Getter
    @Setter
    @ElementCollection
    @CollectionTable(name = "hairEquips", joinColumns = @JoinColumn(name = "alId"))
    @Column(name = "equipId")
    private List<Integer> hairEquips;
    @Getter
    @Setter
    @ElementCollection
    @CollectionTable(name = "unseenEquips", joinColumns = @JoinColumn(name = "alId"))
    @Column(name = "equipId")
    private List<Integer> unseenEquips;
    @Getter
    @Setter
    @ElementCollection
    @CollectionTable(name = "petIDs", joinColumns = @JoinColumn(name = "alId"))
    @Column(name = "petId")
    private List<Integer> petIDs;
    @Getter
    @Setter
    @Column(name = "job")
    private int job;
    @Getter
    @Setter
    @Column(name = "drawElfEar")
    private boolean drawElfEar;
    @Getter
    @Setter
    @Column(name = "demonSlayerDefFaceAcc")
    private int demonSlayerDefFaceAcc;
    @Getter
    @Setter
    @Column(name = "xenonDefFaceAcc")
    private int xenonDefFaceAcc;
    @Getter
    @Setter
    @Column(name = "beastTamerDefFaceAcc")
    private int beastTamerDefFaceAcc;
    @Getter
    @Setter
    @Column(name = "isZeroBetaLook")
    private boolean isZeroBetaLook;
    @Getter
    @Setter
    @Column(name = "mixedHairColor")
    private int mixedHairColor;
    @Getter
    @Setter
    @Column(name = "mixHairPercent")
    private int mixHairPercent;
    @Getter
    @Setter
    @ElementCollection
    @CollectionTable(name = "totems", joinColumns = @JoinColumn(name = "alId"))
    @Column(name = "totemId")
    private List<Integer> totems;
    @Getter
    @Setter
    @Column(name = "ears")
    private int ears;
    @Getter
    @Setter
    @Column(name = "tail")
    private int tail;

    @Getter
    @Setter
    @Transient
    private int demonWingID;
    @Getter
    @Setter
    @Transient
    private int kaiserWingID;
    @Getter
    @Setter
    @Transient
    private int kaiserTailID;

    public AvatarLook() {
        hairEquips = new ArrayList<>();
        unseenEquips = new ArrayList<>();
        petIDs = Arrays.asList(0, 0, 0);
        totems = new ArrayList<>();
    }

    public AvatarLook deepCopy() {
        AvatarLook res = new AvatarLook();
        res.setGender(getGender());
        res.setSkin(getSkin());
        res.setFace(getFace());
        res.setHair(getHair());
        res.setWeaponStickerId(getWeaponStickerId());
        res.setWeaponId(getWeaponId());
        res.setSubWeaponId(getSubWeaponId());
        List<Integer> resHairEquips = new ArrayList<>(getHairEquips());
        res.setHairEquips(resHairEquips);
        List<Integer> resUnseenEquips = new ArrayList<>(getUnseenEquips());
        res.setUnseenEquips(resUnseenEquips);
        List<Integer> resPetIDs = new ArrayList<>(getPetIDs());
        res.setUnseenEquips(resPetIDs);
        res.setJob(getJob());
        res.setDrawElfEar(isDrawElfEar());
        res.setDemonSlayerDefFaceAcc(getDemonSlayerDefFaceAcc());
        res.setXenonDefFaceAcc(getXenonDefFaceAcc());
        res.setBeastTamerDefFaceAcc(getBeastTamerDefFaceAcc());
        res.setZeroBetaLook(isZeroBetaLook());
        res.setMixedHairColor(getMixedHairColor());
        res.setMixHairPercent(getMixHairPercent());
        List<Integer> resTotems = new ArrayList<>(getTotems());
        res.setTotems(resTotems);
        res.setEars(getEars());
        res.setTail(getTail());
        return res;
    }

    public void encode(OutPacket<LoginClient> outPacket) {
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
