package com.msemu.world.client.character;

import com.msemu.commons.database.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Weber on 2018/3/31.
 */
@Schema
@Entity
@Table(name = "keymaps")
@Getter
@Setter
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
}
