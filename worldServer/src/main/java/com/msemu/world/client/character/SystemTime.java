package com.msemu.world.client.character;

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
@Getter
@Setter
public class SystemTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "year")
    private int year;
    @Column(name = "month")
    private int month;

    public SystemTime() {
    }

    public SystemTime(int year, int month) {
        this.year = year;
        this.month = month;
    }
}
