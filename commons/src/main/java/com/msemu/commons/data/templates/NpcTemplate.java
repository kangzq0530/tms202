package com.msemu.commons.data.templates;

import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/4/22.
 */
@Getter
@Setter
public class NpcTemplate implements DatSerializable {

    private int id;

    private String name = "";

    private List<String> scripts = new ArrayList<>();

    @Override
    public String toString() {
        return String.format("[NPC] %s(%d) Scripts: %s", getName(), getId(), getScripts().stream().collect(Collectors.joining(", ")));
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(this.id);
        dos.writeUTF(this.name);
        dos.writeInt(this.scripts.size());
        for (String script : scripts) {
            dos.writeUTF(script);
        }
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        this.setId(dis.readInt());
        this.setName(dis.readUTF());
        int scriptSize = dis.readInt();
        for (int i = 0; i < scriptSize; i++)
            getScripts().add(dis.readUTF());
        return this;
    }
}
