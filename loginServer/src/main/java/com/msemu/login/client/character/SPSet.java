package com.msemu.login.client.character;

import com.msemu.commons.database.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Weber on 2018/3/31.
 */
@Schema
@Entity
@Table(name = "spset")
public class SPSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private int id;
    @Column(name = "jobLevel")
    @Getter
    @Setter
    private byte jobLevel;
    @Column(name = "sp")
    @Getter
    @Setter
    private int sp;

    public SPSet() {
    }

    public SPSet(byte jobLevel, int sp) {
        this.jobLevel = jobLevel;
        this.sp = sp;
    }

}

