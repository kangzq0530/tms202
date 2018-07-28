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

package com.msemu.world.client.field.lifes;

import com.msemu.commons.database.Schema;
import com.msemu.commons.utils.Rand;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Weber on 2018/4/11.
 */
@Schema
@Entity
@Table(name = "dropdata")
@Getter
@Setter
public class DropInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private int id;
    @Column(name = "mobID")
    private int mobID;
    @Column(name = "itemID")
    private int itemID;
    @Column(name = "chance")
    private int chance;
    @Column(name = "questReq")
    private int questReq;
    @Column(name = "maxQuantity")
    private int maxQuantity;
    @Column(name = "minQuantity")
    private int minQuantity;

    public DropInfo() {
    }

    public DropInfo(int itemID, int chance, int questReq) {
        this(itemID, chance, questReq, 1, 1);
    }

    public DropInfo(int itemID, int chance, int questReq, int maxQuantity
            , int minQuantity) {
        this.itemID = itemID;
        this.chance = chance;
        this.questReq = questReq;
        this.maxQuantity = maxQuantity;
        this.minQuantity = minQuantity;
    }

    /**
     * Does an RNG roll to check if this should be dropped.
     *
     * @return Whether or not the drop is successful.
     */
    public boolean willDrop() {
        return Rand.getChance(getChance(), 1000000);
    }

}
