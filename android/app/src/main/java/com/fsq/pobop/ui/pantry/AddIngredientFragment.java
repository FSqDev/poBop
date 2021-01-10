package com.fsq.pobop.ui.pantry;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.fsq.pobop.R;
import com.fsq.pobop.entity.ingredient.Ingredient;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class AddIngredientFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    PantryViewModel viewModel;
    TextView dateTextView;
    View root;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_add_ingredient, container, false);
        viewModel = new ViewModelProvider(this).get(PantryViewModel.class);

        TextInputEditText nameTextView = root.findViewById(R.id.editTextIngredientName);
        dateTextView = root.findViewById(R.id.textViewDate);
        dateTextView.setText(LocalDate.now().format(formatter));
        dateTextView.setOnClickListener(v -> showDatePickerDialog());

        Button addButton = root.findViewById(R.id.buttonAddIngredient);
        addButton.setOnClickListener(v -> {
            Ingredient ingredient = new Ingredient();
            ingredient.setProductName(nameTextView.getText().toString());
            ingredient.setExpiryDate(LocalDate.parse(dateTextView.getText().toString(), formatter));
            ingredient.setDirty(1);
            viewModel.add(ingredient);
            Navigation.findNavController(root).navigateUp();
        });

        return root;
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
        dateTextView.setText(date);
    }
}