package com.fsq.pobop.ui.qr;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.fsq.pobop.R;
import com.fsq.pobop.data.DateConverter;
import com.fsq.pobop.entity.ingredient.Ingredient;
import com.fsq.pobop.ui.pantry.PantryFragmentDirections;

public class QRFragment extends Fragment {
    private CodeScanner mCodeScanner;

    private QRViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View root = inflater.inflate(R.layout.fragment_qr, container, false);
        viewModel = new ViewModelProvider(this).get(QRViewModel.class);
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.setDecodeCallback(result -> activity.runOnUiThread(() -> {
            Ingredient ingredient = new Ingredient();
            DateConverter dateConverter = new DateConverter();
            String[] results = result.getText().split(",");
            ingredient.setBarcode(results[0]);
            ingredient.setExpiryDate(dateConverter.stringToDate(results[1]));
            ingredient.setDirty(1);
            viewModel.add(ingredient);
            Navigation.findNavController(root).navigate(QRFragmentDirections.actionNavQrToNavPantry());
        }));
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}