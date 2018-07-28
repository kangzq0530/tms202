/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.msemu.login.client.character;

import com.msemu.commons.database.Schema;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.core.network.LoginClient;
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

    public void encode(OutPacket<LoginClient> outPacket) {
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
