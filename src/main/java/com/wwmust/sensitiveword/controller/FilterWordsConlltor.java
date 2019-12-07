/**
 * *****************************************************
 * Copyright (C) 2019 wwmust.com. All Rights Reserved
 * This file is part of wwmust project.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 * ****************************************************
 **/
package com.wwmust.sensitiveword.controller;

import com.model.filterWd;
import com.odianyun.util.sensi.SensitiveFilter;
import com.wwmust.sensitiveword.config.JsonResult;
import com.wwmust.sensitiveword.mapper.SensitiveWordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author wangwei<wwfdqc@126.com>
 * @date 11/30/2019 11:35
 */
@RestController
public class FilterWordsConlltor {


    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Autowired
    private SensitiveWordMapper sensitiveWordMapper;

    @PostMapping("filter/api/words")
    public JsonResult getFilter(@RequestBody  WordsParam wordsParam){
         Assert.isTrue(!StringUtils.isEmpty(wordsParam.getKey()),"key为空!");
        // 句子中有敏感词
        List<filterWd> filterWds =  sensitiveWordMapper.getSensitiveWords();
        filterWds.forEach(wd->{
            sensitiveFilter.put(wd.getKeywords());
        });
         return  JsonResult.okJsonResultWithData(sensitiveFilter.filter(wordsParam.getKey(), '*'));
    }


}
