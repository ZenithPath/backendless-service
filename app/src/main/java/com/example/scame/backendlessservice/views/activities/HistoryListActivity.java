package com.example.scame.backendlessservice.views.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.scame.backendlessservice.R;
import com.example.scame.backendlessservice.models.HistoryAdapterItem;
import com.example.scame.backendlessservice.presenters.HistoryPresenter;
import com.example.scame.backendlessservice.presenters.IHistoryPresenter;
import com.example.scame.backendlessservice.repository.BackendlessRepository;
import com.example.scame.backendlessservice.views.adapters.DividerItemDecoration;
import com.example.scame.backendlessservice.views.adapters.HistoryAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryListActivity extends AppCompatActivity implements IHistoryPresenter.HistoryView {

    @BindView(R.id.history_list_rv) RecyclerView historyListRv;

    private IHistoryPresenter<IHistoryPresenter.HistoryView> historyPresenter;

    private List<HistoryAdapterItem> historyItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_list_activity);

        ButterKnife.bind(this);
        historyPresenter = new HistoryPresenter<>(new BackendlessRepository());
        historyPresenter.setView(this);

        showHistory(savedInstanceState);
    }

    private void showHistory(Bundle savedInstanceState) {
        if (savedInstanceState == null || savedInstanceState
                .getParcelableArrayList(getString(R.string.adapter_items_key)) == null) {
            historyPresenter.fetchHistory();
        } else {
            showHistory(savedInstanceState.getParcelableArrayList(getString(R.string.adapter_items_key)));
        }
    }

    @Override
    public void showHistory(List<HistoryAdapterItem> historyItems) {
        this.historyItems = historyItems;

        HistoryAdapter historyAdapter = new HistoryAdapter(this, historyItems);
        historyListRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        historyListRv.setHasFixedSize(true);
        historyListRv.addItemDecoration(new DividerItemDecoration(this));
        historyListRv.setAdapter(historyAdapter);
    }

    @Override
    public void showError(String fault) {
        Toast.makeText(getApplicationContext(), fault, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        historyPresenter.destroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.adapter_items_key), new ArrayList<>(historyItems));
    }
}
