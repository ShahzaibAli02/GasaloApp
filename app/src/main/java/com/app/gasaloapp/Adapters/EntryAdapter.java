package com.app.gasaloapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.app.gasaloapp.Model.Entry;
import com.app.gasaloapp.databinding.ItemEntryBinding;

import java.util.ArrayList;
import java.util.List;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder> {
    private List<Entry>listData;
    Context context;
    public EntryAdapter(List<Entry> listData, Context context) {
        this.listData = listData;
        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        ItemEntryBinding binding=ItemEntryBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {

        Entry entry=listData.get(i);
        ItemEntryBinding binding=holder.binding;
        binding.txtPerson.setText(entry.person_id);
        binding.txtDate.setText(entry.date);
        binding.txtTime.setText(entry.date);
        binding.txtLiteres.setText(entry.total_literes);
        binding.txtTotalLiteres.setText(entry.now_refueling);
        binding.txtNote.setText(entry.note);

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ItemEntryBinding binding;
        public ViewHolder(ItemEntryBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }
    }

}
