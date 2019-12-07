/**
 * *****************************************************
 * Copyright (C) 2019 wwmust.com. All Rights Reserved
 * This file is part of wwmust project.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 * ****************************************************
 **/
package com.wwmust.sensitiveword.mapper;

import com.model.filterWd;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author wangwei<wwfdqc@126.com>
 * @date 11/30/2019 11:46
 */
@Repository
@CacheConfig
public  interface SensitiveWordMapper {


    @Select("select id ,keywords,type from sensitive_word_library")
    @Cacheable(value = "sensitive_word_cache")
    List<filterWd> getSensitiveWords();
}
