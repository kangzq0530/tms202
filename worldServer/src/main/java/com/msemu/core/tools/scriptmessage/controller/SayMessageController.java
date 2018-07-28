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

package com.msemu.core.tools.scriptmessage.controller;

import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.StringUtils;
import com.msemu.core.network.GameClient;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Created by Weber on 2018/5/15.
 */
public class SayMessageController implements IPackeyEncode {

    @FXML
    private CheckBox overrideTemplateIDCheck;

    @FXML
    private TextField overrideTemplateIDText;

    @FXML
    private TextArea text;

    @FXML
    private CheckBox prevCheck;

    @FXML
    private CheckBox nextCheck;

    @FXML
    private TextField delayText;

    private IOverrideTemplateIDChange listener;

    @FXML
    void initialize() {
        overrideTemplateIDCheck.selectedProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (listener != null) {
                        int id = -1;
                        if (StringUtils.isNumber(overrideTemplateIDText.getText())) {
                            id = Integer.parseInt(overrideTemplateIDText.getText());
                        }
                        listener.overrideTemplateChanged(id);
                    }
                }
        );
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        if (overrideTemplateIDCheck.isSelected()) {
            int id = -1;
            if (StringUtils.isNumber(overrideTemplateIDText.getText())) {
                id = Integer.parseInt(overrideTemplateIDText.getText());
            }
            if (id > 0)
                outPacket.encodeInt(id);
        }

        outPacket.encodeString(text.getText());
        outPacket.encodeByte(prevCheck.isSelected());
        outPacket.encodeByte(nextCheck.isSelected());
        int delay = 0;
        if (StringUtils.isNumber(delayText.getText())) {
            delay = Integer.parseInt(delayText.getText());
        }
        outPacket.encodeInt(delay);
    }

    @Override
    public void setOverrideTemplateIDChangeListener(IOverrideTemplateIDChange listener) {
        this.listener = listener;
    }
}
