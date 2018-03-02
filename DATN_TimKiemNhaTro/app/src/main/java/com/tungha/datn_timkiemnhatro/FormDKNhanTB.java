package com.tungha.datn_timkiemnhatro;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import Classs.Database;
import Classs.LoaiNha;
import Classs.TinDKTB;

import static com.tungha.datn_timkiemnhatro.MainActivity.DATABASE_NAME;
import static com.tungha.datn_timkiemnhatro.MainActivity.arrTP;
import static com.tungha.datn_timkiemnhatro.MainActivity.database;
import static com.tungha.datn_timkiemnhatro.MainActivity.db;
import static com.tungha.datn_timkiemnhatro.MainActivity.mAuth;
import static com.tungha.datn_timkiemnhatro.TrangChinh.InfoUser;

public class FormDKNhanTB extends AppCompatActivity {
    ProgressDialog progressBar;
    Spinner spintp_tb,spinquan_tb,spinloai_tb;
    TextView txtngaythue_tb;
    ImageView imgpickngaythue_tb;
    EditText edtDuong_tb,edtsong_tb,edtsoxe_tb,edthd_tb,edtgiaden_tb,edtgiatu_tb,edtdtden_tb,edtdttu_tb,edtsophong_tb;
    CheckBox chkchungchu_tb,chknauan_tb,chkbeprieng_tb,chktoilet_tb,chkdauxe_tb,chkanninh_tb,chkmaylanh_tb,chkmaygiat_tb,chksan_tb
            ,chksanthuong_tb,chktv_tb,chkwifi_tb;
    ArrayAdapter<String> adapter_TP_tb=null;
    ArrayAdapter<String> adapter_Quan_tb=null;
    ArrayAdapter<String> adapter_Loai_tb=null;
    ArrayList<String> arrLoaiNha_tb = new ArrayList<>();
    ArrayList<String> arrQuan_tb = new ArrayList<>();
    ArrayList<String> arrThanhPho_tb = new ArrayList<>();
    Cursor c;
    int vitrichonTP_tb,vitrichonQuan_tb,vitrichonLoaiNha_tb,idTinhThanh_tb,nam_tb,thang_tb,ngay_tb,sophong_tb=0,giatu_tb,giaden_tb,dttu_tb,
            dtden_tb,songuoi_tb,soxe_tb,sohd_tb;
    String strTP_tb,strQuan_tb,strLoai_tb,strDuong_tb;
    boolean chungchu_tb=false,nauan_tb=false,beprieng_tb=false,toilet_tb=false,chodauxe_tb=false,anninh_tb=false,maylanh_tb=false
            ,maygiat_tb=false,tv_tb=false,wifi_tb=false,san_tb=false,santhuong_tb=false;
    Calendar cal_tb = Calendar.getInstance();
    Date ngaythue_tb;
    Button btnDK_tb;
    TinDKTB tintb;

