package com.rikkeisoft.music.Adapter.helper;

/**
 * Created by nguyenquanghung on 9/7/17.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
