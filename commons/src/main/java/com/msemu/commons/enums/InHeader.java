/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.msemu.commons.enums;

import com.msemu.commons.network.packets.IHeader;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Weber on 2018/3/29.
 */
public enum InHeader implements IHeader {
    CP_DummyCode(ClientState.values()),
    CP_SocketBegin,
    CP_ClientStart(ClientState.CONNECTED),
    CP_CheckOTPForWebLaunchingRequest(ClientState.CONNECTED),
    CP_LoginBasicInfo(ClientState.CONNECTED),
    CP_CheckLoginAuthInfo(ClientState.CONNECTED),
    CP_SelectWorld(ClientState.AUTHED),
    CP_CheckSPWRequest(ClientState.AUTHED),
    CP_LogoutWorld(ClientState.CONNECTED),
    CP_ClientLoadingTimeLog,
    CP_CheckSPWExistRequest(ClientState.AUTHED),
    CP_MigrateIn(ClientState.CONNECTED),
    CP_SelectCharacter(ClientState.AUTHED),
    CP_SelectGoToStarPlanet,
    CP_SelectAccount,
    CP_WorldInfoRequest(ClientState.AUTHED),
    CP_CheckDuplicatedID(ClientState.AUTHED),
    CP_CreateNewCharacter(ClientState.AUTHED),
    CP_UpdateCharacterSelectList(ClientState.AUTHED),
    CP_DeleteCharacter(ClientState.AUTHED),
    CP_CreateNewCharacterInCS,
    CP_CreateNewCharacter_PremiumAdventurer,
    CP_ReservedDeleteCharacterConfirm,
    CP_ReservedDeleteCharacterCancel,
    CP_RenameCharacter,
    CP_ExceptionLog(ClientState.CONNECTED),
    CP_PrivateServerPacket,
    CP_ResetLoginStateOnCheckOTP,
    CP_CreateSecurityHandle,
    CP_LogForDebug,
    CP_AlbaRequest,
    CP_UpdateCharacterCard,
    CP_CheckCenterAndGameAreConnected,
    CP_CreateMapleAccount,
    CP_BackToLogin(ClientState.CONNECTED),
    CP_AliveAck(ClientState.CONNECTED),
    CP_ResponseToCheckAliveAck,
    CP_ClientDumpLog(ClientState.values()),
    CP_SetGender(ClientState.AUTHED_GG),
    CP_ServerStatusRequest,
    CP_LoginBackground(ClientState.CONNECTED),
    CP_DirectGoToField(ClientState.AUTHED),
    CP_ApplyChangeName,
    CP_EndSocket,
    CP_UserBegin,
    CP_UserTransferFieldRequest,
    CP_UserTransferChannelRequest,
    CP_UserMigrateToCashShopRequest,
    CP_UserMigrateToPvpRequest,
    CP_UserTransferAswanRequest,
    CP_UserTransferAswanReadyRequest,
    CP_AswanRetireRequest,
    CP_UserRequestPvPStatus,
    CP_UserMigrateToPveRequest,
    CP_UserFinalAttackRequest,
    CP_UserMove,
    CP_UserSitRequest,
    CP_UserPortableChairSitRequest,
    CP_UserMeleeAttack,
    CP_UserShootAttack,
    CP_UserMagicAttack,
    CP_UserBodyAttack,
    CP_UserAreaDotAttack,
    CP_UserMovingShootAttackPrepare,
    CP_UserHit,
    CP_UserAttackUser,
    CP_UserChat,
    CP_UserADBoardClose,
    CP_UserEmotion,
    CP_AndroidEmotion,
    CP_UserActivateEffectItem,
    CP_UserMonkeyEffectItem,
    CP_UserActivateNickItem,
    CP_UserActivateDamageSkin,
    CP_UserDefaultWingItem,
    CP_UserBanMapByMob,
    CP_UserSelectNpc,
    CP_UserScriptMessageAnswer,
    CP_UserShopRequest,
    CP_UserTrunkRequest,
    CP_UserEntrustedShopRequest,
    CP_UserStoreBankRequest,
    CP_UserParcelRequest,
    CP_UserEffectLocal,
    CP_UserSpecialEffectLocal,
    CP_ShopScannerRequest,
    CP_ShopLinkRequest,
    CP_AdminShopRequest,
    CP_UserGatherItemRequest,
    CP_UserSortItemRequest,
    CP_UserChangeSlotPositionRequest,
    CP_UserPopOrPushBagItemToInven,
    CP_UserBagToBagItem,
    CP_UserStatChangeItemUseRequest,
    CP_UserStatChangeItemCancelRequest,
    CP_UserMobSummonItemUseRequest,
    CP_UserPetFoodItemUseRequest,
    CP_UserTamingMobFoodItemUseRequest,
    CP_UserScriptItemUseRequest,
    CP_UserRecipeOpenItemUseRequest,
    CP_UserConsumeCashItemUseRequest,
    CP_UserAdditionalSlotExtendItemUseRequest,
    CP_UserCashPetPickUpOnOffRequest,
    CP_UserCashPetSkillSettingRequest,
    CP_UserOptionChangeRequest,
    CP_UserDestroyPetItemRequest,
    CP_UserSkillLearnItemUseRequest,
    CP_UserExpConsumeItemUseRequest,
    CP_UserShopScannerItemUseRequest,
    CP_UserMapTransferItemUseRequest,
    CP_UserPortalScrollUseRequest,
    CP_UserFieldTransferRequest,
    CP_UserUpgradeItemUseRequest,
    CP_UserUpgradeAssistItemUseRequest,
    CP_UserHyperUpgradeItemUseRequest,
    CP_UserItemOptionUpgradeItemUseRequest,
    CP_UserAdditionalOptUpgradeItemUseRequest,
    CP_UserItemSlotExtendItemUseRequest,
    CP_UserWeaponTempItemOptionRequest,
    CP_UserItemSkillSocketUpgradeItemUseRequest,
    CP_UserItemSkillOptionUpgradeItemUseRequest,
    CP_UserFreeMiracleCubeItemUseRequest,
    CP_UserEquipmentEnchantWithSingleUIRequest,
    CP_UserBagItemUseRequest,
    CP_UserItemReleaseRequest,
    CP_UserToadsHammerRequest,
    CP_UserAbilityUpRequest,
    CP_UserAbilityMassUpRequest,
    CP_UserChangeStatRequest,
    CP_SetSonOfLinkedSkillRequest,
    CP_UserSkillUpRequest,
    CP_UserSkillUseRequest,
    CP_UserSkillCancelRequest,
    CP_UserSkillPrepareRequest,
    CP_UserDropMoneyRequest,
    CP_UserGivePopularityRequest,
    CP_UserCharacterInfoRequest,
    CP_UserActivatePetRequest,
    CP_UserRegisterPetAutoBuffRequest,
    CP_UserTemporaryStatUpdateRequest,
    CP_UserPortalScriptRequest,
    CP_UserPortalTeleportRequest,
    CP_UserMapTransferRequest,
    CP_UserAntiMacroItemUseRequest,
    CP_UserAntiMacroSkillUseRequest,
    CP_UserOldAntiMacroQuestionResult,
    CP_UserAntiMacroRefreshRequest,
    CP_UserClaimRequest,
    CP_UserQuestRequest,
    CP_UserMedalReissueRequest,
    CP_UserCalcDamageStatSetRequest,
    CP_UserB2BodyRequest,
    CP_UserThrowGrenade,
    CP_UserDestroyGrenade,
    CP_UserCreateAuraByGrenade,
    CP_UserSetMoveGrenade,
    CP_UserMacroSysDataModified,
    CP_UserSelectNpcItemUseRequest,
    CP_UserItemMakeRequest,
    CP_UserRepairDurabilityAll,
    CP_UserRepairDurability,
    CP_UserFollowCharacterRequest,
    CP_UserSelectPQReward,
    CP_UserRequestPQReward,
    CP_SetPassenserResult,
    CP_UserRequestInstanceTable,
    CP_UserRequestCreateItemPot,
    CP_UserRequestRemoveItemPot,
    CP_UserRequestIncItemPotLifeSatiety,
    CP_UserRequestCureItemPotLifeSick,
    CP_UserRequestComplateToItemPot,
    CP_UserRequestRespawn,
    CP_UserConsumeHairItemUseRequest,
    CP_UserRequestCharacterPotentialSkillRandSet,
    CP_UserRequestCharacterPotentialSkillRandSetUI,
    CP_UserForceAtomCollision,
    CP_ZeroTag,
    CP_ZeroShareCashEquipPart,
    CP_UserLuckyItemUseRequest,
    CP_UserMobMoveAbilityChange,
    CP_BroadcastMsg,
    CP_GroupMessage,
    CP_Whisper,
    CP_Messenger,
    CP_MiniRoom,
    CP_PartyRequest,
    CP_PartyResult,
    CP_PartyInvitableSet,
    CP_ExpeditionRequest,
    CP_PartyAdverRequest,
    CP_GuildRequest,
    CP_GuildResult,
    CP_GuildJoinRequest,
    CP_GuildJoinCancelRequest,
    CP_GuildJoinAccept,
    CP_GuildJoinReject,
    CP_Admin,
    CP_Log,
    CP_FriendRequest,
    CP_MemoFlagRequest,
    CP_EnterTownPortalRequest,
    CP_EnterOpenGateRequest,
    CP_FuncKeyMappedModified,
    CP_RPSGame,
    CP_MarriageRequest,
    CP_WeddingWishListRequest,
    CP_AllianceRequest,
    CP_AllianceResult,
    CP_TalkToTutor,
    CP_RequestIncCombo,
    CP_RequestDecCombo,
    CP_MakingSkillRequest,
    CP_BroadcastEffectToSplit,
    CP_BroadcastOneTimeActionToSplit,
    CP_UserTransferFreeMarketRequest,
    CP_UserRequestSetStealSkillSlot,
    CP_UserRequestStealSkillMemory,
    CP_UserRequestStealSkillList,
    CP_UserRequestStealSkill,
    CP_UserRequestFlyingSwordStart,
    CP_UserHyperSkillUpRequest,
    CP_UserHyperSkillResetRequset,
    CP_UserHyperStatSkillUpRequest,
    CP_UserHyperStatSkillResetRequest,
    CP_RequestReloginCookie,
    CP_WaitQueueRequest,
    CP_CheckTrickOrTreatRequest,
    CP_MapleStyleBonusRequest,
    CP_UserAntiMacroQuestionResult,
    CP_UserPinkbeanYoYoStack,
    CP_UserQuickMoveScript,
    CP_UserSelectAndroid,
    CP_UserCompleteNpcSpeech,
    CP_UserMobDropMesoPickup,
    CP_RequestEventList,
    CP_AddAttackReset,
    CP_KAISER_QUICK_KEY,
    CP_UseFamiliarCard,
    CP_BINGO,
    CP_CharacterBurning,


