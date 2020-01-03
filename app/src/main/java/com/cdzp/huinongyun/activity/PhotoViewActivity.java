package com.cdzp.huinongyun.activity;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.fragment.PestMonFragment;
import com.cdzp.huinongyun.fragment.SeedlMonitorFragment;
import com.github.chrisbanes.photoview.PhotoView;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

public class PhotoViewActivity extends AppCompatActivity {

    private List<String[]> photoList;
    private TextView tv1, tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).init();
        setContentView(R.layout.activity_photo_view);
        int[] mPosition = {0, 0};
        try {
            mPosition = getIntent().getIntArrayExtra("Position");
        } catch (Exception e) {

        }
        if (mPosition[0] == 0) {
            photoList = PestMonFragment.photoList;
        } else {
            photoList = SeedlMonitorFragment.photoList;
        }
        tv1 = findViewById(R.id.tv_ipv1);
        BarUtils.addMarginTopEqualStatusBarHeight(tv1);
        tv2 = findViewById(R.id.tv_ipv2);
        ViewPager viewPager = findViewById(R.id.vp_apv);
        viewPager.setAdapter(new PhotoPagerAdapter());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv1.setText((position + 1) + "/" + photoList.size());
                tv2.setText(photoList.get(position)[1]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(mPosition[1]);
        if (mPosition[1] == 0) {
            tv1.setText(1 + "/" + photoList.size());
            tv2.setText(photoList.get(0)[1]);
        }
    }

    class PhotoPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return photoList.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    ActivityCompat.finishAfterTransition(PhotoViewActivity.this);
                }
            });
            Glide.with(container.getContext())
                    .load(photoList.get(position)[0])
                    .apply(new RequestOptions().placeholder(R.drawable.ambient_mr)
                            .error(R.drawable.ambient_mr))
                    .into(photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }
}
