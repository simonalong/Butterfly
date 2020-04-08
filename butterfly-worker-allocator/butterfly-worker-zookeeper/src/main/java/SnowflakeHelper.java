import com.ggj.platform.cornerstone.snowflake.exception.SnowflakeException;
import com.ggj.platform.cornerstone.snowflake.splicer.DefaultUUidSplicer;
import com.ggj.platform.cornerstone.snowflake.splicer.UUidSplicer;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.ggj.platform.cornerstone.snowflake.SnowflakeConstant.ROOT_PATH;

/**
 * 雪花算法序列管理器
 *
 * @author shizi
 * @since 2020/2/3 8:50 下午
 */
public class SnowflakeHelper
{
    @Setter
    private ZookeeperClient zkClient = ZookeeperClient.getInstance();
    private NamespaceNodeManager namespaceNodeManager = NamespaceNodeManager.getInstance();
    /**
     * key为对应业务命名空间，value为uuid的序列构造器
     */
    private Map<String, UUidSplicer> uUidBuilderMap = new HashMap<>();

    public SnowflakeHelper(String zkAddress)
    {
        zkClient.connect(zkAddress);
        namespaceNodeManager.setZkClient(zkClient);
    }

    public SnowflakeHelper(ZookeeperClient zkClient)
    {
        this.zkClient = zkClient;
        namespaceNodeManager.setZkClient(zkClient);
    }

    public void addNamespaces(String... namespaces)
    {
        Arrays.stream(namespaces).peek(this::checkNamespace).forEach(n->addUUidSplicer(n, new DefaultUUidSplicer(n, zkClient)));
    }

    public void addNamespacesWithoutCheck(String... namespaces)
    {
        Arrays.stream(namespaces).forEach(n->addUUidSplicer(n, new DefaultUUidSplicer(n, zkClient)));
    }

    /**
     * 添加对应业务命名空间的uuid构造器
     *
     * @param namespace          业务命名空间
     * @param defaultUUidSplicer uuid构造器
     */
    public void addUUidSplicer(String namespace, UUidSplicer defaultUUidSplicer)
    {
        uUidBuilderMap.putIfAbsent(namespace, defaultUUidSplicer);
    }

    /**
     * 获取对应命名空间的全局id
     *
     * @param namespace 业务的命名空间
     * @return 全局id生成器
     */
    public long getUUid(String namespace)
    {
        return getUUidSplicer(namespace).splice();
    }

    public UUidSplicer getUUidSplicer(String namespace)
    {
        if (!uUidBuilderMap.containsKey(namespace))
        {
            throw new SnowflakeException("命名空间" + namespace + "不存在，请确认是否注册或者查看mode是否匹配");
        }
        return uUidBuilderMap.get(namespace);
    }

    /**
     * 核查配置的业务空间是否存在
     *
     * @param namespace 业务的命名空间
     */
    private void checkNamespace(String namespace)
    {
        if (!zkClient.nodeExist(ROOT_PATH + "/" + namespace))
        {
            throw new SnowflakeException("业务空间" + namespace + "不存在，请确认当前是否是local模式且在控制台注册");
        }
    }
}
