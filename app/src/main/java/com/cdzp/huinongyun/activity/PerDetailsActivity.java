package com.cdzp.huinongyun.activity;

import android.view.View;
import android.widget.TextView;

import com.cdzp.huinongyun.BaseActivity;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.util.Data;

public class PerDetailsActivity extends BaseActivity {

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_personal_details);
    }

    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void initView() {
        TextView tv = findViewById(R.id.tv_title);
        tv.setText(getString(R.string.personal_str2));
        findViewById(R.id.rl_right).setVisibility(View.INVISIBLE);

        TextView tv1 = findViewById(R.id.tv_apd1);
        TextView tv2 = findViewById(R.id.tv_apd2);
        TextView tv3 = findViewById(R.id.tv_apd3);
        TextView tv4 = findViewById(R.id.tv_apd4);
        TextView tv5 = findViewById(R.id.tv_apd5);
        TextView tv6 = findViewById(R.id.tv_apd6);
        if (Data.userResult != null && Data.userResult.getData() != null && Data.userResult.getData().size() > 0) {
            if (Data.userResult.getData().get(0).getUserName() != null) {
                tv1.setText(getString(R.string.personal_str20) + Data.userResult.getData().get(0).getUserName());
            }
            if (Data.userResult.getData().get(0).getNickName() != null) {
                tv2.setText(getString(R.string.personal_str21) + Data.userResult.getData().get(0).getNickName());
            }
            if (Data.userResult.getData().get(0).getApplyTime() != null) {
                tv3.setText(getString(R.string.personal_str22) + Data.userResult.getData().get(0).getApplyTime());
            }
            if (Data.userResult.getData().get(0).getContactMethod() != null) {
                if (Data.userResult.getData().get(0).getContactMethod().length() == 11) {
                    tv4.setText(getString(R.string.personal_str23) + Data.userResult.getData().get(0).getContactMethod().substring(0, 3)
                            + "****" + Data.userResult.getData().get(0).getContactMethod().substring(7, 11));
                } else tv4.setText(getString(R.string.personal_str24) + Data.userResult.getData().get(0).getContactMethod());
            }
            if (Data.userResult.getData().get(0).getRoleName() != null) {
                tv5.setText(getString(R.string.personal_str25) + Data.userResult.getData().get(0).getRoleName());
            }
            if (Data.userResult.getData().get(0).getEntName() != null) {
                tv6.setText(getString(R.string.personal_str26) + Data.userResult.getData().get(0).getEntName());
            }
        }

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
