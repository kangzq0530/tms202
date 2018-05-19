package com.msemu.commons.wz.deserializer;


import com.msemu.commons.utils.types.Size;
import com.msemu.commons.wz.*;
import com.msemu.commons.wz.properties.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Weber on 2018/4/20.
 */
public class WzXmlDeserializer extends ProgressingWzDeserializer {
    private static final Logger log = LoggerFactory.getLogger(WzXmlDeserializer.class);

    public WzXmlDeserializer() {
    }

    private List<Element> getChildNodes(Element node) {
        List<Element> ret = new ArrayList<>();
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE)
                ret.add((Element) nodeList.item(i));
        return ret;
    }

    private void importWz(WzFile wz, Path pathToWz) throws ParserConfigurationException, SAXException, IOException {
        for (Path subDir : listDirs(pathToWz)) {
            importDir(wz.getWzDirectory(), subDir);
        }
        for (Path path : listFiles(pathToWz, ".img.xml")) {
            importImage(wz.getWzDirectory(), path);
        }
    }

    private void importDir(WzDirectory wzDir, Path pathToWzDir) throws ParserConfigurationException, SAXException, IOException {
        if (wzDir.get(pathToWzDir.toFile().getName()) == null) {
            WzDirectory subWzDir = new WzDirectory(pathToWzDir.toFile().getName());
            wzDir.addDirectory(subWzDir);
            for (Path subDir : listDirs(pathToWzDir)) {
                importDir(subWzDir, subDir);
            }
            for (Path path : listFiles(pathToWzDir, ".img.xml")) {
                importImage(subWzDir, path);
            }
        }
    }

    private void importImage(WzDirectory wzDir, Path imageFile) throws IOException, SAXException, ParserConfigurationException {
        if (wzDir.get(imageFile.toFile().getName()) == null) {
            WzImage wzImage = new WzImage(imageFile.toFile().getName().replace(".xml", ""));
            wzDir.addImage(wzImage);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            try {
                Document doc = dBuilder.parse(imageFile.toFile());
                readPropertyFromXml(wzImage, (Element) doc.getElementsByTagName("imgdir").item(0));
            } catch (Exception ex) {
                log.error(imageFile.getFileName().toString(), ex);
            }
        }
    }

    private void readPropertyFromXml(WzObject wzObject, Element imageNode) {
        List<Element> list = getChildNodes(imageNode);

        list.forEach(propNode -> {
            String propType = propNode.getTagName();
            String propName = propNode.getAttribute("name");

            WzImageProperty subProp = null;

            switch (propType) {
                case "imgdir":
                    subProp = new WzSubProperty(propName);
                    break;
                case "canvas":
                    subProp = new WzCanvasProperty(propName);
                    int width = Integer.parseInt(propNode.getAttribute("width"));
                    int height = Integer.parseInt(propNode.getAttribute("height"));
                    subProp.setValue(
                            new Size(width, height)
                    );
                    break;
                case "int":
                    subProp = new WzIntProperty(propName);
                    int intVal = Integer.parseInt(propNode.getAttribute("value"));
                    subProp.setValue(intVal);
                    break;
                case "double":
                    subProp = new WzDoubleProperty(propName);
                    double doubleVal = Double.parseDouble(propNode.getAttribute("value"));
                    subProp.setValue(doubleVal);
                    break;
                case "null":
                    subProp = new WzNullProperty(propName);
                    break;
                case "sound":
                    subProp = new WzSoundProperty(propName);
                    break;
                case "string":
                    subProp = new WzStringProperty(propName);
                    String strVal = propNode.getAttribute("value");
                    subProp.setValue(strVal);
                    break;
                case "short":
                    subProp = new WzShortProperty(propName);
                    short shortVal = Short.parseShort(propNode.getAttribute("value"));
                    subProp.setValue(shortVal);
                    break;
                case "long":
                    subProp = new WzLongProperty(propName);
                    long longVal = Long.parseLong(propNode.getAttribute("value"));
                    subProp.setValue(longVal);
                    break;
                case "uol":
                    subProp = new WzUOLProperty(propName);
                    String link = propNode.getAttribute("value");
                    subProp.setValue(link);
                    break;
                case "vector":
                    subProp = new WzVectorProperty(propName);
                    int x = Integer.parseInt(propNode.getAttribute("x"));
                    int y = Integer.parseInt(propNode.getAttribute("y"));
                    subProp.setValue(new Point(x, y));
                    break;
                case "float":
                    subProp = new WzFloatProperty(propName);
                    float floatVal = Float.parseFloat(propNode.getAttribute("value"));
                    subProp.setValue(floatVal);
                    break;
                case "extended":
                    subProp = new WzConvexProperty(propName);
                    break;
            }

            if (subProp != null) {
                if (wzObject instanceof WzImage) {
                    ((WzImage) wzObject).addProperty(subProp);
                    readPropertyFromXml(subProp, propNode);
                } else if (wzObject instanceof IPropertyContainer) {
                    ((IPropertyContainer) wzObject).addProperty(subProp);
                    readPropertyFromXml(subProp, propNode);
                }
            }

        });
    }


    public WzFile deserializeWzFile(Path wzPath) throws IOException, SAXException, ParserConfigurationException {
        if (!wzPath.toFile().getName().endsWith("wz"))
            throw new RuntimeException("The path of wz should ended with .wz");
        else if (!Files.isDirectory(wzPath)) {
            throw new RuntimeException("The path is not a directory");
        } else if (!Files.exists(wzPath)) {
            throw new RuntimeException("The path is not exists");
        }

        WzFile wz = new WzFile(wzPath.toFile().getName());
        importWz(wz, wzPath);
        return wz;
    }


}
