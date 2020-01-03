package com.cdzp.huinongyun.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.adapter.IrrOnlAdapter;
import com.cdzp.huinongyun.util.MyCallback;

/**
 * 在线施肥机基本配置
 */
public class OnlBasicConfiguration extends Fragment implements View.OnClickListener {

    private View view;
    private EditText et1, et2, et3, et4, et6, et7, et8, et9, et91, et92;
    private TextView tv1, tv2, tv3, tv4;
    private IrrOnlAdapter adapter;
    private AlertDialog mDialog;
    private boolean[] check1, check2, check3, check4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_onl_basic_configuration, container, false);
            initView();
        }
        return view;
    }

    private void initView() {
        et1 = view.findViewById(R.id.et_fobc1);
        et2 = view.findViewById(R.id.et_fobc2);
        et3 = view.findViewById(R.id.et_fobc3);
        et4 = view.findViewById(R.id.et_fobc4);
        et6 = view.findViewById(R.id.et_fobc6);
        et7 = view.findViewById(R.id.et_fobc7);
        et8 = view.findViewById(R.id.et_fobc8);
        et9 = view.findViewById(R.id.et_fobc9);
        et91 = view.findViewById(R.id.et_fobc91);
        et92 = view.findViewById(R.id.et_fobc92);

        tv1 = view.findViewById(R.id.tv_fobc1);
        tv2 = view.findViewById(R.id.tv_fobc2);
        tv3 = view.findViewById(R.id.tv_fobc3);
        tv4 = view.findViewById(R.id.tv_fobc4);

        if (OnlFert.config == null || OnlFert.config.getData() == null ||
                OnlFert.config.getData().getBaseInfo() == null) {
            return;
        }

        view.findViewById(R.id.ll_fobc1).setOnClickListener(this);
        view.findViewById(R.id.ll_fobc2).setOnClickListener(this);
        view.findViewById(R.id.ll_fobc3).setOnClickListener(this);
        view.findViewById(R.id.ll_fobc4).setOnClickListener(this);

        et1.setText(OnlFert.config.getData().getBaseInfo().getValveNum() + "");
        et2.setText(OnlFert.config.getData().getBaseInfo().getPulsePerLitre() + "");
        et3.setText(OnlFert.config.getData().getBaseInfo().getFertMaxFlow() + "");
        if (OnlFert.config.getData().getBaseInfo().getHasInPump() == 1) {
            et4.setText(getString(R.string.onl_fert149));
        } else et4.setText(getString(R.string.onl_fert150));
        et6.setText((OnlFert.config.getData().getBaseInfo().getInPumpCh() + 1) + "");
        if (OnlFert.config.getData().getBaseInfo().getHasPertreat() == 1) {
            et7.setText(getString(R.string.onl_fert149));
        } else et7.setText(getString(R.string.onl_fert150));
        switch (OnlFert.config.getData().getBaseInfo().getValvePlan()) {
            case 0:
                et8.setText(getString(R.string.onl_fert151));
                break;
            case 1:
                et8.setText(getString(R.string.onl_fert152));
                break;
            case 2:
                et8.setText(getString(R.string.onl_fert153));
                break;
            default:
                et8.setText("");
                break;
        }
        switch (OnlFert.config.getData().getBaseInfo().getValveType()) {
            case 0:
                et9.setText(getString(R.string.onl_fert154));
                break;
            case 1:
                et9.setText(getString(R.string.onl_fert155));
                break;
            default:
                et9.setText("");
                break;
        }
        et91.setText(OnlFert.config.getData().getBaseInfo().getLevelRange() + "");
        et92.setText(OnlFert.config.getData().getBaseInfo().getInPumpDelay() + "");

        check1 = new boolean[128];
        check2 = new boolean[128];
        check3 = new boolean[128];
        check4 = new boolean[128];

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 64; i++) {
            if ((OnlFert.config.getData().getBaseInfo().getTankOneValveL() & 1l << i) != 0) {
                check1[i] = true;
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append((i + 1));
            }
        }
        for (int i = 0; i < 64; i++) {
            if ((OnlFert.config.getData().getBaseInfo().getTankOneValveH() & 1l << i) != 0) {
                check1[i + 64] = true;
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append((i + 65));
            }
        }
        tv1.setText(sb.toString());


        sb = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            if ((OnlFert.config.getData().getBaseInfo().getTankTwoValveL() & 1l << i) != 0) {
                check2[i] = true;
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append((i + 1));
            }
        }
        for (int i = 0; i < 64; i++) {
            if ((OnlFert.config.getData().getBaseInfo().getTankTwoValveH() & 1l << i) != 0) {
                check2[i + 64] = true;
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append((i + 65));
            }
        }
        tv2.setText(sb.toString());


        sb = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            if ((OnlFert.config.getData().getBaseInfo().getTankThreeValveL() & 1l << i) != 0) {
                check3[i] = true;
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append((i + 1));
            }
        }
        for (int i = 0; i < 64; i++) {
            if ((OnlFert.config.getData().getBaseInfo().getTankThreeValveH() & 1l << i) != 0) {
                check3[i + 64] = true;
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append((i + 65));
            }
        }
        tv3.setText(sb.toString());


        sb = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            if ((OnlFert.config.getData().getBaseInfo().getTankFourValveL() & 1l << i) != 0) {
                check4[i] = true;
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append((i + 1));
            }
        }
        for (int i = 0; i < 64; i++) {
            if ((OnlFert.config.getData().getBaseInfo().getTankFourValveH() & 1l << i) != 0) {
                check4[i + 64] = true;
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append((i + 65));
            }
        }
        tv4.setText(sb.toString());


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.irrlayout, null);
        RecyclerView mRecyclerView = contentView.findViewById(R.id.irr);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        adapter = new IrrOnlAdapter();
        adapter.setValvesNum(0);
        mRecyclerView.setAdapter(adapter);
        builder.setTitle(getString(R.string.fert_str17));
        builder.setView(contentView);
        builder.setPositiveButton(getString(R.string.personal_str6), null);
        mDialog = builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fobc1:
                adapter.setCheck(check1);
                adapter.notifyDataSetChanged();
                mDialog.show();
                break;
            case R.id.ll_fobc2:
                adapter.setCheck(check2);
                adapter.notifyDataSetChanged();
                mDialog.show();
                break;
            case R.id.ll_fobc3:
                adapter.setCheck(check3);
                adapter.notifyDataSetChanged();
                mDialog.show();
                break;
            case R.id.ll_fobc4:
                adapter.setCheck(check4);
                adapter.notifyDataSetChanged();
                mDialog.show();
                break;
            default:
                break;
        }
    }
}
