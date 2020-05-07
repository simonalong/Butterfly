package com.simonalong.butterfly.worker.db;

import com.simonalong.butterfly.sequence.ButterflyConfig;
import com.simonalong.butterfly.sequence.WorkerLoader;
import com.simonalong.butterfly.sequence.spi.WorkerIdHandler;
import com.simonalong.neo.Neo;
import lombok.extern.slf4j.Slf4j;

import static com.simonalong.butterfly.worker.db.DbConstant.DB_LOG_PRE;

/**
 * @author shizi
 * @since 2020/4/25 12:49 AM
 */
@Slf4j
public class DbWorkerLoader implements WorkerLoader {

    private static Neo db;

    @Override
    public boolean acceptConfig(ButterflyConfig butterflyConfig) {
        if (null == butterflyConfig) {
            return false;
        }
        if (!(butterflyConfig instanceof DbButterflyConfig)) {
            return false;
        }

        try {
            DbButterflyConfig dbButterflyConfig = (DbButterflyConfig) butterflyConfig;
            String url = dbButterflyConfig.getUrl();
            String userName = dbButterflyConfig.getUserName();
            String password = dbButterflyConfig.getPassword();
            db = Neo.connect(url, userName, password);
        } catch (Throwable e) {
            log.error(DB_LOG_PRE + "config is illegal ", e);
        }
        return true;
    }

    @Override
    public WorkerIdHandler loadIdHandler(String namespace, ButterflyConfig butterflyConfig) {
        return new DbWorkerIdHandler(namespace, db);
    }
}
