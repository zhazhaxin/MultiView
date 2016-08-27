package cn.lemon.multi;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import cn.alien95.resthttp.view.HttpImageView;
import cn.lemon.multi.adapter.Adapter;
import cn.lemon.multi.ui.ViewImageActivity;
import cn.lemon.multi.util.Util;


/**
 * Created by linlongxin on 2015/12/28.
 */
public class MultiView extends ViewGroup {

    private static final String TAG = "MultiView";
    private static boolean isDataFromAdapter = false;

    private int childWidth, childHeight;
    private int divideSpace;
    private int placeholder;
    private Adapter mAdapter;
    private List<String> mData;
    private int childCount;

    public MultiView(Context context) {
        super(context, null);
    }

    public MultiView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Util.init(context);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiView);
        divideSpace = (int) typedArray.getDimension(R.styleable.MultiView_divideSpace, Util.dip2px(4));
        placeholder = typedArray.getResourceId(R.styleable.MultiView_placeholder, -1);
        typedArray.recycle();
    }

    //测量自己的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, "onMeasure");

        childCount = getChildCount();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {
            Log.i(TAG, "fuck--widthMode");

        }
        if (heightMode == MeasureSpec.AT_MOST) {
            Log.i(TAG, "fuck--heightMode");
        }
        int height;

        if (childCount == 1) {
            childWidth = width - divideSpace * 2;
            height = width;
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
                } else
                    height = childWidth * (childCount / 3 + 1) + divideSpace * (childCount / 3 + 2);
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
    public void setAdapter(Adapter adapter) {
        isDataFromAdapter = true;
        this.mAdapter = adapter;
        addViews();
        adapter.attachView(this);
    }

    /**
     * 添加adapter中所有的view
     */
    public void addViews() {
        if (isDataFromAdapter) {
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
        View item;
        addView(item = mAdapter.getView(this, position));
        mAdapter.setData(mAdapter.mData.get(position));
        item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setOnItemClick();
            }
        });
    }

    public void addView(int position) {
        if (isDataFromAdapter) {
            if (position > 8) {
                addOverNumView(9);
                return;
            }
            addView(mAdapter.getView(this, position));
            mAdapter.setData(mAdapter.mData.get(position));
        }
    }

    /**
     * 同上
     */
    public void setImages(List<String> data) {
        isDataFromAdapter = false;
        this.mData = data;
        if (data.size() > 9) {
            for (int i = 0; i < 9; i++) {  //添加9个item
                addView(getImageView(data.get(i), i));
            }
            addOverNumView(9);  //添加第10个item，覆盖第9个item
        } else {
            for (int i = 0; i < data.size(); i++) {
                addView(getImageView(data.get(i), i));
            }
        }
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

        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        textView.setTextSize(24);
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setBackgroundColor(Color.parseColor("#33000000"));
        textView.setGravity(Gravity.CENTER);
        if (isDataFromAdapter) {
            textView.setText("+" + (mAdapter.getCount() - 9));
        } else
            textView.setText("+" + (mData.size() - 9));

        addView(textView, position);
        Log.i(TAG, "添加最后一个view");
    }

    /**
     * 获取一个ImageView
     */
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

}
