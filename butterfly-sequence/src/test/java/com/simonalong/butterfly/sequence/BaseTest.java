package com.simonalong.butterfly.sequence;

import java.util.Optional;

/**
 * @author shizi
 * @since 2020/4/10 12:18 AM
 */
public class BaseTest {

    public void show(Object object) {
        if(null == object){
            show("obj is null ");
            return;
        }
        Optional.of(object).ifPresent(objects1 -> System.out.println(objects1.toString()));
    }
}
