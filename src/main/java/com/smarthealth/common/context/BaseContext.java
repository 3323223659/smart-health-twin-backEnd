package com.smarthealth.common.context;

public class BaseContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    //ThreadLocal为每一个线程提供一份单独的存储空间，具有线程隔离的效果，只有在线程内才能获取对应的值，线程外不能访问
    //也就是，前端的每一次请求，都可以算是一次线程，在这次线程内，我们可以共享你自己设置的数据

    public static void setCurrentId(Long id) {
        threadLocal.set(id);  //设置当前线程的局部变量的值
    }

    public static Long getCurrentId() {
        return threadLocal.get();   //返回当前线程所对应的线程局部变量的值
    }

    public static void removeCurrentId() {
        threadLocal.remove();   //移除当前线程的线程局部变量
    }

}
