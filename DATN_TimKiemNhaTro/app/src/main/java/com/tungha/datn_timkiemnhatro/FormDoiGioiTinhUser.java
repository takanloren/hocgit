package com.tungha.datn_timkiemnhatro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import Classs.Nha;

import static com.tungha.datn_timkiemnhatro.MainActivity.db;
import static com.tungha.datn_timkiemnhatro.MainActivity.mAuth;
import static com.tungha.datn_timkiemnhatro.TrangChinh.InfoUser;
import static com.tungha.datn_timkiemnhatro.TrangChinh.arrBLglobal;
import static com.tungha.datn_timkiemnhatro.TrangChinh.arrDGglobal;
import static com.tungha.datn_timkiemnhatro.TrangChinh.arrNha;

public class FormDoiGioiTinhUser extends AppCompatActivity {
    RadioButton rdonam,rdonu;
    Button btnok,btnhuy;
    boolean gioitinh;
    ArrayList<Nha> arrNhaCoUser = new ArrayList<>();
    FirebaseUser user = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_doi_gioi_tinh_user);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        AnhXa();
        LoadDSBaiCoUser();
        XuLy();
    }

    private void LoadDSBaiCoUser() {
        for(int i=0;i<arrNha.size();i++){
            if(arrNha.get(i).getUser().getEmail().compareTo(InfoUser.getEmail())==0){
                arrNhaCoUser.add(arrNha.get(i));
            }
        }
    }
    private void UpDateUserTungNha(){
        for (int i=0;i<arrNhaCoUser.size();i++){
            arrNhaCoUser.get(i).getUser().setGioiTinh(gioitinh);
        }
    }
    private void UpNhaLenFBLai(){
        for(int i=0;i<arrNhaCoUser.size();i++) {
            db.collection("Nha").document(arrNhaCoUser.get(i).getId()).set(arrNhaCoUser.get(i));
        }
    }
    private void UpdateBL(){
        for(int i=0;i<arrBLglobal.size();i++){
            db.collection("DSBinhLuan").document(arrBLglobal.get(i).getId()).update("user.gioiTinh",gioitinh);
        }
    }
    private void UpdateDG(){
        for(int i=0;i<arrNha.size();i++){
            db.collection("DSDanhGiaChiTiet").document(user.getEmail()+"."+arrNha.get(i).getId()).update("user.gioiTinh",gioitinh);
        }
    }
    private void XuLy() {
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rdonam.isChecked())
                    gioitinh=true;
                else
                    gioitinh=false;
                db.collection("User").document(InfoUser.getEmail()).update(
                        "gioiTinh",gioitinh
                );
                UpDateUserTungNha();
                UpNhaLenFBLai();
                UpdateBL();
                UpdateDG();
                GuiDuLieuVeTC();
            }
        });
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void GuiDuLieuVeTC() {
        Intent i = new Intent();
        i.putExtra("gioitinh",gioitinh);
        setResult(222,i);
        finish();
    }

    private void AnhXa() {
        rdonam= (RadioButton) findViewById(R.id.rdoNam_DoiGT);
        rdonam.setChecked(true);
        rdonu= (RadioButton) findViewById(R.id.rdoNu_DoiGT);
        btnok = (Button) findViewById(R.id.btnOK_DoiGT);
        btnhuy= (Button) findViewById(R.id.btnHuy_DoiGT);
    }
}
