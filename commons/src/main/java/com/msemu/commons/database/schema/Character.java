package com.msemu.commons.database.schema;

import com.msemu.commons.database.Schema;
import com.msemu.commons.network.netty.NettyClient;

import javax.persistence.*;
import java.beans.Transient;

/**
 * Created by Weber on 2018/3/29.
 */
@Schema
@Entity
@Table(name = "characters")
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "accId")
    private int accId;

    public Character() {

    }
}

