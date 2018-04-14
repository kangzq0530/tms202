package com.msemu.login.network.packets.recv;

import com.msemu.commons.network.packets.InPacket;
import com.msemu.commons.rmi.model.WorldInfo;
import com.msemu.commons.utils.BCryptUtils;
import com.msemu.commons.utils.HexUtils;
import com.msemu.core.startup.StartupComponent;
import com.msemu.login.LoginServer;
import com.msemu.login.client.Account;
import com.msemu.login.client.LoginClient;
import com.msemu.login.enums.LoginResultType;
import com.msemu.login.network.packets.send.login.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/29.
 */

@StartupComponent("Network")
public class LoginPacketHandler {

    private static final AtomicReference<LoginPacketHandler> instance = new AtomicReference<>();

    public static LoginPacketHandler getInstance() {
        LoginPacketHandler value = instance.get();
        if (value == null) {
            synchronized (LoginPacketHandler.instance) {
                value = instance.get();
                if (value == null) {
                    value = new LoginPacketHandler();
                }
                instance.set(value);
            }
        }
        return value;
    }


    public void handleLoginBasicInfo(LoginClient client, InPacket packet) {
//        byte local = packet.decodeByte();
//        short version = packet.decodeShort();
//        String patchVer = String.valueOf(packet.decodeShort());
//        if (CoreConfig.GAME_SERVICE_TYPE.getLocal() != local || CoreConfig.GAME_VERSION != version || !CoreConfig.GAME_PATCH_VERSION.equals(patchVer)) {
//            client.close();
//        }
    }

    public void handleLoginBackground(LoginClient client, InPacket packet) {
        client.write(new LoginBackground());
    }

    public void handleCheckLoginAuthInfo(LoginClient client, InPacket packet) {
        LoginResultType status = LoginResultType.Nop;
        Account accountSchema = null;
        try {
            String macAddress = HexUtils.byteArraytoHex(packet.decodeBytes(6)); //

            packet.skip(16);

            String username = packet.decodeString();
            String password = packet.decodeString();

            accountSchema = Account.findByUserName(username);
            if (accountSchema == null) {
                status = LoginResultType.AccountNotExists;
            } else if (BCryptUtils.checkPassword(password, accountSchema.getPassword())) {
                if (accountSchema.getPic().equals("")) {
                    client.write(new SetAccountGender(client));
                } else {
                    //TODO : Check Password, IP
                    // 踢下線
                    boolean isOnline = LoginServer.getRmi().isAccountOnline(accountSchema);
                    boolean result = LoginServer.getRmi().kickPlayerByAccount(accountSchema);
                    if (isOnline && !result) {
                        status = LoginResultType.ServerError;
                    } else {
                        status = LoginResultType.LoginSuccess;
                    }
                }
            }
        } catch (Exception ignore) {
            status = LoginResultType.ServerError;
        }

        if (!status.equals(LoginResultType.LoginSuccess)) {
            client.setAuthorized(false);
            client.write(new CheckPasswordResult(null, status, null));
            return;
        }
        client.setAuthorized(true);
        client.setAccountSchema(accountSchema);
        client.write(new CheckPasswordResult(accountSchema, status, null));
        List<WorldInfo> worlds = LoginServer.getRmi().getWorlds();
        worlds.forEach(world -> client.write(new WorldInformation(world)));
        client.write(new WorldInformationEnd());
    }

    public void handleWorldInfoRequest(LoginClient client, InPacket packet) {
        return;
    }

    public void handleSelectWorld(LoginClient client, InPacket packet) {

        final int unk = packet.decodeByte();
        final int worldId = packet.decodeByte();
        final int channel = packet.decodeByte() + 1;

//        boolean checkWolrd = LoginServer.getRmi().hasWorldChannel(worldId, channel);

//        if(!checkWolrd) {
//            return;
//        }
//

        //client.write(new SelectWorldResult());
    }

    public void handleClientStart(LoginClient client, InPacket packet) {
        client.write(new ShowMapleStory());
    }

    public void handleBackToLogin(LoginClient client, InPacket packet) {
        String userName = packet.decodeString();
        if (client.getAccount().getUsername().equals(userName)) {
            client.setAuthorized(false);
        }
    }
}
