package com.caolambaokhanh.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.caolambaokhanh.CustomAdapter.AdapterHienThiLoaiMonAnThucDon;
import com.caolambaokhanh.DAO.LoaiMonAnDAO;
import com.caolambaokhanh.DTO.LoaiMonAnDTO;
import com.caolambaokhanh.orderfood.R;
import com.caolambaokhanh.orderfood.SuaLoaiThucDonActivity;
import com.caolambaokhanh.orderfood.ThemBanAnActivity;
import com.caolambaokhanh.orderfood.ThemThucDonActivity;
import com.caolambaokhanh.orderfood.TrangChuActivity;

import java.util.List;

public class HienThiThucDonFragment extends Fragment {

    GridView gridView;
    List<LoaiMonAnDTO> loaiMonAnDTOs;
    LoaiMonAnDAO loaiMonAnDAO;
    FragmentManager fragmentManager;
    int maban;
    int maquyen;
    SharedPreferences sharedPreferences;
    AdapterHienThiLoaiMonAnThucDon adapterHienThiLoaiMonAnThucDon;
    public static int REQUEST_CODE_SUA = 555;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_hienthithucdon,container,false);
        setHasOptionsMenu(true);
        ((TrangChuActivity)getActivity()).getSupportActionBar().setTitle(R.string.thucdon);

        gridView = (GridView) view.findViewById(R.id.gvHIenThiThucDon);

        fragmentManager = getActivity().getSupportFragmentManager();

        loaiMonAnDAO = new LoaiMonAnDAO(getActivity());
        loaiMonAnDTOs = loaiMonAnDAO.LayDanhSachLoaiMonAn();

        sharedPreferences = getActivity().getSharedPreferences("luuquyen", Context.MODE_PRIVATE);
        maquyen = sharedPreferences.getInt("maquyen",0);
        if(maquyen == 1){
            registerForContextMenu(gridView);
        }

        AdapterHienThiLoaiMonAnThucDon adapdater = new AdapterHienThiLoaiMonAnThucDon(getActivity(),R.layout.custom_layout_hienthiloaimonan,loaiMonAnDTOs);
        gridView.setAdapter(adapdater);
        adapdater.notifyDataSetChanged();

        Bundle bDuLieuThucDon = getArguments();
        if(bDuLieuThucDon != null){
            maban = bDuLieuThucDon.getInt("maban");

        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int maloai = loaiMonAnDTOs.get(position).getMaLoai();

                HienThiDanhSachMonAnFragment hienThiDanhSachMonAnFragment = new HienThiDanhSachMonAnFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("maloai", maloai);
                bundle.putInt("maban",maban);
                hienThiDanhSachMonAnFragment.setArguments(bundle);

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content, hienThiDanhSachMonAnFragment).addToBackStack("hienthiloai");

                transaction.commit();
            }
        });



        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(maquyen == 1){
            MenuItem itThemBanAn = menu.add(1,R.id.itThemThucDon,1,R.string.themthucdon);
            itThemBanAn.setIcon(R.drawable.logodangnhap);
            itThemBanAn.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SUA){
            if(resultCode == Activity.RESULT_OK){
                Intent intent = data;
                boolean kiemtra = intent.getBooleanExtra("kiemtra",false);
                HienThiDanhSachLoaiMonAn();
                if(kiemtra){
                    Toast.makeText(getActivity(), getResources().getString(R.string.suathanhcong), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.loi), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        HienThiDanhSachLoaiMonAn();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        //lay vi trí
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        int maloai = loaiMonAnDTOs.get(vitri).getMaLoai();
        switch (id){
            case R.id.itSua:
                Intent intent = new Intent(getActivity(), SuaLoaiThucDonActivity.class);
                intent.putExtra("maloai",maloai);
                startActivityForResult(intent,REQUEST_CODE_SUA);

                ;break;
            case R.id.itXoa:
                boolean kiemtra = loaiMonAnDAO.XoaLoaiMonAnTheoMaLoai(maloai);
                if(kiemtra){
                    HienThiDanhSachLoaiMonAn();
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.xoathanhcong), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.loi), Toast.LENGTH_SHORT).show();
                }
                ;break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itThemThucDon:
                Intent iThemThucDon = new Intent(getActivity(), ThemThucDonActivity.class);
                startActivity(iThemThucDon);
                getActivity().overridePendingTransition(R.anim.hieuung_activity_vao,R.anim.hieuung_activity_ra);
                ;break;

        }
        return super.onOptionsItemSelected(item);
    }
    public void HienThiDanhSachLoaiMonAn(){
        loaiMonAnDTOs = loaiMonAnDAO.LayDanhSachLoaiMonAn();
        adapterHienThiLoaiMonAnThucDon = new AdapterHienThiLoaiMonAnThucDon(getActivity(),R.layout.custom_layout_hienthiloaimonan,loaiMonAnDTOs);
        gridView.setAdapter(adapterHienThiLoaiMonAnThucDon);
        adapterHienThiLoaiMonAnThucDon.notifyDataSetChanged();
    }
}
