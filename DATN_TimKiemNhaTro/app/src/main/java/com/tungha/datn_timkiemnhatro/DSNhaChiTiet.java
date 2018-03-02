package com.tungha.datn_timkiemnhatro;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import Adapter.Adapter_DSNhaCT;
import Classs.Nha;
import Classs.NhaChiTiet;
import Classs.ThongBao;
import Classs.TinDKTB;

import static com.tungha.datn_timkiemnhatro.FormDangNha.upanh;
import static com.tungha.datn_timkiemnhatro.MainActivity.db;
import static com.tungha.datn_timkiemnhatro.MainActivity.mAuth;
import static com.tungha.datn_timkiemnhatro.TrangChinh.arrTINTB;

public class DSNhaChiTiet extends AppCompatActivity {
    int THEM_NCT = 5,sltongphong;
    ProgressDialog progressBar ;
    Button btnThemnha,btnok,btnhuy;
    RecyclerView reView_DSNha;
    Adapter_DSNhaCT adapter ;
    Nha nhadanhan ;
    Bitmap uri_hinhnhan;
    byte[] byte_hinhnhan;
    ArrayList<NhaChiTiet> arrNCT = new ArrayList<>();
    ArrayList<Bitmap> arrUriAnh = new ArrayList<>();
    NhaChiTiet nct;
    Calendar cal = Calendar.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://timkiemnhatro-d501f.appspot.com");
    StorageReference hinhanhRef ;
    String strHinhAnh;
    FirebaseUser user = mAuth.getCurrentUser();
    TinDKTB tinDK;
    Nha nhaSauTurn2;
    Nha nhaSauTurn1;
    ArrayList<NhaChiTiet> arrNCT_SauTurn9 = new ArrayList<>();
    ArrayList<NhaChiTiet> arrNCT_SauTurn8 = new ArrayList<>();
    ArrayList<NhaChiTiet> arrNCT_SauTurn7 = new ArrayList<>();
    ArrayList<NhaChiTiet> arrNCT_SauTurn6 = new ArrayList<>();
    ArrayList<NhaChiTiet> arrNCT_SauTurn5 = new ArrayList<>();
    ArrayList<NhaChiTiet> arrNCT_SauTurn4 = new ArrayList<>();
    ArrayList<NhaChiTiet> arrNCT_SauTurn3 = new ArrayList<>();
    boolean check_turn2=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dsnha_chi_tiet);
        if(user!=null) {
            AnhXa();
            NhanDuLieuNha();
            XuLy();
        }else {
            ThongBao("Lỗi hệ thống. Vui lòng đăng nhập lại!");
        }
    }

    private void XuLy() {
        XuLyRecyleViewDSNha();
        XuLyKhiAddNCT();
        XuLyKhiClickOK();
        XuLyKhiClickHuyBo();
    }

    private void XuLyKhiClickHuyBo() {
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void XuLyKhiClickOK() {
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrNCT.size()!=0) {
                    Progress();
                    SetTienThapNhatChoNha();
                    setTongSoPhongChoNha();
                    progressBar.dismiss();
                    new UpBai().execute();
                    new DuyetTinTaoTB().execute();
                    finish();
                }else {
                    ThongBao("Chưa có chi tiết phòng, không thể đăng bài");
                }
            }
        });

    }

    private void setTongSoPhongChoNha() {
        int tongphong=0;
        for(int i=0;i<arrNCT.size();i++){
            tongphong+=arrNCT.get(i).getSoLuongPhong();
        }
        nhadanhan.setSoLuongPhong(tongphong);
    }

    private void SetTienThapNhatChoNha() {
        int min=arrNCT.get(0).getGiaThue();
        for(int i=1;i<arrNCT.size();i++){
            if(arrNCT.get(i).getGiaThue() < min)
                min = arrNCT.get(i).getGiaThue();
        }
        nhadanhan.setGiaThapNhat(min);
    }

    private void DangNhaCTLenFS() {
        for(int i=0;i<arrNCT.size();i++) {
            NhaChiTiet nct = arrNCT.get(i);
            db.collection("DSNhaChiTiet").document(nct.getId()).set(nct);
        }
    }

    private void DangBaiLenFS() {
        String child = UUID.randomUUID().toString();
        hinhanhRef=storageRef.child(child);
            UploadTask uploadTask = hinhanhRef.putBytes(byte_hinhnhan);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(DSNhaChiTiet.this, "Upload ảnh gặp sự cố! Thử lại sau", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    strHinhAnh = String.valueOf(taskSnapshot.getDownloadUrl()); // << chỗ lấy đc link ảnh
                    nhadanhan.setHinhAnh(strHinhAnh);
                    db.collection("Nha").document(nhadanhan.getId()).set(nhadanhan).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                DangNhaCTLenFS();
                            }
                        }
                    });
                }
            });
    }

    private void XuLyKhiAddNCT() {
        btnThemnha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoFormThemNCT();
            }
        });
    }

    private void MoFormThemNCT() {
        Intent i = new Intent(DSNhaChiTiet.this,FormThemNhaChiTiet.class);
        Bundle b= new Bundle();
        b.putString("idNha",nhadanhan.getId().toString());
        i.putExtra("bundle",b);
        startActivityForResult(i,THEM_NCT);
    }

    private void NhanDuLieuNha(){
        Intent i = getIntent();
        Bundle b= i.getBundleExtra("bundle");
        nhadanhan = (Nha) b.getSerializable("nha");
        byte_hinhnhan=i.getByteArrayExtra("hinhanh");
    }
    private void XuLyRecyleViewDSNha() {
        reView_DSNha.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reView_DSNha.setLayoutManager(linearLayoutManager);
        adapter = new Adapter_DSNhaCT(arrUriAnh,arrNCT, getApplicationContext(),DSNhaChiTiet.this);
        reView_DSNha.setAdapter(adapter);
    }

    private void AnhXa() {
        btnok= (Button) findViewById(R.id.btnOK_XNDangBai);
        btnhuy= (Button) findViewById(R.id.btnHuy_XNDangBai);
        btnThemnha= (Button) findViewById(R.id.btnAdd_DSNhaCT);
        reView_DSNha= (RecyclerView) findViewById(R.id.reView_DSNhaCT);
    }
    public void Progress(){
        progressBar = new ProgressDialog(DSNhaChiTiet.this);
        progressBar.show();
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setContentView(R.layout.customdialog);

    }
    public void ThongBao(String thongbao){
        AlertDialog.Builder b = new AlertDialog.Builder(DSNhaChiTiet.this);
        b.setMessage(thongbao);
        b.setPositiveButton("Biết rồi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.create().show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==THEM_NCT && data!=null){
            if(resultCode==55){
                Bundle b = data.getBundleExtra("data");
                nct = (NhaChiTiet) b.getSerializable("nct_trave");
                arrNCT.add(nct);
                byte[] byteArray = data.getByteArrayExtra("hinhdsnct");
                uri_hinhnhan = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                arrUriAnh.add(uri_hinhnhan);
                adapter.notifyDataSetChanged();
            }
        }
    }
    private void DuyetListTinDK(){
        for(int i=0;i<arrTINTB.size();i++){
            tinDK=arrTINTB.get(i);
            ClearAllArr();
            if(tinDK.getTP().compareTo("")!=0 &&
                    tinDK.getQuan().compareTo("")!=0 &&
                    tinDK.getDuong().compareTo("")!=0){
                TinDuyet_TP_Quan_Duong();
            }else if(tinDK.getTP().compareTo("")!=0 &&
                    tinDK.getQuan().compareTo("")!=0 &&
                    tinDK.getDuong().compareTo("")==0){
                TinDuyet_TP_Quan();
            }else if(tinDK.getTP().compareTo("")!=0 &&
                    tinDK.getQuan().compareTo("")==0 &&
                    tinDK.getDuong().compareTo("")!=0){
                TinDuyet_TP_Duong();
            }else if(tinDK.getTP().compareTo("")!=0 &&
                    tinDK.getQuan().compareTo("")==0 &&
                    tinDK.getDuong().compareTo("")==0){
                TinDuyet_TP();
            }else if(tinDK.getTP().compareTo("")==0 &&
                    tinDK.getQuan().compareTo("")==0 &&
                    tinDK.getDuong().compareTo("")!=0){
                TinDuyet_Duong();
            }else if(tinDK.getTP().compareTo("")==0 &&
                    tinDK.getQuan().compareTo("")==0 &&
                    tinDK.getDuong().compareTo("")==0){
                Ko_Duyet_Turn1();
            }
        }
    }

    private void ClearAllArr() {
        arrNCT_SauTurn3.clear();
        arrNCT_SauTurn4.clear();
        arrNCT_SauTurn5.clear();
        arrNCT_SauTurn6.clear();
        arrNCT_SauTurn7.clear();
        arrNCT_SauTurn8.clear();
        arrNCT_SauTurn9.clear();
        check_turn2=false;
    }

    //////TURN 1
    private void TinDuyet_TP_Quan_Duong(){
        if(tinDK.getTP().compareTo(nhadanhan.getDiaChi().getThanhPho())==0 &&
                tinDK.getQuan().compareTo(nhadanhan.getDiaChi().getQuan())==0 &&
                tinDK.getDuong().compareTo(nhadanhan.getDiaChi().getDuong())==0){
            nhaSauTurn1=nhadanhan;
            if(tinDK.getLoaiphong().compareTo("")!=0 && tinDK.isChungchu()){
                TinDuyet_LoaiNha_CC();
            }else if(tinDK.getLoaiphong().compareTo("")!=0){
                TinDuyet_LoaiNha();
            }else if(tinDK.isChungchu()){
                TinDuyet_CC();
            }else if(tinDK.getLoaiphong().compareTo("")==0 && !tinDK.isChungchu()){
                Khong_Duyet_Loai_CC();
            }
        }

    }
    private void TinDuyet_TP_Quan(){
        if(tinDK.getTP().compareTo(nhadanhan.getDiaChi().getThanhPho())==0 &&
                tinDK.getQuan().compareTo(nhadanhan.getDiaChi().getQuan())==0){
            nhaSauTurn1=nhadanhan;
            if(tinDK.getLoaiphong().compareTo("")!=0 && tinDK.isChungchu()){
                TinDuyet_LoaiNha_CC();
            }else if(tinDK.getLoaiphong().compareTo("")!=0){
                TinDuyet_LoaiNha();
            }else if(tinDK.isChungchu()){
                TinDuyet_CC();
            }else if(tinDK.getLoaiphong().compareTo("")==0 && !tinDK.isChungchu()){
                Khong_Duyet_Loai_CC();
            }
        }

    }
    private void TinDuyet_TP_Duong(){
        if(tinDK.getTP().compareTo(nhadanhan.getDiaChi().getThanhPho())==0 &&
                tinDK.getDuong().compareTo(nhadanhan.getDiaChi().getDuong())==0){
            nhaSauTurn1=nhadanhan;
            if(tinDK.getLoaiphong().compareTo("")!=0 && tinDK.isChungchu()){
                TinDuyet_LoaiNha_CC();
            }else if(tinDK.getLoaiphong().compareTo("")!=0){
                TinDuyet_LoaiNha();
            }else if(tinDK.isChungchu()){
                TinDuyet_CC();
            }else if(tinDK.getLoaiphong().compareTo("")==0 && !tinDK.isChungchu()){
                Khong_Duyet_Loai_CC();
            }
        }

    }
    private void TinDuyet_TP(){
        if(tinDK.getTP().compareTo(nhadanhan.getDiaChi().getThanhPho())==0){
            nhaSauTurn1=nhadanhan;
            if(tinDK.getLoaiphong().compareTo("")!=0 && tinDK.isChungchu()){
                TinDuyet_LoaiNha_CC();
            }else if(tinDK.getLoaiphong().compareTo("")!=0){
                TinDuyet_LoaiNha();
            }else if(tinDK.isChungchu()){
                TinDuyet_CC();
            }else if(tinDK.getLoaiphong().compareTo("")==0 && !tinDK.isChungchu()){
                Khong_Duyet_Loai_CC();
            }
        }

    }
    private void TinDuyet_Duong(){
        if(tinDK.getDuong().compareTo(nhadanhan.getDiaChi().getDuong())==0){
            nhaSauTurn1=nhadanhan;
            if(tinDK.getLoaiphong().compareTo("")!=0 && tinDK.isChungchu()){
                TinDuyet_LoaiNha_CC();
            }else if(tinDK.getLoaiphong().compareTo("")!=0){
                TinDuyet_LoaiNha();
            }else if(tinDK.isChungchu()){
                TinDuyet_CC();
            }else if(tinDK.getLoaiphong().compareTo("")==0 && !tinDK.isChungchu()){
                Khong_Duyet_Loai_CC();
            }
        }

    }
    private void Ko_Duyet_Turn1(){
            nhaSauTurn1=nhadanhan;
            if(tinDK.getLoaiphong().compareTo("")!=0 && tinDK.isChungchu()){
                TinDuyet_LoaiNha_CC();
            }else if(tinDK.getLoaiphong().compareTo("")!=0){
                TinDuyet_LoaiNha();
            }else if(tinDK.isChungchu()){
                TinDuyet_CC();
            }else if(tinDK.getLoaiphong().compareTo("")==0 && !tinDK.isChungchu()){
                Khong_Duyet_Loai_CC();
            }
    }
    /////TURN 2
    private void TinDuyet_LoaiNha_CC(){
        if(tinDK.getLoaiphong().compareTo(nhaSauTurn1.getLoaiNha())==0 && nhaSauTurn1.getChungChu()){
            check_turn2=true;
        }
        if(check_turn2==true) {
            if (tinDK.getSophong() != 0 && tinDK.getNgaynhan() != null && tinDK.getSonguoi() != 0) {
                TinDuyet_Phong_Ngay_Nguoi();
            } else if (tinDK.getSophong() != 0 && tinDK.getNgaynhan() != null && tinDK.getSonguoi() == 0) {
                TinDuyet_Phong_Ngay();
            } else if (tinDK.getSophong() != 0 && tinDK.getNgaynhan() == null && tinDK.getSonguoi() != 0) {
                TinDuyet_Phong_Nguoi();
            } else if (tinDK.getSophong() != 0 && tinDK.getNgaynhan() == null && tinDK.getSonguoi() == 0) {
                TinDuyet_Phong();
            } else if (tinDK.getSophong() == 0 && tinDK.getNgaynhan() != null && tinDK.getSonguoi() != 0) {
                TinDuyet_Ngay_Nguoi();
            } else if (tinDK.getSophong() == 0 && tinDK.getNgaynhan() != null && tinDK.getSonguoi() == 0) {
                TinDuyet_Ngay();
            } else if (tinDK.getSophong() == 0 && tinDK.getNgaynhan() == null && tinDK.getSonguoi() != 0) {
                TinDuyet_Nguoi();
            } else if (tinDK.getSophong() == 0 && tinDK.getNgaynhan() == null && tinDK.getSonguoi() == 0) {
                Khong_Duyet_Turn3();
            }
        }
    }
    private void TinDuyet_LoaiNha(){
        if(tinDK.getLoaiphong().compareTo(nhaSauTurn1.getLoaiNha())==0){
            check_turn2=true;
        }
        if(check_turn2==true) {
            if (tinDK.getSophong() != 0 && tinDK.getNgaynhan() != null && tinDK.getSonguoi() != 0) {
                TinDuyet_Phong_Ngay_Nguoi();
            } else if (tinDK.getSophong() != 0 && tinDK.getNgaynhan() != null && tinDK.getSonguoi() == 0) {
                TinDuyet_Phong_Ngay();
            } else if (tinDK.getSophong() != 0 && tinDK.getNgaynhan() == null && tinDK.getSonguoi() != 0) {
                TinDuyet_Phong_Nguoi();
            } else if (tinDK.getSophong() != 0 && tinDK.getNgaynhan() == null && tinDK.getSonguoi() == 0) {
                TinDuyet_Phong();
            } else if (tinDK.getSophong() == 0 && tinDK.getNgaynhan() != null && tinDK.getSonguoi() != 0) {
                TinDuyet_Ngay_Nguoi();
            } else if (tinDK.getSophong() == 0 && tinDK.getNgaynhan() != null && tinDK.getSonguoi() == 0) {
                TinDuyet_Ngay();
            } else if (tinDK.getSophong() == 0 && tinDK.getNgaynhan() == null && tinDK.getSonguoi() != 0) {
                TinDuyet_Nguoi();
            } else if (tinDK.getSophong() == 0 && tinDK.getNgaynhan() == null && tinDK.getSonguoi() == 0) {
                Khong_Duyet_Turn3();
            }
        }
    }
    private void TinDuyet_CC(){
        if(nhaSauTurn1.getChungChu()){
            check_turn2=true;
        }
        if(check_turn2==true) {
            if (tinDK.getSophong() != 0 && tinDK.getNgaynhan() != null && tinDK.getSonguoi() != 0) {
                TinDuyet_Phong_Ngay_Nguoi();
            } else if (tinDK.getSophong() != 0 && tinDK.getNgaynhan() != null && tinDK.getSonguoi() == 0) {
                TinDuyet_Phong_Ngay();
            } else if (tinDK.getSophong() != 0 && tinDK.getNgaynhan() == null && tinDK.getSonguoi() != 0) {
                TinDuyet_Phong_Nguoi();
            } else if (tinDK.getSophong() != 0 && tinDK.getNgaynhan() == null && tinDK.getSonguoi() == 0) {
                TinDuyet_Phong();
            } else if (tinDK.getSophong() == 0 && tinDK.getNgaynhan() != null && tinDK.getSonguoi() != 0) {
                TinDuyet_Ngay_Nguoi();
            } else if (tinDK.getSophong() == 0 && tinDK.getNgaynhan() != null && tinDK.getSonguoi() == 0) {
                TinDuyet_Ngay();
            } else if (tinDK.getSophong() == 0 && tinDK.getNgaynhan() == null && tinDK.getSonguoi() != 0) {
                TinDuyet_Nguoi();
            } else if (tinDK.getSophong() == 0 && tinDK.getNgaynhan() == null && tinDK.getSonguoi() == 0) {
                Khong_Duyet_Turn3();
            }
        }
    }
    private void Khong_Duyet_Loai_CC(){
        if(tinDK.getSophong()!=0 && tinDK.getNgaynhan()!=null && tinDK.getSonguoi()!=0){
            TinDuyet_Phong_Ngay_Nguoi();
        }else if(tinDK.getSophong()!=0 && tinDK.getNgaynhan()!=null && tinDK.getSonguoi()==0){
            TinDuyet_Phong_Ngay();
        }else if(tinDK.getSophong()!=0 && tinDK.getNgaynhan()==null && tinDK.getSonguoi()!=0){
            TinDuyet_Phong_Nguoi();
        }else if(tinDK.getSophong()!=0 && tinDK.getNgaynhan()==null && tinDK.getSonguoi()==0){
            TinDuyet_Phong();
        }else if(tinDK.getSophong()==0 && tinDK.getNgaynhan()!=null && tinDK.getSonguoi()!=0){
            TinDuyet_Ngay_Nguoi();
        }else if(tinDK.getSophong()==0 && tinDK.getNgaynhan()!=null && tinDK.getSonguoi()==0){
            TinDuyet_Ngay();
        }else if(tinDK.getSophong()==0 && tinDK.getNgaynhan()==null && tinDK.getSonguoi()!=0){
            TinDuyet_Nguoi();
        }else if(tinDK.getSophong()==0 && tinDK.getNgaynhan()==null && tinDK.getSonguoi()==0){
            Khong_Duyet_Turn3();
        }
    }
    //////TURN 3
    private void TinDuyet_Phong_Ngay_Nguoi() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        for (int i = 0; i < arrNCT.size(); i++) {
            String ngaythueuserchon = sdf.format(tinDK.getNgaynhan());
            int intngaythueuserchon = Integer.parseInt(ngaythueuserchon);
            int ngaycuanha = Integer.parseInt(sdf.format(arrNCT.get(i).getNgayNhan()));
            if (tinDK.getSophong() <= arrNCT.get(i).getSoLuongPhong())
                if (tinDK.getSonguoi() <= arrNCT.get(i).getYeuCau().getSoLuongNguoi())
                    if(intngaythueuserchon >=ngaycuanha)
                          arrNCT_SauTurn3.add(arrNCT.get(i));
        }
        if(tinDK.getGiatu()!=0 && tinDK.getDttu()!=0){
            TinDuyet_Gia_DT();
        }else if(tinDK.getGiatu()!=0 && tinDK.getDttu()==0){
            TinDuyet_Gia();
        }else if(tinDK.getGiatu()==0 && tinDK.getDttu()!=0){
            TinDuyet_DT();
        }else if(tinDK.getGiatu()==0 && tinDK.getDttu()==0){
            Khong_Duyet_Turn4();
        }
    }
    private void TinDuyet_Phong_Ngay() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        for (int i = 0; i < arrNCT.size(); i++) {
            String ngaythueuserchon = sdf.format(tinDK.getNgaynhan());
            int intngaythueuserchon = Integer.parseInt(ngaythueuserchon);
            int ngaycuanha = Integer.parseInt(sdf.format(arrNCT.get(i).getNgayNhan()));
            if (tinDK.getSophong() <= arrNCT.get(i).getSoLuongPhong())
                if(intngaythueuserchon >=ngaycuanha)
                        arrNCT_SauTurn3.add(arrNCT.get(i));
        }
        if(tinDK.getGiatu()!=0 && tinDK.getDttu()!=0){
            TinDuyet_Gia_DT();
        }else if(tinDK.getGiatu()!=0 && tinDK.getDttu()==0){
            TinDuyet_Gia();
        }else if(tinDK.getGiatu()==0 && tinDK.getDttu()!=0){
            TinDuyet_DT();
        }else if(tinDK.getGiatu()==0 && tinDK.getDttu()==0){
            Khong_Duyet_Turn4();
        }
    }
    private void TinDuyet_Phong_Nguoi() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        for (int i = 0; i < arrNCT.size(); i++) {
            if (tinDK.getSophong() <= arrNCT.get(i).getSoLuongPhong())
                if (tinDK.getSonguoi() <= arrNCT.get(i).getYeuCau().getSoLuongNguoi())
                        arrNCT_SauTurn3.add(arrNCT.get(i));
        }
        if(tinDK.getGiatu()!=0 && tinDK.getDttu()!=0){
            TinDuyet_Gia_DT();
        }else if(tinDK.getGiatu()!=0 && tinDK.getDttu()==0){
            TinDuyet_Gia();
        }else if(tinDK.getGiatu()==0 && tinDK.getDttu()!=0){
            TinDuyet_DT();
        }else if(tinDK.getGiatu()==0 && tinDK.getDttu()==0){
            Khong_Duyet_Turn4();
        }
    }
    private void TinDuyet_Phong() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        for (int i = 0; i < arrNCT.size(); i++) {
            if (tinDK.getSophong() <= arrNCT.get(i).getSoLuongPhong())
                        arrNCT_SauTurn3.add(arrNCT.get(i));
        }
        if(tinDK.getGiatu()!=0 && tinDK.getDttu()!=0){
            TinDuyet_Gia_DT();
        }else if(tinDK.getGiatu()!=0 && tinDK.getDttu()==0){
            TinDuyet_Gia();
        }else if(tinDK.getGiatu()==0 && tinDK.getDttu()!=0){
            TinDuyet_DT();
        }else if(tinDK.getGiatu()==0 && tinDK.getDttu()==0){
            Khong_Duyet_Turn4();
        }
    }
    private void TinDuyet_Ngay_Nguoi() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        for (int i = 0; i < arrNCT.size(); i++) {
            String ngaythueuserchon = sdf.format(tinDK.getNgaynhan());
            int intngaythueuserchon = Integer.parseInt(ngaythueuserchon);
            int ngaycuanha = Integer.parseInt(sdf.format(arrNCT.get(i).getNgayNhan()));
                if (tinDK.getSonguoi() <= arrNCT.get(i).getYeuCau().getSoLuongNguoi())
                    if(intngaythueuserchon >=ngaycuanha)
                        arrNCT_SauTurn3.add(arrNCT.get(i));
        }
        if(tinDK.getGiatu()!=0 && tinDK.getDttu()!=0){
            TinDuyet_Gia_DT();
        }else if(tinDK.getGiatu()!=0 && tinDK.getDttu()==0){
            TinDuyet_Gia();
        }else if(tinDK.getGiatu()==0 && tinDK.getDttu()!=0){
            TinDuyet_DT();
        }else if(tinDK.getGiatu()==0 && tinDK.getDttu()==0){
            Khong_Duyet_Turn4();
        }
    }
    private void TinDuyet_Ngay() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        for (int i = 0; i < arrNCT.size(); i++) {
            String ngaythueuserchon = sdf.format(tinDK.getNgaynhan());
            int intngaythueuserchon = Integer.parseInt(ngaythueuserchon);
            int ngaycuanha = Integer.parseInt(sdf.format(arrNCT.get(i).getNgayNhan()));
                    if(intngaythueuserchon >=ngaycuanha)
                        arrNCT_SauTurn3.add(arrNCT.get(i));
        }
        if(tinDK.getGiatu()!=0 && tinDK.getDttu()!=0){
            TinDuyet_Gia_DT();
        }else if(tinDK.getGiatu()!=0 && tinDK.getDttu()==0){
            TinDuyet_Gia();
        }else if(tinDK.getGiatu()==0 && tinDK.getDttu()!=0){
            TinDuyet_DT();
        }else if(tinDK.getGiatu()==0 && tinDK.getDttu()==0){
            Khong_Duyet_Turn4();
        }
    }
    private void TinDuyet_Nguoi() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        for (int i = 0; i < arrNCT.size(); i++) {
                if (tinDK.getSonguoi() <= arrNCT.get(i).getYeuCau().getSoLuongNguoi())
                        arrNCT_SauTurn3.add(arrNCT.get(i));
        }
        if(tinDK.getGiatu()!=0 && tinDK.getDttu()!=0){
            TinDuyet_Gia_DT();
        }else if(tinDK.getGiatu()!=0 && tinDK.getDttu()==0){
            TinDuyet_Gia();
        }else if(tinDK.getGiatu()==0 && tinDK.getDttu()!=0){
            TinDuyet_DT();
        }else if(tinDK.getGiatu()==0 && tinDK.getDttu()==0){
            Khong_Duyet_Turn4();
        }
    }
    private void Khong_Duyet_Turn3() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        for (int i = 0; i < arrNCT.size(); i++) {
                        arrNCT_SauTurn3.add(arrNCT.get(i));
        }
        if(tinDK.getGiatu()!=0 && tinDK.getDttu()!=0){
            TinDuyet_Gia_DT();
        }else if(tinDK.getGiatu()!=0 && tinDK.getDttu()==0){
            TinDuyet_Gia();
        }else if(tinDK.getGiatu()==0 && tinDK.getDttu()!=0){
            TinDuyet_DT();
        }else if(tinDK.getGiatu()==0 && tinDK.getDttu()==0){
            Khong_Duyet_Turn4();
        }
    }
    //////TURN 4
    private void TinDuyet_Gia_DT(){
        for(int i=0;i<arrNCT_SauTurn3.size();i++) {
            if(tinDK.getGiatu() <= arrNCT_SauTurn3.get(i).getGiaThue() && arrNCT_SauTurn3.get(i).getGiaThue() <= tinDK.getGiaden())
                if(tinDK.getDttu() <= arrNCT_SauTurn3.get(i).getDienTich() && arrNCT_SauTurn3.get(i).getGiaThue() <= tinDK.getDtden())
                {
                arrNCT_SauTurn4.add(arrNCT_SauTurn3.get(i));
                }
        }
        if(tinDK.getSoxe()!=0 && tinDK.getHd()!=0){
            TinDuyet_SLXe_HD();
        }else if(tinDK.getSoxe()!=0 && tinDK.getHd()==0){
            TinDuyet_SLXe();
        }else if(tinDK.getSoxe()==0 && tinDK.getHd()!=0){
            TinDuyet_HD();
        }else if(tinDK.getSoxe()==0 && tinDK.getHd()==0){
            Khong_Duyet_Turn5();
        }
    }
    private void TinDuyet_Gia(){
        for(int i=0;i<arrNCT_SauTurn3.size();i++) {
            if(tinDK.getGiatu() <= arrNCT_SauTurn3.get(i).getGiaThue() && arrNCT_SauTurn3.get(i).getGiaThue() <= tinDK.getGiaden())
                {
                    arrNCT_SauTurn4.add(arrNCT_SauTurn3.get(i));
                }
        }
        if(tinDK.getSoxe()!=0 && tinDK.getHd()!=0){
            TinDuyet_SLXe_HD();
        }else if(tinDK.getSoxe()!=0 && tinDK.getHd()==0){
            TinDuyet_SLXe();
        }else if(tinDK.getSoxe()==0 && tinDK.getHd()!=0){
            TinDuyet_HD();
        }else if(tinDK.getSoxe()==0 && tinDK.getHd()==0){
            Khong_Duyet_Turn5();
        }
    }
    private void TinDuyet_DT(){
        for(int i=0;i<arrNCT_SauTurn3.size();i++) {
                if(tinDK.getDttu() <= arrNCT_SauTurn3.get(i).getDienTich() && arrNCT_SauTurn3.get(i).getGiaThue() <= tinDK.getDtden())
                {
                    arrNCT_SauTurn4.add(arrNCT_SauTurn3.get(i));
                }
        }
        if(tinDK.getSoxe()!=0 && tinDK.getHd()!=0){
            TinDuyet_SLXe_HD();
        }else if(tinDK.getSoxe()!=0 && tinDK.getHd()==0){
            TinDuyet_SLXe();
        }else if(tinDK.getSoxe()==0 && tinDK.getHd()!=0){
            TinDuyet_HD();
        }else if(tinDK.getSoxe()==0 && tinDK.getHd()==0){
            Khong_Duyet_Turn5();
        }
    }
    private void Khong_Duyet_Turn4(){
        for(int i=0;i<arrNCT_SauTurn3.size();i++) {
                    arrNCT_SauTurn4.add(arrNCT_SauTurn3.get(i));
        }
        if(tinDK.getSoxe()!=0 && tinDK.getHd()!=0){
            TinDuyet_SLXe_HD();
        }else if(tinDK.getSoxe()!=0 && tinDK.getHd()==0){
            TinDuyet_SLXe();
        }else if(tinDK.getSoxe()==0 && tinDK.getHd()!=0){
            TinDuyet_HD();
        }else if(tinDK.getSoxe()==0 && tinDK.getHd()==0){
            Khong_Duyet_Turn5();
        }
    }
    //////TURN 5
    private void TinDuyet_SLXe_HD(){
        for(int i=0;i<arrNCT_SauTurn4.size();i++) {
            if(arrNCT_SauTurn4.get(i).getYeuCau().getSoLuongXe()>=tinDK.getSoxe() &&
                    arrNCT_SauTurn4.get(i).getYeuCau().getHopDong()>=tinDK.getHd()){
                arrNCT_SauTurn5.add(arrNCT_SauTurn4.get(i));
            }
        }
        if(tinDK.isNauan() && tinDK.isSan() && tinDK.isSanthuong()){
            TinDuyet_NauAn_San_ST();
        }else if(tinDK.isNauan() && tinDK.isSan() && !tinDK.isSanthuong()){
            TinDuyet_NauAn_San();
        }else if(tinDK.isNauan() && !tinDK.isSan() && tinDK.isSanthuong()){
            TinDuyet_NauAn_ST();
        }else if(tinDK.isNauan() && !tinDK.isSan() && !tinDK.isSanthuong()){
            TinDuyet_NauAn();
        }else if(!tinDK.isNauan() && tinDK.isSan() && tinDK.isSanthuong()){
            TinDuyet_San_ST();
        }else if(!tinDK.isNauan() && !tinDK.isSan() && tinDK.isSanthuong()){
            TinDuyet_ST();
        }else if(!tinDK.isNauan() && tinDK.isSan() && !tinDK.isSanthuong()){
            TinDuyet_San();
        }else if(!tinDK.isNauan() && !tinDK.isSan() && !tinDK.isSanthuong()){
           Khong_Duyet_Turn6();
        }
    }
    private void TinDuyet_SLXe(){
        for(int i=0;i<arrNCT_SauTurn4.size();i++) {
            if(arrNCT_SauTurn4.get(i).getYeuCau().getSoLuongXe()>=tinDK.getSoxe()){
                arrNCT_SauTurn5.add(arrNCT_SauTurn4.get(i));
            }
        }
        if(tinDK.isNauan() && tinDK.isSan() && tinDK.isSanthuong()){
            TinDuyet_NauAn_San_ST();
        }else if(tinDK.isNauan() && tinDK.isSan() && !tinDK.isSanthuong()){
            TinDuyet_NauAn_San();
        }else if(tinDK.isNauan() && !tinDK.isSan() && tinDK.isSanthuong()){
            TinDuyet_NauAn_ST();
        }else if(tinDK.isNauan() && !tinDK.isSan() && !tinDK.isSanthuong()){
            TinDuyet_NauAn();
        }else if(!tinDK.isNauan() && tinDK.isSan() && tinDK.isSanthuong()){
            TinDuyet_San_ST();
        }else if(!tinDK.isNauan() && !tinDK.isSan() && tinDK.isSanthuong()){
            TinDuyet_ST();
        }else if(!tinDK.isNauan() && tinDK.isSan() && !tinDK.isSanthuong()){
            TinDuyet_San();
        }else if(!tinDK.isNauan() && !tinDK.isSan() && !tinDK.isSanthuong()){
            Khong_Duyet_Turn6();
        }
    }
    private void TinDuyet_HD(){
        for(int i=0;i<arrNCT_SauTurn4.size();i++) {
            if(arrNCT_SauTurn4.get(i).getYeuCau().getHopDong()>=tinDK.getHd()){
                arrNCT_SauTurn5.add(arrNCT_SauTurn4.get(i));
            }
        }
        if(tinDK.isNauan() && tinDK.isSan() && tinDK.isSanthuong()){
            TinDuyet_NauAn_San_ST();
        }else if(tinDK.isNauan() && tinDK.isSan() && !tinDK.isSanthuong()){
            TinDuyet_NauAn_San();
        }else if(tinDK.isNauan() && !tinDK.isSan() && tinDK.isSanthuong()){
            TinDuyet_NauAn_ST();
        }else if(tinDK.isNauan() && !tinDK.isSan() && !tinDK.isSanthuong()){
            TinDuyet_NauAn();
        }else if(!tinDK.isNauan() && tinDK.isSan() && tinDK.isSanthuong()){
            TinDuyet_San_ST();
        }else if(!tinDK.isNauan() && !tinDK.isSan() && tinDK.isSanthuong()){
            TinDuyet_ST();
        }else if(!tinDK.isNauan() && tinDK.isSan() && !tinDK.isSanthuong()){
            TinDuyet_San();
        }else if(!tinDK.isNauan() && !tinDK.isSan() && !tinDK.isSanthuong()){
            Khong_Duyet_Turn6();
        }
    }
    private void Khong_Duyet_Turn5(){
        for(int i=0;i<arrNCT_SauTurn4.size();i++) {
                arrNCT_SauTurn5.add(arrNCT_SauTurn4.get(i));
        }
        if(tinDK.isNauan() && tinDK.isSan() && tinDK.isSanthuong()){
            TinDuyet_NauAn_San_ST();
        }else if(tinDK.isNauan() && tinDK.isSan() && !tinDK.isSanthuong()){
            TinDuyet_NauAn_San();
        }else if(tinDK.isNauan() && !tinDK.isSan() && tinDK.isSanthuong()){
            TinDuyet_NauAn_ST();
        }else if(tinDK.isNauan() && !tinDK.isSan() && !tinDK.isSanthuong()){
            TinDuyet_NauAn();
        }else if(!tinDK.isNauan() && tinDK.isSan() && tinDK.isSanthuong()){
            TinDuyet_San_ST();
        }else if(!tinDK.isNauan() && !tinDK.isSan() && tinDK.isSanthuong()){
            TinDuyet_ST();
        }else if(!tinDK.isNauan() && tinDK.isSan() && !tinDK.isSanthuong()){
            TinDuyet_San();
        }else if(!tinDK.isNauan() && !tinDK.isSan() && !tinDK.isSanthuong()){
            Khong_Duyet_Turn6();
        }
    }
    //////TURN 6
    private void TinDuyet_NauAn_San_ST(){
        for(int i=0;i<arrNCT_SauTurn5.size();i++) {
            if(arrNCT_SauTurn5.get(i).getYeuCau().getChoNauAn() &&
                    arrNCT_SauTurn5.get(i).getTienIch().getSanPhoiDo() &&
                    arrNCT_SauTurn5.get(i).getTienIch().getSanThuong()){
                arrNCT_SauTurn6.add(arrNCT_SauTurn5.get(i));
            }
        }
        if(tinDK.isBeprieng() && tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Bep_Toilet_Cap();
        }else if(tinDK.isBeprieng() && tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep_Toilet();
        }else if(tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep_Cap();
        }else if(tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep();
        }else if(!tinDK.isBeprieng() && tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Toilet_Cap();
        }else if(!tinDK.isBeprieng() && tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Toilet();
        }else if(!tinDK.isBeprieng() && !tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Cap();
        }else if(!tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            Khong_Duyet_Turn7();
        }
    }
    private void TinDuyet_NauAn_San(){
        for(int i=0;i<arrNCT_SauTurn5.size();i++) {
            if(arrNCT_SauTurn5.get(i).getYeuCau().getChoNauAn() &&
                    arrNCT_SauTurn5.get(i).getTienIch().getSanPhoiDo()){
                arrNCT_SauTurn6.add(arrNCT_SauTurn5.get(i));
            }
        }
        if(tinDK.isBeprieng() && tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Bep_Toilet_Cap();
        }else if(tinDK.isBeprieng() && tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep_Toilet();
        }else if(tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep_Cap();
        }else if(tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep();
        }else if(!tinDK.isBeprieng() && tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Toilet_Cap();
        }else if(!tinDK.isBeprieng() && tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Toilet();
        }else if(!tinDK.isBeprieng() && !tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Cap();
        }else if(!tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            Khong_Duyet_Turn7();
        }
    }
    private void TinDuyet_NauAn_ST(){
        for(int i=0;i<arrNCT_SauTurn5.size();i++) {
            if(arrNCT_SauTurn5.get(i).getYeuCau().getChoNauAn() &&
                    arrNCT_SauTurn5.get(i).getTienIch().getSanThuong()){
                arrNCT_SauTurn6.add(arrNCT_SauTurn5.get(i));
            }
        }
        if(tinDK.isBeprieng() && tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Bep_Toilet_Cap();
        }else if(tinDK.isBeprieng() && tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep_Toilet();
        }else if(tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep_Cap();
        }else if(tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep();
        }else if(!tinDK.isBeprieng() && tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Toilet_Cap();
        }else if(!tinDK.isBeprieng() && tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Toilet();
        }else if(!tinDK.isBeprieng() && !tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Cap();
        }else if(!tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            Khong_Duyet_Turn7();
        }
    }
    private void TinDuyet_NauAn(){
        for(int i=0;i<arrNCT_SauTurn5.size();i++) {
            if(arrNCT_SauTurn5.get(i).getYeuCau().getChoNauAn()){
                arrNCT_SauTurn6.add(arrNCT_SauTurn5.get(i));
            }
        }
        if(tinDK.isBeprieng() && tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Bep_Toilet_Cap();
        }else if(tinDK.isBeprieng() && tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep_Toilet();
        }else if(tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep_Cap();
        }else if(tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep();
        }else if(!tinDK.isBeprieng() && tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Toilet_Cap();
        }else if(!tinDK.isBeprieng() && tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Toilet();
        }else if(!tinDK.isBeprieng() && !tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Cap();
        }else if(!tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            Khong_Duyet_Turn7();
        }
    }
    private void TinDuyet_San_ST(){
        for(int i=0;i<arrNCT_SauTurn5.size();i++) {
            if(arrNCT_SauTurn5.get(i).getTienIch().getSanPhoiDo() &&
                    arrNCT_SauTurn5.get(i).getTienIch().getSanThuong()){
                arrNCT_SauTurn6.add(arrNCT_SauTurn5.get(i));
            }
        }
        if(tinDK.isBeprieng() && tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Bep_Toilet_Cap();
        }else if(tinDK.isBeprieng() && tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep_Toilet();
        }else if(tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep_Cap();
        }else if(tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep();
        }else if(!tinDK.isBeprieng() && tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Toilet_Cap();
        }else if(!tinDK.isBeprieng() && tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Toilet();
        }else if(!tinDK.isBeprieng() && !tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Cap();
        }else if(!tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            Khong_Duyet_Turn7();
        }
    }
    private void TinDuyet_San(){
        for(int i=0;i<arrNCT_SauTurn5.size();i++) {
            if(arrNCT_SauTurn5.get(i).getTienIch().getSanPhoiDo()){
                arrNCT_SauTurn6.add(arrNCT_SauTurn5.get(i));
            }
        }
        if(tinDK.isBeprieng() && tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Bep_Toilet_Cap();
        }else if(tinDK.isBeprieng() && tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep_Toilet();
        }else if(tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep_Cap();
        }else if(tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep();
        }else if(!tinDK.isBeprieng() && tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Toilet_Cap();
        }else if(!tinDK.isBeprieng() && tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Toilet();
        }else if(!tinDK.isBeprieng() && !tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Cap();
        }else if(!tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            Khong_Duyet_Turn7();
        }
    }
    private void TinDuyet_ST(){
        for(int i=0;i<arrNCT_SauTurn5.size();i++) {
            if(arrNCT_SauTurn5.get(i).getTienIch().getSanThuong()){
                arrNCT_SauTurn6.add(arrNCT_SauTurn5.get(i));
            }
        }
        if(tinDK.isBeprieng() && tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Bep_Toilet_Cap();
        }else if(tinDK.isBeprieng() && tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep_Toilet();
        }else if(tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep_Cap();
        }else if(tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep();
        }else if(!tinDK.isBeprieng() && tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Toilet_Cap();
        }else if(!tinDK.isBeprieng() && tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Toilet();
        }else if(!tinDK.isBeprieng() && !tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Cap();
        }else if(!tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            Khong_Duyet_Turn7();
        }
    }
    private void Khong_Duyet_Turn6(){
        for(int i=0;i<arrNCT_SauTurn5.size();i++) {
                arrNCT_SauTurn6.add(arrNCT_SauTurn5.get(i));
        }
        if(tinDK.isBeprieng() && tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Bep_Toilet_Cap();
        }else if(tinDK.isBeprieng() && tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep_Toilet();
        }else if(tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep_Cap();
        }else if(tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Bep();
        }else if(!tinDK.isBeprieng() && tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Toilet_Cap();
        }else if(!tinDK.isBeprieng() && tinDK.isToiletrieng() && !tinDK.isTv()){
            TinDuyet_Toilet();
        }else if(!tinDK.isBeprieng() && !tinDK.isToiletrieng() && tinDK.isTv()){
            TinDuyet_Cap();
        }else if(!tinDK.isBeprieng() && !tinDK.isToiletrieng() && !tinDK.isTv()){
            Khong_Duyet_Turn7();
        }
    }
    //////TURN 7
    private void TinDuyet_Bep_Toilet_Cap(){
        for(int i=0;i<arrNCT_SauTurn6.size();i++) {
            if(arrNCT_SauTurn6.get(i).getTienIch().getBepNauAn() &&
                    arrNCT_SauTurn6.get(i).getTienIch().getNhaVSRieng() &&
                    arrNCT_SauTurn6.get(i).getTienIch().getTruyenHinhCap()){
                arrNCT_SauTurn7.add(arrNCT_SauTurn6.get(i));
            }
        }
        if(tinDK.isWifi() && tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_Wifi_ML_MG();
        }else if(tinDK.isWifi() && tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_Wifi_ML();
        }else if(tinDK.isWifi() && !tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_Wifi_MG();
        }else if(tinDK.isWifi() && !tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_Wifi();
        }else if(!tinDK.isWifi() && tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_ML_MG();
        }else if(!tinDK.isWifi() && tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_ML();
        }else if(!tinDK.isWifi() && !tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_MG();
        }else if(!tinDK.isWifi() && !tinDK.isMaylanh() && !tinDK.isMaygiat()){
            Khong_Duyet_Turn8();
        }
    }
    private void TinDuyet_Bep_Toilet(){
        for(int i=0;i<arrNCT_SauTurn6.size();i++) {
            if(arrNCT_SauTurn6.get(i).getTienIch().getBepNauAn() &&
                    arrNCT_SauTurn6.get(i).getTienIch().getNhaVSRieng()){
                arrNCT_SauTurn7.add(arrNCT_SauTurn6.get(i));
            }
        }
        if(tinDK.isWifi() && tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_Wifi_ML_MG();
        }else if(tinDK.isWifi() && tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_Wifi_ML();
        }else if(tinDK.isWifi() && !tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_Wifi_MG();
        }else if(tinDK.isWifi() && !tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_Wifi();
        }else if(!tinDK.isWifi() && tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_ML_MG();
        }else if(!tinDK.isWifi() && tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_ML();
        }else if(!tinDK.isWifi() && !tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_MG();
        }else if(!tinDK.isWifi() && !tinDK.isMaylanh() && !tinDK.isMaygiat()){
            Khong_Duyet_Turn8();
        }
    }
    private void TinDuyet_Bep_Cap(){
        for(int i=0;i<arrNCT_SauTurn6.size();i++) {
            if(arrNCT_SauTurn6.get(i).getTienIch().getBepNauAn() &&
                    arrNCT_SauTurn6.get(i).getTienIch().getTruyenHinhCap()){
                arrNCT_SauTurn7.add(arrNCT_SauTurn6.get(i));
            }
        }
        if(tinDK.isWifi() && tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_Wifi_ML_MG();
        }else if(tinDK.isWifi() && tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_Wifi_ML();
        }else if(tinDK.isWifi() && !tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_Wifi_MG();
        }else if(tinDK.isWifi() && !tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_Wifi();
        }else if(!tinDK.isWifi() && tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_ML_MG();
        }else if(!tinDK.isWifi() && tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_ML();
        }else if(!tinDK.isWifi() && !tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_MG();
        }else if(!tinDK.isWifi() && !tinDK.isMaylanh() && !tinDK.isMaygiat()){
            Khong_Duyet_Turn8();
        }
    }
    private void TinDuyet_Bep(){
        for(int i=0;i<arrNCT_SauTurn6.size();i++) {
            if(arrNCT_SauTurn6.get(i).getTienIch().getBepNauAn()){
                arrNCT_SauTurn7.add(arrNCT_SauTurn6.get(i));
            }
        }
        if(tinDK.isWifi() && tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_Wifi_ML_MG();
        }else if(tinDK.isWifi() && tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_Wifi_ML();
        }else if(tinDK.isWifi() && !tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_Wifi_MG();
        }else if(tinDK.isWifi() && !tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_Wifi();
        }else if(!tinDK.isWifi() && tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_ML_MG();
        }else if(!tinDK.isWifi() && tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_ML();
        }else if(!tinDK.isWifi() && !tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_MG();
        }else if(!tinDK.isWifi() && !tinDK.isMaylanh() && !tinDK.isMaygiat()){
            Khong_Duyet_Turn8();
        }
    }
    private void TinDuyet_Toilet_Cap(){
        for(int i=0;i<arrNCT_SauTurn6.size();i++) {
            if(arrNCT_SauTurn6.get(i).getTienIch().getNhaVSRieng() &&
                    arrNCT_SauTurn6.get(i).getTienIch().getTruyenHinhCap()){
                arrNCT_SauTurn7.add(arrNCT_SauTurn6.get(i));
            }
        }
        if(tinDK.isWifi() && tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_Wifi_ML_MG();
        }else if(tinDK.isWifi() && tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_Wifi_ML();
        }else if(tinDK.isWifi() && !tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_Wifi_MG();
        }else if(tinDK.isWifi() && !tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_Wifi();
        }else if(!tinDK.isWifi() && tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_ML_MG();
        }else if(!tinDK.isWifi() && tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_ML();
        }else if(!tinDK.isWifi() && !tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_MG();
        }else if(!tinDK.isWifi() && !tinDK.isMaylanh() && !tinDK.isMaygiat()){
            Khong_Duyet_Turn8();
        }
    }
    private void TinDuyet_Toilet(){
        for(int i=0;i<arrNCT_SauTurn6.size();i++) {
            if(arrNCT_SauTurn6.get(i).getTienIch().getNhaVSRieng()){
                arrNCT_SauTurn7.add(arrNCT_SauTurn6.get(i));
            }
        }
        if(tinDK.isWifi() && tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_Wifi_ML_MG();
        }else if(tinDK.isWifi() && tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_Wifi_ML();
        }else if(tinDK.isWifi() && !tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_Wifi_MG();
        }else if(tinDK.isWifi() && !tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_Wifi();
        }else if(!tinDK.isWifi() && tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_ML_MG();
        }else if(!tinDK.isWifi() && tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_ML();
        }else if(!tinDK.isWifi() && !tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_MG();
        }else if(!tinDK.isWifi() && !tinDK.isMaylanh() && !tinDK.isMaygiat()){
            Khong_Duyet_Turn8();
        }
    }
    private void TinDuyet_Cap(){
        for(int i=0;i<arrNCT_SauTurn6.size();i++) {
            if(arrNCT_SauTurn6.get(i).getTienIch().getTruyenHinhCap()){
                arrNCT_SauTurn7.add(arrNCT_SauTurn6.get(i));
            }
        }
        if(tinDK.isWifi() && tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_Wifi_ML_MG();
        }else if(tinDK.isWifi() && tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_Wifi_ML();
        }else if(tinDK.isWifi() && !tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_Wifi_MG();
        }else if(tinDK.isWifi() && !tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_Wifi();
        }else if(!tinDK.isWifi() && tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_ML_MG();
        }else if(!tinDK.isWifi() && tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_ML();
        }else if(!tinDK.isWifi() && !tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_MG();
        }else if(!tinDK.isWifi() && !tinDK.isMaylanh() && !tinDK.isMaygiat()){
            Khong_Duyet_Turn8();
        }
    }
    private void Khong_Duyet_Turn7(){
        for(int i=0;i<arrNCT_SauTurn6.size();i++) {
                arrNCT_SauTurn7.add(arrNCT_SauTurn6.get(i));
        }
        if(tinDK.isWifi() && tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_Wifi_ML_MG();
        }else if(tinDK.isWifi() && tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_Wifi_ML();
        }else if(tinDK.isWifi() && !tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_Wifi_MG();
        }else if(tinDK.isWifi() && !tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_Wifi();
        }else if(!tinDK.isWifi() && tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_ML_MG();
        }else if(!tinDK.isWifi() && tinDK.isMaylanh() && !tinDK.isMaygiat()){
            TinDuyet_ML();
        }else if(!tinDK.isWifi() && !tinDK.isMaylanh() && tinDK.isMaygiat()){
            TinDuyet_MG();
        }else if(!tinDK.isWifi() && !tinDK.isMaylanh() && !tinDK.isMaygiat()){
            Khong_Duyet_Turn8();
        }
    }
    ////// TURN 8
    private void TinDuyet_Wifi_ML_MG(){
        for(int i=0;i<arrNCT_SauTurn7.size();i++) {
            if(arrNCT_SauTurn7.get(i).getTienIch().getMayLanh() &&
                    arrNCT_SauTurn7.get(i).getTienIch().getMayGiat() &&
                    arrNCT_SauTurn7.get(i).getTienIch().getWifi()){
                arrNCT_SauTurn8.add(arrNCT_SauTurn7.get(i));
            }
        }
        if(tinDK.isAnninh() && tinDK.isChodauxe()){
            TinDuyet_Xe_AnNinh();
        }else if(!tinDK.isAnninh() && tinDK.isChodauxe()){
            TinDuyet_Xe();
        }else if(tinDK.isAnninh() && !tinDK.isChodauxe()){
            TinDuyet_AnNinh();
        }else if(!tinDK.isAnninh() && !tinDK.isChodauxe()){
            TinKoDuyet_Turn_Cuoi();
        }
    }
    private void TinDuyet_Wifi_ML(){
        for(int i=0;i<arrNCT_SauTurn7.size();i++) {
            if(arrNCT_SauTurn7.get(i).getTienIch().getMayLanh() &&
                    arrNCT_SauTurn7.get(i).getTienIch().getWifi()){
                arrNCT_SauTurn8.add(arrNCT_SauTurn7.get(i));
            }
        }
        if(tinDK.isAnninh() && tinDK.isChodauxe()){
            TinDuyet_Xe_AnNinh();
        }else if(!tinDK.isAnninh() && tinDK.isChodauxe()){
            TinDuyet_Xe();
        }else if(tinDK.isAnninh() && !tinDK.isChodauxe()){
            TinDuyet_AnNinh();
        }else if(!tinDK.isAnninh() && !tinDK.isChodauxe()){
            TinKoDuyet_Turn_Cuoi();
        }
    }
    private void TinDuyet_Wifi_MG(){
        for(int i=0;i<arrNCT_SauTurn7.size();i++) {
            if(arrNCT_SauTurn7.get(i).getTienIch().getMayGiat() &&
                    arrNCT_SauTurn7.get(i).getTienIch().getWifi()){
                arrNCT_SauTurn8.add(arrNCT_SauTurn7.get(i));
            }
        }
        if(tinDK.isAnninh() && tinDK.isChodauxe()){
            TinDuyet_Xe_AnNinh();
        }else if(!tinDK.isAnninh() && tinDK.isChodauxe()){
            TinDuyet_Xe();
        }else if(tinDK.isAnninh() && !tinDK.isChodauxe()){
            TinDuyet_AnNinh();
        }else if(!tinDK.isAnninh() && !tinDK.isChodauxe()){
            TinKoDuyet_Turn_Cuoi();
        }
    }
    private void TinDuyet_Wifi(){
        for(int i=0;i<arrNCT_SauTurn7.size();i++) {
            if(arrNCT_SauTurn7.get(i).getTienIch().getWifi()){
                arrNCT_SauTurn8.add(arrNCT_SauTurn7.get(i));
            }
        }
        if(tinDK.isAnninh() && tinDK.isChodauxe()){
            TinDuyet_Xe_AnNinh();
        }else if(!tinDK.isAnninh() && tinDK.isChodauxe()){
            TinDuyet_Xe();
        }else if(tinDK.isAnninh() && !tinDK.isChodauxe()){
            TinDuyet_AnNinh();
        }else if(!tinDK.isAnninh() && !tinDK.isChodauxe()){
            TinKoDuyet_Turn_Cuoi();
        }
    }
    private void TinDuyet_ML_MG(){
        for(int i=0;i<arrNCT_SauTurn7.size();i++) {
            if(arrNCT_SauTurn7.get(i).getTienIch().getMayLanh() &&
                    arrNCT_SauTurn7.get(i).getTienIch().getMayGiat()){
                arrNCT_SauTurn8.add(arrNCT_SauTurn7.get(i));
            }
        }
        if(tinDK.isAnninh() && tinDK.isChodauxe()){
            TinDuyet_Xe_AnNinh();
        }else if(!tinDK.isAnninh() && tinDK.isChodauxe()){
            TinDuyet_Xe();
        }else if(tinDK.isAnninh() && !tinDK.isChodauxe()){
            TinDuyet_AnNinh();
        }else if(!tinDK.isAnninh() && !tinDK.isChodauxe()){
            TinKoDuyet_Turn_Cuoi();
        }
    }
    private void TinDuyet_ML(){
        for(int i=0;i<arrNCT_SauTurn7.size();i++) {
            if(arrNCT_SauTurn7.get(i).getTienIch().getMayLanh()){
                arrNCT_SauTurn8.add(arrNCT_SauTurn7.get(i));
            }
        }
        if(tinDK.isAnninh() && tinDK.isChodauxe()){
            TinDuyet_Xe_AnNinh();
        }else if(!tinDK.isAnninh() && tinDK.isChodauxe()){
            TinDuyet_Xe();
        }else if(tinDK.isAnninh() && !tinDK.isChodauxe()){
            TinDuyet_AnNinh();
        }else if(!tinDK.isAnninh() && !tinDK.isChodauxe()){
            TinKoDuyet_Turn_Cuoi();
        }
    }
    private void TinDuyet_MG(){
        for(int i=0;i<arrNCT_SauTurn7.size();i++) {
            if(arrNCT_SauTurn7.get(i).getTienIch().getMayLanh() &&
                    arrNCT_SauTurn7.get(i).getTienIch().getMayGiat() &&
                    arrNCT_SauTurn7.get(i).getTienIch().getWifi()){
                arrNCT_SauTurn8.add(arrNCT_SauTurn7.get(i));
            }
        }
        if(tinDK.isAnninh() && tinDK.isChodauxe()){
            TinDuyet_Xe_AnNinh();
        }else if(!tinDK.isAnninh() && tinDK.isChodauxe()){
            TinDuyet_Xe();
        }else if(tinDK.isAnninh() && !tinDK.isChodauxe()){
            TinDuyet_AnNinh();
        }else if(!tinDK.isAnninh() && !tinDK.isChodauxe()){
            TinKoDuyet_Turn_Cuoi();
        }
    }
    private void Khong_Duyet_Turn8(){
        for(int i=0;i<arrNCT_SauTurn7.size();i++) {
            arrNCT_SauTurn8.add(arrNCT_SauTurn7.get(i));
        }
        if(tinDK.isAnninh() && tinDK.isChodauxe()){
            TinDuyet_Xe_AnNinh();
        }else if(!tinDK.isAnninh() && tinDK.isChodauxe()){
            TinDuyet_Xe();
        }else if(tinDK.isAnninh() && !tinDK.isChodauxe()){
            TinDuyet_AnNinh();
        }else if(!tinDK.isAnninh() && !tinDK.isChodauxe()){
            TinKoDuyet_Turn_Cuoi();
        }
    }
    ////// TURN 9
    private void TinDuyet_Xe_AnNinh(){
       for(int i=0;i<arrNCT_SauTurn8.size();i++){
           if(arrNCT_SauTurn8.get(i).getTienIch().getChoDeXe() &&
                   arrNCT_SauTurn8.get(i).getTienIch().getBaoVe()){
               arrNCT_SauTurn9.add(arrNCT_SauTurn8.get(i));
           }
       }
        for(int j=0;j<arrNCT_SauTurn9.size();j++){
            String id = UUID.randomUUID().toString();
            ThongBao tb = new ThongBao(id,arrNCT_SauTurn9.get(j).getIdNha(),arrNCT_SauTurn9.get(j).getId(),tinDK.getMailuser(),new Date(),"Tìm thấy nhà phù hợp với tin đăng ký",false,tinDK.getId());
            db.collection("DSThongBao").document(id).set(tb);
        }
    }
    private void TinDuyet_Xe(){
        for(int i=0;i<arrNCT_SauTurn8.size();i++){
            if(arrNCT_SauTurn8.get(i).getTienIch().getChoDeXe()){
                arrNCT_SauTurn9.add(arrNCT_SauTurn8.get(i));
            }
        }
        for(int j=0;j<arrNCT_SauTurn9.size();j++){
            String id = UUID.randomUUID().toString();
            ThongBao tb = new ThongBao(id,arrNCT_SauTurn9.get(j).getIdNha(),arrNCT_SauTurn9.get(j).getId(),tinDK.getMailuser(),new Date(),"Tìm thấy nhà phù hợp với tin đăng ký",false,tinDK.getId());
            db.collection("DSThongBao").document(id).set(tb);
        }
    }
    private void TinDuyet_AnNinh(){
        for(int i=0;i<arrNCT_SauTurn8.size();i++){
            if(arrNCT_SauTurn8.get(i).getTienIch().getBaoVe()){
                arrNCT_SauTurn9.add(arrNCT_SauTurn8.get(i));
            }
        }
        for(int j=0;j<arrNCT_SauTurn9.size();j++){
            String id = UUID.randomUUID().toString();
            ThongBao tb = new ThongBao(id,arrNCT_SauTurn9.get(j).getIdNha(),arrNCT_SauTurn9.get(j).getId(),tinDK.getMailuser(),new Date(),"Tìm thấy nhà phù hợp với tin đăng ký",false,tinDK.getId());
            db.collection("DSThongBao").document(id).set(tb);
        }
    }
    private void TinKoDuyet_Turn_Cuoi(){
        for(int i=0;i<arrNCT_SauTurn8.size();i++){
                arrNCT_SauTurn9.add(arrNCT_SauTurn8.get(i));
        }
        for(int j=0;j<arrNCT_SauTurn9.size();j++){
            String id = UUID.randomUUID().toString();
            ThongBao tb = new ThongBao(id,arrNCT_SauTurn9.get(j).getIdNha(),arrNCT_SauTurn9.get(j).getId(),tinDK.getMailuser(),new Date(),"Tìm thấy nhà phù hợp với tin đăng ký",false,tinDK.getId());
            db.collection("DSThongBao").document(id).set(tb);
        }
    }
    public class DuyetTinTaoTB extends AsyncTask<Void,Void,Void>{
        public DuyetTinTaoTB(){

        }
        @Override
        protected Void doInBackground(Void... params) {
            DuyetListTinDK();
            return null;
        }
    }

    public class UpBai extends AsyncTask<Void,Void,Void>{

        public UpBai(){

        }
        @Override
        protected Void doInBackground(Void... params) {
            DangBaiLenFS();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(DSNhaChiTiet.this, "Đăng bài thành công", Toast.LENGTH_SHORT).show();
        }
    }
}
