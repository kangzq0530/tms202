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

package com.msemu.commons.config.utils;

import com.msemu.commons.config.annotation.ConfigAfterLoad;
import com.msemu.commons.config.annotation.ConfigComments;
import com.msemu.commons.config.annotation.ConfigFile;
import com.msemu.commons.config.annotation.ConfigProperty;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.core.startup.StartupComponent;
import org.apache.commons.lang3.StringUtils;
import org.atteo.classindex.ClassIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/13.
 */

@Reloadable(
        name = "all",
        group = "configs"
)
@StartupComponent("Configure")
public class ConfigLoader implements IReloadable {
    private static final Logger log = LoggerFactory.getLogger(ConfigLoader.class);
    private static final AtomicReference<ConfigLoader> instance = new AtomicReference<>();
    private static Set<String> VAR_NAMES_CACHE = new HashSet<>();

    private ConfigLoader() {
        this.loadConfigs();
    }

    public static ConfigLoader getInstance() {
        ConfigLoader value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new ConfigLoader();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    private void loadConfigs() {
        for (Class<?> aClass : ClassIndex.getAnnotated(ConfigFile.class)) {
            Class clazz = (Class) aClass;
            boolean loadConfig = false;
            ConfigFile annotation = (ConfigFile) clazz.getAnnotation(ConfigFile.class);
            if (annotation.loadForPackages().length > 0) {
                String[] packages = annotation.loadForPackages();
                for (String classPath : packages) {
                    if (!StringUtils.isEmpty(classPath)) {
                        URL url = this.getClass().getClassLoader().getResource(classPath.replace(".", "/"));
                        if (url != null) {
                            loadConfig = true;
                            break;
                        }
                    }
                }
            } else {
                loadConfig = true;
            }

            if (loadConfig) {
                File configPath = new File(annotation.name());
                if (!configPath.exists() && configPath.isDirectory()) {
                    configPath.mkdirs();
                }
                if (!configPath.exists()) {
                    this.buildConfig(clazz);
                } else {
                    this.updateConfig(clazz);
                }

                this.loadConfig(clazz);
            }
        }

    }

    public void reload() {
        this.loadConfigs();
    }

    private void updateConfig(Class<?> clazz) {
        Properties properties = new Properties();
        String fileName = clazz.getAnnotation(ConfigFile.class).name();

        try {
            FileInputStream out = new FileInputStream(fileName);
            Throwable path = null;

            try {
                properties.load(out);
            } catch (Throwable loadExcept) {
                path = loadExcept;
                throw loadExcept;
            } finally {
                if (path != null) {
                    try {
                        out.close();
                    } catch (Throwable var18) {
                        path.addSuppressed(var18);
                    }
                } else {
                    out.close();
                }
            }
        } catch (IOException var21) {
            log.error("Error while calling loadConfig", var21);
        }

        if (properties.size() != clazz.getDeclaredFields().length) {
            StringBuilder builder = new StringBuilder();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
                String propertyValue = properties.getProperty(annotation.name());
                if (propertyValue == null && !annotation.isMap()) {
                    builder.append("\n\n").append(this.generateFieldConfig(field));
                    log.info("Updated \'{}\' configs with new field \'{}\'", fileName, annotation.name());
                }
            }
            if (builder.length() > 0) {
                Path filePath = Paths.get(fileName);
                try {
                    Files.write(filePath, builder.toString().getBytes(), StandardOpenOption.APPEND);
                } catch (Exception var17) {
                    log.error("Error while writing configs update", var17);
                }
            }

        }
    }

