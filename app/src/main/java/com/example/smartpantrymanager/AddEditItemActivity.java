package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smartpantrymanager.db.AppDatabase;
import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.util.NameNormalizer;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Locale;

public class AddEditItemActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "item_id";

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private TextInputLayout layoutName, layoutQuantity;
    private TextInputEditText editName, editQuantity, editExpiry;
    private Spinner spinnerUnit;
    private String[] units;

    private int itemId = -1;   // -1 means "adding a new item"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        itemId = getIntent().getIntExtra(EXTRA_ITEM_ID, -1);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(itemId == -1 ? R.string.add_item : R.string.edit_item);
        }

        layoutName = findViewById(R.id.layoutName);
        layoutQuantity = findViewById(R.id.layoutQuantity);
        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        editExpiry = findViewById(R.id.editExpiry);
        spinnerUnit = findViewById(R.id.spinnerUnit);

        units = getResources().getStringArray(R.array.units);
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, units);
        spinnerUnit.setAdapter(unitAdapter);

        editExpiry.setOnClickListener(v -> showDatePicker());
        findViewById(R.id.buttonClearExpiry).setOnClickListener(v -> editExpiry.setText(""));

        MaterialButton save = findViewById(R.id.buttonSave);
        save.setOnClickListener(v -> save());

        if (itemId != -1) loadExisting();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void loadExisting() {
        AppDatabase db = AppDatabase.getInstance(this);
        AppDatabase.IO.execute(() -> {
            PantryItem item = db.pantryDao().getById(itemId);
            mainHandler.post(() -> {
                if (item == null) { finish(); return; }
                editName.setText(NameNormalizer.display(item.name));
                editQuantity.setText(item.quantity == Math.floor(item.quantity)
                        ? String.valueOf((long) item.quantity)
                        : String.valueOf(item.quantity));
                for (int i = 0; i < units.length; i++) {
                    if (units[i].equals(item.unit)) { spinnerUnit.setSelection(i); break; }
                }
                editExpiry.setText(item.expiryDate == null ? "" : item.expiryDate);
            });
        });
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, y, m, d) ->
                editExpiry.setText(String.format(Locale.ROOT, "%04d-%02d-%02d", y, m + 1, d)),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void save() {
        // ---- validation ----
        boolean valid = true;

        String rawName = editName.getText() == null ? "" : editName.getText().toString().trim();
        if (rawName.isEmpty()) {
            layoutName.setError(getString(R.string.error_name_required));
            valid = false;
        } else if (!rawName.matches("[\\p{L}][\\p{L} '\\-]*")) {
            layoutName.setError(getString(R.string.error_name_invalid));
            valid = false;
        } else {
            layoutName.setError(null);
        }

        double quantity = 0;
        String rawQty = editQuantity.getText() == null ? "" : editQuantity.getText().toString().trim();
        if (rawQty.isEmpty()) {
            layoutQuantity.setError(getString(R.string.error_quantity_required));
            valid = false;
        } else {
            try {
                quantity = Double.parseDouble(rawQty);
                if (quantity <= 0) {
                    layoutQuantity.setError(getString(R.string.error_quantity_positive));
                    valid = false;
                } else {
                    layoutQuantity.setError(null);
                }
            } catch (NumberFormatException e) {
                layoutQuantity.setError(getString(R.string.error_quantity_invalid));
                valid = false;
            }
        }

        if (!valid) return;

        String unit = (String) spinnerUnit.getSelectedItem();
        String expiry = editExpiry.getText() == null ? "" : editExpiry.getText().toString().trim();
        String expiryOrNull = expiry.isEmpty() ? null : expiry;
        String name = NameNormalizer.normalize(rawName);
        final double qty = quantity;

        // ---- persist ----
        AppDatabase db = AppDatabase.getInstance(this);
        AppDatabase.IO.execute(() -> {
            PantryItem item = new PantryItem(name, qty, unit, expiryOrNull);
            if (itemId == -1) {
                db.pantryDao().insert(item);
            } else {
                item.id = itemId;
                db.pantryDao().update(item);
            }
            mainHandler.post(() -> {
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}
