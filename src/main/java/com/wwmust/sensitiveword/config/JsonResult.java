/**
 * *****************************************************
 * Copyright (C) 2019 wwmust.com. All Rights Reserved
 * This file is part of wwmust EA project.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 * ****************************************************
 **/
package com.wwmust.sensitiveword.config;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.util.TypeUtils;

import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ${DESCRIPTION}
 *
 * @author 37079<37079@wwmust.com>
 * @date 11/11/2019 19:56
 */
public class JsonResult<T> {
    public static final String ERROR = "error";
    public static final String OK = "ok";
    public static final String NEED_LOGIN = "needLogin";
    public static final String PERMISSION_DENY = "permissionDeny";

    public static final String SESSION_TIMEOUT = "sessionTimeout";
    public static final String DATABAS_EEXCEPTION = "databaseException";

    public static final String JSON_TYPE = "json";
    public static final String TEXT_TYPE = "text";
    public static final String FORWARD_TYPE = "forward";

    private String status;
    private String message;
    private String type;
    private Integer total;
    private Boolean onlyData;
    private String dateFmt;
    private String debugInfo;

    private T data;
    private Map<String, Object> dic;

    public JsonResult<T> withMessage(String message) {
        this.setMessage(message);
        return this;
    }

    public JsonResult<T> withDic(Map<String, Object> dic) {
        this.setDic(dic);
        return this;
    }

    // 是否失败
    public boolean isFailed() {
        return ERROR.equals(this.getStatus());
    }
    // 是否成功
    public boolean isSuccess() {
        return OK.equals(this.getStatus());
    }
    // 是否需要登录
    public boolean isNeedLogin() {
        return NEED_LOGIN.equals(this.getStatus());
    }

    // 是否无权限
    public boolean isPermissionDeny() {
        return PERMISSION_DENY.equals(this.getStatus());
    }

    public Map<String, Object> getDic() {
        return dic;
    }

    public void setDic(Map<String, Object> dic) {
        this.dic = dic;
    }


    public String getDebugInfo() {
        return debugInfo;
    }

    public void setDebugInfo(String debugInfo) {
        this.debugInfo = debugInfo;
    }

    public String getDateFmt() {
        return dateFmt;
    }

    public void setDateFmt(String dateFmt) {
        this.dateFmt = dateFmt;
    }

 /*   public String toJSONString() {
        Validate.notBlank(this.getStatus(), "必须提供StatusCode");
        JSONStringBuilder builder = null;
        if (onlyData != null && this.isOnlyData()) {
            builder = new JSONStringBuilder(data);
        } else {
            builder = new JSONStringBuilder(this);
        }
        String[] excludes = {"dateFmt"};
        if (StringUtils.isEmpty(this.message)) {
            excludes = ArrayUtils.add(excludes, "message");
        }
        return builder.dateFmt(dateFmt).exclude(excludes).toString();
    }*/

