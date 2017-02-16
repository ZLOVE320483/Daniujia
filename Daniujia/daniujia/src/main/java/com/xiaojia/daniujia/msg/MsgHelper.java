package com.xiaojia.daniujia.msg;

import android.util.SparseArray;

import com.xiaojia.daniujia.managers.ThreadWorker;
import com.xiaojia.daniujia.msg.annotation.OnMsg;

import java.lang.reflect.Field;

public class MsgHelper {

    private static MsgHelper instance = new MsgHelper();

    public static MsgHelper getInstance() {
        return instance;
    }
    private SparseArray<McMsgQueuesHandle> msgQueueHandleMap;
    private MsgHelper() {
        msgQueueHandleMap = new SparseArray<>();
    }

    public void sendMsg(int cmd) {
        sendMsg(McMsg.newInstance(cmd));
    }

    McMsgQueuesHandle getListener(int cmd) {
        McMsgQueuesHandle msgQueueHandle = msgQueueHandleMap.get(cmd);
        if (msgQueueHandle == null) {
            msgQueueHandle = new McMsgQueuesHandle(cmd);
            msgQueueHandleMap.put(cmd, msgQueueHandle);
        }
        return msgQueueHandle;
    }

    public void sendMsg(McMsg mm) {
        final McMsg temp = mm;
        if (mm != null) {
            ThreadWorker.execute(new Runnable() {
                public void run() {
                    McMsgQueuesHandle msgQueueHandle = getListener(temp.msgCmd);
                    msgQueueHandle.handleMsg(temp);
                }
            });
        }
    }

    public void registMsg(Object obj) {
        // （zp）获取该类的所有字段，包括私有的，但是不包含父类字段
        Field[] fields = obj.getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                // （zp）查看这些字段中是否存在OnMsg这个类中的注释
                OnMsg onMsg = field.getAnnotation(OnMsg.class);
                if (onMsg != null) {
                    // （zp）获取obj中存在注解的msg值，这个值的初始化是在这个注解的上方进行的。
                    // （zp）比如：@OnMsg(msg = { InsideMsg.NOTIFY_PUSH_NEW_MESSAGE }, useLastMsg = false)
                    int[] msgs = onMsg.msg();
                    field.setAccessible(true);
                    try {
                        // （zp）获取obj对象中，field字段的值
                        Object value = field.get(obj);
                        if (value instanceof OnMsgCallback) {
                            OnMsgCallback callback = (OnMsgCallback) value;
                            registMsg(msgs, callback, onMsg);
                        } else {
                            System.out.println(field.getName() + " in " + obj + " is not MsgCallback instance.");
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @param cmds   message type to be annotated
     *
     * @param callback onMsgCallback to be register
     *
     * @param onMsg     total @interface of OnMsg
     */
    public void registMsg(int[] cmds, OnMsgCallback callback, OnMsg onMsg) {
        for (int cmd : cmds) {
            McMsgQueuesHandle msgQueueHandle = getListener(cmd);
            MsgCallbackImpl wrap = new MsgCallbackImpl(cmd, onMsg, callback);
            msgQueueHandle.addCallback(wrap);
            if (onMsg.useLastMsg()) {
                msgQueueHandle.doLastMsg();
            }
        }
    }

    public void unRegistMsg(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                OnMsg onMsg = field.getAnnotation(OnMsg.class);
                if (onMsg != null) {

                    int[] msgs = onMsg.msg();

                    field.setAccessible(true);
                    try {
                        Object value = field.get(obj);
                        if (value instanceof OnMsgCallback) {
                            OnMsgCallback callback = (OnMsgCallback) value;
                            unRegistMsg(msgs, callback);
                        } else {
                            System.out.println(field.getName() + " in " + obj + " is not MsgCallback instance.");
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void unRegistMsg(int[] cmds, OnMsgCallback callback) {
        for (int cmd : cmds) {
            McMsgQueuesHandle msgQueueHandle = getListener(cmd);
            msgQueueHandle.remove(callback);
        }
    }

    public void clearMsgs() {
        for (int i = 0; i < msgQueueHandleMap.size(); i++) {
            int key = msgQueueHandleMap.keyAt(i);
            McMsgQueuesHandle hanlde = msgQueueHandleMap.get(key);
            if (hanlde != null) {
                hanlde.clearLastMsg();
            }
        }
    }

    public void clearCallbacks() {
        msgQueueHandleMap.clear();
    }
}