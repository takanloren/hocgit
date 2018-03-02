package com.tungha.datn_timkiemnhatro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import Classs.*;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import Classs.GiaTien;

import static com.tungha.datn_timkiemnhatro.MainActivity.DATABASE_NAME;
import static com.tungha.datn_timkiemnhatro.MainActivity.arrTP;
import static com.tungha.datn_timkiemnhatro.MainActivity.database;
import static com.tungha.datn_timkiemnhatro.MainActivity.db;
import static com.tungha.datn_timkiemnhatro.MainActivity.mAuth;
import static com.tungha.datn_timkiemnhatro.TrangChinh.InfoUser;

public class FormDangNha extends AppCompatActivity {
    GiaTien giash=new GiaTien(0,0,0,0,0,0);
    Nha nha=null;
    Uri uri;
    ArrayList<Address> address = new ArrayList<>();
    DiaChi dc=null;
    DanhGia rating = new DanhGia(0,0,"");
    Calendar cal = Calendar.getInstance();
    boolean checksetgiash=false;
    byte[] hinhanh;
    Bitmap bitmap;
    String strTP,strQuan,strMoTa="",strSDT,strSoNha,strDuong,strPhuong,strEmailUser,strLoaiNha;
    Boolean trangthai,chungchu;
    Date ngaydang;
    ProgressDialog progressBar ;
    int vitrichonTP,vitrichonQuan,idTinhThanh;
    Button btnGiaSH,btnChonAnh,btnTrove,btnTieptuc;
    EditText edtslphong,edtsdt,edtsonha,edtduong,edtphuong,edtmota;
    Spinner spinTP,spinQuan,spinLoaiNha;
    Switch swChungChu;
    ImageView imgNha;
    Cursor c;
    public static boolean upanh=true;
    ArrayAdapter<String> adapter_TP=null;
    ArrayAdapter<String> adapter_Quan=null;
    ArrayAdapter<String> adapter_Loai=null;
    ArrayList<String> arrLoaiNha = new ArrayList<>();
    ArrayList<String> arrQuan = new ArrayList<>();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://timkiemnhatro-d501f.appspot.com");
    StorageReference hinhanhRef ;
    FirebaseUser user = mAuth.getCurrentUser();
    Geocoder coder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_dang_nha);
        AnhXa();
        XuLy();
        if(user!=null)
             Log.d("mailuser",""+user.getEmail());
        else
            Log.d("mailuser","không có user nào");
    }
    private void LayDuLieuTaoObjectNha(){
        Progress();
        strMoTa=edtmota.getText().toString();
        strSDT=edtsdt.getText().toString();
        ngaydang= new Date();

        if(swChungChu.isChecked()){
            chungchu=true;
        }else{
            chungchu=false;
        }
        trangthai=true;
        strSoNha=edtsonha.getText().toString();
        strDuong=edtduong.getText().toString();
        strPhuong=edtphuong.getText().toString();
        dc = new DiaChi(strTP,strQuan,strPhuong,strDuong,strSoNha);
        strEmailUser = user.getEmail();
        String idNha = ""+cal.getTimeInMillis();
        coder = new Geocoder(this, Locale.getDefault());
        for(int i=0;i<5;i++) {
            try {
                address = (ArrayList<Address>) coder.getFromLocationName(strSoNha +" "+ strDuong+","+strQuan+","+strTP+",Vietnam", 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(address.size()!=0) {
            nha = new Nha(strMoTa, strSDT, 0, ngaydang, chungchu, "", trangthai, dc, InfoUser, giash, strLoaiNha, rating, 0, false, address.get(0).getLatitude(), address.get(0).getLongitude());
        }else {
            nha = new Nha(strMoTa, strSDT, 0, ngaydang, chungchu, "", trangthai, dc, InfoUser, giash, strLoaiNha, rating, 0, false, 0,0);
        }
        nha.setId(idNha);
        rating.setIdNha(idNha);
        LayHinhAnhKieuByte();
        progressBar.dismiss();

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
    private void MoFormDSNCT_KemTheoDuLieuNha(){
        Intent i = new Intent(FormDangNha.this,DSNhaChiTiet.class);
        Bundle b = new Bundle();
        b.putSerializable("nha",nha);
        i.putExtra("hinhanh",hinhanh);
        i.putExtra("bundle",b);

        startActivity(i);
        finish();
    }


    private void LayHinhAnhKieuByte(){
        imgNha.setDrawingCacheEnabled(true);
        imgNha.buildDrawingCache();
        Bitmap bitmap = imgNha.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        hinhanh = baos.toByteArray();
    }

    private void XuLy() {
        btnTrove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnGiaSH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoFormGiaSH();
                checksetgiash=true;
            }
        });
        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkPermission()) {
                        pickImage();
                    } else {
                        requestPermission();
                    }
                }else
                    pickImage();
            }
        });
        btnTieptuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(KiemTraThongTin()==true) {
                    if(uri==null){
                        ThongBaoChuaPickAnh();
                    }else {
                        LayDuLieuTaoObjectNha();
                        MoFormDSNCT_KemTheoDuLieuNha();
                    }
                }
            }
        });
        LoadLoaiNha();
        LoadTP();
        XuLyKhiChonTP();
        XuLyKhiChonQuan();
        XuLyKhiChonLoaiNha();
        XuLySwitch();
    }
    private boolean KiemTraThongTin(){
        if(edtsdt.getText().toString().trim().length()==0){
            ThongBao("Vui lòng nhập số điện thoại");
            edtsdt.requestFocus();
            return false;
        }else if(edtsdt.getText().toString().trim().length()<10){
            ThongBao("Số điện thoại phải gồm 10 hoặc 11 số");
            edtsdt.requestFocus();
            return false;
        }else if(edtsonha.getText().toString().trim().length()==0
                || edtduong.getText().toString().trim().length()==0 || edtphuong.getText().toString().trim().length()==0){
            ThongBao("Vui lòng nhập đầy đủ thông tin địa chỉ");
            edtsonha.requestFocus();
            return false;
        } else if(checksetgiash==false){
            ThongBao("Vui lòng thiết lập chi phí sinh hoạt");
            btnGiaSH.requestFocus();
            return false;
        }
        return true;
    }
    private void XuLySwitch() {
        swChungChu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    swChungChu.setText("Chung chủ");
                }else
                    swChungChu.setText("Không chung chủ");
            }
        });
    }

    private void XuLyKhiChonTP() {
        spinTP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strTP=arrTP.get(position);
                vitrichonTP=position;
                arrQuan.clear();
                LayIDTinhThanh(strTP);
                LayDuLieuQuanHuyenTuTinhThanh(idTinhThanh);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void XuLyKhiChonQuan(){
        spinQuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strQuan=arrQuan.get(position);
                vitrichonQuan=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void XuLyKhiChonLoaiNha(){
        spinLoaiNha.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strLoaiNha=arrLoaiNha.get(position);
                adapter_Loai.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void LoadTP() {
        adapter_TP = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrTP);
        adapter_TP.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinTP.setAdapter(adapter_TP);
    }

    private void LoadLoaiNha() {
        Progress();
        adapter_Loai = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrLoaiNha);
        adapter_Loai.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinLoaiNha.setAdapter(adapter_Loai);
        db.collection("LoaiNha").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()) {
                        LoaiNha loai = document.toObject(LoaiNha.class);
                        arrLoaiNha.add(loai.getTenLoai().toString());
                    }
                    progressBar.dismiss();
                    adapter_Loai.notifyDataSetChanged();
                }
            }
        });

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
    public void Progress(){
        progressBar = new ProgressDialog(FormDangNha.this);
        progressBar.show();
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setContentView(R.layout.customdialog);

    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        //giảm size hình xuống
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    private void MoFormGiaSH(){
        Intent i  = new Intent(FormDangNha.this,MucGiaSinhHoat.class);
        Bundle b = new Bundle();
        b.putSerializable("giatiensh",giash);
        i.putExtra("bundle",b);
        startActivityForResult(i,1);
    }
    public void pickImage() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 123);
    }
    protected boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    protected void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Cần cho phép truy cập thư viện để lấy ảnh", Toast.LENGTH_LONG).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                } else {
                    Toast.makeText(this, "Đã từ chối cho phép truy cập thư viện , không thể lấy ảnh", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && data!=null)
        {
            switch(resultCode)
            {
                case 12:
                    Bundle b = data.getBundleExtra("data");
                    giash = (GiaTien) b.getSerializable("GiaSHTraVe");
                    break;
            }
        }
        if(requestCode==123 && data!=null){
            uri = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            selectedImage = getResizedBitmap(selectedImage, 555);
            Uri uri_resized = getImageUri(this,selectedImage);
            imgNha.setImageURI(uri_resized);
            /*try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgNha.setImageBitmap(bitmap);*/
        }
    }
    public void ThongBao(String thongbao){
        AlertDialog.Builder b = new AlertDialog.Builder(FormDangNha.this);
        b.setMessage(thongbao);
        b.setPositiveButton("Biết rồi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.create().show();
    }
    public void ThongBaoChuaPickAnh(){
        AlertDialog.Builder b = new AlertDialog.Builder(FormDangNha.this);
        b.setMessage("Bạn chưa chọn ảnh cho bài đăng của mình,hệ thống sẽ tự động chọn ảnh mặc định");
        b.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                upanh=false;
                LayDuLieuTaoObjectNha();
                MoFormDSNCT_KemTheoDuLieuNha();
            }
        });
        b.create().show();
    }
    private void AnhXa() {
        btnGiaSH= (Button) findViewById(R.id.btnGiaSH_DangNha);
        edtsdt= (EditText) findViewById(R.id.edtSDT_DangBai);
        edtsonha= (EditText) findViewById(R.id.edtSo_DangBai);
        edtduong= (EditText) findViewById(R.id.edtDuong_DangBai);
        edtphuong= (EditText) findViewById(R.id.edtPhuong_DangBai);
        edtmota= (EditText) findViewById(R.id.edtMoTa_DangNha);
        btnChonAnh= (Button) findViewById(R.id.btnChonAnh_DangNha);
        btnTrove= (Button) findViewById(R.id.btnThoat_DangBai);
        btnTieptuc= (Button) findViewById(R.id.btnTiepTuc_DangBai);
        spinTP= (Spinner) findViewById(R.id.spinTP_DangNha);
        spinQuan= (Spinner) findViewById(R.id.spinQuan_DangNha);
        spinLoaiNha= (Spinner) findViewById(R.id.spinLoaiPhong_DangNha);
        swChungChu= (Switch) findViewById(R.id.switchChungChu_DangBai);
        imgNha= (ImageView) findViewById(R.id.imgNha_DangNha);
    }
}
