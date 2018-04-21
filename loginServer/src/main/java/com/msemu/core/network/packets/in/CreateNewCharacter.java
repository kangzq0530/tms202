package com.msemu.core.network.packets.in;

import com.msemu.commons.database.DatabaseFactory;
import com.msemu.commons.network.packets.InPacket;
import com.msemu.core.network.LoginClient;
import com.msemu.core.network.packets.out.Login.CreateNewCharacterResult;
import com.msemu.login.client.Account;
import com.msemu.login.client.character.Character;
import com.msemu.login.client.character.CharacterStat;
import com.msemu.login.client.character.FuncKeyMap;
import com.msemu.login.client.character.items.Equip;
import com.msemu.login.data.ItemData;
import com.msemu.login.data.StringData;
import com.msemu.login.enums.CharCreateInfo;
import com.msemu.login.enums.CharCreateItemFlag;
import com.msemu.login.enums.LoginResultType;
import com.msemu.world.constants.ItemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.msemu.commons.enums.InvType.EQUIPPED;

/**
 * Created by Weber on 2018/4/19.
 */
public class CreateNewCharacter extends InPacket<LoginClient> {

    private static final Logger log = LoggerFactory.getLogger(CreateNewCharacter.class);

    private String charName;
    private int keyMapType;
    private int jobType;
    private int jobSubCategory;
    private int eventNewCharSaleJob;
    private byte gender;
    private byte skin;
    private List<Integer> items;


    public CreateNewCharacter(short opcode) {
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

        boolean used = Character.isNameExists(charName) ||
                StringData.getInstance().getForbiddenNames().contains(charName);

        if (used)
            return;

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

        account.addCharacter(chr);

        DatabaseFactory.getInstance().saveToDB(account);

        chr.setCharacterPos(account.getCharacters().size());

        CharacterStat stat = chr.getAvatarData().getCharacterStat();
        stat.setCharacterId(chr.getId());
        stat.setCharacterIdForLog(chr.getId());

        DatabaseFactory.getInstance().saveToDB(chr);

        for(int itemID : chr.getAvatarData().getAvatarLook().getHairEquips()) {
            Equip equip = ItemData.getInstance().getEquipFromTemplate(itemID);
            if(equip != null && equip.getItemId() >= 1000000) {
                equip.setBagIndex(ItemConstants.getBodyPartFromItem(
                        equip.getItemId(), chr.getAvatarData().getAvatarLook().getGender()));
                chr.addItemToInventory(EQUIPPED, equip, true);
            }
        }

        if (info == CharCreateInfo.神之子) {
            Equip equip = ItemData.getInstance().getEquipFromTemplate(1562000);
            equip.setBagIndex(ItemConstants.getBodyPartFromItem(
                    equip.getItemId(), chr.getAvatarData().getAvatarLook().getGender()));
            chr.addItemToInventory(EQUIPPED, equip, true);
        }
        DatabaseFactory.getInstance().saveToDB(chr.getInventoryByType(EQUIPPED));


        getClient().write(new CreateNewCharacterResult(LoginResultType.LoginSuccess, chr));

    }
}
