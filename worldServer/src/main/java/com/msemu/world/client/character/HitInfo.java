package com.msemu.world.client.character;

/**
 * Created by Weber on 2018/4/12.
 */
public class HitInfo {

    private int HPDamage;
    private int templateID;
    private int mobID;
    private int MPDamage;

    public int getHPDamage() {
        return HPDamage;
    }

    public void setHPDamage(int HPDamage) {
        this.HPDamage = HPDamage;
    }

    public int getTemplateID() {
        return templateID;
    }

    public void setTemplateID(int templateID) {
        this.templateID = templateID;
    }

    public int getMobID() {
        return mobID;
    }

    public void setMobID(int mobID) {
        this.mobID = mobID;
    }

    public int getMPDamage() {
        return MPDamage;
    }

    public void setMPDamage(int MPDamage) {
        this.MPDamage = MPDamage;
    }
}
