/**
 * *****************************************************
 * Copyright (C) 2019 wwmust.com. All Rights Reserved
 * This file is part of wwmust project.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 * ****************************************************
 **//*

package com.wwmust.sensitiveword.utils;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;



*/
/**
 * ${DESCRIPTION}
 *
 * @author wangwei<wwfdqc@126.com>
 * @date 11/30/2019 13:20
 *//*

public class EhCacheUtils {
    */
/**
     *   * 设置缓存对象
     *   * @param cacheManager
     *   * @param key
     *   * @param object
     *   
     *//*

    public static void setCache(CacheManager cacheManager, String key, Object object) {
        Cache cache = cacheManager.getCache("objectCache");
        Element element = new Element(key, object);
        cache.put(element);
    }

    */
/**
     *   * 从缓存中取出对象
     *   * @param cacheManager
     *   * @param key
     *   * @return
     *   
     *//*

    public static Object getCache(CacheManager cacheManager, String key) {
        Object object = null;
        Cache cache = cacheManager.getCache("objectCache");
        if (cache.get(key) != null && !cache.get(key).equals("")) {
            object = cache.get(key).getObjectValue();
        }
        return object;
    }
}
*/
