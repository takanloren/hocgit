package com.tungha.datn_timkiemnhatro;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.IDNA;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Date;
import java.util.UUID;

import Classs.Feedback;

import static com.tungha.datn_timkiemnhatro.MainActivity.db;
import static com.tungha.datn_timkiemnhatro.TrangChinh.InfoUser;
public class FormGuiFeedback extends AppCompatActivity {
    TextView txtmail,txtten;
    EditText edtchude,edtnoidung;
    Button btnok,btnhuy;
    ProgressDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_gui_feedback);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        AnhXa();
        XuLy();
    }

    private void XuLy() {
        txtmail.setText(InfoUser.getEmail().toString());
        txtten.setText(InfoUser.getHoTen().toString());
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(KiemTraThongTin()==true)
                GuiFeedBack();
            }
        });
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void GuiFeedBack() {
        Progress();
        String chude=edtchude.getText().toString();
        String noidung=edtnoidung.getText().toString();
        String id = UUID.randomUUID().toString();
        Feedback fb = new Feedback(id,InfoUser.getEmail(),InfoUser.getHoTen(),chude,noidung,new Date(),false,false);
        db.collection("DSPhanHoi").document(id).set(fb).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.dismiss();
                ThongBao("Xin cám ơn, phản hồi của bạn đã được gửi!");

            }
        });
    }
    private boolean KiemTraThongTin(){
        if(edtchude.getText().toString().trim().length()==0){
            ThongBao("Không bỏ trống chủ đề");
            return false;
        }else if(edtnoidung.getText().toString().trim().length()==0){
            ThongBao("Không bỏ trống nội dung");
            return false;
        }else if(edtchude.getText().toString().trim().length()<5){
            ThongBao("Chủ đề phải có ít nhất 5 ký tự");
            return false;
        }
        return true;
    }
    public void Progress(){
        progressBar = new ProgressDialog(FormGuiFeedback.this);
        progressBar.show();
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setContentView(R.layout.customdialog);

    }
    public void ThongBao(String thongbao){
        AlertDialog.Builder b = new AlertDialog.Builder(FormGuiFeedback.this);
        b.setMessage(thongbao);
        b.setPositiveButton("Biết rồi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        b.create().show();
    }
    private void AnhXa() {
        txtmail= (TextView) findViewById(R.id.txtMail_Feedback);
        txtten= (TextView) findViewById(R.id.txtHoTen_Feedback);
        edtchude= (EditText) findViewById(R.id.edtChuDe_Feedback);
        edtnoidung= (EditText) findViewById(R.id.edtNoiDung_Feedback);
        btnok= (Button) findViewById(R.id.btnGui_Feedback);
        btnhuy= (Button) findViewById(R.id.btnHuy_Feedback);
    }
}
