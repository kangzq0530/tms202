package com.msemu.login.data;

import com.msemu.commons.data.loader.dat.ForbiddenNameDatLoader;
import com.msemu.commons.data.loader.wz.ForbiddenNameLoader;
import com.msemu.commons.data.loader.wz.WzManager;
import com.msemu.commons.data.templates.ForbiddenName;
import com.msemu.commons.reload.IReloadable;
import com.msemu.commons.reload.Reloadable;
import com.msemu.core.startup.StartupComponent;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimeTypeParameterList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/4/21.
 */
@Reloadable(name = "string", group = "all")
@StartupComponent("Data")
public class StringData implements IReloadable {
    private static final Logger log = LoggerFactory.getLogger(StringData.class);

    @Getter
    private ForbiddenNameDatLoader forbiddenNameDatLoader = new ForbiddenNameDatLoader();

    private static final AtomicReference<StringData> instance = new AtomicReference<>();


    public static StringData getInstance() {
        StringData value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new StringData();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    public StringData() {
        reload();
    }

    private void clear() {
        //TODO
    }

    private void load() {

        getForbiddenNameDatLoader().load();
        log.info("{} forbiddenName loaded.", getForbiddenNameDatLoader().getData().getNames().size());
    }

    @Override
    public void reload() {
        clear();
        load();
    }

    public ForbiddenName getForbiddenName() {
        return getForbiddenNameDatLoader().getData();
    }
}
