package com.yj.gyl.bank.service.yibao.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Administrator 反射工具
 */
public class ReflectHelper {
    /**
     * 获取obj对象fieldName的Field
     *
     * @param obj
     * @param fieldName
     * @return
     */
    public static Field getFieldByFieldName(Object obj, String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
            }
        }
        return null;
    }

    /**
     * 获取obj对象fieldName的属性值
     *
     * @param obj
     * @param fieldName
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object getValueByFieldName(Object obj, String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = getFieldByFieldName(obj, fieldName);
        Object value = null;
        if (field != null) {
            if (field.isAccessible()) {
                value = field.get(obj);
            } else {
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(false);
            }
        }
        return value;
    }

    /**
     * 设置obj对象fieldName的属性值
     *
     * @param obj
     * @param fieldName
     * @param value
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setValueByFieldName(Object obj, String fieldName, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        if (field.isAccessible()) {
            field.set(obj, value);
        } else {
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        }
    }

    /**
     * 将对象1转换为对象2，并给相同属性字段复制
     *
     * @param object1
     * @param object2
     * @return
     */
    public static Object getTargetObject(Object object1, Object object2) {
        Field[] fields1 = object1.getClass().getDeclaredFields();
        Field[] fields2 = object2.getClass().getDeclaredFields();
        if (null != fields1 && null != fields2) {
            for (Field field1 : fields1) {
                String name1 = field1.getName();
                try {
                    if ("serialVersionUID".equals(name1)) {
                        continue;
                    }
                    PropertyDescriptor pd = new PropertyDescriptor(name1, object1.getClass());
                    Method getMethod = pd.getReadMethod();//获得get方法
                    Object ohh = getMethod.invoke(object1);//执行get方法返回一个Object
                    for (Field field2 : fields2) {
                        String name2 = field2.getName();
                        if (name1.equalsIgnoreCase(name2)) {
                            PropertyDescriptor pd2 = new PropertyDescriptor(name2, object2.getClass());
                            Method writeMethod = pd2.getWriteMethod();//获得set方法
                            writeMethod.invoke(object2, ohh);//执行set方法，将ohh的值放入
                        }
                    }
                } catch (Exception e) {
                    System.out.println(name1);
                    //e.printStackTrace();
                }
            }
        }
        return object2;
    }

}