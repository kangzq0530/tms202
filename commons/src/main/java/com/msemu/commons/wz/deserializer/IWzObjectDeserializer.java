package com.msemu.commons.wz.deserializer;

import org.w3c.dom.Node;

/**
 * Created by Weber on 2018/4/20.
 */
public interface IWzObjectDeserializer {
    void deserializeObject(Node node, String path);
}
