/**
 * *****************************************************
 * Copyright (C) 2019 wwmust.com. All Rights Reserved
 * This file is part of wwmust project.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 * ****************************************************
 **/
package com.wwmust.sensitiveword;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.env.Environment;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * ${DESCRIPTION}
 *
 * @author wangwei<wwfdqc@126.com>
 * @date 11/30/2019 11:28
 */
@SpringBootApplication
@MapperScan("com.wwmust.sensitiveword.mapper")
@Slf4j
@EnableCaching
public class SensitivewordAppliction {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SensitivewordAppliction.class);

      Environment env = app.run(args).getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "服务端 '{}' 启动成功!" +
                        "\n\t环境: \t{}" +
                        "\n\t访问地址: \thttp://127.0.0.1:{}" +
                        "\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getActiveProfiles(),
                env.getProperty("server.port")
        );
    }
}
