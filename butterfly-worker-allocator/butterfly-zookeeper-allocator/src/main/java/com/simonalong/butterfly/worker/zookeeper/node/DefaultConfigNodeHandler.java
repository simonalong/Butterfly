package com.simonalong.butterfly.worker.zookeeper.node;

import com.alibaba.fastjson.JSON;
import com.simonalong.butterfly.sequence.exception.ButterflyException;
import com.simonalong.butterfly.worker.zookeeper.ZookeeperClient;
import com.simonalong.butterfly.worker.zookeeper.entity.ConfigNodeEntity;
import lombok.extern.slf4j.Slf4j;

import static com.simonalong.butterfly.worker.zookeeper.ZkConstant.ZK_LOG_PRE;

/**
 * 对配置节点的处理
 *
 * @author shizi
 * @since 2020/4/25 11:17 AM
 */
@Slf4j
public class DefaultConfigNodeHandler implements ConfigNodeHandler {

    private String namespace;
    private ConfigNodeEntity configNodeEntity;
    private ZookeeperClient zookeeperClient;

    public DefaultConfigNodeHandler(String namespace, ZookeeperClient zookeeperClient) {
        this.zookeeperClient = zookeeperClient;
        this.namespace = namespace;
        init();
    }

    @Override
    public int getCurrentMaxMachineNum() {
        return configNodeEntity.getCurrentMaxMachine();
    }

    @Override
    public void updateCurrentMaxMachineNum(int maxMachine) {
        configNodeEntity.setCurrentMaxMachine(maxMachine);
        try {
            zookeeperClient.writeNodeData(ZkNodeHelper.getConfigPath(namespace), JSON.toJSONString(configNodeEntity.setCurrentMaxMachine(maxMachine)));
        } catch (Throwable e) {
            log.error(ZK_LOG_PRE + "update workerId fail");
            throw new ButterflyException("update workerId fail");
        }
    }

    private void init() {
        configNodeEntity = zookeeperClient.readDataJson(ZkNodeHelper.getConfigPath(namespace), ConfigNodeEntity.class);
    }
}
