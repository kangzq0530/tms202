package com.msemu.login.client.character;

import com.msemu.commons.database.Schema;
import com.msemu.commons.network.packets.OutPacket;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/3/31.
 */
@Schema
@Entity
@Table(name = "extendsp")
public class ExtendSP {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private int id;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "extendsp_id")
    @Getter
    @Setter
    private List<SPSet> spSet;

    public ExtendSP() {
        this(0);
    }

    public ExtendSP(int subJobs) {
        spSet = new ArrayList<>();
        for(int i = 1; i <= subJobs; i++) {
            spSet.add(new SPSet((byte) i, 0));
        }
    }

    public int getTotalSp() {
        return spSet.stream().mapToInt(SPSet::getSp).sum();
    }


    public void encode(OutPacket outPacket) {
        outPacket.encodeByte(getSpSet().size());
        for(SPSet spSet : getSpSet()) {
            outPacket.encodeByte(spSet.getJobLevel());
            outPacket.encodeInt(spSet.getSp());
        }
    }


    public void setSpToJobLevel(int jobLevel, int sp) {
        SPSet spSet = getSpSet().stream().filter(sps -> sps.getJobLevel() == jobLevel).findFirst().orElse(null);
        if(spSet != null) {
            spSet.setSp(sp);
        }
    }

    public int getSpByJobLevel(byte jobLevel) {
        SPSet spSet = getSpSet().stream().filter(sps -> sps.getJobLevel() == jobLevel).findFirst().orElse(null);
        if(spSet != null) {
            return spSet.getSp();
        }
        return -1;
    }
}
