package cn.lemon.multi.ui;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.alien95.resthttp.view.HttpImageView;

class ImageAdapter extends PagerAdapter {

    private static final String TAG = "ImageAdapter";
    private List<HttpImageView> cahceViews;
    private List<String> imageUrls;

    public ImageAdapter(List<String> data) {
        imageUrls = data;
        cahceViews = new ArrayList<>();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        HttpImageView httpImageView;
        if (cahceViews.isEmpty()) {
            initView(container);
            httpImageView = cahceViews.get(0);
        } else {
            httpImageView = cahceViews.get(0);
        }
        cahceViews.remove(0);
        httpImageView.setImageUrl(imageUrls.get(position));
        container.addView(httpImageView);
        return httpImageView;
    }

    public void initView(ViewGroup container) {
        for (int i = 0; i < 4; i++) {
            HttpImageView image = new HttpImageView(container.getContext());
            image.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            image.setAdjustViewBounds(true);
            image.setAdjustViewBounds(true);
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            cahceViews.add(image);
        }
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        cahceViews.add((HttpImageView) object);   //添加进缓存
        container.removeView((View) object);
    }

}