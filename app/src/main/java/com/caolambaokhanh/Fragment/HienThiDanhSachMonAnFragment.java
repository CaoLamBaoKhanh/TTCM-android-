package com.caolambaokhanh.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

import com.caolambaokhanh.CustomAdapter.AdapterHienThiDanhSachMonAn;
import com.caolambaokhanh.DAO.MonAnDAO;
import com.caolambaokhanh.DTO.MonAnDTO;
import com.caolambaokhanh.orderfood.R;
import com.caolambaokhanh.orderfood.SoLuongActivity;
import com.caolambaokhanh.orderfood.SuaMonAnActivity;

import java.util.List;

public class HienThiDanhSachMonAnFragment extends Fragment {
    GridView gridView;
    MonAnDAO monAnDAO;
    List<MonAnDTO> monAnDTOList;
    AdapterHienThiDanhSachMonAn adapterHienThiDanhSachMonAn;
    int maban;
    public static int REQUEST_CODE_SUA = 444;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SUA){
            if(resultCode == Activity.RESULT_OK){

            }
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        //lấy vị trí bằng menuinfo
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        int mamon = monAnDTOList.get(vitri).getMaMonAn();
        switch (id){
            case R.id.itSua:
                Intent intent = new Intent(getActivity(), SuaMonAnActivity.class);
                intent.putExtra("mamon",mamon);
                startActivityForResult(intent,REQUEST_CODE_SUA);

                Toast.makeText(getActivity(), "mã món " +mamon, Toast.LENGTH_SHORT).show();
                ;break;

            case R.id.itXoa:
                boolean kiemtra = monAnDAO.XoaMonAnTheoMa(mamon);
                if(kiemtra){
                    HienThiDanhSachMonAn();
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.xoathanhcong), Toast.LENGTH_SHORT).show();
                }else {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_hienthithucdon, container, false);

        gridView = (GridView) view.findViewById(R.id.gvHIenThiThucDon);

        monAnDAO = new MonAnDAO(getActivity());
        //dangky contextmenu
        registerForContextMenu(gridView);
        HienThiDanhSachMonAn();

        Bundle bundle = getArguments();
        if(bundle !=  null){
            int maloai = bundle.getInt("maloai");
            maban = bundle.getInt("maban");

            monAnDTOList = monAnDAO.LayDanhSachMonAnTheoLoai(maloai);

            adapterHienThiDanhSachMonAn = new AdapterHienThiDanhSachMonAn(getActivity(),R.layout.custom_layout_hienthidanhsachmonan,monAnDTOList);
            gridView.setAdapter(adapterHienThiDanhSachMonAn);
            adapterHienThiDanhSachMonAn.notifyDataSetChanged();

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(maban !=0 ){
                        Intent iSoLuong = new Intent(getActivity(), SoLuongActivity.class);
                        iSoLuong.putExtra("maban",maban);
                        iSoLuong.putExtra("mamonan",monAnDTOList.get(position).getMaMonAn());

                        startActivity(iSoLuong);
                    }

                }
            });

        }

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    getFragmentManager().popBackStack("hienthiloai", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                return false;
            }
        });
        return view;
    }

    public void HienThiDanhSachMonAn(){
        monAnDTOList = monAnDAO.LayTatCaMonAn();
        adapterHienThiDanhSachMonAn = new AdapterHienThiDanhSachMonAn(getActivity(),R.layout.custom_layout_hienthidanhsachmonan,monAnDTOList);
        gridView.setAdapter(adapterHienThiDanhSachMonAn);
        adapterHienThiDanhSachMonAn.notifyDataSetChanged();
    }
}
