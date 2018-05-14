package com.msemu.world.data;

import com.msemu.commons.data.loader.dat.MonsterBookDatLoader;
import com.msemu.commons.data.templates.MonsterBook;
import com.msemu.commons.database.DatabaseFactory;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.core.startup.StartupComponent;
import com.msemu.world.client.field.lifes.DropInfo;
import com.msemu.world.constants.ItemConstants;
import lombok.Getter;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/13.
 */
@Reloadable(name = "drop", group = "all")
@StartupComponent("Data")
public class DropData implements IReloadable {
    private static final Logger log = LoggerFactory.getLogger(QuestData.class);

    @Getter
    private final Map<Integer, List<DropInfo>> mobDrops = new HashMap<>();
    @Getter
    private final Map<Integer, List<DropInfo>> reactorDrops = new HashMap<>();
    @Getter
    private final Map<Integer, List<DropInfo>> globalDrops = new HashMap<>();

    private static final AtomicReference<DropData> instance = new AtomicReference<>();

    public static DropData getInstance() {
        DropData value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new DropData();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    public DropData() {
        load();
    }

    private void loadDBDrops() {
        Session session = DatabaseFactory.getInstance().getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<DropInfo> query = builder.createQuery(DropInfo.class);
        Root<DropInfo> root = query.from(DropInfo.class);
        query.select(root);
        List<DropInfo> dropInfos = session.createQuery(query).getResultList();
        dropInfos.forEach(dropInfo -> {
            if (!mobDrops.containsKey(dropInfo.getMobID())) {
                mobDrops.put(dropInfo.getMobID(), new ArrayList<>());
            }
            mobDrops.get(dropInfo.getMobID()).add(dropInfo);
        });
    }

    public void loadMonsterBookDrops() {
        List<MonsterBook> monsterBooks = new MonsterBookDatLoader().load(null);
        monsterBooks.forEach(mb -> {
            mb.getRewards().forEach(itemID -> {
                int cate = itemID / 10000;
                if (cate == 238) { // 怪物卡
                    return;
                } else if (cate == 243) {// 破損的皇家騎士團之書  ?
                    return;
                } else if (cate == 399) { // 字母
                    return;
                }
                DropInfo dropInfo = new DropInfo();
                dropInfo.setMobID(mb.getMobTemplateID());
                dropInfo.setItemID(itemID);
                dropInfo.setChance(ItemConstants.getItemDropChance(itemID));
                dropInfo.setMaxQuantity(1);
                dropInfo.setMinQuantity(1);
                dropInfo.setQuestReq(0);
                if (!mobDrops.containsKey(dropInfo.getMobID())) {
                    mobDrops.put(dropInfo.getMobID(), new ArrayList<>());
                }
                mobDrops.get(dropInfo.getMobID()).add(dropInfo);
            });
        });
    }



    public void load() {
        loadDBDrops();
        loadMonsterBookDrops();
        log.info("{} drops laoded", this.mobDrops.size());
    }

    public List<DropInfo> getDropsInfoByMobID(int mobID) {
        if (!mobDrops.containsKey(mobID)) {
            mobDrops.put(mobID, new ArrayList<>());
        }
        return mobDrops.get(mobID);
    }

    public void clear() {
        this.mobDrops.clear();
        this.reactorDrops.clear();
        this.globalDrops.clear();
    }

    @Override
    public void reload() {
        clear();
        load();
    }
}