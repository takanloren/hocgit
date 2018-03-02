package com.tungha.datn_timkiemnhatro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

import Classs.GiaTien;

public class GiaSH_Viewer extends AppCompatActivity {
    TextView txtnuoc,txtdien,txtxe,txtdondep,txtwifi,txttv,txtm3;
    Button btnthoat;
    GiaTien giash = new GiaTien();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gia_sh__viewer);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        AnhXa();
        XuLy();
        NhanDuLieu();
    }

    private void NhanDuLieu() {
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

    private void AnhXa() {
        txtnuoc= (TextView) findViewById(R.id.txtTienNuoc_GiaSHView);
        txtdien= (TextView) findViewById(R.id.txtTienDien_GiaSHView);
        txtxe= (TextView) findViewById(R.id.txtTienDauXe_GiaSHView);
        txtdondep= (TextView) findViewById(R.id.txtTienDonDep_GiaSHView);
        txtwifi= (TextView) findViewById(R.id.txtTienWifi_GiaSHView);
        txttv= (TextView) findViewById(R.id.txtTienTV_GiaSHView);
        txtm3= (TextView) findViewById(R.id.txtm3_GiaSHView);
        btnthoat= (Button) findViewById(R.id.btnThoat_GiaSHView);
        btnthoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void XuLy() {
        txtm3.setText(Html.fromHtml("VNĐ/m<sup>3</sup>"));
        btnthoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
