package com.msemu.login.enums;

/**
 * Created by Weber on 2018/3/29.
 */
public enum LoginResultType {
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
     * 0xD ip not in master ??
     * 0xE 臨時加入帳號無法遊戲，未滿14歲請法定代理同意後即可進行遊戲
     * 0xF Gash 帳號不存在
     * 0x11 認證資料不符合，請關閉開啟中的楓之谷官方網頁
     * 0x13 現在透過臨時被封鎖的IP連接 (會自動關閉遊戲)
     * 0x27 use playsafe card to Login
     * 0x29 account is not qualified for testing server
     * 0x2A use otp
     * 0x22 server high loading
     * 0x2B ip not in service range
     * 0x2C use beanfun to Login
     * 0x2D plz upgrade account level
     */
    LoginSuccess(0),
    Nop(1),
    BlockForIndecencyBehavior(2),
    AbnormalAccountStatus(3),
    InvalidPassword(4),
    AccountNotExists(5),
    ServerError(6),
    ArealdyLogin(7),
    ServerError2(8),
    ServerBusy(0xA),
    AgeUnder20(0xB),
    Unknown(0xC),
    MasterIPNotAllow(0xD),
    TemporaryAccountComfirm(0xE),
    GashAccountNotExists(0xF),
    AuthenticationDataNotMatch(0x11),
    TempBanIPConnect(0x13),
    InvalidSecondPassword(0x14),
    ServerHighLoading(0x22),
    PleaseLoginFromPlaySafe(0x27),
    AccountNotQualifiedForTesting(0x29),
    PleaseLoginFromOTP(0x2A),
    IPNotInRange(0x2B),
    PleaseLoginFromBeanfun(0x2C),
    PleaseUpgradeAccount(0x2D),
    VerifyWithCaptcha(0x45),
            ;

    private byte value;

    LoginResultType(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }
}
