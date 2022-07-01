package com.caolambaokhanh.orderfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.caolambaokhanh.DAO.LoaiMonAnDAO;

public class SuaLoaiThucDonActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnDongYSuaLoaiThucDon;
    EditText edTenLoaiThucDon;
    LoaiMonAnDAO loaiMonAnDAO;
    int maloai;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_suathucdon);

        btnDongYSuaLoaiThucDon = findViewById(R.id.btnDongYSuaThucDon);
        edTenLoaiThucDon = findViewById(R.id.edSuaTenLoaiThucDon);
        loaiMonAnDAO = new LoaiMonAnDAO(this);
        maloai = getIntent().getIntExtra("maloai",0);
        btnDongYSuaLoaiThucDon.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        String tenloaithucdon = edTenLoaiThucDon.getText().toString();
        if(!tenloaithucdon.trim().equals("") || tenloaithucdon.trim() != null){
            boolean kiemtra = loaiMonAnDAO.CapNhatLaiLoaiMonAn(maloai,tenloaithucdon);
            Intent intent = new Intent();
            intent.putExtra("kiemtra", kiemtra);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }else {
            Toast.makeText(this, getResources().getString(R.string.vuilongnhapdulieu), Toast.LENGTH_SHORT).show();
        }
    }
}
