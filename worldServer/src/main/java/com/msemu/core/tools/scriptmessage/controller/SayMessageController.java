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
                    if(listener != null) {
                        int id = -1;
                        if(StringUtils.isNumber(overrideTemplateIDText.getText())) {
                            id = Integer.parseInt(overrideTemplateIDText.getText());
                        }
                        listener.overrideTemplateChanged(id);
                    }
                }
        );
    }

    @Override
    public void encode(OutPacket<GameClient> outPacket) {
        if(overrideTemplateIDCheck.isSelected()) {
            int id = -1;
            if(StringUtils.isNumber(overrideTemplateIDText.getText())) {
                id = Integer.parseInt(overrideTemplateIDText.getText());
            }
            if(id > 0)
                outPacket.encodeInt(id);
        }

        outPacket.encodeString(text.getText());
        outPacket.encodeByte(prevCheck.isSelected());
        outPacket.encodeByte(nextCheck.isSelected());
        int delay = 0;
        if(StringUtils.isNumber(delayText.getText())) {
            delay = Integer.parseInt(delayText.getText());
        }
        outPacket.encodeInt(delay);
    }

    @Override
    public void setOverrideTemplateIDChangeListener(IOverrideTemplateIDChange listener) {
        this.listener = listener;
    }
}
