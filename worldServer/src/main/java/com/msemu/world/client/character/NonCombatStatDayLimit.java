package com.msemu.world.client.character;

import com.msemu.commons.database.Schema;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.GameClient;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Weber on 2018/3/31.
 */
@Schema
@Entity
@Table(name = "noncombatstatdaylimit")
@Getter
@Setter
public class NonCombatStatDayLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "charisma")
    private short charisma;
    @Column(name = "charm")
    private short charm;
    @Column(name = "insight")
    private short insight;
    @Column(name = "will")
    private short will;
    @Column(name = "craft")
    private short craft;
    @Column(name = "sense")
    private short sense;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ftLastUpdateCharmByCashPR")
    private FileTime ftLastUpdateCharmByCashPR;
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
        this((short) 0, (short) 0, (byte) 0, (short) 0, (short) 0, (short) 0, (short) 0, FileTime.getFileTimeFromType(FileTime.Type.ZERO_TIME));
    }

    public void encode(OutPacket<GameClient> outPacket) {
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
