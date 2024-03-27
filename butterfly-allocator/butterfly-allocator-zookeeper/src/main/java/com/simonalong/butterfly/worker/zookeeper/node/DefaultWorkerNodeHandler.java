package com.simonalong.butterfly.worker.zookeeper.node;

import com.alibaba.fastjson.JSON;
import com.simonalong.butterfly.worker.zookeeper.ZookeeperClient;
import com.simonalong.butterfly.worker.zookeeper.entity.WorkerNodeEntity;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.simonalong.butterfly.sequence.UuidConstant.HEART_TIME;
import static com.simonalong.butterfly.sequence.UuidConstant.KEEP_NODE_EXIST_TIME;
import static com.simonalong.butterfly.worker.zookeeper.ZkConstant.SESSION_NODE;
import static com.simonalong.butterfly.worker.zookeeper.ZkConstant.ZK_LOG_PRE;

/**
 * worker节点处理器
 *
 * @author shizi
 * @since 2020/4/25 11:01 AM
 */
@Slf4j
public class DefaultWorkerNodeHandler implements WorkerNodeHandler {

    /**
     * 当前启动的唯一健
     */
    private String uidKey;
    /**
     * worker_x 节点信息
     */
    private WorkerNodeEntity workerNodeEntity;
    private ScheduledThreadPoolExecutor scheduler;
    private final ZookeeperClient zookeeperClient;
    private final WorkerIdAllocator workerIdAllocator;

    public DefaultWorkerNodeHandler(String namespace, ZookeeperClient zookeeperClient, ConfigNodeHandler configNodeHandler) {
        this.zookeeperClient = zookeeperClient;
        init();
        this.workerIdAllocator = new DefaultWorkerIdAllocator(namespace, zookeeperClient, this, configNodeHandler);
        this.workerNodeEntity = getWorkerNodeEntity();
    }

    private void init() {
        // 初始胡uidKey
        initKey();

        // 初始化心跳上报
        initHeartBeatReport();

        // 添加进程退出钩子
        addShutdownHook();
    }


    @Override
    public String getUidKey() {
        return uidKey;
    }

    @Override
    public Long getLastExpireTime() {
        return workerNodeEntity.getLastExpireTime();
    }

    @Override
    public String getIp() {
        return workerNodeEntity.getIp();
    }

    @Override
    public String getProcessId() {
        return workerNodeEntity.getProcessId();
    }

    @Override
    public Integer getWorkerId() {
        return workerIdAllocator.getWorkerId();
    }

    /**
     * 刷新节点信息
     */
    @Override
    public void refreshNodeInfo() {
        try {
            updateWorkerNodeInfo(getWorkerNodeEntity());
        } catch (Throwable e) {
            log.error("刷新节点信息异常：", e);
        }
    }

    /**
     * 刷新指定节点的信息
     */
    @Override
    public void refreshNodeInfo(String workerNodePath) {
        updateWorkerNodeInfo(workerNodePath, getWorkerNodeEntity());
    }

    /**
     * 获取本次进程启动的唯一编号
     */
    private void initKey() {
        uidKey = UUID.randomUUID().toString();
    }

    /**
     * 初始化数据的心跳上报
     */
    private void initHeartBeatReport() {
        scheduler = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            private AtomicInteger threadNum = new AtomicInteger(0);

            @Override
            @SuppressWarnings("all")
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "Thread-Butterfly-Heart" + threadNum.getAndIncrement());
                thread.setDaemon(true);
                return thread;
            }
        });

        // 每5秒上报一次数据
        scheduler.scheduleWithFixedDelay(this::refreshNodeInfo, 10, HEART_TIME, TimeUnit.SECONDS);
    }

    /**
     * 进程关闭时候清理业务数据
     */
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info(ZK_LOG_PRE + "process ready to quit, clear resources of zookeeper");
            updateWorkerNodeInfo(null);
            zookeeperClient.deleteNode(workerIdAllocator.getWorkerNodePath() + SESSION_NODE);
            if (null != scheduler) {
                scheduler.shutdown();
            }
        }));
    }

    private WorkerNodeEntity getWorkerNodeEntity() {
        return new WorkerNodeEntity().setIp(getIpStr()).setProcessId(getProcessIdStr()).setLastExpireTime(afterHour()).setUidKey(uidKey);
    }

    private void updateWorkerNodeInfo(WorkerNodeEntity workerNodeInfo) {
        if (null != workerIdAllocator) {
            updateWorkerNodeInfo(workerIdAllocator.getWorkerNodePath(), workerNodeInfo);
        }
    }

    /**
     * 更新节点worker_x 中的信息
     */
    private void updateWorkerNodeInfo(String workerNodePath, WorkerNodeEntity workerNodeInfo) {
        try {
            if (null != workerNodeInfo) {
                zookeeperClient.writeNodeData(workerNodePath, JSON.toJSONString(workerNodeInfo));
            } else {
                zookeeperClient.writeNodeData(workerNodePath, "");
            }
        } catch (Throwable e) {
            log.error(ZK_LOG_PRE + "node(worker_" + getWorkerId() + ") update fail", e);
        }

        this.workerNodeEntity = workerNodeInfo;
    }

    private String getIpStr() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "null";
        }
    }

    /**
     * 将时间向未来延长固定的小时
     */
    private long afterHour() {
        return System.currentTimeMillis() + KEEP_NODE_EXIST_TIME;
    }

    /**
     * 获取进程id字符串
     */
    private String getProcessIdStr() {
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }
}
