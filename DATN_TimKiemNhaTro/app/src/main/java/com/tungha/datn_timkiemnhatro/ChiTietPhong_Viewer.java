package com.tungha.datn_timkiemnhatro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import Adapter.Adapter_DSAnhNCT;
import Classs.Database;
import Classs.NhaChiTiet;

import static com.tungha.datn_timkiemnhatro.MainActivity.DATABASE_NAME;
import static com.tungha.datn_timkiemnhatro.MainActivity.database;

public class ChiTietPhong_Viewer extends AppCompatActivity {
    TextView txtdientich,txttienthue,txttiencoc,txtslphong,txtngaydang,txtnguoi,txtxe,txtquydinh,txthopdong;
    ImageView imgnauan,imgbeprieng,imgtoilet,imgnhaxe,imgbv,imgml,imgmg,imgsan,imgst,imgtv,imgwifi;
    Button btnThoat;
    int dem=0;
    ProgressDialog progressBar;
    private int mInterval = 700; // 5 seconds by default, can be changed later
    private Handler mHandler;
    private Handler mHandlerChuyenHinh;
    RecyclerView reView_Anh;
    ArrayList<Bitmap> arrAnh = new ArrayList<>();
    Adapter_DSAnhNCT adapter;
    Cursor c;
    SQLiteDatabase database;
    NhaChiTiet nct;
    Toolbar toolbar;
    int demhinh=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_phong__viewer);
        AnhXa();
        NhanDuLieu();
        LoadAnhNCTTuSQLITE();
        startRepeatingTaskHinh();
    }



    private void NhanDuLieu() {
        Intent i = getIntent();
        Bundle b = i.getBundleExtra("bundle");
        nct = (NhaChiTiet) b.getSerializable("dulieunct");
        XuLyHienThiTextView();
        XuLyHienThiCheck();
    }
    private void LoadAnhNCTTuSQLITE(){
        Progress();
        arrAnh.clear();
        database = Database.initDatabase(this, DATABASE_NAME);
        c = database.rawQuery("SELECT Link FROM HinhNCT WHERE idNCT="+nct.getId(), null);
        dem=c.getCount();
            for (int j = 0; j < c.getCount(); j++) {
                c.moveToPosition(j);
                String linkhinh = c.getString(0);
                new DownloadImageTask(arrAnh).execute(linkhinh);
        }
        c.close();
        database.close();
        mHandler = new Handler();
        startRepeatingTask();

    }
    public void Progress(){
        progressBar = new ProgressDialog(ChiTietPhong_Viewer.this);
        progressBar.show();
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setContentView(R.layout.customdialog);

    }
    private void XuLyHienThiCheck() {
        if(nct.getYeuCau().getChoNauAn()==false){
            imgnauan.setImageResource(android.R.drawable.checkbox_off_background);
        }
        if(nct.getTienIch().getBepNauAn()==false){
            imgbeprieng.setImageResource(android.R.drawable.checkbox_off_background);
        }
        if(nct.getTienIch().getNhaVSRieng()==false){
            imgtoilet.setImageResource(android.R.drawable.checkbox_off_background);
        }
        if(nct.getTienIch().getChoDeXe()==false){
            imgnhaxe.setImageResource(android.R.drawable.checkbox_off_background);
        }
        if(nct.getTienIch().getBaoVe()==false){
            imgbv.setImageResource(android.R.drawable.checkbox_off_background);
        }
        if(nct.getTienIch().getMayLanh()==false){
            imgml.setImageResource(android.R.drawable.checkbox_off_background);
        }
        if(nct.getTienIch().getMayGiat()==false){
            imgmg.setImageResource(android.R.drawable.checkbox_off_background);
        }
        if(nct.getTienIch().getSanPhoiDo()==false){
            imgsan.setImageResource(android.R.drawable.checkbox_off_background);
        }
        if(nct.getTienIch().getSanThuong()==false){
            imgst.setImageResource(android.R.drawable.checkbox_off_background);
        }
        if(nct.getTienIch().getTruyenHinhCap()==false){
            imgtv.setImageResource(android.R.drawable.checkbox_off_background);
        }
        if(nct.getTienIch().getWifi()==false){
            imgwifi.setImageResource(android.R.drawable.checkbox_off_background);
        }
    }

    private void XuLyHienThiTextView() {
        String dt = NumberFormat.getNumberInstance(Locale.US).format(nct.getDienTich());
        String tienthue = NumberFormat.getNumberInstance(Locale.US).format(nct.getGiaThue());
        String coc = NumberFormat.getNumberInstance(Locale.US).format(nct.getTienCoc());
        String slp = NumberFormat.getNumberInstance(Locale.US).format(nct.getSoLuongPhong());
        String sln = NumberFormat.getNumberInstance(Locale.US).format(nct.getYeuCau().getSoLuongNguoi());
        String slx = NumberFormat.getNumberInstance(Locale.US).format(nct.getYeuCau().getSoLuongXe());
        String hd = NumberFormat.getNumberInstance(Locale.US).format(nct.getYeuCau().getHopDong());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String ngaythue = sdf.format(nct.getNgayNhan());
                txtdientich.setText(dt+" m2");
                txttienthue.setText(tienthue+" VNĐ");
                txttiencoc.setText(coc+" VNĐ");
                txtslphong.setText(slp);
                txtngaydang.setText(ngaythue);
                txtnguoi.setText(sln);
                txtxe.setText(slx);
                txtquydinh.setText(nct.getYeuCau().getGioGiac());
                txthopdong.setText(hd+" tháng");
    }

    private void AnhXa() {
        mHandlerChuyenHinh = new Handler();
        txtdientich = (TextView) findViewById(R.id.txtDienTich_CTPhong);
        txttienthue = (TextView) findViewById(R.id.txtTienThue_CTPhong);
        txttiencoc = (TextView) findViewById(R.id.txtTienCoc_CTPhong);
        txtslphong = (TextView) findViewById(R.id.txtSLPhong_CTPhong);
        txtngaydang = (TextView) findViewById(R.id.txtNgayThue_CTPhong);
        txtnguoi = (TextView) findViewById(R.id.txtSoNguoi_CTPhong);
        txtxe = (TextView) findViewById(R.id.txtSoXe_CTPhong);
        txtquydinh = (TextView) findViewById(R.id.txtGioGiac_CTPhong);
        txthopdong = (TextView) findViewById(R.id.txtHopDong_CTPhong);

        imgnauan = (ImageView) findViewById(R.id.imgCheckNauAn_CTPhong);
        imgbeprieng = (ImageView) findViewById(R.id.imgCheckTienIchBep_CTPhong);
        imgtoilet = (ImageView) findViewById(R.id.imgCheckTienIchToilet_CTPhong);
        imgnhaxe = (ImageView) findViewById(R.id.imgCheckTienIchXe_CTPhong);
        imgbv = (ImageView) findViewById(R.id.imgCheckTienIchBV_CTPhong);
        imgml = (ImageView) findViewById(R.id.imgCheckTienIchMaylanh_CTPhong);
        imgmg = (ImageView) findViewById(R.id.imgCheckTienIchMaygiat_CTPhong);
        imgsan = (ImageView) findViewById(R.id.imgCheckTienIchSan_CTPhong);
        imgst = (ImageView) findViewById(R.id.imgCheckTienIchST_CTPhong);
        imgtv = (ImageView) findViewById(R.id.imgCheckTienIchTV_CTPhong);
        imgwifi = (ImageView) findViewById(R.id.imgCheckTienIchWifi_CTPhong);
        btnThoat= (Button) findViewById(R.id.btnThoat_CTPhong);
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        reView_Anh= (RecyclerView) findViewById(R.id.reView_AnhCTPhong_Viewer_CTPhong);
        reView_Anh.setHasFixedSize(true);
        reView_Anh.setDrawingCacheEnabled(true);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(ChiTietPhong_Viewer.this, LinearLayoutManager.HORIZONTAL, false);
        reView_Anh.setLayoutManager(linearLayoutManager2);
        adapter = new Adapter_DSAnhNCT(arrAnh,getApplicationContext());
        reView_Anh.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void AddAnh(ArrayList<Bitmap> arrbitmap, Bitmap bitmap){
        arrbitmap.add(bitmap);
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
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if(arrAnh.size()==dem){
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
    Runnable chuyenhinh = new Runnable() {
        @Override
        public void run() {
            try {
                if(arrAnh!=null && arrAnh.size()>=2 && demhinh<arrAnh.size()){
                    reView_Anh.scrollToPosition(demhinh);
                    demhinh++;
                }else{
                    demhinh=0;
                }

            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(chuyenhinh, 4000);
            }
        }
    };
    void startRepeatingTaskHinh() {
        chuyenhinh.run();
    }

    void stopRepeatingTaskHinh() {
        mHandlerChuyenHinh.removeCallbacks(chuyenhinh);
    }
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
        stopRepeatingTaskHinh();
    }
}
