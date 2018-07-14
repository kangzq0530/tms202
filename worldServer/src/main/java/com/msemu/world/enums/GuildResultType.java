package com.msemu.world.enums;

/**
 * Created by Weber on 2018/4/14.
 */
public enum GuildResultType {
    ReqLoadGuild(0x0),
    ReqFindGuildByCid(0x1),
    ReqFindGuildByGID(0x2),
    ReqInputGuildName(0x3),
    ReqCheckGuildName(0x4),
    ReqCreateGuildAgree(0x5),
    ReqCreateNewGuild(0x6),
    ReqInviteGuild(0x7),
    ReqJoinGuild(0x8),
    ReqJoinGuildDirect(0x9),
    ReqUpdateJoinState(0xA),
    ReqWithdrawGuild(0xB),
    ReqKickGuild(0xC),
    ReqRemoveGuild(0xD),
    ReqIncMaxMemberNum(0xE),
    ReqChangeLevel(0xF),
    ReqChangeJob(0x10),
    ReqSetGuildName(0x11),
    ReqSetGradeName(0x12),
    ReqSetMemberGrade(0x13),
    ReqSetMark(0x14),
    ReqSetNotice(0x15),
    ReqInputMark(0x16),
    ReqCheckQuestWaiting(0x17),
    ReqCheckQuestWaiting2(0x18),
    ReqInsertQuestWaiting(0x19),
    ReqCancelQuestWaiting(0x1A),
    ReqRemoveQuestCompleteGuild(0x1B),
    ReqIncPoint(0x1C),
    ReqIncCommitment(0x1D),
    ReqDecGGP(0x1E),
    ReqDecIGP(0x1F),
    ReqSetQuestTime(0x20),
    ReqShowGuildRanking(0x21),
    ReqSetSkill(0x22),
    ReqSkillLevelSetUp(0x23),
    ReqResetGuildBattleSkill(0x24),
    ReqUseActiveSkill(0x25),
    ReqUseADGuildSkill(0x26),
    ReqExtendSkill(0x27),
    ReqChangeGuildMaster(0x28),
    ReqFromGuildMember_GuildSkillUse(0x29),
    ReqSetGGP(0x2A),
    ReqSetIGP(0x2B),
    ReqBattleSkillOpen(0x2C),
    ReqSearch(0x2D),
    ReqCreateNewGuild_Block(0x2E),
    ReqCreateNewAlliance_Block(0x2F),
    //    ReqChatN_FindGuildIDByCID [-] BY 198
    ResLoadGuildDone(0x30),
    ResFindGuild_Done(0x31),
    ResCheckGuildName_Available(0x32),
    ResCheckGuildName_AlreadyUsed(0x33),  // 這名稱已經有人使用！請您重新輸入…
    ResCheckGuildName_Unknown(0x34),
    ResCreateGuildAgree_Reply(0x35),
    ResCreateGuildAgree_Unknown(0x36),    // 接收同意時發生問題！請您再試一次。
    ResCreateNewGuild_Done(0x37),
    ResCreateNewGuild_AlreadyJoined(0x38),  // 已是加入公會的狀態。
    ResCreateNewGuild_GuildNameAlreadyExist(0x39),
    ResCreateNewGuild_Beginner(0x3A), // 等級不足！無法創立公會。
    ResCreateNewGuild_Disagree(0x3B),   // 看來有人不同意公會的創立哦！建議您重新尋找與您志同道合的夥伴後，再來找我吧！需要所有人都同意，才能創立公會喔！
    ResCreateNewGuild_NotFullParty(0x3C),
    ResCreateNewGuild_Unknown(0x3D),
    ResJoinGuild_Done(0x3E),    // 已加入公會！

    ResJoinGuild_AlreadyJoined(0x3F), // 已是加入公會的狀態。
    ResJoinGuild_AlreadyFull(0x41), // 您要加入的公會人數已達上限！無法再加入該公會。
    ResJoinGuild_AlreadyFull2(0x42),
    ResJoinGuild_UnknownUser(0x43),
    ResJoinGuild_NonRequestFindUser(0x44),
    ResJoinGuild_Unknown(0x45),
    ResJoinRequest_Done(0x46),
    ResJoinRequest_DoneToUser(0x47),
    ResJoinRequest_AlreadyFull(0x48),
    ResJoinRequest_LimitTime(0x49),
    ResJoinRequest_Unknown(0x4A),
    ResJoinCancelRequest_Done(0x4B),
    ResWithdrawGuild_Done(0x4C),
    ResWithdrawGuild_NotJoined(0x4D),
    ResWithdrawGuild_Unknown(0x4E),
    ResKickGuild_Done(0x4F),
    ResKickGuild_NotJoined(0x50),
    ResKickGuild_Unknown(0x51),
    ResRemoveGuild_Done(0x52),
    ResRemoveGuild_NotExist(0x53),
    ResRemoveGuild_Unknown(0x54),
    ResRemoveRequestGuild_Done(0x55),
    ResInviteGuild_BlockedUser(0x56),
    ResInviteGuild_AlreadyInvited(0x57),
    ResInviteGuild_Rejected(0x58),
    ResAdminCannotCreate(0x59),
    ResAdminCannotInvite(0x5A),
    ResIncMaxMemberNum_Done(0x5B),
    ResIncMaxMemberNum_Unknown(0x5C),
    ResChangeMemberName(0x5D),
    ResChangeRequestUserName(0x5E),
    ResChangeLevelOrJob(0x5F),
    ResNotifyLoginOrLogout(0x50),
    ResSetGradeName_Done(0x61),
    ResSetGradeName_Unknown(0x62),
    ResSetMemberGrade_Done(0x63),
    ResSetMemberGrade_Unknown(0x64),
    ResSetMemberCommitment_Done(0x65),
    ResSetMark_Done(0x66),
    ResSetMark_Unknown(0x67),
    ResSetNotice_Done(0x68),
    ResInsertQuest(0x69),
    ResNoticeQuestWaitingOrder(0x6A),
    ResSetGuildCanEnterQuest(0x6B),
    ResIncPoint_Done(0x6C),
    ResShowGuildRanking(0x6D),
    ResSetGGP_Done(0x6E),
    ResSetIGP_Done(0x6F),
    ResGuildQuest_NotEnoughUser(0x70),
    ResGuildQuest_RegisterDisconnected(0x71),
    ResGuildQuest_NoticeOrder(0x72),
    ResAuthkey_Update(0x73),
    ResSetSkill_Done(0x74),
    ResSetSkill_Extend_Unknown(0x75),
    ResSetSkill_LevelSet_Unknown(0x76),
    ResSetSkill_ResetBattleSkill(0x77),
    ResUseSkill_Success(0x78),
    ResUseSkill_Err(0x79),
    ResChangeName_Done(0x7A),
    ResChangeName_Unknown(0x7B),
    ResChangeMaster_Done(0x7C),
    ResChangeMaster_Unknown(0x7D),
    ResBlockedBehaviorCreate(0x7E),
    ResBlockedBehaviorJoin(0x7F),
    ResBattleSkillOpen(0x80),
    ResGetData(0x81),
    ResRank_Reflash(0x82),
    ResFindGuild_Error(0x83),
    ResChangeMaster_Pinkbean(0x84),

    NONE(0xFF);

    private byte value;

    GuildResultType(int val) {
        this.value = (byte) val;
    }

    public byte getValue() {
        return value;
    }

    public static GuildResultType getByValue(int value) {
        for (GuildResultType type : values()) {
            if (type.value == value)
                return type;
        }
        return NONE;
    }
}
