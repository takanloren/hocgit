package com.tungha.datn_timkiemnhatro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.MapMaker;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import Adapter.Adapter_DSBinhLuan;
import Adapter.Adapter_DSDanhGia;
import Adapter.Adapter_DSNhaCT;
import Classs.BinhLuan;
import Classs.DanhGia;
import Classs.DanhGiaChiTiet;
import Classs.Database;
import Classs.DiaChi;
import Classs.Nha;
import Classs.NhaChiTiet;

import static com.tungha.datn_timkiemnhatro.MainActivity.DATABASE_NAME;
import static com.tungha.datn_timkiemnhatro.MainActivity.database;
import static com.tungha.datn_timkiemnhatro.MainActivity.db;
import static com.tungha.datn_timkiemnhatro.MainActivity.inFromRightAnimation;
import static com.tungha.datn_timkiemnhatro.MainActivity.mAuth;
import static com.tungha.datn_timkiemnhatro.MainActivity.outToRightAnimation;
import static com.tungha.datn_timkiemnhatro.TrangChinh.InfoUser;

public class ChiTietBaiDang extends AppCompatActivity {
    ImageView imgNha;
    TextView txtdiachi,txtsophong,txtloainha,txtchungchu,txtsdt,txtmota;
    EditText edtnoidung;
    Button btngiash,btnguibl,btnguidg,btntrove;
    Nha nha;
    TabHost tab;
    Cursor c;
    int currentTab=0,luotdanhgia=0;
    float diemtrungbinh=0;
    public static String emailuser_static;
    private int mInterval = 1555; // 5 seconds by default, can be changed later
    private Handler mHandler;
    ProgressDialog progressBar;
    RecyclerView reView_DSPhong,reView_DSBL,reView_DSDG;
    ArrayList<NhaChiTiet> arrNCT = new ArrayList<>();
    ArrayList<Bitmap> arrAnhNCT = new ArrayList<>();
    Adapter_DSNhaCT adapter_dsNhaCT;
    Adapter_DSBinhLuan adapter_dsBinhLuan;
    Adapter_DSDanhGia adapter_dsDanhGia;
    ArrayList<String> arrlinkhinh = new ArrayList<>();
    ArrayList<BinhLuan> arrBL = new ArrayList<>();
    ArrayList<DanhGiaChiTiet> arrDG = new ArrayList<>();
    ListenerRegistration registration;
    ListenerRegistration registration2;
    RatingBar ratingbar_DG;
    MapFragment map;
    SupportMapFragment mapFragment;
    GoogleMap ggmap;
    ScrollView scroll;
    Geocoder coder ;
    String so,duong,quan,tp;
    TableRow tbrBL,tbrDG,tbrNouserBL,tbrNouserDG;
    FirebaseUser user = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_bai_dang);
        loadTabs();
        AnhXa();
        NhanDuLieu();
        DownloadNCT();
        XuLy();
    }

    private void XuLy() {
        btngiash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoFormGiaSH();
            }
        });
        XuLyGuiBinhLuan();
        XuLyGuiDanhGia();
        LoadBinhLuan();
        LoadDanhGia();
        XuLyMap();
    }
    private void TongHopDanhGia(){
        float tongdiem=0;
        for(int i=0;i<arrDG.size();i++){
            tongdiem+=arrDG.get(i).getDiem();
        }
        luotdanhgia=arrDG.size();
        diemtrungbinh=tongdiem/luotdanhgia;
        DanhGia danhgia = new DanhGia(luotdanhgia,diemtrungbinh,nha.getId());
        db.collection("Nha").document(nha.getId()).update(
                "danhGia.diemTrungBinh",diemtrungbinh,
                "danhGia.luotDanhGia",luotdanhgia
        );

    }
    private void LoadDanhGia() {
        arrDG.clear();
        registration2 = db.collection("DSDanhGiaChiTiet").whereEqualTo("idNha",nha.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            DanhGiaChiTiet dgct_add = dc.getDocument().toObject(DanhGiaChiTiet.class);
                            arrDG.add(dgct_add);
                            final long todayTime = new Date().getTime();
                            Collections.sort(arrDG, new Comparator<DanhGiaChiTiet>() {
                                public int compare(DanhGiaChiTiet nha1, DanhGiaChiTiet nha2) {
                                    Long dist1 = Math.abs(todayTime-nha1.getNgayDang().getTime());
                                    Long dist2 = Math.abs(todayTime-nha2.getNgayDang().getTime());
                                    return dist1.compareTo(dist2);
                                }

                            });
                            adapter_dsDanhGia.notifyDataSetChanged();
                            TongHopDanhGia();
                            break;
                        case MODIFIED:
                            DanhGiaChiTiet dgct_sua = dc.getDocument().toObject(DanhGiaChiTiet.class);
                            for(int i=0;i<arrDG.size();i++){
                                if(arrDG.get(i).getUser().getEmail().compareTo(dgct_sua.getUser().getEmail())==0){
                                    arrDG.remove(i);
                                    arrDG.add(dgct_sua);
                                }
                            }
                            final long todayTime2 = new Date().getTime();
                            Collections.sort(arrDG, new Comparator<DanhGiaChiTiet>() {
                                public int compare(DanhGiaChiTiet nha1, DanhGiaChiTiet nha2) {
                                    Long dist1 = Math.abs(todayTime2-nha1.getNgayDang().getTime());
                                    Long dist2 = Math.abs(todayTime2-nha2.getNgayDang().getTime());
                                    return dist1.compareTo(dist2);
                                }

                            });
                            adapter_dsDanhGia.notifyDataSetChanged();
                            TongHopDanhGia();
                            break;
                        case REMOVED:
                            adapter_dsDanhGia.notifyDataSetChanged();
                            break;
                    }
                }
            }
        });
    }
    private void LoadBinhLuan() {
        arrBL.clear();
        registration = db.collection("DSBinhLuan").whereEqualTo("idNha",nha.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            BinhLuan bl_add = dc.getDocument().toObject(BinhLuan.class);
                            arrBL.add(bl_add);
                            final long todayTime = new Date().getTime();
                            Collections.sort(arrBL, new Comparator<BinhLuan>() {
                                public int compare(BinhLuan nha1, BinhLuan nha2) {
                                    Long dist1 = Math.abs(todayTime-nha1.getNgayDang().getTime());
                                    Long dist2 = Math.abs(todayTime-nha2.getNgayDang().getTime());
                                    return dist1.compareTo(dist2);

                                }

                            });
                            adapter_dsBinhLuan.notifyDataSetChanged();
                            break;
                        case MODIFIED:
                            adapter_dsBinhLuan.notifyDataSetChanged();
                            break;
                        case REMOVED:
                            adapter_dsBinhLuan.notifyDataSetChanged();
                            break;
                    }
                }

            }
        });
    }
    private void XuLyGuiDanhGia() {
        btnguidg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float diem = ratingbar_DG.getRating();
                String id = UUID.randomUUID().toString();
                Date today = new Date();
                String idnha=nha.getId();
                DanhGiaChiTiet dgct = new DanhGiaChiTiet(id,diem,today,InfoUser,idnha);
                db.collection("DSDanhGiaChiTiet").document(InfoUser.getEmail()+"."+idnha).set(dgct);
            }
        });
    }
    private void XuLyGuiBinhLuan() {
        btnguibl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = UUID.randomUUID().toString();
                Date ngaydang = new Date();
                String noidung = edtnoidung.getText().toString();
                String idnha = nha.getId();
                BinhLuan bl = new BinhLuan(id,ngaydang,noidung,true,InfoUser,idnha);
                db.collection("DSBinhLuan").document(id).set(bl);
                edtnoidung.setText("");
            }
        });
    }
    private void XuLyMap(){
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                onMyMapReady(googleMap);
                //Toast.makeText(ChiTietBaiDang.this, "chay ham xu ly map", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void onMyMapReady(GoogleMap googleMap) {
        ggmap = googleMap;
        coder = new Geocoder(this, Locale.getDefault());
        so=nha.getDiaChi().getSoNha();
        duong=nha.getDiaChi().getDuong();
        quan=nha.getDiaChi().getQuan();
        tp=nha.getDiaChi().getThanhPho();
        ggmap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            double longitude,latitude;
            ArrayList<Address> adresses;
            @Override
            public void onMapLoaded() {
                  try {
                      for(int i=0;i<3;i++) {
                          adresses = (ArrayList<Address>) coder.getFromLocationName(so +" "+ duong+","+quan+","+tp+",Vietnam", 1);
                      }
                        if(adresses.size()!=0) {
                            longitude = adresses.get(0).getLongitude();
                            latitude = adresses.get(0).getLatitude();
                            LatLng house = new LatLng(latitude,longitude);
                            ggmap.addMarker(new MarkerOptions().position(house).title(so+" "+duong+","+quan+","+tp)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))).showInfoWindow();
                            ggmap.moveCamera(CameraUpdateFactory.newLatLngZoom(house,16));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        });
        ggmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        ggmap.getUiSettings().setZoomControlsEnabled(true);
    }
    private void MoFormGiaSH(){
        Intent i  = new Intent(ChiTietBaiDang.this, GiaSH_Viewer.class);
        Bundle b = new Bundle();
        b.putSerializable("giatiensh",nha.getGiaTien());
        i.putExtra("bundle",b);
        startActivity(i);
    }
    private void DownloadNCT(){
        Progress();
        db.collection("DSNhaChiTiet")
                .whereEqualTo("idNha",nha.getId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()) {
                        NhaChiTiet nct = document.toObject(NhaChiTiet.class);
                        arrNCT.add(nct);
                    }
                    LoadAnhNCTTuSQLITE();
                    LoadAnhVaoImage();
                }

            }
        });

    }
    private void LoadAnhNCTTuSQLITE(){
        arrlinkhinh.clear();
        database = Database.initDatabase(this, DATABASE_NAME);
        for(int i =0;i<arrNCT.size();i++){
            c = database.rawQuery("SELECT Link FROM HinhNCT WHERE idNCT='"+arrNCT.get(i).getId()+"' LIMIT 1", null);
            for (int j = 0; j < c.getCount(); j++) {
                c.moveToPosition(j);
                String linkhinh = c.getString(0);
                arrlinkhinh.add(linkhinh);
            }
        }
        c.close();
        database.close();
    }
    private void LoadAnhVaoImage(){
        for(int i=0;i<arrlinkhinh.size();i++){
            new DownloadImageTask(arrAnhNCT).execute(arrlinkhinh.get(i));
        }
        mHandler = new Handler();
        startRepeatingTask();
    }
    private void NhanDuLieu(){
        Intent i = getIntent();
        Bundle b= i.getBundleExtra("data");
        nha = (Nha) b.getSerializable("dulieunha");
        emailuser_static = nha.getUser().getEmail();
        HienThiThongTin();
    }

    private void HienThiThongTin() {
        Picasso.with(this).load(nha.getHinhAnh()).into(imgNha);
        DiaChi dc = nha.getDiaChi();
        txtdiachi.setText(dc.getSoNha()+" "+dc.getDuong()+","+dc.getPhuong()+","+dc.getQuan()+","+dc.getThanhPho());
        txtsophong.setText(String.valueOf(nha.getSoLuongPhong()));
        txtloainha.setText(nha.getLoaiNha().toString());
        if(nha.getChungChu()==true)
            txtchungchu.setText("Chung chủ");
        else
            txtchungchu.setText("Không chung chủ");
        txtsdt.setText(nha.getSoDienThoai());
        txtmota.setText(nha.getMoTa());
    }

    private void AnhXa() {
        btntrove= (Button) findViewById(R.id.btnThoat_CTBai);
        btntrove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        tbrNouserBL= (TableRow) findViewById(R.id.tbr_NoUser_BinhLuan);
        tbrNouserDG= (TableRow) findViewById(R.id.tbr_NoUser_DanhGia);
        tbrBL= (TableRow) findViewById(R.id.tbr_BinhLuan);
        tbrDG= (TableRow) findViewById(R.id.tbr_DanhGia);
        if(user==null){
            tbrBL.setVisibility(View.GONE);
            tbrDG.setVisibility(View.GONE);
            tbrNouserBL.setVisibility(View.VISIBLE);
            tbrNouserDG.setVisibility(View.VISIBLE);
        }
        map = ((MapFragment)getFragmentManager().
                findFragmentById(R.id.map));
        scroll= (ScrollView) findViewById(R.id.scrollNoiDungBai);
        ///Đánh giá
        ratingbar_DG= (RatingBar) findViewById(R.id.ratingBar_DanhGia);
        btnguidg= (Button) findViewById(R.id.btnGui_DanhGia);
        //BL
        edtnoidung= (EditText) findViewById(R.id.edtNoiDung_BinhLuan);
        btnguibl= (Button) findViewById(R.id.btnGuiBL_BinhLuan);
        ////
        imgNha= (ImageView) findViewById(R.id.imgHinh_CTBai);
        txtdiachi= (TextView) findViewById(R.id.txtDiaChi_CTBai);
        txtsophong= (TextView) findViewById(R.id.txtSoPhong_CTBai);
        txtloainha= (TextView) findViewById(R.id.txtLoaiNha_CTBai);
        txtchungchu= (TextView) findViewById(R.id.txtChungChu_CTBai);
        txtsdt= (TextView) findViewById(R.id.txtSDT_CTBai);
        txtmota= (TextView) findViewById(R.id.txtMoTa_CTBai);
        btngiash= (Button) findViewById(R.id.btnGiaSH_CTBai);
        btngiash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter_dsNhaCT.notifyDataSetChanged();
            }
        });
        /////rv dsphong
        reView_DSPhong= (RecyclerView) findViewById(R.id.reViewDSPhong_CTBai);
        reView_DSPhong.setNestedScrollingEnabled(false);
        reView_DSPhong.setFocusable(false);
        reView_DSPhong.setHasFixedSize(true);
        reView_DSPhong.setDrawingCacheEnabled(true);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(ChiTietBaiDang.this, LinearLayoutManager.VERTICAL, false);
        reView_DSPhong.setLayoutManager(linearLayoutManager2);
        adapter_dsNhaCT = new Adapter_DSNhaCT(arrAnhNCT,arrNCT,ChiTietBaiDang.this,ChiTietBaiDang.this);
        reView_DSPhong.setAdapter(adapter_dsNhaCT);
        //////rv binhluan
        reView_DSBL= (RecyclerView) findViewById(R.id.reView_BinhLuan);
        reView_DSBL.setFocusable(false);
        reView_DSBL.setHasFixedSize(true);
        reView_DSBL.setDrawingCacheEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChiTietBaiDang.this, LinearLayoutManager.VERTICAL, false);
        reView_DSBL.setLayoutManager(linearLayoutManager);
        adapter_dsBinhLuan = new Adapter_DSBinhLuan(arrBL,getApplicationContext());
        reView_DSBL.setAdapter(adapter_dsBinhLuan);
        /////rv danh gia
        reView_DSDG= (RecyclerView) findViewById(R.id.reView_DanhGia);
        reView_DSDG.setFocusable(false);
        reView_DSDG.setHasFixedSize(true);
        reView_DSDG.setDrawingCacheEnabled(true);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(ChiTietBaiDang.this, LinearLayoutManager.VERTICAL, false);
        reView_DSDG.setLayoutManager(linearLayoutManager3);
        adapter_dsDanhGia = new Adapter_DSDanhGia(arrDG,getApplicationContext());
        reView_DSDG.setAdapter(adapter_dsDanhGia);

    }
    public void Progress(){
        progressBar = new ProgressDialog(ChiTietBaiDang.this);
        progressBar.show();
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setContentView(R.layout.customdialog);

    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void AddAnh(ArrayList<Bitmap> arrbitmap,Bitmap bitmap){
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
                if(arrlinkhinh.size()==arrAnhNCT.size()){
                    adapter_dsNhaCT.notifyDataSetChanged();
                    progressBar.dismiss();
                }
            } finally {
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
        registration.remove();
        registration2.remove();
    }
    public void loadTabs() {

        tab = (TabHost) findViewById(R.id.tabAdmin_TC);
        //gọi lệnh setup

        tab.setup();
        tab.getTabWidget().setStripEnabled(false);
        TabHost.TabSpec spec;
        //Tạo tab1
        spec = tab.newTabSpec("t1");
        spec.setContent(R.id.tabNoiDungBaiDang);
        spec.setIndicator("Thông tin");
        tab.addTab(spec);
        //Tạo tab2
        spec = tab.newTabSpec("t2");
        spec.setContent(R.id.tabBinhLuan);
        spec.setIndicator("Bình luận");
        tab.addTab(spec);
        ////t3
        spec = tab.newTabSpec("t3");
        spec.setContent(R.id.tabDanhGia);
        spec.setIndicator("Đánh giá");
        tab.addTab(spec);
        //Thiết lập tab mặc định được chọn ban đầu là tab 0
        tab.setCurrentTab(0);

        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                View currentView = tab.getCurrentView();
                switch (tab.getCurrentTab()){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;

                }
                if (tab.getCurrentTab() > currentTab)
                {
                    currentView.setAnimation( inFromRightAnimation() );
                    currentTab=tab.getCurrentTab();
                }
                else
                {
                    currentView.setAnimation( outToRightAnimation() );
                    currentTab=tab.getCurrentTab();
                }


            }
        });
        for (int i = 0; i < tab.getTabWidget().getChildCount(); i++) {
            tab.getTabWidget().getChildAt(i).getLayoutParams().height=75;
        }

    }
}
