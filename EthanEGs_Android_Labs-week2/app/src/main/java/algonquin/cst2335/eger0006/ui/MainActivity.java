package algonquin.cst2335.eger0006.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import algonquin.cst2335.eger0006.data.MainViewModel;
import algonquin.cst2335.eger0006.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding variableBinding;
    private MainViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());


        //button click listener
        variableBinding.mybutton.setOnClickListener(click ->
        {
            //this is run when you click the button
            model.editString.postValue(variableBinding.myedittext.getText().toString());
            model.editString.observe(this, s -> {
                variableBinding.textview.setText("Your edit text has : "+ s);
            });
        });

        model.coffeeSelect.observe(this, selected -> {
            variableBinding.checkBox.setChecked(selected);
            variableBinding.switch1.setChecked(selected);
            variableBinding.radioButton.setChecked(selected);
                Toast.makeText(this, "The value is now: "+ variableBinding.switch1.isChecked(),Toast.LENGTH_SHORT).show();
        });

        variableBinding.checkBox.setOnCheckedChangeListener((checkBox, isChecked)-> {
            // Update the mutableLiveData object in the MainViewModel
            model.coffeeSelect.postValue(isChecked);
        });
        variableBinding.radioButton.setOnCheckedChangeListener((radioButton, isChecked)-> {
            // Update the mutableLiveData object in the MainViewModel
            model.coffeeSelect.postValue(isChecked);
        });
        variableBinding.switch1.setOnCheckedChangeListener((switch1, isChecked)-> {
            // Update the mutableLiveData object in the MainViewModel
            model.coffeeSelect.postValue(isChecked);
        });

        variableBinding.myimagebutton.setOnClickListener(view -> {
            Toast.makeText(this, "The Width= " + view.getWidth() + "The Height= " + view.getHeight(),Toast.LENGTH_SHORT).show();
        });

    }
}