package com.example.scame.backendlessservice.presenters;


import com.example.scame.backendlessservice.models.HistoryAdapterItem;
import com.example.scame.backendlessservice.repository.BackendlessRepository;
import com.example.scame.backendlessservice.repository.IBackendlessRepository;

import java.util.List;

public class HistoryPresenter<T extends IHistoryPresenter.HistoryView> implements IHistoryPresenter<T>,
                    IBackendlessRepository.HistoryListener {

    private IBackendlessRepository backendlessRepository;

    private T view;

    public HistoryPresenter(IBackendlessRepository backendlessRepository) {
        this.backendlessRepository = backendlessRepository;
    }

    @Override
    public void fetchHistory() {
        ((BackendlessRepository) backendlessRepository).setHistoryListener(this);
        backendlessRepository.fetchHistory();
    }

    @Override
    public void onSuccessfulRequest(List<HistoryAdapterItem> historyItems) {
        if (view != null) {
            view.showHistory(historyItems);
        }
    }

    @Override
    public void onFailedRequest(String fault) {
        if (view != null) {
            view.showError(fault);
        }
    }

    @Override
    public void setView(T view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
        ((BackendlessRepository) backendlessRepository).setHistoryListener(null);
    }
}
