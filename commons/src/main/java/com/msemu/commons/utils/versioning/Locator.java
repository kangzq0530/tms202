package com.msemu.commons.utils.versioning;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.StringCharacterIterator;
import java.util.Locale;

/**
 * Created by Weber on 2018/3/14.
 */
public final class Locator {
    private Locator() {
    }

    public static File getClassSource(Class<?> c) {
        String classResource = c.getName().replace('.', '/') + ".class";
        return getResourceSource(c.getClassLoader(), classResource);
    }

    public static File getResourceSource(ClassLoader c, String resource) {
        if (c == null) {
            c = Locator.class.getClassLoader();
        }

        URL url;
        if (c == null) {
            url = ClassLoader.getSystemResource(resource);
        } else {
            url = c.getResource(resource);
        }

        if (url != null) {
            String u = url.toString();
            int tail;
            String dirName;
            if (u.startsWith("jar:file:")) {
                tail = u.indexOf("!");
                dirName = u.substring(4, tail);
                return new File(fromURI(dirName));
            }

            if (u.startsWith("file:")) {
                tail = u.indexOf(resource);
                dirName = u.substring(0, tail);
                return new File(fromURI(dirName));
            }
        }

        return null;
    }

    public static String fromURI(String uri) {
        URL url = null;

        try {
            url = new URL(uri);
        } catch (MalformedURLException var5) {
            ;
        }

        if (url != null && "file".equals(url.getProtocol())) {
            StringBuilder buf = new StringBuilder(url.getHost());
            if (buf.length() > 0) {
                buf.insert(0, File.separatorChar).insert(0, File.separatorChar);
            }

            String file = url.getFile();
            int queryPos = file.indexOf(63);
            buf.append(queryPos < 0 ? file : file.substring(0, queryPos));
            uri = buf.toString().replace('/', File.separatorChar);
            if (File.pathSeparatorChar == 59 && uri.startsWith("\\") && uri.length() > 2 && Character.isLetter(uri.charAt(1)) && uri.lastIndexOf(58) > -1) {
                uri = uri.substring(1);
            }

            return decodeUri(uri);
        } else {
            throw new IllegalArgumentException("Can only handle valid file: URIs");
        }
    }

    private static String decodeUri(String uri) {
        if (uri.indexOf(37) == -1) {
            return uri;
        } else {
            StringBuilder sb = new StringBuilder();
            StringCharacterIterator iter = new StringCharacterIterator(uri);

            for (char c = iter.first(); c != '\uffff'; c = iter.next()) {
                if (c == 37) {
                    char c1 = iter.next();
                    if (c1 != '\uffff') {
                        int i1 = Character.digit(c1, 16);
                        char c2 = iter.next();
                        if (c2 != '\uffff') {
                            int i2 = Character.digit(c2, 16);
                            sb.append((char) ((i1 << 4) + i2));
                        }
                    }
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        }
    }

    public static File getToolsJar() {
        boolean toolsJarAvailable = false;

        try {
            Class.forName("com.sun.tools.javac.Main");
            toolsJarAvailable = true;
        } catch (Exception var4) {
            try {
                Class.forName("sun.tools.javac.Main");
                toolsJarAvailable = true;
            } catch (Exception var3) {
                ;
            }
        }

        if (toolsJarAvailable) {
            return null;
        } else {
            String javaHome = System.getProperty("java.home");
            if (javaHome.toLowerCase(Locale.US).endsWith("jre")) {
                javaHome = javaHome.substring(0, javaHome.length() - 4);
            }

            File toolsJar = new File(javaHome + "/lib/tools.jar");
            if (!toolsJar.exists()) {
                System.out.println("Unable to locate tools.jar. Expected to find it in " + toolsJar.getPath());
                return null;
            } else {
                return toolsJar;
            }
        }
    }

    public static URL[] getLocationURLs(File location) throws MalformedURLException {
        return getLocationURLs(location, new String[]{".jar"});
    }

    public static URL[] getLocationURLs(File location, String[] extensions) throws MalformedURLException {
        URL[] urls = new URL[0];
        if (!location.exists()) {
            return urls;
        } else if (!location.isDirectory()) {
            urls = new URL[1];
            String var8 = location.getPath();
            String[] var9 = extensions;
            int var5 = extensions.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                String extension = var9[var6];
                if (var8.toLowerCase().endsWith(extension)) {
                    urls[0] = location.toURI().toURL();
                    break;
                }
            }

            return urls;
        } else {
            File[] matches = location.listFiles((dir, name) -> {
                String[] var3 = extensions;
                int var4 = extensions.length;

                for (int var5 = 0; var5 < var4; ++var5) {
                    String extension = var3[var5];
                    if (name.toLowerCase().endsWith(extension)) {
                        return true;
                    }
                }

                return false;
            });
            urls = new URL[matches.length];

            for (int i = 0; i < matches.length; ++i) {
                urls[i] = matches[i].toURI().toURL();
            }

            return urls;
        }
    }
}
