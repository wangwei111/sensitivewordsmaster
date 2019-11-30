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
import com.model.filterWd;
import com.odianyun.util.sensi.SensitiveFilter;
import com.wwmust.sensitiveword.mapper.SensitiveWordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author wangwei<wwfdqc@126.com>
 * @date 11/30/2019 11:39
 */
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class SetSensitiveWordScheduleTask {

    @Autowired
    private SensitiveWordMapper sensitiveWordMapper;

    @Autowired
    private  ApplicationContext context;

        //3.添加定时任务
      //  @Scheduled(cron = "0/5 * * * * ?")
        //或直接指定时间间隔，例如：5秒
         public  SensitiveFilter setSensitiveWords() {
            SensitiveFilter sensitiveFilter = new SensitiveFilter();
            log.info("执行了...");
            sensitiveFilter.clear();
            List<filterWd> filterWds =  sensitiveWordMapper.getSensitiveWords();
            filterWds.forEach(wd->{
                sensitiveFilter.put(wd.getKeywords());
            });
            System.err.println("执行静态定时任务时间: " + System.currentTimeMillis());
            return sensitiveFilter;
        }


        @Bean
        public SensitiveFilter sensitiveFilter(){
            SensitiveFilter sensitiveFilter = new SensitiveFilter();
            log.info("执行了...");
            sensitiveFilter.clear();
            List<filterWd> filterWds =  sensitiveWordMapper.getSensitiveWords();
            filterWds.forEach(wd->{
                sensitiveFilter.put(wd.getKeywords());
            });
                return sensitiveFilter;
            }
}
