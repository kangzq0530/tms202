package com.msemu.world.enums;

import lombok.Getter;

/**
 * Created by Weber on 2018/4/28.
 */
public enum NpcMessageType {
    NM_SAY_OK(0, false, false),
    NM_SAY_NEXT(0, false, true),
    NM_SAT_PREV(0, true, false),
    NM_SAY(0, true, true),
    NM_UNK_1(1),
    NM_SAY_IMAGE(2),
    NM_ASK_YES_NO(3),
    NM_ASK_TEXT(4),
    NM_ASK_NUMBER(5),
    NM_ASK_MENU(6),
    NM_UNK_7(7),
    NM_ASK_QUIZ(8),
    NM_ASK_SPEED_QUIZ(9),
    NM_ASK_ICQ_QUIZ(10),
    NM_ASK_AVATAREX(11),
    NM_ASK_ANDROID(12),
    NM_ASK_PET(13),
    NM_ASK_PET_ALL(14),
    NM_ASK_ACTION_PET_EVOLUTION(15),
    NM_SCRIPT(16),
    NM_ASK_ACCEPT(17),
    NM_UNK_18(18),
    NM_ASK_BOX_TEXT(19),
    NM_ASK_SLIDE_MENU(20),
    NM_ASK_GAME_DIRECTION_EVENT(21),
    NM_PLAY_MOVIE_CLIP(22),
    NM_PLAY_MOVIE_CLIP_URL(23),
    NM_ACK_CENTER(24),
    NM_UNK_25(25),
    NM_UNK_26(26),
    NM_ASK_SELECT_MENU(27),
    NM_ASK_ANGELIC_BUSTER(28),
    NM_SAY_ILLUSTRATION(29),
    NM_SAY_DUAL_ILLUSTRATION(30),
    NM_ASK_YES_NO_ILLUSTRATION(31),
    NM_ASK_ACCEPT_ILLUSTRATION(32),
    NM_ASK_MENU_ILLUSTRATION(33),
    NM_ASK_YES_NO_DUAL_ILLUSTRATION(34),
    NM_ASK_ACCEPT_DUAL_ILLUSTRATION(35),
    NM_ASK_MENU_DUAL_ILLUSTRATION(36),
    NM_ASK_SSN2(37),
    NM_ASK_AVATAR_EX_ZERO(38),
    NM_MONOLOGUE(39),
    NM_ASK_WEAPON_BOX(40),
    NM_ASK_BOX_TEXT_BGIMG(41),
    NM_ASK_USER_SURVEY(42),
    NM_SUCCESS_CAMERA(43),
    NM_ASK_MIX_HAIR(44),
    NM_ASK_MIX_HAIR_EX_ZERO(45),
    NM_ASK_CUSTOM_MIX_HAIR(46),
    NM_ASK_CUSTOM_MIX_HAIR_AND_PROB(47),
    NM_ASK_MIX_HAIR_NEW(48),
    NM_ASK_MIX_HAIR_NEW_EX_ZERO(49),
    NM_NPC_ACTION(50),
    NM_ASK_SCREEN_SHINING_START_MSG(51),
    NM_INPUT_UI(52),
    NM_UNK_53(53),
    NM_ASK_NUMBER_KEYPAD(54),
    NM_ASK_SPIN_OFF_GUITAR_RHYTHM_GAME(55),
    NM_ASK_GHOST_PARTK_ENTER_UI(56),
    NM_UNK_57(57),
    NM_UNK_58(58),
    NM_UNK_59(59),
    NM_UNK_60(60),
    NM_UNK_61(61),
    NM_UNK_62(62),
    NM_UNK_63(63),
    NM_UNK_64(64),
    NM_UNK_65(65),
    NM_UNK_66(66),
    NM_UNK_67(67),
    NM_UNK_68(68),
    NM_UNK_69(69),
    NM_UNK_70(70),;

    @Getter
    private byte value;
    @Getter
    private boolean prev, next;
    @Getter
    private int delay;

    NpcMessageType(int value) {
        this(value, false, false);
    }

    NpcMessageType(int value, boolean prev, boolean next) {
        this.value = (byte)value;
        this.prev = prev;
        this.next = next;
        this.delay = 0;
    }
}