    private void buildConfig(Class<?> clazz) {
        String fileName = clazz.getAnnotation(ConfigFile.class).name();
        Path path = Paths.get(fileName);
        log.info("Generated \'{}\'", fileName);
        try {
            Files.deleteIfExists(path);
            Files.createDirectories(path.getParent());
        } catch (IOException var11) {
            log.error("Error while buildConfig()", var11);
            return;
        }
        StringBuilder out = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String configFields = this.generateFieldConfig(field);
            if (!configFields.isEmpty()) {
                out.append("\n").append(configFields);
            }
        }
        try {
            Files.write(path, out.toString().getBytes(), StandardOpenOption.CREATE);
        } catch (IOException var10) {
            if (log.isErrorEnabled()) {
                log.error("Error while writing configs file: " + path, var10);
            }
        }

    }

    private String generateFieldConfig(Field field) {
        ConfigComments configComments = field.getAnnotation(ConfigComments.class);
        ConfigProperty configProperty = field.getAnnotation(ConfigProperty.class);
        StringBuilder out = new StringBuilder();
        String value;
        if (configComments != null && configComments.comment().length > 0) {
            String[] comments = configComments.comment();
            for (String comment : comments) {
                value = comment;
                out.append("\n# ").append(value);
            }
        }

        if (VAR_NAMES_CACHE.contains(configProperty.name())) {
            log.warn("Config property name [{}] already defined!", configProperty.name());
        } else {
            VAR_NAMES_CACHE.add(configProperty.name());
        }

        if (configProperty.isMap()) {
            String[] values = configProperty.values();
            for (String value1 : values) {
                value = value1;
                out.append("\n").append(value);
            }
        } else {
            out.append("\n").append(configProperty.name()).append(" = ").append(configProperty.value());
        }

        return out.toString();
    }

    private void loadConfig(Class<?> clazz) {
        Properties properties = new Properties();
        String fileName = clazz.getAnnotation(ConfigFile.class).name();
        log.info("Loading configs file: {}", fileName);

        try {
            FileInputStream e = new FileInputStream(fileName);
            Throwable except = null;
            try {
                properties.load(new InputStreamReader(e, Charset.forName("UTF-8")));
            } catch (Throwable loadExcept) {
                except = loadExcept;
                throw loadExcept;
            } finally {
                if (except != null) {
                    try {
                        e.close();
                    } catch (Throwable var30) {
                        except.addSuppressed(var30);
                    }
                } else {
                    e.close();
                }
            }
        } catch (IOException var34) {
            log.error("Error while calling loadConfig", var34);
        }

        try {
            Object object = clazz.newInstance();
            Field[] fields = clazz.getFields();
            int i = 0;
            while (true) {
                if (i >= fields.length) {
                    Method[] methods = clazz.getDeclaredMethods();
                    for (i = 0; i < methods.length; ++i) {
                        Method method = methods[i];
                        if (method.isAnnotationPresent(ConfigAfterLoad.class)) {
                            boolean isAccessible = method.isAccessible();
                            method.setAccessible(true);
                            try {
                                method.invoke(object);
                            } catch (Exception var28) {
                                log.error("Error while calling ConfigAfterLoad method", var28);
                            } finally {
                                try {
                                    method.setAccessible(isAccessible);
                                } catch (Exception ignore) {
                                }
                            }
                        }
                    }
                    break;
                }

                Field method = fields[i];
                ConfigProperty isMethodAccessible = method.getAnnotation(ConfigProperty.class);
                if (isMethodAccessible != null) {
                    if (!Modifier.isStatic(method.getModifiers()) || Modifier.isFinal(method.getModifiers())) {
                        log.warn("Invalid modifiers for {}", method);
                        return;
                    }

                    this.setConfigValue(object, method, properties, isMethodAccessible);
                }

                ++i;
            }
        } catch (Exception var32) {
            log.error("Error while initializing configs object", var32);
        }

    }

    private void setConfigValue(Object object, Field field, Properties properties, ConfigProperty annotation) {
        String propertyValue = properties.getProperty(annotation.name(), annotation.value());

        try {
            Class e;
            String[] mapPrefix;
            if (field.getType().isArray()) {
                int entry;
                e = field.getType().getComponentType();
                if (propertyValue != null) {
                    mapPrefix = propertyValue.split(annotation.splitter());
                    Object array = Array.newInstance(e, mapPrefix.length);
                    field.set(null, array);
                    entry = 0;
                    String[] name = mapPrefix;
                    int key = mapPrefix.length;
                    for (int value = 0; value < key; ++value) {
                        String arrValue = name[value];
                        Array.set(array, entry, ConfigTypeCaster.cast(e, arrValue.trim()));
                        ++entry;
                    }
                    field.set(null, array);
                }
            } else {
                if (field.getType().isAssignableFrom(List.class)) {
                    e = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    if (propertyValue != null) {
                        mapPrefix = propertyValue.split(annotation.splitter());
                        ((List<?>) field.get(object)).clear();
                        for (String aMapPrefix : mapPrefix) {
                            if (!aMapPrefix.trim().isEmpty()) {
                                ((List<Object>) field.get(object)).add(ConfigTypeCaster.cast(e, aMapPrefix.trim()));
                            }
                        }
                    }
                } else if (field.getType().isAssignableFrom(Map.class)) {
                    ((Map) field.get(object)).clear();
                    e = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[1];
                    String var15 = annotation.name();

                    for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                        String key = (String) entry.getKey();
                        if (key.startsWith(var15)) {
                            String akey = key.replace(var15 + ".", "");
                            Object avalue = ConfigTypeCaster.cast(e, ((String) entry.getValue()).trim());
                            ((Map<String, Object>) field.get(object)).put(akey, avalue);
                        }
                    }
                } else {
                    ConfigTypeCaster.cast(object, field, propertyValue);
                }
            }
        } catch (IllegalAccessException var14) {
            log.error("Invalid modifiers for field {}", field);
        }

    }
}
