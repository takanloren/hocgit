package com.tungha.datn_timkiemnhatro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Classs.TinDKTB;

import static com.tungha.datn_timkiemnhatro.ListTinDK.arrTinCuaUser;
import static com.tungha.datn_timkiemnhatro.MainActivity.db;
import static com.tungha.datn_timkiemnhatro.MainActivity.mAuth;
import static com.tungha.datn_timkiemnhatro.R.color.common_google_signin_btn_text_dark_focused;
import static com.tungha.datn_timkiemnhatro.TrangChinh.arrTINTB;

public class FormXemTinDaDK extends AppCompatActivity {
    TextView txtngaythue,txtTP,txtQuan,txtDuong,txtGiatu,txtGiaden,txtDTtu,txtDTden,txtSophong,txtLoainha
            ,txtSonguoi,txtSoxe,txtHd;

    CheckBox chkchungchu,chknauan,chkbeprieng,chktoilet,chkdauxe,chkanninh,chkmaylanh,chkmaygiat,chksan
            ,chksanthuong,chktv,chkwifi;
    Cursor c;
    TinDKTB tin;
    Button btnXoaTin,btnThoat;
    ProgressDialog progressBar;
    ScrollView scrollV_TB;
    FirebaseUser user = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_xem_tin_da_dk);
        AnhXa();
        NhanDuLieu();
        XuLy();
    }

    private void XuLy() {
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnXoaTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XoaTin();
            }
        });
    }

    private void XoaTin() {
        Progress();
        for(int i=0;i<arrTinCuaUser.size();i++){
            if(arrTinCuaUser.get(i).getId().compareTo(tin.getId())==0){
                arrTinCuaUser.remove(i);
            }
        }
        Log.d("DCMM"," so tin con trong list : "+arrTinCuaUser.size());
            db.collection("DSTinDKNhanThongBao").document(user.getEmail()+"."+tin.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        if(arrTinCuaUser.size()==0) {
                            CapNhatLaiTTUser();
                        }else {
                            if(progressBar.isShowing())
                                progressBar.dismiss();
                            finish();
                        }
                    }
                }
            });
    }

    private void CapNhatLaiTTUser() {
        db.collection("User").document(user.getEmail()).update("daDKTin",false).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(FormXemTinDaDK.this, "Đã xóa tin đăng ký!", Toast.LENGTH_SHORT).show();
                    if(progressBar.isShowing())
                        progressBar.dismiss();
                    finish();
                }
            }
        });
    }

    public void Progress(){
        progressBar = new ProgressDialog(FormXemTinDaDK.this);
        progressBar.show();
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setContentView(R.layout.customdialog);

    }
    private void NhanDuLieu() {
        Intent i = getIntent();
        Bundle b = i.getBundleExtra("bundle");
        tin = (TinDKTB) b.getSerializable("tin");
        HienThiDuLieuLenForm();
        Log.d("DCMM","cc"+tin.isChungchu());
        Log.d("DCMM","bep"+tin.isBeprieng());
        Log.d("DCMM","bv"+tin.isAnninh());
        Log.d("DCMM","mg"+tin.isMaygiat());
        Log.d("DCMM","wjwj"+tin.isWifi());
        Log.d("DCMM","xe"+tin.isChodauxe());
        Log.d("DCMM","eat"+tin.isNauan());

    }

    private void HienThiDuLieuLenForm() {

        if(tin.getNgaynhan()!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String ngay = sdf.format(tin.getNgaynhan());
            txtngaythue.setText(ngay);
        }
        if(tin.getTP().toString().compareTo("")!=0){
            txtTP.setText(tin.getTP().toString());
        }
        if(tin.getQuan().toString().compareTo("")!=0){
            txtQuan.setText(tin.getQuan().toString());
        }
        if(tin.getDuong().toString().compareTo("")!=0){
            txtDuong.setText(tin.getDuong().toString());
        }
        if(tin.getGiatu()!=0){
            txtGiatu.setText(String.valueOf(tin.getGiatu()));
        }
        if(tin.getGiaden()!=0){
            txtGiaden.setText(String.valueOf(tin.getGiaden()));
        }
        if(tin.getDttu()!=0){
            txtDTtu.setText(String.valueOf(tin.getDttu()));
        }
        if(tin.getDtden()!=0){
            txtDTden.setText(String.valueOf(tin.getDtden()));
        }
        if(tin.getSophong()!=0){
            txtSophong.setText(String.valueOf(tin.getSophong()));
        }if(tin.getSonguoi()!=0){
            txtSonguoi.setText(String.valueOf(tin.getSonguoi()));
        }if(tin.getSoxe()!=0){
            txtSoxe.setText(String.valueOf(tin.getSoxe()));
        }if(tin.getHd()!=0){
            txtHd.setText(String.valueOf(tin.getHd()));
        }
        if(tin.getLoaiphong().toString().compareTo("")!=0){
            txtDuong.setText(tin.getLoaiphong().toString());
        }
        if(tin.isChungchu()){
            chkchungchu.setChecked(true);
            chkchungchu.setTextColor(Color.parseColor("#ff000000"));
        }
        if(tin.isNauan()){
            chknauan.setChecked(true);
            chknauan.setTextColor(Color.parseColor("#ff000000"));
        }
        if(tin.isSan()){
            chksan.setChecked(true);
            chksan.setTextColor(Color.parseColor("#ff000000"));
        }
        if(tin.isSanthuong()){
            chksanthuong.setChecked(true);
            chksanthuong.setTextColor(Color.parseColor("#ff000000"));
        }
        if(tin.isBeprieng()){
            chkbeprieng.setChecked(true);
            chkbeprieng.setTextColor(Color.parseColor("#ff000000"));
        }
        if(tin.isToiletrieng()){
            chktoilet.setChecked(true);
            chktoilet.setTextColor(Color.parseColor("#ff000000"));
        }
        if(tin.isTv()){
            chktv.setChecked(true);
            chktv.setTextColor(Color.parseColor("#ff000000"));
        }
        if(tin.isWifi()){
            chkwifi.setChecked(true);
            chkwifi.setTextColor(Color.parseColor("#ff000000"));
        }
        if(tin.isMaylanh()){
            chkmaylanh.setChecked(true);
            chkmaylanh.setTextColor(Color.parseColor("#ff000000"));
        }
        if(tin.isMaygiat()){
            chkmaygiat.setChecked(true);
            chkmaygiat.setTextColor(Color.parseColor("#ff000000"));
        }
        if(tin.isChodauxe()){
            chkdauxe.setChecked(true);
            chkdauxe.setTextColor(Color.parseColor("#ff000000"));
        }
        if(tin.isAnninh()){
            chkanninh.setChecked(true);
            chkanninh.setTextColor(Color.parseColor("#ff000000"));
        }
        chkchungchu.setEnabled(false);
        chknauan.setEnabled(false);
        chksan.setEnabled(false);
        chksanthuong.setEnabled(false);
        chkbeprieng.setEnabled(false);
        chktoilet.setEnabled(false);
        chktv.setEnabled(false);
        chkwifi.setEnabled(false);
        chkmaylanh.setEnabled(false);
        chkmaygiat.setEnabled(false);
        chkdauxe.setEnabled(false);
        chkanninh.setEnabled(false);
    }

    private void AnhXa() {
        txtngaythue= (TextView) findViewById(R.id.txtHienThiNgayThue_XemTin);
        txtTP=(TextView) findViewById(R.id.txtTP_XemTin);
        txtQuan=(TextView) findViewById(R.id.txtQuan_XemTin);
        txtDuong=(TextView) findViewById(R.id.txtDuong_XemTin);
        txtGiatu=(TextView) findViewById(R.id.txtGiaTu_XemTin);
        txtGiaden=(TextView) findViewById(R.id.txtGiaDen_XemTin);
        txtDTtu=(TextView) findViewById(R.id.txtDienTichTu_XemTin);
        txtDTden=(TextView) findViewById(R.id.txtDienTichDen_XemTin);
        txtSophong=(TextView) findViewById(R.id.txtSoPhong_XemTin);
        txtLoainha=(TextView) findViewById(R.id.txtLoaiPhong_XemTin);
        txtSonguoi=(TextView) findViewById(R.id.txtSLNguoi_XemTin);
        txtSoxe=(TextView) findViewById(R.id.txtSLXe_XemTin);
        txtHd=(TextView) findViewById(R.id.txtHD_XemTin);
        //////////////
        chkchungchu = (CheckBox) findViewById(R.id.chkChungChu_XemTin);
        chknauan= (CheckBox) findViewById(R.id.chkNauAn_XemTin);
        chkbeprieng= (CheckBox) findViewById(R.id.chkBepRieng_XemTin);
        chktoilet= (CheckBox) findViewById(R.id.chkToilet_XemTin);
        chkdauxe= (CheckBox) findViewById(R.id.chkDauXe_XemTin);
        chkanninh= (CheckBox) findViewById(R.id.chkAnNinh_XemTin);
        chkmaylanh= (CheckBox) findViewById(R.id.chkMayLanh_XemTin);
        chkmaygiat= (CheckBox) findViewById(R.id.chkMayGiat_XemTin);
        chksan= (CheckBox) findViewById(R.id.chkSan_XemTin);
        chksanthuong= (CheckBox) findViewById(R.id.chkSanThuong_XemTin);
        chktv= (CheckBox) findViewById(R.id.chkTV_XemTin);
        chkwifi= (CheckBox) findViewById(R.id.chkWifi_XemTin);
        //////
        btnXoaTin= (Button) findViewById(R.id.btnXoaTin_XemTin);
        btnThoat= (Button) findViewById(R.id.btnThoat_XemTin);
    }
}
