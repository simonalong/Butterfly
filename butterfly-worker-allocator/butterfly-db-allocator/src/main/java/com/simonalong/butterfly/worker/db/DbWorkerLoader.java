package com.simonalong.butterfly.worker.db;

import com.simonalong.butterfly.sequence.ButterflyConfig;
import com.simonalong.butterfly.sequence.WorkerLoader;
import com.simonalong.butterfly.sequence.exception.ButterflyException;
import com.simonalong.butterfly.sequence.spi.WorkerIdHandler;
import com.simonalong.neo.Neo;

/**
 * @author shizi
 * @since 2020/4/25 12:49 AM
 */
public class DbWorkerLoader implements WorkerLoader {

    @Override
    public Boolean configAvailable(ButterflyConfig butterflyConfig) {
        if (null == butterflyConfig) {
            return false;
        }
        return butterflyConfig instanceof DbButterflyConfig;
    }

    @Override
    public WorkerIdHandler loadIdHandler(String namespace, ButterflyConfig butterflyConfig) {
        if (!configAvailable(butterflyConfig)) {
            throw new ButterflyException("the config is available for db：" + butterflyConfig);
        }

        DbButterflyConfig dbButterflyConfig = (DbButterflyConfig) butterflyConfig;
        String url = dbButterflyConfig.getUrl();
        String userName = dbButterflyConfig.getUserName();
        String password = dbButterflyConfig.getPassword();

        return new DbWorkerIdHandler(namespace, Neo.connect(url, userName, password));
    }
}
