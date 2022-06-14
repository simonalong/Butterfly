package com.simonalong.butterfly.worker.db;

import com.simonalong.butterfly.sequence.ButterflyIdGenerator;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class DbWorkerIdHandlerTest {

    private static ButterflyIdGenerator generator;
    private static final String DEFAULT_NAMESPACE = "ID";

    @BeforeClass
    public static void beforeClass() {
        DbButterflyConfig config = new DbButterflyConfig();
        config.setUrl("jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false&&allowPublicKeyRetrieval=true");
        config.setUserName("neo_test");
        config.setPassword("neo@Test123");

        generator = ButterflyIdGenerator.getInstance(config);

        generator.setStartTime(2020, 5, 1, 0, 0, 0);
        generator.addNamespaces(DEFAULT_NAMESPACE);
    }

    @Test
    public void should_get_uuid_successfully_given_empty_database() {
        Long uuid = generator.getUUid(DEFAULT_NAMESPACE);
        assertNotNull(uuid);
    }

}
