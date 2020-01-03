package com.cdzp.huinongyun.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cdzp.huinongyun.R;

/**
 * 在线施肥机运行参数
 */
public class OnlOperatingParameters extends Fragment {

    private View view;
    private EditText et_water1, et_water2, et_ph1, et_ph2, et_a1, et_a2, et_b1, et_b2, et_c1,
            et_c2, et_d1, et_d2, et1, et2, et3, et4, et5, et6, et7, et8, et9, et10, et11;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_onl_operating_parameters, container, false);
            initView();
        }
        return view;
    }

    private void initView() {
        et_water1 = view.findViewById(R.id.et_onl_foop_water1);
        et_water2 = view.findViewById(R.id.et_onl_foop_water2);
        et_ph1 = view.findViewById(R.id.et_onl_foop_ph1);
        et_ph2 = view.findViewById(R.id.et_onl_foop_ph2);
        et_a1 = view.findViewById(R.id.et_onl_foop_a1);
        et_a2 = view.findViewById(R.id.et_onl_foop_a2);
        et_b1 = view.findViewById(R.id.et_onl_foop_b1);
        et_b2 = view.findViewById(R.id.et_onl_foop_b2);
        et_c1 = view.findViewById(R.id.et_onl_foop_c1);
        et_c2 = view.findViewById(R.id.et_onl_foop_c2);
        et_d1 = view.findViewById(R.id.et_onl_foop_d1);
        et_d2 = view.findViewById(R.id.et_onl_foop_d2);
        et1 = view.findViewById(R.id.et_onl_foop1);
        et2 = view.findViewById(R.id.et_onl_foop2);
        et3 = view.findViewById(R.id.et_onl_foop3);
        et4 = view.findViewById(R.id.et_onl_foop4);
        et5 = view.findViewById(R.id.et_onl_foop5);
        et6 = view.findViewById(R.id.et_onl_foop6);
        et7 = view.findViewById(R.id.et_onl_foop7);
        et8 = view.findViewById(R.id.et_onl_foop8);
        et9 = view.findViewById(R.id.et_onl_foop9);
        et10 = view.findViewById(R.id.et_onl_foop10);
        et11 = view.findViewById(R.id.et_onl_foop11);

        if (OnlFert.config != null && OnlFert.config.getData() != null &&
                OnlFert.config.getData().getRunParameter() != null) {
            et_water1.setText(OnlFert.config.getData().getRunParameter().getFertSCI() + "");
            et_water2.setText(OnlFert.config.getData().getRunParameter().getPH_S() + "");
            et_ph1.setText(OnlFert.config.getData().getRunParameter().getFertPHCI() + "");
            et_ph2.setText(OnlFert.config.getData().getRunParameter().getPH_PH() + "");
            et_a1.setText(OnlFert.config.getData().getRunParameter().getFertACI() + "");
            et_a2.setText(OnlFert.config.getData().getRunParameter().getPH_A() + "");
            et_b1.setText(OnlFert.config.getData().getRunParameter().getFertBCI() + "");
            et_b2.setText(OnlFert.config.getData().getRunParameter().getPH_B() + "");
            et_c1.setText(OnlFert.config.getData().getRunParameter().getFertCCI() + "");
            et_c2.setText(OnlFert.config.getData().getRunParameter().getPH_C() + "");
            et_d1.setText(OnlFert.config.getData().getRunParameter().getFertDCI() + "");
            et_d2.setText(OnlFert.config.getData().getRunParameter().getPH_D() + "");
            et1.setText(OnlFert.config.getData().getRunParameter().getClearTime() + "");
            et2.setText(OnlFert.config.getData().getRunParameter().getErrEC() + "");
            et3.setText(OnlFert.config.getData().getRunParameter().getErrPH() + "");
            et4.setText(OnlFert.config.getData().getRunParameter().getHighECAlarm() + "");
            et5.setText(OnlFert.config.getData().getRunParameter().getLowPHAlarm() + "");
            if (OnlFert.config.getData().getRunParameter().getLowPHAlarm() == 1) {
                et6.setText(getString(R.string.onl_fert83));
            } else et6.setText(getString(R.string.onl_fert82));
            et7.setText(OnlFert.config.getData().getRunParameter().getProportion_A() + "");
            et8.setText(OnlFert.config.getData().getRunParameter().getProportion_B() + "");
            et9.setText(OnlFert.config.getData().getRunParameter().getProportion_C() + "");
            et10.setText(OnlFert.config.getData().getRunParameter().getProportion_D() + "");
            et11.setText(OnlFert.config.getData().getRunParameter().getPertreatECvalue() + "");
        }
    }

}
