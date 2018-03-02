package com.tungha.datn_timkiemnhatro;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import Adapter.Adapter_DSAnhNCT;
import Classs.Database;
import Classs.HinhAnhNhaChiTiet;
import Classs.Nha;
import Classs.NhaChiTiet;
import Classs.TienIchNCT;
import Classs.YeuCauNCT;

import static com.tungha.datn_timkiemnhatro.MainActivity.DATABASE_NAME;
import static com.tungha.datn_timkiemnhatro.MainActivity.database;
import static com.tungha.datn_timkiemnhatro.MainActivity.db;
import static com.tungha.datn_timkiemnhatro.MainActivity.mAuth;

public class FormThemNhaChiTiet_SuaNha extends AppCompatActivity {
    ProgressDialog progressBar ;
    YeuCauNCT yeucau = new YeuCauNCT(0,0,"Tự do",false,0);
    TienIchNCT tienich= new TienIchNCT(false,false,false,false,false,false,false,false,false,false);
    int dientich,soluong,tiencoc,tienthue;
    Calendar cal = Calendar.getInstance();
    Date ngaythue;
    Cursor c;
    byte[] hinhanh,hinhdaidien;
    int nam,thang,ngay;
    Boolean checkdanhapyeucau=false,checkdanhaptienich=false;
    EditText edtdientich,edttienthue,edttiencoc,edtsoluong;
    TextView txtNgayNhan;
    ImageView imgPickngay,imgGiaiDap,btnPickAnh;
    Button btnLuu,btnHuy,btnYC,btnTienIch;
    RecyclerView reView_HinhNCT;
    ArrayList<Bitmap> arrHinh = new ArrayList<>();
    Adapter_DSAnhNCT adapter;
    NhaChiTiet nct,nctdesua;
    String idNha,strHinhAnh,idNhaCT;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://timkiemnhatro-d501f.appspot.com");
    StorageReference hinhanhRef ;
    FirebaseUser user = mAuth.getCurrentUser();
    ArrayList<NhaChiTiet> arrNCTupdate = new ArrayList<>();
    private int mInterval = 700; // 5 seconds by default, can be changed later
    private Handler mHandler;
    int dem=0,mintien,sophong;
    ArrayList<String> arrIDDOC = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_them_nha_chi_tiet);
        AnhXa();
        XuLy();
        NhanDuLieuDeSua();
        DownloadIDDOCANH();
        LoadAnhNCTTuSQLITE();
    }

    private void DownloadIDDOCANH() {
        db.collection("HinhAnhCuaNhaChiTiet").whereEqualTo("idNhaChiTiet",nctdesua.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
                        String id = doc.getId();
                        arrIDDOC.add(id);
                    }
                }
            }
        });
    }
    private void DownloadNCTofNha(){
        db.collection("DSNhaChiTiet").whereEqualTo("idNha",nctdesua.getIdNha()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
                        NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                        arrNCTupdate.add(nct);
                    }
                    TimMinGiaPhong();
                    LaySoPhong();
                    UpdateNha();
                }
            }
        });
    }

    private void UpdateNha() {
        db.collection("Nha").document(nctdesua.getIdNha()).update(
                "giaThapNhat",mintien,
                "soLuongPhong",sophong);
    }

    private void TimMinGiaPhong() {
        mintien=arrNCTupdate.get(0).getGiaThue();
        for(int i=1;i<arrNCTupdate.size();i++){
            if(arrNCTupdate.get(i).getGiaThue() < mintien){
                mintien=arrNCTupdate.get(i).getGiaThue();
            }
        }
    }

    private void LaySoPhong() {
        for(int i=0;i<arrNCTupdate.size();i++){
            sophong+=arrNCTupdate.get(i).getSoLuongPhong();
        }
    }

    private void NhanDuLieuDeSua(){
        Intent i = getIntent();
        Bundle b=i.getBundleExtra("bundle");
        nctdesua= (NhaChiTiet) b.getSerializable("dulieunct");
        edtdientich.setText(String.valueOf(nctdesua.getDienTich()));
        edttienthue.setText(String.valueOf(nctdesua.getGiaThue()));
        ngaythue = nctdesua.getNgayNhan();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ngay = sdf.format(ngaythue);
        txtNgayNhan.setText(ngay);
        edtsoluong.setText(String.valueOf(nctdesua.getSoLuongPhong()));
        edttiencoc.setText(String.valueOf(nctdesua.getTienCoc()));
        yeucau=nctdesua.getYeuCau();
        tienich=nctdesua.getTienIch();

    }
     public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        ArrayList<Bitmap> bitmap = new ArrayList<>();

        public DownloadImageTask(ArrayList<Bitmap> bitmap) {
            this.bitmap = bitmap;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            AddAnh(bitmap, result);
        }
    }
    public class CapNhatNha extends AsyncTask<Void,Void,Void> {

        public CapNhatNha() {
        }
        @Override
        protected Void doInBackground(Void... params) {
            DownloadNCTofNha();
            UpAnhLenStorage();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(FormThemNhaChiTiet_SuaNha.this, "Cập nhật hoàn tất!", Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
        }
    }
    public void AddAnh(ArrayList<Bitmap> arrbitmap, Bitmap bitmap){
        arrbitmap.add(bitmap);
    }
    private void LoadAnhNCTTuSQLITE(){
        Progress();
        arrHinh.clear();
        database = Database.initDatabase(this, DATABASE_NAME);
        c = database.rawQuery("SELECT Link FROM HinhNCT WHERE idNCT="+nctdesua.getId(), null);
        dem=c.getCount();
        for (int j = 0; j < c.getCount(); j++) {
            c.moveToPosition(j);
            String linkhinh = c.getString(0);
            new DownloadImageTask(arrHinh).execute(linkhinh);
        }
        c.close();
        database.close();
        mHandler = new Handler();
        startRepeatingTask();


    }
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if(arrHinh.size()==dem){
                    adapter.notifyDataSetChanged();
                    progressBar.dismiss();
                }
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };
    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }
    private void XuLy() {
        XuLyNhapYC();
        XuLyNhapTI();
        XuLyPickNgay();
        XuLyRCCVAnh();
        XuLyPickAnh();
        XuLyKhiBamXacNhan();
    }

    private void XuLyKhiBamXacNhan() {
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(user!=null) {
                    if(KiemTraThongTin()==true) {
                        Toast.makeText(FormThemNhaChiTiet_SuaNha.this, "Đang xử lý...", Toast.LENGTH_LONG).show();
                        LayDuLieuTuForm();
                    }
                }else {
                    ThongBao("KO CO USER");
                }
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void UpAnhLenStorage() {
        for(int i=0;i<arrHinh.size();i++){
            String child = UUID.randomUUID().toString();
            hinhanhRef = storageRef.child(child);
            Uri uri_resized = getImageUri(this,arrHinh.get(i));
            UploadTask uploadTask = hinhanhRef.putFile(uri_resized);
            final int finalI = i;
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(FormThemNhaChiTiet_SuaNha.this, "Upload ảnh gặp sự cố! Thử lại sau", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    strHinhAnh= String.valueOf(taskSnapshot.getDownloadUrl()); // << chỗ lấy đc link ảnh
                    HinhAnhNhaChiTiet anh = new HinhAnhNhaChiTiet(nctdesua.getId(),strHinhAnh);
                    if(finalI<arrIDDOC.size()) {
                        db.collection("HinhAnhCuaNhaChiTiet").document(arrIDDOC.get(finalI)).delete();
                    }
                    db.collection("HinhAnhCuaNhaChiTiet").add(anh);
            }
        });
        }

    }

    private void LayDuLieuTuForm() {
        tiencoc=Integer.parseInt(edttiencoc.getText().toString());
        tienthue=Integer.parseInt(edttienthue.getText().toString());
        dientich=Integer.parseInt(edtdientich.getText().toString());
        soluong=Integer.parseInt(edtsoluong.getText().toString());
        nct = new NhaChiTiet(ngaythue,tiencoc,dientich,soluong,tienthue,tienich,yeucau,nctdesua.getIdNha());
        nct.setId(nctdesua.getId());
        db.collection("DSNhaChiTiet").document(nctdesua.getId()).set(nct).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                new CapNhatNha().execute();
                finish();
            }
        });
    }


    private void XuLyPickAnh() {
        btnPickAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkPermission()) {
                        arrHinh.clear();
                        MoThuVienAnh();
                    } else {
                        requestPermission();
                    }
                }else
                    MoThuVienAnh();
            }
        });
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
                    MoThuVienAnh();
                } else {
                    Toast.makeText(this, "Đã từ chối cho phép truy cập thư viện , không thể lấy ảnh", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void MoThuVienAnh() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 4);
    }

    private void XuLyRCCVAnh() {

        reView_HinhNCT.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        reView_HinhNCT.setLayoutManager(linearLayoutManager);
        adapter = new Adapter_DSAnhNCT(arrHinh, getApplicationContext());
        reView_HinhNCT.setAdapter(adapter);
    }

    private void XuLyPickNgay() {
        imgPickngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        imgGiaiDap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThongBao("Ngày khách hàng có thể bắt đầu thuê");
            }
        });
    }

    private void XuLyNhapTI() {
        btnTienIch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoFormTIKemDuLieu();
                checkdanhaptienich=true;
            }
        });
    }

    private void XuLyNhapYC() {
        btnYC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoFormYCKemDuLieu();
            }
        });
    }
    private void MoFormTIKemDuLieu(){
        Intent i = new Intent(FormThemNhaChiTiet_SuaNha.this,FormTienIch.class);
        Bundle b = new Bundle();
        b.putSerializable("tienich",tienich);
        i.putExtra("bundle",b);
        startActivityForResult(i,3);
    }
    private void MoFormYCKemDuLieu(){
        Intent i = new Intent(FormThemNhaChiTiet_SuaNha.this,FormYeuCau.class);
        Bundle b = new Bundle();
        b.putSerializable("yeucau",yeucau);
        i.putExtra("bundle",b);
        startActivityForResult(i,3);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && data != null) {
            switch (resultCode) {
                case 33:
                    Bundle b = data.getBundleExtra("data");
                    checkdanhapyeucau=b.getBoolean("checkdanhapyeucau");
                    yeucau = (YeuCauNCT) b.getSerializable("yeucau_trave");
                    break;
                case 44:
                    Bundle b2 = data.getBundleExtra("data");
                    tienich = (TienIchNCT) b2.getSerializable("tienich_trave");
                    break;
            }
        }
        if (requestCode==4 && data.getClipData() != null) {

            ClipData mClipData = data.getClipData();
            Progress();
            for (int i = 0; i < mClipData.getItemCount(); i++) {
                ClipData.Item item = mClipData.getItemAt(i);
                Uri uri = item.getUri();
                ////////// uri -> bitmap
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                //////////////////////////////
                selectedImage = getResizedBitmap(selectedImage, 555);
                //Uri uri_resized = getImageUri(this,selectedImage);
                arrHinh.add(selectedImage);
            }
            Toast.makeText(this, "Thêm ảnh thành công", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
            if (progressBar.isShowing())
            progressBar.dismiss();
        }
        if(requestCode==4 && data.getClipData() == null){
            ThongBao("Vui lòng chọn ít nhất 2 ảnh cho phòng");
        }
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

    public void showDatePickerDialog() {
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
                    cal = Calendar.getInstance();
                    txtNgayNhan.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    cal.set(year, month, dayOfMonth);
                    ngaythue = cal.getTime();
                    Log.d("TEST",ngaythue.toString());
                } else {
                    ThongBao("Ngày có thể thuê không nhỏ hơn ngày hiện tại!");
                }
            }
        };
            String s = txtNgayNhan.getText() + "";
        if (txtNgayNhan.getText().toString().equals("")) {
            Date date = new Date(); // your date
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            nam = year;
            thang = month;
            ngay = day;
        } else {
            String strArrtmp[] = s.split("-");
            nam = Integer.parseInt(strArrtmp[0]);
            thang = Integer.parseInt(strArrtmp[1]) - 1;
            ngay = Integer.parseInt(strArrtmp[2]);
        }
        DatePickerDialog pic = new DatePickerDialog(FormThemNhaChiTiet_SuaNha.this, callback, nam, thang, ngay);
        pic.setTitle("Ngày thuê ");
        pic.show();
    }
    public void ThongBao(String thongbao){
        AlertDialog.Builder b = new AlertDialog.Builder(FormThemNhaChiTiet_SuaNha.this);
        b.setMessage(thongbao);
        b.setPositiveButton("Biết rồi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.create().show();
    }
    private Boolean KiemTraThongTin(){
        if(edtdientich.getText().toString().trim().length()==0){
            ThongBao("Vui lòng nhập diện tích phòng");
            edtdientich.requestFocus();
            return false;
        }else if(edttienthue.getText().toString().trim().length()==0){
            ThongBao("Vui lòng nhập giá thuê phòng");
            edttienthue.requestFocus();
            return false;
        }else if(edttiencoc.getText().toString().trim().length()==0){
            ThongBao("Vui lòng nhập tiền cọc phòng");
            edttiencoc.requestFocus();
            return false;
        }else if(edttiencoc.getText().toString().trim().length()!=0){
            if(Integer.parseInt(edttiencoc.getText().toString()) > Integer.parseInt(edttienthue.getText().toString())){
                ThongBao("Tiền cọc không được lớn hơn tiền thuê nhà");
                edttiencoc.requestFocus();
                return false;
            }
        }
        else if(edtsoluong.getText().toString().trim().length()==0){
            ThongBao("Vui lòng nhập số lượng phòng đang có");
            edtsoluong.requestFocus();
            return false;
        }else if(txtNgayNhan.getText().toString().trim().length()==0){
            ThongBao("Vui lòng chọn ngày có thể bắt đầu thuê phòng");
            txtNgayNhan.requestFocus();
            return false;
        }
        return true;
    }
    public void Progress(){
        progressBar = new ProgressDialog(FormThemNhaChiTiet_SuaNha.this);
        progressBar.show();
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setContentView(R.layout.customdialog);

    }
    private void AnhXa() {
        edtsoluong= (EditText) findViewById(R.id.edtSoLuong_FormNCT);
        edtdientich= (EditText) findViewById(R.id.edtDienTich_FormNCT);
        edttienthue= (EditText) findViewById(R.id.edtTienThue_FormNCT);
        edttiencoc= (EditText) findViewById(R.id.edtTienCoc_FormNCT);
        txtNgayNhan= (TextView) findViewById(R.id.txtHienThiNgay_FormNCT);
        imgPickngay= (ImageView) findViewById(R.id.imgPickNgay_FormNCT);
        imgGiaiDap= (ImageView) findViewById(R.id.imgGiaiDapPickNgay_FormNCT);
        btnPickAnh= (ImageView) findViewById(R.id.btnPickAnh_FormNCT);
        btnLuu= (Button) findViewById(R.id.btnXacNhan_FormNCT);
        btnHuy= (Button) findViewById(R.id.btnHuy_FormNCT);
        btnYC= (Button) findViewById(R.id.btnYeuCau_FormNCT);
        btnTienIch= (Button) findViewById(R.id.btnTienIch_FormNCT);
        reView_HinhNCT= (RecyclerView) findViewById(R.id.reView_DSAnh_FormNCT);
        reView_HinhNCT.setNestedScrollingEnabled(false);
    }
}
