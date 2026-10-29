package com.example.smartpantrymanager.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.util.ExpiryUtil;
import com.example.smartpantrymanager.util.Formatters;
import com.example.smartpantrymanager.util.NameNormalizer;
import com.example.smartpantrymanager.util.Prefs;

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
        Context ctx = h.itemView.getContext();

        h.name.setText(NameNormalizer.display(item.name));
        h.quantity.setText(Formatters.quantity(item.quantity) + " " + item.unit);

        Integer days = ExpiryUtil.daysUntil(item.expiryDate);
        if (days == null) {
            h.expiry.setVisibility(View.GONE);
        } else {
            h.expiry.setVisibility(View.VISIBLE);
            boolean alerts = Prefs.expiryAlertsEnabled(ctx);
            int limit = Prefs.expiryDays(ctx);

            if (alerts && days < 0) {
                h.expiry.setText(ctx.getString(R.string.expired_on, item.expiryDate));
                h.expiry.setTextColor(ContextCompat.getColor(ctx, R.color.expiry_warning));
            } else if (alerts && days <= limit) {
                h.expiry.setText(ctx.getString(R.string.expires_soon, item.expiryDate));
                h.expiry.setTextColor(ContextCompat.getColor(ctx, R.color.expiry_warning));
            } else {
                h.expiry.setText(ctx.getString(R.string.expires_on, item.expiryDate));
                h.expiry.setTextColor(h.defaultExpiryColors);
            }
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
        final ColorStateList defaultExpiryColors;

        ViewHolder(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.textName);
            quantity = v.findViewById(R.id.textQuantity);
            expiry = v.findViewById(R.id.textExpiry);
            delete = v.findViewById(R.id.buttonDelete);
            defaultExpiryColors = expiry.getTextColors();
        }
    }
}
