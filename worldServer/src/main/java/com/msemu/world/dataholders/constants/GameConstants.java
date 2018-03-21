package com.msemu.world.dataholders.constants;

/**
 * Created by Weber on 2018/3/21.
 */
public class GameConstants {


    public static int getModifier(int itemId, int up) {
        if (up <= 0) {
            return 0;
        }
        switch (itemId) {
            case 2022459:
            case 2860179:
            case 2860193:
            case 2860207:
                return 130;
            case 2022460:
            case 2022462:
            case 2022730:
                return 150;
            case 2860181:
            case 2860195:
            case 2860209:
                return 200;
        }
        if (itemId / 10000 == 286) { //familiars
            return 150;
        }
        return 200;
    }

}
