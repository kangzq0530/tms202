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

public class NpcTemplate implements DatSerializable {
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private String name = "";
    @Getter
    @Setter
    private String script = "";

    @Override
    public String toString() {
        return String.format("[NPC] %s(%d) Scripts: %s", getName(), getId(), getScript());
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(this.id);
        dos.writeUTF(this.name);
        dos.writeUTF(this.script);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        this.setId(dis.readInt());
        this.setName(dis.readUTF());
        this.setScript(dis.readUTF());
        return this;
    }
}
