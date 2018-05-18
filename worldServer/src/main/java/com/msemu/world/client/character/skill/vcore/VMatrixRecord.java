package com.msemu.world.client.character.skill.vcore;

import com.msemu.commons.database.Schema;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Weber on 2018/5/7.
 */
@Getter
@Setter
@Entity
@Schema
@Table(name = "vmatrixRecords")
public class VMatrixRecord {

    @JoinColumn(name = "dateExpire")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    protected FileTime dateExpire = FileTime.getFileTimeFromType(FileTime.Type.PERMANENT);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "charId")
    private int charId;
    @Column(name = "coreId")
    private int coreId;
    @Column(name = "connectSkill1")
    private int connectSkill1;
    @Column(name = "connectSkill2")
    private int connectSkill2;
    private int connectSkill3;
    private int skillLv;
    private int masterLevel;
    @Column(name = "coreState")
    private int coreState;
    @Transient
    private int row, exp;
    @Transient
    private long crc;

    public VMatrixRecord(int iconID, int skillid1, int skillid2, int skillid3, int skillLv, int masterlv, int row, int exp, long crc) {
        this.connectSkill1 = skillid1;
        this.connectSkill2 = skillid2;
        this.connectSkill3 = skillid3;
        this.skillLv = skillLv;
        this.masterLevel = masterlv;
        this.row = row;
        this.exp = exp;
        this.crc = crc;
        this.coreState = VCoreRecordState.INACTIVE;
    }

    public VMatrixRecord(int iconID, int skillID1, int skillID2, int skillID3, int maxLevel) {
        this(iconID, skillID1, skillID2, skillID3, 1, maxLevel, 0, 0, 0);
    }

    public VMatrixRecord() {
        this(0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    public void encode(OutPacket<GameClient> outPacket) {
        outPacket.encodeLong(crc);
        outPacket.encodeInt(coreId);
        outPacket.encodeInt(skillLv);
        outPacket.encodeInt(exp);
        outPacket.encodeInt(coreState);
        outPacket.encodeInt(connectSkill1);
        outPacket.encodeInt(connectSkill2);
        outPacket.encodeInt(connectSkill3);
        dateExpire.encode(outPacket);
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof VMatrixRecord) {
            VMatrixRecord vmr = (VMatrixRecord) o;
            result = vmr.getCoreId() == getCoreId() && vmr.getConnectSkill1() == getConnectSkill1()
                    && vmr.getConnectSkill2() == getConnectSkill2() && vmr.getConnectSkill3() == getConnectSkill3()
                    && vmr.getExp() == getExp() && vmr.getMasterLevel() == getMasterLevel();
        }
        return result;
    }

    public boolean hasSameSkillsAs(VMatrixRecord vmr) {
        return vmr.getConnectSkill1() == getConnectSkill1() && vmr.getConnectSkill2() == getConnectSkill2() && vmr.getConnectSkill3() == getConnectSkill3();
    }

    public boolean canBeUsedFor(VMatrixRecord vmr) {
        return vmr.getConnectSkill1() == getConnectSkill1();
    }

    public boolean isBoostNode() {
        return connectSkill1 != 0 && connectSkill3 != 0;
    }

    public static class VCoreRecordState {
        public static final int DISASSEMBLED = 0;
        public static final int INACTIVE = 1;
        public static final int ACTIVE = 2;
    }


}
