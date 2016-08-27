package cn.lemon.multi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



import cn.lemon.multi.MultiView;



/**
 * Created by linlongxin on 2015/12/31.
 */
public abstract class Adapter<T> {

    protected static Context mContext;

    private MultiView mMultiView;

    public List<T> mData = new ArrayList<>();

    public Adapter(Context context) {
        mContext = context;
    }

    public void attachView(MultiView view){
        mMultiView = view;
    }

    public abstract View getView(ViewGroup parent, int position);

    public int getCount() {
        return mData.size();
    }

    public T getItem(int position){
        return mData.get(position);
    }

    public void add(T object) {
        mData.add(object);
        notifyItemInsert(mData.indexOf(object));
    }

    public void addAll(List<T> mData) {
        this.mData.addAll(mData);
        notifyDataChanged();
    }

    public void addAll(T[] objects) {
        mData.addAll(Arrays.asList(objects));
        notifyDataChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataChanged();
    }

    public void notifyDataChanged() {
        mMultiView.addViews();
    }
    public void notifyItemInsert(int position){
        mMultiView.addView(position);
    }
    public void setData(T object){

    }
    public void setOnItemClick(){

    }
}
