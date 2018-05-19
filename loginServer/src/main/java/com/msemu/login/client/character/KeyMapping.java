package com.msemu.login.client.character;

import com.msemu.commons.database.Schema;

import javax.persistence.*;

/**
 * Created by Weber on 2018/4/21.
 */
@Schema
@Entity
@Table(name = "keymaps")
public class KeyMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "idx")
    private int index;
    @Column(name = "type")
    private byte type;
    @Column(name = "val")
    private int val;

    public KeyMapping() {
    }

    public KeyMapping(byte type, int val) {
        this.type = type;
        this.val = val;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

