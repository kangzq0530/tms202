package com.msemu.commons.data.loader.dat;

import com.msemu.commons.wz.WzFile;
import com.msemu.core.configs.CoreConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Weber on 2018/4/27.
 */
public class DatManager {
    public static final String Equip = "equip.dat";
    public static final String Field = "field.dat";
    public static final String Item = "item.dat";
    public static final String QuestInfo = "quest.dat";
    public static final String SkillInfo = "skill.dat";
    public static final String Npc = "npc.dat";
    public static final String Mob = "mob.dat";
    public static final String ItemOption = "itemOption.dat";
    public static final String ForbiddenName = "forbiddenName.dat";
}