    CP_UserUpdateMatrix,
    CP_PetMove,
    CP_PetAction,
    CP_PetInteractionRequest,
    CP_PetDropPickUpRequest,
    CP_PetStatChangeItemUseRequest,
    CP_PetUpdateExceptionListRequest,
    CP_PetFoodItemUseRequest,
    CP_SkillPetMove,
    CP_SkillPetAction,
    CP_SummonedMove,
    CP_SummonedAttack,
    CP_SummonedHit,
    CP_SummonedSkill,
    CP_SummonedAssistAttackDone,
    CP_Remove,
    CP_DragonMove,
    CP_USE_ITEM_QUEST,
    CP_AndroidMove,
    CP_AndroidActionSet,
    CP_UPDATE_QUEST,
    CP_QUEST_ITEM,
    CP_FoxManMove,
    CP_FoxManActionSetUseRequest,
    CP_QuickslotKeyMappedModified,
    CP_PassiveskillInfoUpdate,
    CP_DirectionNodeCollision,
    CP_CheckProcess,
    CP_EgoEquipGaugeCompleteReturn,
    CP_EgoEquipCreateUpgradeItem,
    CP_EgoEquipCreateUpgradeItemCostRequest,
    CP_EgoEquipTalkRequest,
    CP_EgoEquipCheckUpdateItemRequest,
    CP_InheritanceInfoRequest,
    CP_InheritanceUpgradeRequest,
    CP_UserUpdateMapleTVShowTime,
    CP_RequestArrowPlaterObj,
    CP_LP_GuildTransfer,
    CP_LP_GuildTransfer2,
    CP_DMG_FLAME,
    CP_SHINING_STAR_WORLD,
    CP_BOSS_LIST,
    CP_BBS_OPERATION,
    CP_EXIT_GAME,
    CP_ORBITAL_FLAME,
    CP_PAM_SONG,
    CP_TRANSFORM_PLAYER,
    CP_ATTACK_ON_TITAN_SELECT,
    CP_ENTER_MTS,
    CP_SOLOMON,
    CP_GACH_EXP,
    CP_CHRONOSPHERE,
    CP_UIWindowTW,
    CP_SAVE_DAMAGE_SKIN,
    CP_CHANGE_DAMAGE_SKIN,
    CP_REMOVE_DAMAGE_SKIN,
    CP_PSYCHIC_GREP_R,
    CP_CANCEL_PSYCHIC_GREP_R,
    CP_PSYCHIC_ATTACK_R,
    CP_PSYCHIC_DAMAGE_R,
    CP_PSYCHIC_ULTIMATE_R,
    CP_MobMove,
    CP_MobApplyCtrl,
    CP_MobTimeBombEnd,
    CP_MobLiftingEnd,
    CP_NpcMove,
    CP_DropPickUpRequest,
    CP_ReactorHit,
    CP_ReactorClick,
    CP_DecomposerRequest,
    CP_UPDATE_ENV,
    CP_SnowBallHit,
    CP_SnowBallTouch,
    CP_PLAYER_UPDATE,
    CP_PartyMemberCandidateRequest,
    CP_UrusPartyMemberCandidateRequest,
    CP_PartyCandidateRequest,
    CP_GatherRequest,
    CP_GatherEndNotice,
    CP_MakeEnterFieldPacketForQuickMove,
    CP_RuneStoneUseReq,
    CP_RuneStoneSkillReq,
    CP_CashShopChargeParamRequest,
    CP_CashShopQueryCashRequest,
    CP_CashShopCashItemRequest,
    CP_CashShopCheckCouponRequest,
    CP_CashShopMemberShopRequest,
    CP_CashShopCoodinationRequest,
    CP_CashShopCheckMileageRequest,
    CP_CheckSPWOnCreateNewCharacter(ClientState.AUTHED),
    CP_GoldHammerRequest,
    CP_GoldHammerComplete,
    CP_PlatinumHammerRequest,
    CP_BattleRecordOnOffRequest,
    CP_REWARD,
    CP_EFFECT_SWITCH,
    CP_UNKNOWN,
    CP_USE_ABYSS_SCROLL,
    CP_MONSTER_BOOK_DROPS,
    CP_RSA_KEY,
    CP_MAPLETV,
    CP_CRASH_INFO,
    CP_GUEST_LOGIN,
    CP_TOS,
    CP_VIEW_SERVERLIST,
    CP_REDISPLAY_SERVERLIST,
    CP_CHAR_SELECT_NO_PIC,
    CP_AUTH_REQUEST,
    CP_VIEW_REGISTER_PIC,
    CP_VIEW_SELECT_PIC,
    CP_CLIENT_FAILED,
    CP_ENABLE_SPECIAL_CREATION,
    CP_CREATE_SPECIAL_CHAR,
    CP_AUTH_SECOND_PASSWORD,
    CP_WRONG_PASSWORD,
    CP_ENTER_FARM,
    CP_CHANGE_CODEX_SET,
    CP_CODEX_UNK,
    CP_USE_NEBULITE,
    CP_USE_ALIEN_SOCKET,
    CP_USE_ALIEN_SOCKET_RESPONSE,
    CP_USE_NEBULITE_FUSION,
    CP_TOT_GUIDE,
    CP_GET_BOOK_INFO,
    CP_USE_FAMILIAR,
    CP_SPAWN_FAMILIAR,
    CP_RENAME_FAMILIAR,
    CP_PET_BUFF,
    CP_USE_TREASURE_CHEST,
    CP_SOLOMON_EXP,
    CP_NEW_YEAR_CARD,
    CP_XMAS_SURPRISE,
    CP_TWIN_DRAGON_EGG,
    CP_YOUR_INFORMATION,
    CP_FIND_FRIEND,
    CP_PINKBEAN_CHOCO_OPEN,
    CP_PINKBEAN_CHOCO_SUMMON,
    CP_BUY_SILENT_CRUSADE,
    CP_CASSANDRAS_COLLECTION,
    CP_BUDDY_ADD,
    CP_PVP_SUMMON,
    CP_MOVE_FAMILIAR,
    CP_TOUCH_FAMILIAR,
    CP_ATTACK_FAMILIAR,
    CP_REVEAL_FAMILIAR,
    CP_FRIENDLY_DAMAGE,
    CP_HYPNOTIZE_DMG,
    CP_MOB_BOMB,
    CP_MOB_NODE,
    CP_DISPLAY_NODE,
    CP_MONSTER_CARNIVAL,
    CP_CLICK_REACTOR,
    CP_CANDY_RANKING,
    CP_COCONUT,
    CP_SHIP_OBJECT,
    CP_PLACE_FARM_OBJECT,
    CP_FARM_SHOP_BUY,
    CP_FARM_COMPLETE_QUEST,
    CP_FARM_NAME,
    CP_HARVEST_FARM_BUILDING,
    CP_USE_FARM_ITEM,
    CP_RENAME_MONSTER,
    CP_NURTURE_MONSTER,
    CP_EXIT_FARM,
    CP_FARM_QUEST_CHECK,
    CP_FARM_FIRST_ENTRY,
    CP_PYRAMID_BUY_ITEM,
    CP_CLASS_COMPETITION,
    CP_MAGIC_WHEEL,
    CP_BLACK_FRIDAY,
    CP_RECEIVE_GIFT_EFFECT,
    CP_UPDATE_RED_LEAF,
    CP_CLICK_BINGO_CARD,
    CP_PRESS_BINGO,
    CP_DRESSUP_TIME,
    CP_OS_INFORMATION,
    CP_LUCKY_LOGOUT,
    CP_MESSENGER_RANKING;


