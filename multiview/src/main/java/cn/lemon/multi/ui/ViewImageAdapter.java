package cn.lemon.multi.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.alien95.resthttp.view.HttpImageView;

class ViewImageAdapter extends PagerAdapter {

    private static final String TAG = "ImageAdapter";
    private LinkedList<HttpImageView> mCacheImageViews = new LinkedList<>();
    private List<String> mImageUrls;
    private Context mContext;

    public ViewImageAdapter(List<String> data, Context context) {
        mImageUrls = data;
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        String url = "";
        if (position >= 0 && position < mImageUrls.size()) {
            url = mImageUrls.get(position);
        }
        HttpImageView image;
        if (mCacheImageViews.isEmpty()){
            image = createImage();
        } else {
            image = mCacheImageViews.getFirst();
            mCacheImageViews.removeFirst();
        }
        if (image != null) {
            image.setImageUrl(url);
            container.addView(image);
        }
        return image;
    }

    private HttpImageView createImage(){
        HttpImageView image = new HttpImageView(mContext);
        image.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        image.setAdjustViewBounds(true);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
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
        if (object instanceof HttpImageView) {
            container.removeView((View) object);
            mCacheImageViews.add((HttpImageView) object);
        }
    }

}