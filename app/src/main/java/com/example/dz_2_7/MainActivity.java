package com.example.dz_2_7;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    private  static double startSum;
    @SuppressLint("StaticFieldLeak")
    private  static  EditText editSum;
    private  static double sumEnd;
    private static double startCurrencyWeght;   //вес валюты №1
    private static double  finishCurrencyWeght;
    private static double correctFinishWeght;   //скоррект. вес валюты №2 (в завис. от радиобат)
    private static double tabloCourse;      //курс на табло (относит. разн. веса валют)
    private static double volatileCurrency; //служеб. велич. для расчета разброса ставок
    private static Currency itemStart;      //объект валюты №1
    private static Currency itemFinish;     //объект валюты №2

    @SuppressLint("StaticFieldLeak")
    private  static TextView textViewCourse;
    @SuppressLint("StaticFieldLeak")
    private static  TextView textViewResult;

    private List<Currency> currencies = new ArrayList<Currency>(){
        {
            add(new Currency("USD", 1, 0));
            add(new Currency("Euro", 0.89, 0.0015));
            add(new Currency("JPY", 108.24, 0.0031));
            add(new Currency("RUB", 65.29, 0.0044));
            add(new Currency("BYN", 2.10, 0.0033));
            add(new Currency("KZT", 384.04, 0.0055));
            add(new Currency("GBP", 0.79, 0.0011));
        }
    };
    private RadioButton rbtnNationBank;
    private RadioButton rbtnPurchase;
    private RadioButton rbtnBuy;

    private  final static String TAG = "===MainActivity=== ";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editSum = findViewById(R.id.edtSum);
        textViewCourse = findViewById(R.id.tvCourse);
        textViewResult = findViewById(R.id.tvExit_sum);

        Log.d(TAG, "sumEnd: "+sumEnd); //знач. сохран.!!!!
        textViewResult.setText(String.valueOf(sumEnd)); //но, почему нужно отдельно вписывать сохран.?

        Spinner spinnerStartCurrency = findViewById(R.id.currency_start_spinner);
        Spinner spinnerFinishCurrency = findViewById(R.id.currency_finish_spinner);

        ArrayAdapter<Currency> adapter = new ArrayAdapter<>(this, R.layout.spinner_currency, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartCurrency.setAdapter(adapter);
        spinnerFinishCurrency.setAdapter(adapter);

        rbtnNationBank = findViewById(R.id.rbtn_nation_bank);
        rbtnPurchase = findViewById(R.id.rbtn_purchase);
        rbtnBuy = findViewById(R.id.rbtn_buy);

        MyCheckedChangeListener listener = new MyCheckedChangeListener();
        rbtnNationBank.setOnCheckedChangeListener(listener);
        rbtnPurchase.setOnCheckedChangeListener(listener);
        rbtnBuy.setOnCheckedChangeListener(listener);

//обработка 1-го спинера - валюта №1
        spinnerStartCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                itemStart = (Currency)parent.getItemAtPosition(position);
                startCurrencyWeght = itemStart.getPriceWeight();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
  //обработка 2-го спинера - валюта №2
        spinnerFinishCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                itemFinish = (Currency)parent.getItemAtPosition(position);
                volatileCurrency = itemFinish.getVolatil(); //волатильность
                finishCurrencyWeght = itemFinish.getPriceWeight();  //вытаскив. веса конечн. валюты
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
//кнопка-расчитать цену валюты в завис. от радиобат. (c использ. временн. знач.)
    public void btnClick(View view) {
        try{
            startSum = Double.parseDouble(editSum.getText().toString());
        }catch (Exception e){
            editSum.setText("");
        }
        sumEnd = startSum * correctFinishWeght/startCurrencyWeght;

        sumEnd = Math.floor(sumEnd*100)/100.0;
        textViewResult.setText(String.valueOf(sumEnd));
    }

//обработч. радиобат.-различ. удель. вес
    class MyCheckedChangeListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked) {
                if (buttonView.getId() == rbtnNationBank.getId()) {
                    correctFinishWeght = finishCurrencyWeght;
                    Log.d(TAG, "onCheck/correctFinishWeight: " + correctFinishWeght); //384,04!
                }
                else if (buttonView.getId() == rbtnPurchase.getId())
                    correctFinishWeght = finishCurrencyWeght - finishCurrencyWeght * volatileCurrency;

                else
                    correctFinishWeght = finishCurrencyWeght + finishCurrencyWeght * volatileCurrency;
            }
//вывести курс на табло (курс расчит. из отнош. абс. веса валют) - вес 1-ой валюты может быть != 1
            tabloCourse = Math.floor(correctFinishWeght/startCurrencyWeght*100)/100.0;
            textViewCourse.setText(String.valueOf(tabloCourse));
        }
    }
}
