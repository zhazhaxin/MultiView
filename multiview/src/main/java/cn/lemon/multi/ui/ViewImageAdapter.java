package cn.lemon.multi.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.alien95.resthttp.view.HttpImageView;

class ViewImageAdapter extends PagerAdapter {

    private static final String TAG = "ImageAdapter";
    private Map<String,WeakReference<HttpImageView>> mCacheImages;
    private List<String> mImageUrls;
    private Context mContext;

    public ViewImageAdapter(List<String> data, Context context) {
        mImageUrls = data;
        mContext = context;
        mCacheImages = new HashMap<>(5);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        String key = mImageUrls.get(position);
        HttpImageView image;
        if(mCacheImages.containsKey(key)){
            image = mCacheImages.get(key).get();
            if (image == null){
                image = createImage(key);
            }
        } else {
            image = createImage(key);
        }
        container.addView(image);

        return image;
    }

    public HttpImageView createImage(String url){
        HttpImageView image = new HttpImageView(mContext);
        image.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        image.setAdjustViewBounds(true);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        image.setImageUrl(url);
        mCacheImages.put(url, new WeakReference<>(image));
        return image;
    }

    @Override
    public int getCount() {
        return mImageUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}