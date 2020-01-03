package com.cdzp.huinongyun.util;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 *
 */

public class Toasts {
//    private static String oldMsg;
//    private static Toast toast;
//    private static long oneTime = 0;
//    private static long twoTime = 0;
    private static Toast mToast;

//    public static void showToast(Context context, String s) {
//        if (toast == null) {
//            toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
//            toast.show();
//            oneTime = System.currentTimeMillis();
//        } else {
//            twoTime = System.currentTimeMillis();
//            if (s.equals(oldMsg)) {
//                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
//                    toast.show();
//                }
//            } else {
//                oldMsg = s;
//                toast.setText(s);
//                toast.show();
//            }
//        }
//        oneTime = twoTime;
//    }

    public static void showSuccess(Context context, String s) {
        if (mToast == null) {
            mToast = Toasty.success(context.getApplicationContext(), s, Toast.LENGTH_SHORT, false);
            mToast.show();
        } else {
            mToast.cancel();
            mToast = Toasty.success(context.getApplicationContext(), s, Toast.LENGTH_SHORT, false);
            mToast.show();
        }
    }

    public static void showInfo(Context context, String s) {
        if (mToast == null) {
            mToast = Toasty.info(context.getApplicationContext(), s, Toast.LENGTH_SHORT, true);
            mToast.show();
        } else {
            mToast.cancel();
            mToast = Toasty.info(context.getApplicationContext(), s, Toast.LENGTH_SHORT, true);
            mToast.show();
        }
    }

    public static void showError(Context context, String s) {
        if (mToast == null) {
            mToast = Toasty.error(context.getApplicationContext(), s, Toast.LENGTH_SHORT, false);
            mToast.show();
        } else {
            mToast.cancel();
            mToast = Toasty.error(context.getApplicationContext(), s, Toast.LENGTH_SHORT, false);
            mToast.show();
        }
    }
}
