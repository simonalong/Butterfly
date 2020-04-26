package com.simonalong.butterfly.worker.zookeeper.node;

import com.alibaba.fastjson.JSON;
import com.simonalong.butterfly.sequence.exception.ButterflyException;
import com.simonalong.butterfly.worker.zk.ZookeeperClient;
import com.simonalong.butterfly.worker.zk.entity.ConfigNodeEntity;
import lombok.extern.slf4j.Slf4j;

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
    public int getRsv() {
        return configNodeEntity.getRsv();
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
            log.error("更新机器个数失败");
            throw new ButterflyException("更新机器个数失败");
        }
    }

    private void init() {
        configNodeEntity = zookeeperClient.readDataJson(ZkNodeHelper.getConfigPath(namespace), ConfigNodeEntity.class);
    }
}
