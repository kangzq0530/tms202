package com.msemu.commons.database.schema;

import com.msemu.commons.database.Schema;

import javax.persistence.*;

/**
 * Created by Weber on 2018/3/29.
 */
@Schema
@Entity
@Table(name = "avatarData")
public class AvatarData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @JoinColumn(name = "characterStat")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private CharacterStat characterStat;
    @JoinColumn(name = "avatarLook")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private AvatarLook avatarLook;
    @JoinColumn(name = "zeroAvatarLook")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private AvatarLook zeroAvatarLook;

}
