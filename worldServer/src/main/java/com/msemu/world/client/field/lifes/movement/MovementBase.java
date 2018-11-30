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

package com.msemu.world.client.field.lifes.movement;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/5/1.
 */
public abstract class MovementBase implements IMovement {

    private static final Logger log = LoggerFactory.getLogger(MovementBase.class);

    @Setter
    protected byte command;
    @Setter
    protected byte moveAction;
    @Setter
    protected byte forcedStop;
    @Setter
    protected byte stat;
    @Setter
    protected short fh;
    @Setter
    protected short footStart;
    @Getter
    @Setter
    protected short elapse;
    @Setter
    protected Position position;
    @Setter
    protected Position vPosition;
    @Setter
    protected Position offset;

    public static List<IMovement> decode(InPacket<GameClient> inPacket) {
        List<IMovement> res = new ArrayList<>();
        byte size = inPacket.decodeByte();
        for (int i = 0; i < size; i++) {
            byte type = inPacket.decodeByte();
            switch (type) {
                case 0:
                case 8:
                case 15:
                case 17:
                case 19:
                case 67:
                case 68:
                case 69:
                case 70:
                case 71:
                    res.add(new Movement1(inPacket, type));
                    break;
                case 56:
                case 66:
                case 90:
                    res.add(new Movement2(inPacket, type));
                    break;
                case 1:
                case 2:
                case 18:
                case 21:
                case 22:
                case 24:
                case 62:
                case 63:
                case 64:
                case 65:
                    res.add(new Movement3(inPacket, type));
                    break;
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 50:
                case 51:
                case 55:
                case 57:
                case 59:
                case 60:
                case 61:
                case 72:
                case 73:
                case 74:
                case 76:
                case 81:
                case 83:
                case 85:
                case 86:
                case 87:
                case 88:
                    res.add(new Movement4(inPacket, type));
                    break;
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 9:
                case 10:
                case 11:
                case 13:
                case 26:
                case 27:
                case 52:
                case 53:
                case 54:
                case 58:
                case 78:
                case 79:
                case 80:
                case 82:
                case 84:
                case 91:
                    res.add(new Movement5(inPacket, type));
                    break;
                case 14:
                case 16:
                    res.add(new Movement6(inPacket, type));
                    break;
                case 23:
                    res.add(new Movement7(inPacket, type));
                    break;
                case 12:
                    res.add(new Movement8(inPacket, type));
                    break;
                case 75:
                case 77: // in default
                    res.add(new Movement9(inPacket, type));
                    break;
                default:
                    res.add(new Movement4(inPacket, type));
                    break;
            }
        }
        double skip = inPacket.decodeByte();
        skip = Math.ceil(skip / 2.0D);
        inPacket.skip((int) skip);
        if (res.size() != size) {
            log.error("MovementBase error");
        }
        return res;
    }

    @Override
    public byte getCommand() {
        return command;
    }

    @Override
    public byte getMoveAction() {
        return moveAction;
    }

    @Override
    public byte getForcedStop() {
        return forcedStop;
    }

    @Override
    public byte getStat() {
        return stat;
    }

    @Override
    public short getFh() {
        return fh;
    }

    @Override
    public short getFootStart() {
        return footStart;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Position getVPosition() {
        return vPosition;
    }

    @Override
    public Position getOffset() {
        return offset;
    }

    @Override
    public short getDuration() {
        return elapse;
    }
}
