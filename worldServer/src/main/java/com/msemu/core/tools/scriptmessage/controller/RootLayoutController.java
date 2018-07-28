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

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.StringUtils;
import com.msemu.core.network.GameClient;
import com.msemu.world.World;
import com.msemu.world.enums.NpcDialogColorType;
import com.msemu.world.enums.NpcMessageType;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class RootLayoutController implements IOverrideTemplateIDChange {

    @FXML
    private ResourceBundle resources;

    @FXML
    private ComboBox<GameClient> clientsList;

    @FXML
    private URL location;

    @FXML
    private TextField speakerTypeIDText;

    @FXML
    private TextField speakerTemplateText;

    @FXML
    private CheckBox anotherSpeakerTemplateCheck;

    @FXML
    private TextField anotherSpeakerTemplateText;

    @FXML
    private ComboBox<NpcMessageType> messageTypeList;

    @FXML
    private TextField bParamText;

    @FXML
    private ComboBox<NpcDialogColorType> eColorList;

    @FXML
    private VBox otherFieldsView;

    private IPackeyEncode otherDataController;

    private Map<NpcMessageType, String> views = new HashMap<>();

    @FXML
    void onChangeNpcMessageType(InputMethodEvent event) {

    }

    @FXML
    void onSendBtnClicked(MouseEvent event) {

        if (otherDataController != null) {
            OutPacket<GameClient> outPacket = new OutPacket<>(OutHeader.LP_ScriptMessage);
            encode(outPacket);
            otherDataController.encode(outPacket);
            World.getInstance().getChannels().forEach(channel -> {
                channel.getCharacters().forEach((id, chr) -> {
                    chr.write(outPacket);
                });
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Error");
        }

    }

    @FXML
    void sendBtn(ActionEvent event) {

    }

    @FXML
    void initialize() {
        messageTypeList.setItems(FXCollections.observableArrayList(NpcMessageType.values()));
        eColorList.setItems(FXCollections.observableArrayList(NpcDialogColorType.values()));
        messageTypeList.getSelectionModel().select(0);
        eColorList.getSelectionModel().select(0);

        views.put(NpcMessageType.NM_SAY, "view/SayMessageLayout.fxml");

        messageTypeList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (views.containsKey(newValue)) {
                otherFieldsView.getChildren().clear();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(RootLayoutController.class.getClassLoader().getResource(views.get(newValue)));
                try {
                    otherFieldsView.getChildren().add(loader.load());
                    otherDataController = loader.getController();
                    otherDataController.setOverrideTemplateIDChangeListener(this);
                    VBox box = (VBox) otherFieldsView.getChildren().get(0);
                    otherFieldsView.setPrefHeight(box.getPrefHeight());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void encode(OutPacket<GameClient> outPacket) {

        if (StringUtils.isNumber(speakerTypeIDText.getText())) {
            outPacket.encodeByte(Integer.parseInt(speakerTypeIDText.getText()));
        } else {
            outPacket.encodeByte(4);
        }
        if (StringUtils.isNumber(speakerTemplateText.getText())) {
            outPacket.encodeInt(Integer.parseInt(speakerTemplateText.getText()));
        } else {
            outPacket.encodeInt(2007);
        }

        boolean hasAnotherSpeakerTemplate = anotherSpeakerTemplateCheck.isSelected();
        if (hasAnotherSpeakerTemplate) {
            if (StringUtils.isNumber(anotherSpeakerTemplateText.getText())) {
                outPacket.encodeByte(1);
                outPacket.encodeInt(Integer.parseInt(anotherSpeakerTemplateText.getText()));
            } else {
                outPacket.encodeByte(0);
            }
        } else {
            outPacket.encodeByte(0);
        }
        NpcMessageType type = messageTypeList.getSelectionModel().getSelectedItem();
        outPacket.encodeByte(type.getValue());
        int bParam = 0;
        if (StringUtils.isNumber(bParamText.getText())) {
            bParam = Integer.parseInt(bParamText.getText());
        }
        outPacket.encodeShort(bParam);
        NpcDialogColorType color = eColorList.getSelectionModel().getSelectedItem();
        outPacket.encodeByte(color.getValue());
    }

    @Override
    public void overrideTemplateChanged(int overrideTemplateID) {
        int bParam = 0;
        if (StringUtils.isNumber(bParamText.getText())) {
            bParam = Integer.parseInt(bParamText.getText());
        }
        if (overrideTemplateID > 0)
            bParam |= 4;
        else {
            if ((bParam & 4) > 0) {
                bParam ^= 4;
            }
        }
        bParamText.setText(String.valueOf(bParam));
    }
}
