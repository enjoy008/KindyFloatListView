package com.muhe.kindy.floatlistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kindy on 2015/9/2.
 */
public class MyAdapter extends BaseListFloatViewAdapter<Bean> {
    private static final int TYPE_ITEM     = 0;
    private static final int TYPE_ITEM_SUB = 1;

    private LayoutInflater mLayoutInflater;

    public MyAdapter(Context context, ArrayList<Bean> data, View floatView, OnChangeFloatViewContentListener<Bean> l) {
        super(data, floatView, l);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(isGroup(position)) {
            return TYPE_ITEM;
        }

        return TYPE_ITEM_SUB;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        return !isGroup(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            if(isGroup(position)) {
                convertView = mLayoutInflater.inflate(R.layout.item, parent, false);
                holder.tv = (TextView) convertView.findViewById(R.id.id_tv_item);
            } else {
                convertView = mLayoutInflater.inflate(R.layout.item_sub, parent, false);
                holder.tv = (TextView) convertView.findViewById(R.id.id_tv_item_sub);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Bean bean = mData.get(position);
        holder.tv.setText(bean.name);
        return convertView;
    }

    @Override
    public boolean isGroup(int position) {
        Bean bean = mData.get(position);
        return bean.isGroup;
    }

    private static class ViewHolder {
        public TextView tv;
    }
}
