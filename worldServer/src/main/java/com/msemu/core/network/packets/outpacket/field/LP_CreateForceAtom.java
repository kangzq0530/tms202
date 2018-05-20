package com.msemu.core.network.packets.outpacket.field;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.commons.utils.types.Rect;
import com.msemu.core.network.GameClient;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.forceatoms.types.ForceAtomInfo;
import com.msemu.world.enums.ForceAtomType;

import java.util.List;

/**
 * Created by Weber on 2018/5/12.
 */
public class LP_CreateForceAtom extends OutPacket<GameClient> {


    public LP_CreateForceAtom(boolean byMob, Character chr, int targetOD, boolean toMob
            , ForceAtomType type, List<Integer> targetMob, int target, List<ForceAtomInfo> atoms, Position forcedTarget, Rect rcStart, int bulletItemID, int arriveDir, int arriveRange) {
        super(OutHeader.LP_CreateForceAtom);
        encodeByte(byMob);
        if (byMob) {
            encodeInt(chr.getId());
        }
        encodeInt(targetOD);
        encodeInt(type.getValue());


        switch (type) {
            case DEMON_FORCE_LOCAL:
            case ZERO_FORCE_LOCAL:
            case EVENT_POINT_LOCAL:
            case TYPE_1D:
            case TYPE_23:
                break;
            default: {
                encodeByte(toMob);
                switch (type) {
                    case FLYING_SWORD_BOTH:
                    case SOUL_SEEKER_BOTH:
                    case AEGIS_ACTIVE_BOTH:
                    case TRIFLING_WHIM_BOTH:
                    case MARK_OF_ASSASSIN_BOTH:
                    case MESO_EXPLOSION_BOTH:
                    case POSSESSION_BOTH:
                    case NON_TARGET_BOTH:
                    case SSF_SHOOTING_BOTH:
                    case HORMING:
                    case MAGIC_WRECKAGE:
                    case ADV_MAGIC_WRECKAGE:
                    case AUTO_SOUL_SEEKER_BOTH:
                    case TYPE_1B:
                    case TYPE_1C:
                    case TYPE_1E:
                    case TYPE_20:
                    case TYPE_22: {
                        encodeInt(targetMob.size());
                        targetMob.forEach(this::encodeInt);
                    }
                    default:
                        encodeInt(targetMob.get(0));
                }
                encodeInt(target);
            }

        }

        atoms.forEach(atom -> atom.encode(this));

        for (ForceAtomInfo fi : atoms) {

        }

        encodeByte(0);

        switch (type) {
            case MARK_OF_ASSASSIN_BOTH:
                encodeRect(rcStart);
                encodeInt(bulletItemID);
                break;
            case ZERO_FORCE_LOCAL:
            case SHADOW_BAT_BOTH:
                encodeRect(rcStart);
                break;
            case TYPE_1D:
                encodeRect(rcStart);
                encodeInt(forcedTarget.getX());
                encodeInt(forcedTarget.getY());
                break;
            case SHADOW_BAT_BOUND_BOTH:
                encodeInt(rcStart.getLeft());
                encodeInt(rcStart.getTop());
                break;
            case NON_TARGET_BOTH:
                encodeInt(arriveDir);
                encodeInt(arriveRange);
                break;
            case TYPING_GAME_BOTH:
                encodeInt(forcedTarget.getX());
                encodeInt(forcedTarget.getY());
                break;
            case TYPE_1B:
                // TODO decodeBuffer 0x10 / decode 4
                break;
            case TYPE_1C:
            case TYPE_22:
                // TODO decodeBuffer 0x10 / decode 4
                break;
        }

    }
}
