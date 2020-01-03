package com.cdzp.huinongyun.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.KeyboardUtils;
import com.cdzp.huinongyun.BaseActivity;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.bean.BaiDuLocLat;
import com.cdzp.huinongyun.bean.CityResult;
import com.cdzp.huinongyun.bean.UniversalResult;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity2 extends BaseActivity {

    private Context context;
    private EditText et_companyName, et_address, et_companyAccounts, et_password, et_rassword;
    private QMUITipDialog mDialog;
    private OptionsPickerView pvOptions;
    private String strCity = "", strCitys = "", MapCoords = "", phone;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_register2);
    }

    @Override
    public void dealLogicBeforeInitView() {
        context = this;
        phone = getIntent().getStringExtra("phone");
        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                WebServiceManage.dispose();
            }
        });
    }

    @Override
    public void initView() {
        TextView tv = findViewById(R.id.tv_title);
        tv.setText(getResources().getString(R.string.top_register_str));
        et_companyName = findViewById(R.id.et_ar2_companyName);
        et_address = findViewById(R.id.et_ar_address);
        et_companyAccounts = findViewById(R.id.et_ar2_companyAccounts);
        et_password = findViewById(R.id.et_ar2_password);
        et_rassword = findViewById(R.id.et_ar2_rassword);

        findViewById(R.id.btn_register2_ok).setOnClickListener(this);
        findViewById(R.id.ll_back).setOnClickListener(this);
        et_address.setOnClickListener(this);
    }

    @Override
    public void dealLogicAfterInitView() {
        getcity();
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.btn_register2_ok:
                if (et_companyName.getText().length() > 0 && et_address.getText().length() > 0 &&
                        et_companyAccounts.getText().length() > 0 && et_password.getText().length() > 0 &&
                        et_rassword.getText().length() > 0) {
                    if (et_password.getText().toString().equals(et_rassword.getText().toString())) {
                        if (phone != null) {
                            requestRegister();
                        } else Toasts.showInfo(context, getString(R.string.request_error7));
                    } else Toasts.showInfo(context, getString(R.string.request_error5));
                } else Toasts.showInfo(context, getString(R.string.request_error6));
                break;
            case R.id.et_ar_address:
                KeyboardUtils.hideSoftInput(this);
                if (pvOptions != null) {
                    pvOptions.show();
                } else getcity();
                break;
            default:

                break;
        }
    }

    private void requestRegister() {
        mDialog.show();
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Phone", phone});
        data.add(new String[]{"EntName", et_companyName.getText().toString()});
        data.add(new String[]{"EntAddress", strCitys});
        data.add(new String[]{"Region", strCity});
        data.add(new String[]{"MapCoords", MapCoords});
        data.add(new String[]{"UserName", et_companyAccounts.getText().toString()});
        data.add(new String[]{"Pwd", et_password.getText().toString()});
        WebServiceManage.requestData("RegisterSubmitInfo", "/StringService/BaseSet.asmx", data, 2, context);
    }

    private void getcity() {
        mDialog.show();
        List<String[]> data = new ArrayList<>();
        WebServiceManage.requestData("AdministrativeList", "/StringService/BaseSet.asmx", data, 1, context);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 1:
                    parseCity(message);
                    break;
                case 2:
                    parseRegister(message);
                    break;
                default:
                    mDialog.dismiss();
                    Toasts.showError(context, getString(R.string.request_error4));
                    break;
            }
        } else {
            mDialog.dismiss();
            Toasts.showError(context, message.getmResult());
        }
    }

    private void parseRegister(WebMessage message) {
        UniversalResult result = new Gson().fromJson(message.getmResult(), UniversalResult.class);
        if (result.isSuccess()) {
            LoginActivity.account = et_companyAccounts.getText().toString();
            LoginActivity.password = et_password.getText().toString();
            startActivity(RegisterActivity3.class);
            finish();
        } else Toasts.showError(context, result.getMsg());
    }

    private void parseCity(WebMessage message) {
        CityResult cr = new Gson().fromJson(message.getmResult(), CityResult.class);
        if (cr.getData().get(0).size() > 0) {
            final List<String> province = new ArrayList<>();
            final List<List<String>> city = new ArrayList<>();
            final List<List<List<String>>> county = new ArrayList<>();
            for (int i = 0; i < cr.getData().get(0).size(); i++) {
                if (cr.getData().get(0).get(i).getLevel() == 1) {
                    List<String> list1 = new ArrayList<>();
                    List<List<String>> list2 = new ArrayList<>();
                    for (int i1 = 0; i1 < cr.getData().get(0).size(); i1++) {
                        if (cr.getData().get(0).get(i1).getParentCode().equals(cr.getData().get(0).get(i).getAdCode())) {
                            list1.add(cr.getData().get(0).get(i1).getAdName());
                            List<String> list3 = new ArrayList<>();
                            for (int i2 = 0; i2 < cr.getData().get(0).size(); i2++) {
                                if (cr.getData().get(0).get(i2).getParentCode().equals(cr.getData().get(0).get(i1).getAdCode())) {
                                    list3.add(cr.getData().get(0).get(i2).getAdName());
                                }
                                if (i2 == cr.getData().get(0).size() - 1 && list3.size() == 0) {
                                    list3.add("");
                                }
                            }
                            list2.add(list3);
                        }
                        if (i1 == cr.getData().get(0).size() - 1 && list1.size() == 0) {
                            list1.add("");
                        }
                    }
                    list2.add(list1);
                    county.add(list2);
                    city.add(list1);
                    province.add(cr.getData().get(0).get(i).getAdName());
                }
            }
            pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3, View v) {
                    //返回的分别是三个级别的选中位置
                    if (county.get(options1).get(option2).get(options3).length() > 0) {
                        if (county.get(options1).get(option2).get(options3).contains("神农架林区")) {
                            strCity = county.get(options1).get(option2).get(options3);
                        } else if (county.get(options1).get(option2).get(options3).contains("区")) {
                            if (city.get(options1).get(option2).contains("市辖区")) {
                                strCity = province.get(options1);
                            } else strCity = city.get(options1).get(option2);
                        } else strCity = county.get(options1).get(option2).get(options3);
                    } else if (city.get(options1).get(option2).length() > 0) {
                        if (city.get(options1).get(option2).contains("区")) {
                            strCity = province.get(options1);
                        } else strCity = city.get(options1).get(option2);
                    } else {
                        strCity = province.get(options1);
                    }
                    strCitys = province.get(options1) + city.get(options1).get(option2) + county.get(options1).get(option2).get(options3);
                    et_address.setText("");
                    if (strCity.contains("江津")) {
                        strCity = "江津区";
                    }
                    requestCoords(strCity);
                }
            }).build();
            pvOptions.setPicker(province, city, county);
            mDialog.dismiss();
        } else {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.request_error_oncity));
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    et_address.setText(strCity);
                    mDialog.dismiss();
                    break;
                case 1:
                    mDialog.dismiss();
                    Toasts.showError(context, getString(R.string.request_error_Coords));
                    break;
                default:
                    mDialog.dismiss();
                    break;
            }
        }
    };

    private void requestCoords(String mCity) {
        mDialog.show();
        String city = "郫县";
        try {
            city = URLEncoder.encode(mCity, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://api.map.baidu.com/geocoder/v2/?ak=2f6aa8e46b8c124f11d6c4f70c0c6ad5&output=json&address=" +
                        city)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                BaiDuLocLat ll = new Gson().fromJson(result, BaiDuLocLat.class);
                if (ll.getStatus() == 0 && ll.getResult() != null && ll.getResult().getLocation() != null) {
                    ll.getResult().getLocation().getLng();
                    double mlongitude = new BigDecimal(ll.getResult().getLocation().getLng()).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                    double mlatitude = new BigDecimal(ll.getResult().getLocation().getLat()).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                    MapCoords = mlongitude + "," + mlatitude;
                    handler.sendEmptyMessage(0);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
