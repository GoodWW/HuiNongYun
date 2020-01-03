package com.cdzp.huinongyun.util;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ZoomListenter implements OnTouchListener {

    private int mode = 0;
    float starX, starY = 0;
    float start = 0;
    private boolean isone = true;
    private boolean iscall = true;
    private boolean ontouch = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                starX = event.getX();
                starY = event.getY();
                mode = 1;
                break;
            case MotionEvent.ACTION_UP:
                isone = true;
                isX = true;
                isY = true;
                mode = 0;
                listener.callback(7);
                iscall = true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode -= 1;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                float x = event.getX(0) - event.getX(1);
                float y = event.getY(0) - event.getY(1);
                start = (float) Math.sqrt(x * x + y * y);
                mode += 1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == 1 && isone) {
                    calculate(event);
                }
                if (mode >= 2) {
                    isone = false;
                    calculate2(event);
                }
                break;
        }
        return ontouch;
    }

    public void setOntouch(boolean ontouch) {
        this.ontouch = ontouch;
    }


    public boolean isOntouch() {
        return ontouch;
    }

    private void calculate2(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        float end = (float) Math.sqrt(x * x + y * y);
        float gap = end - start;
        if (gap > 100) {
            if (iscall) {
                listener.callback(5);
                iscall = false;
            }
            start = end;
        } else if (gap < -100) {
            if (iscall) {
                listener.callback(6);
                iscall = false;
            }
            start = end;
        }
    }

    private boolean isX = true;
    private boolean isY = true;

    private void calculate(MotionEvent event) {
        float endX = event.getX();
        float endY = event.getY();
        float gapX = endX - starX;
        float gapY = endY - starY;
        if (isX) {
            if (gapX > 100) {
                if (iscall) {
                    listener.callback(4);
                    iscall = false;
                }
                starX = endX;
                isY = false;
            } else if (gapX < -100) {
                if (iscall) {
                    listener.callback(3);
                    iscall = false;
                }
                starX = endX;
                isY = false;
            }
        }

        if (isY) {
            if (gapY > 100) {
                if (iscall) {
                    listener.callback(2);
                    iscall = false;
                }
                starY = endY;
                isX = false;
            } else if (gapY < -100) {
                if (iscall) {
                    listener.callback(1);
                    iscall = false;
                }
                starY = endY;
                isX = false;
            }
        }

    }

    private Listener listener;

    //num:1上，2下，3左，4右，5两指间距扩大，6两指间距缩小，7手松开
    public interface Listener {
        void callback(int num);
    }

    public void call(int num) {
        if (listener != null) {
            listener.callback(num);
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

}
