package com.kindy.floatlistview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private MyAdapter mMyAdapter;
    private ArrayList<Bean> mData;

    private ListView mListView;
    private LinearLayout mFloatView;
    private TextView mFloatText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.id_listview);
        mFloatView = (LinearLayout) findViewById(R.id.id_float_view);
        mFloatText = (TextView) mFloatView.findViewById(R.id.id_tv_item);

        // TODO 方便观察
        //ViewHelper.setX(mFloatText, 10);

        mData = new ArrayList<>();
        for(int i=0; i<3; i++) {
            mData.add(new Bean("Kindy__" + i, false));
        }
        mData.add(new Bean("Group_1", true));
        for(int i=0; i<1; i++) {
            mData.add(new Bean("小__" + i, false));
        }
        mData.add(new Bean("Group_2", true));
        for(int i=0; i<5; i++) {
            mData.add(new Bean("美__" + i, false));
        }
        mData.add(new Bean("Group_3", true));
        for(int i=0; i<20; i++) {
            mData.add(new Bean("女__" + i, false));
        }

        mMyAdapter = new MyAdapter(this, mData, mFloatView, mOnChangeFloatViewContentListener);
        mMyAdapter.setHeanderItemCount(mListView.getHeaderViewsCount());
        mMyAdapter.setDividerHeight(mListView.getDividerHeight());
        mListView.setAdapter(mMyAdapter);
        mListView.setOnScrollListener(mMyAdapter);
    }

    public void addItem(View v) {
        mData.add(4, new Bean("new item", false));
        mMyAdapter.notifyDataSetChanged();
    }

    private OnChangeFloatViewContentListener<Bean> mOnChangeFloatViewContentListener = new OnChangeFloatViewContentListener<Bean>() {
        @Override
        public void onChangeFloatViewContent(Bean bean) {
            String txt = mFloatText.getText().toString();
            if(!txt.equalsIgnoreCase(bean.name)) {
                mFloatText.setText(bean.name);
            }
        }
    };
}