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

package com.msemu.login.enums;

/**
 * Created by Weber on 2018/3/29.
 */
public enum LoginResultCode {
    /**
     * 0x2 帳號鎖定 (不雅言語)
     * 0x3 帳號非正常狀態
     * 0x4 帳號密碼錯誤
     * 0x5 帳號不存在
     * 0x6 system error , plz reconnect
     * 0x7 帳號已登入
     * 0x8 system error , plz reconnect
     * 0x9
     * 0xA 伺服器忙碌中
     * 0xB age 20 limit
     * 0xD host not in master ??
     * 0xE 臨時加入帳號無法遊戲，未滿14歲請法定代理同意後即可進行遊戲
     * 0xF Gash 帳號不存在
     * 0x11 認證資料不符合，請關閉開啟中的楓之谷官方網頁
     * 0x13 現在透過臨時被封鎖的IP連接 (會自動關閉遊戲)
     * 0x27 use playsafe card to login
     * 0x29 account is not qualified for testing server
     * 0x2A use otp
     * 0x22 server high loading
     * 0x2B host not in service range
     * 0x2C use beanfun to login
     * 0x2D plz upgrade account level
     */
    ProcFailed(-1),
    LoginSuccess(0),
    TempBlock(1),
    BlockForIndecencyBehavior(2),
    AbnormalAccountStatus(3),
    IncorrectPassword(4),
    NotRegistered(5),
    DBFail(6),
    AlreadyConnected(7),
    NotConnectableWorld(8),
    ServerBusy(0xA),
    NotAdult(0xB),
    AuthFail(0xC),
    ImpossibleIP(0xD),
    NotAuthorizedNexonID(0xE),
    NoNexonID(0xF),
    WebAuthNeeded(0x11),
    DeleteCharacterFailedOnGuildMaster(0x12),
    TempBlockedIP(0x13),
    IncorrectSPW(0x14),
    DeleteCharacterFailedEngaged(0x15),
    SamePasswordAndSPW(0x16),
    WaitOTP(0x17),
    WorongOTP(0x18),
    OverCountErrOTP(0x19),
    SystemErr(0x1A),

    WorldTooBusy(0x22),
    AlbaPerform(0x27),
    AccountNotQualifiedForTesting(0x29),
    PleaseLoginFromOTP(0x2A),
    IPNotInRange(0x2B),
    PleaseLoginFromBeanfun(0x2C),
    PleaseUpgradeAccount(0x2D),
    VerifyWithCaptcha(0x45),;

    private byte value;

    LoginResultCode(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }
}

