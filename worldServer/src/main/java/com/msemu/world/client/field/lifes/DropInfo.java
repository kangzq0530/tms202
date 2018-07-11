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
