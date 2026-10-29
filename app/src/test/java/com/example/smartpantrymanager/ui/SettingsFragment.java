package com.example.smartpantrymanager.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.util.Prefs;
import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        Context ctx = requireContext();

        MaterialSwitch alertSwitch = root.findViewById(R.id.switchExpiry);
        RadioGroup daysGroup = root.findViewById(R.id.radioDays);

        alertSwitch.setChecked(Prefs.expiryAlertsEnabled(ctx));

        int days = Prefs.expiryDays(ctx);
        daysGroup.check(days == 2 ? R.id.radio2 : days == 7 ? R.id.radio7 : R.id.radio3);
        setGroupEnabled(daysGroup, alertSwitch.isChecked());

        alertSwitch.setOnCheckedChangeListener((button, checked) -> {
            Prefs.setExpiryAlertsEnabled(ctx, checked);
            setGroupEnabled(daysGroup, checked);
        });

        daysGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int value = checkedId == R.id.radio2 ? 2 : checkedId == R.id.radio7 ? 7 : 3;
            Prefs.setExpiryDays(ctx, value);
        });

        return root;
    }

    private static void setGroupEnabled(RadioGroup group, boolean enabled) {
        for (int i = 0; i < group.getChildCount(); i++) {
            group.getChildAt(i).setEnabled(enabled);
        }
    }
}
