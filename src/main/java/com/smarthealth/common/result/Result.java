package com.smarthealth.common.result;
import com.smarthealth.common.constant.CommonConstants;
import com.smarthealth.common.constant.HttpCodeConstant;
import com.smarthealth.domain.VO.UserInfoVO;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.util.*;


//@Slf4j
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class Result<T> {
//
//    private int code; // 状态码
//    private String message; // 返回消息
//    private T data; // 返回数据
//
//
//    public static <T> Result<T> success() {
//        Result<T> result = new Result<T>();
//        result.code=200;
//        result.message="成功";
//        return result;
//    }
//
//
//    public static <T> Result<T> success(T data) {
//        Result<T> result = new Result<>();
//        result.setCode(200);
//        result.setMessage("成功");
//        result.setData(data);
//        return result;
//    }
//
//    public static <T> Result<T> error(int code, String message) {
//        Result<T> result = new Result<T>();
//        result.code = code;
//        result.message = message;
//        return result;
//    }
//
//    public static <T> Result<T> error(String message) {
//        Result<T> result = new Result<>();
//        result.setCode(HttpCodeConstant.CONFLICT);
//        result.setMessage(message);
//        return result;
//    }
//
//}
@Data
@Slf4j
public class Result {

    private Boolean isSuccess;

    private Integer code;

    private String message;

    private Map<String, Object> data = new HashMap<>();

    public static Result ok() {
        return new Result()
                .setCode(CommonConstants.ResultCode.SUCCESS.CODE)
                .setMessage(CommonConstants.ResultCode.SUCCESS.MESSAGE)
                .setSuccess(true);
    }

    public static Result ok(Map<String, ?> map) {
        return new Result()
                .setCode(CommonConstants.ResultCode.SUCCESS.CODE)
                .setMessage(CommonConstants.ResultCode.SUCCESS.MESSAGE)
                .setSuccess(true)
                .putData(map);
    }

    public static Result ok(Collection<?> map) {
        return new Result()
                .setCode(CommonConstants.ResultCode.SUCCESS.CODE)
                .setMessage(CommonConstants.ResultCode.SUCCESS.MESSAGE)
                .setSuccess(true)
                .putData(map);
    }

    public static Result ok(Object object) {
        return new Result()
                .setCode(CommonConstants.ResultCode.SUCCESS.CODE)
                .setMessage(CommonConstants.ResultCode.SUCCESS.MESSAGE)
                .setSuccess(true)
                .putData(object);
    }

    public static Result error() {
        return new Result()
                .setCode(CommonConstants.ResultCode.ERROR.CODE)
                .setMessage(CommonConstants.ResultCode.ERROR.MESSAGE)
                .setSuccess(false);
    }

    public static Result error(String message) {
        return new Result()
                .setCode(CommonConstants.ResultCode.ERROR.CODE)
                .setMessage(message)
                .setSuccess(false);
    }

    public static Result error(Integer code, String message) {
        return new Result()
                .setCode(code)
                .setMessage(message)
                .setSuccess(false);
    }

    public static Result forward() {
        return new Result().setCode(CommonConstants.ResultCode.FORWARD.CODE).setMessage(CommonConstants.ResultCode.FORWARD.MESSAGE).setSuccess(true);
    }

    public static Result flush() {
        return new Result().setCode(CommonConstants.ResultCode.FLUSH.CODE).setMessage(CommonConstants.ResultCode.FLUSH.MESSAGE).setSuccess(true);
    }

    public static Result refuse() {
        return new Result().setCode(CommonConstants.ResultCode.REFUSE.CODE).setMessage(CommonConstants.ResultCode.REFUSE.MESSAGE).setSuccess(false);
    }

    private static Map<String, Object> convertObjectToMap(Object obj, @Nullable List<Class<?>> classList) {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true); // 使私有属性可访问，JDK 17 需要配置JVM参数
            try {
                if (classList != null && matchesClassList(field.getType(), classList)) {
                    map.put(field.getName(), null);
                } else {
                    map.put(field.getName(), field.get(obj));
                }
            } catch (IllegalAccessException e) {
                log.error(e.getMessage());
            }
        }
        return map;
    }

    private static boolean matchesClassList(Class<?> fieldType, List<Class<?>> classList) {
        for (Class<?> cls : classList) {
            if (cls.isAssignableFrom(fieldType)) {
                return true;
            }
        }
        return false;
    }

    private static Map<String, Object> convertObjectToMap(Object obj) {
        return convertObjectToMap(obj, null);
    }

    public Result setData(String key, Object object) {
        data.put(key, object);
        return this;
    }

    public Result putData(Map<String, ?> map) {
        data.putAll(map);
        return this;
    }

    public Result putData(Collection<?> list) {
        data.put("list", list);
        return this;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public Result setSuccess(Boolean success) {
        isSuccess = success;
        return this;
    }

    public Result setCode(Integer code) {
        this.code = code;
        return this;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Result putData(Object object) {
        if (object instanceof String) {
            data.put("value", object);
        } else {
            ArrayList<Class<?>> classList = new ArrayList<>();
            data.putAll(convertObjectToMap(object, classList));
        }
        return this;
    }


    @Override
    public String toString() {
        return "Result{" +
                "isSuccess=" + isSuccess +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

}
