package com.fsq.pobop.ui.qr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.fsq.pobop.R;
import com.fsq.pobop.data.DateConverter;
import com.fsq.pobop.entity.ingredient.Ingredient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class QRFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    View root;

    private CodeScanner mCodeScanner;

    private QRViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)  {
        final Activity activity = getActivity();
        root = inflater.inflate(R.layout.fragment_qr, container, false);
        viewModel = new ViewModelProvider(this).get(QRViewModel.class);
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.setDecodeCallback(result -> activity.runOnUiThread(() -> {
            Ingredient ingredient = new Ingredient();
            DateConverter dateConverter = new DateConverter();
            String[] results = result.getText().split(",");
            if(results.length == 2) {
                ingredient.setBarcode(results[0]);
                ingredient.setExpiryDate(dateConverter.stringToDate(results[1]));
                ingredient.setDirty(1);
                viewModel.add(ingredient);
            } else if (results.length == 1) {
                createAlertDialog().show();
                ingredient.setBarcode(results[0]);
                ingredient.setExpiryDate(content.findViewById(R.id.dialogExpiryDate).getText())
                ingredient.setDirty(1);
                viewModel.add(ingredient);
            } else {
                //What the fuck have you brought upon this cursed land
            }
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

    TextView expiryDate;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private AlertDialog createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View content = layoutInflater.inflate(R.layout.dialog_expiry_date, null);
        expiryDate = content.findViewById(R.id.dialogExpiryDate);
        expiryDate.setText(LocalDate.now().format(formatter));

        expiryDate.setOnClickListener(v -> showDatePickerDialog());

        builder.setView(content)
                .setTitle("Set expiry date")
                .setPositiveButton("Confirm", ((dialog, which) -> {
                    // do stuff
                    dialog.dismiss();
                }))
                .setNegativeButton("Cancel", (((dialog, which) -> {
                    dialog.cancel();
                })));
        return builder.create();
    }

    // Initializes Date Picker and sets default values to be current date
    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                root.getContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    // Modifies textView to match user input into date picker
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month += 1;
        String monthTxt = Integer.toString(month);
        String dayTxt = Integer.toString(dayOfMonth);
        if (month<10){
            monthTxt = '0'+ monthTxt;
        }
        if (dayOfMonth<10){
            dayTxt = '0' + dayTxt;
        }
        String date = year + "-" + monthTxt + "-" + dayTxt;
        expiryDate.setText(date);
    }
}