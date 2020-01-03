package com.cdzp.huinongyun.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.adapter.OnlWaterConfigAdapter;
import com.cdzp.huinongyun.util.AdapterListener;
import com.cdzp.huinongyun.util.Toasts;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

/**
 * 在线施肥机水罐水池配置
 */
public class OnlWaterConfiguration extends Fragment {

    private View view;
    private OnlWaterConfigAdapter adapter;
    private AlertDialog mDialog;
    private TextView tv1, tv2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_onl_water_configuration, container, false);
            initView();
        }
        return view;
    }

    private void initView() {
        if (OnlFert.config != null && OnlFert.config.getData() != null &&
                OnlFert.config.getData().getWList() != null &&
                OnlFert.config.getData().getWList().size() > 0) {
            RecyclerView recyclerView = view.findViewById(R.id.recycler_fowc);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.addItemDecoration(new DefaultItemDecoration(getContext().getResources().getColor(R.color.decoration), 1, 1));
            adapter = new OnlWaterConfigAdapter(getContext());
            adapter.setListener(new AdapterListener() {
                @Override
                public void onClick(int position) {
                    showDialog(position);
                }
            });
            recyclerView.setAdapter(adapter);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View mView = LayoutInflater.from(getContext()).inflate(R.layout.onl_water_config_sd, null);
            tv1 = mView.findViewById(R.id.tv_onl_owcs1);
            tv2 = mView.findViewById(R.id.tv_onl_owcs2);
            builder.setView(mView);
            builder.setPositiveButton(getString(R.string.personal_str6), null);
            mDialog = builder.create();
        }
    }

    private void showDialog(int position) {
        switch (position) {
            case 0:
                tv1.setText(getString(R.string.onl_fert156));
                break;
            case 1:
                tv1.setText(getString(R.string.onl_fert157));
                break;
            case 2:
                tv1.setText(getString(R.string.onl_fert158));
                break;
            case 3:
                tv1.setText(getString(R.string.onl_fert159));
                break;
            case 4:
                tv1.setText(getString(R.string.onl_fert160));
                break;
            case 5:
                tv1.setText(getString(R.string.onl_fert161));
                break;
            case 6:
                tv1.setText(getString(R.string.onl_fert162));
                break;
            case 7:
                tv1.setText(getString(R.string.onl_fert163));
                break;
            case 8:
                tv1.setText(getString(R.string.onl_fert164));
                break;
            case 9:
                tv1.setText(getString(R.string.onl_fert165));
                break;
            case 10:
                tv1.setText(getString(R.string.onl_fert166));
                break;
            case 11:
                tv1.setText(getString(R.string.onl_fert167));
                break;
            case 12:
                tv1.setText(getString(R.string.onl_fert168));
                break;
            case 13:
                tv1.setText(getString(R.string.onl_fert169));
                break;
            case 14:
                tv1.setText(getString(R.string.onl_fert170));
                break;
            case 15:
                tv1.setText(getString(R.string.onl_fert171));
                break;
            case 16:
                tv1.setText(getString(R.string.onl_fert172));
                break;
            default:
                tv1.setText(getString(R.string.onl_fert173));
                break;
        }
        StringBuilder sb = new StringBuilder();
        if (OnlFert.config.getData().getWList().get(position).getHasTank() == 1) {
            sb.append(getString(R.string.onl_fert174));
        } else sb.append(getString(R.string.onl_fert175));
        sb.append("\n");
        sb.append("\n");

        if (OnlFert.config.getData().getWList().get(position).getHasValve() == 1) {
            sb.append(getString(R.string.onl_fert176) + "     " + getString(R.string.onl_fert1762));
        } else
            sb.append(getString(R.string.onl_fert177) + "     " + getString(R.string.onl_fert1762));
        sb.append(OnlFert.config.getData().getWList().get(position).getValveCh() + 1);
        sb.append("\n");
        sb.append("\n");

        if (OnlFert.config.getData().getWList().get(position).getHasPump() == 1) {
            sb.append(getString(R.string.onl_fert178) + "     " + getString(R.string.onl_fert1782));
        } else
            sb.append(getString(R.string.onl_fert179) + "     " + getString(R.string.onl_fert1782));
        sb.append(OnlFert.config.getData().getWList().get(position).getPumpCh() + 1);
        sb.append("\n");
        sb.append("\n");

        if (OnlFert.config.getData().getWList().get(position).getHasLevel() == 1) {
            sb.append(getString(R.string.onl_fert180) + "     " + getString(R.string.onl_fert1802));
        } else
            sb.append(getString(R.string.onl_fert181) + "     " + getString(R.string.onl_fert1802));
        if (OnlFert.config.getData().getWList().get(position).getLevelType() == 1) {
            sb.append(getString(R.string.onl_fert182));
            sb.append("\n");
            sb.append("\n");
            sb.append(getString(R.string.onl_fert183));
            sb.append(OnlFert.config.getData().getWList().get(position).getCurrentCh() + 1);
            sb.append("\n");
            sb.append(getString(R.string.onl_fert184));
            sb.append(OnlFert.config.getData().getWList().get(position).getLowLevelCh());
            sb.append("     " + getString(R.string.onl_fert185));
            sb.append(OnlFert.config.getData().getWList().get(position).getHighLevelCh());
        } else {
            sb.append(getString(R.string.onl_fert186));
            sb.append("\n");
            sb.append("\n");
            sb.append(getString(R.string.onl_fert187));
            sb.append(OnlFert.config.getData().getWList().get(position).getLowLevelCh() + 1);
            sb.append("     " + getString(R.string.onl_fert188));
            sb.append(OnlFert.config.getData().getWList().get(position).getHighLevelCh() + 1);
        }

        tv2.setText(sb.toString());
        mDialog.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
    }
}
