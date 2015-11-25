package com.muhe.kindy.floatlistview;

import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;

/**
 * Created by Kindy on 2015/9/2.
 */
public abstract class BaseListFloatViewAdapter<T> extends BaseAdapter implements IGroupHelper , AbsListView.OnScrollListener {
    private static final int NOT_FROUND = -1;
    protected ArrayList<T> mData;

    private View mFloatView;
    private OnChangeFloatViewContentListener<T> mOnChangeFloatViewContentListener;

    /** 默认为 0 */
    private int mHeaderItemCount;
    private int mDividerHeight;
    public void setHeanderItemCount(int count) {
        mHeaderItemCount = count;
    }

    public void setFloatView(View floatView) {
        this.mFloatView = floatView;
    }

    public void setDividerHeight(int dividerHeight) {
        if(dividerHeight < 0) {
            dividerHeight = 0;
        }
        mDividerHeight = dividerHeight;
    }

    public BaseListFloatViewAdapter(ArrayList<T> mData, View floatView, OnChangeFloatViewContentListener<T> l) {
        this.mData = mData;
        this.mFloatView = floatView;
        this.mOnChangeFloatViewContentListener = l;
        if(mFloatView != null) {
            // 截断Touche
            mFloatView.setClickable(true);
        }
        mHeaderItemCount = 0;
    }

    @Override
    public int getPreviousGroupPosition(int position) {
        if(mData != null && position >= 0 && position < mData.size()) {
            for(int i=position; i>=0; i--) {
                if(isGroup(i)) {
                    return i;
                }
            }
        }
        return NOT_FROUND;
    }

    @Override
    public int getNextGroupPosition(int position) {
        if(mData != null && position >= 0 && position < mData.size()) {
            for(int i=position; i<mData.size(); i++) {
                if(isGroup(i)) {
                    return i;
                }
            }
        }
        return NOT_FROUND;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(AbsListView.OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
            int firstVisibleItem = view.getFirstVisiblePosition();
            int lastVisibleItem = view.getLastVisiblePosition();
            int visibleItemCount = lastVisibleItem - firstVisibleItem + 1;
            this.onScroll(view, firstVisibleItem, visibleItemCount, this.getCount());
        }
    }

    private void notifyChangeFloatViewContent(int position) {
        if(mData != null && position >= 0 && position < mData.size() && mOnChangeFloatViewContentListener != null) {
            mOnChangeFloatViewContentListener.onChangeFloatViewContent((T)this.getItem(position));
        }
    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(mFloatView == null) {
            return;
        }

        L.o(this, " firstVisibleItem:" + firstVisibleItem + " visibleItemCount:" + visibleItemCount + " totalItemCount:" + totalItemCount);

        if(visibleItemCount == 0) {
            mFloatView.setVisibility(View.INVISIBLE);
            return;
        }

        // 第一个child的top
        int firstVisibleItemTop = view.getChildAt(0).getTop();
        int firstVisibleItemBottom = view.getChildAt(0).getBottom();
        L.o(this, " firstVisibleItemTop : " + firstVisibleItemTop + "  firstVisibleItemBottom : " + firstVisibleItemBottom);
        if(firstVisibleItem == 0) {
            if(firstVisibleItemTop == 0) {
                mFloatView.setVisibility(View.INVISIBLE);
            }
        }

        // 第一个可见的item位置
        int firstVisiblePosition = firstVisibleItem - mHeaderItemCount;
        if(firstVisiblePosition < 0) {
            return;
        }

        boolean firstIsGroup = this.isGroup(firstVisiblePosition);
        int pre = this.getPreviousGroupPosition(firstVisiblePosition - 1);
        int next = this.getNextGroupPosition(firstVisiblePosition+1);
        L.o(this, " firstIsGroup : " + firstIsGroup + " pre : " + pre + " next : " + next);
        if(firstIsGroup) {
            if(firstVisibleItemTop <= 0) {
                mFloatView.setVisibility(View.VISIBLE);
                if(next == NOT_FROUND) {
                    ViewHelper.setY(mFloatView, 0);
                } else {
                    View nextView = view.getChildAt(next-firstVisiblePosition);
                    L.o(this, " next:" + next + " nextView.getTop():" + nextView.getTop());
                    if(nextView.getTop() > mFloatView.getHeight()) {
                        ViewHelper.setY(mFloatView, 0);
                    } else {
                        ViewHelper.setY(mFloatView, nextView.getTop() - mFloatView.getHeight());
                    }
                }
                notifyChangeFloatViewContent(firstVisiblePosition);
            } else {
                if(pre == NOT_FROUND) {
                    mFloatView.setVisibility(View.INVISIBLE);
                } else {
                    mFloatView.setVisibility(View.VISIBLE);
                    ViewHelper.setY(mFloatView, firstVisibleItemTop - mFloatView.getHeight());
                    notifyChangeFloatViewContent(firstVisiblePosition);
                }
            }
        } else {
            if(pre == NOT_FROUND) {
                mFloatView.setVisibility(View.INVISIBLE);
                return;
            }

            mFloatView.setVisibility(View.VISIBLE);
            if(next == NOT_FROUND) {
                ViewHelper.setY(mFloatView, 0);
            } else {
                View nextView = view.getChildAt(next-firstVisiblePosition);
                L.o(this, " next:" + next + " nextView.getTop():" + nextView.getTop());
                if(nextView.getTop() > mFloatView.getHeight()) {
                    ViewHelper.setY(mFloatView, 0);
                } else {
                    ViewHelper.setY(mFloatView, nextView.getTop() - mFloatView.getHeight());
                }
            }
            notifyChangeFloatViewContent(pre);
        }
    }
}
