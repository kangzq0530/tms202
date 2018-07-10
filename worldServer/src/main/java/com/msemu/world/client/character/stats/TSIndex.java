package com.msemu.world.client.character.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Weber on 2018/4/11.
 */
public enum TSIndex {

    EnergyCharged(0),
    DashSpeed(1),
    DashJump(2),
    RideVehicle(3),
    PartyBooster(4),
    GuidedBullet(5),
    Undead(6),
    Undead2(7),
    RideVehicleExpire(8),;

    private final int index;

    TSIndex(int index) {
        this.index = index;
    }

    public static TSIndex getTSEByIndex(int index) {
        return Arrays.stream(TSIndex.values()).filter(tse -> tse.getIndex() == index).findFirst().orElse(null);
    }

    public static CharacterTemporaryStat getCTSFromTwoStatIndex(int index) {
        switch (index) {
            case 0:
                return CharacterTemporaryStat.EnergyCharged;
            case 1:
                return CharacterTemporaryStat.DashSpeed;
            case 2:
                return CharacterTemporaryStat.DashJump;
            case 3:
                return CharacterTemporaryStat.RideVehicle;
            case 4:
                return CharacterTemporaryStat.PartyBooster;
            case 5:
                return CharacterTemporaryStat.GuidedBullet;
            case 6:
                return CharacterTemporaryStat.Undead;
            case 7:
                return CharacterTemporaryStat.Undead;
            case 8:
                return CharacterTemporaryStat.RideVehicleExpire;
            default:
                return null;
        }
    }

    public static TSIndex getTSEFromCTS(CharacterTemporaryStat cts) {
        switch (cts) {
            case EnergyCharged:
                return EnergyCharged;
            case DashJump:
                return DashJump;
            case DashSpeed:
                return DashSpeed;
            case RideVehicle:
                return RideVehicle;
            case PartyBooster:
                return PartyBooster;
            case GuidedBullet:
                return GuidedBullet;
            case Undead:
                return Undead;
            case RideVehicleExpire:
                return RideVehicleExpire;
        }
        return null;
    }

    public static boolean isTwoStat(CharacterTemporaryStat cts) {
        return getTSEFromCTS(cts) != null;
    }

    /**
     * Creates a list of all {@link CharacterTemporaryStat CharacterTemporaryStats} that are a TwoState.
     * Is guaranteed to be sorted by their index.
     *
     * @return
     */
    public static List<CharacterTemporaryStat> getAllCTS() {
        List<CharacterTemporaryStat> characterTemporaryStats = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            characterTemporaryStats.add(getCTSFromTwoStatIndex(i));
        }
        return characterTemporaryStats;
    }

    public int getIndex() {
        return index;
    }
}

