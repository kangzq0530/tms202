package com.msemu.login.client.character;

import com.msemu.commons.database.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Weber on 2018/4/14.
 */
@Schema
@Entity
@Table(name = "systemtimes")
public class SystemTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    @Column(name = "year")
    private int year;
    @Getter
    @Setter
    @Column(name = "month")
    private int month;

    public SystemTime(){}

    public SystemTime(int year, int month) {
        this.year = year;
        this.month = month;
    }

}
