package com.simonalong.butterfly.sequence;

import com.simonalong.butterfly.sequence.allocator.BitAllocator;
import com.simonalong.butterfly.sequence.exception.ButterflyException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.simonalong.butterfly.sequence.UuidConstant.*;

/**
 * @author shizi
 * @since 2020/4/8 11:55 PM
 */
public final class ButterflyIdGenerator {

    /**
     * 2020-02-22 00:00:00.000 对应的时间
     */
    static Long startTime = 1582300800000L;
    private static final Integer FIRST_YEAR = 2020;
    private ButterflyConfig butterflyConfig;
    private static volatile ButterflyIdGenerator instance;
    /**
     * key为对应业务命名空间，value为uuid的序列构造器
     */
    private Map<String, UuidSplicer> uUidBuilderMap = new HashMap<>();

    /**
     * 全局id生成器的构造函数
     *
     * @return 全局id生成器对象
     */
    public static ButterflyIdGenerator getInstance(ButterflyConfig butterflyConfig) {
        if (null == instance) {
            synchronized (ButterflyIdGenerator.class) {
                if (null == instance) {
                    instance = new ButterflyIdGenerator();
                    instance.butterflyConfig = butterflyConfig;
                }
            }
        }
        return instance;
    }

    /**
     * 声明命名空间
     * <p>
     *  如果命名空间不存在，则新建，如果存在，则采用已经存在的命名空间
     *
     * @param namespaces 命名空间
     */
    public void addNamespaces(String... namespaces) {
        Arrays.stream(namespaces).forEach(n -> uUidBuilderMap.putIfAbsent(n, new UuidSplicer(n, butterflyConfig)));
    }

    /**
     * 获取对应命名空间的全局id
     *
     * @param namespace 业务的命名空间
     * @return 全局id生成器
     */
    public long getUUid(String namespace) {
        return getUUidSplicer(namespace).splice();
    }

    public BitAllocator getBitAllocator(String namespace) {
        return getUUidSplicer(namespace).getBitAllocator();
    }

    /**
     * 设置启动时间
     * <p>
     * 目前当前的启动时间是按照2020年2月22号算起，如果不设置，则最久可以用到2083年左右
     */
    public void setStartTime(Date startTimeDate) {
        if (null != startTimeDate) {
            startTime = startTimeDate.getTime();
        }
    }

    public void setStartTime(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        if (year < FIRST_YEAR) {
            throw new ButterflyException("请设置未来时间");
        }
        LocalDateTime localDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
        startTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()).getTime();
    }

    private UuidSplicer getUUidSplicer(String namespace) {
        if (!uUidBuilderMap.containsKey(namespace)) {
            throw new ButterflyException("命名空间" + namespace + "不存在，请先添加命名空间");
        }
        return uUidBuilderMap.get(namespace);
    }

    /**
     * 解析uuid
     *
     * <ul>
     *     <li>uuid：对应的是当前的全局id</li>
     *     <li>symbol：对应的符号位</li>
     *     <li>time：对应的时间值</li>
     *     <li>abstractTime：相对起始时间的具体时间</li>
     *     <li>sequence：序列值</li>
     *     <li>workerId：分配的机器id</li>
     * </ul>
     * @param uid
     * @return
     */
    @SuppressWarnings("all")
    public static Map<String, Object> parseUid(Long uid) {
        long symbolMark = 1 << (SYMBOL_LEFT_SHIFT);
        long timeMark = (~(-1L << TIME_BITS)) << TIME_LEFT_SHIFT;
        long seqMark = (~(-1L << SEQ_BITS)) << SEQ_LEFT_SHIFT;
        long workerMark = ~(-1L << WORKER_BITS);

        Map<String, Object> resultMap = new HashMap<>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        resultMap.put("uuid", uid);
        resultMap.put("symbol", ((uid & symbolMark) >>> SYMBOL_LEFT_SHIFT));
        resultMap.put("time", ((uid & timeMark) >> TIME_LEFT_SHIFT));
        resultMap.put("abstractTime", dateFormat.format(new Date(((uid & timeMark) >> TIME_LEFT_SHIFT) + startTime)));
        resultMap.put("sequence", ((uid & seqMark) >> SEQ_LEFT_SHIFT));
        resultMap.put("workerId", (uid & workerMark));
        return resultMap;
    }
}
