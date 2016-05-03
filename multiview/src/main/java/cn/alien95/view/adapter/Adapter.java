package cn.alien95.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.alien95.view.util.MessageNotify;


/**
 * Created by linlongxin on 2015/12/31.
 */
public abstract class Adapter<T> {

    public static Context context;
    private List<T> data = new ArrayList<>();

    public Adapter(Context context) {
        this.context = context;
    }

    public abstract View getView(ViewGroup parent, int position);

    public int getCount() {
        return data.size();
    }

    public T getItem(int position){
        return data.get(position);
    }

    public void add(T object) {
        data.add(object);
        notifyDataChanged();
    }

    public void addAll(List<T> mData) {
        data.addAll(mData);
        notifyDataChanged();
    }

    public void addAll(T[] objects) {
        data.addAll(Arrays.asList(objects));
        notifyDataChanged();
    }

    public void clear() {
        data.clear();
        notifyDataChanged();
    }

    public void notifyDataChanged() {
        MessageNotify.getInstance().sendMessage();
    }

}
