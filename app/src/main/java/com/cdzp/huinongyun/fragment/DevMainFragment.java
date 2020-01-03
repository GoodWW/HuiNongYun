package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.adapter.DevMainAdapter;
import com.cdzp.huinongyun.bean.GetGhListResult;
import com.cdzp.huinongyun.bean.UniversalResult;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备维护
 */
public class DevMainFragment extends Fragment {


    private View view;
    private Context context;
    private QMUITipDialog mDialog;
    private boolean isVisible, isInit = false;
    private SmartRefreshLayout mRefreshLayout;
    private SwipeMenuRecyclerView recyclerView;
    private DevMainAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_dev_maint, container, false);
            initView();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
            if (!isInit && view != null) {
                isInit = true;
                mRefreshLayout.autoRefresh();
            }
        } else {
            WebServiceManage.dispose();
            EventBus.getDefault().unregister(this);
            if (mRefreshLayout != null) {
                mRefreshLayout.finishRefresh(0, false);
            }
        }
    }

    private void initView() {
        context = getContext();
        BarUtils.addMarginTopEqualStatusBarHeight(view.findViewById(R.id.tv_top_fdm));
        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();


        mRefreshLayout = view.findViewById(R.id.mRefreshLayout_fdm);
        mRefreshLayout.setEnableOverScrollBounce(false);//是否启用越界回弹
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(context));
        mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        recyclerView.addItemDecoration(new DefaultItemDecoration(context.getResources().getColor(R.color.itemDecoration)));
        recyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        recyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);

        mAdapter = new DevMainAdapter(context);
        recyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                requestData();
            }
        });

        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                WebServiceManage.dispose();
            }
        });

        if (!isInit && isVisible) {
            isInit = true;
            mRefreshLayout.autoRefresh();
        }
    }

    private void requestData() {
        if (Data.userResult != null) {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("GetGhList",
                    "/StringService/DeviceSet.asmx", data, 1, context);
        } else {
            stopLoading(false);
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 1:
                    parseGGLResult(message.getmResult());
                    break;
                case 2:
                    parseClearResult(message.getmResult());
                    break;
                case 3:
                    parseDeleteResult(message.getmResult());
                    break;
                case 4:
                    parseEditResult(message.getmResult());
                    break;
                default:
                    stopLoading(false);
                    break;
            }
        } else {
            stopLoading(false);
            Toasts.showError(context, message.getmResult());
        }
    }

    private void parseEditResult(String s) {
        try {
            UniversalResult result = new Gson().fromJson(s, UniversalResult.class);
            if (result.isSuccess()) {
                mAdapter.getList().get(mPosition)[4] = temStr[2];
                mAdapter.getList().get(mPosition)[1] = temStr[0] + " | " + getString(R.string.devmain_str5) +
                        temStr[1];
                mAdapter.notifyItemChanged(mPosition);
                Toasts.showSuccess(context, getString(R.string.devmain_str7));
            } else Toasts.showInfo(context, getString(R.string.devmain_str8));
        } catch (Exception e) {
            Toasts.showError(context, getString(R.string.devmain_str8));
        }
        mDialog.dismiss();
    }

    private void parseClearResult(String s) {
        UniversalResult result = new Gson().fromJson(s, UniversalResult.class);
        if (result.isSuccess()) {
            stopLoading(false);
            Toasts.showSuccess(context, result.getMsg());
        } else {
            stopLoading(false);
            Toasts.showInfo(context, result.getMsg());
        }
    }

    private void parseDeleteResult(String s) {
        UniversalResult result = new Gson().fromJson(s, UniversalResult.class);
        if (result.isSuccess()) {
            Toasts.showSuccess(context, result.getMsg());
            requestData();
        } else {
            stopLoading(false);
            Toasts.showInfo(context, result.getMsg());
        }
    }

    private void parseGGLResult(String s) {
        GetGhListResult result = new Gson().fromJson(s, GetGhListResult.class);
        if (result.isSuccess()) {
            mAdapter.getList().clear();
            if (result.getData().size() > 0) {
                for (int i = 0; i < result.getData().size(); i++) {
                    String typeName;
                    switch (result.getData().get(i).getGhType() + "") {
                        case "1":
                            typeName = getString(R.string.terminal_str1);
                            break;
                        case "2":
                            typeName = getString(R.string.terminal_str2);
                            break;
                        case "3":
                            typeName = getString(R.string.terminal_str3);
                            break;
                        case "4":
                            typeName = getString(R.string.terminal_str4);
                            break;
                        case "5":
                            typeName = getString(R.string.terminal_str5);
                            break;
                        case "6":
                            typeName = getString(R.string.terminal_str6);
                            break;
                        case "7":
                            typeName = getString(R.string.terminal_str7);
                            break;
                        case "8":
                            typeName = getString(R.string.terminal_str8);
                            break;
                        case "9":
                            typeName = getString(R.string.terminal_str9);
                            break;
                        default:
                            typeName = result.getData().get(i).getTypeName();
                            break;
                    }
                    mAdapter.getList().add(new String[]{
                            "0", result.getData().get(i).getGhType() + "",
                            typeName});
                    for (int i1 = 0; i1 < result.getData().get(i).getData().size(); i1++) {
                        mAdapter.getList().add(new String[]{
                                "1", result.getData().get(i).getData().get(i1).getGreenhouseName() +
                                " | " + getString(R.string.devmain_str5) +
                                result.getData().get(i).getData().get(i1).getDisplayNo(),
                                getString(R.string.devmain_str6) +
                                        result.getData().get(i).getData().get(i1).getDisplayIP() + ":" +
                                        result.getData().get(i).getData().get(i1).getDisplayPort(),
                                result.getData().get(i).getData().get(i1).getGreenhouseID(),
                                result.getData().get(i).getData().get(i1).getRemark()});
                    }
                }
                mAdapter.notifyDataSetChanged();
                stopLoading(true);
            } else {
                mAdapter.notifyDataSetChanged();
                stopLoading(true);
                Toasts.showInfo(context, getString(R.string.request_error8));
            }
        } else {
            stopLoading(true);
            Toasts.showInfo(context, result.getMsg());
        }
    }

    private void requestDelete(String ghID) {
        if (Data.userResult != null) {
            mDialog.show();
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"GhID", ghID});
            data.add(new String[]{"Type", "-1"});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("GreenhouseDel",
                    "/StringService/DeviceSet.asmx", data, 3, context);
        } else Toasts.showError(context, getString(R.string.request_error9));
    }

    private void requestClear(String ghID) {
        if (Data.userResult != null) {
            mDialog.show();
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"GhID", ghID});
            data.add(new String[]{"Type", "2"});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("GreenhouseClearData",
                    "/StringService/DeviceSet.asmx", data, 2, context);
        } else Toasts.showError(context, getString(R.string.request_error9));
    }

    private MyAutoDialogBuilder builder;
    private int mPosition;
    private String[] temStr = new String[3];

    private void requestEdit(final int position) {
        String[] greenhouse = mAdapter.getList().get(position)[1].split(" \\| " + getString(R.string.devmain_str5));
        String greenhouseID = "", greenhouseName = "";
        if (greenhouse.length == 2) {
            greenhouseID = greenhouse[0];
            greenhouseName = greenhouse[1];
        }
        builder = (MyAutoDialogBuilder) new MyAutoDialogBuilder(context, new String[]{
                greenhouseID, greenhouseName, mAdapter.getList().get(position)[4]
        })
                .addAction(getString(R.string.devmain_str4), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        if (builder.getEt1().getText().length() > 0 &&
                                builder.getEt2().getText().length() > 0) {
                            dialog.dismiss();
                            KeyboardUtils.hideSoftInput(getActivity());
                            if (Data.userResult != null) {
                                mDialog.show();
                                List<String[]> data = new ArrayList<>();
                                data.add(new String[]{"GhID", mAdapter.getList().get(position)[3]});
                                temStr[0] = builder.getEt1().getText().toString();
                                temStr[1] = builder.getEt2().getText().toString();
                                temStr[2] = builder.getEt3().getText().toString();
                                data.add(new String[]{"GhName", temStr[0]});
                                data.add(new String[]{"GhNo", temStr[1]});
                                data.add(new String[]{"Remark", temStr[2]});
                                data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
                                data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
                                mPosition = position;
                                WebServiceManage.requestData("GreenhouseEdit",
                                        "/StringService/DeviceSet.asmx", data, 4, context);
                            } else Toasts.showError(context, getString(R.string.request_error9));
                        } else Toasts.showInfo(context, getString(R.string.request_error6));
                    }
                })
                .addAction(getString(R.string.envir_str1), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        KeyboardUtils.hideSoftInput(getActivity());
                    }
                });
        builder.create().show();
    }

    private void stopLoading(boolean isSucc) {
        mRefreshLayout.finishRefresh(0, isSucc);
        mDialog.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isVisible && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 菜单创建器。
     */
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            if (viewType == 1) {
                int width = getResources().getDimensionPixelSize(R.dimen.dp_66);

                // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
                // 2. 指定具体的高，比如80;
                // 3. WRAP_CONTENT，自身高度，不推荐;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;

                SwipeMenuItem editItem = new SwipeMenuItem(context)
                        .setBackground(R.color.title)
                        .setImage(R.drawable.fixing_edit)
                        .setText(getString(R.string.devmain_str3))
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(editItem); // 添加菜单到右侧。

                SwipeMenuItem closeItem = new SwipeMenuItem(context)
                        .setBackground(R.color.ycolor)
                        .setImage(R.drawable.fixing_qk)
                        .setText(getString(R.string.devmain_str1))
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(closeItem); // 添加菜单到右侧。

                SwipeMenuItem addItem = new SwipeMenuItem(context)
                        .setBackground(R.color.homewarn)
                        .setImage(R.drawable.fixing_del)
                        .setText(getString(R.string.devmain_str2))
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(addItem); // 添加菜单到右侧。
            }
        }
    };

    /**
     * 侧滑菜单监听器
     */
    SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
            menuBridge.closeMenu();

