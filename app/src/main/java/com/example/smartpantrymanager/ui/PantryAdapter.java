package com.example.smartpantrymanager.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.util.NameNormalizer;

import java.util.ArrayList;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    public interface Listener {
        void onItemClick(PantryItem item);
        void onDeleteClick(PantryItem item);
    }

    private final List<PantryItem> items = new ArrayList<>();
    private final Listener listener;

    public PantryAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<PantryItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        PantryItem item = items.get(position);
        h.name.setText(NameNormalizer.display(item.name));

        String qty = item.quantity == Math.floor(item.quantity)
                ? String.valueOf((long) item.quantity)
                : String.valueOf(item.quantity);
        h.quantity.setText(qty + " " + item.unit);

        if (item.expiryDate == null || item.expiryDate.isEmpty()) {
            h.expiry.setVisibility(View.GONE);
        } else {
            h.expiry.setVisibility(View.VISIBLE);
            h.expiry.setText(h.itemView.getContext()
                    .getString(R.string.expires_on, item.expiryDate));
        }

        h.itemView.setOnClickListener(v -> listener.onItemClick(item));
        h.delete.setOnClickListener(v -> listener.onDeleteClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name, quantity, expiry;
        final ImageButton delete;

        ViewHolder(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.textName);
            quantity = v.findViewById(R.id.textQuantity);
            expiry = v.findViewById(R.id.textExpiry);
            delete = v.findViewById(R.id.buttonDelete);
        }
    }
}
