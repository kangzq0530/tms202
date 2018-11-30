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

package com.msemu.commons.data.templates.field;

import com.msemu.commons.data.loader.dat.DatSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReactorStateInfo implements DatSerializable {

    private int state;

    private boolean repeat, follow;

    private List<ReactorEventInfo> events = new ArrayList<>();


    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(state);
        dos.writeBoolean(repeat);
        dos.writeBoolean(follow);
        dos.writeInt(events.size());
        for (ReactorEventInfo event : events)
            event.write(dos);
    }

    @Override
    public DatSerializable load(DataInputStream dis) throws IOException {
        setState(dis.readInt());
        setRepeat(dis.readBoolean());
        setFollow(dis.readBoolean());
        int eventsCount = dis.readInt();
        for (int i = 0; i < eventsCount; i++) {
            ReactorEventInfo event = new ReactorEventInfo();
            event.load(dis);
            getEvents().add(event);
        }
        return this;
    }
}
