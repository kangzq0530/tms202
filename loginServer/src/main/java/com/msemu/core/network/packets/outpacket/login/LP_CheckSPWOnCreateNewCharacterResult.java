package com.msemu.core.network.packets.outpacket.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.core.network.LoginClient;
import com.msemu.login.constants.JobConstants;
import com.msemu.login.enums.CharCreateInfo;
import com.msemu.login.enums.LoginResultCode;

/**
 * Created by Weber on 2018/4/19.
 */
public class LP_CheckSPWOnCreateNewCharacterResult extends OutPacket<LoginClient> {

    public LP_CheckSPWOnCreateNewCharacterResult(LoginResultCode type) {
        super(OutHeader.LP_CheckSPWOnCreateNewCharacterResult);

        encodeByte(type.getValue());

        if (type == LoginResultCode.LoginSuccess) {
            encodeByte(JobConstants.enableJobs);
            if (JobConstants.enableJobs) {
                encodeByte(JobConstants.jobOrder);
                for (CharCreateInfo job : CharCreateInfo.values()) {
                    encodeByte(job.enableCreate());
                    encodeShort(1);
                }
            }
        } else if (type == LoginResultCode.VerifyWithCaptcha) {
            //TODO 生成CAPTCHA
        }

    }
}
