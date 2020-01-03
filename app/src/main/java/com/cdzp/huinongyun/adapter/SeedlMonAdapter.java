package com.cdzp.huinongyun.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.activity.PhotoViewActivity;
import com.cdzp.huinongyun.fragment.SeedlMonitorFragment;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * 苗情监控适配器
 */
public class SeedlMonAdapter extends Adapter<SeedlMonAdapter.MyViewHolder> {

    private Activity mActivity;

    public SeedlMonAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_pest_mon, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(mActivity)
                .load(SeedlMonitorFragment.photoList.get(position)[0])
                .apply(new RequestOptions().placeholder(R.drawable.ambient_mr)
                        .error(R.drawable.ambient_mr))
                .into(holder.iv);
        holder.tv.setText(SeedlMonitorFragment.photoList.get(position)[1]);
    }

    @Override
    public int getItemCount() {
        return SeedlMonitorFragment.photoList.size();
    }

    class MyViewHolder extends ViewHolder implements View.OnClickListener {

        private ImageView iv;
        private TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            iv = itemView.findViewById(R.id.iv_ipm);
            tv = itemView.findViewById(R.id.tv_ipm);
            iv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mActivity, PhotoViewActivity.class);
            intent.putExtra("Position", new int[]{1, getAdapterPosition()});
            if (Build.VERSION.SDK_INT < 21) {
                mActivity.startActivity(intent);
            } else {
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(mActivity, v, mActivity.getString(R.string.transition));
                mActivity.startActivity(intent, options.toBundle());
            }
        }
    }
}
