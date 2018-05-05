package com.msemu.world.client.character;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.commons.utils.types.Rect;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.life.Summon;
import com.msemu.world.constants.SkillConstants;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/12.
 */
@Getter
@Setter
public class AttackInfo {
    public boolean isMeleeAttack = false;
    public boolean isShootAttack = false;
    public boolean isMagicAttack = false;
    public boolean isBodyAttack = false;
    public boolean isDotAttack = false;

    public byte fieldKey;
    public byte hits;
    public int mobCount;
    public int skillId;
    public byte slv;
    public int keyDown;
    public byte idk;
    public boolean left;
    public short attackAction;
    public byte attackActionType;
    public byte idk0;
    public byte attackSpeed;
    public byte reduceCount;
    public int psdTargetPlus;
    public int someId;
    public List<MobAttackInfo> mobAttackInfo = new ArrayList<>();
    public int y;
    public int x;
    public short forcedY;
    public short forcedX;
    public short rcDstRight;
    public short rectRight;
    public int option;
    public int[] mists;
    public short forcedYSh;
    public short forcedXSh;
    public byte force;
    public short delay;
    public short[] shortArr;
    public byte addAttackProc;
    public int crc;
    public int grenadeId;
    public byte zero;
    public int bySummonedID;
    public Position ptTarget = new Position();
    public int finalAttackLastSkillID;
    public byte finalAttackByte;
    public boolean ignorePCounter;
    public int spiritCoreEnhance;
    public Position ptAttackRefPoint = new Position();
    public Position idkPos = new Position();
    public Position pos = new Position();
    public byte fh;
    public Position teleportPt = new Position();
    public short Vx;
    public Position grenadePos;
    public Rect rect;
    public int elemAttr;
    public int areaPAD;
    public int attackCount;
    public int wt;
    public int ar01Mad;
    public Position pos3;
    public Summon summon;
    public int updateTime;
    public int bulletID;
    public short mobMove;
    public boolean isJablin;
    public int bulletCount;
    public Position bodyRelMove;
    public Position keyDownRectMoveXY;
    public int tick;
    public int passiveSLV;
    public int passiveSkillID;
    public byte someMask;
    public byte buckShot;
    public int option3;
    public int buckShotSkillID;
    public int buckShotSlv;
    public byte passiveAddAttackCount;
    public byte showFixedDamage;
    public boolean isDragonAttack;
    public byte mastery;
    public byte actionSpeed;
    public byte byteIdk1;
    public byte byteIdk2;
    public byte byteIdk3;
    public byte byteIdk4;
    public byte byteIdk5;

    public void encode(OutPacket<GameClient> outPacket, Character chr) {
        outPacket.encodeInt(chr.getId());
        outPacket.encodeByte(fieldKey);
        outPacket.encodeByte(mobCount << 4 | hits);
        outPacket.encodeByte(chr.getLevel());
        outPacket.encodeByte(slv);
        if (slv > 0) {
            outPacket.encodeInt(skillId);
        }
        if (SkillConstants.isZeroSkill(skillId)) {
            outPacket.encodeByte(zero);
            if (zero != 0) {
                outPacket.encodePosition(chr.getPosition());
            }
        }
        if (isShootAttack &&
                (SkillConstants.getAdvancedCountHyperSkill(skillId) != 0 ||
                        SkillConstants.getAdvancedAttackCountHyperSkill(skillId) != 0 ||
                        SkillConstants.isShikigamiHauntingSkill(skillId))) {
            outPacket.encodeByte(passiveSLV);
            if (passiveSLV > 0) {
                outPacket.encodeInt(passiveSkillID);
            }
        }
        outPacket.encodeByte(someMask);
        outPacket.encodeByte(buckShot);
        outPacket.encodeInt(option3);
        outPacket.encodeInt(bySummonedID);
        if ((buckShot & 2) != 0) {
            outPacket.encodeInt(buckShotSkillID);
            outPacket.encodeInt(buckShotSlv);
        }
        if ((buckShot & 8) != 0) {
            outPacket.encodeByte(psdTargetPlus);
        }
        byte left = (byte) (this.left ? 1 : 0);
        outPacket.encodeShort((left << 15) | attackAction);
        if (attackAction <= 1713) {
            outPacket.encodeByte(attackActionType);
            outPacket.encodeShort(x);
            outPacket.encodeShort(y);
            outPacket.encodeByte(showFixedDamage);
            outPacket.encodeByte(!isDragonAttack);
            outPacket.encodeByte(actionSpeed);
            outPacket.encodeByte(mastery);
            outPacket.encodeInt(bulletID);
            for (MobAttackInfo mai : mobAttackInfo) {
                outPacket.encodeInt(mai.getObjectID());
                if (mai.getObjectID() > 0) {
                    outPacket.encodeByte(mai.getByteIdk1());
                    outPacket.encodeByte(mai.getByteIdk2());
                    outPacket.encodeByte(mai.getByteIdk3());
                    outPacket.encodeShort(mai.getByteIdk4());
                    if (skillId == 42111002 || skillId == 80011050) {
                        // 紅玉咒印 || 紅玉咒印
                        outPacket.encodeByte(mai.getDamages().length);
                        for (Long dmg : mai.getDamages()) {
                            outPacket.encodeLong(dmg);
                        }
                    } else {
                        for (Long dmg : mai.getDamages()) {
                            outPacket.encodeLong(dmg);
                        }
                        if (SkillConstants.isKinesisPsychicLockSkill(skillId)) {
                            outPacket.encodeInt(mai.getPsychicLockInfo());
                        }
                        if (skillId == 37111005) {
                            outPacket.encodeByte(mai.getRocketRushInfo());
                        }
                    }
                }
            }
            // 核爆術 || 雷霆萬鈞 || 黃泉十字架 || 龍氣息
            if (skillId == 2321001 || skillId == 2221052 || skillId == 11121052 || skillId == 12121054) {
                outPacket.encodeInt(keyDown);
            } else if (SkillConstants.isSuperNovaSkill(skillId) || SkillConstants.isScreenCenterAttackSkill(skillId)
                    || skillId == 101000202  // 暗影降臨(劍氣)
                    || skillId == 101000102  // 進階威力震擊(衝擊波)
                    || skillId == 80001762) { // 解放雷之輪
                outPacket.encodePosition(ptAttackRefPoint);
            }
            if (SkillConstants.isKeydownSkillRectMoveXY(skillId)) {
                outPacket.encodePosition(keyDownRectMoveXY);
            }
            if (skillId == 51121009) {  // 閃光交叉
                outPacket.encodeByte(showFixedDamage);
            }
            if (skillId == 112110003) {  // 隊伍攻擊
                outPacket.encodeInt(0);
            }
            if (skillId == 42100007) {   // 御身消滅
                outPacket.encodeShort(0);
                byte size = 0;
                outPacket.encodeByte(size);
                for (int i = 0; i < size; i++) {
                    outPacket.encodePosition(new Position());
                }
            }
            if (skillId == 21120019 || skillId == 37121052) {
                outPacket.encodePositionInt(teleportPt);
            }
//            if(attackHeader == )
            outPacket.encodeZeroBytes(50);
        }
    }
}
