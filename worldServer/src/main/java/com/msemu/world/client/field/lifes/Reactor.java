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

package com.msemu.world.client.field.lifes;

import com.msemu.commons.data.enums.ReactorEventType;
import com.msemu.commons.data.templates.field.ReactorEventInfo;
import com.msemu.commons.data.templates.field.ReactorInField;
import com.msemu.commons.data.templates.field.ReactorStateInfo;
import com.msemu.commons.data.templates.field.ReactorTemplate;
import com.msemu.commons.utils.types.Position;
import com.msemu.core.network.GameClient;
import com.msemu.core.network.packets.outpacket.reactor.LP_ReactorChangeState;
import com.msemu.core.network.packets.outpacket.reactor.LP_ReactorEnterField;
import com.msemu.core.network.packets.outpacket.reactor.LP_ReactorLeaveField;
import com.msemu.world.client.character.Character;
import com.msemu.world.client.field.Field;
import com.msemu.world.data.ReactorData;
import com.msemu.world.enums.FieldObjectType;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Reactor extends Life {

    private static final Logger log = LoggerFactory.getLogger(Reactor.class);

    @Getter
    @Setter
    private int templacteId;

    @Getter
    @Setter
    private int state;

    @Getter
    @Setter
    private boolean flip;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int ownerId;

    public Reactor(ReactorInField ri) {
        setName(ri.getName());
        setFlip(ri.getF() > 0);
        setPosition(new Position(ri.getX(), ri.getY()));
        setTemplacteId(ri.getId());
    }

    public ReactorTemplate getTemplate() {
        return ReactorData.getInstance().getReactorTemplateById(templacteId);
    }

    public boolean hasNextState() {
        final ReactorTemplate template = getTemplate();
        return (state + 1) < template.getStatesInfo().size();
    }

    public void hit(Character chr, int actDelay) {
        final Field field = chr.getField();
        final ReactorTemplate template = getTemplate();
        if (hasNextState()) {
            final int statesCount = template.getStatesInfo().size();
            final ReactorStateInfo nextStateInfo = template.getStatesInfo().get(getState() + 1);
            final ReactorStateInfo endStateInfo = template.getStatesInfo().get(statesCount - 1);

            final List<ReactorEventInfo> events = nextStateInfo.getEvents();

            // TODO process reactor events

            for (ReactorEventInfo event : events) {

                final ReactorEventType eventType = ReactorEventType.getByValue(event.getType());
                switch (eventType) {
                    case REACTOR_EVENT_HIT:
                        break;
                    default:
                        log.warn(String.format("[Unknown ReactorEvent] value = %d", event.getType()));
                        break;
                }


            }

            setOwnerId(chr.getId());
            setState(nextStateInfo.getState());
            field.broadcastPacket(new LP_ReactorChangeState(this, state, 0, statesCount), chr);
        }
    }


    @Override
    public FieldObjectType getFieldObjectType() {
        return FieldObjectType.REACTOR;
    }

    @Override
    public void enterScreen(GameClient client) {
        client.write(new LP_ReactorEnterField(this));
    }

    @Override
    public void outScreen(GameClient client) {
        client.write(new LP_ReactorLeaveField(this));
    }
}