    private short value;

    @Getter
    private ClientState[] states;

    InHeader(short value, ClientState... clientStates) {
        this.states = clientStates;
        this.value = value;
    }

    InHeader() {
        this((short) 0xFFFF);
    }

    InHeader(short value) {
        this(value, ClientState.ENTERED, ClientState.CONNECTED);
    }

    InHeader(ClientState... clientStates) {
        this((short) 0xFFFF, clientStates);
    }

    public static InHeader getInHeaderByOp(int op) {
        for (InHeader inHeader : InHeader.values()) {
            if (inHeader.getValue() == op) {
                return inHeader;
            }
        }
        return null;
    }

    public static boolean isSpamHeader(InHeader inHeaderByOp) {
        List<InHeader> spam = Arrays.asList(
        );
        return spam.contains(inHeaderByOp);
    }

    @Override
    public short getValue() {
        return this.value;
    }

    @Override
    public void setValue(short value) {
        this.value = value;
    }

    public boolean ignoreDebug() {
        switch (this.name()) {
            case "CP_UserQuestRequest":
            case "CP_MobMove":
            case "CP_UserMove":
            case "CP_UserHit":
            case "CP_DummyCode":
            case "CP_UserChangeStatRequest":
            case "CP_MobApplyCtrl":
            case "CP_UserRequestInstanceTable":
            case "CP_UserPortalScriptRequest":
            case "CP_ReactorHit":
            case "CP_UserMeleeAttack":
            case "CP_UserChangeSlotPositionRequest":
                return true;
            default:
                return false;
        }
    }
}
