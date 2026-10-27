package com.example.smartpantrymanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.AddEditItemActivity;
import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.db.AppDatabase;
import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.util.NameNormalizer;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PantryFragment extends Fragment implements PantryAdapter.Listener {

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private PantryAdapter adapter;
    private TextView emptyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pantry, container, false);

        emptyView = root.findViewById(R.id.textEmpty);
        RecyclerView recycler = root.findViewById(R.id.recyclerPantry);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new PantryAdapter(this);
        recycler.setAdapter(adapter);

        FloatingActionButton fab = root.findViewById(R.id.fabAdd);
        fab.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), AddEditItemActivity.class)));
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadItems();   // refreshes after returning from the Add/Edit screen
    }

    private void loadItems() {
        AppDatabase db = AppDatabase.getInstance(requireContext());
        AppDatabase.IO.execute(() -> {
            List<PantryItem> items = db.pantryDao().getAll();
            mainHandler.post(() -> {
                if (!isAdded()) return;
                adapter.setItems(items);
                emptyView.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
            });
        });
    }

    @Override
    public void onItemClick(PantryItem item) {
        Intent intent = new Intent(requireContext(), AddEditItemActivity.class);
        intent.putExtra(AddEditItemActivity.EXTRA_ITEM_ID, item.id);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(PantryItem item) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.delete_title)
                .setMessage(getString(R.string.delete_message, NameNormalizer.display(item.name)))
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (d, w) -> {
                    AppDatabase db = AppDatabase.getInstance(requireContext());
                    AppDatabase.IO.execute(() -> {
                        db.pantryDao().delete(item);
                        mainHandler.post(() -> { if (isAdded()) loadItems(); });
                    });
                })
                .show();
    }
}
