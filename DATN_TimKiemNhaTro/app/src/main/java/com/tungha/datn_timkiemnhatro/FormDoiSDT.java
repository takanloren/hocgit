package com.tungha.datn_timkiemnhatro;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Classs.Nha;

import static com.tungha.datn_timkiemnhatro.MainActivity.db;
import static com.tungha.datn_timkiemnhatro.MainActivity.mAuth;
import static com.tungha.datn_timkiemnhatro.TrangChinh.InfoUser;
import static com.tungha.datn_timkiemnhatro.TrangChinh.arrBLglobal;
import static com.tungha.datn_timkiemnhatro.TrangChinh.arrDGglobal;
import static com.tungha.datn_timkiemnhatro.TrangChinh.arrNha;

public class FormDoiSDT extends AppCompatActivity {
    EditText edtsdt;
    Button btnok,btnhuy;
    String sdt;
    ArrayList<Nha> arrNhaCoUser = new ArrayList<>();
    FirebaseUser user = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_doi_sdt);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        AnhXa();
        LoadDSBaiCoUser();
    }

    private void AnhXa() {
        edtsdt= (EditText) findViewById(R.id.edtSDTMoi_DoiSDT);
        btnok= (Button) findViewById(R.id.btnOK_DoiSDT);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("User").whereEqualTo("soDienThoai",edtsdt.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().getDocuments().size()!=0){
                                ThongBao("Số điện thoại này đã được đăng ký trong hệ thống");
                            }else {
                                if(KiemTraThongTin()==true){
                                    sdt=edtsdt.getText().toString();
                                    UpDateUserTungNha();
                                    UpNhaLenFBLai();
                                    UpdateUser();
                                    UpdateBL();
                                    UpdateDG();
                                    GuiDataVe();
                                }
                            }
                        }
                    }
                });
            }
        });
        btnhuy = (Button) findViewById(R.id.btnHuy_DoiSDT);
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void UpdateUser() {
        db.collection("User").document(InfoUser.getEmail()).update("soDienThoai",sdt);
    }

    private boolean KiemTraThongTin() {
        if (edtsdt.getText().toString().trim().length() == 0) {
            ThongBao("Vui lòng nhập số điện thoại");
            edtsdt.requestFocus();
            return false;
        } else if (edtsdt.getText().toString().trim().length() < 10) {
            ThongBao("Số điện thoại phải gồm 10 hoặc 11 số");
            edtsdt.requestFocus();
            return false;
        }else {

        }
        return true;
    }
    private void UpdateBL(){
        for(int i=0;i<arrBLglobal.size();i++){
            db.collection("DSBinhLuan").document(arrBLglobal.get(i).getId()).update("user.soDienThoai",sdt);
        }
    }
    private void UpdateDG(){
        for(int i=0;i<arrNha.size();i++){
            db.collection("DSDanhGiaChiTiet").document(user.getEmail()+"."+arrNha.get(i).getId()).update("user.soDienThoai",sdt);
        }
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
            arrNhaCoUser.get(i).getUser().setSoDienThoai(sdt);
        }
    }
    private void UpNhaLenFBLai(){
        for(int i=0;i<arrNhaCoUser.size();i++) {
            db.collection("Nha").document(arrNhaCoUser.get(i).getId()).set(arrNhaCoUser.get(i));
        }
    }
    public void ThongBao(String thongbao){
        AlertDialog.Builder b = new AlertDialog.Builder(FormDoiSDT.this);
        b.setMessage(thongbao);
        b.setPositiveButton("Biết rồi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.create().show();
    }
    private void GuiDataVe(){
        Intent i = new Intent();
        i.putExtra("sdt",sdt);
        setResult(123123123,i);
        finish();
    }
}
