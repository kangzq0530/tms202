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

/**
 * Created by Weber on 2018/3/14.
 */
@ConfigFile(name = "configs/login.properties")
public class LoginConfig {


    @ConfigComments(comment = { "Automatic registration for accounts.", "Default: false" })
    @ConfigProperty(name = "login.accounts.autoreg", value = "false")
    public static boolean AUTO_REGISTRATION;

    @ConfigComments(comment = { "Check second password on select character", "Default: false" })
    @ConfigProperty(name = "login.accounts.checkSPW", value = "false")
    public static boolean CHECK_SECOND_PASSWORD;
}
