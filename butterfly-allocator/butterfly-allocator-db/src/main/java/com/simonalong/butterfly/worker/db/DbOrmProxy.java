package com.simonalong.butterfly.worker.db;

import com.simonalong.butterfly.worker.db.entity.UuidGeneratorDO;
import com.simonalong.butterfly.worker.db.mapper.UuidGeneratorMapper;
import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.Pair;
import com.simonalong.neo.TableMap;
import com.simonalong.neo.util.LocalDateTimeUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import javax.swing.*;
import java.util.Date;
import java.util.Properties;

import static com.simonalong.butterfly.sequence.UuidConstant.KEEP_NODE_EXIST_TIME;
import static com.simonalong.butterfly.worker.db.DbConstant.UUID_TABLE;

/**
 * @author shizi
 * @since 2022-06-11 16:46:17
 */
public class DbOrmProxy {

    private static final DbOrmProxy INSTANCE = new DbOrmProxy();
    private String ormType = "neo";
    private Neo neo = null;
    private SqlSessionFactory sqlSessionFactory = null;

    private DbOrmProxy(){}

    public static DbOrmProxy getInstance() {
        return INSTANCE;
    }

    public void load(String url, String user, String password) {
        String ormType = System.getProperty("ORM_TYPE");
        if ("mybatis".equals(ormType)) {
            Configuration configuration = new Configuration(new Environment("development", new JdbcTransactionFactory(), getDatasource(url, user, password)));
            configuration.addMapper(UuidGeneratorMapper.class);

            this.ormType = "mybatis";
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        } else {
            this.ormType = "neo";
            neo = Neo.connect(url, user, password);
        }
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

    public void delete(Long id) {
        if ("neo".equals(ormType)) {
            neo.delete(UUID_TABLE, id);
        } else {
            SqlSession sqlSession = null;
            try {
                sqlSession = sqlSessionFactory.openSession();
                UuidGeneratorMapper mapper = sqlSession.getMapper(UuidGeneratorMapper.class);
                mapper.delete(id);
                sqlSession.commit();
            } catch (Throwable e) {
                if (sqlSession != null) {
                    sqlSession.rollback();
                }
            } finally {
                if (sqlSession != null) {
                    sqlSession.close();
                }
            }
        }
    }

    public UuidGeneratorDO update(String tableName, UuidGeneratorDO uuidGeneratorDO) {
        if ("neo".equals(ormType)) {
            return neo.update(tableName, uuidGeneratorDO);
        } else {
            SqlSession sqlSession = null;
            try {
                sqlSession = sqlSessionFactory.openSession();
                UuidGeneratorMapper mapper = sqlSession.getMapper(UuidGeneratorMapper.class);
                mapper.update(tableName, uuidGeneratorDO);
                sqlSession.commit();
                return null;
            } catch (Throwable e) {
                if (sqlSession != null) {
                    sqlSession.rollback();
                }
                return uuidGeneratorDO;
            } finally {
                if (sqlSession != null) {
                    sqlSession.close();
                }
            }
        }
    }

    public Long selectMinId(String namespace) {
        if ("neo".equals(ormType)) {
            return neo.exeValue(Long.class, "select min(id) from %s where namespace =? and last_expire_time < ?, namespace, date)", UUID_TABLE, namespace, new Date());
        } else {
            SqlSession sqlSession = null;
            try {
                sqlSession = sqlSessionFactory.openSession(true);
                UuidGeneratorMapper mapper = sqlSession.getMapper(UuidGeneratorMapper.class);
                return mapper.selectMinId(namespace, new Date());
            }catch (Throwable e) {
                if (sqlSession != null) {
                    sqlSession.rollback();
                }
                return null;
            } finally {
                if (sqlSession != null) {
                    sqlSession.close();
                }
            }
        }
    }

    public Pair<Boolean, UuidGeneratorDO> doApplyWorkerFromExistExpire(Long minId, String namespace, String uidKey, String processId, String ip) {
        if ("neo".equals(ormType)) {
            return neo.tx(() -> {
                TableMap result = neo.exeOne("select id, work_id, last_expire_time from %s where id = ? for update", UUID_TABLE, minId);
                if (null == result) {
                    return new Pair<>(false, null);
                }
                NeoMap selectOne = result.getNeoMap(UUID_TABLE);
                if (null != selectOne && selectOne.get(Date.class, "last_expire_time").compareTo(new Date()) < 0) {
                    UuidGeneratorDO uuidGeneratorDO = neo.update(UUID_TABLE, generateUuidGeneratorDo(selectOne.getLong("id"), selectOne.getInteger("work_id"), namespace, uidKey, processId, ip));
                    return new Pair<>(true, uuidGeneratorDO);
                }
                return new Pair<>(false, null);
            });
        } else {
            SqlSession sqlSession = null;
            try {
                sqlSession = sqlSessionFactory.openSession();

                // todo 修改
                TableMap result = neo.exeOne("select id, work_id, last_expire_time from %s where id = ? for update", UUID_TABLE, minId);
                if (null == result) {
                    return new Pair<>(false, null);
                }
                NeoMap selectOne = result.getNeoMap(UUID_TABLE);
                if (null != selectOne && selectOne.get(Date.class, "last_expire_time").compareTo(new Date()) < 0) {
                    UuidGeneratorDO uuidGeneratorDO = neo.update(UUID_TABLE, generateUuidGeneratorDo(selectOne.getLong("id"), selectOne.getInteger("work_id"), namespace, uidKey, processId, ip));
                    return new Pair<>(true, uuidGeneratorDO);
                }

                sqlSession.commit();
                return new Pair<>(true, null);
            }catch (Throwable e) {
                if (sqlSession != null) {
                    sqlSession.rollback();
                }
                return new Pair<>(false, null);
            } finally {
                if (sqlSession != null) {
                    sqlSession.close();
                }
            }
        }
    }

    private UuidGeneratorDO generateUuidGeneratorDo(Long id, Integer workerId, String namespace, String uidKey, String processId, String ip) {
        UuidGeneratorDO uuidGeneratorDO = new UuidGeneratorDO();
        uuidGeneratorDO.setId(id);
        uuidGeneratorDO.setWorkId(workerId);
        uuidGeneratorDO.setNamespace(namespace);
        uuidGeneratorDO.setLastExpireTime(LocalDateTimeUtil.longToTimestamp(System.currentTimeMillis() + KEEP_NODE_EXIST_TIME));
        uuidGeneratorDO.setUid(uidKey);
        uuidGeneratorDO.setProcessId(processId);
        uuidGeneratorDO.setIp(ip);
        return uuidGeneratorDO;
    }


}
