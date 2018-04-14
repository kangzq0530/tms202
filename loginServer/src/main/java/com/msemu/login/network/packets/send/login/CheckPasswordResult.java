package com.msemu.login.network.packets.send.login;

import com.msemu.commons.enums.OutHeader;
import com.msemu.commons.network.packets.OutPacket;
import com.msemu.commons.utils.types.FileTime;
import com.msemu.login.client.Account;
import com.msemu.login.constants.JobConstants;
import com.msemu.login.enums.LoginJob;
import com.msemu.login.enums.LoginResultType;

/**
 * Created by Weber on 2018/3/30.
 */
public class CheckPasswordResult extends OutPacket {

    public CheckPasswordResult(Account account, LoginResultType status, FileTime dtUnblockDate) {
        super(OutHeader.CheckPasswordResult);
        encodeByte(status.getValue());
        if (status == LoginResultType.LoginSuccess || status == LoginResultType.Unknown) {
            encodeString(account.getUsername());
            encodeInt(account.getId());
            encodeByte(account.getGender());
            encodeByte(account.getMsg2());
            encodeInt(account.getAccountType());
            encodeInt(account.getAge());
            encodeInt(0);
            encodeInt(0x23);
            encodeByte(account.getCharacterSlots() + 3);
            encodeByte(account.getpBlockReason());
            encodeLong(account.getChatUnblockDate());
            encodeByte(0);
            encodeLong(account.getChatUnblockDate());
            encodeByte(0);
            encodeString(account.getSecureUserName());
            encodeString("");
            encodeByte(JobConstants.enableJobs);
            if (JobConstants.enableJobs) {
                encodeByte(JobConstants.jobOrder);
                for(LoginJob job : LoginJob.values()) {
                    encodeByte(job.enableCreate());
                    encodeShort(1);
                }
            }

            encodeByte(1);
            encodeInt(-1);
            encodeByte(0);

        } else if (status == LoginResultType.BlockForIndecencyBehavior) {
            encodeByte(dtUnblockDate != null);
            encodeFT(dtUnblockDate);
        }
    }
}
