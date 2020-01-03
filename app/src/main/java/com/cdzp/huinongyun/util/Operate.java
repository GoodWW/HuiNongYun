package com.cdzp.huinongyun.util;

import android.content.Context;

/**
 * 发送接收操作类
 */

public class Operate {
    private static Operate operate = null;
    private Sender sender;
    private Receiver receiver;

    private Operate(Context context) {
        sender = new Sender(context);
        receiver = new Receiver(sender.getSocket(),context);
    }

    /**
     * 初始化
     */
    public static void initSocket(Context context) {
        if (operate == null) {
            synchronized (Operate.class) {
                if (operate == null) {
                    operate = new Operate(context);
                }
            }
        }
    }

    public static Operate getDefault() {
        return operate;
    }

    public Sender getSender() {
        return sender;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void clear() {
        if (receiver != null) receiver.setResult(false);
        receiver = null;
        if (sender != null) sender.getSocket().close();
        sender = null;
        operate = null;
    }
}
