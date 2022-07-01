package com.caolambaokhanh.orderfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.caolambaokhanh.DAO.MonAnDAO;

public class SuaMonAnActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnDongYSuaMonAn;
    EditText edSuaTenMonAn, edSuaGiaMonAn;
    MonAnDAO monAnDAO;
    int mamon;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_suamonan);

        btnDongYSuaMonAn = (Button) findViewById(R.id.btnDongYSuaMonAn);
        edSuaTenMonAn = (EditText) findViewById(R.id.edSuaTenMonAn);
        edSuaGiaMonAn = (EditText) findViewById(R.id.edSuaGiaMonAn);

        monAnDAO = new MonAnDAO(this);

        mamon = getIntent().getIntExtra("mamon",0);

        btnDongYSuaMonAn.setOnClickListener(this);
    }
//
    @Override
    public void onClick(View v) {
        String tenmon = edSuaTenMonAn.getText().toString();
        String gia = edSuaGiaMonAn.getText().toString();
        if(!tenmon.trim().equals("")  || tenmon.trim() != null
                && !gia.trim().equals("")  || gia.trim() != null ){
            boolean kiemtra = monAnDAO.CapNhatLaiMonAn(mamon,tenmon,gia);
            Intent intent = new Intent();
            intent.putExtra("kiemtra", kiemtra);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }else {
            Toast.makeText(this, getResources().getString(R.string.vuilongnhapdulieu), Toast.LENGTH_SHORT).show();
        }
    }
}
