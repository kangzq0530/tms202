package com.msemu.core.network;

import com.msemu.commons.network.Client;
import com.msemu.commons.network.Connection;
import com.msemu.commons.network.crypt.MapleCrypt;
import com.msemu.core.configs.CoreConfig;
import com.msemu.core.network.packets.out.Login.ConnectToClient;
import com.msemu.login.client.Account;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/3/29.
 */
public class LoginClient extends Client<LoginClient> {


    private Account accountSchema;

    @Getter
    @Setter
    private int world;

    @Getter
    @Setter
    private int channel;

    public LoginClient(Connection<LoginClient> connection) {
        super(connection);
    }


    @Override
    public void onOpen() {
        super.onOpen();
        write(new ConnectToClient(this));
        MapleCrypt crypt = new MapleCrypt(CoreConfig.GAME_SERVICE_TYPE, (short) CoreConfig.GAME_VERSION);
        getConnection().setCipher(crypt);
    }

    public Account getAccount() {
        return accountSchema;
    }

    public void setAccount(Account accountSchema) {
        this.accountSchema = accountSchema;
    }

}
