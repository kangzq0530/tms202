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

package com.msemu.commons.data.loader.wz;

import com.msemu.commons.data.enums.InvType;
import com.msemu.commons.data.loader.WzDataLoader;
import com.msemu.commons.data.loader.dat.EquipTemplateDatLoader;
import com.msemu.commons.data.templates.EquipOption;
import com.msemu.commons.data.templates.EquipTemplate;
import com.msemu.commons.wz.WzDirectory;
import com.msemu.commons.wz.WzFile;
import com.msemu.commons.wz.WzImage;
import com.msemu.commons.wz.WzImageProperty;
import com.msemu.commons.wz.properties.WzSubProperty;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Weber on 2018/4/21.
 */
public class EquipTemplateLoader extends WzDataLoader<Map<Integer, EquipTemplate>> {

    private static final Logger log = LoggerFactory.getLogger(EquipTemplateLoader.class);
    @Getter
    Map<Integer, EquipTemplate> data = new HashMap<>();

    public EquipTemplateLoader(WzManager wzManager) {
        super(wzManager);
    }

    private EquipTemplate importItem(WzImage itemImage) {

        EquipTemplate equip = new EquipTemplate();
        equip.setInvType(InvType.EQUIP);

        int itemId = Integer.parseInt(itemImage.getName().replace(".img", ""));
        equip.setItemId(itemId);

        WzSubProperty info = (WzSubProperty) itemImage.getFromPath("info");

        if (info == null)
            return equip;

        info.getProperties().forEach(prop -> {

            String propName = prop.getName();

            if (propName.equalsIgnoreCase("islot")) {
                equip.setISlot(prop.getString());
            } else if (propName.equalsIgnoreCase("vslot")) {
                equip.setVSlot(prop.getString());
            } else if (propName.equalsIgnoreCase("reqJob")) {
                equip.setRJob(prop.getShort());
            } else if (propName.equalsIgnoreCase("reqLevel")) {
                equip.setRLevel(prop.getShort());
            } else if (propName.equalsIgnoreCase("reqSTR")) {
                equip.setRStr(prop.getShort());
            } else if (propName.equalsIgnoreCase("reqDEX")) {
                equip.setRDex(prop.getShort());
            } else if (propName.equalsIgnoreCase("reqINT")) {
                equip.setRInt(prop.getShort());
            } else if (propName.equalsIgnoreCase("reqLUK")) {
                equip.setRLuk(prop.getShort());
            } else if (propName.equalsIgnoreCase("incSTR")) {
                equip.setIStr(prop.getShort());
            } else if (propName.equalsIgnoreCase("incDEX")) {
                equip.setIDex(prop.getShort());
            } else if (propName.equalsIgnoreCase("incINT")) {
                equip.setIInt(prop.getShort());
            } else if (propName.equalsIgnoreCase("incLUK")) {
                equip.setILuk(prop.getShort());
            } else if (propName.equalsIgnoreCase("incPDD")) {
                equip.setIPDD(prop.getShort());
            } else if (propName.equalsIgnoreCase("incMDD")) {
                equip.setIMDD(prop.getShort());
            } else if (propName.equalsIgnoreCase("incMHP")) {
                equip.setIMaxHp(prop.getShort());
            } else if (propName.equalsIgnoreCase("incMHPr")) {
                equip.setIMaxHpR(prop.getShort());
            } else if (propName.equalsIgnoreCase("incMMP")) {
                equip.setIMaxMp(prop.getShort());
            } else if (propName.equalsIgnoreCase("incMMPr")) {
                equip.setIMaxMpR(prop.getShort());
            } else if (propName.equalsIgnoreCase("incPAD")) {
                equip.setIPad(prop.getShort());
            } else if (propName.equalsIgnoreCase("incMAD")) {
                equip.setIMad(prop.getShort());
            } else if (propName.equalsIgnoreCase("incPAD")) {
                equip.setIPad(prop.getShort());
            } else if (propName.equalsIgnoreCase("incEVA")) {
                equip.setIEva(prop.getShort());
            } else if (propName.equalsIgnoreCase("incACC")) {
                equip.setIAcc(prop.getShort());
            } else if (propName.equalsIgnoreCase("incSpeed")) {
                equip.setISpeed(prop.getShort());
            } else if (propName.equalsIgnoreCase("incJump")) {
                equip.setIJump(prop.getShort());
            } else if (propName.equalsIgnoreCase("damR")) {
                equip.setDamR(prop.getShort());
            } else if (propName.equalsIgnoreCase("statR")) {
                equip.setStatR(prop.getShort());
            } else if (propName.equalsIgnoreCase("tuc")) {
                equip.setTuc(prop.getShort());
            } else if (propName.equalsIgnoreCase("setItemID")) {
                equip.setSetItemID(prop.getInt());
            } else if (propName.equalsIgnoreCase("price")) {
                equip.setPrice(prop.getInt());
            } else if (propName.equalsIgnoreCase("attackSpeed")) {
                equip.setAttackSpeed(prop.getInt());
            } else if (propName.equalsIgnoreCase("cash")) {
                equip.setCash(prop.getInt() > 0);
            } else if (propName.equalsIgnoreCase("expireOnLogout")) {
                equip.setExpireOnLogout(prop.getInt() > 0);
            } else if (propName.equalsIgnoreCase("exItem")) {
                equip.setExItem(prop.getInt() > 0);
            } else if (propName.equalsIgnoreCase("notSale")) {
                equip.setNotSale(prop.getInt() > 0);
            } else if (propName.equalsIgnoreCase("only")) {
                equip.setAttackSpeed(prop.getInt());
            } else if (propName.equalsIgnoreCase("tradeBlock")) {
                equip.setTradeBlock(prop.getInt() > 0);
            } else if (propName.equalsIgnoreCase("equipTradeBlock")) {
                equip.setEquipTradeBlock(prop.getInt() > 0);
            } else if (propName.equalsIgnoreCase("fixedPotential")) {
                equip.setFixedPotential(prop.getInt() > 0);
            } else if (propName.equalsIgnoreCase("fixedGrade")) {
                equip.setFixedGrade(prop.getInt());
            } else if (propName.equalsIgnoreCase("specialGrade")) {
                equip.setSpecialGrade(prop.getInt());
            } else if (propName.equalsIgnoreCase("attackSpeed")) {
                equip.setAttackSpeed(prop.getInt());
            } else if (propName.equalsIgnoreCase("option")) {
                WzSubProperty optsProp = (WzSubProperty) prop;
                optsProp.getProperties().stream().map(p -> (WzSubProperty) p).forEach(indexProp -> {
                    indexProp.getProperties().forEach(optProp -> {
                        EquipOption option = new EquipOption();
                        if (optProp.getName().equalsIgnoreCase("option")) {
                            option.setOption(optProp.getInt());
                        } else if (optProp.getName().equalsIgnoreCase("level")) {
                            option.setLevel(optProp.getInt());
                        } else {
                            log.warn("new equip option : {}", optProp.getName());
                        }
                        equip.getOptions().put(indexProp.getInt(), option);
                    });
                });
                while (equip.getOptions().size() < 7) {
                    EquipOption empty = new EquipOption();
                    empty.setLevel(0);
                    empty.setOption(0);
                    equip.getOptions().put(equip.getOptions().size(), empty);
                }
            }
            // options
        });


        return equip;
    }

