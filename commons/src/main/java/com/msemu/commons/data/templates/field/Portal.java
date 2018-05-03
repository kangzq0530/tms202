package com.msemu.commons.data.templates.field;

import com.msemu.commons.data.enums.PortalType;
import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/4/23.
 */
@Getter
@Setter
public class Portal implements DatSerializable {
    protected PortalType type;
    protected String name = "";
    protected String targetPortalName = "";
    protected String script = "";
    protected int delay;
    protected int id;
    protected int targetMapId = -1;
    protected int x;
    protected int y;
    protected int horizontalImpact;
    protected int verticalImpact;
    protected boolean onlyOnce;
    protected boolean hideTooltip;


    public Portal() {

    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(type.name());
        dos.writeUTF(name);
        dos.writeUTF(script);
        dos.writeUTF(targetPortalName);
        dos.writeInt(id);
        dos.writeInt(targetMapId);
        dos.writeInt(delay);
        dos.writeInt(x);
        dos.writeInt(y);
        dos.writeInt(horizontalImpact);
        dos.writeInt(verticalImpact);
        dos.writeBoolean(onlyOnce);
        dos.writeBoolean(hideTooltip);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setType(PortalType.valueOf(dis.readUTF()));
        setName(dis.readUTF());
        setScript(dis.readUTF());
        setTargetPortalName(dis.readUTF());
        setId(dis.readInt());
        setTargetMapId(dis.readInt());
        setDelay(dis.readInt());
        setX(dis.readInt());
        setY(dis.readInt());
        setHorizontalImpact(dis.readInt());
        setVerticalImpact(dis.readInt());
        setOnlyOnce(dis.readBoolean());
        setHideTooltip(dis.readBoolean());
        return this;
    }

}
