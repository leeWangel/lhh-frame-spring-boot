package com.lhh.base.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class UtilsModel {
    /**
     * 把source对象中非空的值COPY到target中
     * ModelUtils.transferValue(source, target);
     * @param source
     * @param target
     */
    public static void transferValue(Object source, Object target) {
        if (source == target) {
            return;
        }
        Method[] methodsOld = source.getClass().getMethods();
        Method[] methodsNew = target.getClass().getMethods();
        for (int i = 0; i < methodsOld.length; i++) {
            if (!methodsOld[i].getName().startsWith("get") && !methodsOld[i].getName().startsWith("is")) {
                continue;
            }
            //suffix of methodName
            String suffix = methodsOld[i].getName().substring(3);
            if(methodsOld[i].getName().startsWith("get")) {
                suffix = methodsOld[i].getName().substring(3);
            } else {
                suffix = methodsOld[i].getName().substring(2);
            }
            for (int j = 0; j < methodsNew.length; j++) {
                if (!methodsNew[j].getName().equals("set" + suffix)) {
                    continue;
                }
                if (!isGetMethod(methodsOld[i]) || !isSetMethod(methodsNew[j])
                /*get的返回类型要和SET的参数类型一致*/
                        || methodsNew[j].getParameterTypes()[0] != methodsOld[i].getReturnType()
                        ) {
                    continue;
                }

                Object getObj = invokeGetMethod(source, methodsOld[i]);
                if (getObj != null) {
                    invokeSetMethod(target, methodsNew[j], getObj);
                }
                break;
            }
        }
    }
    /**
     * get没有参数，get方法是public的
     * @param method
     * @return
     */
    public static boolean isGetMethod(Method method) {
        if (method == null) {
            return false;
        }
        if (!method.getName().startsWith("get") && !method.getName().startsWith("is")) {
            return false;
        }
        if (method.getParameterTypes().length > 0) {
            return false;
        }
        if (!Modifier.isPublic(method.getModifiers())) {
            return false;
        }
        return true;
    }

    /**
     * set的参数必须只有一个，set的返回类型为void，set方法是public的
     * @param method
     * @return
     */
    public static boolean isSetMethod(Method method) {
        if (method == null) {
            return false;
        }
        if (!method.getName().startsWith("set")) {
            return false;
        }
        if (method.getParameterTypes().length != 1) {
            return false;
        }
        if (method.getReturnType() != Void.TYPE) {
            return false;
        }
        if (!Modifier.isPublic(method.getModifiers())) {
            return false;
        }
        return true;
    }

    /**
     * 调用get方法，转换非RuntimeException
     * @param object
     * @param method
     * @return
     */
    public static Object invokeGetMethod(Object object, Method method) {
        try {
            return method.invoke(object, new Object[0]);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        } catch (InvocationTargetException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    /**
     * 调用set方法，转换非RuntimeException
     * @param object
     * @param method
     * @return
     */
    public static void invokeSetMethod(Object object, Method method, Object value) {
        try {
            method.invoke(object, new Object[] {value});
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        } catch (InvocationTargetException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
}
