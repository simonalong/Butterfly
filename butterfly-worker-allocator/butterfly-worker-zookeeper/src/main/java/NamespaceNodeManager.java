import com.ggj.platform.cornerstone.snowflake.exception.SnowflakeException;
import com.ggj.platform.cornerstone.snowflake.handler.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.ggj.platform.cornerstone.snowflake.SnowflakeConstant.*;

/**
 * 对应命名空间下所有节点信息进行统一管理
 *
 * @author shizi
 * @since 2020/2/6 10:50 上午
 */
@Slf4j
public class NamespaceNodeManager {

    private static volatile NamespaceNodeManager INSTANCE = null;
    /**
     * config节点操作map
     */
    private Map<String, ConfigNodeHandler> configNodeHandlerMap = new HashMap<>(12);
    /**
     * worker节点操作map
     */
    private Map<String, WorkerNodeHandler> workerNodeHandlerMap = new HashMap<>(12);
    @Setter
    private ZookeeperClient zkClient;

    private NamespaceNodeManager() {
    }

    public static NamespaceNodeManager getInstance() {
        if (null == INSTANCE) {
            synchronized (NamespaceNodeManager.class) {
                if (null == INSTANCE) {
                    INSTANCE = new NamespaceNodeManager();
                }
            }
        }
        return INSTANCE;
    }

    public void add(String namespace) {
        // 包含则不再添加
        if (configNodeHandlerMap.containsKey(namespace)) {
            return;
        }
        if (null == zkClient) {
            throw new SnowflakeException("zkClient不可为空");
        }

        ConfigNodeHandler configNodeHandler = new DefaultConfigNodeHandler(namespace, zkClient);
        configNodeHandlerMap.putIfAbsent(namespace, configNodeHandler);
        workerNodeHandlerMap.putIfAbsent(namespace, new DefaultWorkerNodeHandler(namespace, zkClient, configNodeHandler));
    }

    public int getWorkerId(String namespace) {
        return getWorkerNodeHandler(namespace).getWorkerId();
    }

    /**
     * 获取保留字段的值，默认为0
     */
    public int getRsv(String namespace) {
        return getConfigNodeHandler(namespace).getRsv();
    }

    /**
     * 获取过期时间
     */
    public Long getExpireTime(String namespace) {
        return getWorkerNodeHandler(namespace).getLastExpireTime();
    }

    public ConfigNodeHandler getConfigNodeHandler(String namespace) {
        check(namespace);
        return configNodeHandlerMap.get(namespace);
    }

    public WorkerNodeHandler getWorkerNodeHandler(String namespace) {
        check(namespace);
        return workerNodeHandlerMap.get(namespace);
    }

    /**
     * 核查命名空间
     */
    private void check(String namespace) {
        if (!configNodeHandlerMap.containsKey(namespace)) {
            throw new SnowflakeException("命名空间" + namespace + "不存在");
        }
    }
}
