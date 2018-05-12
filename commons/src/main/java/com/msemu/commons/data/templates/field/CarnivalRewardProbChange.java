package com.msemu.commons.data.templates.field;

import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Weber on 2018/5/12.
 */
@Getter
@Setter
public class CarnivalRewardProbChange implements DatSerializable {
    private float winCoin, winRecovery, winNuff, winCP;
    private float loseCoin, loseRecovery, loseNuff, loseCP;

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeFloat(winCoin);
        dos.writeFloat(winRecovery);
        dos.writeFloat(winNuff);
        dos.writeFloat(winCP);
        dos.writeFloat(loseCoin);
        dos.writeFloat(loseRecovery);
        dos.writeFloat(loseNuff);
        dos.writeFloat(loseCP);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setWinCoin(dis.readFloat());
        setWinRecovery(dis.readFloat());
        setWinNuff(dis.readFloat());
        setWinCP(dis.readFloat());
        setLoseCoin(dis.readFloat());
        setLoseRecovery(dis.readFloat());
        setLoseNuff(dis.readFloat());
        setLoseCP(dis.readFloat());
        return this;
    }
}
