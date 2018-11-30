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

package com.msemu.core.configs;

import com.msemu.commons.config.annotation.ConfigComments;
import com.msemu.commons.config.annotation.ConfigFile;
import com.msemu.commons.config.annotation.ConfigProperty;
import com.msemu.commons.reload.Reloadable;

@Reloadable(name = "world",
        group = "all")
@ConfigFile(name = "configs/debug.properties")
public class DebugConfig {

    @ConfigComments(comment = {"Show the information of damage calculation", "Default: true"})
    @ConfigProperty(name = "debug.showCalculateDamage", value = "true")
    public static boolean SHOW_CALC_DAMAGE_INFO = true;

    @ConfigComments(comment = {"Show the information of attack", "Default: true"})
    @ConfigProperty(name = "debug.showAttackInfo", value = "true")
    public static boolean SHOW_ATTACK_INFO = true;

    @ConfigComments(comment = {"Show the information of character's damage", "Default: true"})
    @ConfigProperty(name = "debug.showCharacterDamageInfo", value = "true")
    public static boolean SHOW_CHARACTER_DAMAGE_INFO = true;

}
