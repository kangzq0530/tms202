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

import com.msemu.commons.data.enums.PortalType;
import com.msemu.commons.data.loader.WzDataLoader;
import com.msemu.commons.data.loader.dat.FieldTemplateDatLoader;
import com.msemu.commons.data.templates.field.*;
import com.msemu.commons.utils.types.Position;
import com.msemu.commons.utils.types.Rect;
import com.msemu.commons.utils.types.Tuple;
import com.msemu.commons.wz.WzFile;
import com.msemu.commons.wz.WzImage;
import com.msemu.commons.wz.WzPropertyType;
import com.msemu.commons.wz.properties.WzIntProperty;
import com.msemu.commons.wz.properties.WzSubProperty;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/4/24.
 */
public class FieldTemplateLoader extends WzDataLoader<Map<Integer, FieldTemplate>> {

    @Getter
    private Map<Integer, FieldTemplate> data = new HashMap<>();

    public FieldTemplateLoader(WzManager wzManager) {
        super(wzManager);
    }

    @Override
    public void load() {
        WzFile map1 = wzManager.getWz(WzManager.MAP);
        WzFile map2 = wzManager.getWz(WzManager.MAP2);
        List<WzImage> maps = new ArrayList<>();
        map1.getWzDirectory().getDir("Map").getDirs()
                .forEach(dir -> maps.addAll(dir.getImages()));

        for (WzImage mapImg : maps) {
            importMap(data, mapImg);
        }
        WzImage mapStrImg = wzManager.getWz(WzManager.STRING).getWzDirectory().getImage("Map.img");

        List<WzSubProperty> strs = mapStrImg.getProperties().stream().map(e -> (WzSubProperty) e).collect(Collectors.toList());

        data.forEach((k, v) -> {
            for (WzSubProperty p : strs) {
                if (p.hasProperty(k.toString())) {
                    if (p.hasProperty(k.toString() + "/mapName")) {
                        v.setName(p.getFromPath(k.toString() + "/mapName").getString());
                    }
                    if (p.hasProperty(k.toString() + "/streetName")) {
                        v.setStreetName(p.getFromPath(k.toString() + "/streetName").getString());
                    }
                    break;
                }
            }
        });
    }

