package com.msemu.login.client.character;

import com.msemu.commons.database.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Weber on 2018/4/14.
 */
@Schema
@Entity
@Table(name = "characters")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private int id;

    @Column(name = "accId")
    @Getter
    @Setter
    private int accId;

    @JoinColumn(name = "avatarData")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private AvatarData avatarData;


    // TODO:
    @Transient
    @Getter
    @Setter
    private Ranking ranking = null;

}
