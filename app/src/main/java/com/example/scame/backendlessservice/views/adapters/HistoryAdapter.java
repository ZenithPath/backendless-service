package com.example.scame.backendlessservice.views.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.scame.backendlessservice.R;
import com.example.scame.backendlessservice.models.HistoryAdapterItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context context;

    private List<HistoryAdapterItem> historyItems;

    public HistoryAdapter(Context context, List<HistoryAdapterItem> historyItems) {
        this.historyItems = historyItems;
        this.context = context;
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.registration_type_tv) TextView registrationTypeTv;
        @BindView(R.id.date_tv) TextView dateTextView;
        @BindView(R.id.name_tv) TextView nameTextView;

        HistoryViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View historyView = inflater.inflate(R.layout.history_item, parent, false);

        return new HistoryViewHolder(historyView);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        HistoryAdapterItem historyItem = historyItems.get(position);

        holder.registrationTypeTv.setText(historyItem.getRegistrationType());
        holder.dateTextView.setText(historyItem.getDate().toString());
        holder.nameTextView.setText(historyItem.getName());
    }

    @Override
    public int getItemCount() {
        return historyItems != null ? historyItems.size() : 0;
    }
}
