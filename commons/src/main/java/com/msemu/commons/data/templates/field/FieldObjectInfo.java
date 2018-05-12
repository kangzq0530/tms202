package com.msemu.commons.data.templates.field;

import com.msemu.commons.data.loader.dat.DatSerializable;
import com.msemu.commons.utils.types.Tuple;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Weber on 2018/5/12.
 */
@Setter
@Getter
public class FieldObjectInfo implements DatSerializable {
    private String os = "", l0 = "", l1 = "", l2 = "", l3 = "", name = "", tags = "";
    private int x, y, z, f, rx, ry, cx, cy;
    private int zM, r, flow, force;
    private int piece, start, speed, repeat, x1, y1, x2, y2, yOffset;
    private int reactor;
    private boolean move, dynamic, forbidFallDown, cantThrough;
    private List<Integer> sns = new ArrayList<>();
    private Set<Tuple<Integer, Integer>> quests = new HashSet<>();

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(os);
        dos.writeUTF(l0);
        dos.writeUTF(l1);
        dos.writeUTF(l2);
        dos.writeUTF(l3);
        dos.writeUTF(name);
        dos.writeUTF(tags);
        dos.writeInt(x);
        dos.writeInt(y);
        dos.writeInt(z);
        dos.writeInt(f);
        dos.writeInt(zM);
        dos.writeInt(r);
        dos.writeInt(rx);
        dos.writeInt(ry);
        dos.writeInt(cx);
        dos.writeInt(cy);
        dos.writeInt(yOffset);
        dos.writeInt(flow);
        dos.writeInt(force);
        dos.writeBoolean(move);
        dos.writeBoolean(dynamic);
        dos.writeBoolean(forbidFallDown);
        dos.writeBoolean(cantThrough);
        dos.writeInt(piece);
        dos.writeInt(start);
        dos.writeInt(speed);
        dos.writeInt(repeat);
        dos.writeInt(reactor);
        dos.writeInt(quests.size());
        for(Tuple<Integer, Integer> pair : quests) {
            dos.writeInt(pair.getLeft());
            dos.writeInt(pair.getRight());
        }
        dos.writeInt(x1);
        dos.writeInt(y1);
        dos.writeInt(x2);
        dos.writeInt(y2);
        dos.writeInt(sns.size());
        for(Integer sn : sns) {
            dos.writeInt(sn);
        }

    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setOs(dis.readUTF());
        setL0((dis.readUTF()));
        setL1(dis.readUTF());
        setL2(dis.readUTF());
        setL3(dis.readUTF());
        setName(dis.readUTF());
        setTags(dis.readUTF());
        setX(dis.readInt());
        setY(dis.readInt());
        setZ(dis.readInt());
        setF(dis.readInt());
        setZM(dis.readInt());
        setR(dis.readInt());
        setRx(dis.readInt());
        setRy(dis.readInt());
        setCx(dis.readInt());
        setCy(dis.readInt());
        setYOffset(dis.readInt());
        setFlow(dis.readInt());
        setForce(dis.readInt());
        setMove(dis.readBoolean());
        setDynamic(dis.readBoolean());
        setForbidFallDown(dis.readBoolean());
        setCantThrough(dis.readBoolean());
        setPiece(dis.readInt());
        setStart(dis.readInt());
        setSpeed(dis.readInt());
        setRepeat(dis.readInt());
        setReactor(dis.readInt());
        int qSize = dis.readInt();
        for(int i = 0 ; i < qSize; i++) {
            Tuple<Integer, Integer> tup = new Tuple<>(dis.readInt(), dis.readInt());
            getQuests().add(tup);
        }
        setX1(dis.readInt());
        setY1(dis.readInt());
        setX2(dis.readInt());
        setY2(dis.readInt());
        int size = dis.readInt();
        for(int i = 0 ; i < size; i++)
            getSns().add(dis.readInt());
        return this;
    }
}
