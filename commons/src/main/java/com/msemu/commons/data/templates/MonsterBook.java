package com.msemu.commons.data.templates;

import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Weber on 2018/5/6.
 */
@Getter
@Setter
public class MonsterBook implements DatSerializable {
    private int mobTemplateID;
    private String episode = "";
    private Set<Integer> maps = new HashSet<>();
    private Set<Integer> rewards = new HashSet<>();

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(mobTemplateID);
        dos.writeUTF(episode);
        dos.writeInt(maps.size());
        for(Integer v : maps)
            dos.writeInt(v);
        dos.writeInt(rewards.size());
        for(Integer v : rewards)
            dos.writeInt(v);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setMobTemplateID(dis.readInt());
        setEpisode(dis.readUTF());
        int size = dis.readInt();
        for(int i = 0; i < size; i++)
            maps.add(dis.readInt());
        int size2 = dis.readInt();
        for(int i = 0; i < size2; i++)
            rewards.add(dis.readInt());
        return this;
    }
}
