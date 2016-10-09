package com.example.scame.backendlessservice.presenters;


import com.example.scame.backendlessservice.models.HistoryAdapterItem;

import java.util.List;

public interface IHistoryPresenter<T> extends Presenter<T> {

    interface HistoryView {

        void showHistory(List<HistoryAdapterItem> historyItems);

        void showError(String fault);
    }

    void fetchHistory();
}
