package com.msemu.login.network.packets.send.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.enums.WorldServerType;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.rmi.model.WorldInfo;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.login.client.Account;
import com.msemu.login.client.character.Character;

import java.util.List;

/**
 * Created by Weber on 2018/3/31.
 */
public class SelectWorldResult extends OutPacket {

    public SelectWorldResult(Account account, WorldInfo worldInfo) {
        super(OutHeader.SelectWorldResult);

        encodeByte(0); // loginResultType

        encodeString(worldInfo.getServerType().getValue());
        encodeInt(4);
        encodeByte(worldInfo.getServerType().equals(WorldServerType.Reboot));

        int reserved = 0;
        encodeInt(reserved); // TODO: 閃耀之星
        FileTime.getFTFromLong(0).encode(this);
        for (int i = 0; i < reserved; i++) {
            FileTime ft = FileTime.getFTFromLong(0);
            encodeInt(ft.getLowDateTime());
            ft.encode(this);
        }

        encodeByte(false);

        List<Character> chars = account.getCharacters();

        encodeInt(chars.size());
        chars.forEach(c -> {
            encodeInt(c.getId());
        });
        chars.forEach(c -> {
            c.getAvatarData().encode(this);
            encodeByte(false);
            boolean hasRanking = c.getRanking() != null;
            encodeByte(hasRanking);
            if (hasRanking) {
                c.getRanking().encode(this);
            }
        });

        encodeByte(3);
        encodeByte(account.getPicStatus().getValue()); // bLoginOpt
        encodeInt(account.getCharacterSlots());
        encodeInt(0); // 50 等角色卡數量
        encodeInt(-1); // nEventNewCharJob
        encodeByte(0); // nRenameCount
        encodeFT(new FileTime(System.currentTimeMillis()));
        encodeByte(0);   // 變更角色名稱開關(在名字下方的, 不能單獨控制哪個角色能改)[0:關、1:開]
        encodeByte(0);  // 協議書開關[-1:開、0:關]
        encodeInt(0);
        encodeInt(0);
        encodeFT(new FileTime(System.currentTimeMillis()));
    }
}
