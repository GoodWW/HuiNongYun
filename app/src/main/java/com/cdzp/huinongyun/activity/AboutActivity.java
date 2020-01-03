package com.cdzp.huinongyun.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.cdzp.huinongyun.BaseActivity;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.util.Toasts;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.blankj.utilcode.constant.PermissionConstants.STORAGE;

public class AboutActivity extends BaseActivity {

    private Context context;
    private ImageView iv;
    private TextView tv, tv_qr;
    private QMUITipDialog mDialog;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_about);
    }

    @Override
    public void dealLogicBeforeInitView() {
        context = this;
        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
    }

    @Override
    public void initView() {
        TextView title = findViewById(R.id.tv_title);
        title.setText(getString(R.string.personal_str35));
        iv = findViewById(R.id.iv_about);
        tv = findViewById(R.id.tv_about);
        tv_qr = findViewById(R.id.tv_qr);
    }

    @Override
    public void dealLogicAfterInitView() {
        Bitmap bitmap = createQRCodeBitmap("http://101.200.193.60:8083/FGO/Download/huinongyun.apk",
//        Bitmap bitmap = createQRCodeBitmap("http://101.200.193.60:7120/Content/Download/IntelArgi.apk",//三农股份
//        Bitmap bitmap = createQRCodeBitmap("http://101.200.193.60:7110/Content/Download/IntelArgi.apk",//昆明苍松
//        Bitmap bitmap = createQRCodeBitmap("http://39.97.228.247:8081/Content/Download/IntelArgi.apk",//中农富兴
                700, 700, "UTF-8", "H", "1", Color.BLACK, Color.WHITE);
//        Bitmap bitmap = createQRCodeBitmap("http://101.200.193.60:7119/Content/Download/IntelArgi.apk",//里田农业
//        Bitmap bitmap = createQRCodeBitmap("http://39.106.173.112:8088/Content/Download/IntelArgi.apk",
//                700, 700, "UTF-8", "H", "1", Color.BLACK, Color.WHITE);
        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
        }
        tv.setText(getString(R.string.personal_str34) + AppUtils.getAppVersionName());
        tv_qr.setText(getString(R.string.personal_str27));

        findViewById(R.id.ll_back).setOnClickListener(this);
        findViewById(R.id.ll_about).setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_about:
