package com.vietdung.beautymusic.presenter.called_listener;

import com.vietdung.beautymusic.model.Songs;

import java.util.List;

public interface OnCustomerListChangedListener {
    void onNoteListChanged(List<Songs> songsList);
}
