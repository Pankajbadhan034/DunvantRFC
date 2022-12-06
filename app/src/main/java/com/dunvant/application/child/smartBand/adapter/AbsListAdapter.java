package com.dunvant.application.child.smartBand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * ListView适配器抽象类
 * 
 * @author hzy
 * 
 * @param <T>
 */

public abstract class AbsListAdapter<T> extends BaseAdapter {

    protected List<T> mList;
    protected Context mContext;
    protected LayoutInflater mInflater;


    public AbsListAdapter(Context context, List<T> mList) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        setList(mList);
    }


    public AbsListAdapter(Context context, T[] mArray) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        setList(mArray);
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return mList != null ? mList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private void setList(List<T> mList2) {
        this.mList = mList2 != null ? mList2 : new ArrayList<T>();
    }

    public List<T> getList() {
        return mList;
    }


    private void setList(T[] mArray) {
        List<T> list = new ArrayList<T>();
        if (mArray != null) {
            for (T t : mArray) {
                list.add(t);
            }
        }
        setList(list);
    }


    public void changeData(List<T> mList) {
        setList(mList);
        this.notifyDataSetChanged();
    }

    public void changeData(T[] mArray) {
        setList(mArray);
        this.notifyDataSetChanged();
    }


    public boolean add(T t) {
        boolean result = this.mList.add(t);
        this.notifyDataSetChanged();
        return result;
    }


    public boolean addAll(List<T> mList) {
        if (mList == null) {
            return false;
        }
//        HashSet<T> set = new HashSet<T>(mList);
//        set.addAll(this.mList);
//        this.mList.clear();
        boolean result = this.mList.addAll(mList);
        notifyDataSetChanged();
        return result;
    }

    public boolean set(List<T> mList){
        this.mList.clear();
        boolean result = this.mList.addAll(mList);
        notifyDataSetChanged();
        return result;
    }


    public void insert(int position, T t) {
        this.mList.add(position, t);
        this.notifyDataSetChanged();
    }


    public boolean remove(T t) {
        boolean removed = this.mList.remove(t);
        this.notifyDataSetChanged();
        return removed;
    }

    public boolean removeAll(Collection<T> list) {
        boolean removed = mList.removeAll(list);
        this.notifyDataSetChanged();
        return removed;
    }


    public T remove(int position) {
        T t = this.mList.remove(position);
        this.notifyDataSetChanged();
        return t;
    }


    public void clear() {
        this.mList.clear();
        this.notifyDataSetChanged();
    }


    public void set(int position, T t) {
        this.mList.set(position, t);
        this.notifyDataSetChanged();
    }

    public void sort(Comparator<T> comparator) {
        Collections.sort(mList, comparator);
        this.notifyDataSetChanged();
    }

    @Override
    public abstract View getView(int position, View convertView,
            ViewGroup parent);

}
