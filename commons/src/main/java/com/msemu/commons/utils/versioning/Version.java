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

package com.msemu.commons.utils.versioning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.Attributes;

/**
 * Created by Weber on 2018/3/14.
 */
public class Version {
    private static final Logger log = LoggerFactory.getLogger(Version.class);
    private static final AtomicReference<Version> instance = new AtomicReference<>();
    private String versionRevision;
    private String buildDate = "";
    private String buildJdk = "";

    public Version() {
    }

    public static Version getInstance() {
        Version value = instance.get();
        if (value == null) {
            AtomicReference var1 = instance;
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new Version();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    public void init(Class<?> c) {
//        File jarName = null;
//
//        try {
//            jarName = Locator.getClassSource(c);
//            if (StringUtils.endsWith(jarName.getName(), ".jar")) {
//                JarFile e = new JarFile(jarName);
//                Attributes attrs = e.getManifest().getMainAttributes();
//                this.setBuildJdk(attrs);
//                this.setBuildDate(attrs);
//                this.setVersionRevision(attrs);
//            } else {
//                this.versionRevision = "Dev";
//                this.buildDate = "N/A";
//                this.buildJdk = System.getProperty("java.version");
//            }
//        } catch (Exception var8) {
//            log.error("Unable to get soft information\nFile name \'" + jarName.getAbsolutePath() + "\' isn\'t a valid jar", var8);
//        } finally {
//            this.info();
//        }

    }

    public String getVersionRevision() {
        return this.versionRevision;
    }

    private void setVersionRevision(Attributes attrs) {
        String versionRevision = attrs.getValue("Implementation-Version");
        if (versionRevision != null && !versionRevision.isEmpty()) {
            this.versionRevision = versionRevision;
        } else {
            this.versionRevision = "N/A";
        }

    }

    public String getBuildDate() {
        return this.buildDate;
    }

    private void setBuildDate(Attributes attrs) {
        String buildDate = attrs.getValue("Built-Date");
        if (buildDate != null) {
            this.buildDate = buildDate;
        } else {
            this.buildDate = "-1";
        }

    }

    public String getBuildJdk() {
        return this.buildJdk;
    }

    private void setBuildJdk(Attributes attrs) {
        String buildJdk = attrs.getValue("Built-JDK");
        if (buildJdk != null) {
            this.buildJdk = buildJdk;
        } else {
            buildJdk = attrs.getValue("Source-Compatibility");
            if (buildJdk != null) {
                this.buildJdk = buildJdk;
            } else {
                this.buildJdk = "-1";
            }
        }

    }

    private void info() {
        log.info("=================================================");
        log.info("Revision: ................ " + this.getVersionRevision());
        log.info("Build date: .............. " + this.getBuildDate());
        log.info("=================================================");
    }

    public String toString() {
        return "Version " + this.versionRevision + " from " + this.buildDate;
    }
}
