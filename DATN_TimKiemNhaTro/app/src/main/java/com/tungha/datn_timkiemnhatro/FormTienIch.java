package com.tungha.datn_timkiemnhatro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import Classs.TienIchNCT;

public class FormTienIch extends AppCompatActivity {
    RadioButton rdoCO_bep,rdoCO_toilet,rdoCO_nhaxe,rdoCO_bv,rdoCO_maylanh,rdoCO_maygiat,rdoCO_santhuong,rdoCO_san,rdoCO_captv,rdoCO_wifi,
            rdoKO_bep,rdoKO_toilet,rdoKO_nhaxe,rdoKO_bv,rdoKO_maylanh,rdoKO_maygiat,rdoKO_santhuong,rdoKO_san,rdoKO_captv,rdoKO_wifi;
    Button btnLuu,btnDong;
    Boolean bep=false,toilet=false,nhaxe=false,bv=false,maylanh=false,maygiat=false,santhuong=false,san=false,captv=false,wifi=false;
    TienIchNCT tienich;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_tien_ich);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        AnhXa();
        XuLy();
    }

    private void XuLy() {
        SetALLRadioButtonToKO();
        NhanDuLieuTuFormNCT();
        KhiClickLuu();
    }

    private void KhiClickLuu() {
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDuLieuTuForm();
                GuiDuLieuVeFormNCT();
            }
        });
        btnDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void GuiDuLieuVeFormNCT() {
        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putSerializable("tienich_trave",tienich);
        i.putExtra("data",b);
        setResult(44,i);
        finish();
    }

    private void GetDuLieuTuForm() {
        if(rdoCO_bep.isChecked()){
            bep=true;
        }
        if(rdoCO_toilet.isChecked()){
            toilet=true;
        }
        if(rdoCO_nhaxe.isChecked()){
            nhaxe=true;
        }
        if(rdoCO_bv.isChecked()){
            bv=true;
        }
        if(rdoCO_maylanh.isChecked()){
            maylanh=true;
        }
        if(rdoCO_maygiat.isChecked()){
            maygiat=true;
        }
        if(rdoCO_santhuong.isChecked()){
            santhuong=true;
        }
        if(rdoCO_san.isChecked()){
            san=true;
        }
        if(rdoCO_captv.isChecked()){
            captv=true;
        }
        if(rdoCO_wifi.isChecked()){
            wifi=true;
        }
        tienich=new TienIchNCT(bep,toilet,nhaxe,bv,maylanh,maygiat,san,captv,wifi,santhuong);

    }

    private void SetALLRadioButtonToKO() {
        rdoKO_bep.setChecked(true);
        rdoKO_toilet.setChecked(true);
        rdoKO_nhaxe.setChecked(true);
        rdoKO_bv.setChecked(true);
        rdoKO_maylanh.setChecked(true);
        rdoKO_maygiat.setChecked(true);
        rdoKO_santhuong.setChecked(true);
        rdoKO_san.setChecked(true);
        rdoKO_captv.setChecked(true);
        rdoKO_wifi.setChecked(true);
    }
    private void NhanDuLieuTuFormNCT() {
        Intent i = getIntent();
        Bundle b = i.getBundleExtra("bundle");
        tienich = (TienIchNCT) b.getSerializable("tienich");

        if(tienich.getBepNauAn()==true){
            rdoCO_bep.setChecked(true);
        }
        if(tienich.getNhaVSRieng()==true){
            rdoCO_toilet.setChecked(true);
        }
        if(tienich.getChoDeXe()==true){
            rdoCO_toilet.setChecked(true);
        }
        if(tienich.getBaoVe()==true){
            rdoCO_bv.setChecked(true);
        }
        if(tienich.getMayLanh()==true){
            rdoCO_maylanh.setChecked(true);
        }
        if(tienich.getMayGiat()==true){
            rdoCO_maygiat.setChecked(true);
        }
        if(tienich.getSanThuong()==true){
            rdoCO_santhuong.setChecked(true);
        }
        if(tienich.getSanPhoiDo()==true){
            rdoCO_san.setChecked(true);
        }
        if(tienich.getTruyenHinhCap()==true){
            rdoCO_captv.setChecked(true);
        }
        if(tienich.getWifi()==true){
            rdoCO_wifi.setChecked(true);
        }
    }

    private void AnhXa() {
        btnLuu= (Button) findViewById(R.id.btnLuu_TI);
        btnDong= (Button) findViewById(R.id.btnDong_TI);

        rdoCO_bep = (RadioButton) findViewById(R.id.rdoCo_Bep_TI);
        rdoCO_toilet = (RadioButton) findViewById(R.id.rdoCo_toilet_TI);
        rdoCO_nhaxe = (RadioButton) findViewById(R.id.rdoCo_NhaXe_TI);
        rdoCO_bv = (RadioButton) findViewById(R.id.rdoCo_BV_TI);
        rdoCO_maylanh = (RadioButton) findViewById(R.id.rdoCo_MayLanh_TI);
        rdoCO_maygiat = (RadioButton) findViewById(R.id.rdoCo_MayGiat_TI);
        rdoCO_santhuong = (RadioButton) findViewById(R.id.rdoCo_SThuong_TI);
        rdoCO_san = (RadioButton) findViewById(R.id.rdoCo_San_TI);
        rdoCO_captv = (RadioButton) findViewById(R.id.rdoCo_CapTV_TI);
        rdoCO_wifi = (RadioButton) findViewById(R.id.rdoCo_Wifi_TI);

        rdoKO_bep = (RadioButton) findViewById(R.id.rdoKO_Bep_TI);
        rdoKO_toilet = (RadioButton) findViewById(R.id.rdoKO_toilet_TI);
        rdoKO_nhaxe = (RadioButton) findViewById(R.id.rdoKO_NhaXe_TI);
        rdoKO_bv = (RadioButton) findViewById(R.id.rdoKO_BV_TI);
        rdoKO_maylanh = (RadioButton) findViewById(R.id.rdoKO_MayLanh_TI);
        rdoKO_maygiat = (RadioButton) findViewById(R.id.rdoKO_MayGiat_TI);
        rdoKO_santhuong = (RadioButton) findViewById(R.id.rdoKO_SThuong_TI);
        rdoKO_san = (RadioButton) findViewById(R.id.rdoKO_San_TI);
        rdoKO_captv = (RadioButton) findViewById(R.id.rdoKO_CapTV_TI);
        rdoKO_wifi = (RadioButton) findViewById(R.id.rdoKO_Wifi_TI);


    }
}