//            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            final int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
            switch (menuPosition) {
                case 0:
                    requestEdit(adapterPosition);
                    break;
                case 1:
                    new QMUIDialog.MessageDialogBuilder(context)
                            .setTitle(getString(R.string.fou_sit_mon_str7))
                            .setMessage(getString(R.string.devmain_str12))
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
                                    requestClear(mAdapter.getList().get(adapterPosition)[3]);
                                }
                            })
                            .create().show();
                    break;
                case 2:
                    new QMUIDialog.MessageDialogBuilder(context)
                            .setTitle(getString(R.string.fou_sit_mon_str7))
                            .setMessage(getString(R.string.devmain_str13))
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
                                    requestDelete(mAdapter.getList().get(adapterPosition)[3]);
                                }
                            })
                            .create().show();
                    break;
                default:

                    break;
            }
        }
    };

    class MyAutoDialogBuilder extends QMUIDialogBuilder {

        private EditText et1, et2, et3;
        private String[] str;

        public MyAutoDialogBuilder(Context context, String[] str) {
            super(context);
            this.str = str;
        }

        public EditText getEt1() {
            return et1;
        }

        public EditText getEt2() {
            return et2;
        }

        public EditText getEt3() {
            return et3;
        }

        @Override
        protected void onCreateContent(QMUIDialog dialog, ViewGroup parent, Context context) {
            View mView = LayoutInflater.from(context).inflate(R.layout.dev_main_fragment_dialog, parent, false);
            et1 = mView.findViewById(R.id.et_dmfd1);
            et2 = mView.findViewById(R.id.et_dmfd2);
            et3 = mView.findViewById(R.id.et_dmfd3);
            et1.setText(str[0]);
            et2.setText(str[1]);
            et3.setText(str[2]);
            parent.addView(mView);
        }
    }
}