    ScrollView scrollV_TB;
    FirebaseUser user = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_dknhan_tb);
        AnhXaTB_TB();
        XuLyTB_TB();
    }
    private void AnhXaTB_TB(){

        spintp_tb= (Spinner) findViewById(R.id.spinTP_ThongBao);
        spinquan_tb= (Spinner) findViewById(R.id.spinQuan_ThongBao);
        spinloai_tb= (Spinner) findViewById(R.id.spinLoaiPhong_ThongBao);
        txtngaythue_tb = (TextView) findViewById(R.id.txtHienThiNgayThue_ThongBao);
        imgpickngaythue_tb = (ImageView) findViewById(R.id.imgbtnChonNgayThue_ThongBao);
        /////
        edtDuong_tb = (EditText) findViewById(R.id.edtDuong_ThongBao);
        edtsong_tb= (EditText) findViewById(R.id.edtSLNguoi_ThongBao);
        edtsoxe_tb= (EditText) findViewById(R.id.edtSLXe_ThongBao);
        edthd_tb= (EditText) findViewById(R.id.edtHD_ThongBao);
        edtgiatu_tb= (EditText) findViewById(R.id.edtGiaTu_ThongBao);
        edtgiaden_tb= (EditText) findViewById(R.id.edtGiaDen_ThongBao);
        edtdttu_tb= (EditText) findViewById(R.id.edtDienTichTu_ThongBao);
        edtdtden_tb= (EditText) findViewById(R.id.edtDienTichDen_ThongBao);
        edtsophong_tb= (EditText) findViewById(R.id.edtSoPhong_ThongBao);
        /////
        chkchungchu_tb = (CheckBox) findViewById(R.id.chkChungChu_ThongBao);
        chknauan_tb= (CheckBox) findViewById(R.id.chkNauAn_ThongBao);
        chkbeprieng_tb= (CheckBox) findViewById(R.id.chkBepRieng_ThongBao);
        chktoilet_tb= (CheckBox) findViewById(R.id.chkToilet_ThongBao);
        chkdauxe_tb= (CheckBox) findViewById(R.id.chkDauXe_ThongBao);
        chkanninh_tb= (CheckBox) findViewById(R.id.chkAnNinh_ThongBao);
        chkmaylanh_tb= (CheckBox) findViewById(R.id.chkMayLanh_ThongBao);
        chkmaygiat_tb= (CheckBox) findViewById(R.id.chkMayGiat_ThongBao);
        chksan_tb= (CheckBox) findViewById(R.id.chkSan_ThongBao);
        chksanthuong_tb= (CheckBox) findViewById(R.id.chkSanThuong_ThongBao);
        chktv_tb= (CheckBox) findViewById(R.id.chkTV_ThongBao);
        chkwifi_tb= (CheckBox) findViewById(R.id.chkWifi_ThongBao);
        ////
        btnDK_tb= (Button) findViewById(R.id.btnThongBao);

        scrollV_TB= (ScrollView) findViewById(R.id.scrollV_ThongBao);
    }
    private void XuLyTB_TB(){
        LoadTP_tb();
        XuLyKhiChonTP_tb();
        XuLyKhiChonQuan_tb();
        XuLyKhiChonLoaiNha_tb();
        XuLyKhiChonNgayThue_tb();
        btnDK_tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XuLyKhiBamDK();
            }
        });

    }

    private void XuLyKhiBamDK() {
        Progress();
        if(KiemTraTongTinDKTB()==true) {
            LayDuLieuTuFormTB();
            String id = UUID.randomUUID().toString();
            tintb = new TinDKTB(id, user.getEmail(), new Date(), strTP_tb, strQuan_tb, strDuong_tb, giatu_tb,
                    giaden_tb, dttu_tb, dtden_tb, sophong_tb, strLoai_tb, ngaythue_tb, songuoi_tb, soxe_tb, sohd_tb, chungchu_tb, nauan_tb
                    , san_tb, santhuong_tb, beprieng_tb, toilet_tb, tv_tb, wifi_tb, maylanh_tb, maygiat_tb, chodauxe_tb, anninh_tb,false);

                db.collection("DSTinDKNhanThongBao").document(user.getEmail() + "." + id).set(tintb).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(InfoUser.isDaDKTin()==false) {
                            db.collection("User").document(user.getEmail()).update("daDKTin", true);
                        }
                        if(progressBar.isShowing())
                            progressBar.dismiss();
                        finish();
                    }
                });
        }

    }

    private boolean KiemTraTongTinDKTB() {
        if(edtdttu_tb.getText().toString().trim().length()==0 && edtdtden_tb.getText().toString().trim().length()!=0
                || edtdttu_tb.getText().toString().trim().length()!=0 && edtdtden_tb.getText().toString().trim().length()==0){
            ThongBao("Vui lòng điền đủ thông tin diện tích hoặc bỏ trống cả 2 ô");
            return false;
        }else if( edtgiatu_tb.getText().toString().trim().length()==0 && edtgiaden_tb.getText().toString().trim().length()!=0
                || edtgiatu_tb.getText().toString().trim().length()!=0 && edtgiaden_tb.getText().toString().trim().length()==0){
            ThongBao("Vui lòng điền đủ thông tin giá phòng hoặc bỏ trống cả 2 ô");
            return false;
        }else if(edtgiatu_tb.getText().toString().trim().length()!=0) {
            if (Integer.parseInt(edtgiatu_tb.getText().toString()) == 0) {
                ThongBao("Vui lòng nhập lớn hơn 0");
                return false;
            }
        }
        else if(edtgiaden_tb.getText().toString().trim().length()!=0) {
            if (Integer.parseInt(edtgiaden_tb.getText().toString()) == 0) {
                ThongBao("Vui lòng nhập lớn hơn 0");
                return false;
            }
        }
        else if(edtdttu_tb.getText().toString().trim().length()!=0) {
            if (Integer.parseInt(edtdttu_tb.getText().toString()) == 0) {
                ThongBao("Vui lòng nhập lớn hơn 0");
                return false;
            }
        }
        else if(edtdtden_tb.getText().toString().trim().length()!=0) {
            if (Integer.parseInt(edtdtden_tb.getText().toString()) == 0) {
                ThongBao("Vui lòng nhập lớn hơn 0");
                return false;
            }
        }
        else if(edtdtden_tb.getText().toString().trim().length()!=0) {
            if (Integer.parseInt(edtdtden_tb.getText().toString()) == 0) {
                ThongBao("Vui lòng nhập lớn hơn 0");
                return false;
            }
        }
        else if(edtdtden_tb.getText().toString().trim().length()!=0) {
            if (Integer.parseInt(edtdtden_tb.getText().toString()) == 0) {
                ThongBao("Vui lòng nhập lớn hơn 0");
                return false;
            }
        }
        else if(edtsong_tb.getText().toString().trim().length()!=0) {
            if (Integer.parseInt(edtsong_tb.getText().toString())==0) {
                ThongBao("Vui lòng nhập lớn hơn 0");
                return false;
            }
        }
        else if(edtsophong_tb.getText().toString().trim().length()!=0) {
            if (Integer.parseInt(edtsophong_tb.getText().toString())==0) {
                ThongBao("Vui lòng nhập lớn hơn 0");
                return false;
            }
        }
        else if(edtsoxe_tb.getText().toString().trim().length()!=0) {
            if (Integer.parseInt(edtsoxe_tb.getText().toString())==0) {
                ThongBao("Vui lòng nhập lớn hơn 0");
                return false;
            }
        }
        else if(edthd_tb.getText().toString().trim().length()!=0) {
            if (Integer.parseInt(edthd_tb.getText().toString())==0) {
                ThongBao("Vui lòng nhập lớn hơn 0");
                return false;
            }
        }
        if (edtgiatu_tb.getText().toString().trim().length() !=0 && edtgiaden_tb.getText().toString().trim().length()!=0){
            if(Integer.parseInt(edtgiatu_tb.getText().toString()) >= Integer.parseInt(edtgiaden_tb.getText().toString()) ){
                ThongBao("Giá thấp nhất không được lớn hơn hoặc bằng giá tối đa");
                return false;
            }
        }
        if (edtdttu_tb.getText().toString().trim().length() !=0 && edtdtden_tb.getText().toString().trim().length()!=0){
            if(Integer.parseInt(edtdttu_tb.getText().toString()) >= Integer.parseInt(edtdtden_tb.getText().toString()) ){
                ThongBao("Diện tích nhỏ nhất không được lớn hơn hoặc bằng diện tích tối đa");
                return false;
            }
        }
        return true;
    }


    public void LoadTP_tb() {
        database = Database.initDatabase(this, DATABASE_NAME);
        c = database.rawQuery("SELECT * FROM TinhThanh", null);
        arrThanhPho_tb.clear();
        arrThanhPho_tb.add("--Chọn tỉnh thành--");
        for (int i = 0; i < c.getCount(); i++) {
            c.moveToPosition(i);
            String ten = c.getString(1);
            arrThanhPho_tb.add(ten);
        }
        adapter_TP_tb = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrThanhPho_tb);
        adapter_TP_tb.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spintp_tb.setAdapter(adapter_TP_tb);
        c.close();
        database.close();
    }
    private void LoadLoaiNha_tb() {


        arrLoaiNha_tb.clear();
        arrLoaiNha_tb.add("--Chọn loại nhà--");
        db.collection("LoaiNha").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()) {
                        LoaiNha loai = document.toObject(LoaiNha.class);
                        arrLoaiNha_tb.add(loai.getTenLoai().toString());
                    }
                    adapter_Loai_tb.notifyDataSetChanged();
                }
            }
        });
        adapter_Loai_tb = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrLoaiNha_tb);
        adapter_Loai_tb.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinloai_tb.setAdapter(adapter_Loai_tb);

    }
    private void LayDuLieuQuanHuyenTuTinhThanh_tb(int idTT){
        database = Database.initDatabase(this, DATABASE_NAME);
        c = database.rawQuery("SELECT * FROM QuanHuyen WHERE idTinhThanh="+idTT, null);
        arrQuan_tb.clear();
        arrQuan_tb.add("--Chọn quận huyện--");
        for (int i = 0; i < c.getCount(); i++) {
            c.moveToPosition(i);
            String ten = c.getString(1);
            arrQuan_tb.add(ten);
        }
        adapter_Quan_tb = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrQuan_tb);
        adapter_Quan_tb.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinquan_tb.setAdapter(adapter_Quan_tb);
        c.close();
        database.close();
    }
    private void XuLyKhiChonNgayThue_tb(){
        imgpickngaythue_tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog_tb();
            }
        });
    }
    private void XuLyKhiChonTP_tb() {
        spintp_tb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vitrichonTP_tb=position;
                arrQuan_tb.clear();
                if(position!=0) {
                    strTP_tb=arrTP.get(position-1);
                    LayIDTinhThanh_tb(strTP_tb);
                    LayDuLieuQuanHuyenTuTinhThanh_tb(idTinhThanh_tb);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void XuLyKhiChonQuan_tb(){
        spinquan_tb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vitrichonQuan_tb=position;
                if(position!=0){
                    strQuan_tb=arrQuan_tb.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void XuLyKhiChonLoaiNha_tb(){
        spinloai_tb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vitrichonLoaiNha_tb=position;
                if(position!=0) {
                    strLoai_tb = arrLoaiNha_tb.get(position);
                    adapter_Loai_tb.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void LayIDTinhThanh_tb(String ten){
        database = Database.initDatabase(this, DATABASE_NAME);
        c = database.rawQuery("SELECT * FROM TinhThanh where Ten='"+ten+"'", null);
        for (int i = 0; i < c.getCount(); i++) {
            c.moveToPosition(i);
            idTinhThanh_tb = c.getInt(0);
        }
        c.close();
        database.close();
    }
    public void showDatePickerDialog_tb() {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date today = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String stringtoday = sdf.format(today);
                String stringngdung = String.valueOf(year + "" + String.format("%02d", month + 1) + "" + String.format("%02d", dayOfMonth));
                int intngdungchon = Integer.parseInt(stringngdung);
                Log.d("TEST", "ngay ng dung chon : " + intngdungchon);
                int inttoday = Integer.parseInt(stringtoday);
                Log.d("TEST", "ngay hien tai : " + inttoday);
                if (intngdungchon>=inttoday) {
                    cal_tb = Calendar.getInstance();
                    txtngaythue_tb.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    cal_tb.set(year, month, dayOfMonth);
                    ngaythue_tb = cal_tb.getTime();
                } else {
                    ThongBao("Ngày có thể thuê không nhỏ hơn ngày hiện tại!");
                }
            }
        };
        String s = txtngaythue_tb.getText() + "";
        if (txtngaythue_tb.getText().toString().equals("")) {
            Date date = new Date(); // your date
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            nam_tb = year;
            thang_tb = month;
            ngay_tb = day;
        } else {
            String strArrtmp[] = s.split("-");
            nam_tb = Integer.parseInt(strArrtmp[0]);
            thang_tb = Integer.parseInt(strArrtmp[1]) - 1;
            ngay_tb = Integer.parseInt(strArrtmp[2]);
        }
        DatePickerDialog pic = new DatePickerDialog(FormDKNhanTB.this, callback, nam_tb, thang_tb, ngay_tb);
        pic.setTitle("Ngày thuê ");
        pic.show();
    }
    private void GetBooleanTimKiem_TB(){
        if(chkchungchu_tb.isChecked())
            chungchu_tb=true;
        if(chkanninh_tb.isChecked())
            anninh_tb=true;
        if(chknauan_tb.isChecked())
            nauan_tb=true;
        if(chktoilet_tb.isChecked())
            toilet_tb=true;
        if(chkbeprieng_tb.isChecked())
            beprieng_tb=true;
        if(chkdauxe_tb.isChecked())
            chodauxe_tb=true;
        if(chkmaygiat_tb.isChecked())
            maygiat_tb=true;
        if(chkmaylanh_tb.isChecked())
            maylanh_tb=true;
        if(chktv_tb.isChecked())
            tv_tb=true;
        if(chkwifi_tb.isChecked())
            wifi_tb=true;
        if(chksan_tb.isChecked())
            san_tb=true;
        if(chksanthuong_tb.isChecked())
            santhuong_tb=true;
    }
    private void LayDuLieuTuFormTB(){
        GetBooleanTimKiem_TB();
        if(vitrichonTP_tb==0){
            strTP_tb="";
        }
        if(vitrichonQuan_tb==0){
            strQuan_tb="";
        }
        if(vitrichonLoaiNha_tb==0){
            strLoai_tb="";
        }
        if(edtDuong_tb.getText().toString().trim().length()==0){
            strDuong_tb="";
        }else
            strDuong_tb=edtDuong_tb.getText().toString();

        if(edtdttu_tb.getText().toString().trim().length()==0 && edtdtden_tb.getText().toString().trim().length()==0 ){
            dttu_tb=0;
            dtden_tb=0;
        } else if(edtdttu_tb.getText().toString().trim().length()!=0 && edtdtden_tb.getText().toString().trim().length()!=0 ){
            dttu_tb=Integer.parseInt(edtdttu_tb.getText().toString());
            dtden_tb=Integer.parseInt(edtdtden_tb.getText().toString());
        }

        if(edtgiatu_tb.getText().toString().trim().length()==0 && edtgiaden_tb.getText().toString().trim().length()==0 ){
            giatu_tb=0;
            giaden_tb=0;
        }else if(edtgiatu_tb.getText().toString().trim().length()!=0 && edtgiaden_tb.getText().toString().trim().length()!=0 ){
            giatu_tb=Integer.parseInt(edtgiatu_tb.getText().toString());
            giaden_tb=Integer.parseInt(edtgiaden_tb.getText().toString());
        }

        if(edtsophong_tb.getText().toString().trim().length()==0){
            sophong_tb=0;
        }else
            sophong_tb=Integer.parseInt(edtsophong_tb.getText().toString());

        if(txtngaythue_tb.getText().toString().trim().length()==0){
            ngaythue_tb=null;
        }
        if(edtsong_tb.getText().toString().trim().length()==0){
            songuoi_tb=0;
        }else
            songuoi_tb=Integer.parseInt(edtsong_tb.getText().toString());

        if(edtsoxe_tb.getText().toString().trim().length()==0){
            soxe_tb=0;
        }else
            soxe_tb=Integer.parseInt(edtsoxe_tb.getText().toString());

        if(edthd_tb.getText().toString().trim().length()==0){
            sohd_tb=0;
        }else
            sohd_tb=Integer.parseInt(edthd_tb.getText().toString());

    }
    public void Progress(){
        progressBar = new ProgressDialog(FormDKNhanTB.this);
        progressBar.show();
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setContentView(R.layout.customdialog);

    }
    public void ThongBao(String thongbao){
        AlertDialog.Builder b = new AlertDialog.Builder(FormDKNhanTB.this);
        b.setMessage(thongbao);
        b.setCancelable(false);
        b.setPositiveButton("Biết rồi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(progressBar!=null)
                if(progressBar.isShowing())
                    progressBar.dismiss();
            }
        });
        b.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadLoaiNha_tb();
    }
}
