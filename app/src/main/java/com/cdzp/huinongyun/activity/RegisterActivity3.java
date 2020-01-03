package com.cdzp.huinongyun.activity;

import android.view.View;
import android.widget.TextView;

import com.cdzp.huinongyun.BaseActivity;
import com.cdzp.huinongyun.R;

public class RegisterActivity3 extends BaseActivity {


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_register3);
    }

    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void initView() {
        TextView tv = findViewById(R.id.tv_title);
        tv.setText(getResources().getString(R.string.top_register_str));

        findViewById(R.id.ll_back).setOnClickListener(this);
    }

    @Override
    public void dealLogicAfterInitView() {

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            default:

                break;
        }
    }
}
