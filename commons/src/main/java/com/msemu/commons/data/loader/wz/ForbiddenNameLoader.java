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
import com.msemu.commons.data.loader.dat.ForbiddenNameDatLoader;
import com.msemu.commons.data.templates.ForbiddenName;
import com.msemu.commons.wz.WzDirectory;
import com.msemu.commons.wz.WzFile;
import com.msemu.commons.wz.WzImage;
import lombok.Getter;

import java.io.IOException;

/**
 * Created by Weber on 2018/4/21.
 */
public class ForbiddenNameLoader extends WzDataLoader<ForbiddenName> {

    @Getter
    ForbiddenName data = null;

    public ForbiddenNameLoader(WzManager wzManager) {
        super(wzManager);
    }

    @Override
    public void load() {
        data = new ForbiddenName();
        WzFile wzFile = wzManager.getWz(WzManager.ETC);
        WzDirectory wzDir = wzFile.getWzDirectory();
        WzImage forbiddenNamesImg = wzDir.getImage("ForbiddenName.img");
        forbiddenNamesImg.getProperties().forEach(prop -> data.getNames().add(prop.getString()));
    }

    @Override
    public void saveToDat() throws IOException {
        if (getData() == null) {
            load();
        }
        new ForbiddenNameDatLoader().saveDat(getData());
    }
}
