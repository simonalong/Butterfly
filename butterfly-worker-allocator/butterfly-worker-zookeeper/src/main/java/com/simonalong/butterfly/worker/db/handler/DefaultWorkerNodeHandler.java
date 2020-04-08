package com.simonalong.butterfly.worker.db.handler;

import com.alibaba.fastjson.JSON;
import com.ggj.platform.cornerstone.snowflake.ZookeeperClient;
import com.ggj.platform.cornerstone.snowflake.allocator.DefaultWorkerIdAllocator;
import com.ggj.platform.cornerstone.snowflake.allocator.WorkerIdAllocator;
import com.ggj.platform.cornerstone.snowflake.entity.WorkerNodeInfo;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ggj.platform.cornerstone.snowflake.SnowflakeConstant.*;

/**
 * @author shizi
 * @since 2020/2/7 11:06 上午
 */
@Slf4j
public class DefaultWorkerNodeHandler implements WorkerNodeHandler
{
    /**
     * 当前启动的唯一健
     */
    private String uidKey;
    /**
     * worker_x 节点信息
     */
    private WorkerNodeInfo workerNodeInfo;
    private ScheduledThreadPoolExecutor scheduler;
    private ZookeeperClient zookeeperClient;
    private WorkerIdAllocator workerIdAllocator;

    public DefaultWorkerNodeHandler(String namespace, ZookeeperClient zookeeperClient,
                                    ConfigNodeHandler configNodeHandler)
    {
        this.zookeeperClient = zookeeperClient;
        init();
        this.workerIdAllocator = new DefaultWorkerIdAllocator(namespace, zookeeperClient, this, configNodeHandler);
        this.workerNodeInfo = getWorkerNodeInfo();
    }

    private void init()
    {
        // 初始胡uidKey
        initKey();

        // 初始化心跳上报
        initHeartBeatReport();

        // 添加进程退出钩子
        addShutdownHook();
    }


    @Override
    public String getUidKey()
    {
        return uidKey;
    }

    @Override
    public Long getLastExpireTime()
    {
        return workerNodeInfo.getLastExpireTime();
    }

    @Override
    public String getIp()
    {
        return workerNodeInfo.getIp();
    }

    @Override
    public String getProcessId()
    {
        return workerNodeInfo.getProcessId();
    }

    @Override
    public Integer getWorkerId()
    {
        return workerIdAllocator.getWorkerId();
    }

    /**
     * 刷新节点信息
     */
    public void refreshNodeInfo()
    {
        updateWorkerNodeInfo(getWorkerNodeInfo());
    }

    /**
     * 刷新指定节点的信息
     */
    public void refreshNodeInfo(String workerNodePath)
    {
        updateWorkerNodeInfo(workerNodePath, getWorkerNodeInfo());
    }

    /**
     * 获取本次进程启动的唯一编号
     */
    private void initKey()
    {
        uidKey = UUID.randomUUID().toString();
    }

    /**
     * 初始化数据的心跳上报
     */
    private void initHeartBeatReport()
    {
        scheduler = new ScheduledThreadPoolExecutor(1, new ThreadFactory()
        {
            private AtomicInteger threadNum = new AtomicInteger(0);

            @Override
            @SuppressWarnings("all")
            public Thread newThread(Runnable r)
            {
                Thread thread = new Thread(r, "Thread-Snowflake-heart" + threadNum.getAndIncrement());
                thread.setDaemon(true);
                return thread;
            }
        });

        // 每5秒上报一次数据
        scheduler.scheduleWithFixedDelay(this::refreshNodeInfo, 10, HEART_INTERVAL_TIME, TimeUnit.SECONDS);
    }

    /**
     * 进程关闭时候清理业务数据
     */
    private void addShutdownHook()
    {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            log.info(LOG_PRE + "进程即将退出，清理本次启动申请的zk资源");
            updateWorkerNodeInfo(null);
            zookeeperClient.deleteNode(workerIdAllocator.getWorkerNodePath() + SESSION_NODE);
            if (null != scheduler)
            {
                scheduler.shutdown();
            }
        }));
    }

    private WorkerNodeInfo getWorkerNodeInfo()
    {
        return new WorkerNodeInfo().setIp(getIpStr()).setProcessId(getProcessIdStr()).setLastExpireTime(afterHour()).setUidKey(uidKey);
    }

    private void updateWorkerNodeInfo(WorkerNodeInfo workerNodeInfo)
    {
        updateWorkerNodeInfo(workerIdAllocator.getWorkerNodePath(), workerNodeInfo);
    }

    /**
     * 更新节点worker_x 中的信息
     */
    private void updateWorkerNodeInfo(String workerNodePath, WorkerNodeInfo workerNodeInfo)
    {
        try
        {
            if (null != workerNodeInfo)
            {
                zookeeperClient.writeNodeData(workerNodePath, JSON.toJSONString(workerNodeInfo));
            }
            else
            {
                zookeeperClient.writeNodeData(workerNodePath, "");
            }
        }
        catch (Throwable e)
        {
            log.error("节点worker_" + getWorkerId() + "更新失败", e);
        }

        this.workerNodeInfo = workerNodeInfo;
    }

    private String getIpStr()
    {
        try
        {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e)
        {
            return "null";
        }
    }

    /**
     * 将时间向未来延长固定的小时
     */
    private long afterHour()
    {
        return System.currentTimeMillis() + KEEP_EXPIRE_TIME;
    }

    /**
     * 获取进程id字符串
     */
    private String getProcessIdStr()
    {
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }
}
