package com.mei.financial.entity;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.meis.base.mei.entity.Result;

import java.lang.reflect.Type;
import java.util.List;

import rx.functions.Func1;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/30
 */
public class ResultFunc<T> implements Func1<String, Result<T>> {
    Class beanClass;

    public ResultFunc(Class beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public Result<T> call(String result) {
        Result<T> t = null;
        try {
            t = (Result<T>) fromJsonObject(result, beanClass);
        } catch (JsonSyntaxException e) {//解析异常，说明是array数组
            t = (Result<T>) fromJsonArray(result, beanClass);
        }
        return t;
    }

    public static <T> Result<T> fromJsonObject(String reader, Class<T> clazz) {
        Type type = new ParameterizedTypeImpl(Result.class, new Class[]{clazz});
        return new Gson().fromJson(reader, type);
    }

    public static <T> Result<List<T>> fromJsonArray(String reader, Class<T> clazz) {
        // 生成List<T> 中的 List<T>
        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{clazz});
        // 根据List<T>生成完整的Result<List<T>>
        Type type = new ParameterizedTypeImpl(Result.class, new Type[]{listType});
        return new Gson().fromJson(reader, type);
    }
}
