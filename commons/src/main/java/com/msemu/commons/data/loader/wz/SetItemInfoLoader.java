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

import com.msemu.commons.data.loader.WzDataLoader;
import com.msemu.commons.data.loader.dat.SetItemInfoDatLoader;
import com.msemu.commons.data.templates.SetInfo;
import com.msemu.commons.data.templates.SetItemInfo;
import com.msemu.commons.utils.types.Tuple;
import com.msemu.commons.wz.WzImage;
import com.msemu.commons.wz.properties.WzIntProperty;
import com.msemu.commons.wz.properties.WzSubProperty;
import lombok.Getter;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weber on 2018/5/6.
 */
public class SetItemInfoLoader extends WzDataLoader<Map<Integer, SetItemInfo>> {

    @Getter
    Map<Integer, SetItemInfo> data = new HashMap<>();


    public SetItemInfoLoader(WzManager wzManager) {
        super(wzManager);
    }

    @Override
    public void load() throws IOException {
        WzImage setInfoImg = wzManager.getWz(WzManager.ETC).getWzDirectory().getImage("SetItemInfo.img");

        setInfoImg.getProperties().stream()
                .map(p -> (WzSubProperty) p)
                .forEach(setItemInfoProp -> {
                    SetItemInfo setItemInfo = new SetItemInfo();
                    setItemInfo.setSetItemID(Integer.parseInt(setItemInfoProp.getName()));
                    data.put(setItemInfo.getSetItemID(), setItemInfo);
                    setItemInfoProp.getProperties()
                            .forEach(prop -> {
                                String propName = prop.getName();
                                if (propName.equalsIgnoreCase("setItemName")) {
                                    setItemInfo.setSetItemName(prop.getString());
                                } else if (propName.equalsIgnoreCase("completeCount")) {
                                    setItemInfo.setCompleteCount(prop.getInt());
                                } else if (propName.equalsIgnoreCase("ItemID")) {
                                    if (prop instanceof WzIntProperty) {
                                        setItemInfo.getItemIDs().add(prop.getInt());
                                    } else {
                                        ((WzSubProperty) prop).getProperties()
                                                .forEach(pp -> {
                                                    setItemInfo.getItemIDs().add(pp.getInt());
                                                });
                                    }
                                } else if (propName.equalsIgnoreCase("Effect")) {
                                    ((WzSubProperty) prop).getProperties()
                                            .stream()
                                            .map(pp -> (WzSubProperty) pp)
                                            .forEach(pp -> {
                                                SetInfo setInfo = new SetInfo();
                                                Integer completeCount = Integer.parseInt(pp.getName());
                                                setItemInfo.getEffects().put(completeCount, setInfo);
                                                pp.getProperties()
                                                        .forEach(ppp -> {
                                                            String pppName = ppp.getName();
                                                            if (pppName.equalsIgnoreCase("activeSkill")) {
                                                                pp.getProperties()
                                                                        .forEach(pppp -> {
                                                                            int skillID = 0, level = 1;
                                                                            if (pppp.hasProperty("id")) {
                                                                                skillID = pppp.getFromPath("id").getInt();
                                                                            }
                                                                            if (pppp.hasProperty("level")) {
                                                                                level = pppp.getFromPath("level").getInt();
                                                                            }
                                                                            if (skillID > 0) {
                                                                                setInfo.getActiveSkills().add(new Tuple<>(skillID, level));
                                                                            }
                                                                        });
                                                            } else if (pppName.equalsIgnoreCase("Option")) {
                                                                pp.getProperties()
                                                                        .forEach(pppp -> {
                                                                            int option = 0, level = 1;
                                                                            if (pppp.hasProperty("option")) {
                                                                                option = pppp.getFromPath("option").getInt();
                                                                            }
                                                                            if (pppp.hasProperty("level")) {
                                                                                level = pppp.getFromPath("level").getInt();
                                                                            }
                                                                            if (option > 0) {
                                                                                setInfo.getOptions().add(new Tuple<>(option, level));
                                                                            }
                                                                        });
                                                            } else if (pppName.equalsIgnoreCase("incAllStat")) {
                                                                setInfo.setIncAllStat(ppp.getInt());
                                                            } else if (pppName.equalsIgnoreCase("incSTR")) {
                                                                setInfo.setIncSTR(ppp.getInt());
                                                            } else if (pppName.equalsIgnoreCase("incDEX")) {
                                                                setInfo.setIncDEX(ppp.getInt());
                                                            } else if (pppName.equalsIgnoreCase("incINT")) {
                                                                setInfo.setIncINT(ppp.getInt());
                                                            } else if (pppName.equalsIgnoreCase("incLUK")) {
                                                                setInfo.setIncLUK(ppp.getInt());
                                                            } else if (pppName.equalsIgnoreCase("incMHP")) {
                                                                setInfo.setIncMHP(ppp.getInt());
                                                            } else if (pppName.equalsIgnoreCase("incMMP")) {
                                                                setInfo.setIncMMP(ppp.getInt());
                                                            } else if (pppName.equalsIgnoreCase("incMHPr")) {
                                                                setInfo.setIncMHPr(ppp.getInt());
                                                            } else if (pppName.equalsIgnoreCase("incMMPr")) {
                                                                setInfo.setIncMMPr(ppp.getInt());
                                                            } else if (pppName.equalsIgnoreCase("incACC")) {
                                                                setInfo.setIncACC(ppp.getInt());
                                                            } else if (pppName.equalsIgnoreCase("incEVA")) {
                                                                setInfo.setIncEVA(ppp.getInt());
                                                            } else if (pppName.equalsIgnoreCase("incSpeed")) {
                                                                setInfo.setIncSpeed(ppp.getInt());
                                                            } else if (pppName.equalsIgnoreCase("incJump")) {
                                                                setInfo.setIncJump(ppp.getInt());
                                                            } else if (pppName.equalsIgnoreCase("incPAD")) {
                                                                setInfo.setIncPAD(ppp.getInt());
                                                            } else if (pppName.equalsIgnoreCase("incMAD")) {
                                                                setInfo.setIncMAD(ppp.getInt());
                                                            } else if (pppName.equalsIgnoreCase("incPDD")) {
                                                                setInfo.setIncPDD(ppp.getInt());
                                                            } else if (pppName.equalsIgnoreCase("incMDD")) {
                                                                setInfo.setIncMDD(ppp.getInt());
                                                            } else {
                                                                LoggerFactory.getLogger(SetItemInfoLoader.class)
                                                                        .warn(String.format("SetItemLoader %s prop not load", pppName));
                                                            }
                                                        });

                                            });
                                }

                            });

                });
    }

    @Override
    public void saveToDat() throws IOException {
        if (getData().isEmpty())
            load();
        new SetItemInfoDatLoader().saveDat(getData());
    }
}
