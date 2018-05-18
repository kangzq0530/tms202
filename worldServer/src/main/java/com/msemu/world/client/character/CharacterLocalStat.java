package com.msemu.world.client.character;

import com.msemu.commons.data.templates.SetInfo;
import com.msemu.commons.data.templates.SetItemInfo;
import com.msemu.world.client.character.inventory.items.Equip;
import com.msemu.world.client.character.inventory.items.Item;
import com.msemu.world.client.character.skill.TemporaryStatManager;
import com.msemu.world.constants.MapleJob;
import com.msemu.world.data.ItemData;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/5/7.
 */
public class CharacterLocalStat {

    private Character character;

    @Getter
    private int maxHp, maxMp;
    @Getter
    private int shouldHealHp, shouldHealMp;


    public CharacterLocalStat(Character character) {
        this.character = character;
    }

    public void recalculateLocalStat() {
        AvatarData ad = character.getAvatarData();
        CharacterStat cs = ad.getCharacterStat();
        TemporaryStatManager tsm = character.getTemporaryStatManager();

        int job = character.getJob();
        int baseMaxHp = cs.getMaxHp();
        int baseMaxMp = cs.getMaxMp();
        int incMaxHp = 0, incMaxMp = 0, incMaxHpR = 0, incMaxMpR = 0;
        List<Integer> equippedItemIDs = new ArrayList<>();
        List<Integer> setItemIDs = new ArrayList<>();
        for (Item item : character.getEquippedInventory().getItems()) {
            equippedItemIDs.add(item.getItemId());
            Equip equip = (Equip) item;
            incMaxHp += MapleJob.is惡魔復仇者(job) ? equip.getIMaxHp() / 2 : equip.getIMaxHp();
            incMaxMp += equip.getIMaxMp();
            incMaxHpR += equip.getIMaxHpR();
            incMaxMpR += equip.getIMaxMpR();
        }

        for (Integer setItemID : setItemIDs) {
            SetItemInfo setItemInfo = ItemData.getInstance().getSetItemInfo(setItemID);
            int matchCount = (int) setItemInfo.getItemIDs().stream()
                    .filter(equippedItemIDs::contains).count();
            if (matchCount < setItemInfo.getCompleteCount()) {
                SetInfo effect = setItemInfo.getEffects().get(matchCount);
                incMaxHp += effect.getIncMHP();
                incMaxMp += effect.getIncMMP();
            }
        }


        maxHp = (baseMaxHp + incMaxHp) * (100 + incMaxHpR) / 100;
        maxMp = (baseMaxMp + incMaxMp) * (100 + incMaxMpR) / 100;


        this.shouldHealHp = 10;
    }


}
