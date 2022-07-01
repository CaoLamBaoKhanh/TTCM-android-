package com.caolambaokhanh.orderfood;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SuaMonAnActivity extends AppCompatActivity {

    Button btnDongYSuaMonAn;
    TextView txtSuaTenMonAn, txtSuaGiaMonAn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_suamonan);

//        btnDongYSuaMonAn = (Button) findViewById(R.id.btnDongYSuaMonAn);

        int mamon = getIntent().getIntExtra("mamon",0);


    }
}
