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

package com.msemu.core.network.packets.inpacket;

import com.msemu.commons.data.enums.InvType;
import com.msemu.commons.database.DatabaseFactory;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.outpacket.login.LP_CreateNewCharacterResult;
import com.msemu.login.client.Account;
import com.msemu.login.client.character.Character;
import com.msemu.login.client.character.CharacterStat;
import com.msemu.login.client.character.FuncKeyMap;
import com.msemu.login.client.character.items.Equip;
import com.msemu.login.client.character.items.Item;
import com.msemu.login.constants.ItemConstants;
import com.msemu.login.data.ItemData;
import com.msemu.login.data.StringData;
import com.msemu.login.enums.CharCreateInfo;
import com.msemu.login.enums.CharCreateItemFlag;
import com.msemu.login.enums.LoginResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.msemu.commons.data.enums.InvType.EQUIPPED;

/**
 * Created by Weber on 2018/4/19.
 */
public class CP_CreateNewCharacter extends InPacket<LoginClient> {

    private static final Logger log = LoggerFactory.getLogger(CP_CreateNewCharacter.class);

    private String charName;
    private int keyMapType;
    private int jobType;
    private int jobSubCategory;
    private int eventNewCharSaleJob;
    private byte gender;
    private byte skin;
    private List<Integer> items;


    public CP_CreateNewCharacter(short opcode) {
        super(opcode);
        items = new ArrayList<>();
    }

    @Override
    public void read() {
        charName = decodeString();
        keyMapType = decodeInt();
        eventNewCharSaleJob = decodeInt();
        jobType = decodeInt();
        jobSubCategory = decodeShort();
        gender = decodeByte();
        skin = decodeByte();

        int size = decodeByte();

        for (int i = 0; i < size; i++) {
            items.add(decodeInt());
        }

    }

    @Override
    public void runImpl() {

        boolean checkLogin = getClient().getLoginResult() == LoginResultCode.LoginSuccess;

        boolean used = Character.isNameExists(charName) ||
                StringData.getInstance().getForbiddenName().getNames().contains(charName);

        if (used && checkLogin) {
            getClient().write(new LP_CreateNewCharacterResult(LoginResultCode.TempBlock, null));
            return;
        }

        CharCreateInfo info = CharCreateInfo.getByJobType(jobType);

        if (info == null || !info.enableCreate() || info.getSubJob() != jobSubCategory) {
            if (info == null) {
                log.error("Found new Job jobType:{}", jobType);
            }
            return;
        }

        boolean checkSkin = skin == 0;

        switch (info) {
            case 皇家騎士團:
            case 米哈逸:
                skin = 10;
                checkSkin = true;
                break;
            case 狂狼勇士:
                skin = 11;
                checkSkin = true;
                break;
            case 精靈遊俠:
                skin = 12;
                checkSkin = true;
                break;
            case 惡魔:
                checkSkin = checkSkin || skin == 13;
                break;
        }

        if (!checkSkin) {
            return;
        }

        Map<CharCreateItemFlag, Integer> equipIds = new LinkedHashMap<>();

        int i = 0;
        for (CharCreateItemFlag itemFlag : CharCreateItemFlag.values()) {
            if (itemFlag.check(info.getFlag())) {
                int itemId = items.get(i++);
                equipIds.put(itemFlag, itemId);
            } else {
                equipIds.put(itemFlag, 0);
            }
        }
        Account account = getClient().getAccount();

        Character chr = Character
                .getDefault(account.getId(),
                        charName, keyMapType, eventNewCharSaleJob,
                        info.getId(), info.getSubJob(), info.getMap(), gender, skin, equipIds);


        if (info == CharCreateInfo.精靈遊俠) {
            chr.getAvatarData().getAvatarLook().setDrawElfEar(true);
        } else if (info == CharCreateInfo.神之子) {
            chr.getAvatarData().setZeroAvatarLook(chr.getAvatarData().getAvatarLook().deepCopy());
            chr.getAvatarData().getAvatarLook().getHairEquips().remove(new Integer(1562000));
            chr.getAvatarData().getZeroAvatarLook().getHairEquips().remove(new Integer(1572000));
            chr.getAvatarData().getZeroAvatarLook().setWeaponId(1562000);
            chr.getAvatarData().getZeroAvatarLook().setGender(1);
            chr.getAvatarData().getZeroAvatarLook().setSkin(skin);
            chr.getAvatarData().getZeroAvatarLook().setFace(equipIds.get(CharCreateItemFlag.臉型));
            chr.getAvatarData().getZeroAvatarLook().setHair(equipIds.get(CharCreateItemFlag.髮型));
            chr.getAvatarData().getZeroAvatarLook().setZeroBetaLook(true);
            chr.getAvatarData().getCharacterStat().setLevel(100);
            chr.getAvatarData().getCharacterStat().setStr(300); //TODO give lv 100 zero proper stats
        }

        chr.setFuncKeyMap(FuncKeyMap.getDefaultMapping());

        DatabaseFactory.getInstance().saveToDB(chr);

        account.addCharacter(chr);

        DatabaseFactory.getInstance().saveToDB(account);

        chr.setCharacterPos(account.getCharacters().size());

        CharacterStat stat = chr.getAvatarData().getCharacterStat();
        stat.setCharacterId(chr.getId());
        stat.setCharacterIdForLog(chr.getId());

        DatabaseFactory.getInstance().saveToDB(chr);

        for (int itemID : chr.getAvatarData().getAvatarLook().getHairEquips()) {
            Item item = ItemData.getInstance().getEquipFromTemplate(itemID);
            if (item != null && item.getItemId() >= 1000000) {
                item.setBagIndex(ItemConstants.getBodyPartFromItem(
                        item.getItemId(), chr.getAvatarData().getAvatarLook().getGender()));
                chr.addItemToInventory(EQUIPPED, item, true);
            }
        }

        if (info == CharCreateInfo.神之子) {
            Equip equip = ItemData.getInstance().getEquipFromTemplate(1562000);
            equip.setBagIndex(ItemConstants.getBodyPartFromItem(
                    equip.getItemId(), chr.getAvatarData().getAvatarLook().getGender()));
            chr.addItemToInventory(EQUIPPED, equip, true);
        }
        DatabaseFactory.getInstance().saveToDB(chr.getInventoryByType(EQUIPPED));
        getClient().write(new LP_CreateNewCharacterResult(LoginResultCode.LoginSuccess, chr));

    }
}
