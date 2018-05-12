package com.msemu.world.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/5/12.
 */
public enum TransferPvpReqIgnoredType {

    CANT_ENTER_CHAOS_SERVER_NOT(1), // 現在無法進入大亂鬥伺服器，請稍後再試。
    FIND_PARTY_MEMBER_INFO_FAIL(2), // 在頻道裡，尋找隊員情報失敗。
    ONLY_FOR_PARTY_BOSS(3),   // 只有隊長可以。
    PARTY_MEMBER_CANT_REVIVE(4), //  不能復活的隊員。
    PARTY_MEMBER_IN_OTHER_MAPS(5), // 有在其他地方的隊員。
    ONLY_USED_IN_CHAOS_SERVER(6), // 只有在大亂鬥伺服器裡可以使用。
    CANT_MOVE_BECAUSE_OF_LEVEL_NOT_MATCH(7), // 因沒有符合頻道立場條件的等級無法移動
    THIS_MODE_IS_ONLY_FOR_PARTY(8), // 此為組隊的型態無法進場的模式。

    ;
    @Getter
    private int value;
    TransferPvpReqIgnoredType(int value) {
        this.value = value;
    }
}
