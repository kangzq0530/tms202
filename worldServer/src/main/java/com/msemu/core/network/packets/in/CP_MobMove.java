package com.msemu.core.network.packets.in;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.out.mob.LP_MobCtrlAck;
import com.msemu.core.network.packets.out.mob.LP_MobMove;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.life.Mob;
import com.msemu.world.client.life.MobSkillAttackInfo;
import com.msemu.world.client.life.movement.IMovement;
import com.msemu.world.client.life.movement.MovementBase;

import java.util.List;

/**
 * Created by Weber on 2018/5/2.
 */
public class CP_MobMove extends InPacket<GameClient> {

    private int mobObjectID;
    private short moveId;
    private boolean useSkill;
    private byte moveAction;
    private int mobSkillData;

    private int mobSkillID;
    private int mobSkillLevel;
    private int mobSkillDelay;
    private MobSkillAttackInfo msai = new MobSkillAttackInfo();

    private int crc0, crc1, crc2;

    private int encodedGatherDuration;
    Position mPos, oPos;
    List<IMovement> movements;

    private Mob mob;

    public CP_MobMove(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        mobObjectID = decodeInt();
        mob = (Mob) getClient().getCharacter().getField().getLifeByObjectID(mobObjectID);
        moveId = decodeShort();
        useSkill = decodeByte() > 0;
        moveAction = decodeByte();
        mobSkillData = decodeInt();
        msai.setActionAndDir(moveAction);
        msai.setActionAndDirMask((byte) (useSkill ? 1 : 0));
        msai.setTargetInfo(mobSkillData);

        mobSkillID = mobSkillData & 0xFF000000;
        mobSkillLevel = mobSkillData & 0xFF0000;
        mobSkillDelay = mobSkillData & 0xFFFF;

        byte unkData1Size = decodeByte();
        for (int i = 0; i < unkData1Size; i++) {
            msai.getMultiTargetForBalls().add(new Position(decodeShort(), decodeShort()));
        }
        byte unkData2Size = decodeByte();
        for (int i = 0; i < unkData1Size; i++) {
            msai.getRandTimeForAreaAttacks().add(decodeShort());
        }
        decodeByte();
        crc0 = decodeInt();
        crc1 = decodeInt();
        crc2 = decodeInt();
        decodeInt();

        boolean skip = crc0 != 0 && unkData2Size > 0;

        if (mob.getTemplateId() == 9300281 && skip) {
            if (decodeByte() > 10) {
                skip(8);
            }
        } else {
            if (crc0 == 18) {
                skip(2);
            }
            skip(1);
        }

        encodedGatherDuration = decodeInt();
        mPos = decodePosition();
        oPos = decodePosition();
        movements = MovementBase.decode(this);

        msai.setEncodedGatherDuration(encodedGatherDuration);
        msai.setOldPos(mPos);
        msai.setOldVPos(oPos);
    }

    @Override
    public void runImpl() {
        Character chr = getClient().getCharacter();
        Field field = chr.getField();
        Character contoller = field.getLifeController(mob);

        if (contoller != null && contoller.equals(chr)) {
            for (IMovement movement : movements) {
                mob.setPosition(movement.getPosition());
                mob.setMoveAction(movement.getMoveAction());
                mob.setFh(movement.getFh());
            }

            if(movements.size() > 0) {
                getClient().write(new LP_MobCtrlAck(mob, true, moveId, mobSkillID, (byte) mobSkillLevel, 0));
                field.broadcastPacket(new LP_MobMove(mob, msai, movements), contoller);
            }
        }
    }
}
