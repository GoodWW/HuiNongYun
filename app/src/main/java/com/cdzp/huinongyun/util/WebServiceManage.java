package com.cdzp.huinongyun.util;

import android.content.Context;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.bean.GreenHouseSelResult;
import com.cdzp.huinongyun.message.WebMessage;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.cdzp.huinongyun.util.Data.locList;
import static com.cdzp.huinongyun.util.Data.locList1;
import static com.cdzp.huinongyun.util.Data.cGhList;
import static com.cdzp.huinongyun.util.Data.pGhList;

/**
 * WebService接口请求管理类
 */

public class WebServiceManage {

    private static Disposable disposable;

    /**
     * 接口请求
     *
     * @param methodName
     * @param url
     * @param data       提交的数据
     * @param label      标记当前请求的哪个接口
     * @param mContext
     */
    public static void requestData(final String methodName, final String url,
                                   final List<String[]> data, final int label, final Context mContext) {
        Observable.create(new ObservableOnSubscribe<WebMessage>() {
            @Override
            public void subscribe(final ObservableEmitter<WebMessage> emitter) throws Exception {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        emitter.onNext(getData(methodName, url, data, label, mContext));
                    }
                }).start();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<WebMessage>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(WebMessage webMessage) {
                        if (label == 666) {//获取终端，大多数页面需要用到，在这里统一处理
                            if (webMessage.isOk()) {
                                parseGHSelResult(webMessage.getmResult(), mContext);
                            } else {
                                EventBus.getDefault().post(webMessage);
                            }
                        } else
                            EventBus.getDefault().post(webMessage);
                    }

                    @Override
                    public void onError(Throwable e) {
                        EventBus.getDefault().post(new WebMessage(false, label, mContext.getString(R.string.request_error3)));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private static void parseGHSelResult(String s, Context mContext) {

        boolean isEnglish;
        if (mContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
                .getString("Language", "").equals("English")) {
            isEnglish = true;
        } else {
            isEnglish = false;
        }

        boolean istwo;//是否是日光，电磁阀
        if (Data.GhType != null) {
            switch (Data.GhType) {
                case "8":
                case "9":
                    istwo = true;
                    break;
                default:
                    istwo = false;
                    break;
            }
        } else {
            EventBus.getDefault().post(new WebMessage(false, 666, mContext.getString(R.string.request_error7)));
            return;
        }

        GreenHouseSelResult result = new Gson().fromJson(s, GreenHouseSelResult.class);
        if (result.isSuccess()) {
            if (result.getData().size() > 0 && result.getData().get(0) != null &&
                    result.getData().get(0).getPGhList().size() > 0) {
                pGhList = new ArrayList<>();
                cGhList = new ArrayList<>();
                locList = new ArrayList<>();
                locList1 = new ArrayList<>();
//                if (Data.GhType != null ) {//气象站只有室外，6是气象站  && !Data.GhType.equals("6")
                for (int i = 0; i < result.getData().get(0).getPGhList().size(); i++) {
                    String Status = result.getData().get(0).getPGhList().get(i).getStatus();
                    String GreenhouseName = result.getData().get(0).getPGhList().get(i).getGreenhouseName();
                    if (!Status.equals("1")) {
                        GreenhouseName += mContext.getString(R.string.parsetstr10);
                    }
                    String[] str = new String[]{
                            result.getData().get(0).getPGhList().get(i).getGreenhouseID(),
                            GreenhouseName,
                            result.getData().get(0).getPGhList().get(i).getDisplayNo(),
                            result.getData().get(0).getPGhList().get(i).getConnectType(),
                            result.getData().get(0).getPGhList().get(i).getDisplayIP(),
                            result.getData().get(0).getPGhList().get(i).getDisplayPort(),
                            Status
                    };
                    if (istwo) {
                        List<String[]> list = new ArrayList<>();
                        List<List<String[]>> list2 = new ArrayList<>();

                        //取日光温室的终端
                        for (int i1 = 0; i1 < result.getData().get(0).getCGhList().size(); i1++) {
                            if (str[0].equals(result.getData().get(0).getCGhList().get(i1).getGwID())) {
                                String Status1 = result.getData().get(0).getCGhList().get(i1).getStatus();
                                String GreenhouseName1 = result.getData().get(0).getCGhList().get(i1).getGreenhouseName();
                                if (!Status1.equals("1")) {
                                    GreenhouseName1 += mContext.getString(R.string.parsetstr10);
                                }
                                String[] str1 = new String[]{
                                        result.getData().get(0).getCGhList().get(i1).getGreenhouseID(),
                                        GreenhouseName1,
                                        result.getData().get(0).getCGhList().get(i1).getDisplayNo(),
                                        result.getData().get(0).getCGhList().get(i1).getConnectType(),
                                        result.getData().get(0).getCGhList().get(i1).getDisplayIP(),
                                        result.getData().get(0).getCGhList().get(i1).getDisplayPort(),
                                        Status1
                                };
                                list.add(str1);
                                List<String[]> list3 = new ArrayList<>();
                                if (result.getData().get(0).getCGhList().get(i1).getMaxLocation().length() > 0) {
                                    int maxLocation = Integer.parseInt(result.getData().get(0).getCGhList().get(i1).getMaxLocation());
                                    if (maxLocation <= result.getData().get(0).getLocList().size()) {
                                        for (int i2 = 0; i2 < maxLocation; i2++) {
                                            String value = result.getData().get(0).getLocList().get(i2).getBaseValue();
                                            String name;
                                            if (isEnglish) {
                                                name = value + mContext.getString(R.string.dev_str1);
                                            } else {
                                                name = result.getData().get(0).getLocList().get(i2).getBaseName();
                                            }
                                            String[] str3 = new String[]{value, name};
                                            list3.add(str3);
                                        }
                                    }
                                }
                                list2.add(list3);
                            }
                        }


                        //取室外终端
                        for (int i1 = 0; i1 < result.getData().get(0).getOutGhList().size(); i1++) {
                            String Status1 = result.getData().get(0).getOutGhList().get(i1).getStatus();
                            String GreenhouseName1 = mContext.getString(R.string.dev_str2);//result.getData().get(0).getOutGhList().get(i1).getGreenhouseName();
                            if (!Status1.equals("1")) {
                                GreenhouseName1 += mContext.getString(R.string.parsetstr10);
                            }
                            String[] str1 = new String[]{
                                    result.getData().get(0).getOutGhList().get(i1).getGreenhouseID(),
                                    GreenhouseName1,
                                    result.getData().get(0).getOutGhList().get(i1).getDisplayNo(),
                                    result.getData().get(0).getOutGhList().get(i1).getConnectType(),
                                    result.getData().get(0).getOutGhList().get(i1).getDisplayIP(),
                                    result.getData().get(0).getOutGhList().get(i1).getDisplayPort(),
                                    Status1
                            };
                            list.add(str1);

                            List<String[]> list3 = new ArrayList<>();
                            if (result.getData().get(0).getOutGhList().get(i1).getMaxLocation().length() > 0) {
                                int maxLocation = Integer.parseInt(result.getData().get(0).getOutGhList().get(i1).getMaxLocation());
                                if (maxLocation < result.getData().get(0).getLocList().size()) {
                                    //室外终端maxLocation为下标，所以循环条件为<=
                                    for (int i2 = 0; i2 <= maxLocation; i2++) {
                                        if (i2 == 0) {
                                            list3.add(new String[]{"0", mContext.getString(R.string.dev_str2)});
                                        } else {
                                            String value = result.getData().get(0).getLocList().get(i2 - 1).getBaseValue();
                                            String name;
                                            if (isEnglish) {
                                                name = value + mContext.getString(R.string.dev_str1);
                                            } else {
                                                name = result.getData().get(0).getLocList().get(i2 - 1).getBaseName();
                                            }
                                            String[] str3 = new String[]{value, name};
                                            list3.add(str3);
                                        }
                                    }
                                }
//                                list2.add(list3);
                            }
                            list2.add(list3);
                        }


                        cGhList.add(list);
                        locList1.add(list2);
                    } else {
                        List<String[]> list = new ArrayList<>();
                        if (result.getData().get(0).getPGhList().get(i).getMaxLocation().length() == 0) {
                            locList.add(list);
                        } else {
                            int maxLocation = Integer.parseInt(result.getData().get(0).getPGhList().get(i).getMaxLocation());
                            for (int i1 = 0; i1 < maxLocation; i1++) {
                                String value = result.getData().get(0).getLocList().get(i1).getBaseValue();
                                String name;
                                if (isEnglish) {
                                    name = value + mContext.getString(R.string.dev_str1);
                                } else {
                                    name = result.getData().get(0).getLocList().get(i1).getBaseName();
                                }
                                String[] str1 = new String[]{value, name};
                                list.add(str1);
                            }
                            locList.add(list);
                        }
                    }
                    pGhList.add(str);
                }
//                }

                //取非日光温室室外
                if (!istwo) {
                    for (int i1 = 0; i1 < result.getData().get(0).getOutGhList().size(); i1++) {
                        String Status1 = result.getData().get(0).getOutGhList().get(i1).getStatus();
                        String GreenhouseName1 = mContext.getString(R.string.dev_str2);//result.getData().get(0).getOutGhList().get(i1).getGreenhouseName();
                        if (!Status1.equals("1")) {
                            GreenhouseName1 += mContext.getString(R.string.parsetstr10);
                        }
                        String[] str1 = new String[]{
                                result.getData().get(0).getOutGhList().get(i1).getGreenhouseID(),
                                GreenhouseName1,
                                result.getData().get(0).getOutGhList().get(i1).getDisplayNo(),
                                result.getData().get(0).getOutGhList().get(i1).getConnectType(),
                                result.getData().get(0).getOutGhList().get(i1).getDisplayIP(),
                                result.getData().get(0).getOutGhList().get(i1).getDisplayPort(),
                                Status1
                        };

                        List<String[]> list = new ArrayList<>();
                        if (result.getData().get(0).getOutGhList().get(i1).getMaxLocation().length() == 0) {
                            locList.add(list);
                        } else {
                            int maxLocation = Integer.parseInt(result.getData().get(0).getOutGhList().get(i1).getMaxLocation());
                            if (maxLocation < result.getData().get(0).getLocList().size()) {
                                for (int i2 = 0; i2 <= maxLocation; i2++) {
                                    if (i2 == 0) {
                                        list.add(new String[]{"0", mContext.getString(R.string.dev_str2)});
                                    } else {
                                        String value = result.getData().get(0).getLocList().get(i2 - 1).getBaseValue();
                                        String name;
                                        if (isEnglish) {
                                            name = value + mContext.getString(R.string.dev_str1);
                                        } else {
                                            name = result.getData().get(0).getLocList().get(i2 - 1).getBaseName();
                                        }
                                        String[] str2 = new String[]{value, name};
                                        list.add(str2);
                                    }
                                }
                            }
                            locList.add(list);
                        }
                        pGhList.add(str1);
                    }
                }

                EventBus.getDefault().post(new WebMessage(true, 666, ""));
            } else {
                EventBus.getDefault().post(new WebMessage(false, 666, mContext.getString(R.string.request_error8)));
            }
        } else {
            EventBus.getDefault().post(new WebMessage(false, 666, result.getMsg()));
        }
    }

    private static WebMessage getData(String methodName, String mUrl, List<String[]> data, int label, Context mContext) {
        boolean isOk = false;
        String mResult;
        try {
            String nameSpace = "http://tempuri.org/";
            String url = "http://" + Data.url + mUrl;
            String soapAction = nameSpace + methodName;

            SoapObject soapObject = new SoapObject(nameSpace, methodName);
            for (int i = 0; i < data.size(); i++) {
                soapObject.addProperty(data.get(i)[0], data.get(i)[1]);
            }

            // 生成调用WebService方法调用的soap信息，并且指定Soap版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.bodyOut = soapObject;
            // 是否调用DotNet开发的WebService
            envelope.dotNet = true;
            envelope.setOutputSoapObject(soapObject);

            HttpTransportSE transport = new HttpTransportSE(url);
            transport.debug = true;

            try {
                transport.call(soapAction, envelope);
                mResult = envelope.getResponse().toString();
                isOk = true;
            } catch (IOException e) {
                mResult = mContext.getString(R.string.request_error1);
            } catch (XmlPullParserException e) {
                mResult = mContext.getString(R.string.request_error1);
            }
        } catch (Exception e) {
            mResult = mContext.getString(R.string.request_error3);
        }
        return new WebMessage(isOk, label, mResult);
    }

    /**
     * 特殊接口请求
     *
     * @param methodName
     * @param url
     * @param data       提交的数据
     * @param label      标记当前请求的哪个接口
     * @param mContext
     */
    public static void requestData2(final String methodName, final String url,
                                    final List<String[]> data, final int label, final Context mContext) {
        Observable.create(new ObservableOnSubscribe<WebMessage>() {
            @Override
            public void subscribe(final ObservableEmitter<WebMessage> emitter) throws Exception {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean isOk = false;
                        String mResult;
                        try {
                            String nameSpace = "http://tempuri.org/";
                            String soapAction = nameSpace + methodName;
                            SoapObject soapObject = new SoapObject(nameSpace, methodName);
                            for (int i = 0; i < data.size(); i++) {
                                soapObject.addProperty(data.get(i)[0], data.get(i)[1]);
                            }
                            // 生成调用WebService方法调用的soap信息，并且指定Soap版本
                            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                            envelope.bodyOut = soapObject;
                            // 是否调用DotNet开发的WebService
                            envelope.dotNet = true;
                            envelope.setOutputSoapObject(soapObject);

                            HttpTransportSE transport = new HttpTransportSE(url);
                            transport.debug = true;

                            try {
                                transport.call(soapAction, envelope);
                                mResult = envelope.getResponse().toString();
                                isOk = true;
                            } catch (IOException e) {
                                mResult = mContext.getString(R.string.request_error1);
                            } catch (XmlPullParserException e) {
                                mResult = mContext.getString(R.string.request_error1);
                            }
                        } catch (Exception e) {
                            mResult = mContext.getString(R.string.request_error3);
                        }
                        emitter.onNext(new WebMessage(isOk, label, mResult));
                    }
                }).start();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<WebMessage>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(WebMessage webMessage) {
                        EventBus.getDefault().post(webMessage);
                    }

                    @Override
                    public void onError(Throwable e) {
                        EventBus.getDefault().post(new WebMessage(false, label, mContext.getString(R.string.request_error3)));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static void dispose() {
        if (disposable != null) disposable.dispose();
    }

}
