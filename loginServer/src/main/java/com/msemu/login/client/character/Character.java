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

package com.msemu.login.client.character;

import com.msemu.commons.data.enums.InvType;
import com.msemu.commons.database.DatabaseFactory;
import com.msemu.commons.database.Schema;
import com.msemu.login.client.character.items.Equip;
import com.msemu.login.client.character.items.Item;
import com.msemu.login.client.character.quest.QuestManager;
import com.msemu.login.client.character.skill.Skill;
import com.msemu.login.constants.ItemConstants;
import com.msemu.login.constants.MapleJob;
import com.msemu.login.data.ItemData;
import com.msemu.login.enums.BodyPart;
import com.msemu.login.enums.CharCreateItemFlag;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Weber on 2018/4/14.
 */
@Schema
@Entity
@Table(name = "characters")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private int id;

    @Column(name = "accId")
    @Getter
    @Setter
    private int accId;


    @JoinColumn(name = "equippedInventory")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private Inventory equippedInventory = new Inventory(InvType.EQUIPPED, 50);

    @JoinColumn(name = "equipInventory")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private Inventory equipInventory = new Inventory(InvType.EQUIP, 50);

    @JoinColumn(name = "consumeInventory")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private Inventory consumeInventory = new Inventory(InvType.CONSUME, 50);

    @JoinColumn(name = "etcInventory")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private Inventory etcInventory = new Inventory(InvType.ETC, 50);

    @JoinColumn(name = "installInventory")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private Inventory installInventory = new Inventory(InvType.INSTALL, 50);

    @JoinColumn(name = "cashInventory")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private Inventory cashInventory = new Inventory(InvType.CASH, 50);

    @JoinColumn(name = "avatarData")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private AvatarData avatarData;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private FuncKeyMap funcKeyMap;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "charId")
    @Getter
    @Setter
    private List<Skill> skills;

    @JoinColumn(name = "questManager")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private QuestManager questManager;

    @Getter
    @Setter
    @Column(name = "characterPos")
    private int characterPos;


    // TODO:
    @Transient
    @Getter
    @Setter
    private Ranking ranking = null;

    public Character() {
        avatarData = new AvatarData();
        avatarData.setAvatarLook(new AvatarLook());
        questManager = new QuestManager(this);

    }

    public static Character getDefault(int accId, String charName, int keyMapType, int eventNewCharSaleJob, int jobId, int subJob, long posMap, byte gender, byte skin, Map<CharCreateItemFlag, Integer> creatData) {
        Character newChar = new Character();
        AvatarData avatarData = newChar.getAvatarData();
        AvatarLook look = avatarData.getAvatarLook();
        CharacterStat stat = new CharacterStat(charName, jobId);
        newChar.getAvatarData().setCharacterStat(stat);
        newChar.setAccId(accId);

        look.setGender(gender);
        look.setSkin(skin);

        if (MapleJob.is傑諾(jobId)) {
            look.setXenonDefFaceAcc(creatData.get(CharCreateItemFlag.臉飾));
        } else if (MapleJob.is惡魔(jobId)) {
            look.setDemonSlayerDefFaceAcc(creatData.get(CharCreateItemFlag.臉飾));
        } else if (MapleJob.is幻獸師(jobId)) {
            look.setBeastTamerDefFaceAcc(creatData.get(CharCreateItemFlag.臉飾));
        }

        look.setEars(creatData.get(CharCreateItemFlag.耳朵));
        look.setFace(creatData.get(CharCreateItemFlag.臉型));
        look.setHair(creatData.get(CharCreateItemFlag.髮型));
        if (MapleJob.is傑諾(jobId)) {
            int hair = look.getHair();
            if (hair == 30000)
                look.setHair(36137);
            else if (hair == 31002)
                look.setHair(37467);
        }

        DatabaseFactory.getInstance().saveToDB(newChar);

        List<Integer> hairEquips = new ArrayList<>();

        creatData.forEach((flag, itemId) -> {
            Equip equip = ItemData.getInstance().getEquipFromTemplate(itemId);
            if (equip == null)
                return;
            hairEquips.add(itemId);
            if (equip.getISlot().equals("Wp")) {
                {
                    if (!equip.isCash()) {
                        look.setWeaponId(equip.getItemId());
                    } else {
                        look.setWeaponStickerId(equip.getItemId());
                    }
                }
            }
        });

        look.setHairEquips(hairEquips);

        stat.setSubJob(subJob);
        stat.setMoney(0);
        stat.setLevel(1);
        stat.setAp(0);
        stat.setSp(0);
        stat.setPop(0);
        stat.setStr(12);
        stat.setDex(5);
        stat.setInte(4);
        stat.setLuk(0);
        stat.setHp(50);
        stat.setMaxHp(50);
        stat.setMp(5);
        stat.setMaxMp(5);
        stat.setGachExp(0);
        stat.setPosMap(posMap);
        stat.setGender(gender);
        stat.setFace(creatData.get(CharCreateItemFlag.臉型));
        stat.setHair(creatData.get(CharCreateItemFlag.髮型));

        return newChar;
    }

    public static boolean isNameExists(String charName) {
        Session session = DatabaseFactory.getInstance().getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<CharacterStat> query = builder.createQuery(CharacterStat.class);
        Root<CharacterStat> root = query.from(CharacterStat.class);
        query.select(root);
        query.where(
                builder.equal(builder.lower(
                        root.get("name")), charName.toLowerCase()));
        List<CharacterStat> result = session.createQuery(query).getResultList();
        session.clear();
        return result.size() > 0;
    }

    public static Character getById(int userId) {
        Character chr;
        Session session = DatabaseFactory.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        chr = session.get(Character.class, userId);
        transaction.commit();
        session.close();
        return chr;
    }

    public Inventory getInventoryByType(InvType invType) {
        switch (invType) {
            case EQUIPPED:
                return getEquippedInventory();
            case EQUIP:
                return getEquipInventory();
            case CONSUME:
                return getConsumeInventory();
            case ETC:
                return getEtcInventory();
            case INSTALL:
                return getInstallInventory();
            case CASH:
                return getCashInventory();
            default:
                return null;
        }
    }

    public void addItemToInventory(InvType type, Item item, boolean hasCorrectBagIndex) {
        Inventory inventory = getInventoryByType(type);
        if (inventory != null) {
            Item existingItem = inventory.getItemByItemID(item.getItemId());
            if (existingItem != null && existingItem.getInvType().isStackable()) {
                existingItem.addQuantity(item.getQuantity());
            } else {
                item.setInventoryId(inventory.getId());
                if (!hasCorrectBagIndex) {
                    item.setBagIndex(inventory.getFirstOpenSlot());
                }
                inventory.addItem(item);
                if (item.getId() == 0) {
                    DatabaseFactory.getInstance().saveToDB(item);
                }
            }
        }
    }

    public void addItemToInventory(Item item) {
        addItemToInventory(item.getInvType(), item, false);
    }
}
