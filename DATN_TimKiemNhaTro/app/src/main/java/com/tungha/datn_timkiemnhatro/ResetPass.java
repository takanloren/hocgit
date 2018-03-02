package com.tungha.datn_timkiemnhatro;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import static com.tungha.datn_timkiemnhatro.MainActivity.mAuth;

public class ResetPass extends AppCompatActivity {
    EditText edtEmail;
    Button btnRS;
    String email;
    ProgressDialog progressBar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        overridePendingTransition(R.anim.slide_up,R.anim.slide_up_out);
        AnhXa();
        XuLy();
    }
    public void Progress(){
        progressBar = new ProgressDialog(ResetPass.this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Loading ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;
                try {
                    sleep(200);
                    jumpTime += 5;
                    progressBar.setProgress(jumpTime);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
        t.start();
    }
    private void XuLy() {
        btnRS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Progress();
                LayDuLieu();
                if(KiemTraEmail(email)==true){
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPass.this, "Đã cấp lại mật khẩu cho bạn qua email!", Toast.LENGTH_SHORT).show();
                                finish();
                                overridePendingTransition(R.anim.slide_up,R.anim.slide_up_out);
                            } else {
                                Toast.makeText(ResetPass.this, "Không thể thực hiện!", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.dismiss();
                        }
                    });
                }else {
                    Toast.makeText(ResetPass.this, "Vui lòng nhập thông tin chính xác!", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            }
        });
    }

    private void LayDuLieu() {
        email=edtEmail.getText().toString();
    }

    public final static boolean KiemTraEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    private void AnhXa() {
        edtEmail= (EditText) findViewById(R.id.edtEmail_ResetPass);
        btnRS= (Button) findViewById(R.id.btnResetPass_ResetPass);
    }
}
