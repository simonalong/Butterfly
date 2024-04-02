package com.simonalong.butterfly.worker.zookeeper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shizi
 * @since 2024-03-27 20:03:50
 */
@Slf4j
public class ZookeeperClientTest {
//
//    @Test
//    public void testDistributeTryLock_1() {
//        try {
//            ZookeeperClient zkClient = ZookeeperClient.getInstance();
//            zkClient.connect("127.0.0.1:2181");
//            Boolean result = zkClient.distributeLockAndRun("/lock_tem", () -> {
//                log.info("handle one biz");
//                return true;
//            });
//
//            if (result) {
//                log.info("one_finish");
//            } else {
//                log.info("one_error");
//            }
//
//        } catch (Exception e) {
//            log.info("error");
//        }
//    }
//
//    @Test
//    public void testDistributeTryLock_2() {
//        try {
//            ZookeeperClient zkClient = ZookeeperClient.getInstance();
//            zkClient.connect("127.0.0.1:2181");
//            log.info("ok");
//            Boolean result = zkClient.distributeLockAndRun("/lock_tem", () -> {
//                log.info("handle");
//                return true;
//            });
//
//            if (result) {
//                log.info("two_finish");
//            } else {
//                log.info("two_error");
//            }
//
//        } catch (Exception e) {
//            log.info("error");
//        }
//    }
}
