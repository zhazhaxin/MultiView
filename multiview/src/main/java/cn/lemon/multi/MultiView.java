package cn.lemon.multi;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.alien95.resthttp.view.HttpImageView;
import cn.lemon.multi.adapter.MultiAdapter;
import cn.lemon.multi.ui.ViewImageActivity;
import cn.lemon.multi.util.Util;


/**
 * Created by linlongxin on 2015/12/28.
 */
public class MultiView extends ViewGroup {

    private static final String TAG = "MultiView";
    private int TEXT_NUM_COLOR = 0xffffffff;
    private int TEXT_NUM_BACKGROUND_COLOR = 0x33000000;
    private static boolean isImageURL = true;
    private static boolean isBitmap = false;
    private static boolean isUri = false;

    private int childWidth, childHeight;
    private int divideSpace; //默认2dp
    private int placeholder;
    private MultiAdapter mAdapter;
    private int childCount;

    //不通过Adapter设置图片
    private List<String> mData; //针对
    private List<Bitmap> mBitmaps;
    private List<Uri> mUris;

    private TextView mTextNum;

    public MultiView(Context context) {
        this(context, null);
    }

    public MultiView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Util.init(context);

        mData = new ArrayList<>();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiView);
        divideSpace = (int) typedArray.getDimension(R.styleable.MultiView_divideSpace, Util.dip2px(2));
        placeholder = typedArray.getResourceId(R.styleable.MultiView_placeholder, -1);
        typedArray.recycle();
    }

    //测量自己的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, "onMeasure");

        childCount = getChildCount();

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width;

        if (childCount == 0) {
            width = 0;
            height = 0;
        } else if (childCount == 1) {
            childWidth = width - divideSpace * 2;
        } else if (childCount == 2) {
            childWidth = (width - divideSpace * 3) / 2;
            height = childWidth + divideSpace * 2;
        } else if (childCount == 4) {
            childWidth = (width - divideSpace * 3) / 2;
            height = childWidth * 2 + divideSpace * 3;
        } else {
            /**
             * 九宫格模式
             */
            childWidth = (width - divideSpace * 4) / 3;
            if (childCount < 9) {
                if (childCount % 3 == 0) {
                    height = childWidth * childCount / 3 + divideSpace * (childCount / 3 + 1);
                } else {
                    height = childWidth * (childCount / 3 + 1) + divideSpace * (childCount / 3 + 2);
                }
            } else {
                height = width;
            }
        }

        childHeight = childWidth;

        /**
         * 全所有的child都用AT_MOST模式，而child的width和height仅仅只是建议
         */
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
        measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.i(TAG, "onLayout");

        if (childCount == 1) {
            getChildAt(0).layout(divideSpace, divideSpace, childWidth + divideSpace, childWidth + divideSpace);
        } else if (childCount == 2) {
            getChildAt(0).layout(divideSpace, divideSpace, (childWidth + divideSpace), (childWidth + divideSpace));
            getChildAt(1).layout((childWidth + divideSpace * 2), divideSpace, childWidth * 2 + divideSpace * 2, (childWidth + divideSpace));
        } else if (childCount == 4) {
            for (int i = 0; i < 4; i++) {
                getChildAt(i).layout(divideSpace * (i % 2 + 1) + childWidth * (i % 2), i / 2 * childWidth + divideSpace * (i / 2 + 1),
                        divideSpace * (i % 2 + 1) + childWidth * (i % 2 + 1), divideSpace * (i / 2 + 1) + (i / 2 + 1) * childWidth);
            }
        } else {
            if (childCount <= 9) {
                for (int i = 0; i < childCount; i++) {
                    getChildAt(i).layout(divideSpace * (i % 3 + 1) + childWidth * (i % 3), i / 3 * childWidth + divideSpace * (i / 3 + 1),
                            divideSpace * (i % 3 + 1) + childWidth * (i % 3 + 1), divideSpace * (i / 3 + 1) + (i / 3 + 1) * childWidth);
                }
            } else {
                for (int i = 0; i < 9; i++) {
                    getChildAt(i).layout(divideSpace * (i % 3 + 1) + childWidth * (i % 3), i / 3 * childWidth + divideSpace * (i / 3 + 1),
                            divideSpace * (i % 3 + 1) + childWidth * (i % 3 + 1), divideSpace * (i / 3 + 1) + (i / 3 + 1) * childWidth);
                }
                getChildAt(9).layout(divideSpace * 3 + childWidth * 2, 2 * childWidth + divideSpace * 3,
                        divideSpace * 3 + childWidth * 3, divideSpace * 3 + 3 * childWidth);
            }
        }
    }

    /**
     * 设置adapter，同时设置注册MessageNotify
     */
    public void setAdapter(MultiAdapter adapter) {
        isImageURL = false;
        this.mAdapter = adapter;
        addViews();
        adapter.attachView(this);
    }

    /**
     * 添加adapter中所有的view
     */
    public void addViews() {
        if (!isImageURL) {
            if (mAdapter.getCount() > 9) {
                for (int i = 0; i < 9; i++) {
                    configView(i);
                }
                addOverNumView(9);
            } else {
                for (int i = 0; i < mAdapter.getCount(); i++) {
                    configView(i);
                }
            }
        } else {
            setImages(mData);
        }
    }

    public void configView(int position) {
        View item = mAdapter.getView(this, position);
        if (item.getParent() != null && item.getParent() instanceof ViewGroup) {
            ((ViewGroup)item.getParent()).removeView(item);
        }
        if (item.getParent() == null) {
            addView(item);
            mAdapter.setData(mAdapter.getItem(position));
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.setOnItemClick();
                }
            });
        }
    }

    public void addView(int position) {
        if (!isImageURL) {
            if (position > 8) {
                addOverNumView(9);
                return;
            }
            addView(mAdapter.getView(this, position));
            mAdapter.setData(mAdapter.getItem(position));
        }
    }

    /**
     * 网络图片地址
     */
    public void setImages(List<String> data) {
        clear();
        isImageURL = true;
        mData = data;
        if (data.size() > 9) {
            for (int i = 0; i < 9; i++) {  //添加9个item
                addView(getImageView(data.get(i), i), i);
            }
            addOverNumView(9);  //添加第10个item，覆盖第9个item
        } else {
            for (int i = 0; i < data.size(); i++) {
                addView(getImageView(data.get(i), i), i);
            }
        }
    }

    public void addImage(String url) {
        mData.add(url);
        if (mData.size() > 9 && mTextNum != null) {
            mTextNum.setText("+" + (mData.size() - 9));  //添加第10个item，覆盖第9个item
        } else {
            int index = mData.size() - 1;
            addView(getImageView(mData.get(index), index), index);
        }
    }

    /**
     * Bitmap
     */
    public void setBitmaps(List<Bitmap> bitmaps) {
        isImageURL = false;
        isBitmap = true;
        mBitmaps = bitmaps;
        if (bitmaps.size() > 9) {
            for (int i = 0; i < 9; i++) {  //添加9个item
                addView(getImageView(bitmaps.get(i), i), i);
            }
            addOverNumView(9);  //添加第10个item，覆盖第9个item
        } else {
            for (int i = 0; i < bitmaps.size(); i++) {
                addView(getImageView(bitmaps.get(i), i), i);
            }
        }
    }

    public void addBitmap(Bitmap bitmap) {
        if (mBitmaps == null) {
            mBitmaps = new ArrayList<>();
        }
        mBitmaps.add(bitmap);
        if (mBitmaps.size() > 9 && mTextNum != null) {
            mTextNum.setText("+" + (mBitmaps.size() - 9));  //添加第10个item，覆盖第9个item
        } else {
            int index = mBitmaps.size() - 1;
            addView(getImageView(mBitmaps.get(index), index), index);
        }
    }

    /**
     * Uri
     */
    public void setUris(List<Uri> uris) {
        isImageURL = false;
        isUri = true;
        mUris = uris;
        if (uris.size() > 9) {
            for (int i = 0; i < 9; i++) {  //添加9个item
                addView(getImageView(uris.get(i), i), i);
            }
            addOverNumView(9);  //添加第10个item，覆盖第9个item
        } else {
            for (int i = 0; i < uris.size(); i++) {
                addView(getImageView(uris.get(i), i), i);
            }
        }
    }

    public void addUri(Uri uri) {
        if (mUris == null) {
            mUris = new ArrayList<>();
        }
        mUris.add(uri);
        if (mUris.size() > 9 && mTextNum != null) {
            mTextNum.setText("+" + (mUris.size() - 9));  //添加第10个item，覆盖第9个item
        } else {
            int index = mUris.size() - 1;
            addView(getImageView(mUris.get(index), index), index);
        }
    }

    public void clear() {
        mData.clear();
        if (mBitmaps != null) {
            mBitmaps.clear();
        }
        if (mUris != null) {
            mUris.clear();
        }
        removeAllViews();
    }

    /**
     * 当数据是死数据时：推荐使用此方法
     */
    public void setImages(String[] data) {
        setImages(Arrays.asList(data));
    }

    /**
     * 设置最后一个view
     */
    public void addOverNumView(int position) {

        mTextNum = new TextView(getContext());
        mTextNum.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        mTextNum.setTextSize(24);
        mTextNum.setTextColor(TEXT_NUM_COLOR);
        mTextNum.setBackgroundColor(TEXT_NUM_BACKGROUND_COLOR);
        mTextNum.setGravity(Gravity.CENTER);
        if (isImageURL) {
            mTextNum.setText("+" + (mData.size() - 9));
        } else if (isBitmap) {
            mTextNum.setText("+" + (mBitmaps.size() - 9));
        } else if (isUri) {
            mTextNum.setText("+" + (mUris.size() - 9));
        } else {
            mTextNum.setText("+" + (mAdapter.getCount() - 9));
        }

        addView(mTextNum, position);
        Log.i(TAG, "添加最后一个view");
    }

    //通过图片网络地址生成HttpImageView实例
    public HttpImageView getImageView(String url, final int position) {
        HttpImageView img = new HttpImageView(getContext());
        img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (placeholder != -1) {
            img.setLoadImageId(placeholder);
            img.setImageUrl(url);
        } else {
            img.setImageUrl(url);
        }

        img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewImageActivity.class);
                intent.putExtra(ViewImageActivity.IMAGE_NUM, position);
                intent.putExtra(ViewImageActivity.IMAGES_DATA_LIST, (Serializable) mData);
                getContext().startActivity(intent);
            }
        });
        return img;
    }

    //通过Bitmap生成HttpImageView实例
    public HttpImageView getImageView(Bitmap bitmap, final int position) {
        HttpImageView img = new HttpImageView(getContext());
        img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setImageBitmap(bitmap);
        return img;
    }

    //通过Uri生成HttpImageView实例
    public HttpImageView getImageView(Uri uri, final int position) {
        HttpImageView img = new HttpImageView(getContext());
        img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setImageURI(uri);
        return img;
    }

}