    private void importMap(Map<Integer, FieldTemplate> data, WzImage mapImg) {

        FieldTemplate template = new FieldTemplate();
        int mapId = Integer.parseInt(mapImg.getName().replace(".img", ""));
        data.put(mapId, template);

        template.setId(mapId);

        mapImg.getProperties().stream().map(e -> (WzSubProperty) e)
                .forEach(prop -> {
                    String propName = prop.getName();
                    if (propName.equalsIgnoreCase("info")) {
                        if (prop.hasProperty("mobRate"))
                            template.setMobRate(prop.get("mobRate").getInt());
                        if (prop.hasProperty("returnMap"))
                            template.setReturnMap(prop.get("returnMap").getInt());
                        if (prop.hasProperty("VRTop")) {
                            Rect rect = new Rect();
                            rect.setLeft(prop.get("VRTop").getInt());
                            rect.setLeft(prop.get("VRLeft").getInt());
                            rect.setLeft(prop.get("VRBottom").getInt());
                            rect.setLeft(prop.get("VRRight").getInt());
                            template.setRect(rect);
                        }
                        if (prop.hasProperty("timeLimit")) {
                            template.setTimeLimit(prop.get("timeLimit").getInt());
                        }
                        if (prop.hasProperty("forcedReturn")) {
                            template.setForcedReturn(prop.get("forcedReturn").getInt());
                        }
                        if (prop.hasProperty("fieldLimit")) {
                            template.setFieldLimit(prop.get("fieldLimit").getInt());
                        }
                        if (prop.hasProperty("lvLimit")) {
                            template.setLvLimit(prop.get("lvLimit").getInt());
                        }
                        if (prop.hasProperty("consumeItemCoolTime")) {
                            template.setConsumeItemCoolTime(prop.get("consumeItemCoolTime").getInt());
                        }
                        if (prop.hasProperty("link")) {
                            template.setLink(Integer.parseInt(prop.get("link").getString()));
                        }
                        if (prop.hasProperty("bossMobID")) {
                            template.setLink(Integer.parseInt(prop.get("bossMobID").getString()));
                        }
                        if (prop.hasProperty("createMobInterval")) {
                            template.setCreateMobInterval(prop.get("createMobInterval").getInt());
                        }
                        if (prop.hasProperty("lvForceMove")) {
                            template.setLvForceMove(prop.get("lvForceMove").getInt());
                        }
                        if (prop.hasProperty("isNeedSkillForFly")) {
                            template.setNeedSkillForFly(prop.get("isNeedSkillForFly").getInt() > 0);
                        }
                        if (prop.hasProperty("swim")) {
                            template.setSwim(prop.get("swim").getInt() > 0);
                        }
                        if (prop.hasProperty("fly")) {
                            template.setFly(prop.get("fly").getInt() > 0);
                        }
                        if (prop.hasProperty("reactorShuffle")) {
                            template.setReactorShuffle(prop.get("reactorShuffle").getInt() > 0);
                        }
                        if (prop.hasProperty("expeditionOnly")) {
                            template.setExpeditionOnly(prop.get("expeditionOnly").getInt() > 0);
                        }
                        if (prop.hasProperty("partyOnly")) {
                            template.setPartyOnly(prop.get("partyOnly").getInt() > 0);
                        }
                        if (prop.hasProperty("onUserEnter")) {
                            template.setOnUserEnter(prop.get("onUserEnter").getString());
                        }
                        if (prop.hasProperty("onFirstUserEnter")) {
                            template.setOnFirstUserEnter(prop.get("onFirstUserEnter").getString());
                        }
                        if (prop.hasProperty("partyBonusR")) {
                            template.setPartyBonusR(prop.get("partyBonusR").getInt());
                        }
                    } else if (propName.equalsIgnoreCase("foothold")) {
                        Position lBound = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
                        Position uBound = new Position(Integer.MIN_VALUE, Integer.MIN_VALUE);
                        ;
                        prop.getProperties().stream()
                                .map(e -> (WzSubProperty) e)
                                .forEach(layerProp -> {
                                    final int layerID = Integer.parseInt(layerProp.getName());
                                    layerProp.getProperties().stream()
                                            .map(e -> (WzSubProperty) e)
                                            .forEach(groupProp -> {
                                                final int groupID = Integer.parseInt(groupProp.getName());
                                                groupProp.getProperties().stream()
                                                        .map(e -> (WzSubProperty) e)
                                                        .forEach(fieldProp -> {
                                                            final int fieldID = Integer.parseInt(fieldProp.getName());
                                                            Foothold foothold = new Foothold(
                                                                    fieldID, layerID, groupID
                                                            );
                                                            fieldProp.getProperties().forEach(p -> {
                                                                String pName = p.getName();
                                                                if (pName.equalsIgnoreCase("x1")) {
                                                                    foothold.setX1(p.getInt());
                                                                } else if (pName.equalsIgnoreCase("x2")) {
                                                                    foothold.setX2(p.getInt());
                                                                } else if (pName.equalsIgnoreCase("y1")) {
                                                                    foothold.setY1(p.getInt());
                                                                } else if (pName.equalsIgnoreCase("y2")) {
                                                                    foothold.setY2(p.getInt());
                                                                } else if (pName.equalsIgnoreCase("next")) {
                                                                    foothold.setNext(p.getInt());
                                                                } else if (pName.equalsIgnoreCase("prev")) {
                                                                    foothold.setPrev(p.getInt());
                                                                } else if (pName.equalsIgnoreCase("force")) {
                                                                    foothold.setForce(p.getInt());
                                                                } else if (pName.equalsIgnoreCase("forbidFallDown")) {
                                                                    foothold.setForbidFallDown(p.getInt() > 0);
                                                                }
                                                            });
                                                            if (foothold.getX1() < lBound.getX())
                                                                lBound.setX(foothold.getX1());
                                                            if (foothold.getX2() > uBound.getX())
                                                                uBound.setX(foothold.getX2());
                                                            if (foothold.getY2() < lBound.getY())
                                                                lBound.setY(foothold.getY1());
                                                            if (foothold.getY1() > uBound.getY())
                                                                uBound.setY(foothold.getY2());
                                                            template.getFootholds().add(foothold);
                                                        });
                                            });
                                });
                        template.setRect(new Rect((int) lBound.getX(), (int) uBound.getY(), (int) uBound.getX(), (int) lBound.getY()));
                    } else if (propName.equalsIgnoreCase("life")) {

                        boolean isCategory = prop.hasProperty("isCategory") && prop.get("isCategory").getInt() > 0;

                        if (!isCategory) {
                            prop.getProperties().stream()
                                    .filter(p -> p.propType() == WzPropertyType.SubProperty)
                                    .map(p -> (WzSubProperty) p)
                                    .forEach(lifeProp -> {
                                        LifeData lifeData = loadLife(lifeProp);
                                        template.addLifeData(lifeData);
                                    });
                        } else {
                            prop.getProperties()
                                    .stream()
                                    .filter(p -> !p.getName().equalsIgnoreCase("isCategory"))
                                    .map(p -> (WzSubProperty) p)
                                    .forEach(cateProp -> {
                                        int category = Integer.parseInt(cateProp.getName());
                                        cateProp.getProperties()
                                                .stream()
                                                .map(p -> (WzSubProperty) p)
                                                .forEach(lifeProp -> {
                                                    LifeData lifeData = loadLife(lifeProp);
                                                    template.addLifeData(category, lifeData);
                                                });
                                    });
                        }


                    } else if (propName.equalsIgnoreCase("portal")) {
                        prop.getProperties().stream()
                                .map(p -> (WzSubProperty) p)
                                .forEach(portalProp -> {
                                    Portal pt = new Portal();
                                    pt.setId(Integer.parseInt(portalProp.getName()));
                                    portalProp.getProperties().forEach(p -> {
                                        if (p.getName().equalsIgnoreCase("pn")) {
                                            pt.setName(p.getString());
                                        } else if (p.getName().equalsIgnoreCase("tn")) {
                                            pt.setTargetPortalName(p.getString());
                                        } else if (p.getName().equalsIgnoreCase("script")) {
                                            pt.setScript(p.getString());
                                        } else if (p.getName().equalsIgnoreCase("x")) {
                                            pt.getPosition().setX(p.getInt());
                                        } else if (p.getName().equalsIgnoreCase("y")) {
                                            pt.getPosition().setY(p.getInt());
                                        } else if (p.getName().equalsIgnoreCase("pt")) {
                                            pt.setType(PortalType.getTypeByInt(p.getInt()));
                                        } else if (p.getName().equalsIgnoreCase("tm")) {
                                            pt.setTargetMapId(p.getInt());
                                        } else if (p.getName().equalsIgnoreCase("horizontalImpact")) {
                                            pt.setHorizontalImpact(p.getInt());
                                        } else if (p.getName().equalsIgnoreCase("verticalImpact")) {
                                            pt.setVerticalImpact(p.getInt());
                                        } else if (p.getName().equalsIgnoreCase("onlyOnce")) {
                                            pt.setOnlyOnce(p.getInt() > 0);
                                        } else if (p.getName().equalsIgnoreCase("delay")) {
                                            pt.setDelay(p.getInt());
                                        } else if (p.getName().equalsIgnoreCase("hideTooltip")) {
                                            pt.setHideTooltip(p.getInt() > 0);
                                        }

                                    });
                                    template.addPortal(pt);
                                });
                    } else if (propName.equalsIgnoreCase("monsterCarnival")) {
                        MonsterCarnivalInfo carnivalInfo = new MonsterCarnivalInfo();
                        prop.getProperties().forEach(mcSubProp -> {
                            String mcSubPropName = mcSubProp.getName();
                            if (mcSubPropName.equalsIgnoreCase("mobGenPos")) {
                                WzSubProperty mobGenPosProp = (WzSubProperty) mcSubProp;
                                mobGenPosProp.getProperties().stream().map(p -> (WzSubProperty) p).forEach(each -> {
                                    CarnivalMobGenPos mobGenPos = new CarnivalMobGenPos();
                                    each.getProperties().forEach(mobGenPosSubProp -> {
                                        String mobGenPosSubPropName = mobGenPosSubProp.getName();
                                        if (mobGenPosSubPropName.equalsIgnoreCase("x")) {
                                            mobGenPos.setX(mobGenPosSubProp.getInt());
                                        } else if (mobGenPosSubPropName.equalsIgnoreCase("y")) {
                                            mobGenPos.setY(mobGenPosSubProp.getInt());
                                        } else if (mobGenPosSubPropName.equalsIgnoreCase("fh")) {
                                            mobGenPos.setFh(mobGenPosSubProp.getInt());
                                        } else if (mobGenPosSubPropName.equalsIgnoreCase("cy")) {
                                            mobGenPos.setCy(mobGenPosSubProp.getInt());
                                        }
                                    });
                                    carnivalInfo.addMobGenPos(mobGenPos);
                                });
                            } else if (mcSubPropName.equalsIgnoreCase("mob")) {
                                WzSubProperty mobProp = (WzSubProperty) mcSubProp;
                                mobProp.getProperties().stream().map(p -> ((WzSubProperty) p)).forEach(each -> {
                                    CarnivalMob mob = new CarnivalMob();
                                    each.getProperties().forEach(mobSubProp -> {
                                        String mobSubPropName = mobSubProp.getName();
                                        if (mobSubPropName.equalsIgnoreCase("id")) {
                                            mob.setTemplateID(mobSubProp.getInt());
                                        } else if (mobSubPropName.equalsIgnoreCase("spendCP")) {
                                            mob.setSpendCP(mobSubProp.getInt());
                                        } else if (mobSubPropName.equalsIgnoreCase("mobCount")) {
                                            mob.setMobCount(mobSubProp.getInt());
                                        }
                                    });
                                    carnivalInfo.addMob(mob);
                                });
                            } else if (mcSubPropName.equalsIgnoreCase("guardianGenPos")) {
                                WzSubProperty guardianGenPosProp = (WzSubProperty) mcSubProp;
                                guardianGenPosProp.getProperties().stream().map(p -> ((WzSubProperty) p)).forEach(each -> {
                                    CarnivalGuardianGenPos guardianGenPos = new CarnivalGuardianGenPos();
                                    each.getProperties().forEach(guardianGenPosSubProp -> {
                                        String mobSubPropName = guardianGenPosSubProp.getName();
                                        if (mobSubPropName.equalsIgnoreCase("x")) {
                                            guardianGenPos.setX(guardianGenPosSubProp.getInt());
                                        } else if (mobSubPropName.equalsIgnoreCase("y")) {
                                            guardianGenPos.setY(guardianGenPosSubProp.getInt());
                                        } else if (mobSubPropName.equalsIgnoreCase("f")) {
                                            guardianGenPos.setF(guardianGenPosSubProp.getInt());
                                        }
                                    });
                                    carnivalInfo.addGuardianGenPos(guardianGenPos);
                                });
                            } else if (mcSubPropName.equalsIgnoreCase("guardian")) {
//                                WzSubProperty mobProp = (WzSubProperty) mcSubProp;
//                                mobProp.getProperties().stream().map(p -> ((WzSubProperty) p)).forEach(each -> {
//                                    carnivalInfo.addGuardian(each.getInt());
//                                });
                            } else if (mcSubPropName.equalsIgnoreCase("reward")) {
                                WzSubProperty rewardProp = (WzSubProperty) mcSubProp;
                                rewardProp.getProperties().stream().forEach(rewardSubProp -> {
                                    CarnivalReward reward = new CarnivalReward();
                                    String rewardSubPropName = rewardSubProp.getName();
                                    if (rewardSubPropName.equalsIgnoreCase("climax")) {
                                        reward.setClimax(rewardSubProp.getFloat());
                                    } else if (rewardSubPropName.equalsIgnoreCase("cpDiff")) {
                                        WzSubProperty cpDiffProp = (WzSubProperty) rewardSubProp;
                                        cpDiffProp.getProperties().forEach(eachCPDiff -> {
                                            reward.getCpDiff().add(eachCPDiff.getInt());
                                        });
                                    } else if (rewardSubPropName.equalsIgnoreCase("probChange")) {
                                        WzSubProperty probChangeProp = (WzSubProperty) rewardSubProp;
                                        probChangeProp.getProperties().stream()
                                                .map(p -> (WzSubProperty) p).forEach(eachProbChange -> {
                                            CarnivalRewardProbChange probChange = new CarnivalRewardProbChange();
                                            eachProbChange.getProperties().forEach(ppp -> {
                                                String pppName = ppp.getName();
                                                if (pppName.equalsIgnoreCase("wInCoin")) {
                                                    probChange.setWinCoin(ppp.getFloat());
                                                } else if (pppName.equalsIgnoreCase("winRecovery")) {
                                                    probChange.setWinRecovery(ppp.getFloat());
                                                } else if (pppName.equalsIgnoreCase("winNuff")) {
                                                    probChange.setWinNuff(ppp.getFloat());
                                                } else if (pppName.equalsIgnoreCase("winCP")) {
                                                    probChange.setWinCP(ppp.getFloat());
                                                } else if (pppName.equalsIgnoreCase("loseCoin")) {
                                                    probChange.setLoseCoin(ppp.getFloat());
                                                } else if (pppName.equalsIgnoreCase("loseRecovery")) {
                                                    probChange.setLoseRecovery(ppp.getFloat());
                                                } else if (pppName.equalsIgnoreCase("loseNuff")) {
                                                    probChange.setLoseNuff(ppp.getFloat());
                                                } else if (pppName.equalsIgnoreCase("loseCP")) {
                                                    probChange.setLoseCP(ppp.getFloat());
                                                }
                                            });
                                        });
                                    }
                                    carnivalInfo.addReward(reward);
                                });
                            } else if (mcSubPropName.equalsIgnoreCase("timeDefault")) {
                                carnivalInfo.setTimeDefault(mcSubProp.getInt());
                            } else if (mcSubPropName.equalsIgnoreCase("timeExpand")) {
                                carnivalInfo.setTimeExpand(mcSubProp.getInt());
                            } else if (mcSubPropName.equalsIgnoreCase("timeMessage")) {
                                carnivalInfo.setTimeMessage(mcSubProp.getInt());
                            } else if (mcSubPropName.equalsIgnoreCase("timeFinish")) {
                                carnivalInfo.setTimeFinish(mcSubProp.getInt());
                            } else if (mcSubPropName.equalsIgnoreCase("effectWin")) {
                                carnivalInfo.setEffectWin(mcSubProp.getString());
                            } else if (mcSubPropName.equalsIgnoreCase("effectLose")) {
                                carnivalInfo.setEffectLose(mcSubProp.getString());
                            } else if (mcSubPropName.equalsIgnoreCase("soundWin")) {
                                carnivalInfo.setSoundWin(mcSubProp.getString());
                            } else if (mcSubPropName.equalsIgnoreCase("soundLose")) {
                                carnivalInfo.setSoundLose(mcSubProp.getString());
                            } else if (mcSubPropName.equalsIgnoreCase("rewardMapWin")) {
                                carnivalInfo.setRewardMapWin(mcSubProp.getInt());
                            } else if (mcSubPropName.equalsIgnoreCase("rewardMapLose")) {
                                carnivalInfo.setRewardMapLose(mcSubProp.getInt());
                            } else if (mcSubPropName.equalsIgnoreCase("mobGenMax")) {
                                carnivalInfo.setMobGenMax(mcSubProp.getInt());
                            } else if (mcSubPropName.equalsIgnoreCase("mapDivided")) {
                                carnivalInfo.setMapDivided(mcSubProp.getInt() > 0);
                            } else if (mcSubPropName.equalsIgnoreCase("deathCP")) {
                                carnivalInfo.setDeathCP(mcSubProp.getInt());
                            } else if (mcSubPropName.equalsIgnoreCase("guardianGenMax")) {
                                carnivalInfo.setGuardianGenMax(mcSubProp.getInt());
                            } else if (mcSubPropName.equalsIgnoreCase("reactorRed")) {
                                carnivalInfo.setReactorRed(mcSubProp.getInt());
                            } else if (mcSubPropName.equalsIgnoreCase("reactorBlue")) {
                                carnivalInfo.setReactorBlue(mcSubProp.getInt());
                            }
                        });
                        template.setMonsterCarnivalInfo(carnivalInfo);
                    } else if (propName.equalsIgnoreCase("ladderRope")) {

                        prop.getProperties()
                                .stream()
                                .map(p -> (WzSubProperty) p)
                                .forEach(each -> {
                                    RadderRope radderRope = new RadderRope();
                                    each.getProperties().forEach(eachSubProp -> {
                                        String eachSubPropname = eachSubProp.getName();
                                        if (eachSubPropname.equalsIgnoreCase("x")) {
                                            radderRope.setX(eachSubProp.getInt());
                                        } else if (eachSubPropname.equalsIgnoreCase("y1")) {
                                            radderRope.setY1(eachSubProp.getInt());
                                        } else if (eachSubPropname.equalsIgnoreCase("y2")) {
                                            radderRope.setY2(eachSubProp.getInt());
                                        } else if (eachSubPropname.equalsIgnoreCase("page")) {
                                            radderRope.setPage(eachSubProp.getInt());
                                        } else if (eachSubPropname.equalsIgnoreCase("piece")) {
                                            radderRope.setPiece(eachSubProp.getInt());
                                        }
                                    });
                                    template.addRadderRope(radderRope);
                                });

                    } else if (propName.equalsIgnoreCase("directionInfo")) {
                        prop.getProperties().stream()
                                .map(p -> (WzSubProperty) p).forEach(dfProp -> {
                            FieldDirectionInfo df = new FieldDirectionInfo();
                            dfProp.getProperties().forEach(dfSubProp -> {
                                String dfSubName = dfSubProp.getName();
                                if (dfSubName.equalsIgnoreCase("x")) {
                                    df.setX(dfSubProp.getInt());
                                } else if (dfSubName.equalsIgnoreCase("y")) {
                                    df.setY(dfSubProp.getInt());
                                } else if (dfSubName.equalsIgnoreCase("forcedInput")) {
                                    df.setForcedInput(dfSubProp.getInt() > 0);
                                } else if (dfSubName.equalsIgnoreCase("EventQ")) {
                                    ((WzSubProperty) dfSubProp).getProperties().forEach(eventQProp -> {
                                        df.getEventQs().add(eventQProp.getString());
                                    });
                                }
                            });
                            template.addDirectionInfo(df);
                        });
                    } else if (propName.equalsIgnoreCase("nodeInfo")) {
                        FieldNodeInfo fieldNodeInfo = template.getFieldNodeInfo();
                        if (prop.hasProperty("start"))
                            fieldNodeInfo.setStart(prop.get("start").getInt());
                        if (prop.hasProperty("end"))
                            fieldNodeInfo.setStart(prop.get("end").getInt());

                        for (int i = fieldNodeInfo.getStart(); i <= fieldNodeInfo.getEnd() && i >= 0; i++) {
                            if (!prop.hasProperty(String.valueOf(i)))
                                continue;
                            WzSubProperty nodeProp = (WzSubProperty) prop.get(String.valueOf(i));
                            FieldNode fieldNode = new FieldNode();
                            nodeProp.getProperties().forEach(nodePropSub -> {
                                String nodePropSubName = nodePropSub.getName();
                                if (nodePropSubName.equalsIgnoreCase("key")) {
                                    fieldNode.setKey(nodePropSub.getInt());
                                } else if (nodePropSubName.equalsIgnoreCase("x")) {
                                    fieldNode.setX(nodePropSub.getInt());
                                } else if (nodePropSubName.equalsIgnoreCase("y")) {
                                    fieldNode.setY(nodePropSub.getInt());
                                } else if (nodePropSubName.equalsIgnoreCase("attr")) {
                                    fieldNode.setAttr(nodePropSub.getInt());
                                }
                            });
                            fieldNodeInfo.getNodes().add(fieldNode);
                        }
                    } else if (propName.matches("^[0-7]$")) {
                        if (!prop.hasProperty("obj"))
                            return;
                        WzSubProperty objProp = (WzSubProperty) prop.get("obj");
                        objProp.getProperties()
                                .stream()
                                .map(p -> (WzSubProperty) p).forEach(eachObjProp -> {

                            FieldObjectInfo obj = new FieldObjectInfo();
                            eachObjProp.getProperties().forEach(eachObjSubProp -> {
                                String eachObjSubPropName = eachObjSubProp.getName();
                                if (eachObjSubPropName.equalsIgnoreCase("oS")) {
                                    obj.setOs(eachObjSubProp.getString());
                                } else if (eachObjSubPropName.equalsIgnoreCase("l0")) {
                                    obj.setL0(eachObjSubProp.getString());
                                } else if (eachObjSubPropName.equalsIgnoreCase("l1")) {
                                    obj.setL1(eachObjSubProp.getString());
                                } else if (eachObjSubPropName.equalsIgnoreCase("l2")) {
                                    obj.setL2(eachObjSubProp.getString());
                                } else if (eachObjSubPropName.equalsIgnoreCase("l3")) {
                                    obj.setL3(eachObjSubProp.getString());
                                } else if (eachObjSubPropName.equalsIgnoreCase("x")) {
                                    obj.setX(eachObjSubProp.getInt());
                                } else if (eachObjSubPropName.equalsIgnoreCase("y")) {
                                    obj.setY(eachObjSubProp.getInt());
                                } else if (eachObjSubPropName.equalsIgnoreCase("z")) {
                                    obj.setZ(eachObjSubProp.getInt());
                                } else if (eachObjSubPropName.equalsIgnoreCase("f")) {
                                    obj.setF(eachObjSubProp.getInt());
                                } else if (eachObjSubPropName.equalsIgnoreCase("r")) {
                                    obj.setR(eachObjSubProp.getInt());
                                } else if (eachObjSubPropName.equalsIgnoreCase("rx")) {
                                    obj.setRx(eachObjSubProp.getInt());
                                } else if (eachObjSubPropName.equalsIgnoreCase("ry")) {
                                    obj.setRy(eachObjSubProp.getInt());
                                } else if (eachObjSubPropName.equalsIgnoreCase("cx")) {
                                    obj.setCx(eachObjSubProp.getInt());
                                } else if (eachObjSubPropName.equalsIgnoreCase("cy")) {
                                    obj.setCy(eachObjSubProp.getInt());
                                } else if (eachObjSubPropName.equalsIgnoreCase("quest")) {
                                    ((WzSubProperty) eachObjSubProp).getProperties().forEach(questProp -> {
                                        obj.getQuests().add(new Tuple<>(Integer.parseInt(questProp.getName()), questProp.getInt()));
                                    });
                                } else if (eachObjSubPropName.equalsIgnoreCase("zM")) {
                                    obj.setZ(eachObjSubProp.getInt());
                                } else if (eachObjSubPropName.equalsIgnoreCase("move")) {
                                    obj.setMove(eachObjSubProp.getInt() > 0);
                                } else if (eachObjSubPropName.equalsIgnoreCase("dynamic")) {
                                    obj.setDynamic(eachObjSubProp.getInt() > 0);
                                } else if (eachObjSubPropName.equalsIgnoreCase("forbidFallDown")) {
                                    obj.setForbidFallDown(eachObjSubProp.getInt() > 0);
                                } else if (eachObjSubPropName.equalsIgnoreCase("cantThrough")) {
                                    obj.setCantThrough(eachObjSubProp.getInt() > 0);
                                } else if (eachObjSubPropName.equalsIgnoreCase("flow")) {
                                    obj.setFlow(eachObjSubProp.getInt());
                                } else if (eachObjSubPropName.equalsIgnoreCase("force")) {
                                    if (eachObjSubProp instanceof WzIntProperty) {
                                        obj.setForce(eachObjSubProp.getInt());
                                    } else {
                                        obj.setForce(Integer.parseInt(eachObjSubProp.getString()));
                                    }
                                } else if (eachObjSubPropName.equalsIgnoreCase("reactor")) {
                                    obj.setReactor(eachObjSubProp.getInt());
                                } else if (eachObjSubPropName.equalsIgnoreCase("piece")) {
                                    obj.setPiece(eachObjSubProp.getInt());
                                } else if (eachObjSubPropName.equalsIgnoreCase("start")) {
                                    obj.setStart(eachObjSubProp.getInt());
                                } else if (eachObjSubPropName.equalsIgnoreCase("speed")) {
                                    obj.setSpeed(eachObjSubProp.getInt());
                                } else if (eachObjSubPropName.equalsIgnoreCase("repeat")) {
                                    obj.setRepeat(eachObjSubProp.getInt());
                                } else if (eachObjSubPropName.equalsIgnoreCase("x1")) {
                                    obj.setX1(eachObjSubProp.getInt());
                                } else if (eachObjSubPropName.equalsIgnoreCase("y1")) {
                                    obj.setY2(eachObjSubProp.getInt());
                                } else if (eachObjSubPropName.equalsIgnoreCase("x2")) {
                                    obj.setX2(eachObjSubProp.getInt());
                                } else if (eachObjSubPropName.equalsIgnoreCase("y2")) {
                                    obj.setY2(eachObjSubProp.getInt());
                                } else if (eachObjSubPropName.equalsIgnoreCase("name")) {
                                    obj.setName(eachObjSubProp.getString());
                                } else if (eachObjSubPropName.matches("^SN[0-9]+$")) {
                                    int sn = eachObjSubProp.getInt();
                                    obj.getSns().add(sn);
                                } else if (eachObjSubPropName.equalsIgnoreCase("tags")) {
                                    obj.setTags(eachObjSubProp.getString());
                                } else {
                                    //     LoggerFactory.getLogger(FieldTemplate.class)
                                    //            .warn("FieldObjectInfo not load " + eachObjSubPropName);
                                }
                            });
                            template.addObject(obj);
                        });

                    }
                });

        int x = 1;

    }

