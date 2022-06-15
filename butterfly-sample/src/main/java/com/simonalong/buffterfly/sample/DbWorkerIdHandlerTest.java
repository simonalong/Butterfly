package com.simonalong.buffterfly.sample;

import com.simonalong.butterfly.sequence.ButterflyIdGenerator;
import com.simonalong.butterfly.worker.db.DbButterflyConfig;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class DbWorkerIdHandlerTest {

    private static ButterflyIdGenerator generator;
    private static final String DEFAULT_NAMESPACE = "ID";

    @BeforeClass
    public static void beforeClass() {
        DbButterflyConfig config = new DbButterflyConfig();
        config.setUrl("jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false&&allowPublicKeyRetrieval=true");
        config.setUserName("neo_test");
        config.setPassword("neo@Test123");
        config.setAutoCreateTable(true);

        generator = ButterflyIdGenerator.getInstance(config);

        generator.setStartTime(2020, 5, 1, 0, 0, 0);
        generator.addNamespaces(DEFAULT_NAMESPACE);
    }

    @Test
    public void should_get_uuid_successfully_given_when_auto_create_table() {
        generator.getUUid(DEFAULT_NAMESPACE);
    }

    @Test
    public void should_get_uuid_fail_given_empty_database_when_do_not_auto_create_table() {
        DbButterflyConfig config = new DbButterflyConfig();
        config.setUrl("jdbc:mysql://127.0.0.1:3306/neo_new?useUnicode=true&characterEncoding=UTF-8&useSSL=false&&allowPublicKeyRetrieval=true");
        config.setUserName("neo_test");
        config.setPassword("neo@Test123");

        generator = ButterflyIdGenerator.getInstance(config);

        generator.setStartTime(2020, 5, 1, 0, 0, 0);
        generator.addNamespaces(DEFAULT_NAMESPACE);

        assertThrows(RuntimeException.class, () -> assertNotNull(generator.getUUid(DEFAULT_NAMESPACE)));
    }
}
