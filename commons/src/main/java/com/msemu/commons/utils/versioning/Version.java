package com.msemu.commons.utils.versioning;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

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
        File jarName = null;

        try {
            jarName = Locator.getClassSource(c);
            if (StringUtils.endsWith(jarName.getName(), ".jar")) {
                JarFile e = new JarFile(jarName);
                Attributes attrs = e.getManifest().getMainAttributes();
                this.setBuildJdk(attrs);
                this.setBuildDate(attrs);
                this.setVersionRevision(attrs);
            } else {
                this.versionRevision = "Dev";
                this.buildDate = "N/A";
                this.buildJdk = System.getProperty("java.version");
            }
        } catch (IOException var8) {
            log.error("Unable to get soft information\nFile name \'" + jarName.getAbsolutePath() + "\' isn\'t a valid jar", var8);
        } finally {
            this.info();
        }

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