    /**
     * @param response 响应对象
     * @param body 数据
     * @param <S> 数据类型
     * @return 带缓存的rest返回数据对象
     */
    public static <S> ResponseEntity<S> buildResponse(HttpServletResponse response, S body) {
        CacheControl cacheControl = CacheControl.maxAge(1L, TimeUnit.DAYS);
        response.setDateHeader("Expires", System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1L));
        response.setHeader("Cache-Control", cacheControl.getHeaderValue());
        response.setHeader("Pragma", cacheControl.getHeaderValue());
        return ResponseEntity.ok().cacheControl(cacheControl).body(body);
    }

    /**
     * 构建带缓存的rest返回数据对象
     *
     * @param response 响应对象
     * @param data 数据（未被{@link JsonResult}封装）
     * @param <T> 数据类型
     * @return 带缓存的rest返回数据对象（已被{@link JsonResult}封装）
     */
    public static <T> ResponseEntity<JsonResult<T>> okJsonResultWithDataResponse(
            HttpServletResponse response, T data) {
        return buildResponse(response, JsonResult.okJsonResultWithData(data));
    }

    public static JsonResult checkRespStrJsonResult(String result) {
        return StringUtils.isBlank(result)
                ? JsonResult.okJsonResult()
                : JsonResult.failJsonResult(result);
    }

    public static <T> JsonResult<T> okJsonResultWithData(T data) {
        JsonResult<T> jsonResult = new JsonResult<>();
        jsonResult.setData(data);
        return jsonResult;
    }

    public static JsonResult<?> okJsonResult() {
        return okJsonResultWithMsg("");
    }

    public static JsonResult<?> okJsonResultWithMsg(String message) {
        JsonResult<?> jsonResult = new JsonResult<>();
        jsonResult.setMessage(message);
        return jsonResult;
    }

    public static JsonResult<String> okJsonResultWithContent(String dataType, String data) {
        JsonResult<String> jsonResult = new JsonResult<>();
        jsonResult.setType(dataType);
        jsonResult.setData(data);
        return jsonResult;
    }

    public static JsonResult<String> okJsonResultWithForward(String forwardUrl) {
        JsonResult<String> jsonResult = new JsonResult<String>();
        jsonResult.setForwardUrl(forwardUrl);
        return jsonResult;
    }

    public static JsonResult<?> failJsonResult(String message) {
        JsonResult<?> jsonResult = new JsonResult<>();
        jsonResult.setStatus(ERROR);
        jsonResult.setMessage(message);
        return jsonResult;
    }
    public static JsonResult<?> failJsonResult(Throwable wrapped) {
        JsonResult<?> jsonResult = failJsonResult(getMsg(wrapped));
        jsonResult.setDebugInfo(getStackTraceAsString(wrapped));
        return jsonResult;
    }
    public static JsonResult<?> failJsonResult(String status, String message) {
        JsonResult<?> jsonResult = new JsonResult<>();
        jsonResult.setStatus(status);
        jsonResult.setMessage(message);
        return jsonResult;
    }

    public static <T> JsonResult<T> failJsonResultWithData(T data) {
        JsonResult<T> jsonResult = new JsonResult<T>();
        jsonResult.setStatus(ERROR);
        jsonResult.setData(data);
        return jsonResult;
    }

    public static JsonResult<?> needLoginJsonResult() {
        JsonResult<?> jsonResult = new JsonResult<>();
        jsonResult.setStatus(NEED_LOGIN);
        jsonResult.setMessage("need login!");
        jsonResult.setForwardUrl("/logout");
        return jsonResult;
    }

    public static JsonResult<?> permissionDenyJsonResult() {
        return permissionDenyJsonResult(PERMISSION_DENY);
    }

    public static JsonResult<?> permissionDenyJsonResult(String message) {
        JsonResult<?> jsonResult = new JsonResult<>();
        jsonResult.setStatus(PERMISSION_DENY);
        jsonResult.setMessage(message);
        return jsonResult;
    }

    /**
     *  ControllerAdvice  专用异常返回方法
     * @param ex
     * @return
     */
    public static JsonResult<?> permissionDenyJsonResult(NoPermissionException ex) {
        JsonResult<?> jsonResult = new JsonResult<>();
        jsonResult.setStatus(PERMISSION_DENY);
        jsonResult.setMessage(StringUtils.isEmpty(ex.getMessage()) ? PERMISSION_DENY : ex.getMessage());
        return jsonResult;
    }

    public JsonResult() {
        this.setStatus(OK);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
        if (data instanceof List) {
            this.total = ((List) data).size();
        }
    }

    public void setData(T data, boolean b) {
        this.onlyData(b).setData(data);
    }


    public JsonResult<T> data(String key, Object value) {
        if (this.data == null) {
            this.data = (T) Maps.<String, Object>newLinkedHashMap();
        }
        MetaObject metaObject = SystemMetaObject.forObject(data);
        metaObject.setValue(key, value);
        return this;
    }

    public Boolean isOnlyData() {
        return onlyData;
    }

    public static String getMsg(Throwable wrapped) {
        Throwable unthrowable = ExceptionUtil.unwrapThrowable(wrapped);
        if (isTheThrowable(unthrowable, InvocationTargetException.class)) {
            InvocationTargetException invocationTargetException = castException(unthrowable, InvocationTargetException.class);
            if(invocationTargetException != null){
                unthrowable = invocationTargetException.getTargetException();
            }else{
                return "null exception";
            }
        }
        StringBuilder errorMsg = new StringBuilder(StringUtils.defaultString(unthrowable.getMessage()));
        Throwable throwable = unthrowable.getCause();
        while (StringUtils.isEmpty(unthrowable.getMessage()) && throwable != null && !TypeUtils.isAssignable(throwable.getClass(), unthrowable.getClass())) {
            errorMsg.append(StringUtils.defaultString(throwable.getMessage()));
            throwable = throwable.getCause();
        }
        return StringUtils.defaultIfEmpty(errorMsg.toString(), "null exception");
    }
    public static <T extends Throwable> boolean isTheThrowable(Throwable wrapped, Class<T> excls) {
        return castException(wrapped, excls) != null;
    }
    public static <T extends Throwable> T castException(Throwable wrapped, Class<T> excls) {
        Throwable unthrowable = ExceptionUtil.unwrapThrowable(wrapped);
        if (TypeUtils.isAssignable(unthrowable.getClass(), excls)) {
            return (T) unthrowable;
        }
        while ((unthrowable = unthrowable.getCause()) != null) {
            if (TypeUtils.isAssignable(unthrowable.getClass(), excls)) {
                return (T) unthrowable;
            }
        }
        return null;
    }
    public JsonResult<T> onlyData(Boolean onlyData) {
        this.onlyData = onlyData;
        return this;
    }

    @SuppressWarnings("unchecked")
    public void setForwardUrl(String forwardUrl) {
        this.setType(FORWARD_TYPE);
        this.setData(((T) forwardUrl));
    }

    /**
     * 将ErrorStack转化为String.
     */
    public static String getStackTraceAsString(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String statusCode) {
        this.status = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
