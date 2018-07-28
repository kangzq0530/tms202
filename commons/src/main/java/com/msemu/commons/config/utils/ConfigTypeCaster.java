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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

/**
 * Created by Weber on 2018/3/13.
 */
public class ConfigTypeCaster {
    private static final Logger log = LoggerFactory.getLogger(ConfigTypeCaster.class);
    private static final Class[] _allowedTypes;

    static {
        _allowedTypes = new Class[]{Integer.class, Integer.TYPE, Short.class, Short.TYPE, Float.class, Float.TYPE, Double.class, Double.TYPE, Long.class, Long.TYPE, Boolean.class, Boolean.TYPE, String.class, Character.class, Character.TYPE, Byte.class, Byte.TYPE, AtomicInteger.class, AtomicBoolean.class, AtomicLong.class, BigInteger.class, BigDecimal.class, Path.class, Pattern.class, Duration.class};
    }

    public ConfigTypeCaster() {
    }

    public static void cast(Object object, Field field, String value) throws IllegalAccessException {
        if (!isCastable(field)) {
            log.error("Unsupported type [{}] for field [{}]", field.getType().getName(), field.getName());
        } else {
            Class type = field.getType();
            boolean oldAccess = field.isAccessible();
            field.setAccessible(true);
            if (type.isEnum()) {
                field.set(object, Enum.valueOf(type, value));
            } else if (type != Integer.class && type != Integer.TYPE) {
                if (type != Short.class && type != Short.TYPE) {
                    if (type != Float.class && type != Float.TYPE) {
                        if (type != Double.class && type != Double.TYPE) {
                            if (type != Long.class && type != Long.TYPE) {
                                if (type != Boolean.class && type != Boolean.TYPE) {
                                    if (type == String.class) {
                                        field.set(object, value);
                                    } else if (type != Character.class && type != Character.TYPE) {
                                        if (type != Byte.class && type != Byte.TYPE) {
                                            if (type == AtomicInteger.class) {
                                                field.set(object, new AtomicInteger(Integer.decode(value)));
                                            } else if (type == AtomicBoolean.class) {
                                                field.set(object, new AtomicBoolean(Boolean.parseBoolean(value)));
                                            } else if (type == AtomicLong.class) {
                                                field.set(object, new AtomicLong(Long.decode(value)));
                                            } else if (type == BigInteger.class) {
                                                field.set(object, new BigInteger(value));
                                            } else if (type == BigDecimal.class) {
                                                field.set(object, new BigDecimal(value));
                                            } else if (type == Path.class) {
                                                field.set(object, Paths.get(value));
                                            } else if (type == Pattern.class) {
                                                field.set(object, Pattern.compile(value));
                                            } else if (type == Duration.class) {
                                                field.set(object, Duration.parse(value));
                                            } else {
                                                field.setAccessible(oldAccess);
                                                log.error("Unsupported type [{}] for field [{}]", field.getType().getName(), field.getName());
                                            }
                                        } else {
                                            field.set(object, Byte.parseByte(value));
                                        }
                                    } else {
                                        field.set(object, value.charAt(0));
                                    }
                                } else {
                                    field.set(object, Boolean.parseBoolean(value));
                                }
                            } else {
                                field.set(object, Long.decode(value));
                            }
                        } else {
                            field.set(object, Double.parseDouble(value));
                        }
                    } else {
                        field.set(object, Float.parseFloat(value));
                    }
                } else {
                    field.set(object, Short.decode(value));
                }
            } else {
                field.set(object, Integer.decode(value));
            }

            field.setAccessible(oldAccess);
        }
    }

    public static <T> T cast(final Class<T> type, String value) throws IllegalAccessException {
        if (!isCastable(type)) {
            log.error("Unsupported type [{}]", type.getName());
            return null;
        } else if (type.isEnum()) {
            Enum<?> convertedValue = Enum.valueOf((Class<Enum>) type, value);
            return type.cast(convertedValue);
        } else if (type != Integer.class && type != Integer.TYPE) {
            if (type != Short.class && type != Short.TYPE) {
                if (type != Float.class && type != Float.TYPE) {
                    if (type != Double.class && type != Double.TYPE) {
                        if (type != Long.class && type != Long.TYPE) {
                            if (type != Boolean.class && type != Boolean.TYPE) {
                                if (type == String.class) {
                                    return type.cast(value);
                                } else if (type != Character.class && type != Character.TYPE) {
                                    if (type != Byte.class && type != Byte.TYPE) {
                                        if (type == AtomicInteger.class) {
                                            return type.cast(new AtomicInteger(Integer.decode(value)));
                                        } else if (type == AtomicBoolean.class) {
                                            return type.cast(new AtomicBoolean(Boolean.parseBoolean(value)));
                                        } else if (type == AtomicLong.class) {
                                            return type.cast(new AtomicLong(Long.decode(value)));
                                        } else if (type == BigInteger.class) {
                                            return type.cast(new BigInteger(value));
                                        } else if (type == BigDecimal.class) {
                                            return type.cast(new BigDecimal(value));
                                        } else if (type == Path.class) {
                                            return type.cast(Paths.get(value));
                                        } else if (type == Pattern.class) {
                                            return type.cast(Pattern.compile(value));
                                        } else if (type == Duration.class) {
                                            return type.cast(Duration.parse(value));
                                        } else {
                                            log.error("Unsupported type [{}]", type.getName());
                                            return null;
                                        }
                                    } else {
                                        return type.cast(Byte.class.cast(Byte.decode(value)));
                                    }
                                } else {
                                    return type.cast(value.charAt(0));
                                }
                            } else {
                                return type.cast(Boolean.class.cast(Boolean.parseBoolean(value)));
                            }
                        } else {
                            return type.cast(Long.class.cast(Long.decode(value)));
                        }
                    } else {
                        return type.cast(Double.class.cast(Double.parseDouble(value)));
                    }
                } else {
                    return type.cast(Float.class.cast(Float.parseFloat(value)));
                }
            } else {
                return type.cast(Short.class.cast(Short.decode(value)));
            }
        } else {
            return type.cast(Integer.class.cast(Integer.decode(value)));
        }
    }

    public static boolean isCastable(Class type) {
        if (type.isEnum()) {
            return true;
        } else {
            Class[] types = _allowedTypes;
            for (Class t : types) {
                if (t == type) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean isCastable(Object object) {
        return isCastable(object.getClass());
    }

    public static boolean isCastable(Field field) {
        return isCastable(field.getType());
    }
}