package com.msemu.core.network;

import com.msemu.commons.network.Client;
import com.msemu.commons.network.Connection;
import com.msemu.commons.network.crypt.MapleCrypt;
import com.msemu.core.configs.CoreConfig;
import com.msemu.core.network.packets.outpacket.login.LP_ConnectToClient;
import com.msemu.login.client.Account;
import com.msemu.login.enums.LoginResultCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/3/29.
 */
public class LoginClient extends Client<LoginClient> {

    @Getter
    @Setter
    private Account account;

    @Getter
    @Setter
    private int world;

    @Getter
    @Setter
    private int channel;

    @Getter
    @Setter
    private LoginResultCode loginResult = LoginResultCode.TempBlock;

    public LoginClient(Connection<LoginClient> connection) {
        super(connection);
    }


    @Override
    public void onOpen() {
        super.onOpen();
        write(new LP_ConnectToClient(this));
        MapleCrypt crypt = new MapleCrypt(CoreConfig.GAME_SERVICE_TYPE, (short) CoreConfig.GAME_VERSION);
        getConnection().setSendCipher(crypt);
        getConnection().setRecvCipher(crypt);
    }





}