    private List<EquipTemplate> importCates(WzDirectory cateDir) {
        List<EquipTemplate> data = new ArrayList<>();
        cateDir.getImages()
                .stream()
                .filter(image -> !image.getName().equals("CommonFaceCN.img"))
                .forEach(itemImage -> data.add(importItem(itemImage)));
        return data;
    }


    @Override
    public void load() {
        data.clear();
        WzFile wzFile = getWzManager().getWz(WzManager.CHARACTER);
        WzDirectory wzDir = wzFile.getWzDirectory();
        wzDir.getDirs()
                .stream()
                .filter(dir -> !dir.getName().equals("Afterimage"))
                .forEach(dir -> importCates(dir).forEach(equip -> data.put(equip.getItemId(), equip)));
        WzFile stringFile = getWzManager().getWz(WzManager.STRING);
        WzImage eqpImg = stringFile.getWzDirectory().getImage("Eqp.img");
        WzSubProperty eqpSub = (WzSubProperty) eqpImg.getFromPath("Eqp");
        eqpSub.getProperties().stream()
                .map(prop -> (WzSubProperty) prop)
                .forEach(cateProp -> {
                    cateProp.getProperties().stream()
                            .map(prop -> (WzSubProperty) prop)
                            .forEach(itemProp -> {
                                final int itemId = Integer.parseInt(itemProp.getName());
                                if (data.containsKey(itemId)) {
                                    WzImageProperty stringProperty =
                                            itemProp.getFromPath("name");
                                    if (stringProperty != null)
                                        data.get(itemId).setName(stringProperty.getString());
                                }
                            });
                });

    }

    @Override
    public void saveToDat() throws IOException {
        if (data.isEmpty())
            load();
        new EquipTemplateDatLoader().saveDat(data);
    }
}
