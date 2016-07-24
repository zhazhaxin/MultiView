package cn.lemon.multi.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linlongxin on 2016/1/19.
 * 在两个没有任何联系的类里面实现通讯，类似EventBus
 */
public class MessageNotify {

    private static MessageNotify instance;

    private Map<Object, Method> map = new HashMap<>();

    private MessageNotify() {
    }

    public static MessageNotify getInstance() {
        if (instance == null) {
            synchronized (MessageNotify.class) {
                instance = new MessageNotify();
            }
        }
        return instance;
    }

    /**
     * 注册一个方法
     *
     * @param object  method方法的实例类
     * @param method
     */
    public void registerEvent(Object object, Method method) {
        map.put(object, method);
    }

    /**
     * 通过sendMessage()方法来执行这个注册的实例的方法
     */
    private void executeEvent() {
        for (Map.Entry<Object, Method> entry : map.entrySet()) {
            try {
                entry.getValue().invoke(entry.getKey());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送消息通知注册的实例
     */
    public void sendMessage() {
        executeEvent();
    }


}
