package com.tungha.datn_timkiemnhatro;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
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

import static com.tungha.datn_timkiemnhatro.MainActivity.mAuth;

public class FormDoiMK extends AppCompatActivity {
    EditText edtmk,edtnhaplai;
    Button btnok,btnhuy;
    FirebaseUser user = mAuth.getCurrentUser();
    ProgressDialog progressBar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_doi_mk);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        AnhXa();
        XuLy();
    }

    private void XuLy() {
    btnok.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (user != null) {
                if (KiemTraThongTin() == true) {
                    Progress();
                    user.updatePassword(edtmk.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressBar.dismiss();
                                ThongBao("Đổi mật khẩu thành công!");
                            }
                        }
                    });
                }
            }
        }
    });
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Boolean KiemTraThongTin(){
        if(edtmk.getText().toString().trim().length()==0){
            ThongBao("Vui lòng không bỏ trống mật khẩu");
            return false;
        }else if(edtnhaplai.getText().toString().trim().length()==0){
            ThongBao("Vui lòng nhập lại mật khẩu phía trên");
            return false;
        }else if(edtmk.getText().toString().compareTo(edtnhaplai.getText().toString())!=0){
            ThongBao("Vui lòng nhập 2 lần mật khẩu giống nhau");
            return false;
        }else if(edtmk.getText().toString().trim().length()<6 || edtnhaplai.getText().toString().trim().length()<6){
            ThongBao("Mật khẩu có ít nhất 6 ký tự");
            return false;
        }
        return true;
    }
    public void Progress(){
        progressBar = new ProgressDialog(FormDoiMK.this);
        progressBar.show();
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setContentView(R.layout.customdialog);

    }
    public void ThongBao(String thongbao){
        AlertDialog.Builder b = new AlertDialog.Builder(FormDoiMK.this);
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
        edtmk= (EditText) findViewById(R.id.edtNhapMK_DoiMK);
        edtnhaplai= (EditText) findViewById(R.id.edtNhapLaiMK_DoiMK);
        btnok= (Button) findViewById(R.id.btnOK_DoiMK);
        btnhuy= (Button) findViewById(R.id.btnHuy_DoiMK);
    }
}
