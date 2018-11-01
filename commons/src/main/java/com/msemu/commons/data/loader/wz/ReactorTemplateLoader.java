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
import com.msemu.commons.data.loader.dat.ReactorTemplateDatLoader;
import com.msemu.commons.data.templates.field.ReactorClickArea;
import com.msemu.commons.data.templates.field.ReactorEventInfo;
import com.msemu.commons.data.templates.field.ReactorStateInfo;
import com.msemu.commons.data.templates.field.ReactorTemplate;
import com.msemu.commons.wz.*;
import com.msemu.commons.wz.properties.WzSubProperty;
import com.msemu.commons.wz.properties.WzVectorProperty;
import lombok.Getter;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReactorTemplateLoader extends WzDataLoader<Map<Integer, ReactorTemplate>> {

    @Getter
    private Map<Integer, ReactorTemplate> data = new HashMap<>();

    public ReactorTemplateLoader(WzManager wzManager) {
        super(wzManager);
    }


    @Override
    public void load() throws IOException {
        WzFile wzFile = wzManager.getWz(WzManager.REACTOR);
        WzDirectory wzDir = wzFile.getWzDirectory();

        for (WzImage reactorImg : wzDir.getImages()) {

            final ReactorTemplate template = new ReactorTemplate();
            final String imgName = reactorImg.getName();
            
            if(imgName.equalsIgnoreCase("ReactorEvent.img")){
                addReactorEvent(imgName);
                continue;
            }

            final int reactorId = Integer.parseInt(imgName.replace(".img", ""));


            template.setId(reactorId);
            for (WzImageProperty prop : reactorImg.getProperties()) {
                final String propName = prop.getName();
                switch (propName) {
                    case "info":
                        final WzSubProperty infoNode = (WzSubProperty) prop;
                        for (WzImageProperty infoProp : infoNode.getProperties()) {
                            switch (infoProp.getName()) {
                                case "level":
                                    template.setLevel(infoProp.getInt());
                                    break;
                                case "frontTile":
                                    template.setFrontTile(infoProp.getInt() > 0);
                                    break;
                                case "backTile":
                                    template.setBackTile(infoProp.getInt() > 0);
                                    break;
                                case "link":
                                    template.setLink(infoProp.getInt());
                                    break;
                                case "viewName":
                                    template.setViewName(infoProp.getString());
                                    break;
                                case "resetTime":
                                    template.setResetTime(infoProp.getInt());
                                    break;
                            }
                        }
                        break;
                    case "action":
                        template.setAction(prop.getString());
                        break;
                }

                if (propName.matches("^[0-9]+$")) {
                    // state info
                    final ReactorStateInfo stateInfo = new ReactorStateInfo();
                    final int state = Integer.parseInt(propName);
                    final WzSubProperty eventNode = (WzSubProperty) prop.getFromPath("event");


                    if (prop.getFromPath("follow") != null)
                        stateInfo.setFollow(prop.getFromPath("follow").getInt() > 0);
                    else if (prop.getFromPath("repeat") != null)
                        stateInfo.setRepeat(prop.getFromPath("repeat").getInt() > 0);

                    loadReactorEvents(stateInfo, eventNode);
                    if (state <= template.getStatesInfo().size()) {
                        template.getStatesInfo().add(state, stateInfo);
                    } else {
                        template.getStatesInfo().add(stateInfo);
                    }
                }

            }
            getData().put(template.getId(), template);
        }


    }

    private void addReactorEvent(String imgName) {
        // TODO
    }

    private void loadReactorEvents(ReactorStateInfo stateInfo, WzSubProperty eventsNode) {
        if (eventsNode == null)
            return;
        int timeOut = 0;
        for (WzImageProperty prop : eventsNode.getProperties()) {
            final String propName = prop.getName();
            switch (propName) {
                case "timeOut":
                    timeOut = prop.getInt();
                    break;
            }
            if (prop.getName().matches("^[0-9]+$")) {
                final int eventId = Integer.parseInt(prop.getName());
                WzSubProperty eventNode = (WzSubProperty) prop;
                final ReactorEventInfo eventInfo = new ReactorEventInfo();
                eventInfo.setTimeOut(timeOut);

                for (WzImageProperty eventProp : eventNode.getProperties()) {
                    WzVectorProperty vector;
                    WzSubProperty sub;
                    final String eventPropName = eventProp.getName();

                    switch (eventPropName) {
                        case "type":
                            eventInfo.setType(eventProp.getInt());
                            break;
                        case "actCount":
                            eventInfo.setActCount(eventProp.getInt());
                            break;
                        case "state":
                            eventInfo.setNextState(eventProp.getInt());
                            break;
                        case "act":
                            eventInfo.setAct(eventProp.getString());
                            break;
                        case "rb":
                            vector = (WzVectorProperty) eventProp;
                            eventInfo.setRb(new Point(vector.getPoint()));
                            break;
                        case "lt":
                            vector = (WzVectorProperty) eventProp;
                            eventInfo.setRb(new Point(vector.getPoint()));
                            break;
                        case "activeSkillID":
                            sub = (WzSubProperty) eventProp;
                            sub.getProperties().forEach(p -> eventInfo.getActiveSkills().add(p.getInt()));
                            break;
                        case "clickArea":
                            final ReactorClickArea clickArea = new ReactorClickArea();
                            sub = (WzSubProperty) eventProp;
                            for (WzImageProperty areaProp : sub.getProperties()) {
                                final String areaPropName = areaProp.getName();
                                switch (areaPropName) {
                                    case "lt":
                                        vector = (WzVectorProperty) areaProp;
                                        clickArea.setLt(new Point(vector.getPoint()));
                                        break;
                                    case "rb":
                                        vector = (WzVectorProperty) areaProp;
                                        clickArea.setRb(new Point(vector.getPoint()));
                                        break;
                                    case "Message":
                                        clickArea.setMessage(areaProp.getString());
                                        break;
                                }
                            }
                            break;
                    }

                    if (eventPropName.matches("^[0-9]+$")) {
                        int index = Integer.parseInt(eventPropName);
                        WzPropertyType type = eventProp.propType();
                        switch (type) {
                            case Int:
                                if (index <= eventInfo.getArgs().size()) {
                                    eventInfo.getArgs().add(index, eventProp.getInt());
                                } else {
                                    eventInfo.getArgs().add(eventProp.getInt());
                                }
                                break;
                            case String:
                                if (index <= eventInfo.getArgs().size()) {
                                    eventInfo.getStrings().add(index, eventProp.getString());
                                } else {
                                    eventInfo.getStrings().add(eventProp.getString());
                                }
                                break;
                        }

                    }
                }
                stateInfo.getEvents().add(eventId, eventInfo);
            }
        }


    }

    @Override
    public void saveToDat() throws IOException {
        if (getData().isEmpty())
            load();
        new ReactorTemplateDatLoader().saveDat(getData());
    }
}
