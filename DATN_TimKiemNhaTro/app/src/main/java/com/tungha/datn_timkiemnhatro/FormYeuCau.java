package com.tungha.datn_timkiemnhatro;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import Classs.YeuCauNCT;

public class FormYeuCau extends AppCompatActivity {
    int slnguoi,slxe,hopdong;
    Boolean nauan,checkdanhapyeucau=false;
    String giogiac;
    EditText edtslnguoi,edtslxe,edttime,edthopdong;
    RadioGroup rdoG_cook,rdoG_contact;
    RadioButton rdoDCNA,rdoKONA,rdoHD,rdoKOHD;
    Button btnhuy,btnluu;
    YeuCauNCT yeucau;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_yeu_cau);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        AnhXa();
        XuLy();
    }

    private void XuLy() {
        NhanDuLieuTuFormNCT();
        KhiClickLuu();
    }

    private void KhiClickLuu() {
        btnluu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(KiemTraThongTin()==true) {
                    checkdanhapyeucau=true;
                    GetDuLieuTuForm();
                    GuiDuLieuVeFormNCT();
                }
            }
        });
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edtslnguoi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    edtslnguoi.setText("");
            }
        });
        edtslxe.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    edtslxe.setText("");
            }
        });
        edttime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    edttime.setText("");
            }
        });
        edthopdong.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    edthopdong.setText("");
            }
        });

    }

    private void GuiDuLieuVeFormNCT() {
        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putSerializable("yeucau_trave",yeucau);
        b.putBoolean("checkdanhapyeucau",checkdanhapyeucau);
        i.putExtra("data",b);
        setResult(33,i);
        finish();
    }
    private Boolean KiemTraThongTin(){
        if(edtslnguoi.getText().toString().trim().length()==0){
            ThongBao("Vui lòng nhập số lượng người tối đa");
            edtslnguoi.requestFocus();
            return false;
        }else if(Integer.parseInt(edtslnguoi.getText().toString())<=0){
            ThongBao("Số lượng người phải lớn hơn 0");
            edtslnguoi.requestFocus();
            return false;
        }else if(edtslxe.getText().toString().trim().length()==0){
            ThongBao("Vui lòng nhập số lượng xe tối đa");
            edtslxe.requestFocus();
            return false;
        }else if(edttime.getText().toString().trim().length()==0){
            ThongBao("Vui lòng điền quy định giờ giấc");
            edttime.requestFocus();
            return false;
        }else if(edthopdong.getText().toString().trim().length()==0){
            ThongBao("Vui lòng nhập số tháng làm hợp đồng");
            edthopdong.requestFocus();
            return false;
        }else if(Integer.parseInt(edthopdong.getText().toString())<=0){
            ThongBao("Số tháng làm hợp đồng phải lớn hơn 0");
            edthopdong.requestFocus();
            return false;
        }
        return true;
    }
    private void GetDuLieuTuForm() {
        slnguoi= Integer.parseInt(edtslnguoi.getText().toString());
        slxe= Integer.parseInt(edtslxe.getText().toString());
        giogiac=edttime.getText().toString();
        if(rdoDCNA.isChecked()){
            nauan=true;
        }else {
            nauan=false;
        }
        hopdong=Integer.parseInt(edthopdong.getText().toString());
        yeucau=new YeuCauNCT(slnguoi,slxe,giogiac,nauan,hopdong);
    }

    private void NhanDuLieuTuFormNCT() {
        Intent i = getIntent();
        Bundle b = i.getBundleExtra("bundle");
        yeucau = (YeuCauNCT) b.getSerializable("yeucau");
        edtslnguoi.setText(String.valueOf(yeucau.getSoLuongNguoi()));
        edtslxe.setText(String.valueOf(yeucau.getSoLuongXe()));
        edttime.setText(yeucau.getGioGiac().toString());
        edthopdong.setText(String.valueOf(yeucau.getHopDong()));
        if(yeucau.getChoNauAn()==true){
            rdoDCNA.setChecked(true);
        }else {
            rdoKONA.setChecked(true);
        }

    }
    public void ThongBao(String thongbao){
        AlertDialog.Builder b = new AlertDialog.Builder(FormYeuCau.this);
        b.setMessage(thongbao);
        b.setPositiveButton("Biết rồi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.create().show();
    }
    private void AnhXa() {
        edtslnguoi= (EditText) findViewById(R.id.edtSLNguoi_FormYC);
        edtslxe= (EditText) findViewById(R.id.edtSLXe_FormYC);
        edttime= (EditText) findViewById(R.id.edtGioGiac_FormYC);
        rdoG_cook= (RadioGroup) findViewById(R.id.rdoG_ChoNauAn_FormYC);
        rdoDCNA= (RadioButton) findViewById(R.id.rdoDuocNA_FormYC);
        rdoKONA= (RadioButton) findViewById(R.id.rdoKONA_FormYC);
        btnhuy= (Button) findViewById(R.id.btnHuy_FormYC);
        btnluu= (Button) findViewById(R.id.btnLuu_FormYC);
        edthopdong= (EditText) findViewById(R.id.edtHopDong_FormYC);
    }
}
