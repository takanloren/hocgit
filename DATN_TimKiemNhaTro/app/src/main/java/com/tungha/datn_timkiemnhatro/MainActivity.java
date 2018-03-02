package com.tungha.datn_timkiemnhatro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import Classs.*;


public class MainActivity extends AppCompatActivity {
    int currentTab=0,vitrispinTP=0,vitrispinQuan=0,idTinhThanh;
    TabHost tab=null;
    ProgressDialog progressBar ;
    Button btnDK,btnreDK,btnDN,btnSkip;
    RadioButton rdoNam,rdoNu;
    Spinner spinTP,spinQuan;
    TextView txtQuenMK;
    CheckBox nhoTK;
    Cursor c;
    public static SQLiteDatabase database;
    EditText edtEmail,edtPW,edtrePW,edtHoten,edtSDT,edtSoNha,edtDuong,edtPhuong,edtMail_DN,edtPW_DN;
    String strEmail,strPW,strrePW,strHoten,strSDT,strSoNha,strDuong,strPhuong,strQuan,strTP,strEmail_DN,strPW_DN;
    Boolean gioitinh;
    public static ArrayList<String> arrTP = new ArrayList<String>();
    ArrayList<String> arrQuan = new ArrayList<String>();
    ArrayAdapter<String> adapter_TP=null;
    ArrayAdapter<String> adapter_Quan=null;
    Map<String, Object> data = new HashMap<>();
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    public static String DATABASE_NAME = "TimKiemNhaTro3.sqlite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        KiemTraAuth();
        /*data.put("a", "a");
        for(int i=0;i<strQuann.length;i++){
            Quan q = new Quan(strQuann[i]);
            UpDuLieu(q,"Đồng Tháp");
        }*/
        loadTabs();
        AnhXa();
        LoadTinhThanh();
        XuLy();


    }

    private void KiemTraAuth() {
        if(user!=null){
            mAuth.signOut();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadUserPreferences();
    }

    @Override
    protected void onStart() {
        super.onStart();
        KiemTraAuth();
    }
    public void LoadTinhThanh() {
        database = Database.initDatabase(this, DATABASE_NAME);
        c = database.rawQuery("SELECT * FROM TinhThanh", null);
        arrTP.clear();
        for (int i = 0; i < c.getCount(); i++) {
            c.moveToPosition(i);
            String ten = c.getString(1);
            arrTP.add(ten);
        }
        adapter_TP = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrTP);
        adapter_TP.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinTP.setAdapter(adapter_TP);
        c.close();
        database.close();
    }
    private void LayIDTinhThanh(String ten){
        database = Database.initDatabase(this, DATABASE_NAME);
        c = database.rawQuery("SELECT * FROM TinhThanh where Ten='"+ten+"'", null);
        for (int i = 0; i < c.getCount(); i++) {
            c.moveToPosition(i);
            idTinhThanh = c.getInt(0);
        }
        c.close();
        database.close();
    }
    private void LoadUserPreferences(){
        SharedPreferences pre=getSharedPreferences("data", Context.MODE_PRIVATE);
        boolean bchk=pre.getBoolean("checked", false);
        if(bchk)
        {
            //lấy user, pwd, nếu không thấy giá trị mặc định là rỗng
            String user=pre.getString("user", "");
            String pwd=pre.getString("pwd", "");
            edtMail_DN.setText(user);
            edtPW_DN.setText(pwd);
        }
        nhoTK.setChecked(bchk);
    }
    private void LuuUserPreferences(){
        SharedPreferences pre=getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        boolean bchk=nhoTK.isChecked();
        String user=strEmail_DN;
        String pwd=strPW_DN;
        if(!bchk)
        {
            //xóa mọi lưu trữ trước đó
            editor.clear();
        }
        else
        {
            //lưu vào editor
            editor.putString("user", user);
            editor.putString("pwd", pwd);
            editor.putBoolean("checked", bchk);
        }
        //chấp nhận lưu xuống file
        editor.commit();
        if(progressBar!=null)
            if(progressBar.isShowing())
                progressBar.dismiss();
    }
    public final static boolean KiemTraEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void XuLy() {
        spinTP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    strTP = arrTP.get(position);
                    vitrispinTP=position;
                    arrQuan.clear();
                    Log.d("TP", strTP);
                    LayIDTinhThanh(strTP);
                    LayDuLieuQuanHuyenTuTinhThanh(idTinhThanh);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        spinQuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strQuan = arrQuan.get(position);
                vitrispinQuan=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayThongTinDK();
                if(KiemTraThongTinDK()==true) {
                    Progress();
                    mAuth.createUserWithEmailAndPassword(strEmail, strPW)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Đăng ký thất bại!",
                                                Toast.LENGTH_SHORT).show();
                                        progressBar.dismiss();
                                    } else {
                                        DKUserTrongFB();
                                    }
                                }
                            });
                }
            }


        });
        btnreDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XoaTrangDK();
            }
        });
        //////////////////////////////
        btnDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Progress();
                LayThongTinDN();
                if(KiemTraThongTinDN()==true){
                    mAuth.signInWithEmailAndPassword(strEmail_DN, strPW_DN)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        Toast.makeText(MainActivity.this, "Đăng nhập thất bại , vui lòng kiểm tra lại thông tin hoặc network", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, TrangChinh.class);
                                        startActivity(intent);
                                        LuuUserPreferences();
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TrangChinh.class);
                startActivity(intent);
                finish();
            }
        });
        txtQuenMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoFormRSPass();

            }
        });

    }
    public void DangXuat(){
        mAuth.signOut();
    }
    private void MoFormRSPass(){
        Intent i = new Intent(MainActivity.this,ResetPass.class);
        startActivity(i);
    }
    private void DKUserTrongFB(){
        DiaChi dc = new DiaChi(strTP,strQuan,strPhuong,strDuong,strSoNha);
        User u = new User(strHoten,strSDT,strEmail,dc,gioitinh,true,false,"https://firebasestorage.googleapis.com/v0/b/timkiemnhatro-d501f.appspot.com/o/default_user_avatar.png?alt=media&token=f4fe4ac0-b4fe-4b71-9976-068463991d13");
        db.collection("User").document(strEmail).set(u).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressBar.dismiss();
                    XoaTrangDK();
                    tab.setCurrentTab(0);
                    Toast.makeText(MainActivity.this, "Đăng ký thành công tài khoản :" + strEmail, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void LayThongTinDK() {
        strEmail = edtEmail.getText().toString();
        strPW = edtPW.getText().toString();
        strrePW=edtrePW.getText().toString();
        strHoten=edtHoten.getText().toString();
        strSDT=edtSDT.getText().toString();
        strSoNha=edtSoNha.getText().toString();
        strDuong=edtDuong.getText().toString();
        strPhuong=edtPhuong.getText().toString();
        if(rdoNam.isChecked()){
            gioitinh=true;
        }else {
            gioitinh=false;
        }
    }
    private void LayThongTinDN(){
        strEmail_DN=edtMail_DN.getText().toString();
        strPW_DN=edtPW_DN.getText().toString();
    }
    private Boolean KiemTraThongTinDK() {
        if(KiemTraEmail(strEmail)==false){
            ThongBao("Email chưa chính xác!");
            return false;
        }else {
            if(strPW.compareTo(strrePW)!=0){
                ThongBao("Mật khẩu không khớp!");
                return false;
                 }else if(strSDT.trim().length() < 10){
                    ThongBao("Số điện thoại chưa chính xác!");
                    return false;
                     }else if(strPW.trim().length() <6){
                         ThongBao("Mật khẩu phải có ít nhất 6 ký tự!");
                          return false;
                     }else if(strSoNha.isEmpty() || strPhuong.isEmpty() || strDuong.isEmpty()){
                            ThongBao("Vui lòng điền đầy đủ thông tin địa chỉ");
                            return false;
                       }
             }
        return true;
    }
    private Boolean KiemTraThongTinDN(){
        if(strEmail_DN.isEmpty() || strPW_DN.isEmpty()){
            ThongBao("Vui lòng nhập đầy đủ thông tin!");
            progressBar.dismiss();
            return false;
        }
        return true;
    }
    private void XoaTrangDK(){
        edtEmail.setText("");
        edtPW.setText("");
        edtrePW.setText("");
        edtHoten.setText("");
        edtSDT.setText("");
        edtSoNha.setText("");
        edtDuong.setText("");
        edtPhuong.setText("");
        spinTP.setSelection(0);
        spinQuan.setSelection(0);
        vitrispinQuan=0;
        vitrispinTP=0;
    }
    public void Progress(){
        progressBar = new ProgressDialog(MainActivity.this);
        progressBar.show();
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setContentView(R.layout.customdialog);

    }
    private void AnhXa() {
        /////////////////////////////////////////////////
        ////// Dang Nhap ////////////////////////////////
        btnDN= (Button) findViewById(R.id.btnLogin_Login);
        edtMail_DN = (EditText) findViewById(R.id.edtEmail_Login);
        edtPW_DN= (EditText) findViewById(R.id.edtMK_Login);
        /////////////////////////////////////////////////
        btnDK = (Button) findViewById(R.id.btnOK_SignUp);
        btnreDK = (Button) findViewById(R.id.btnNhapLai_SignUp);
        spinTP = (Spinner) findViewById(R.id.spinTinhThanh_SignUp);
        spinQuan = (Spinner) findViewById(R.id.spinQuan_SignUp);
        rdoNam = (RadioButton) findViewById(R.id.rdoNam_SignUp);
        rdoNam.setChecked(true);
        rdoNu= (RadioButton) findViewById(R.id.rdoNu_SignUp);
        edtDuong= (EditText) findViewById(R.id.edtDuong_SignUp);
        edtPhuong= (EditText) findViewById(R.id.edtPhuong_SignUp);
        edtSoNha= (EditText) findViewById(R.id.edtSoNha_SignUp);
        edtEmail= (EditText) findViewById(R.id.edtEmail_SignUp);
        edtHoten= (EditText) findViewById(R.id.edtHoTen_SignUp);
        edtSDT= (EditText) findViewById(R.id.edtSDT_SignUp);
        edtPW= (EditText) findViewById(R.id.edtMK_SignUp);
        edtrePW= (EditText) findViewById(R.id.edtreMK_SignUp);
        /////////////
        nhoTK= (CheckBox) findViewById(R.id.checkboxNhoTK_Login);
        btnSkip= (Button) findViewById(R.id.btnSkipLogin_Login);
        txtQuenMK= (TextView) findViewById(R.id.txtQuenMK_Login);
    }

    public static Animation inFromRightAnimation()
    {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(400);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    public static Animation outToRightAnimation()
    {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(400);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }
    public void loadTabs() {

        tab = (TabHost) findViewById(R.id.tabAdmin);
        //gọi lệnh setup

        tab.setup();
        tab.getTabWidget().setStripEnabled(false);
        TabHost.TabSpec spec;
        //Tạo tab1
        spec = tab.newTabSpec("t1");
        spec.setContent(R.id.tabLogin);
        spec.setIndicator("Đăng nhập");
        tab.addTab(spec);
        //Tạo tab2
        spec = tab.newTabSpec("t2");
        spec.setContent(R.id.tabSignup);
        spec.setIndicator("Đăng ký");
        tab.addTab(spec);
        //Thiết lập tab mặc định được chọn ban đầu là tab 0
        tab.setCurrentTab(0);
        tab.getTabWidget().getChildAt(tab.getCurrentTab())
                .setBackgroundColor((Color.parseColor("#1E90FF")));
        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                View currentView = tab.getCurrentView();
                if (tab.getCurrentTab() > currentTab)
                {
                    currentView.setAnimation( inFromRightAnimation() );
                }
                else
                {
                    currentView.setAnimation( outToRightAnimation() );
                }
                currentTab = tab.getCurrentTab();

                for (int i = 0; i < tab.getTabWidget().getChildCount(); i++) {
                    tab.getTabWidget().getChildAt(i)
                            .setBackgroundColor((Color.parseColor("#F0F8FF")));

                }
                tab.getTabWidget().getChildAt(tab.getCurrentTab())
                        .setBackgroundColor((Color.parseColor("#1E90FF"))); //selected

            }
        });
        for (int i = 0; i < tab.getTabWidget().getChildCount(); i++) {
            tab.getTabWidget().getChildAt(i).getLayoutParams().height=75;
        }
    }
    private void LayDuLieuQuanHuyenTuTinhThanh(int idTT){
        database = Database.initDatabase(this, DATABASE_NAME);
        c = database.rawQuery("SELECT * FROM QuanHuyen WHERE idTinhThanh="+idTT, null);
        arrQuan.clear();
        for (int i = 0; i < c.getCount(); i++) {
            c.moveToPosition(i);
            String ten = c.getString(1);
            arrQuan.add(ten);
        }
        adapter_Quan = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrQuan);
        adapter_Quan.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinQuan.setAdapter(adapter_Quan);
        c.close();
        database.close();
    }
    private void UpDuLieu(Quan quan,String tinhthanh){
        db.collection("TinhThanhQuanHuyen").document(tinhthanh).set(data);
        db.collection("TinhThanhQuanHuyen").document(tinhthanh).collection("quan.huyen").add(quan);
    }
    public void ThongBao(String thongbao){
            AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
            b.setTitle("Thông báo");
            b.setMessage(thongbao);
            b.setPositiveButton("Biết rồi", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            b.create().show();
    }
}

