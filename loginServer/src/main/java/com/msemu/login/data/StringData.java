package com.msemu.login.data;

import com.msemu.commons.config.annotation.ConfigFile;
import com.msemu.commons.config.utils.ConfigLoader;
import com.msemu.commons.utils.XMLApi;
import com.msemu.core.configs.CoreConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weber on 2018/4/19.
 */
public class StringData {

    private List<String> forbiddenName = new ArrayList<>();


    public void loadFromWZ() {

    }

    private void loadForbiddenName() {
        String filePath = CoreConfig.WZ_PATH + "/Etc.wz/ForbiddenName.img.xml";
        File file = new File(filePath);
        Document doc = XMLApi.getRoot(file);
        Node mainNode = XMLApi.getAllChildren(doc).get(0);
        for (Node node : XMLApi.getAllChildren(mainNode)) {
            System.out.println(node.getAttributes().getNamedItem("value").getNodeValue());
        }


    }


    public static void main(String[] args) {
        ConfigLoader.getInstance().reload();
        new StringData().loadForbiddenName();
    }
}
