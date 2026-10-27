package com.example.smartpantrymanager.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/** Temporary screen for tabs that are built in Increment 3. */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_TEXT = "text";

    public static PlaceholderFragment newInstance(String text) {
        PlaceholderFragment f = new PlaceholderFragment();
        Bundle b = new Bundle();
        b.putString(ARG_TEXT, text);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        TextView tv = new TextView(requireContext());
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(16);
        tv.setText(getArguments() != null ? getArguments().getString(ARG_TEXT) : "");
        return tv;
    }
}