//                checkUpdate();
                break;
            default:
                break;
        }
    }

    /**
     * 创建二维码位图 (支持自定义配置和自定义样式)
     *
     * @param content          字符串内容
     * @param width            位图宽度,要求>=0(单位:px)
     * @param height           位图高度,要求>=0(单位:px)
     * @param character_set    字符集/字符转码格式 (支持格式:{@link  })。传null时,zxing源码默认使用 "ISO-8859-1"
     * @param error_correction 容错级别 (支持级别:{@link  })。传null时,zxing源码默认使用 "L"
     * @param margin           空白边距 (可修改,要求:整型且>=0), 传null时,zxing源码默认使用"4"。
     * @param color_black      黑色色块的自定义颜色值
     * @param color_white      白色色块的自定义颜色值
     * @return
     */
    private Bitmap createQRCodeBitmap(String content, int width, int height, String character_set, String error_correction, String margin,
                                      int color_black, int color_white) {
        /** 1.参数合法性判断 */
        if (TextUtils.isEmpty(content)) { // 字符串内容判空
            return null;
        }
        if (width < 0 || height < 0) { // 宽和高都需要>=0
            return null;
        }
        try {
            /** 2.设置二维码相关配置,生成BitMatrix(位矩阵)对象 */
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            if (!TextUtils.isEmpty(character_set)) {
                hints.put(EncodeHintType.CHARACTER_SET, character_set); // 字符转码格式设置
            }
            if (!TextUtils.isEmpty(error_correction)) {
                hints.put(EncodeHintType.ERROR_CORRECTION, error_correction); // 容错级别设置
            }
            if (!TextUtils.isEmpty(margin)) {
                hints.put(EncodeHintType.MARGIN, margin); // 空白边距设置
            }
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值 */
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = color_black; // 黑色色块像素设置
                    } else {
                        pixels[y * width + x] = color_white; // 白色色块像素设置
                    }
                }
            }
            /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,之后返回Bitmap对象 */
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检测更新
     */
    private void checkUpdate() {
        mDialog.show();
        Observable.create(new ObservableOnSubscribe<String[]>() {
            @Override
            public void subscribe(final ObservableEmitter<String[]> emitter) throws Exception {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        InputStream is = null;
                        try {
                            //E:\WebRoot\IntelV3\FGO\Download
                            URL url = new URL("http://101.200.193.60:8083/FGO/Download/hny.json");
//                            URL url = new URL("http://39.97.228.247:8081/Content/Download/hny.json");//中农富兴
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setConnectTimeout(10000);
                            connection.setReadTimeout(5000);
                            connection.setRequestMethod("GET");
                            int responseCode = connection.getResponseCode();
                            if (responseCode == 200) {
                                is = connection.getInputStream();
                                String json = ConvertUtils.inputStream2String(is, "UTF-8");
                                JSONObject jsonObject = new JSONObject(json);
                                int newVersionCode = jsonObject.getInt("versionCode");
                                String fileSize = jsonObject.getString("fileSize");
                                if (AppUtils.getAppVersionCode() < newVersionCode) {
                                    emitter.onNext(new String[]{"0", fileSize});
                                } else emitter.onNext(new String[]{"3", ""});
                            } else emitter.onNext(new String[]{"2", ""});
                        } catch (Exception e) {
                            emitter.onNext(new String[]{"1", ""});
                        } finally {
                            try {
                                if (is != null) is.close();
                            } catch (IOException e) {

                            }
                        }
                    }
                }).start();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String[] strs) {
                        mDialog.dismiss();
                        switch (strs[0]) {
                            case "0":
                                new QMUIDialog.MessageDialogBuilder(context)
                                        .setTitle(getString(R.string.fou_sit_mon_str7))
                                        .setMessage(getString(R.string.personal_str30) + strs[1] + "MB)")
                                        .addAction(getString(R.string.envir_str1), new QMUIDialogAction.ActionListener() {
                                            @Override
                                            public void onClick(QMUIDialog dialog, int index) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .addAction(getString(R.string.personal_str6), new QMUIDialogAction.ActionListener() {
                                            @Override
                                            public void onClick(QMUIDialog dialog, int index) {
                                                dialog.dismiss();
                                                //请求读写权限
                                                PermissionUtils.permission(PermissionConstants.getPermissions(STORAGE))
                                                        .callback(new PermissionUtils.SimpleCallback() {
                                                            @Override
                                                            public void onGranted() {
                                                                downFile();
                                                            }

                                                            @Override
                                                            public void onDenied() {

                                                            }
                                                        })
                                                        .rationale(new PermissionUtils.OnRationaleListener() {
                                                            @Override
                                                            public void rationale(ShouldRequest shouldRequest) {
                                                                shouldRequest.again(true);
                                                            }
                                                        })
                                                        .request();
                                            }
                                        })
                                        .create().show();
                                break;
                            case "1":
                                Toasts.showInfo(context, getString(R.string.request_error2));
                                break;
                            case "2":
                                Toasts.showInfo(context, getString(R.string.personal_str29));
                                break;
                            case "3":
                                Toasts.showInfo(context, getString(R.string.personal_str28));
                                break;
                            default:
                                Toasts.showInfo(context, getString(R.string.request_error2));
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        mDialog.dismiss();
                    }
                });
    }

    /**
     * 下载apk
     */
    private void downFile() {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle(getString(R.string.personal_str31));
        dialog.setMax(100);
        dialog.setProgress(0);
        dialog.setCancelable(false);
        dialog.show();

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(final ObservableEmitter<Integer> emitter) throws Exception {
                String url = "http://101.200.193.60:8083/FGO/Download/huinongyun.apk";
                Request request = new Request.Builder().url(url).build();
                new OkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onNext(-2);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code() != 200) {
                            emitter.onNext(-2);
                            return;
                        }
                        InputStream is = null;
                        byte[] buf = new byte[2048];
                        int len = 0;
                        FileOutputStream fos = null;
                        try {
                            //储存下载文件的目录
                            String savePath = Environment.getExternalStorageDirectory() + "/huinongyun/";
                            File dirFile = new File(savePath);
                            if (!dirFile.exists()) {
                                dirFile.mkdirs();
                            }
                            is = response.body().byteStream();
                            long total = response.body().contentLength();
                            File file = new File(savePath, "慧农云.apk");
                            fos = new FileOutputStream(file);
                            long sum = 0;
                            while ((len = is.read(buf)) != -1) {
                                fos.write(buf, 0, len);
                                sum += len;
                                int progress = (int) (sum * 1.0f / total * 100);
                                //下载中
                                emitter.onNext(progress);
                            }
                            fos.flush();
                            //下载完成
                            emitter.onNext(-1);
                        } catch (Exception e) {
                            emitter.onNext(-2);
                        } finally {
                            try {
                                if (is != null) is.close();
                                if (fos != null) fos.close();
                            } catch (IOException e) {

                            }
                        }
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        switch (integer) {
                            case -1:
                                dialog.dismiss();
                                AppUtils.installApp(Environment.getExternalStorageDirectory()
                                        + "/huinongyun/慧农云.apk");
                                break;
                            case -2:
                                dialog.dismiss();
                                Toasts.showInfo(context, getString(R.string.personal_str32));
                                break;
                            default:
                                if (integer >= 0 && integer <= 100) {
                                    dialog.setProgress(integer);
                                }
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });
    }
}
