/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.mob.LP_MobCtrlAck;
import com.msemu.core.network.packets.outpacket.mob.LP_MobMove;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;
import com.msemu.world.client.field.lifes.Mob;
import com.msemu.world.client.field.lifes.movement.IMovement;
import com.msemu.world.client.field.lifes.movement.MovementBase;
import com.msemu.world.client.field.lifes.skills.MobSkillAttackInfo;

import java.util.List;

/**
 * Created by Weber on 2018/5/2.
 */
public class CP_MobMove extends InPacket<GameClient> {

    Position mPos, oPos;
    List<IMovement> movements;
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
    private Mob mob;

    public CP_MobMove(short opcode) {
        super(opcode);
    }

    @Override
    public void read() {
        final GameClient client = getClient();
        final Character chr = client.getCharacter();
        final Field field = chr.getField();
        if(field == null)
            return;
        mobObjectID = decodeInt();
        mob = field.getMobByObjectId(mobObjectID);
        if (mob == null)
            return;
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
        if (mob == null)
            return;
        Character chr = getClient().getCharacter();
        Field field = chr.getField();
        Character controller = mob.getController();

        if (controller != null && controller.equals(chr)) {
            mob.move(movements);
            if (!movements.isEmpty()) {
                getClient().write(new LP_MobCtrlAck(mob, true, moveId, mobSkillID, (byte) mobSkillLevel, 0));
                field.broadcastPacket(new LP_MobMove(mob, msai, movements), controller);
            }
        } else if (controller == null) {
            mob.setController(chr);
            mob.setControllerLevel(2);
        }
    }
}
