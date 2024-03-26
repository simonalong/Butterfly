package com.simonalong.buffterfly.sample.mybatis;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author shizi
 * @since 2022-06-11 17:07:49
 */
public class MybatisDemo {

    @Test
    public void testSelectOne() {
        String URL = "jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false&&allowPublicKeyRetrieval=true";
        String USER = "neo_test";
        String PASSWORD = "neo@Test123";

        DataSource dataSource = getDatasource(URL, USER, PASSWORD);
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(DemoMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        SqlSession sqlSession = sqlSessionFactory.openSession();

        DemoMapper demoMapper = sqlSession.getMapper(DemoMapper.class);
        NeoTable table = demoMapper.one(1);
        System.out.println(table);
    }

    private DataSource getDatasource(String url, String username, String password) {
        Properties baseProper = new Properties();
        if (null != url) {
            baseProper.setProperty("jdbcUrl", url);
        }

        if (null != username) {
            baseProper.setProperty("dataSource.user", username);
        }

        if (null != password) {
            baseProper.setProperty("dataSource.password", password);
        }
        return new HikariDataSource(new HikariConfig(baseProper));
    }
}
