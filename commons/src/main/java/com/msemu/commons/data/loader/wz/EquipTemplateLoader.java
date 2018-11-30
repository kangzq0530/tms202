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
import java.util.stream.Collectors;

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
        final EquipTemplate template = new EquipTemplate();
        final int itemId = Integer.parseInt(itemImage.getName().replace(".img", ""));

        template.setInvType(InvType.EQUIP);
        template.setItemId(itemId);

        WzSubProperty infoNode = (WzSubProperty) itemImage.getFromPath("info");

        if (infoNode == null)
            return template;

        for (WzImageProperty prop : infoNode.getProperties()) {
            String propName = prop.getName().toLowerCase();

            if (propName.equalsIgnoreCase("islot")) {
                template.setISlot(prop.getString());
            } else if (propName.equalsIgnoreCase("vslot")) {
                template.setVSlot(prop.getString());
            } else if (propName.equalsIgnoreCase("reqJob")) {
                template.setRJob(prop.getShort());
            } else if (propName.equalsIgnoreCase("reqLevel")) {
                template.setRLevel(prop.getShort());
            } else if (propName.equalsIgnoreCase("reqSTR")) {
                template.setRStr(prop.getShort());
            } else if (propName.equalsIgnoreCase("reqDEX")) {
                template.setRDex(prop.getShort());
            } else if (propName.equalsIgnoreCase("reqINT")) {
                template.setRInt(prop.getShort());
            } else if (propName.equalsIgnoreCase("reqLUK")) {
                template.setRLuk(prop.getShort());
            } else if (propName.equalsIgnoreCase("incSTR")) {
                template.setIStr(prop.getShort());
            } else if (propName.equalsIgnoreCase("incDEX")) {
                template.setIDex(prop.getShort());
            } else if (propName.equalsIgnoreCase("incINT")) {
                template.setIInt(prop.getShort());
            } else if (propName.equalsIgnoreCase("incLUK")) {
                template.setILuk(prop.getShort());
            } else if (propName.equalsIgnoreCase("incPDD")) {
                template.setIPDD(prop.getShort());
            } else if (propName.equalsIgnoreCase("incMDD")) {
                template.setIMDD(prop.getShort());
            } else if (propName.equalsIgnoreCase("incMHP")) {
                template.setIMaxHp(prop.getShort());
            } else if (propName.equalsIgnoreCase("incMHPr")) {
                template.setIMaxHpR(prop.getShort());
            } else if (propName.equalsIgnoreCase("incMMP")) {
                template.setIMaxMp(prop.getShort());
            } else if (propName.equalsIgnoreCase("incMMPr")) {
                template.setIMaxMpR(prop.getShort());
            } else if (propName.equalsIgnoreCase("incPAD")) {
                template.setIPad(prop.getShort());
            } else if (propName.equalsIgnoreCase("incMAD")) {
                template.setIMad(prop.getShort());
            } else if (propName.equalsIgnoreCase("incPAD")) {
                template.setIPad(prop.getShort());
            } else if (propName.equalsIgnoreCase("incEVA")) {
                template.setIEva(prop.getShort());
            } else if (propName.equalsIgnoreCase("incACC")) {
                template.setIAcc(prop.getShort());
            } else if (propName.equalsIgnoreCase("incSpeed")) {
                template.setISpeed(prop.getShort());
            } else if (propName.equalsIgnoreCase("incJump")) {
                template.setIJump(prop.getShort());
            } else if (propName.equalsIgnoreCase("damR")) {
                template.setDamR(prop.getShort());
            } else if (propName.equalsIgnoreCase("statR")) {
                template.setStatR(prop.getShort());
            } else if (propName.equalsIgnoreCase("tuc")) {
                template.setTuc(prop.getShort());
            } else if (propName.equalsIgnoreCase("setItemID")) {
                template.setSetItemID(prop.getInt());
            } else if (propName.equalsIgnoreCase("price")) {
                template.setPrice(prop.getInt());
            } else if (propName.equalsIgnoreCase("attackSpeed")) {
                template.setAttackSpeed(prop.getInt());
            } else if (propName.equalsIgnoreCase("cash")) {
                template.setCash(prop.getInt() > 0);
            } else if (propName.equalsIgnoreCase("expireOnLogout")) {
                template.setExpireOnLogout(prop.getInt() > 0);
            } else if (propName.equalsIgnoreCase("exItem")) {
                template.setExItem(prop.getInt() > 0);
            } else if (propName.equalsIgnoreCase("superiorEqp")) {
                template.setSuperiorEqp(prop.getInt() > 0);
            } else if (propName.equalsIgnoreCase("notSale")) {
                template.setNotSale(prop.getInt() > 0);
            } else if (propName.equalsIgnoreCase("only")) {
                template.setAttackSpeed(prop.getInt());
            } else if (propName.equalsIgnoreCase("tradeBlock")) {
                template.setTradeBlock(prop.getInt() > 0);
            } else if (propName.equalsIgnoreCase("tradeAvailable")) {
                template.setTradeAvailable(prop.getInt() > 0);
            } else if (propName.equalsIgnoreCase("equipTradeBlock")) {
                template.setEquipTradeBlock(prop.getInt() > 0);
            } else if (propName.equalsIgnoreCase("fixedPotential")) {
                template.setFixedPotential(prop.getInt() > 0);
            } else if (propName.equalsIgnoreCase("fixedGrade")) {
                template.setFixedGrade(prop.getInt());
            } else if (propName.equalsIgnoreCase("specialGrade")) {
                template.setSpecialGrade(prop.getInt());
            } else if (propName.equalsIgnoreCase("attackSpeed")) {
                template.setAttackSpeed(prop.getInt());
            } else if (propName.equalsIgnoreCase("option")) {
                WzSubProperty optionNode = (WzSubProperty) prop;
                for (WzSubProperty optionsNode : optionNode.getProperties()
                        .stream().map(p -> (WzSubProperty)p).collect(Collectors.toList()))
                {
                    for(WzImageProperty eachOptionNode : optionsNode.getProperties())
                    {
                        EquipOption option = new EquipOption();
                        if (eachOptionNode.getName().equalsIgnoreCase("option")) {
                            option.setOption(eachOptionNode.getInt());
                        } else if (eachOptionNode.getName().equalsIgnoreCase("level")) {
                            option.setLevel(eachOptionNode.getInt());
                        }
                        template.getOptions().put(optionsNode.getInt(), option);
                    }
                }
                while (template.getOptions().size() < 7) {
                    EquipOption empty = new EquipOption();
                    empty.setLevel(0);
                    empty.setOption(0);
                    template.getOptions().put(template.getOptions().size(), empty);
                }
            }
        }
        return template;
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
