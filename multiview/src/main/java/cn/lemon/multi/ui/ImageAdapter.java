package cn.lemon.multi.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.alien95.resthttp.view.HttpImageView;

class ImageAdapter extends PagerAdapter {

    private static final String TAG = "ImageAdapter";
    private List<WeakReference<HttpImageView>> cahceViews;
    private List<String> imageUrls;
    private Context mContext;

    public ImageAdapter(List<String> data, Context context) {
        imageUrls = data;
        cahceViews = new ArrayList<>();
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        HttpImageView httpImageView;
        if (cahceViews.isEmpty()) {
            initView(4);
        }
        httpImageView = cahceViews.get(0).get();

        if (httpImageView == null) {  //由于用了弱引用，所以当垃圾回收器进行回收的时候就回收所有内存
            cahceViews.clear();   //需要先清理，GC后对象内存被回收
            initView(2);
            httpImageView = cahceViews.get(0).get();
            Log.i(TAG, "----发生了GC----  init view size : " + cahceViews.size());
        }

        ViewGroup parent = (ViewGroup) httpImageView.getParent();
        if (parent != null) {   //这里的parent是ViewPager
            Log.i(TAG, "parent : " + parent.toString());
        } else {
            container.addView(httpImageView);
        }
        httpImageView.setImageUrl(imageUrls.get(position));

        cahceViews.remove(0);
        Log.i(TAG, "instantiateItem cache size : " + cahceViews.size());

        return httpImageView;
    }

    public void initView(int num) {
        for (int i = 0; i < num; i++) {
            HttpImageView image = new HttpImageView(mContext);
            image.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            image.setAdjustViewBounds(true);
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            cahceViews.add(new WeakReference<>(image));
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
        cahceViews.add(new WeakReference<>((HttpImageView) object));   //添加进缓存
        container.removeView((View) object);
        Log.i(TAG, "destroyItem cache size : " + cahceViews.size());
    }

}