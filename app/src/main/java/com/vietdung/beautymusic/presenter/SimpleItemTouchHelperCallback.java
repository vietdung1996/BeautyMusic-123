package com.vietdung.beautymusic.presenter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback{
    private ItemTouchListenner listenner;

    public SimpleItemTouchHelperCallback(ItemTouchListenner listenner) {
        this.listenner = listenner;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragflag =  ItemTouchHelper.UP|ItemTouchHelper.DOWN;
        int swipeFlag = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragflag,swipeFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        listenner.onMode(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return false;
    }



    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }
}
