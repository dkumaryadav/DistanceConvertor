package com.deepakyadav.convertor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    public double converterValue = 1.60934; // Miles to K.M Ratio
    String fromUnit = "Mi";
    String toUnit = "Km";
    public TextView conversionHistory ;
    public TextView fromLabel;
    public TextView toLabel;
    public EditText fromEditText;
    public EditText toEditText;
    public RadioGroup radioGroup;
    DecimalFormat resultFormat = new DecimalFormat("0.0");

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialize fromEditText and toEditText and radio buttons
        fromLabel = (TextView)findViewById(R.id.conversionFromText);
        fromEditText = (EditText)findViewById(R.id.conversionFromInput);
        toLabel = (TextView)findViewById(R.id.convertToText);
        toEditText = (EditText)findViewById(R.id.conversionToInput);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        // Make conversion History scrollable
        conversionHistory = (TextView)findViewById(R.id.coversionHistoryView);
        conversionHistory.setMovementMethod(new ScrollingMovementMethod());

        // Get preference
        preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        String prefIs = preferences.getString("conversionPref", "Mi");
        if( prefIs.equals("Mi")) {
            radioGroup.check(R.id.radioButton1);
            updateValues("Miles Value", "Kilometers Value", 1.60934, "Mi", "Km");
        } else{
            radioGroup.check(R.id.radioButton2);
            updateValues("Kilometers Value", "Miles Value", 0.621371, "Km", "Mi");
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt("RadioButtonID", radioGroup.getCheckedRadioButtonId());
        outState.putString("fromValue", fromEditText.getText().toString().trim());
        outState.putString("toValue", toEditText.getText().toString().trim());
        outState.putString("fromText", fromLabel.getText().toString());
        outState.putString("toText", toLabel.getText().toString());
        outState.putString("conversionHistory", conversionHistory.getText().toString().trim());
        outState.putDouble("converterValue", converterValue);
        outState.putString("fromUnit", fromUnit);
        outState.putString("toUnit", toUnit);

        // Call super last
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);

        int radioButtonID = savedInstanceState.getInt("RadioButtonID");
        radioGroup.check( radioButtonID);
        fromEditText.setText( savedInstanceState.getString("fromValue"));
        toEditText.setText( savedInstanceState.getString("toValue"));
        fromLabel.setText( savedInstanceState.getString("fromText"));
        toLabel.setText( savedInstanceState.getString("toText"));
        conversionHistory.setText( savedInstanceState.getString("conversionHistory"));
        converterValue = savedInstanceState.getDouble("converterValue");
        fromUnit = savedInstanceState.getString("fromUnit");
        toUnit = savedInstanceState.getString("toUnit");
    }

    // Calculate logic
    public void calculateConversion(View v){

        String fromText = fromEditText.getText().toString().trim();

        // Calculate only when input length is >0
        if( fromText.length() > 0 ){

            // Update result
            double fromValue = Double.parseDouble( fromText );
            String resultValue = resultFormat.format(fromValue * converterValue);
            toEditText.setText(resultValue);

            // Update conversion history
            String existingHist = conversionHistory.getText().toString();
            existingHist = (fromValue+" "+fromUnit+" ==> "+resultValue+" "+toUnit) + "\n"+existingHist;
            conversionHistory.setText( existingHist );

            // Reset Input Field
            fromEditText.setText("");
        }
    }

    // Clear Conversation History
    public void clearHistory(View v){
        conversionHistory.setText("");
    }

    // Radio Button Clicked
    public void radioClicked(View v){
        String selectionText = ((RadioButton) v).getText().toString();
        switch( selectionText ){
            case "Miles to Kilometers" :
                updateValues("Miles Value", "Kilometers Value", 1.60934, "Mi", "Km");
                break;
            case "Kilometers to Miles" :
                updateValues("Kilometers Value", "Miles Value", 0.621371, "Km", "Mi");
                break;
        }
    }

    // Function to switch between miles-km and km-mile
    public void updateValues( String fromText, String toText, double converterFactor, String from, String to){
            fromLabel.setText(fromText);
            toLabel.setText(toText);
            converterValue = converterFactor;
            fromUnit = from;
            toUnit = to;

            SharedPreferences.Editor prefsEditor = preferences.edit();
            prefsEditor.putString("conversionPref", from);
            prefsEditor.apply();
    }

}