    private LifeData loadLife(WzSubProperty lifeProp) {

        boolean mob = lifeProp.hasProperty("type") && lifeProp.get("type").getString().equals("m");

        LifeData lifeData = new LifeData();
        lifeProp.getProperties().forEach(p -> {

            if (p.getName().equalsIgnoreCase("id")) {
                lifeData.setId(p.getInt());
            } else if (p.getName().equalsIgnoreCase("type")) {
                lifeData.setType(p.getString());
            } else if (p.getName().equalsIgnoreCase("x")) {
                lifeData.setX(p.getInt());
            } else if (p.getName().equalsIgnoreCase("y")) {
                lifeData.setY(p.getInt());
            } else if (p.getName().equalsIgnoreCase("mobTime")) {
                lifeData.setMobTime(p.getInt());
            } else if (p.getName().equalsIgnoreCase("f")) {
                lifeData.setF(p.getInt());
            } else if (p.getName().equalsIgnoreCase("fh")) {
                lifeData.setFh(p.getInt());
            } else if (p.getName().equalsIgnoreCase("hide")) {
                lifeData.setHide(p.getInt() > 0);
            } else if (p.getName().equalsIgnoreCase("cy")) {
                lifeData.setCy(p.getInt());
            } else if (p.getName().equalsIgnoreCase("rx0")) {
                lifeData.setRx0(p.getInt());
            } else if (p.getName().equalsIgnoreCase("rx1")) {
                lifeData.setRx1(p.getInt());
            } else if (p.getName().equalsIgnoreCase("limitedname")) {
                lifeData.setLimitedname(p.getString());
            } else if (p.getName().equalsIgnoreCase("useDay")) {
                lifeData.setUseDay(p.getInt() > 0);
            } else if (p.getName().equalsIgnoreCase("useNight")) {
                lifeData.setUseNight(p.getInt() > 0);
            } else if (p.getName().equalsIgnoreCase("hold")) {
                lifeData.setHold(p.getInt() > 0);
            } else if (p.getName().equalsIgnoreCase("nofoothold")) {
                lifeData.setNofoothold(p.getInt() > 0);
            } else if (p.getName().equalsIgnoreCase("dummy")) {
                lifeData.setDummy(p.getInt() > 0);
            } else if (p.getName().equalsIgnoreCase("spine")) {
                lifeData.setSpine(p.getInt() > 0);
            } else if (p.getName().equalsIgnoreCase("mobTimeOnDie")) {
                lifeData.setMobTimeOnDie(p.getInt() > 0);
            } else if (p.getName().equalsIgnoreCase("regenStart")) {
                lifeData.setRx1(p.getInt());
            } else if (p.getName().equalsIgnoreCase("mobAliveReq")) {
                lifeData.setRx1(p.getInt());
            }
        });

        return lifeData;
    }

    @Override
    public void saveToDat() throws IOException {
        if (data.isEmpty())
            load();
        new FieldTemplateDatLoader().saveDat(data);
    }
}
