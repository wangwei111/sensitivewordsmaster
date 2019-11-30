/**
 * *****************************************************
 * Copyright (C) 2019 wwmust.com. All Rights Reserved
 * This file is part of wwmust project.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 * ****************************************************
 **/
package com.wwmust.sensitiveword.config;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.cache.GuavaCache;
import com.model.filterWd;
import com.wwmust.sensitiveword.mapper.SensitiveWordMapper;
import org.apache.ibatis.mapping.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ${DESCRIPTION}
 *
 * @author wangwei<wwfdqc@126.com>
 * @date 11/30/2019 11:39
 */
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@CacheConfig
public class SetSensitiveWordScheduleTask {

    @Autowired
    private SensitiveWordMapper sensitiveWordMapper;

        //3.添加定时任务
        @Scheduled(cron = "0/5 * * * * ?")
        //或直接指定时间间隔，例如：5秒\
        private void setSensitiveWords() {

       List<filterWd> filterWds =  sensitiveWordMapper.getSensitiveWords();
       if(CollectionUtils.isEmpty(filterWds)){
       }
       System.out.println(JSON.toJSONString(filterWds));
            System.err.println("执行静态定时任务时间: " + System.currentTimeMillis());
        }
}
