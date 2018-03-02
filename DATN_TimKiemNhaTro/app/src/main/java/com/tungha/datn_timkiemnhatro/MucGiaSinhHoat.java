package com.tungha.datn_timkiemnhatro;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import Classs.*;
import java.text.NumberFormat;
import java.util.Locale;

public class MucGiaSinhHoat extends AppCompatActivity {
    TextView txtnuoc,txtdien,txtxe,txtdondep,txtwifi,txttv,txtm3;
    EditText edtnuoc,edtdien,edtxe,edtdondep,edtwifi,edttv;
    int tnuoc,tdien,txe,tdondep,twifi,tivi;
    Button btnluu,btnsua,btnthoat;
    GiaTien giash = new GiaTien();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muc_gia_sinh_hoat);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        AnhXa();
        NhanDuLieuGiaSH();
        XuLy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void XuLy() {
        txtm3.setText(Html.fromHtml("VNĐ/m<sup>3</sup>"));
        btnthoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KhiBamSua();
                TruyenDuLieuKhiBamSua();
            }
        });
        btnluu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(KiemTraTT()==true) {
                    KhiBamLuu();
                    TruyenDuLieuKhiBamLuu();
                    Log.d("TEST","nuoc "+edtnuoc.getText());
                    GuiDuLieuGiaSHVeForm();
                }
            }
        });
        /*edtnuoc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    edtnuoc.setText("");
                }
            }
        });
        edtdien.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    edtdien.setText("");
                }
            }
        });
        edtxe.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    edtxe.setText("");
                }
            }
        });
        edtdondep.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    edtdondep.setText("");
                }
            }
        });
        edtwifi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    edtwifi.setText("");
                }
            }
        });
        edttv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    edttv.setText("");
                }
            }
        });*/
    }
    private void GuiDuLieuGiaSHVeForm(){
        Intent i = new Intent();
        Bundle b = new Bundle();
        giash = new GiaTien(tnuoc,tdien,txe,tdondep,twifi,tivi);
        b.putSerializable("GiaSHTraVe",giash);
        i.putExtra("data",b);
        setResult(12,i);
        finish();
    }
    private boolean KiemTraTT(){
        if(edtdondep.getText().toString().trim().length()==0){
            ThongBao("Vui lòng không bỏ trống thông tin");
            return false;
        }else if(edttv.getText().toString().trim().length()==0){
            ThongBao("Vui lòng không bỏ trống thông tin");
            return false;
        }else if(edtwifi.getText().toString().trim().length()==0){
            ThongBao("Vui lòng không bỏ trống thông tin");
            return false;
        }else if(edtnuoc.getText().toString().trim().length()==0){
            ThongBao("Vui lòng không bỏ trống thông tin");
            return false;
        }else if(edtxe.getText().toString().trim().length()==0){
            ThongBao("Vui lòng không bỏ trống thông tin");
            return false;
        }else if(edtdien.getText().toString().trim().length()==0){
            ThongBao("Vui lòng không bỏ trống thông tin");
            return false;
        }
        return true;
    }
    public void ThongBao(String thongbao){
        AlertDialog.Builder b = new AlertDialog.Builder(MucGiaSinhHoat.this);
        b.setMessage(thongbao);
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.create().show();
    }
    private void NhanDuLieuGiaSH(){
        Intent i = getIntent();
        Bundle b = i.getBundleExtra("bundle");
        giash = (GiaTien) b.getSerializable("giatiensh");
        String nuoc = NumberFormat.getNumberInstance(Locale.US).format(giash.getTienNuoc());
        String dien = NumberFormat.getNumberInstance(Locale.US).format(giash.getTienDien());
        String xe = NumberFormat.getNumberInstance(Locale.US).format(giash.getTienDauXe());
        String dondep = NumberFormat.getNumberInstance(Locale.US).format(giash.getTienDonDep());
        String wifi = NumberFormat.getNumberInstance(Locale.US).format(giash.getTienWifi());
        String tv = NumberFormat.getNumberInstance(Locale.US).format(giash.getTienTV());
        txtnuoc.setText(nuoc);
        txtdien.setText(dien);
        txtxe.setText(xe);
        txtdondep.setText(dondep);
        txtwifi.setText(wifi);
        txttv.setText(tv);

    }
    private void KhiBamSua(){
        txtnuoc.setVisibility(View.GONE);
        txtdien.setVisibility(View.GONE);
        txtxe.setVisibility(View.GONE);
        txtdondep.setVisibility(View.GONE);
        txtwifi.setVisibility(View.GONE);
        txttv.setVisibility(View.GONE);
        btnsua.setVisibility(View.GONE);

        edtnuoc.setVisibility(View.VISIBLE);
        edtdien.setVisibility(View.VISIBLE);
        edtxe.setVisibility(View.VISIBLE);
        edtdondep.setVisibility(View.VISIBLE);
        edtwifi.setVisibility(View.VISIBLE);
        edttv.setVisibility(View.VISIBLE);
        btnluu.setVisibility(View.VISIBLE);
    }
    private void KhiBamLuu(){
        txtnuoc.setVisibility(View.VISIBLE);
        txtdien.setVisibility(View.VISIBLE);
        txtxe.setVisibility(View.VISIBLE);
        txtdondep.setVisibility(View.VISIBLE);
        txtwifi.setVisibility(View.VISIBLE);
        txttv.setVisibility(View.VISIBLE);
        btnsua.setVisibility(View.VISIBLE);

        edtnuoc.setVisibility(View.GONE);
        edtdien.setVisibility(View.GONE);
        edtxe.setVisibility(View.GONE);
        edtdondep.setVisibility(View.GONE);
        edtwifi.setVisibility(View.GONE);
        edttv.setVisibility(View.GONE);
        btnluu.setVisibility(View.GONE);
    }
    private void TruyenDuLieuKhiBamLuu(){

        tnuoc= Integer.parseInt(edtnuoc.getText().toString());
        Log.d("TEST","nuoc sau khi parse "+tnuoc);
        tdien= Integer.parseInt(edtdien.getText().toString());
        txe= Integer.parseInt(edtxe.getText().toString());
        tdondep= Integer.parseInt(edtdondep.getText().toString());
        twifi= Integer.parseInt(edtwifi.getText().toString());
        tivi= Integer.parseInt(edttv.getText().toString());
        String nuoc = NumberFormat.getNumberInstance(Locale.US).format(tnuoc);
        String dien = NumberFormat.getNumberInstance(Locale.US).format(tdien);
        String xe = NumberFormat.getNumberInstance(Locale.US).format(txe);
        String dondep = NumberFormat.getNumberInstance(Locale.US).format(tdondep);
        String wifi = NumberFormat.getNumberInstance(Locale.US).format(twifi);
        String tv = NumberFormat.getNumberInstance(Locale.US).format(tivi);
        txtnuoc.setText(nuoc);
        txtdien.setText(dien);
        txtxe.setText(xe);
        txtdondep.setText(dondep);
        txtwifi.setText(wifi);
        txttv.setText(tv);
    }
    private void TruyenDuLieuKhiBamSua(){
        edtnuoc.setText(String.valueOf(giash.getTienNuoc()));
        edtdien.setText(String.valueOf(giash.getTienDien()));
        edtxe.setText(String.valueOf(giash.getTienDauXe()));
        edtdondep.setText(String.valueOf(giash.getTienDonDep()));
        edtwifi.setText(String.valueOf(giash.getTienWifi()));
        edttv.setText(String.valueOf(giash.getTienTV()));
    }
    private void AnhXa() {
        txtnuoc= (TextView) findViewById(R.id.txtTienNuoc_GiaSH);
        txtdien= (TextView) findViewById(R.id.txtTienDien_GiaSH);
        txtxe= (TextView) findViewById(R.id.txtTienDauXe_GiaSH);
        txtdondep= (TextView) findViewById(R.id.txtTienDonDep_GiaSH);
        txtwifi= (TextView) findViewById(R.id.txtTienWifi_GiaSH);
        txttv= (TextView) findViewById(R.id.txtTienTV_GiaSH);
        txtm3= (TextView) findViewById(R.id.txtm3_GiaSH);
        edtnuoc = (EditText) findViewById(R.id.edtTienNuoc_GiaSH);
        edtdien = (EditText) findViewById(R.id.edtTienDien_GiaSH);
        edtxe = (EditText) findViewById(R.id.edtTienDauXe_GiaSH);
        edtdondep = (EditText) findViewById(R.id.edtTienDonDep_GiaSH);
        edtwifi = (EditText) findViewById(R.id.edtTienWifi_GiaSH);
        edttv = (EditText) findViewById(R.id.edtTienTV_GiaSH);
        btnluu = (Button) findViewById(R.id.btnLuu_GiaSH);
        btnsua= (Button) findViewById(R.id.btnSua_GiaSH);
        btnthoat= (Button) findViewById(R.id.btnThoat_GiaSH);
    }
}
