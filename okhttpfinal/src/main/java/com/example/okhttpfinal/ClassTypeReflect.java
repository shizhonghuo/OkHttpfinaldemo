package com.example.okhttpfinal;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017/6/14.
 */

public class ClassTypeReflect {
    static Type getModelClazz(Class<?> subclass){
        return getGenericType(0,subclass);
    }


    private static Type getGenericType(int index, Class<?> subclass){
        //返回表示此 subclass所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type superclass = subclass.getGenericSuperclass();
        if(!(superclass instanceof ParameterizedType)){
            return Object.class;
        }
        //返回表示此superclass实际类型参数的 Type 对象的数组
        Type[] params=((ParameterizedType) superclass).getActualTypeArguments();

        if(index >=params.length || index <0){
            throw  new RuntimeException("index out of bounds");
        }
        if(!(params[index] instanceof  Class)){
            return  Object.class;
        }
        return params[index];
    }
}