/***
 * enum $82EEEA89A4E26205EA19E0AB95DD1238
 {
 LoginResCode_ProcFail = 0xFFFFFFFF,
 LoginResCode_Success = 0x0,
 LoginResCode_TempBlocked = 0x1,
 LoginResCode_Blocked = 0x2,
 LoginResCode_Abandoned = 0x3,
 LoginResCode_IncorrectPassword = 0x4,
 LoginResCode_NotRegistered = 0x5,
 LoginResCode_DBFail = 0x6,
 LoginResCode_AlreadyConnected = 0x7,
 LoginResCode_NotConnectableWorld = 0x8,
 LoginResCode_Unknown = 0x9,
 LoginResCode_Timeout = 0xA,
 LoginResCode_NotAdult = 0xB,
 LoginResCode_AuthFail = 0xC,
 LoginResCode_ImpossibleIP = 0xD,
 LoginResCode_NotAuthorizedNexonID = 0xE,
 LoginResCode_NoNexonID = 0xF,
 LoginResCode_IncorrectSSN2 = 0x10,
 LoginResCode_WebAuthNeeded = 0x11,
 LoginResCode_DeleteCharacterFailedOnGuildMaster = 0x12,
 LoginResCode_TempBlockedIP = 0x13,
 LoginResCode_IncorrectSPW = 0x14,
 LoginResCode_DeleteCharacterFailedEngaged = 0x15,
 LoginResCode_SamePasswordAndSPW = 0x16,
 LoginResCode_WaitOTP = 0x17,
 LoginResCode_WrongOTP = 0x18,
 LoginResCode_OverCountErrOTP = 0x19,
 LoginResCode_SystemErr = 0x1A,
 LoginResCode_CancelInputDeleteCharacterOTP = 0x1B,
 LoginResCode_PaymentWarning = 0x1C,
 LoginResCode_DeleteCharacterFailedOnFamily = 0x1D,
 LoginResCode_InvalidCharacterName = 0x1E,
 LoginResCode_IncorrectSSN = 0x1F,
 LoginResCode_SSNConfirmFailed = 0x20,
 LoginResCode_SSNNotConfirmed = 0x21,
 LoginResCode_WorldTooBusy = 0x22,
 LoginResCode_OTPReissuing = 0x23,
 LoginResCode_OTPInfoNotExist = 0x24,
 LoginResCode_Shutdowned = 0x25,
 LoginResCode_DeleteCharacterFailedHasEntrustedShop = 0x26,
 LoginResCode_AlbaPerform = 0x27,
 LoginResCode_TransferredToNxEmailID = 0x28,
 LoginResCode_UntransferredToNxEmailID = 0x29,
 LoginResCode_RequestedMapleIDAlreadyInUse = 0x2A,
 LoginResCode_WaitSelectAccount = 0x2B,
 LoginResCode_DeleteCharacterFailedProtectedItem = 0x2C,
 LoginResCode_UnauthorizedUser = 0x2D,
 LoginResCode_CannotCreateMoreMapleAccount = 0x2E,
 LoginResCode_CreateBanned = 0x2F,
 LoginResCode_CreateTemporarilyBanned = 0x30,
 LoginResCode_EventNewCharacterExpireFail = 0x31,
 LoginResCode_SelectiveShutdowned = 0x32,
 LoginResCode_NonownerRequest = 0x33,
 LoginResCode_OTPRequired = 0x34,
 LoginResCode_GuestServiceClosed = 0x35,
 LoginResCode_BlockedNexonID = 0x36,
 LoginResCode_DupMachineID = 0x37,
 LoginResCode_NotActiveAccount = 0x38,
 LoginResCode_IncorrectSPW4th = 0x39,
 LoginResCode_IncorrectSPW5th = 0x3A,
 LoginResCode_InsufficientSPW = 0x3B,
 LoginResCode_SameCharSPW = 0x3C,
 LoginResCode_WebLaunchingOTPRequired = 0x3D,
 LoginResCode_MergeWorld_CreateCharacterBanned = 0x3E,
 LoginResCode_ChangeNewOTP = 0x3F,
 LoginResCode_BlockedByServiceArea = 0x40,
 LoginResCode_ExceedReservedDeleteCharacter = 0x41,
 LoginResCode_UnionFieldChannelClosed = 0x43,
 LoginResCode_ProtectAccount = 0x44,
 LoginResCode_AntiMacroReq = 0x45,
 LoginResCode_AntiMacroCreateFailed = 0x46,
 LoginResCode_AntiMacroIncorrect = 0x47,
 LoginResCode_LimitCreateCharacter = 0x48,
 LoginResCode_ProtectSSOLogin = 0x49,
 LoginResCode_InvalidMapleIDThroughMobile = 0x4A,
 LoginResCode_InvalidPasswordThroughMobile = 0x4B,
 LoginResCode_HashedPasswordIsEmpty = 0x4C,
 LoginResCode_NGS_For_Ass = 0x4D,
 LoginResCode_AlreadyConnectedThroughMobile = 0x4E,
 LoginResCode_Protected_For_Ass = 0x4F,
 LoginResCode_Blocked_For_Ass = 0x50,
 LoginResCode_WrongVer = 0x51,
 LoginResCode_EMailVerify = 0x52,
 LoginResCode_DenyJob = 0x53,
 LoginResCode_InvalidObject = 0x54,
 LoginResCode_IncorrectLoginType_OtherToMapleID = 0x55,
 LoginResCode_FailedUserCreate = 0x56,
 LoginResCode_MobileTokenInvalid = 0x57,
 LoginResCode_MobileTokenDeviceIDInvalid = 0x58,
 LoginResCode_MobileTokenExpired = 0x59,
 LoginResCode_NotHaveNaverID = 0x5A,
 LoginResCode_UserTossAIPlayer = 0x5B,
 LoginResCode_InactivateMember = 0x5C,
 };

 */
