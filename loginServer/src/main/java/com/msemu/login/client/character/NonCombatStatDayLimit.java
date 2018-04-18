package com.msemu.login.client.character;

import com.msemu.commons.database.Schema;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Weber on 2018/3/31.
 */
@Schema
@Entity
@Table(name = "noncombatstatdaylimit")

public class NonCombatStatDayLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private int id;
    @Column(name = "charisma")
    @Getter
    @Setter
    private short charisma;
    @Getter
    @Setter
    @Column(name = "charm")
    private short charm;
    @Getter
    @Setter
    @Column(name = "insight")
    private short insight;
    @Getter
    @Setter
    @Column(name = "will")
    private short will;
    @Getter
    @Setter
    @Column(name = "craft")
    private short craft;
    @Getter
    @Setter
    @Column(name = "sense")
    private short sense;
    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ftLastUpdateCharmByCashPR")
    private FileTime ftLastUpdateCharmByCashPR;
    @Getter
    @Setter
    @Column(name = "charmByCashPR")
    private byte charmByCashPR;

    public NonCombatStatDayLimit(short charisma, short charm, byte charmByCashPR, short insight, short will, short craft, short sense, FileTime ftLastUpdateCharmByCashPR) {
        this.charisma = charisma;
        this.charm = charm;
        this.charmByCashPR = charmByCashPR;
        this.insight = insight;
        this.will = will;
        this.craft = craft;
        this.sense = sense;
        this.ftLastUpdateCharmByCashPR = ftLastUpdateCharmByCashPR;
    }

    public NonCombatStatDayLimit() {
        this((short) 0, (short) 0, (byte) 0, (short) 0, (short) 0, (short) 0, (short) 0, FileTime.getFTFromLong(0));
    }

    public void encode(OutPacket outPacket) {
        outPacket.encodeShort(getCharisma());
        outPacket.encodeShort(getInsight());
        outPacket.encodeShort(getWill());
        outPacket.encodeShort(getCraft());
        outPacket.encodeShort(getSense());
        outPacket.encodeShort(getCharm());
        outPacket.encodeByte(getCharmByCashPR());
        getFtLastUpdateCharmByCashPR().encode(outPacket);
    }

}
