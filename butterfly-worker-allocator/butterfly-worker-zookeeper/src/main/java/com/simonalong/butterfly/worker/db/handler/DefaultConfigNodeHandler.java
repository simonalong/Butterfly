package com.simonalong.butterfly.worker.db.handler;

import com.alibaba.fastjson.JSON;
import com.ggj.platform.cornerstone.snowflake.ZookeeperClient;
import com.ggj.platform.cornerstone.snowflake.entity.ConfigNodeInfo;
import com.ggj.platform.cornerstone.snowflake.exception.SnowflakeException;
import lombok.extern.slf4j.Slf4j;

import static com.ggj.platform.cornerstone.snowflake.SnowflakeConstant.*;
import static com.ggj.platform.cornerstone.snowflake.SnowflakeConstant.getConfigPath;

/**
 * 对配置节点的处理
 *
 * @author shizi
 * @since 2020/2/6 9:57 下午
 */
@Slf4j
public class DefaultConfigNodeHandler implements ConfigNodeHandler
{
    private String namespace;
    private ConfigNodeInfo configNodeInfo;
    private ZookeeperClient zookeeperClient;

    public DefaultConfigNodeHandler(String namespace, ZookeeperClient zookeeperClient)
    {
        this.zookeeperClient = zookeeperClient;
        this.namespace = namespace;
        init();
    }

    @Override
    public int getRsv()
    {
        return configNodeInfo.getRsv();
    }

    @Override
    public int getCurrentMaxMachineNum()
    {
        return configNodeInfo.getCurrentMaxMachine();
    }

    @Override
    public void updateCurrentMaxMachineNum(int maxMachine)
    {
        configNodeInfo.setCurrentMaxMachine(maxMachine);
        try
        {
            zookeeperClient.writeNodeData(getConfigPath(namespace), JSON.toJSONString(configNodeInfo.setCurrentMaxMachine(maxMachine)));
        }
        catch (Throwable e)
        {
            log.error("更新机器个数失败");
            throw new SnowflakeException("更新机器个数失败");
        }
    }

    private void init()
    {
        configNodeInfo = zookeeperClient.readDataJson(getConfigPath(namespace), ConfigNodeInfo.class);
    }
}
