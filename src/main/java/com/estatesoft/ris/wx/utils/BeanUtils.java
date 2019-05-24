package com.estatesoft.ris.wx.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * .
 *
 * @author .
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class BeanUtils {

    /**
     * 构造函数.
     */
    private BeanUtils() {
        throw new RuntimeException("This is util class,can not instance");
    }

    /**
     * 添加方法注释.
     *
     * @return 日期
     */
    private static Date getStartDate() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1900-01-01 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }

    public static void copyProperties(Object source, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target);
    }

    public static <T> List<T> copyList(List source, Class<T> targetClass) {
        String jsonStr = JSON.toJSONString(source);
        return JSON.parseArray(jsonStr, targetClass);
    }

    private static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyPropertiesIgnoreNull(Object src, Object target){
        org.springframework.beans.BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    /**
     * 添加方法注释.
     *
     * @param obj 类
     */
    public static void assignmentObject(Object obj) {
        try {
            if (null == obj) {
                return;
            }
            Field[] fields = obj.getClass().getDeclaredFields();
            Class cla = obj.getClass();
            for (Field field : fields) {
                setFieldValue(obj, cla, field);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加方法注释.
     *
     * @param obj
     * @param cla
     * @param field
     * @throws Exception
     */
    private static void setFieldValue(Object obj, Class cla, Field field)
            throws Exception {
        Class type = field.getType();
        String fieldName = field.getName();
        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method method;
        if (String.class.equals(type)) {
            method = cla.getMethod(methodName, String.class);
            method.invoke(obj, "");
        } else if (char.class.equals(type)) {
            method = cla.getMethod(methodName, char.class);
            method.invoke(obj, ' ');
        } else if (Character.class.equals(type)) {
            method = cla.getMethod(methodName, Character.class);
            method.invoke(obj, ' ');
        } else if (boolean.class.equals(type)) {
            method = cla.getMethod(methodName, boolean.class);
            method.invoke(obj, true);
        } else if (Boolean.class.equals(type)) {
            method = cla.getMethod(methodName, Boolean.class);
            method.invoke(obj, true);
        } else if (byte.class.equals(type)) {
            method = cla.getMethod(methodName, byte.class);
            method.invoke(obj, (byte) 0);
        } else if (Byte.class.equals(type)) {
            method = cla.getMethod(methodName, Byte.class);
            method.invoke(obj, (byte) 0);
        } else if (short.class.equals(type)) {
            method = cla.getMethod(methodName, short.class);
            method.invoke(obj, (short) 0);
        } else if (Short.class.equals(type)) {
            method = cla.getMethod(methodName, Short.class);
            method.invoke(obj, (short) 0);
        } else if (int.class.equals(type)) {
            method = cla.getMethod(methodName, int.class);
            method.invoke(obj, 0);
        } else if (Integer.class.equals(type)) {
            method = cla.getMethod(methodName, Integer.class);
            method.invoke(obj, 0);
        } else if (long.class.equals(type)) {
            method = cla.getMethod(methodName, long.class);
            method.invoke(obj, 0);
        } else if (Long.class.equals(type)) {
            method = cla.getMethod(methodName, Long.class);
            method.invoke(obj, 0L);
        } else if (float.class.equals(type)) {
            method = cla.getMethod(methodName, float.class);
            method.invoke(obj, 0.0f);
        } else if (Float.class.equals(type)) {
            method = cla.getMethod(methodName, Float.class);
            method.invoke(obj, 0.0f);
        } else if (double.class.equals(type)) {
            method = cla.getMethod(methodName, double.class);
            method.invoke(obj, 0.0d);
        } else if (Double.class.equals(type)) {
            method = cla.getMethod(methodName, Double.class);
            method.invoke(obj, 0.0d);
        } else if (Date.class.equals(type)) {
            if ("setModifyTime".equals(methodName)) {
                method = cla.getMethod(methodName, Date.class);
                method.invoke(obj, new Date());
                return;
            }
            method = cla.getMethod(methodName, Date.class);
            method.invoke(obj, getStartDate());
        } else if (BigDecimal.class.equals(type)) {
            method = cla.getMethod(methodName, BigDecimal.class);
            method.invoke(obj, new BigDecimal(0.0D));
        }
    }

    /**
     * 初始化对象属性
     *
     * @param obj 对象
     */
    public static void notNull(Object obj) {
        try {
            if (null == obj) {
                return;
            }
            Field[] fields = getFields(obj);
            Class cla = obj.getClass();
            for (Field field : fields) {
                if(isId(field))
                    continue;
                field.setAccessible(true);
                if (null == field.get(obj)) {
                    setFieldValue(obj, cla, field);
                }
                field.setAccessible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Field[] getFields(Object obj) {
        Class cla = obj.getClass();
        List<Field> fieldList = new ArrayList<>();
        for (; cla != Object.class; cla = cla.getSuperclass()) {
            Field[] fields = cla.getDeclaredFields();
            for (Field field : fields) {
                fieldList.add(field);
            }
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }
    private static boolean isId(Field field){
        Annotation[] annotations=field.getDeclaredAnnotations();
        for(Annotation annotation:annotations){
            if(annotation instanceof Id)
                return true;
        }
        return false;
    }
}
