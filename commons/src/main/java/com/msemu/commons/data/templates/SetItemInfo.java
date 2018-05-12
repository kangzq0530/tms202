package com.msemu.commons.data.templates;

import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Weber on 2018/5/6.
 */
@Getter
@Setter
public class SetItemInfo implements DatSerializable {
    private int completeCount, setItemID;
    private String setItemName;
    private Map<Integer, SetInfo> effects = new LinkedHashMap<>();
    private List<Integer> itemIDs = new ArrayList<>();

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(setItemName);
        dos.writeInt(completeCount);
        dos.writeInt(setItemID);
        dos.writeInt(effects.size());
        for(Map.Entry<Integer, SetInfo> entry : effects.entrySet()) {
            dos.writeInt(entry.getKey());
            entry.getValue().write(dos);
        }
        dos.writeInt(itemIDs.size());
        for(Integer v : itemIDs) {
            dos.writeInt(v);
        }
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setSetItemName(dis.readUTF());
        setCompleteCount(dis.readInt());
        setSetItemID(dis.readInt());
        int size = dis.readInt();
        for(int i = 0 ; i < size; i++) {
            int key = dis.readInt();
            SetInfo setInfo = new SetInfo();
            setInfo.load(dis);
            getEffects().put(key, setInfo);
        }
        int size2 = dis.readInt();
        for(int i = 0 ; i < size2; i++) {
            getItemIDs().add(dis.readInt());
        }
        return this;
    }
}
