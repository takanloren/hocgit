package com.tungha.datn_timkiemnhatro;

import android.*;
import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.readystatesoftware.viewbadger.BadgeView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import Adapter.Adapter_DSNha_DaDang;
import Adapter.Adapter_DSNha_DaLuu;
import Adapter.Adapter_DSNha_TrangChinh;
import Adapter.Adapter_ThongBao;
import Classs.BinhLuan;
import Classs.DanhGia;
import Classs.DanhGiaChiTiet;
import Classs.Database;
import Classs.HinhAnhNhaChiTiet;
import Classs.LoaiNha;
import Classs.LuuBai;
import Classs.Nha;
import Classs.NhaChiTiet;
import Classs.ThongBao;
import Classs.TinDKTB;
import Classs.User;
import Classs.abc;
import de.hdodenhof.circleimageview.CircleImageView;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

import static android.R.id.tabs;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static com.tungha.datn_timkiemnhatro.KetQuaSauKhiSearch.arrnct_da_nhan;
import static com.tungha.datn_timkiemnhatro.MainActivity.DATABASE_NAME;
import static com.tungha.datn_timkiemnhatro.MainActivity.arrTP;
import static com.tungha.datn_timkiemnhatro.MainActivity.database;
import static com.tungha.datn_timkiemnhatro.MainActivity.db;
import static com.tungha.datn_timkiemnhatro.MainActivity.inFromRightAnimation;
import static com.tungha.datn_timkiemnhatro.MainActivity.mAuth;
import static com.tungha.datn_timkiemnhatro.MainActivity.outToRightAnimation;

public class TrangChinh extends AppCompatActivity {
    FirebaseUser user = mAuth.getCurrentUser();
    static TabHost tab = null;
    int currentTab = 0;
    com.github.clans.fab.FloatingActionButton fabToTop;
    FloatingTextButton flbtn;
    String idNhaDaLuuMuonXoa;
    TableLayout tbly_DSluu;
    SwipeRefreshLayout swipe, swipe_Luu;
    ProgressDialog progressBar;
    android.support.v7.app.ActionBar actbar;
    public static User InfoUser;
    Button btnDangBai, btnDangNhapFromProfile;
    ImageView imgthongbao;
    RecyclerView reView_TC, reView_Luu, reView_DangBoiUser;
    public static ArrayList<Nha> arrNha = new ArrayList<>();
    ArrayList<Nha> arrNhaHienThiLoadMore = new ArrayList<>();
    public static ArrayList<BinhLuan> arrBLglobal = new ArrayList<>();
    public static ArrayList<DanhGiaChiTiet> arrDGglobal = new ArrayList<>();
    public static ArrayList<LuuBai> arrLB = new ArrayList<>();
    TabHost.TabSpec spec;
    ArrayList<Nha> arrNhaDangBoiUser = new ArrayList<>();
    ArrayList<Nha> arrDSLuu = new ArrayList<>();
    Adapter_DSNha_TrangChinh adapter_DSBai;
    Adapter_DSNha_DaLuu adapter_DSLuu;
    Adapter_DSNha_DaDang adapter_DangBoiUser;
    ListenerRegistration registrationBL, registrationDG, registrationTB, registrationDSTB, registrationBai;
    ////////////////PROFILE
    TextView txtUser, txtTenuser, txtEmail, txtGioiTinh, txtSDT;
    ImageView imgsuagioitinh, imgsuasdt, imgprofilenull, imgthongbaonull, imgbaidangboinull;
    CircleImageView imgavatar;
    TabHost tab2;
    Cursor c;
    int currentTab2 = 0;
    TableRow tbrDoiMK, tbrGioiThieu, tbrPhanHoi, tbrAvatar, tbrDangXuat;
    LinearLayout tblprofile;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://timkiemnhatro-d501f.appspot.com");
    StorageReference hinhanhRef;
    ////////////////////////////SEARCHHHHHHHHHHHHHHHHHH
    SeekBar seekBar;
    LatLng vitriclicktrenmap;
    Marker markermap;
    MarkerOptions markeronclick;
    Location lastLocation;
    private Circle mCircle;
    LocationManager locationManager;
    Criteria criteria;
    TabHost tab3;
    GoogleMap ggmap;
    Geocoder coder;
    int currentTab3 = 0;
    SupportMapFragment mapFragment;
    Spinner spintp, spinquan, spinloai;
    TextView txtngaythue;
    ImageView imgpickngaythue;
    EditText edtDuong, edtsong, edtsoxe, edthd, edtgiaden, edtgiatu, edtdtden, edtdttu, edtsophong;
    CheckBox chkchungchu, chknauan, chkbeprieng, chktoilet, chkdauxe, chkanninh, chkmaylanh, chkmaygiat, chksan, chksanthuong, chktv, chkwifi;
    ArrayAdapter<String> adapter_TP = null;
    ArrayAdapter<String> adapter_Quan = null;
    ArrayAdapter<String> adapter_Loai = null;
    ArrayList<LatLng> arrLatLngNha = new ArrayList<>();
    ArrayList<Marker> arrMarker = new ArrayList<>();
    ArrayList<String> arrLoaiNha = new ArrayList<>();
    ArrayList<String> arrQuan = new ArrayList<>();
    ArrayList<String> arrThanhPho = new ArrayList<>();
    int vitrichonTP, vitrichonQuan, vitrichonLoaiNha, idTinhThanh, nam, thang, ngay, sophong = 0;
    String strTP, strQuan, strLoai, strDuong;
    boolean chungchu = false, nauan = false, beprieng = false, toilet = false, chodauxe = false, anninh = false, maylanh = false, maygiat = false, tv = false, wifi = false, san = false, santhuong = false;
    Calendar cal = Calendar.getInstance();
    Date ngaythue;
    Button btnTim;
    ArrayList<Nha> arrNhaTimDuocKhiSearch = new ArrayList<>();
    ArrayList<NhaChiTiet> arrNCTSauKhiSearchTT = new ArrayList<>();
    ArrayList<NhaChiTiet> arrNCTSauKhiDuyetYC = new ArrayList<>();
    ArrayList<NhaChiTiet> arrNCTKetQuaSauCung = new ArrayList<>();
    ArrayList<NhaChiTiet> arrNCTSauKhiDuyetTI_1 = new ArrayList<>();
    ArrayList<NhaChiTiet> arrNCTSauKhiDuyetTI_2 = new ArrayList<>();
    ArrayList<NhaChiTiet> arrNCTSauKhiDuyetTI_3 = new ArrayList<>();
    ////////////////////////THÔNG BÁO ////////////////THÔNG BÁO////////////////THÔNG BÁO////////////////THÔNG BÁO////////////////THÔNG BÁO
    ////////////////THÔNG BÁO////////////////THÔNG BÁO////////////////THÔNG BÁO////////////////THÔNG BÁO////////////////THÔNG BÁO
    TableRow tbrKOTIN, tbrCOTIN;
    TextView txtkocotb, txttitledstb;
    Button btnDK_tb, btnMOFORMDKTIN_TB, btnXEMTINDADK_TB;
    RecyclerView re_ViewTB;
    ArrayList<ThongBao> arrTB = new ArrayList<>();
    public static ArrayList<TinDKTB> arrTINTB = new ArrayList<>();
    Adapter_ThongBao adapter_tb;
    ImageView imgxoaall;
    BadgeView badge, badge2;
    abc test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chinh);
        AnhXa();
        AnhXa2();
        AnhXaTK();
        AnhXaTB();
        XuLy();
        XuLy2();
        XuLyTK();
        XuLyTB();
        loadTabs();
        loadTabs2();
        loadTabs3();
        XuLyMap();
    }


    private void DownloadDSTinDK() {
        arrTINTB.clear();
        registrationTB = db.collection("DSTinDKNhanThongBao").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            TinDKTB tin = dc.getDocument().toObject(TinDKTB.class);
                            arrTINTB.add(tin);
                            Log.d("TESTDCMM", "size tin : " + arrTINTB.size());
                            break;
                        case MODIFIED:
                            break;
                        case REMOVED:
                            TinDKTB tin_del = dc.getDocument().toObject(TinDKTB.class);
                            for (int i = 0; i < arrTINTB.size(); i++) {
                                if (arrTINTB.get(i).getId().compareTo(tin_del.getId()) == 0) {
                                    arrTINTB.remove(i);
                                }
                            }
                            break;
                    }
                }
            }
        });
    }

    private void AnhXa() {
        fabToTop = findViewById(R.id.FAB_toTop_TC);

        tbrCOTIN = findViewById(R.id.tbr_DaDK_ThongBao);
        tbrKOTIN = findViewById(R.id.tbr_ChuaDK_ThongBao);
        ////////
        btnDangNhapFromProfile = findViewById(R.id.btnDangNhapFromProfile);
        flbtn = findViewById(R.id.floatingbtnmain_TC);
        flbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null)
                    MoFormDangBai();
                else
                    ThongBao("Vui lòng đăng nhập để đăng bài");
            }
        });
        imgthongbao = findViewById(R.id.imgThongBao_DSBaiLuu);
        btnDangBai = findViewById(R.id.btnDangBai_TC);
        ///
        reView_TC = findViewById(R.id.reViewBaiDang_TC);
        reView_TC.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    fabToTop.setVisibility(View.VISIBLE);
                } else if (recyclerView.canScrollVertically(1)) {
                    fabToTop.setVisibility(View.GONE);
                }
            }
        });
        reView_TC.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reView_TC.setLayoutManager(linearLayoutManager);
        adapter_DSBai = new Adapter_DSNha_TrangChinh(arrNha, getApplicationContext());
        reView_TC.setAdapter(adapter_DSBai);
        ///
        reView_Luu = findViewById(R.id.reView_DSBaiLuu);
        reView_Luu.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reView_Luu.setLayoutManager(linearLayoutManager2);
        adapter_DSLuu = new Adapter_DSNha_DaLuu(arrDSLuu, getApplicationContext());
        reView_Luu.setAdapter(adapter_DSLuu);
        ///swipe trang chính
        swipe = findViewById(R.id.swipelayout_TC);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new DownAnhNCT().execute();
                //LoadBaiDang();
                LoadUserFromDB();

            }
        });
        ///swipe ds lưu
        swipe_Luu = findViewById(R.id.swipe_DSBaiLuu);
        swipe_Luu.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadBaiLuu();
            }
        });
        tbly_DSluu = findViewById(R.id.tblayout_dsluu);

    }

    private void XuLy() {
        fabToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reView_TC.scrollToPosition(0);
            }
        });
        btnDangNhapFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TrangChinh.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnDangBai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null)
                    MoFormDangBai();
                else
                    ThongBao("Vui lòng đăng nhập để đăng bài");
            }
        });

        new LoadBaiDangAsync().execute();
    }

    class ThisClassForNotFreezeUI extends AsyncTask<Void, Void, Void> {
        ThisClassForNotFreezeUI() {

        }

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }

    private void LoadBaiDang() {
        arrNha.clear();
        registrationBai = db.collection("Nha").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Nha nha = dc.getDocument().toObject(Nha.class);
                            arrNha.add(nha);
                            adapter_DSBai.notifyDataSetChanged();
                            final long todayTime = new Date().getTime();
                            Collections.sort(arrNha, new Comparator<Nha>() {
                                public int compare(Nha nha1, Nha nha2) {
                                    Long dist1 = Math.abs(todayTime - nha1.getNgayDang().getTime());
                                    Long dist2 = Math.abs(todayTime - nha2.getNgayDang().getTime());
                                    return dist1.compareTo(dist2);
                                }

                            });
                            LoadBaiLuu();
                            if (user != null) {
                                LoadNhaDangBoiUser();
                            }
                            ADDMarkerVaoMap();
                            if (swipe.isRefreshing()) {
                                swipe.setRefreshing(false);
                            }
                            break;
                        case MODIFIED:
                            Nha nha_sua = dc.getDocument().toObject(Nha.class);
                            for (int i = 0; i < arrNha.size(); i++) {
                                if (arrNha.get(i).getId().compareTo(nha_sua.getId()) == 0)
                                    arrNha.set(i, nha_sua);
                            }
                            adapter_DSBai.notifyDataSetChanged();
                            final long todayTime3 = new Date().getTime();
                            Collections.sort(arrNha, new Comparator<Nha>() {
                                public int compare(Nha nha1, Nha nha2) {
                                    Long dist1 = Math.abs(todayTime3 - nha1.getNgayDang().getTime());
                                    Long dist2 = Math.abs(todayTime3 - nha2.getNgayDang().getTime());
                                    return dist1.compareTo(dist2);
                                }

                            });
                            LoadBaiLuu();
                            if (user != null) {
                                LoadNhaDangBoiUser();
                            }
                            ADDMarkerVaoMap();
                            if (swipe.isRefreshing()) {
                                swipe.setRefreshing(false);
                            }

                            if (progressBar != null) {
                                if (progressBar.isShowing()) {
                                    progressBar.dismiss();
                                }
                            }
                            break;
                        case REMOVED:
                            Nha nha_xoa = dc.getDocument().toObject(Nha.class);
                            for (int i = 0; i < arrNha.size(); i++) {
                                if (arrNha.get(i).getId().compareTo(nha_xoa.getId()) == 0) {
                                    arrNha.remove(i);
                                }
                            }
                            adapter_DSBai.notifyDataSetChanged();
                            final long todayTime2 = new Date().getTime();
                            Collections.sort(arrNha, new Comparator<Nha>() {
                                public int compare(Nha nha1, Nha nha2) {
                                    Long dist1 = Math.abs(todayTime2 - nha1.getNgayDang().getTime());
                                    Long dist2 = Math.abs(todayTime2 - nha2.getNgayDang().getTime());
                                    return dist1.compareTo(dist2);
                                }

                            });
                            LoadBaiLuu();
                            if (user != null) {
                                LoadNhaDangBoiUser();
                            }
                            ADDMarkerVaoMap();
                            if (swipe.isRefreshing()) {
                                swipe.setRefreshing(false);
                            }

                            if (progressBar != null) {
                                if (progressBar.isShowing()) {
                                    progressBar.dismiss();
                                }
                            }
                            break;
                    }
                }
            }

        });
    }

    private void LoadBaiLuu() {
        arrDSLuu.clear();
        arrLB.clear();
        if (user != null) {
            adapter_DSLuu.notifyDataSetChanged();
            db.collection("NhaDaLuu").document(user.getEmail()).collection("DSNhaDaLuu").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            LuuBai luu = doc.toObject(LuuBai.class);
                            arrLB.add(luu);
                        }
                        LoadNhaLayBaiLuu();
                    }
                }
            });
        }
    }

    private void LoadNhaLayBaiLuu() {
        for (int i = 0; i < arrLB.size(); i++) {
            for (int j = 0; j < arrNha.size(); j++) {
                if (arrNha.get(j).getId().compareTo(arrLB.get(i).getIdNha()) == 0) {
                    arrNha.get(j).setDaluu(true);
                    adapter_DSBai.notifyDataSetChanged();
                    arrDSLuu.add(arrNha.get(j));
                    adapter_DSLuu.notifyDataSetChanged();
                }
            }
        }
        if (arrDSLuu.size() == 0) {
            imgthongbao.setVisibility(View.VISIBLE);
            imgthongbao.setImageResource(R.drawable.dsluutrong);
        } else {
            imgthongbao.setVisibility(View.GONE);
        }
        if (swipe_Luu.isRefreshing()) {
            swipe_Luu.setRefreshing(false);
        }
        if (progressBar != null) {
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }
        }


    }

    private void LoadUserFromDB() {
        if (user != null) {
            db.collection("User").document(user.getEmail().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    InfoUser = documentSnapshot.toObject(User.class);
                    if (InfoUser.isDaDKTin() == true) {
                        tbrCOTIN.setVisibility(View.VISIBLE);
                        tbrKOTIN.setVisibility(View.GONE);
                    } else if (InfoUser.isDaDKTin() == false) {
                        tbrKOTIN.setVisibility(View.VISIBLE);
                        tbrCOTIN.setVisibility(View.GONE);
                    }
                    if (InfoUser.isTrangThai() == false) {
                        ThongBaoTKbikhoa("Tài khoản của bạn đã bị khóa , vui lòng liên hệ Admin để biết thêm chi tiết!");
                    }
                    if (swipe.isRefreshing())
                        swipe.setRefreshing(false);
                    HienThiInfoUser();
                    LoadBLUser();
                    LoadDGUser();
                }
            });
        }
    }

    private void LoadDGUser() {
        registrationDG = db.collection("DSDanhGiaChiTiet").whereEqualTo(
                "user.email", InfoUser.getEmail()
        ).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            DanhGiaChiTiet dg = dc.getDocument().toObject(DanhGiaChiTiet.class);
                            arrDGglobal.add(dg);
                            break;
                        case MODIFIED:
                            break;
                        case REMOVED:
                            break;
                    }
                }
            }
        });
    }

    private void LoadBLUser() {
        registrationBL = db.collection("DSBinhLuan").whereEqualTo(
                "user.email", InfoUser.getEmail()
        ).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            BinhLuan bl = dc.getDocument().toObject(BinhLuan.class);
                            arrBLglobal.add(bl);
                            break;
                        case MODIFIED:
                            break;
                        case REMOVED:
                            break;
                    }
                }
            }
        });
    }

    public void Progress() {
        progressBar = new ProgressDialog(TrangChinh.this);
        progressBar.show();
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setContentView(R.layout.customdialog);

    }

    private void MoFormDangBai() {
        Intent i = new Intent(TrangChinh.this, FormDangNha.class);
        startActivity(i);
    }

    public void ThongBao(String thongbao) {
        AlertDialog.Builder b = new AlertDialog.Builder(TrangChinh.this);
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

    public void ThongBaoTKbikhoa(String thongbao) {
        AlertDialog.Builder b = new AlertDialog.Builder(TrangChinh.this);
        b.setTitle("Thông báo");
        b.setMessage(thongbao);
        b.setCancelable(false);
        b.setPositiveButton("Biết rồi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                TroVeTrangDangNhap();
            }
        });
        b.create().show();
    }

    public void loadTabs() {

        tab = findViewById(R.id.tabAdmin_TC);
        //gọi lệnh setup

        tab.setup();
        tab.getTabWidget().setStripEnabled(false);

        //Tạo tab1
        spec = tab.newTabSpec("t1");
        spec.setContent(R.id.tabTrangChinh);
        spec.setIndicator("", getResources().getDrawable(R.drawable.tabicon));
        tab.addTab(spec);
        //Tạo tab2
        spec = tab.newTabSpec("t2");
        spec.setContent(R.id.tabDSDaLuu);
        spec.setIndicator("", getResources().getDrawable(R.drawable.tabicon2));
        tab.addTab(spec);
        //Tạo tab3
        spec = tab.newTabSpec("t3");
        spec.setContent(R.id.tabTimKiem);
        spec.setIndicator("", getResources().getDrawable(R.drawable.tabicon3));
        tab.addTab(spec);
        //Tạo tab4
        spec = tab.newTabSpec("t4");
        spec.setContent(R.id.tabUser);
        spec.setIndicator("", getResources().getDrawable(R.drawable.tabicon4));
        tab.addTab(spec);
        //Thiết lập tab mặc định được chọn ban đầu là tab 0
        tab.getTabWidget().getChildTabViewAt(0).setBackgroundDrawable(null);
        tab.getTabWidget().getChildTabViewAt(1).setBackgroundDrawable(null);
        tab.getTabWidget().getChildTabViewAt(2).setBackgroundDrawable(null);
        tab.getTabWidget().getChildTabViewAt(3).setBackgroundDrawable(null);
        tab.setCurrentTab(0);
        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                View currentView = tab.getCurrentView();
                switch (tab.getCurrentTab()) {
                    case 0:

                        break;
                    case 1:
                        if (user == null) {
                            imgthongbao.setVisibility(View.VISIBLE);
                            imgthongbao.setImageResource(R.drawable.chuadangnhap_dsluu);
                        } else {
                            Progress();
                            LoadBaiLuu();

                        }
                        break;
                    case 2:
                        break;
                    case 3:
                        //new LoadBaiDangAsync().execute();
                        break;

                }
                if (tab.getCurrentTab() > currentTab) {
                    currentView.setAnimation(inFromRightAnimation());
                    currentTab = tab.getCurrentTab();
                } else {
                    currentView.setAnimation(outToRightAnimation());
                    currentTab = tab.getCurrentTab();
                }


            }
        });
        for (int i = 0; i < tab.getTabWidget().getChildCount(); i++) {
            tab.getTabWidget().getChildAt(i).getLayoutParams().height = 70;
        }

    }


    public static void switchTabs(boolean direction) {
        if (direction) // true = move left
        {
            if (tab.getCurrentTab() == 0)
                tab.setCurrentTab(tab.getTabWidget().getTabCount() - 1);
            else
                tab.setCurrentTab(tab.getCurrentTab() - 1);
        } else
        // move right
        {
            if (tab.getCurrentTab() != (tab.getTabWidget().getTabCount() - 1))
                tab.setCurrentTab(tab.getCurrentTab() + 1);
            else
                tab.setCurrentTab(0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (user != null) {
            if (registrationDG != null)
                registrationDG.remove();
            if (registrationBL != null)
                registrationBL.remove();
            if (registrationTB != null)
                registrationTB.remove();
            if (registrationDSTB != null)
                registrationDSTB.remove();
            if (registrationBai != null)
                registrationBai.remove();
        }
    }
    /////// TAB PROFILE /////// TAB PROFILE /////////////// TAB PROFILE ///////////// TAB PROFILE ///////// TAB PROFILE /////
    /////// TAB PROFILE /////// TAB PROFILE /////////////// TAB PROFILE ///////////// TAB PROFILE ///////// TAB PROFILE /////
    /////// TAB PROFILE /////// TAB PROFILE /////////////// TAB PROFILE ///////////// TAB PROFILE ///////// TAB PROFILE /////

    private void LoadNhaDangBoiUser() {
        arrNhaDangBoiUser.clear();
        for (int i = 0; i < arrNha.size(); i++) {
            if (arrNha.get(i).getUser().getEmail().compareTo(user.getEmail()) == 0) {
                arrNhaDangBoiUser.add(arrNha.get(i));
            }
        }
        final long todayTime = new Date().getTime();
        Collections.sort(arrNhaDangBoiUser, new Comparator<Nha>() {
            public int compare(Nha nha1, Nha nha2) {
                Long dist1 = Math.abs(todayTime - nha1.getNgayDang().getTime());
                Long dist2 = Math.abs(todayTime - nha2.getNgayDang().getTime());
                return dist1.compareTo(dist2);
            }

        });
        adapter_DangBoiUser.notifyDataSetChanged();
    }

    public void loadTabs2() {

        tab2 = findViewById(R.id.tabAdmin_ProfileUser);
        //gọi lệnh setup

        tab2.setup();
        tab2.getTabWidget().setStripEnabled(false);
        TabHost.TabSpec spec;
        //Tạo tab1
        spec = tab2.newTabSpec("t1");
        spec.setContent(R.id.tabThongBao);
        spec.setIndicator("Thông báo");
        tab2.addTab(spec);
        //Tạo tab2
        spec = tab2.newTabSpec("t2");
        spec.setContent(R.id.tabTTUser);
        spec.setIndicator("Thông tin");
        tab2.addTab(spec);
        //Tạo tab3
        spec = tab2.newTabSpec("t3");
        spec.setContent(R.id.tabBaiDaDang);
        spec.setIndicator("Bài đăng");
        tab2.addTab(spec);
        //Thiết lập tab mặc định được chọn ban đầu là tab 0
        tab2.setCurrentTab(0);

        tab2.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                View currentView = tab2.getCurrentView();
                switch (tab2.getCurrentTab()) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;

                }
                if (tab2.getCurrentTab() > currentTab2) {
                    currentView.setAnimation(inFromRightAnimation());
                    currentTab2 = tab2.getCurrentTab();
                } else {
                    currentView.setAnimation(outToRightAnimation());
                    currentTab2 = tab2.getCurrentTab();
                }


            }
        });
        for (int i = 0; i < tab2.getTabWidget().getChildCount(); i++) {
            tab2.getTabWidget().getChildAt(i).getLayoutParams().height = 75;
        }

    }

    private void PickAnhAvatar() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3333);
    }

    private void HienThiInfoUser() {
        Glide.with(TrangChinh.this).load(InfoUser.getHinhAnh()).into(imgavatar); // load hinhvao ava trong profile
        txtUser.setText(InfoUser.getHoTen());
        txtTenuser.setText(InfoUser.getHoTen());
        txtEmail.setText(InfoUser.getEmail());
        if (InfoUser.isGioiTinh() == true) {
            txtGioiTinh.setText("Nam");
        } else
            txtGioiTinh.setText("Nữ");
        txtSDT.setText(InfoUser.getSoDienThoai());
    }

    private void XuLy2() {
        if (user == null) {
            tblprofile.setVisibility(View.GONE);
            imgprofilenull.setVisibility(View.VISIBLE);
            imgthongbaonull.setVisibility(View.VISIBLE);
            imgbaidangboinull.setVisibility(View.VISIBLE);
            btnDangNhapFromProfile.setVisibility(View.VISIBLE);
        }
        tbrDoiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoFormDoiMK();
            }
        });
        imgsuagioitinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoFormDoiGioiTinh();
            }
        });
        imgsuasdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoFormDoiSDT();
            }
        });
        tbrAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkPermission()) {
                        PickAnhAvatar();
                    } else {
                        requestPermission();
                    }
                } else
                    PickAnhAvatar();
            }
        });
        tbrPhanHoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoFormGuiPhanHoi();
            }
        });
        tbrDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null)
                    ThongBaoDangXuat("Bạn muốn đăng xuất khỏi tài khoản : " + user.getEmail());
            }
        });
    }

    protected boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    protected void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
                    PickAnhAvatar();
                } else {
                    Toast.makeText(this, "Đã từ chối cho phép truy cập thư viện , không thể lấy ảnh", Toast.LENGTH_SHORT).show();
                }
                break;
            case 123123:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    HienThiNutCurrentLocation();
                } else {
                    Toast.makeText(this, "Đã từ chối cho phép truy cập tọa độ", Toast.LENGTH_SHORT).show();
                }
                break;
            case 12341234:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LayToaDo();
                } else {
                    Toast.makeText(this, "Đã từ chối cho phép truy cập tọa độ", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1234512345:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LayToaDo_Passive();
                } else {
                    Toast.makeText(this, "Đã từ chối cho phép truy cập tọa độ", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void TroVeTrangDangNhap() {
        Intent i = new Intent(TrangChinh.this, MainActivity.class);
        mAuth.signOut();
        startActivity(i);
        finish();
    }

    private void MoFormGuiPhanHoi() {
        Intent i = new Intent(TrangChinh.this, FormGuiFeedback.class);
        startActivity(i);
    }

    private void MoFormDoiSDT() {
        Intent i = new Intent(TrangChinh.this, FormDoiSDT.class);
        startActivityForResult(i, 2222);
    }

    private void MoFormDoiMK() {
        Intent i = new Intent(TrangChinh.this, FormDoiMK.class);
        startActivity(i);
    }

    private void MoFormDoiGioiTinh() {
        Intent i = new Intent(TrangChinh.this, FormDoiGioiTinhUser.class);
        startActivityForResult(i, 1111);
    }

    private void AnhXa2() {
        tbrDangXuat = findViewById(R.id.tbrow_DangXuat_Profile);
        tbrAvatar = findViewById(R.id.AvatarChange_Profile);
        imgthongbaonull = findViewById(R.id.imgThongBao_USERNULL);
        imgbaidangboinull = findViewById(R.id.imgBaiDangBoi_USERNULL);
        imgprofilenull = findViewById(R.id.imgProfile_USERNULL);
        txtUser = findViewById(R.id.txtTenUser_Profile);
        txtTenuser = findViewById(R.id.txtHoTen_Profile);
        txtEmail = findViewById(R.id.txtEmail_Profile);
        txtGioiTinh = findViewById(R.id.txtGioiTinh_Profile);
        txtSDT = findViewById(R.id.txtSDT_Profile);
        imgsuagioitinh = findViewById(R.id.imgSuaGioiTinh_Profile);
        imgsuasdt = findViewById(R.id.imgSuaSDT_Profile);
        imgavatar = findViewById(R.id.imgAvaUser_Profile);
        tbrDoiMK = findViewById(R.id.tbrow_DoiMK_Profile);
        tbrGioiThieu = findViewById(R.id.tbrow_GioiThieu_Profile);
        tbrPhanHoi = findViewById(R.id.tbrow_GuiPhanHoi_Profile);
        tblprofile = findViewById(R.id.tblayout_Profile);
        reView_DangBoiUser = findViewById(R.id.reView_DangBoiUser);
        reView_DangBoiUser.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reView_DangBoiUser.setLayoutManager(linearLayoutManager3);
        adapter_DangBoiUser = new Adapter_DSNha_DaDang(arrNhaDangBoiUser, getApplicationContext());
        reView_DangBoiUser.setAdapter(adapter_DangBoiUser);
        tbrGioiThieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TrangChinh.this,"Hello!",Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111 && data != null) {
            if (data.getBooleanExtra("gioitinh", InfoUser.isGioiTinh()) == true) {
                txtGioiTinh.setText("Nam");
            } else {
                txtGioiTinh.setText("Nữ");
            }
        } else if (requestCode == 2222 && data != null) {
            String sdt = data.getStringExtra("sdt");
            txtSDT.setText(sdt);
        } else if (requestCode == 3333 && data != null) {
            Uri uri = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            selectedImage = getResizedBitmap(selectedImage, 555);
            imgavatar.setImageBitmap(selectedImage);
            Uri uri_resized = getImageUri(this, selectedImage);
            UpAnhLenStorage(uri_resized);
        }
    }

    private void UpAnhLenStorage(Uri uri) {
        Progress();
        String child = UUID.randomUUID().toString();
        hinhanhRef = storageRef.child(child);
        UploadTask uploadTask = hinhanhRef.putFile(uri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(TrangChinh.this, "Cập nhật ảnh đại diện gặp sự cố! Kiểm tra kết nối và thử lại sau", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String strHinhAnh = String.valueOf(taskSnapshot.getDownloadUrl()); // << chỗ lấy đc link ảnh
                UpdateAnhFB_User(strHinhAnh);
                UpdateAnhFB_Nha(strHinhAnh);
                UpdateBL(strHinhAnh);
                UpdateDG(strHinhAnh);
                LoadUserFromDB();
                Toast.makeText(TrangChinh.this, "Cập nhật ảnh đại diện thành công", Toast.LENGTH_SHORT).show();
                progressBar.dismiss();
            }
        });
    }

    private void UpdateAnhFB_Nha(String strHinhAnh) {
        for (int i = 0; i < arrNhaDangBoiUser.size(); i++) {
            db.collection("Nha").document(arrNhaDangBoiUser.get(i).getId()).update("user.hinhAnh", strHinhAnh);
        }
    }

    private void UpdateAnhFB_User(String strHinhAnh) {
        db.collection("User").document(user.getEmail()).update(
                "hinhAnh", strHinhAnh);
    }

    private void UpdateBL(String strHinhAnh) {
        for (int i = 0; i < arrBLglobal.size(); i++) {
            db.collection("DSBinhLuan").document(arrBLglobal.get(i).getId()).update("user.hinhAnh", strHinhAnh);
        }
    }

    private void UpdateDG(String strHinhAnh) {
        for (int i = 0; i < arrNha.size(); i++) { // dùng arrNha vì đánh giá chi tiết phải duyệt hết lấy id nhà xem nhà nào user đã đánh giá , có thể dùng arrDGglobal thay , sẽ ngắn hơn
            db.collection("DSDanhGiaChiTiet").document(user.getEmail() + "." + arrNha.get(i).getId()).update("user.hinhAnh", strHinhAnh);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dsdaluu_context_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    ////// TÌM KIẾM //////// ////// TÌM KIẾM //////// ////// TÌM KIẾM //////// ////// TÌM KIẾM //////// ////// TÌM KIẾM //////// ////// TÌM KIẾM ////////
    private void AnhXaTK() {
        seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                filterMarkers(progress * 10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_tktd);
        /////
        spintp = findViewById(R.id.spinTP_TimKiem);
        spinquan = findViewById(R.id.spinQuan_TimKiem);
        spinloai = findViewById(R.id.spinLoaiPhong_TimKiem);
        txtngaythue = findViewById(R.id.txtHienThiNgayThue_TimKiem);
        imgpickngaythue = findViewById(R.id.imgbtnChonNgayThue_TimKiem);
        /////
        edtDuong = findViewById(R.id.edtDuong_TimKiem);
        edtsong = findViewById(R.id.edtSLNguoi_TimKiem);
        edtsoxe = findViewById(R.id.edtSLXe_TimKiem);
        edthd = findViewById(R.id.edtHD_TimKiem);
        edtgiatu = findViewById(R.id.edtGiaTu_TimKiem);
        edtgiaden = findViewById(R.id.edtGiaDen_TimKiem);
        edtdttu = findViewById(R.id.edtDienTichTu_TimKiem);
        edtdtden = findViewById(R.id.edtDienTichDen_TimKiem);
        edtsophong = findViewById(R.id.edtSoPhong_TimKiem);
        /////
        chkchungchu = findViewById(R.id.chkChungChu_TimKiem);
        chknauan = findViewById(R.id.chkNauAn_TimKiem);
        chkbeprieng = findViewById(R.id.chkBepRieng_TimKiem);
        chktoilet = findViewById(R.id.chkToilet_TimKiem);
        chkdauxe = findViewById(R.id.chkDauXe_TimKiem);
        chkanninh = findViewById(R.id.chkAnNinh_TimKiem);
        chkmaylanh = findViewById(R.id.chkMayLanh_TimKiem);
        chkmaygiat = findViewById(R.id.chkMayGiat_TimKiem);
        chksan = findViewById(R.id.chkSan_TimKiem);
        chksanthuong = findViewById(R.id.chkSanThuong_TimKiem);
        chktv = findViewById(R.id.chkTV_TimKiem);
        chkwifi = findViewById(R.id.chkWifi_TimKiem);
        ////
        btnTim = findViewById(R.id.btnTimKiem);
    }

    private void XuLyMap() {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                onMyMapReady(googleMap);
            }
        });

    }

    private void onMyMapReady(GoogleMap googleMap) {
        ggmap = googleMap;
        ADDMarkerVaoMap();
        coder = new Geocoder(this, Locale.getDefault());
        XuLySuKienClickOnMap();
        ggmap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            double longitude, latitude;
            ArrayList<Address> adresses;

            @Override
            public void onMapLoaded() {
                TuiDangODau();
                ADDMarkerVaoMap();
                //showCircleToGoogleMap(position,100);
            }
        });
        ggmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        ggmap.getUiSettings().setZoomControlsEnabled(true);
        //MoToaDo();
        HienThiNutCurrentLocation();




        ggmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ThongBaoMoBaiDang("Bạn muốn xem chi tiết nhà?",marker);
                    }
                }, 500);
                return false;
            }
        });

    }
    private void HienThiNutCurrentLocation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                ggmap.setMyLocationEnabled(true);
            } else {
                askPermission1();
            }
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ggmap.setMyLocationEnabled(true);
        }
    }
    private void LayToaDo(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                criteria = new Criteria();
                lastLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            } else {
                askPermission2();
            }
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            criteria = new Criteria();
            lastLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        }
    }
    private void LayToaDo_Passive(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                criteria = new Criteria();
                lastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            } else {
                askPermission3();
            }
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            criteria = new Criteria();
            lastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }
    }
    private void askPermission1() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "Cần cho phép truy cập vị trí!", Toast.LENGTH_LONG).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 123123);
            }
        }
    }
    private void askPermission2() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "Cần cho phép truy cập vị trí!", Toast.LENGTH_LONG).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 12341234);
            }
        }
    }
    private void askPermission3() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "Cần cho phép truy cập vị trí!", Toast.LENGTH_LONG).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1234512345);
            }
        }
    }
    public void ThongBaoMoBaiDang(String thongbao, final Marker markerr){
        AlertDialog.Builder b = new AlertDialog.Builder(TrangChinh.this);
        b.setMessage(thongbao);
        b.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.setNegativeButton("Mở", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LayNhaTuIdMarker(markerr);
            }
        });
        b.create().show();
    }

    private void LayNhaTuIdMarker(Marker marker) {
        String idnha = marker.getSnippet();
        Nha nha = null;
        for(int i=0;i<arrNha.size();i++){
            if(arrNha.get(i).getId().compareTo(idnha)==0){
                nha = arrNha.get(i);
            }
        }
        MoCTBai(nha);
    }
    private void MoCTBai(Nha nha){
        Intent i = new Intent(TrangChinh.this, ChiTietBaiDang.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("dulieunha",nha);
        i.putExtra("data",bundle);
        startActivity(i);
    }
    private void XuLySuKienClickOnMap(){
        ggmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                seekBar.setVisibility(View.VISIBLE);
                vitriclicktrenmap=latLng;
                if(mCircle!=null) {
                    mCircle.remove();
                }
                //ggmap.clear();
                if(markermap!=null)
                      markermap.remove();
                markeronclick= new MarkerOptions()
                        .position(vitriclicktrenmap)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                ggmap.animateCamera(CameraUpdateFactory.newLatLngZoom(vitriclicktrenmap, 3));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(vitriclicktrenmap)      // Sets the center of the map to location user
                        .zoom(16)                   // Sets the zoom
                        // Sets the orientation of the camera to east
                        //.tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                ggmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                markermap = ggmap.addMarker(markeronclick);
                showCircleToGoogleMap(vitriclicktrenmap,200);
                filterMarkers(200);
                seekBar.setProgress(20);
            }
        });
    }
    private void TuiDangODau() {

        //lastLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        LayToaDo();
        if(lastLocation != null){
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();
            //Toast.makeText(this,latitude+"-"+longitude, Toast.LENGTH_SHORT).show();
            LatLng vitri = new LatLng(latitude,longitude);
            ggmap.animateCamera(CameraUpdateFactory.newLatLngZoom(vitri, 3));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(vitri)      // Sets the center of the map to location user
                    .zoom(15)                   // Sets the zoom
                    //.bearing(90)                // Sets the orientation of the camera to east
                    //.tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            ggmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            //showCircleToGoogleMap(vitri,200);
        }else{
            //lastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            LayToaDo_Passive();
            double longitude = lastLocation.getLongitude();
            double latitude = lastLocation.getLatitude();
            //Toast.makeText(this,latitude+"-"+longitude, Toast.LENGTH_SHORT).show();
            LatLng vitri = new LatLng(latitude,longitude);
            ggmap.animateCamera(CameraUpdateFactory.newLatLngZoom(vitri, 3));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(vitri)      // Sets the center of the map to location user
                    .zoom(15)                   // Sets the zoom
                    //.bearing(90)                // Sets the orientation of the camera to east
                    //.tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            ggmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            //showCircleToGoogleMap(vitri,200);
        }
    }
    private void ADDMarkerVaoMap(){
        if(ggmap!=null){
            ggmap.clear();
            seekBar.setVisibility(View.GONE);
            for (int i = 0; i < arrNha.size(); i++) {
                if (arrNha.get(i).getLatitude() != 0 && arrNha.get(i).getLongitude() != 0) {
                    double lat = arrNha.get(i).getLatitude();
                    double lng = arrNha.get(i).getLongitude();
                    LatLng latLng = new LatLng(lat, lng);
                    String so=arrNha.get(i).getDiaChi().getSoNha();
                    String duong=arrNha.get(i).getDiaChi().getDuong();
                    String quan=arrNha.get(i).getDiaChi().getQuan();
                    String tp=arrNha.get(i).getDiaChi().getThanhPho();
                    Marker marker = ggmap.addMarker(
                            new MarkerOptions()
                                    .visible(false)
                                    .position(latLng)
                                    .title(so+" "+duong+", "+quan+", "+tp)
                                    .snippet(arrNha.get(i).getId())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarkerresize6464)));
                    arrMarker.add(marker);
                }
            }
        }
    }
    private void showCircleToGoogleMap(LatLng position, float radius) {
        if(ggmap==null)
            return;
        mCircle = ggmap.addCircle(new CircleOptions()
                .strokeWidth(3)
                .radius(radius)
                .center(position)
                .strokeColor(Color.parseColor("#D1C4E9"))
                .fillColor(Color.parseColor("#657C4DFF")));

    }
    private void filterMarkers(double radiusForCircle){
        mCircle.setRadius(radiusForCircle);
        float[] distance = new float[2];
        for(int m = 0; m < arrMarker.size(); m++){
            Marker marker = arrMarker.get(m);
            LatLng position = marker.getPosition();
            double lat = position.latitude;
            double lon = position.longitude;

            Location.distanceBetween(lat, lon, vitriclicktrenmap.latitude,
                    vitriclicktrenmap.longitude, distance);

            boolean inCircle = distance[0] <= radiusForCircle;
            marker.setVisible(inCircle);
        }
    }

    public void loadTabs3() {

        tab3 = findViewById(R.id.tabAdmin_TK);
        //gọi lệnh setup

        tab3.setup();
        tab3.getTabWidget().setStripEnabled(false);
        TabHost.TabSpec spec;
        //Tạo tab1
        spec = tab3.newTabSpec("t1");
        spec.setContent(R.id.tabTKtheoTT);
        spec.setIndicator("Theo thông tin");
        tab3.addTab(spec);
        //Tạo tab2
        spec = tab3.newTabSpec("t2");
        spec.setContent(R.id.tabTKtheoToaDo);
        spec.setIndicator("Theo tọa độ");
        tab3.addTab(spec);
        //Thiết lập tab mặc định được chọn ban đầu là tab 0
        tab3.setCurrentTab(0);

        tab3.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                View currentView = tab3.getCurrentView();
                switch (tab3.getCurrentTab()){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;

                }
                if (tab3.getCurrentTab() > currentTab3)
                {
                    currentView.setAnimation( inFromRightAnimation() );
                    currentTab3=tab3.getCurrentTab();
                }
                else
                {
                    currentView.setAnimation( outToRightAnimation() );
                    currentTab3=tab3.getCurrentTab();
                }


            }
        });
        for (int i = 0; i < tab3.getTabWidget().getChildCount(); i++) {
            tab3.getTabWidget().getChildAt(i).getLayoutParams().height=75;
        }

    }
    private void XuLyTK(){
        LoadTP();
        XuLyKhiChonTP();
        XuLyKhiChonQuan();
        XuLyKhiChonLoaiNha();
        XuLyKhiChonNgayThue();
        XuLyKhiBamTimKiem();

    }
    private void GetBooleanTimKiem(){
        if(chkchungchu.isChecked())
            chungchu=true;
        if(chkanninh.isChecked())
            anninh=true;
        if(chknauan.isChecked())
            nauan=true;
        if(chktoilet.isChecked())
            toilet=true;
        if(chkbeprieng.isChecked())
            beprieng=true;
        if(chkdauxe.isChecked())
            chodauxe=true;
        if(chkmaygiat.isChecked())
            maygiat=true;
        if(chkmaylanh.isChecked())
            maylanh=true;
        if(chktv.isChecked())
            tv=true;
        if(chkwifi.isChecked())
            wifi=true;
        if(chksan.isChecked())
            san=true;
        if(chksanthuong.isChecked())
            santhuong=true;
    }
    private void ClearAllList(){
        arrNCTSauKhiSearchTT.clear();
        arrNCTSauKhiDuyetYC.clear();
        arrNCTKetQuaSauCung.clear();
        arrNhaTimDuocKhiSearch.clear();
        arrnct_da_nhan.clear();
        arrNCTSauKhiDuyetTI_1.clear();
        arrNCTSauKhiDuyetTI_2.clear();
        arrNCTSauKhiDuyetTI_3.clear();
    }
    private void XuLyKhiBamTimKiem() {
        btnTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (KiemTraTongTinTK() == true) {
                    if (edtsophong.getText().toString().trim().length() == 0) {
                        sophong = 0;
                    } else
                        sophong = Integer.parseInt(edtsophong.getText().toString());
                    ClearAllList();
                    GetBooleanTimKiem();
                    Log.d("DEBUG",""+sophong);
                    Log.d("DEBUG","nau an : "+nauan);
                    if (vitrichonTP == 0 && vitrichonQuan == 0 && edtDuong.getText().toString().trim().length() == 0 && vitrichonLoaiNha == 0
                            && edtdttu.getText().toString().trim().length() == 0 && edtdtden.getText().toString().trim().length() == 0 &&
                            edtgiatu.getText().toString().trim().length() == 0 && edtgiaden.getText().toString().trim().length() == 0 &&
                            txtngaythue.getText().toString().trim().length() == 0
                            && edtsong.getText().toString().trim().length() == 0 && edtsoxe.getText().toString().trim().length() == 0
                            && edthd.getText().toString().trim().length() == 0 && chungchu == false && nauan == true) { // cả Đc + TT + YC đều null , chỉ check tiện ích
                        DuyetTienIch_AllNull_ChiTimNauAn();

                    }else if (sophong!=0 && vitrichonTP == 0 && vitrichonQuan == 0 && edtDuong.getText().toString().trim().length() == 0 && vitrichonLoaiNha == 0
                            && edtdttu.getText().toString().trim().length() == 0 && edtdtden.getText().toString().trim().length() == 0 &&
                            edtgiatu.getText().toString().trim().length() == 0 && edtgiaden.getText().toString().trim().length() == 0 &&
                            txtngaythue.getText().toString().trim().length() == 0
                            && edtsong.getText().toString().trim().length() == 0 && edtsoxe.getText().toString().trim().length() == 0
                            && edthd.getText().toString().trim().length() == 0 && chungchu == false && nauan == false) { // cả Đc + TT + YC đều null , chỉ check tiện ích
                        DuyetTienIch_AllNull_ChiTimSP();

                    }else if (vitrichonTP == 0 && vitrichonQuan == 0 && edtDuong.getText().toString().trim().length() == 0 && vitrichonLoaiNha == 0
                            && edtdttu.getText().toString().trim().length() == 0 && edtdtden.getText().toString().trim().length() == 0 &&
                            edtgiatu.getText().toString().trim().length() == 0 && edtgiaden.getText().toString().trim().length() == 0 &&
                            txtngaythue.getText().toString().trim().length() == 0
                            && edtsong.getText().toString().trim().length() == 0 && edtsoxe.getText().toString().trim().length() == 0
                            && edthd.getText().toString().trim().length() == 0 && chungchu == false && nauan == false) { // cả Đc + TT + YC đều null , chỉ check tiện ích
                        DuyetTienIch_TrucTiep_AllLISTNCT();

                    } else {
                        if (vitrichonTP != 0 && vitrichonQuan != 0 && edtDuong.getText().toString().trim().length() != 0 && vitrichonLoaiNha != 0) {
                            Tim_TP_QUAN_DUONG_LOAI();
                        } else if (vitrichonTP != 0 && vitrichonQuan != 0 && edtDuong.getText().toString().trim().length() != 0 && vitrichonLoaiNha == 0) {
                            Tim_TP_QUAN_DUONG();
                        } else if (vitrichonTP != 0 && vitrichonQuan != 0 && edtDuong.getText().toString().trim().length() == 0 && vitrichonLoaiNha != 0) {
                            Tim_TP_QUAN_LOAI();
                        } else if (vitrichonTP != 0 && vitrichonQuan != 0 && edtDuong.getText().toString().trim().length() == 0 && vitrichonLoaiNha == 0) {
                            Tim_TP_QUAN();
                        } else if (vitrichonTP != 0 && vitrichonQuan == 0 && edtDuong.getText().toString().trim().length() != 0 && vitrichonLoaiNha != 0) {
                            Tim_TP_DUONG_LOAI();
                        } else if (vitrichonTP != 0 && vitrichonQuan == 0 && edtDuong.getText().toString().trim().length() != 0 && vitrichonLoaiNha == 0) {
                            Tim_TP_DUONG();
                        } else if (vitrichonTP != 0 && vitrichonQuan == 0 && edtDuong.getText().toString().trim().length() == 0 && vitrichonLoaiNha != 0) {
                            Tim_TP_LOAI();
                        } else if (vitrichonTP != 0 && vitrichonQuan == 0 && edtDuong.getText().toString().trim().length() == 0 && vitrichonLoaiNha == 0) {
                            Tim_TP();
                        } else if (vitrichonTP == 0 && vitrichonQuan == 0 && edtDuong.getText().toString().trim().length() == 0 && vitrichonLoaiNha != 0) {
                            Tim_LOAI();
                        } else if (vitrichonTP == 0 && vitrichonQuan == 0 && edtDuong.getText().toString().trim().length() != 0 && vitrichonLoaiNha == 0) {
                            Tim_DUONG();
                        } else if (vitrichonTP == 0 && vitrichonQuan == 0 && edtDuong.getText().toString().trim().length() != 0 && vitrichonLoaiNha != 0) {
                            Tim_DUONG_LOAI();
                        } else if (vitrichonTP == 0 && vitrichonQuan == 0 && edtDuong.getText().toString().trim().length() == 0 && vitrichonLoaiNha == 0) {
                            Tim_TrucTiep_TT();
                        }
                    }

                }
            }
        });

    }

    private void DuyetTienIch_TrucTiep_AllLISTNCT() {
        db.collection("DSNhaChiTiet").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
                        NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                        arrNCTSauKhiDuyetYC.add(nct);
                    }
                    DuyetTienIch();
                }
            }
        });
    }
    private void DuyetTienIch_AllNull_ChiTimSP() {
        db.collection("DSNhaChiTiet").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
                        NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                        if(nct.getSoLuongPhong()>=sophong)
                        arrNCTSauKhiDuyetYC.add(nct);
                    }
                    DuyetTienIch();
                }
            }
        });
    }
    private void DuyetTienIch_AllNull_ChiTimNauAn() {
        db.collection("DSNhaChiTiet").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
                        NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                        if(nct.getYeuCau().getChoNauAn())
                            arrNCTSauKhiDuyetYC.add(nct);
                    }
                    DuyetTienIch();
                }
            }
        });
    }
    private boolean KiemTraTongTinTK() {
        if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()!=0
                || edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()==0){
            ThongBao("Vui lòng điền đủ thông tin diện tích hoặc bỏ trống cả 2 ô");
            return false;
        }else if( edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()!=0
                || edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()==0){
            ThongBao("Vui lòng điền đủ thông tin giá phòng hoặc bỏ trống cả 2 ô");
            return false;
        }else if (edtgiatu.getText().toString().trim().length() !=0 && edtgiaden.getText().toString().trim().length()!=0){
            if(Integer.parseInt(edtgiatu.getText().toString()) >= Integer.parseInt(edtgiaden.getText().toString()) ){
                ThongBao("Giá thấp nhất không được lớn hơn hoặc bằng giá tối đa");
                return false;
            }
        }else if (edtdttu.getText().toString().trim().length() !=0 && edtdtden.getText().toString().trim().length()!=0){
            if(Integer.parseInt(edtdttu.getText().toString()) >= Integer.parseInt(edtdtden.getText().toString()) ){
                ThongBao("Diện tích nhỏ nhất không được lớn hơn hoặc bằng diện tích tối đa");
                return false;
            }
        }
        return true;
    }
    //////////////////DUYET DIA CHI
    private void Tim_TrucTiep_TT(){
        Progress();
                for(int i=0;i<arrNha.size();i++) {
                    if(chungchu==true) {
                        if (arrNha.get(i).getChungChu() == true) {
                            arrNhaTimDuocKhiSearch.add(arrNha.get(i));
                        }
                    }else if(chungchu==false){
                        arrNhaTimDuocKhiSearch.add(arrNha.get(i));
                    }
                }
        if (edtdttu.getText().toString().trim().length() != 0 && edtdtden.getText().toString().trim().length() != 0 &&
                edtgiatu.getText().toString().trim().length() != 0 && edtgiaden.getText().toString().trim().length() != 0 &&
                txtngaythue.getText().toString().trim().length() != 0) {
            Tim_DT_GIA_NGAY();
        } else if (edtdttu.getText().toString().trim().length() == 0 && edtdtden.getText().toString().trim().length() == 0 &&
                edtgiatu.getText().toString().trim().length() != 0 && edtgiaden.getText().toString().trim().length() != 0 &&
                txtngaythue.getText().toString().trim().length() == 0) {
            Tim_GIA();
        } else if (edtdttu.getText().toString().trim().length() != 0 && edtdtden.getText().toString().trim().length() != 0 &&
                edtgiatu.getText().toString().trim().length() != 0 && edtgiaden.getText().toString().trim().length() != 0 &&
                txtngaythue.getText().toString().trim().length() == 0) {
            Tim_DT_GIA();
        } else if (edtdttu.getText().toString().trim().length() == 0 && edtdtden.getText().toString().trim().length() == 0 &&
                edtgiatu.getText().toString().trim().length() != 0 && edtgiaden.getText().toString().trim().length() != 0 &&
                txtngaythue.getText().toString().trim().length() != 0) {
            Tim_GIA_NGAY();
        } else if (edtdttu.getText().toString().trim().length() != 0 && edtdtden.getText().toString().trim().length() != 0 &&
                edtgiatu.getText().toString().trim().length() == 0 && edtgiaden.getText().toString().trim().length() == 0 &&
                txtngaythue.getText().toString().trim().length() != 0) {
            Tim_DT_NGAY();
        } else if (edtdttu.getText().toString().trim().length() != 0 && edtdtden.getText().toString().trim().length() != 0 &&
                edtgiatu.getText().toString().trim().length() == 0 && edtgiaden.getText().toString().trim().length() == 0 &&
                txtngaythue.getText().toString().trim().length() == 0) {
            Tim_DT();
        } else if (edtdttu.getText().toString().trim().length() == 0 && edtdtden.getText().toString().trim().length() == 0 &&
                edtgiatu.getText().toString().trim().length() == 0 && edtgiaden.getText().toString().trim().length() == 0 &&
                txtngaythue.getText().toString().trim().length() != 0) {
            Tim_NGAY();
        } else if (edtdttu.getText().toString().trim().length() == 0 && edtdtden.getText().toString().trim().length() == 0 &&
                edtgiatu.getText().toString().trim().length() == 0 && edtgiaden.getText().toString().trim().length() == 0 &&
                txtngaythue.getText().toString().trim().length() == 0) {
            DuyetYC_ALLTT_NULL();
        }
    }
    private void Tim_DUONG_LOAI() {
        Progress();
        strDuong=edtDuong.getText().toString();
        db.collection("Nha")
                .whereEqualTo("diaChi.duong",strDuong)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
                        Nha nha = doc.toObject(Nha.class);
                        if(chungchu==true) {
                            if (nha.getLoaiNha().equals(strLoai)
                                    && nha.getChungChu() == true) {
                                arrNhaTimDuocKhiSearch.add(nha);
                            }
                        }else if(chungchu==false){
                            if (nha.getLoaiNha().equals(strLoai)) {
                                arrNhaTimDuocKhiSearch.add(nha);
                            }
                        }
                    }
                    if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()!=0) {
                        Tim_DT_GIA_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()==0 ){
                        Tim_GIA();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        Tim_DT_GIA();
                    } else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_GIA_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_DT_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        Tim_DT();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        DuyetYC_ALLTT_NULL();
                    }
                }
            }
        });
    }
    private void Tim_DUONG() {
        Progress();
        strDuong=edtDuong.getText().toString();
        db.collection("Nha")
                .whereEqualTo("diaChi.duong",strDuong)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
                        Nha nha = doc.toObject(Nha.class);
                        if(chungchu==true) {
                            if (nha.getChungChu() == true) {
                                arrNhaTimDuocKhiSearch.add(nha);
                            }
                        }else if(chungchu==false){
                            arrNhaTimDuocKhiSearch.add(nha);
                        }
                    }
                    if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()!=0) {
                        Tim_DT_GIA_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()==0 ){
                        Tim_GIA();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        Tim_DT_GIA();
                    } else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_GIA_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_DT_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        Tim_DT();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        DuyetYC_ALLTT_NULL();
                    }
                }else {
                    Toast.makeText(TrangChinh.this, "Tìm thất bại!", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            }
        });
    }
    private void Tim_LOAI() {
        Progress();
        strDuong=edtDuong.getText().toString();
        db.collection("Nha").whereEqualTo("loaiNha",strLoai)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
                        Nha nha = doc.toObject(Nha.class);
                        if(chungchu==true) {
                            if (nha.getChungChu() == true) {
                                arrNhaTimDuocKhiSearch.add(nha);
                            }
                        }else if(chungchu==false){
                            arrNhaTimDuocKhiSearch.add(nha);
                        }
                    }
                    if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()!=0) {
                        Tim_DT_GIA_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()==0 ){
                        Tim_GIA();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        Tim_DT_GIA();
                    } else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_GIA_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_DT_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        Tim_DT();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        DuyetYC_ALLTT_NULL();
                    }
                }else {
                    Toast.makeText(TrangChinh.this, "Tìm thất bại!", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            }
        });
    }
    private void Tim_TP() {
        Progress();
        strDuong=edtDuong.getText().toString();
        db.collection("Nha").whereEqualTo("diaChi.thanhPho",strTP)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
                        Nha nha = doc.toObject(Nha.class);
                        if(chungchu==true) {
                            if (nha.getChungChu() == true) {
                                arrNhaTimDuocKhiSearch.add(nha);
                            }
                        }else if(chungchu==false){
                            arrNhaTimDuocKhiSearch.add(nha);
                        }
                    }
                    if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()!=0) {
                        Tim_DT_GIA_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()==0 ){
                        Tim_GIA();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        Tim_DT_GIA();
                    } else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_GIA_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_DT_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        Tim_DT();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        DuyetYC_ALLTT_NULL();
                    }
                }else {
                    Toast.makeText(TrangChinh.this, "Tìm thất bại!", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            }
        });
    }
    private void Tim_TP_LOAI() {
        Progress();
        strDuong=edtDuong.getText().toString();
        db.collection("Nha").whereEqualTo("diaChi.thanhPho",strTP)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
                        Nha nha = doc.toObject(Nha.class);
                        if(chungchu==true) {
                            if (nha.getLoaiNha().equals(strLoai)
                                    && nha.getChungChu() == true) {
                                arrNhaTimDuocKhiSearch.add(nha);

                            }
                        }else if(chungchu==false){
                            if (nha.getLoaiNha().equals(strLoai)) {
                                arrNhaTimDuocKhiSearch.add(nha);
                            }
                        }
                    }
                    if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()!=0) {
                        Tim_DT_GIA_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()==0 ){
                        Tim_GIA();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        Tim_DT_GIA();
                    } else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_GIA_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_DT_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        Tim_DT();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        DuyetYC_ALLTT_NULL();
                    }
                }else {
                    Toast.makeText(TrangChinh.this, "Tìm thất bại!", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            }
        });
    }
    private void Tim_TP_DUONG() {
        Progress();
        strDuong=edtDuong.getText().toString();
        db.collection("Nha").whereEqualTo("diaChi.thanhPho",strTP)
                .whereEqualTo("diaChi.duong",strDuong)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
                        Nha nha = doc.toObject(Nha.class);
                        if(chungchu==true) {
                            if (nha.getChungChu() == true) {
                                arrNhaTimDuocKhiSearch.add(nha);
                            }
                        }else if(chungchu==false){
                            arrNhaTimDuocKhiSearch.add(nha);
                        }
                    }
                    if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()!=0) {
                        Tim_DT_GIA_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()==0 ){
                        Tim_GIA();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        Tim_DT_GIA();
                    } else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_GIA_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_DT_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        Tim_DT();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        DuyetYC_ALLTT_NULL();
                    }
                }else {
                    Toast.makeText(TrangChinh.this, "Tìm thất bại!", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            }
        });
    }
    private void Tim_TP_DUONG_LOAI() {
        Progress();
        strDuong=edtDuong.getText().toString();
        db.collection("Nha").whereEqualTo("diaChi.thanhPho",strTP)
                .whereEqualTo("diaChi.duong",strDuong)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
                        Nha nha = doc.toObject(Nha.class);
                        if(chungchu==true) {
                            if (nha.getLoaiNha().equals(strLoai)
                                    && nha.getChungChu() == true) {
                                arrNhaTimDuocKhiSearch.add(nha);

                            }
                        }else if(chungchu==false){
                            if (nha.getLoaiNha().equals(strLoai)) {
                                arrNhaTimDuocKhiSearch.add(nha);
                            }
                        }
                    }
                    if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()!=0) {
                        Tim_DT_GIA_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()==0 ){
                        Tim_GIA();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        Tim_DT_GIA();
                    } else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_GIA_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_DT_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        Tim_DT();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        DuyetYC_ALLTT_NULL();
                    }
                }else {
                    Toast.makeText(TrangChinh.this, "Tìm thất bại!", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            }
        });
    }
    private void Tim_TP_QUAN() {
        Progress();
        db.collection("Nha").whereEqualTo("diaChi.thanhPho",strTP)
                .whereEqualTo("diaChi.quan",strQuan)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
                        Nha nha = doc.toObject(Nha.class);
                        if(chungchu==true) {
                            if (nha.getChungChu() == true) {
                                arrNhaTimDuocKhiSearch.add(nha);
                            }
                        }else if(chungchu==false){
                            arrNhaTimDuocKhiSearch.add(nha);
                        }
                    }
                    if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()!=0) {
                        Tim_DT_GIA_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()==0 ){
                        Tim_GIA();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        Tim_DT_GIA();
                    } else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_GIA_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_DT_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        Tim_DT();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        DuyetYC_ALLTT_NULL();
                    }
                }else {
                    Toast.makeText(TrangChinh.this, "Tìm thất bại!", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            }
        });
    }
    private void Tim_TP_QUAN_LOAI() {
        Progress();
        db.collection("Nha").whereEqualTo("diaChi.thanhPho",strTP)
                .whereEqualTo("diaChi.quan",strQuan)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
                        Nha nha = doc.toObject(Nha.class);
                        if(chungchu==true) {
                            if (nha.getLoaiNha().equals(strLoai)
                                    && nha.getChungChu() == true) {
                                arrNhaTimDuocKhiSearch.add(nha);

                            }
                        }else if(chungchu==false){
                            if (nha.getLoaiNha().equals(strLoai)) {
                                arrNhaTimDuocKhiSearch.add(nha);
                            }
                        }
                    }
                    if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()!=0) {
                        Tim_DT_GIA_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()==0 ){
                        Tim_GIA();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        Tim_DT_GIA();
                    } else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_GIA_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_DT_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        Tim_DT();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        DuyetYC_ALLTT_NULL();
                    }
                }else {
                    Toast.makeText(TrangChinh.this, "Tìm thất bại!", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            }
        });
    }
    private void Tim_TP_QUAN_DUONG() {
        Progress();
        strDuong=edtDuong.getText().toString();
        db.collection("Nha").whereEqualTo("diaChi.thanhPho",strTP)
                .whereEqualTo("diaChi.quan",strQuan)
                .whereEqualTo("diaChi.duong",strDuong)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
                        Nha nha = doc.toObject(Nha.class);
                        if(chungchu==true) {
                            if (nha.getChungChu() == true) {
                                arrNhaTimDuocKhiSearch.add(nha);
                            }
                        }else if(chungchu==false){
                            arrNhaTimDuocKhiSearch.add(nha);
                        }
                    }
                    if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()!=0) {
                        Tim_DT_GIA_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()==0 ){
                        Tim_GIA();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        Tim_DT_GIA();
                    } else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_GIA_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_DT_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        Tim_DT();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()!=0){
                        Tim_NGAY();
                    }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                            edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                            txtngaythue.getText().toString().trim().length()==0){
                        DuyetYC_ALLTT_NULL();
                    }
                }else {
                    Toast.makeText(TrangChinh.this, "Tìm thất bại!", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            }
        });
    }
    private void Tim_TP_QUAN_DUONG_LOAI() {
            Progress();
            strDuong=edtDuong.getText().toString();
            db.collection("Nha").whereEqualTo("diaChi.thanhPho",strTP)
                    .whereEqualTo("diaChi.quan",strQuan)
                    .whereEqualTo("diaChi.duong",strDuong)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot doc : task.getResult()){
                            Nha nha = doc.toObject(Nha.class);
                            if(chungchu==true) {
                                if (nha.getLoaiNha().equals(strLoai)
                                        && nha.getChungChu() == true) {
                                    arrNhaTimDuocKhiSearch.add(nha);
                                }
                            }else if(chungchu==false){
                                if (nha.getLoaiNha().equals(strLoai)) {
                                    arrNhaTimDuocKhiSearch.add(nha);
                                }
                            }
                        }
                        if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                                edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                                txtngaythue.getText().toString().trim().length()!=0) {
                            Tim_DT_GIA_NGAY();
                        }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                                edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                                txtngaythue.getText().toString().trim().length()==0 ){
                            Tim_GIA();
                        }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                                edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                                txtngaythue.getText().toString().trim().length()==0){
                            Tim_DT_GIA();
                        } else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                                edtgiatu.getText().toString().trim().length()!=0 && edtgiaden.getText().toString().trim().length()!=0 &&
                                txtngaythue.getText().toString().trim().length()!=0){
                            Tim_GIA_NGAY();
                        }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                                edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                                txtngaythue.getText().toString().trim().length()!=0){
                            Tim_DT_NGAY();
                        }else if(edtdttu.getText().toString().trim().length()!=0 && edtdtden.getText().toString().trim().length()!=0 &&
                                edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                                txtngaythue.getText().toString().trim().length()==0){
                            Tim_DT();
                        }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                                edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                                txtngaythue.getText().toString().trim().length()!=0){
                            Tim_NGAY();
                        }else if(edtdttu.getText().toString().trim().length()==0 && edtdtden.getText().toString().trim().length()==0 &&
                                edtgiatu.getText().toString().trim().length()==0 && edtgiaden.getText().toString().trim().length()==0 &&
                                txtngaythue.getText().toString().trim().length()==0){
                            DuyetYC_ALLTT_NULL();
                        }
                    }else {
                        Toast.makeText(TrangChinh.this, "Tìm thất bại!", Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();
                    }
                }
            });
    }
    //////////////// DUYET TT
    private void DuyetYC_ALLTT_NULL() {
        if(vitrichonTP==0 && vitrichonQuan==0 && edtDuong.getText().toString().trim().length()==0 && vitrichonLoaiNha==0) {
            db.collection("DSNhaChiTiet").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (sophong == 0) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                                arrNCTSauKhiSearchTT.add(nct);
                            }
                        } else if (sophong != 0) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                                if(nct.getSoLuongPhong()>=sophong) {
                                    Log.d("DEBUG",nct.getSoLuongPhong()+" - "+sophong);
                                    arrNCTSauKhiSearchTT.add(nct);
                                }
                            }
                        }
                        if (edtsong.getText().toString().trim().length() != 0 && edtsoxe.getText().toString().trim().length() != 0
                                && edthd.getText().toString().trim().length() != 0) {
                            Tim_SONG_SOXE_HD();
                        } else if (edtsong.getText().toString().trim().length() != 0 && edtsoxe.getText().toString().trim().length() == 0
                                && edthd.getText().toString().trim().length() == 0) {
                            Tim_SONG();
                        } else if (edtsong.getText().toString().trim().length() != 0 && edtsoxe.getText().toString().trim().length() != 0
                                && edthd.getText().toString().trim().length() == 0) {
                            Tim_SONG_SOXE();
                        } else if (edtsong.getText().toString().trim().length() != 0 && edtsoxe.getText().toString().trim().length() == 0
                                && edthd.getText().toString().trim().length() != 0) {
                            Tim_SONG_HD();
                        } else if (edtsong.getText().toString().trim().length() == 0 && edtsoxe.getText().toString().trim().length() != 0
                                && edthd.getText().toString().trim().length() != 0) {
                            Tim_SOXE_HD();
                        } else if (edtsong.getText().toString().trim().length() == 0 && edtsoxe.getText().toString().trim().length() != 0
                                && edthd.getText().toString().trim().length() == 0) {
                            Tim_SOXE();
                        } else if (edtsong.getText().toString().trim().length() == 0 && edtsoxe.getText().toString().trim().length() == 0
                                && edthd.getText().toString().trim().length() != 0) {
                            Tim_HD();
                        } else if (edtsong.getText().toString().trim().length() == 0 && edtsoxe.getText().toString().trim().length() == 0
                                && edthd.getText().toString().trim().length() == 0) {
                            DuyetTienIch_ALLYC_NULL();
                        }
                    }
                }
            });
        }else if(vitrichonTP!=0 || vitrichonQuan!=0 || edtDuong.getText().toString().trim().length()!=0 || vitrichonLoaiNha!=0) {
                db.collection("DSNhaChiTiet")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (sophong == 0) {
                                for (DocumentSnapshot doc : task.getResult()) {
                                    NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                                    for (int i = 0; i < arrNhaTimDuocKhiSearch.size(); i++) {
                                        if(nct.getIdNha().compareTo(arrNhaTimDuocKhiSearch.get(i).getId())==0)
                                            arrNCTSauKhiSearchTT.add(nct);
                                    }
                                }
                            } else if (sophong != 0) {
                                for (DocumentSnapshot doc : task.getResult()) {
                                    NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                                    for (int i = 0; i < arrNhaTimDuocKhiSearch.size(); i++) {
                                        if(nct.getIdNha().compareTo(arrNhaTimDuocKhiSearch.get(i).getId())==0)
                                            if(nct.getSoLuongPhong()>=sophong)
                                                 arrNCTSauKhiSearchTT.add(nct);
                                    }
                                }
                            }
                            if (edtsong.getText().toString().trim().length() != 0 && edtsoxe.getText().toString().trim().length() != 0
                                    && edthd.getText().toString().trim().length() != 0) {
                                Tim_SONG_SOXE_HD();
                            } else if (edtsong.getText().toString().trim().length() != 0 && edtsoxe.getText().toString().trim().length() == 0
                                    && edthd.getText().toString().trim().length() == 0) {
                                Tim_SONG();
                            } else if (edtsong.getText().toString().trim().length() != 0 && edtsoxe.getText().toString().trim().length() != 0
                                    && edthd.getText().toString().trim().length() == 0) {
                                Tim_SONG_SOXE();
                            } else if (edtsong.getText().toString().trim().length() != 0 && edtsoxe.getText().toString().trim().length() == 0
                                    && edthd.getText().toString().trim().length() != 0) {
                                Tim_SONG_HD();
                            } else if (edtsong.getText().toString().trim().length() == 0 && edtsoxe.getText().toString().trim().length() != 0
                                    && edthd.getText().toString().trim().length() != 0) {
                                Tim_SOXE_HD();
                            } else if (edtsong.getText().toString().trim().length() == 0 && edtsoxe.getText().toString().trim().length() != 0
                                    && edthd.getText().toString().trim().length() == 0) {
                                Tim_SOXE();
                            } else if (edtsong.getText().toString().trim().length() == 0 && edtsoxe.getText().toString().trim().length() == 0
                                    && edthd.getText().toString().trim().length() != 0) {
                                Tim_HD();
                            } else if (edtsong.getText().toString().trim().length() == 0 && edtsoxe.getText().toString().trim().length() == 0
                                    && edthd.getText().toString().trim().length() == 0) {
                                DuyetTienIch_ALLYC_NULL();
                            }
                        }
                    }
                });


        }
    }
    private void Tim_NGAY() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String ngaythueuserchon = sdf.format(ngaythue);
        final int intngaythueuserchon = Integer.parseInt(ngaythueuserchon);
            db.collection("DSNhaChiTiet")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {

                        if (sophong == 0) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                                int ngaycuanha = Integer.parseInt(sdf.format(nct.getNgayNhan()));
                                for (int i = 0; i < arrNhaTimDuocKhiSearch.size(); i++) {
                                    if (nct.getIdNha().compareTo(arrNhaTimDuocKhiSearch.get(i).getId()) == 0)
                                        if (intngaythueuserchon >= ngaycuanha) {
                                            arrNCTSauKhiSearchTT.add(nct);
                                        }
                                }
                            }
                        } else if (sophong != 0) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                                int ngaycuanha = Integer.parseInt(sdf.format(nct.getNgayNhan()));
                                for (int i = 0; i < arrNhaTimDuocKhiSearch.size(); i++) {
                                    if (nct.getIdNha().compareTo(arrNhaTimDuocKhiSearch.get(i).getId()) == 0)
                                        if (intngaythueuserchon >= ngaycuanha) {
                                            if(nct.getSoLuongPhong()>=sophong)
                                                 arrNCTSauKhiSearchTT.add(nct);
                                        }
                                }
                            }
                        }
                        if (edtsong.getText().toString().trim().length() != 0 && edtsoxe.getText().toString().trim().length() != 0
                                && edthd.getText().toString().trim().length() != 0) {
                            Tim_SONG_SOXE_HD();
                        } else if (edtsong.getText().toString().trim().length() != 0 && edtsoxe.getText().toString().trim().length() == 0
                                && edthd.getText().toString().trim().length() == 0) {
                            Tim_SONG();
                        } else if (edtsong.getText().toString().trim().length() != 0 && edtsoxe.getText().toString().trim().length() != 0
                                && edthd.getText().toString().trim().length() == 0) {
                            Tim_SONG_SOXE();
                        } else if (edtsong.getText().toString().trim().length() != 0 && edtsoxe.getText().toString().trim().length() == 0
                                && edthd.getText().toString().trim().length() != 0) {
                            Tim_SONG_HD();
                        } else if (edtsong.getText().toString().trim().length() == 0 && edtsoxe.getText().toString().trim().length() != 0
                                && edthd.getText().toString().trim().length() != 0) {
                            Tim_SOXE_HD();
                        } else if (edtsong.getText().toString().trim().length() == 0 && edtsoxe.getText().toString().trim().length() != 0
                                && edthd.getText().toString().trim().length() == 0) {
                            Tim_SOXE();
                        } else if (edtsong.getText().toString().trim().length() == 0 && edtsoxe.getText().toString().trim().length() == 0
                                && edthd.getText().toString().trim().length() != 0) {
                            Tim_HD();
                        } else if (edtsong.getText().toString().trim().length() == 0 && edtsoxe.getText().toString().trim().length() == 0
                                && edthd.getText().toString().trim().length() == 0) {
                            DuyetTienIch_ALLYC_NULL();
                        }
                    }
                }
            });
        }
    private void Tim_DT() {
        final int dttu=Integer.parseInt(edtdttu.getText().toString());
        final int dtden=Integer.parseInt(edtdtden.getText().toString());
            db.collection("DSNhaChiTiet")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){

                        if (sophong == 0) {
                            for(DocumentSnapshot doc : task.getResult()) {
                                NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                                for (int i = 0; i < arrNhaTimDuocKhiSearch.size(); i++) {
                                    if (nct.getIdNha().compareTo(arrNhaTimDuocKhiSearch.get(i).getId()) == 0)
                                        if (nct.getDienTich() >= dttu && nct.getDienTich() <= dtden) {
                                            arrNCTSauKhiSearchTT.add(nct);
                                        }
                                }
                            }
                        } else if (sophong != 0) {
                            for(DocumentSnapshot doc : task.getResult()) {
                                NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                                for (int i = 0; i < arrNhaTimDuocKhiSearch.size(); i++) {
                                    if (nct.getIdNha().compareTo(arrNhaTimDuocKhiSearch.get(i).getId()) == 0)
                                        if (nct.getDienTich() >= dttu && nct.getDienTich() <= dtden) {
                                            if(nct.getSoLuongPhong()>=sophong)
                                            arrNCTSauKhiSearchTT.add(nct);
                                        }
                                }
                            }
                        }
                        if(edtsong.getText().toString().trim().length()!=0 && edtsoxe.getText().toString().trim().length()!=0
                                && edthd.getText().toString().trim().length()!=0) {
                            Tim_SONG_SOXE_HD();
                        }else if(edtsong.getText().toString().trim().length()!=0 && edtsoxe.getText().toString().trim().length()==0
                                && edthd.getText().toString().trim().length()==0) {
                            Tim_SONG();
                        }else if(edtsong.getText().toString().trim().length()!=0 && edtsoxe.getText().toString().trim().length()!=0
                                && edthd.getText().toString().trim().length()==0){
                            Tim_SONG_SOXE();
                        }else if(edtsong.getText().toString().trim().length()!=0 && edtsoxe.getText().toString().trim().length()==0
                                && edthd.getText().toString().trim().length()!=0){
                            Tim_SONG_HD();
                        }else if(edtsong.getText().toString().trim().length()==0 && edtsoxe.getText().toString().trim().length()!=0
                                && edthd.getText().toString().trim().length()!=0){
                            Tim_SOXE_HD();
                        }else if(edtsong.getText().toString().trim().length()==0 && edtsoxe.getText().toString().trim().length()!=0
                                && edthd.getText().toString().trim().length()==0){
                            Tim_SOXE();
                        }else if(edtsong.getText().toString().trim().length()==0 && edtsoxe.getText().toString().trim().length()==0
                                && edthd.getText().toString().trim().length()!=0){
                            Tim_HD();
                        }else if(edtsong.getText().toString().trim().length()==0 && edtsoxe.getText().toString().trim().length()==0
                                && edthd.getText().toString().trim().length()==0){
                            DuyetTienIch_ALLYC_NULL();
                        }
                            }
                }
            });
        // progressBar.dismiss();
    }
    private void Tim_DT_NGAY() {
        final int dttu=Integer.parseInt(edtdttu.getText().toString());
        final int dtden=Integer.parseInt(edtdtden.getText().toString());
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String ngaythueuserchon = sdf.format(ngaythue);
        final int intngaythueuserchon = Integer.parseInt(ngaythueuserchon);

            db.collection("DSNhaChiTiet")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){

                        if (sophong == 0) {
                            for(DocumentSnapshot doc : task.getResult()) {
                                NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                                int ngaycuanha = Integer.parseInt(sdf.format(nct.getNgayNhan()));
                                for (int i = 0; i < arrNhaTimDuocKhiSearch.size(); i++) {
                                    if (nct.getIdNha().compareTo(arrNhaTimDuocKhiSearch.get(i).getId()) == 0)
                                        if (nct.getDienTich() >= dttu && nct.getDienTich() <= dtden) {
                                            if (intngaythueuserchon >= ngaycuanha) {
                                                arrNCTSauKhiSearchTT.add(nct);
                                            }
                                        }
                                }
                            }
                        } else if (sophong != 0) {
                            for(DocumentSnapshot doc : task.getResult()) {
                                NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                                int ngaycuanha = Integer.parseInt(sdf.format(nct.getNgayNhan()));
                                for (int i = 0; i < arrNhaTimDuocKhiSearch.size(); i++) {
                                    if (nct.getIdNha().compareTo(arrNhaTimDuocKhiSearch.get(i).getId()) == 0)
                                        if (nct.getDienTich() >= dttu && nct.getDienTich() <= dtden) {
                                            if (intngaythueuserchon >= ngaycuanha) {
                                                if(nct.getSoLuongPhong()>=sophong)
                                                arrNCTSauKhiSearchTT.add(nct);
                                            }
                                        }
                                }
                            }
                        }
                        if(edtsong.getText().toString().trim().length()!=0 && edtsoxe.getText().toString().trim().length()!=0
                                && edthd.getText().toString().trim().length()!=0) {
                            Tim_SONG_SOXE_HD();
                        }else if(edtsong.getText().toString().trim().length()!=0 && edtsoxe.getText().toString().trim().length()==0
                                && edthd.getText().toString().trim().length()==0) {
                            Tim_SONG();
                        }else if(edtsong.getText().toString().trim().length()!=0 && edtsoxe.getText().toString().trim().length()!=0
                                && edthd.getText().toString().trim().length()==0){
                            Tim_SONG_SOXE();
                        }else if(edtsong.getText().toString().trim().length()!=0 && edtsoxe.getText().toString().trim().length()==0
                                && edthd.getText().toString().trim().length()!=0){
                            Tim_SONG_HD();
                        }else if(edtsong.getText().toString().trim().length()==0 && edtsoxe.getText().toString().trim().length()!=0
                                && edthd.getText().toString().trim().length()!=0){
                            Tim_SOXE_HD();
                        }else if(edtsong.getText().toString().trim().length()==0 && edtsoxe.getText().toString().trim().length()!=0
                                && edthd.getText().toString().trim().length()==0){
                            Tim_SOXE();
                        }else if(edtsong.getText().toString().trim().length()==0 && edtsoxe.getText().toString().trim().length()==0
                                && edthd.getText().toString().trim().length()!=0){
                            Tim_HD();
                        }else if(edtsong.getText().toString().trim().length()==0 && edtsoxe.getText().toString().trim().length()==0
                                && edthd.getText().toString().trim().length()==0){
                            DuyetTienIch_ALLYC_NULL();
                        }
                        }
                }
            });
        // progressBar.dismiss();
    }
    private void Tim_GIA_NGAY() {

        final int giatu=Integer.parseInt(edtgiatu.getText().toString());
        final int giaden=Integer.parseInt(edtgiaden.getText().toString());
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String ngaythueuserchon = sdf.format(ngaythue);
        final int intngaythueuserchon = Integer.parseInt(ngaythueuserchon);
            db.collection("DSNhaChiTiet")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        if (sophong == 0) {
                            for(DocumentSnapshot doc : task.getResult()) {
                                NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                                int ngaycuanha = Integer.parseInt(sdf.format(nct.getNgayNhan()));
                                for (int i = 0; i < arrNhaTimDuocKhiSearch.size(); i++) {
                                    if (nct.getIdNha().compareTo(arrNhaTimDuocKhiSearch.get(i).getId()) == 0)
                                        if (nct.getGiaThue() >= giatu && nct.getGiaThue() <= giaden) {
                                            if (intngaythueuserchon >= ngaycuanha) {
                                                arrNCTSauKhiSearchTT.add(nct);

                                            }
                                        }
                                }
                            }
                        } else if (sophong != 0) {
                            for(DocumentSnapshot doc : task.getResult()) {
                                NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                                int ngaycuanha = Integer.parseInt(sdf.format(nct.getNgayNhan()));
                                for (int i = 0; i < arrNhaTimDuocKhiSearch.size(); i++) {
                                    if (nct.getIdNha().compareTo(arrNhaTimDuocKhiSearch.get(i).getId()) == 0)
                                        if (nct.getGiaThue() >= giatu && nct.getGiaThue() <= giaden) {
                                            if (intngaythueuserchon >= ngaycuanha) {
                                                if(nct.getSoLuongPhong()>=sophong)
                                                arrNCTSauKhiSearchTT.add(nct);

                                            }
                                        }
                                }
                            }
                        }
                        if(edtsong.getText().toString().trim().length()!=0 && edtsoxe.getText().toString().trim().length()!=0
                                && edthd.getText().toString().trim().length()!=0) {
                            Tim_SONG_SOXE_HD();
                        }else if(edtsong.getText().toString().trim().length()!=0 && edtsoxe.getText().toString().trim().length()==0
                                && edthd.getText().toString().trim().length()==0) {
                            Tim_SONG();
                        }else if(edtsong.getText().toString().trim().length()!=0 && edtsoxe.getText().toString().trim().length()!=0
                                && edthd.getText().toString().trim().length()==0){
                            Tim_SONG_SOXE();
                        }else if(edtsong.getText().toString().trim().length()!=0 && edtsoxe.getText().toString().trim().length()==0
                                && edthd.getText().toString().trim().length()!=0){
                            Tim_SONG_HD();
                        }else if(edtsong.getText().toString().trim().length()==0 && edtsoxe.getText().toString().trim().length()!=0
                                && edthd.getText().toString().trim().length()!=0){
                            Tim_SOXE_HD();
                        }else if(edtsong.getText().toString().trim().length()==0 && edtsoxe.getText().toString().trim().length()!=0
                                && edthd.getText().toString().trim().length()==0){
                            Tim_SOXE();
                        }else if(edtsong.getText().toString().trim().length()==0 && edtsoxe.getText().toString().trim().length()==0
                                && edthd.getText().toString().trim().length()!=0){
                            Tim_HD();
                        }else if(edtsong.getText().toString().trim().length()==0 && edtsoxe.getText().toString().trim().length()==0
                                && edthd.getText().toString().trim().length()==0){
                            DuyetTienIch_ALLYC_NULL();
                        }
                    }

                }
            });
        // progressBar.dismiss();
    }
    private void Tim_DT_GIA(){
        final int giatu=Integer.parseInt(edtgiatu.getText().toString());
        final int giaden=Integer.parseInt(edtgiaden.getText().toString());
        final int dttu=Integer.parseInt(edtdttu.getText().toString());
        final int dtden=Integer.parseInt(edtdtden.getText().toString());
            db.collection("DSNhaChiTiet")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        if (sophong == 0) {
                            for(DocumentSnapshot doc : task.getResult()) {
                                NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                                for (int i = 0; i < arrNhaTimDuocKhiSearch.size(); i++) {
                                    if (nct.getIdNha().compareTo(arrNhaTimDuocKhiSearch.get(i).getId()) == 0)
                                        if (nct.getGiaThue() >= giatu && nct.getGiaThue() <= giaden) {
                                            if (nct.getDienTich() >= dttu && nct.getDienTich() <= dtden) {
                                                arrNCTSauKhiSearchTT.add(nct);

                                            }
                                        }
                                }
                            }
                        } else if (sophong != 0) {
                            for(DocumentSnapshot doc : task.getResult()) {
                                NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                                for (int i = 0; i < arrNhaTimDuocKhiSearch.size(); i++) {
                                    if (nct.getIdNha().compareTo(arrNhaTimDuocKhiSearch.get(i).getId()) == 0)
                                        if (nct.getGiaThue() >= giatu && nct.getGiaThue() <= giaden) {
                                            if (nct.getDienTich() >= dttu && nct.getDienTich() <= dtden) {
                                                if(nct.getSoLuongPhong()>=sophong)
                                                arrNCTSauKhiSearchTT.add(nct);

                                            }
                                        }
                                }
                            }
                        }
                        if(edtsong.getText().toString().trim().length()!=0 && edtsoxe.getText().toString().trim().length()!=0
                                && edthd.getText().toString().trim().length()!=0) {
                            Tim_SONG_SOXE_HD();
                        }else if(edtsong.getText().toString().trim().length()!=0 && edtsoxe.getText().toString().trim().length()==0
                                && edthd.getText().toString().trim().length()==0) {
                            Tim_SONG();
                        }else if(edtsong.getText().toString().trim().length()!=0 && edtsoxe.getText().toString().trim().length()!=0
                                && edthd.getText().toString().trim().length()==0){
                            Tim_SONG_SOXE();
                        }else if(edtsong.getText().toString().trim().length()!=0 && edtsoxe.getText().toString().trim().length()==0
                                && edthd.getText().toString().trim().length()!=0){
                            Tim_SONG_HD();
                        }else if(edtsong.getText().toString().trim().length()==0 && edtsoxe.getText().toString().trim().length()!=0
                                && edthd.getText().toString().trim().length()!=0){
                            Tim_SOXE_HD();
                        }else if(edtsong.getText().toString().trim().length()==0 && edtsoxe.getText().toString().trim().length()!=0
                                && edthd.getText().toString().trim().length()==0){
                            Tim_SOXE();
                        }else if(edtsong.getText().toString().trim().length()==0 && edtsoxe.getText().toString().trim().length()==0
                                && edthd.getText().toString().trim().length()!=0){
                            Tim_HD();
                        }else if(edtsong.getText().toString().trim().length()==0 && edtsoxe.getText().toString().trim().length()==0
                                && edthd.getText().toString().trim().length()==0){
                            DuyetTienIch_ALLYC_NULL();
                        }
                        }
                }
            });
        // progressBar.dismiss();
    }
    private void Tim_GIA() {
        final int giatu = Integer.parseInt(edtgiatu.getText().toString());
        final int giaden = Integer.parseInt(edtgiaden.getText().toString());
        db.collection("DSNhaChiTiet")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (sophong == 0) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                            for (int i = 0; i < arrNhaTimDuocKhiSearch.size(); i++) {
                                if (nct.getIdNha().compareTo(arrNhaTimDuocKhiSearch.get(i).getId()) == 0)
                                    if (nct.getGiaThue() >= giatu && nct.getGiaThue() <= giaden) {
                                        arrNCTSauKhiSearchTT.add(nct);

                                    }
                            }
                        }
                    } else if (sophong != 0) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                            for (int i = 0; i < arrNhaTimDuocKhiSearch.size(); i++) {
                                if (nct.getIdNha().compareTo(arrNhaTimDuocKhiSearch.get(i).getId()) == 0)
                                    if (nct.getGiaThue() >= giatu && nct.getGiaThue() <= giaden) {
                                        if (nct.getSoLuongPhong() >= sophong)
                                            arrNCTSauKhiSearchTT.add(nct);

                                    }
                            }
                        }
                    }
                    if (edtsong.getText().toString().trim().length() != 0 && edtsoxe.getText().toString().trim().length() != 0
                            && edthd.getText().toString().trim().length() != 0) {
                        Tim_SONG_SOXE_HD();
                    } else if (edtsong.getText().toString().trim().length() != 0 && edtsoxe.getText().toString().trim().length() == 0
                            && edthd.getText().toString().trim().length() == 0) {
                        Tim_SONG();
                    } else if (edtsong.getText().toString().trim().length() != 0 && edtsoxe.getText().toString().trim().length() != 0
                            && edthd.getText().toString().trim().length() == 0) {
                        Tim_SONG_SOXE();
                    } else if (edtsong.getText().toString().trim().length() != 0 && edtsoxe.getText().toString().trim().length() == 0
                            && edthd.getText().toString().trim().length() != 0) {
                        Tim_SONG_HD();
                    } else if (edtsong.getText().toString().trim().length() == 0 && edtsoxe.getText().toString().trim().length() != 0
                            && edthd.getText().toString().trim().length() != 0) {
                        Tim_SOXE_HD();
                    } else if (edtsong.getText().toString().trim().length() == 0 && edtsoxe.getText().toString().trim().length() != 0
                            && edthd.getText().toString().trim().length() == 0) {
                        Tim_SOXE();
                    } else if (edtsong.getText().toString().trim().length() == 0 && edtsoxe.getText().toString().trim().length() == 0
                            && edthd.getText().toString().trim().length() != 0) {
                        Tim_HD();
                    } else if (edtsong.getText().toString().trim().length() == 0 && edtsoxe.getText().toString().trim().length() == 0
                            && edthd.getText().toString().trim().length() == 0) {
                        DuyetTienIch_ALLYC_NULL();
                    }
                }

            }
        });
        // progressBar.dismiss();
    }
    private void Tim_DT_GIA_NGAY() {

            final int giatu=Integer.parseInt(edtgiatu.getText().toString());
            final int giaden=Integer.parseInt(edtgiaden.getText().toString());
            final int dttu=Integer.parseInt(edtdttu.getText().toString());
            final int dtden=Integer.parseInt(edtdtden.getText().toString());
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String ngaythueuserchon = sdf.format(ngaythue);
            final int intngaythueuserchon = Integer.parseInt(ngaythueuserchon);
                db.collection("DSNhaChiTiet")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(sophong==0) {
                                for (DocumentSnapshot doc : task.getResult()) {
                                    NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                                    int ngaycuanha = Integer.parseInt(sdf.format(nct.getNgayNhan()));
                                    for (int i = 0; i < arrNhaTimDuocKhiSearch.size(); i++) {
                                        if (nct.getIdNha().compareTo(arrNhaTimDuocKhiSearch.get(i).getId()) == 0)
                                            if (nct.getGiaThue() >= giatu && nct.getGiaThue() <= giaden)
                                                if (nct.getDienTich() >= dttu && nct.getDienTich() <= dtden)
                                                    if (intngaythueuserchon >= ngaycuanha)
                                                        arrNCTSauKhiSearchTT.add(nct);
                                    }
                                }
                            }else if(sophong!=0) {
                                for (DocumentSnapshot doc : task.getResult()) {
                                    NhaChiTiet nct = doc.toObject(NhaChiTiet.class);
                                    int ngaycuanha = Integer.parseInt(sdf.format(nct.getNgayNhan()));
                                    for (int i = 0; i < arrNhaTimDuocKhiSearch.size(); i++) {
                                        if (nct.getIdNha().compareTo(arrNhaTimDuocKhiSearch.get(i).getId()) == 0)
                                            if (nct.getGiaThue() >= giatu && nct.getGiaThue() <= giaden)
                                                if (nct.getDienTich() >= dttu && nct.getDienTich() <= dtden)
                                                    if (intngaythueuserchon >= ngaycuanha)
                                                        if (nct.getSoLuongPhong() >= sophong)
                                                            arrNCTSauKhiSearchTT.add(nct);

                                    }
                                }
                            }
                            if(edtsong.getText().toString().trim().length()!=0 && edtsoxe.getText().toString().trim().length()!=0
                                    && edthd.getText().toString().trim().length()!=0) {
                                Tim_SONG_SOXE_HD();
                            }else if(edtsong.getText().toString().trim().length()!=0 && edtsoxe.getText().toString().trim().length()==0
                                    && edthd.getText().toString().trim().length()==0) {
                                Tim_SONG();
                            }else if(edtsong.getText().toString().trim().length()!=0 && edtsoxe.getText().toString().trim().length()!=0
                                    && edthd.getText().toString().trim().length()==0){
                                Tim_SONG_SOXE();
                            }else if(edtsong.getText().toString().trim().length()!=0 && edtsoxe.getText().toString().trim().length()==0
                                    && edthd.getText().toString().trim().length()!=0){
                                Tim_SONG_HD();
                            }else if(edtsong.getText().toString().trim().length()==0 && edtsoxe.getText().toString().trim().length()!=0
                                    && edthd.getText().toString().trim().length()!=0){
                                Tim_SOXE_HD();
                            }else if(edtsong.getText().toString().trim().length()==0 && edtsoxe.getText().toString().trim().length()!=0
                                    && edthd.getText().toString().trim().length()==0){
                                Tim_SOXE();
                            }else if(edtsong.getText().toString().trim().length()==0 && edtsoxe.getText().toString().trim().length()==0
                                    && edthd.getText().toString().trim().length()!=0){
                                Tim_HD();
                            }else if(edtsong.getText().toString().trim().length()==0 && edtsoxe.getText().toString().trim().length()==0
                                    && edthd.getText().toString().trim().length()==0){
                                DuyetTienIch_ALLYC_NULL();
                            }
                        }

                    }
                });
           // progressBar.dismiss();
    }
    ////////////////DUYET YC

    private void Tim_HD() {
        int hd = Integer.parseInt(edthd.getText().toString());
        if(nauan==true) {
            for (int i = 0; i < arrNCTSauKhiSearchTT.size(); i++) {
                if (arrNCTSauKhiSearchTT.get(i).getYeuCau().getHopDong() >= hd)
                    if (arrNCTSauKhiSearchTT.get(i).getYeuCau().getChoNauAn())
                        arrNCTSauKhiDuyetYC.add(arrNCTSauKhiSearchTT.get(i));
            }
        }else if(nauan==false){
            for (int i = 0; i < arrNCTSauKhiSearchTT.size(); i++) {
                if (arrNCTSauKhiSearchTT.get(i).getYeuCau().getHopDong() >= hd)
                        arrNCTSauKhiDuyetYC.add(arrNCTSauKhiSearchTT.get(i));
            }
        }
        DuyetTienIch();

    }
    private void Tim_SOXE() {
        int slxe = Integer.parseInt(edtsoxe.getText().toString());

        if(nauan==true) {
            for (int i = 0; i < arrNCTSauKhiSearchTT.size(); i++) {
                if(arrNCTSauKhiSearchTT.get(i).getYeuCau().getSoLuongXe()>=slxe)
                {
                    if (arrNCTSauKhiSearchTT.get(i).getYeuCau().getChoNauAn())
                    arrNCTSauKhiDuyetYC.add(arrNCTSauKhiSearchTT.get(i));

                }
            }
        }else if(nauan==false){
            for (int i = 0; i < arrNCTSauKhiSearchTT.size(); i++) {
                if(arrNCTSauKhiSearchTT.get(i).getYeuCau().getSoLuongXe()>=slxe)
                {
                    arrNCTSauKhiDuyetYC.add(arrNCTSauKhiSearchTT.get(i));

                }
            }
        }
        DuyetTienIch();
    }
    private void Tim_SOXE_HD() {
        int slxe = Integer.parseInt(edtsoxe.getText().toString());
        int hd = Integer.parseInt(edthd.getText().toString());

        if(nauan==true) {
            for (int i = 0; i < arrNCTSauKhiSearchTT.size(); i++) {
                if(
                        arrNCTSauKhiSearchTT.get(i).getYeuCau().getSoLuongXe()>=slxe
                                && arrNCTSauKhiSearchTT.get(i).getYeuCau().getHopDong()>=hd)
                {
                    if (arrNCTSauKhiSearchTT.get(i).getYeuCau().getChoNauAn())
                    arrNCTSauKhiDuyetYC.add(arrNCTSauKhiSearchTT.get(i));

                }
            }
        }else if(nauan==false){
            for (int i = 0; i < arrNCTSauKhiSearchTT.size(); i++) {
                if(
                        arrNCTSauKhiSearchTT.get(i).getYeuCau().getSoLuongXe()>=slxe
                                && arrNCTSauKhiSearchTT.get(i).getYeuCau().getHopDong()>=hd)
                {
                    arrNCTSauKhiDuyetYC.add(arrNCTSauKhiSearchTT.get(i));

                }
            }
        }
        DuyetTienIch();
    }
    private void Tim_SONG_HD() {
        int slnguoi=Integer.parseInt(edtsong.getText().toString());
        int hd = Integer.parseInt(edthd.getText().toString());

        if(nauan==true) {
            for (int i = 0; i < arrNCTSauKhiSearchTT.size(); i++) {
                if(arrNCTSauKhiSearchTT.get(i).getYeuCau().getSoLuongNguoi()>=slnguoi
                        && arrNCTSauKhiSearchTT.get(i).getYeuCau().getHopDong()>=hd)
                {
                    if (arrNCTSauKhiSearchTT.get(i).getYeuCau().getChoNauAn())
                    arrNCTSauKhiDuyetYC.add(arrNCTSauKhiSearchTT.get(i));

                }
            }
        }else if(nauan==false){
            for (int i = 0; i < arrNCTSauKhiSearchTT.size(); i++) {
                if(arrNCTSauKhiSearchTT.get(i).getYeuCau().getSoLuongNguoi()>=slnguoi
                        && arrNCTSauKhiSearchTT.get(i).getYeuCau().getHopDong()>=hd)
                {
                    arrNCTSauKhiDuyetYC.add(arrNCTSauKhiSearchTT.get(i));

                }
            }
        }
        DuyetTienIch();
    }
    private void Tim_SONG_SOXE() {
        int slnguoi=Integer.parseInt(edtsong.getText().toString());
        int slxe = Integer.parseInt(edtsoxe.getText().toString());

        if(nauan==true) {
            for (int i = 0; i < arrNCTSauKhiSearchTT.size(); i++) {
                if(arrNCTSauKhiSearchTT.get(i).getYeuCau().getSoLuongNguoi()>=slnguoi
                        && arrNCTSauKhiSearchTT.get(i).getYeuCau().getSoLuongXe()>=slxe)
                {
                    if (arrNCTSauKhiSearchTT.get(i).getYeuCau().getChoNauAn())
                    arrNCTSauKhiDuyetYC.add(arrNCTSauKhiSearchTT.get(i));

                }
            }
        }else if(nauan==false){
            for (int i = 0; i < arrNCTSauKhiSearchTT.size(); i++) {
                if(arrNCTSauKhiSearchTT.get(i).getYeuCau().getSoLuongNguoi()>=slnguoi
                        && arrNCTSauKhiSearchTT.get(i).getYeuCau().getSoLuongXe()>=slxe)
                {
                    arrNCTSauKhiDuyetYC.add(arrNCTSauKhiSearchTT.get(i));

                }
            }
        }
        DuyetTienIch();
    }
    private void Tim_SONG() {
            int slnguoi=Integer.parseInt(edtsong.getText().toString());

        if(nauan) {
            for (int i = 0; i < arrNCTSauKhiSearchTT.size(); i++) {
                if(arrNCTSauKhiSearchTT.get(i).getYeuCau().getSoLuongNguoi()>=slnguoi)
                {
                    if (arrNCTSauKhiSearchTT.get(i).getYeuCau().getChoNauAn())
                    arrNCTSauKhiDuyetYC.add(arrNCTSauKhiSearchTT.get(i));

                }
            }
        }else if(!nauan){
            for (int i = 0; i < arrNCTSauKhiSearchTT.size(); i++) {
                if(arrNCTSauKhiSearchTT.get(i).getYeuCau().getSoLuongNguoi()>=slnguoi)
                {
                    arrNCTSauKhiDuyetYC.add(arrNCTSauKhiSearchTT.get(i));

                }
            }
        }
        DuyetTienIch();
    }
    private void Tim_SONG_SOXE_HD() {
            int slnguoi=Integer.parseInt(edtsong.getText().toString());
            int slxe = Integer.parseInt(edtsoxe.getText().toString());
            int hd = Integer.parseInt(edthd.getText().toString());

        if(nauan==true) {
            for (int i = 0; i < arrNCTSauKhiSearchTT.size(); i++) {
                if(arrNCTSauKhiSearchTT.get(i).getYeuCau().getSoLuongNguoi()>=slnguoi
                        && arrNCTSauKhiSearchTT.get(i).getYeuCau().getSoLuongXe()>=slxe
                        && arrNCTSauKhiSearchTT.get(i).getYeuCau().getHopDong()>=hd)
                {
                    if (arrNCTSauKhiSearchTT.get(i).getYeuCau().getChoNauAn())
                    arrNCTSauKhiDuyetYC.add(arrNCTSauKhiSearchTT.get(i));

                }
            }
        }else if(nauan==false){
            for (int i = 0; i < arrNCTSauKhiSearchTT.size(); i++) {
                if(arrNCTSauKhiSearchTT.get(i).getYeuCau().getSoLuongNguoi()>=slnguoi
                        && arrNCTSauKhiSearchTT.get(i).getYeuCau().getSoLuongXe()>=slxe
                        && arrNCTSauKhiSearchTT.get(i).getYeuCau().getHopDong()>=hd)
                {
                    arrNCTSauKhiDuyetYC.add(arrNCTSauKhiSearchTT.get(i));

                }
            }
        }
        DuyetTienIch();
        }
    ////////////DUYET TIEN ICH 1
    private void Tim_ML_MG_XE_BV() {
        for(int i=0;i<arrNCTSauKhiDuyetYC.size();i++) {
            if (arrNCTSauKhiDuyetYC.get(i).getTienIch().getMayLanh() == true
                    && arrNCTSauKhiDuyetYC.get(i).getTienIch().getMayGiat() == true
                    && arrNCTSauKhiDuyetYC.get(i).getTienIch().getChoDeXe() == true
                    && arrNCTSauKhiDuyetYC.get(i).getTienIch().getBaoVe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiDuyetYC.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_ML_MG_XE() {
        for(int i=0;i<arrNCTSauKhiDuyetYC.size();i++) {
            if (arrNCTSauKhiDuyetYC.get(i).getTienIch().getMayLanh() == true
                    && arrNCTSauKhiDuyetYC.get(i).getTienIch().getMayGiat() == true
                    && arrNCTSauKhiDuyetYC.get(i).getTienIch().getChoDeXe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiDuyetYC.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_ML_MG_BV() {
        for(int i=0;i<arrNCTSauKhiDuyetYC.size();i++){
            if (arrNCTSauKhiDuyetYC.get(i).getTienIch().getMayLanh() == true
                    && arrNCTSauKhiDuyetYC.get(i).getTienIch().getMayGiat() == true
                    && arrNCTSauKhiDuyetYC.get(i).getTienIch().getBaoVe() == true)
            {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiDuyetYC.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_ML_MG() {
        for(int i=0;i<arrNCTSauKhiDuyetYC.size();i++) {
            if (arrNCTSauKhiDuyetYC.get(i).getTienIch().getMayLanh() == true
                    && arrNCTSauKhiDuyetYC.get(i).getTienIch().getMayGiat() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiDuyetYC.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_ML_XE_BV() {
        for(int i=0;i<arrNCTSauKhiDuyetYC.size();i++) {
            if (arrNCTSauKhiDuyetYC.get(i).getTienIch().getMayLanh() == true
                    && arrNCTSauKhiDuyetYC.get(i).getTienIch().getChoDeXe() == true
                    && arrNCTSauKhiDuyetYC.get(i).getTienIch().getBaoVe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiDuyetYC.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_ML_XE() {
        for(int i=0;i<arrNCTSauKhiDuyetYC.size();i++) {
            if (arrNCTSauKhiDuyetYC.get(i).getTienIch().getMayLanh() == true
                    && arrNCTSauKhiDuyetYC.get(i).getTienIch().getChoDeXe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiDuyetYC.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_ML_BV() {
        for(int i=0;i<arrNCTSauKhiDuyetYC.size();i++) {
            if (arrNCTSauKhiDuyetYC.get(i).getTienIch().getMayLanh() == true
                    && arrNCTSauKhiDuyetYC.get(i).getTienIch().getBaoVe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiDuyetYC.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_ML() {
        for(int i=0;i<arrNCTSauKhiDuyetYC.size();i++) {
            if (arrNCTSauKhiDuyetYC.get(i).getTienIch().getMayLanh() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiDuyetYC.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_All_TI1_NULL() {
        for(int i=0;i<arrNCTSauKhiDuyetYC.size();i++) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiDuyetYC.get(i));
            }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_BV() {
        for(int i=0;i<arrNCTSauKhiDuyetYC.size();i++) {
            if (arrNCTSauKhiDuyetYC.get(i).getTienIch().getBaoVe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiDuyetYC.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_XE() {
        for(int i=0;i<arrNCTSauKhiDuyetYC.size();i++) {
            if (arrNCTSauKhiDuyetYC.get(i).getTienIch().getChoDeXe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiDuyetYC.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_XE_BV() {
        for(int i=0;i<arrNCTSauKhiDuyetYC.size();i++) {
            if (arrNCTSauKhiDuyetYC.get(i).getTienIch().getChoDeXe() == true
                    && arrNCTSauKhiDuyetYC.get(i).getTienIch().getBaoVe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiDuyetYC.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_MG() {
        for(int i=0;i<arrNCTSauKhiDuyetYC.size();i++) {
            if (arrNCTSauKhiDuyetYC.get(i).getTienIch().getMayGiat() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiDuyetYC.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_MG_BV() {
        for(int i=0;i<arrNCTSauKhiDuyetYC.size();i++) {
            if (arrNCTSauKhiDuyetYC.get(i).getTienIch().getMayGiat() == true
                    && arrNCTSauKhiDuyetYC.get(i).getTienIch().getBaoVe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiDuyetYC.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_MG_XE() {
        for(int i=0;i<arrNCTSauKhiDuyetYC.size();i++) {
            if (arrNCTSauKhiDuyetYC.get(i).getTienIch().getMayGiat() == true
                    && arrNCTSauKhiDuyetYC.get(i).getTienIch().getChoDeXe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiDuyetYC.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_MG_XE_BV() {
        for(int i=0;i<arrNCTSauKhiDuyetYC.size();i++) {
            if (arrNCTSauKhiDuyetYC.get(i).getTienIch().getMayGiat() == true
                    && arrNCTSauKhiDuyetYC.get(i).getTienIch().getChoDeXe() == true
                    && arrNCTSauKhiDuyetYC.get(i).getTienIch().getBaoVe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiDuyetYC.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    ///////////////////DUYET TIEN ICH 1 _ KHI ALL YEU CAU NULL
    private void Tim_ML_MG_XE_BV_YCNULL() {
        for(int i=0;i<arrNCTSauKhiSearchTT.size();i++) {
            if (arrNCTSauKhiSearchTT.get(i).getTienIch().getMayLanh() == true
                    && arrNCTSauKhiSearchTT.get(i).getTienIch().getMayGiat() == true
                    && arrNCTSauKhiSearchTT.get(i).getTienIch().getChoDeXe() == true
                    && arrNCTSauKhiSearchTT.get(i).getTienIch().getBaoVe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiSearchTT.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_ML_MG_XE_YCNULL() {
        for(int i=0;i<arrNCTSauKhiSearchTT.size();i++) {
            if (arrNCTSauKhiSearchTT.get(i).getTienIch().getMayLanh() == true
                    && arrNCTSauKhiSearchTT.get(i).getTienIch().getMayGiat() == true
                    && arrNCTSauKhiSearchTT.get(i).getTienIch().getChoDeXe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiSearchTT.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_ML_MG_BV_YCNULL() {
        for(int i=0;i<arrNCTSauKhiSearchTT.size();i++){
            if (arrNCTSauKhiSearchTT.get(i).getTienIch().getMayLanh() == true
                    && arrNCTSauKhiSearchTT.get(i).getTienIch().getMayGiat() == true
                    && arrNCTSauKhiSearchTT.get(i).getTienIch().getBaoVe() == true)
            {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiSearchTT.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_ML_MG_YCNULL() {
        for(int i=0;i<arrNCTSauKhiSearchTT.size();i++) {
            if (arrNCTSauKhiSearchTT.get(i).getTienIch().getMayLanh() == true
                    && arrNCTSauKhiSearchTT.get(i).getTienIch().getMayGiat() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiSearchTT.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_ML_XE_BV_YCNULL() {
        for(int i=0;i<arrNCTSauKhiSearchTT.size();i++) {
            if (arrNCTSauKhiSearchTT.get(i).getTienIch().getMayLanh() == true
                    && arrNCTSauKhiSearchTT.get(i).getTienIch().getChoDeXe() == true
                    && arrNCTSauKhiSearchTT.get(i).getTienIch().getBaoVe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiSearchTT.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_ML_XE_YCNULL() {
        for(int i=0;i<arrNCTSauKhiSearchTT.size();i++) {
            if (arrNCTSauKhiSearchTT.get(i).getTienIch().getMayLanh() == true
                    && arrNCTSauKhiSearchTT.get(i).getTienIch().getChoDeXe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiSearchTT.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_ML_BV_YCNULL() {
        for(int i=0;i<arrNCTSauKhiSearchTT.size();i++) {
            if (arrNCTSauKhiSearchTT.get(i).getTienIch().getMayLanh() == true
                    && arrNCTSauKhiSearchTT.get(i).getTienIch().getBaoVe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiSearchTT.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_ML_YCNULL() {
        for(int i=0;i<arrNCTSauKhiSearchTT.size();i++) {
            if (arrNCTSauKhiSearchTT.get(i).getTienIch().getMayLanh() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiSearchTT.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_All_TI1_NULL_YCNULL() {
        for(int i=0;i<arrNCTSauKhiSearchTT.size();i++) {
            arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiSearchTT.get(i));
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_BV_YCNULL() {
        for(int i=0;i<arrNCTSauKhiSearchTT.size();i++) {
            if (arrNCTSauKhiSearchTT.get(i).getTienIch().getBaoVe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiSearchTT.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_XE_YCNULL() {
        for(int i=0;i<arrNCTSauKhiSearchTT.size();i++) {
            if (arrNCTSauKhiSearchTT.get(i).getTienIch().getChoDeXe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiSearchTT.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_XE_BV_YCNULL() {
        for(int i=0;i<arrNCTSauKhiSearchTT.size();i++) {
            if (arrNCTSauKhiSearchTT.get(i).getTienIch().getChoDeXe() == true
                    && arrNCTSauKhiSearchTT.get(i).getTienIch().getBaoVe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiSearchTT.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_MG_YCNULL() {
        for(int i=0;i<arrNCTSauKhiSearchTT.size();i++) {
            if (arrNCTSauKhiSearchTT.get(i).getTienIch().getMayGiat() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiSearchTT.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_MG_BV_YCNULL() {
        for(int i=0;i<arrNCTSauKhiSearchTT.size();i++) {
            if (arrNCTSauKhiSearchTT.get(i).getTienIch().getMayGiat() == true
                    && arrNCTSauKhiSearchTT.get(i).getTienIch().getBaoVe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiSearchTT.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_MG_XE_YCNULL() {
        for(int i=0;i<arrNCTSauKhiSearchTT.size();i++) {
            if (arrNCTSauKhiSearchTT.get(i).getTienIch().getMayGiat() == true
                    && arrNCTSauKhiSearchTT.get(i).getTienIch().getChoDeXe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiSearchTT.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    private void Tim_MG_XE_BV_YCNULL() {
        for(int i=0;i<arrNCTSauKhiSearchTT.size();i++) {
            if (arrNCTSauKhiSearchTT.get(i).getTienIch().getMayGiat() == true
                    && arrNCTSauKhiSearchTT.get(i).getTienIch().getChoDeXe() == true
                    && arrNCTSauKhiSearchTT.get(i).getTienIch().getBaoVe() == true) {
                arrNCTSauKhiDuyetTI_1.add(arrNCTSauKhiSearchTT.get(i));
            }
        }
        if(san && beprieng && tv){
            Tim_San_Bep_TV();
        }else if(san && beprieng && !tv){
            Tim_San_Bep();
        }else if(san && !beprieng && tv){
            Tim_San_TV();
        }else if(san && !beprieng && !tv){
            Tim_San();
        }else if(!san && beprieng && tv){
            Tim_Bep_TV();
        }else if(!san && beprieng && !tv){
            Tim_Bep();
        }else if(!san && !beprieng && tv){
            Tim_TV();
        }else if(!san && !beprieng && !tv){
            KhongTimGiOTI2();
        }
    }
    //////////DUYET TIEN ICH 2
    private void Tim_San_Bep_TV(){
        for(int i=0;i<arrNCTSauKhiDuyetTI_1.size();i++){
            if(arrNCTSauKhiDuyetTI_1.get(i).getTienIch().getSanPhoiDo() == true
                    && arrNCTSauKhiDuyetTI_1.get(i).getTienIch().getBepNauAn() == true
                    && arrNCTSauKhiDuyetTI_1.get(i).getTienIch().getTruyenHinhCap() == true){
                arrNCTSauKhiDuyetTI_2.add(arrNCTSauKhiDuyetTI_1.get(i));
            }
        }
        if(santhuong && toilet && wifi){
            Tim_ST_Toilet_Wifi();
        }else if(santhuong && toilet && !wifi){
            Tim_ST_Toilet();
        }else if(santhuong && !toilet &&wifi){
            Tim_ST_Wifi();
        }else if(santhuong && !toilet && !wifi){
            Tim_ST();
        }else if(!santhuong && toilet && wifi){
            Tim_Toilet_Wifi();
        }else if(!santhuong && toilet && !wifi){
            Tim_Toilet();
        }else if(!santhuong && !toilet && wifi){
            Tim_Wifi();
        }else if(!santhuong && !toilet && !wifi){
            KhongTimGiOTI3();
        }
    }
    private void Tim_San_Bep(){
        for(int i=0;i<arrNCTSauKhiDuyetTI_1.size();i++){
            if(arrNCTSauKhiDuyetTI_1.get(i).getTienIch().getSanPhoiDo() == true
                    && arrNCTSauKhiDuyetTI_1.get(i).getTienIch().getBepNauAn() == true){
                arrNCTSauKhiDuyetTI_2.add(arrNCTSauKhiDuyetTI_1.get(i));
            }
        }
        if(santhuong && toilet && wifi){
            Tim_ST_Toilet_Wifi();
        }else if(santhuong && toilet && !wifi){
            Tim_ST_Toilet();
        }else if(santhuong && !toilet &&wifi){
            Tim_ST_Wifi();
        }else if(santhuong && !toilet && !wifi){
            Tim_ST();
        }else if(!santhuong && toilet && wifi){
            Tim_Toilet_Wifi();
        }else if(!santhuong && toilet && !wifi){
            Tim_Toilet();
        }else if(!santhuong && !toilet && wifi){
            Tim_Wifi();
        }else if(!santhuong && !toilet && !wifi){
            KhongTimGiOTI3();
        }
    }
    private void Tim_San_TV(){
        for(int i=0;i<arrNCTSauKhiDuyetTI_1.size();i++){
            if(arrNCTSauKhiDuyetTI_1.get(i).getTienIch().getSanPhoiDo() == true
                    && arrNCTSauKhiDuyetTI_1.get(i).getTienIch().getTruyenHinhCap() == true){
                arrNCTSauKhiDuyetTI_2.add(arrNCTSauKhiDuyetTI_1.get(i));
            }
        }
        if(santhuong && toilet && wifi){
            Tim_ST_Toilet_Wifi();
        }else if(santhuong && toilet && !wifi){
            Tim_ST_Toilet();
        }else if(santhuong && !toilet &&wifi){
            Tim_ST_Wifi();
        }else if(santhuong && !toilet && !wifi){
            Tim_ST();
        }else if(!santhuong && toilet && wifi){
            Tim_Toilet_Wifi();
        }else if(!santhuong && toilet && !wifi){
            Tim_Toilet();
        }else if(!santhuong && !toilet && wifi){
            Tim_Wifi();
        }else if(!santhuong && !toilet && !wifi){
            KhongTimGiOTI3();
        }
    }
    private void Tim_San(){
        for(int i=0;i<arrNCTSauKhiDuyetTI_1.size();i++){
            if(arrNCTSauKhiDuyetTI_1.get(i).getTienIch().getSanPhoiDo() == true){
                arrNCTSauKhiDuyetTI_2.add(arrNCTSauKhiDuyetTI_1.get(i));
            }
        }
        if(santhuong && toilet && wifi){
            Tim_ST_Toilet_Wifi();
        }else if(santhuong && toilet && !wifi){
            Tim_ST_Toilet();
        }else if(santhuong && !toilet &&wifi){
            Tim_ST_Wifi();
        }else if(santhuong && !toilet && !wifi){
            Tim_ST();
        }else if(!santhuong && toilet && wifi){
            Tim_Toilet_Wifi();
        }else if(!santhuong && toilet && !wifi){
            Tim_Toilet();
        }else if(!santhuong && !toilet && wifi){
            Tim_Wifi();
        }else if(!santhuong && !toilet && !wifi){
            KhongTimGiOTI3();
        }
    }
    private void Tim_Bep_TV(){
        for(int i=0;i<arrNCTSauKhiDuyetTI_1.size();i++){
            if(arrNCTSauKhiDuyetTI_1.get(i).getTienIch().getBepNauAn() == true
                    && arrNCTSauKhiDuyetTI_1.get(i).getTienIch().getTruyenHinhCap() == true){
                arrNCTSauKhiDuyetTI_2.add(arrNCTSauKhiDuyetTI_1.get(i));
            }
        }
        if(santhuong && toilet && wifi){
            Tim_ST_Toilet_Wifi();
        }else if(santhuong && toilet && !wifi){
            Tim_ST_Toilet();
        }else if(santhuong && !toilet &&wifi){
            Tim_ST_Wifi();
        }else if(santhuong && !toilet && !wifi){
            Tim_ST();
        }else if(!santhuong && toilet && wifi){
            Tim_Toilet_Wifi();
        }else if(!santhuong && toilet && !wifi){
            Tim_Toilet();
        }else if(!santhuong && !toilet && wifi){
            Tim_Wifi();
        }else if(!santhuong && !toilet && !wifi){
            KhongTimGiOTI3();
        }
    }
    private void Tim_Bep(){
        for(int i=0;i<arrNCTSauKhiDuyetTI_1.size();i++){
            if(arrNCTSauKhiDuyetTI_1.get(i).getTienIch().getBepNauAn() == true){
                arrNCTSauKhiDuyetTI_2.add(arrNCTSauKhiDuyetTI_1.get(i));
            }
        }
        if(santhuong && toilet && wifi){
            Tim_ST_Toilet_Wifi();
        }else if(santhuong && toilet && !wifi){
            Tim_ST_Toilet();
        }else if(santhuong && !toilet &&wifi){
            Tim_ST_Wifi();
        }else if(santhuong && !toilet && !wifi){
            Tim_ST();
        }else if(!santhuong && toilet && wifi){
            Tim_Toilet_Wifi();
        }else if(!santhuong && toilet && !wifi){
            Tim_Toilet();
        }else if(!santhuong && !toilet && wifi){
            Tim_Wifi();
        }else if(!santhuong && !toilet && !wifi){
            KhongTimGiOTI3();
        }
    }
    private void Tim_TV(){
        for(int i=0;i<arrNCTSauKhiDuyetTI_1.size();i++){
            if(arrNCTSauKhiDuyetTI_1.get(i).getTienIch().getTruyenHinhCap() == true){
                arrNCTSauKhiDuyetTI_2.add(arrNCTSauKhiDuyetTI_1.get(i));
            }
        }
        if(santhuong && toilet && wifi){
            Tim_ST_Toilet_Wifi();
        }else if(santhuong && toilet && !wifi){
            Tim_ST_Toilet();
        }else if(santhuong && !toilet &&wifi){
            Tim_ST_Wifi();
        }else if(santhuong && !toilet && !wifi){
            Tim_ST();
        }else if(!santhuong && toilet && wifi){
            Tim_Toilet_Wifi();
        }else if(!santhuong && toilet && !wifi){
            Tim_Toilet();
        }else if(!santhuong && !toilet && wifi){
            Tim_Wifi();
        }else if(!santhuong && !toilet && !wifi){
            KhongTimGiOTI3();
        }
    }
    private void KhongTimGiOTI2(){
        for(int i=0;i<arrNCTSauKhiDuyetTI_1.size();i++){
                arrNCTSauKhiDuyetTI_2.add(arrNCTSauKhiDuyetTI_1.get(i));
        }
        if(santhuong && toilet && wifi){
            Tim_ST_Toilet_Wifi();
        }else if(santhuong && toilet && !wifi){
            Tim_ST_Toilet();
        }else if(santhuong && !toilet &&wifi){
            Tim_ST_Wifi();
        }else if(santhuong && !toilet && !wifi){
            Tim_ST();
        }else if(!santhuong && toilet && wifi){
            Tim_Toilet_Wifi();
        }else if(!santhuong && toilet && !wifi){
            Tim_Toilet();
        }else if(!santhuong && !toilet && wifi){
            Tim_Wifi();
        }else if(!santhuong && !toilet && !wifi){
            KhongTimGiOTI3();
        }
    }

    ///////DUYET TIEN ICH 3
    private void Tim_ST_Toilet_Wifi(){
        for (int i=0;i<arrNCTSauKhiDuyetTI_2.size();i++){
            if(arrNCTSauKhiDuyetTI_2.get(i).getTienIch().getSanThuong()
                    && arrNCTSauKhiDuyetTI_2.get(i).getTienIch().getNhaVSRieng()
                    && arrNCTSauKhiDuyetTI_2.get(i).getTienIch().getWifi()){
                arrNCTKetQuaSauCung.add(arrNCTSauKhiDuyetTI_2.get(i));
            }
        }
    }
    private void Tim_ST_Toilet(){
        for (int i=0;i<arrNCTSauKhiDuyetTI_2.size();i++){
            if(arrNCTSauKhiDuyetTI_2.get(i).getTienIch().getSanThuong()
                    && arrNCTSauKhiDuyetTI_2.get(i).getTienIch().getNhaVSRieng()){
                arrNCTKetQuaSauCung.add(arrNCTSauKhiDuyetTI_2.get(i));
            }
        }
    }
    private void Tim_ST_Wifi(){
        for (int i=0;i<arrNCTSauKhiDuyetTI_2.size();i++){
            if(arrNCTSauKhiDuyetTI_2.get(i).getTienIch().getSanThuong()
                    && arrNCTSauKhiDuyetTI_2.get(i).getTienIch().getWifi()){
                arrNCTKetQuaSauCung.add(arrNCTSauKhiDuyetTI_2.get(i));
            }
        }
    }
    private void Tim_ST(){
        for (int i=0;i<arrNCTSauKhiDuyetTI_2.size();i++){
            if(arrNCTSauKhiDuyetTI_2.get(i).getTienIch().getSanThuong()){
                arrNCTKetQuaSauCung.add(arrNCTSauKhiDuyetTI_2.get(i));
            }
        }
    }
    private void Tim_Toilet_Wifi(){
        for (int i=0;i<arrNCTSauKhiDuyetTI_2.size();i++){
            if(arrNCTSauKhiDuyetTI_2.get(i).getTienIch().getNhaVSRieng()
                    && arrNCTSauKhiDuyetTI_2.get(i).getTienIch().getWifi()){
                arrNCTKetQuaSauCung.add(arrNCTSauKhiDuyetTI_2.get(i));
            }
        }
    }
    private void Tim_Toilet(){
        for (int i=0;i<arrNCTSauKhiDuyetTI_2.size();i++){
            if(arrNCTSauKhiDuyetTI_2.get(i).getTienIch().getNhaVSRieng()){
                arrNCTKetQuaSauCung.add(arrNCTSauKhiDuyetTI_2.get(i));
            }
        }
    }
    private void Tim_Wifi(){
        for (int i=0;i<arrNCTSauKhiDuyetTI_2.size();i++){
            if(arrNCTSauKhiDuyetTI_2.get(i).getTienIch().getWifi()==true){
                arrNCTKetQuaSauCung.add(arrNCTSauKhiDuyetTI_2.get(i));
            }
        }
    }
    private void KhongTimGiOTI3(){
        for (int i=0;i<arrNCTSauKhiDuyetTI_2.size();i++){
                arrNCTKetQuaSauCung.add(arrNCTSauKhiDuyetTI_2.get(i));
        }
    }

    ///////////////////////
    private void DuyetTienIch() {
        Log.d("DEBUG",""+sophong);
        Log.d("DEBUG","nau an : "+nauan);
        for(int i=0;i<arrNCTSauKhiDuyetYC.size();i++){
                                if(maylanh && maygiat && chodauxe && anninh){
                                    Tim_ML_MG_XE_BV();
                                }else if (maylanh && maygiat && chodauxe && !anninh){
                                    Tim_ML_MG_XE();
                                }else if (maylanh && maygiat && !chodauxe && anninh){
                                    Tim_ML_MG_BV();
                                }else if (maylanh && maygiat && !chodauxe && !anninh){
                                    Tim_ML_MG();
                                }else if (maylanh && !maygiat && chodauxe && anninh){
                                    Tim_ML_XE_BV();
                                }else if (maylanh && !maygiat && chodauxe && !anninh){
                                    Tim_ML_XE();
                                }else if (maylanh && !maygiat && !chodauxe && anninh){
                                    Tim_ML_BV();
                                }else if (maylanh && !maygiat && !chodauxe && !anninh){
                                    Tim_ML();
                                }else if (!maylanh && !maygiat && !chodauxe && anninh){
                                    Tim_BV();
                                }else if (!maylanh && !maygiat && chodauxe && !anninh){
                                    Tim_XE();
                                }else if (!maylanh && !maygiat && chodauxe && anninh){
                                    Tim_XE_BV();
                                }else if (!maylanh && maygiat && !chodauxe && !anninh){
                                    Tim_MG();
                                }else if (!maylanh && maygiat && !chodauxe && anninh){
                                    Tim_MG_BV();
                                }else if (!maylanh && maygiat && chodauxe && !anninh){
                                    Tim_MG_XE();
                                }else if (!maylanh && maygiat && chodauxe && anninh){
                                    Tim_MG_XE_BV();
                                }else if (!maylanh && !maygiat && !chodauxe && !anninh){
                                    Tim_All_TI1_NULL();
                                }
            }
            Log.d("DCMM","KQ SAU KHI SEARCH XONG : "+arrNCTKetQuaSauCung.size());
        arrNCTKetQuaSauCung = new ArrayList<>(new LinkedHashSet<>(arrNCTKetQuaSauCung));
        Log.d("DCMM","KQ SAU KHI SEARCH XONG + hashset : "+arrNCTKetQuaSauCung.size());
        if(progressBar.isShowing())
            progressBar.dismiss();
        MoFormKetQuaSearch();
    }
    private void DuyetTienIch_ALLYC_NULL() {
        Log.d("DEBUG",""+sophong);
        Log.d("DEBUG","nau an : "+nauan);
        if(nauan==false) {
                if (maylanh && maygiat && chodauxe && anninh) {
                    Tim_ML_MG_XE_BV_YCNULL();
                } else if (maylanh && maygiat && chodauxe && !anninh) {
                    Tim_ML_MG_XE_YCNULL();
                } else if (maylanh && maygiat && !chodauxe && anninh) {
                    Tim_ML_MG_BV_YCNULL();
                } else if (maylanh && maygiat && !chodauxe && !anninh) {
                    Tim_ML_MG_YCNULL();
                } else if (maylanh && !maygiat && chodauxe && anninh) {
                    Tim_ML_XE_BV_YCNULL();
                } else if (maylanh && !maygiat && chodauxe && !anninh) {
                    Tim_ML_XE_YCNULL();
                } else if (maylanh && !maygiat && !chodauxe && anninh) {
                    Tim_ML_BV_YCNULL();
                } else if (maylanh && !maygiat && !chodauxe && !anninh) {
                    Tim_ML_YCNULL();
                } else if (!maylanh && !maygiat && !chodauxe && anninh) {
                    Tim_BV_YCNULL();
                } else if (!maylanh && !maygiat && chodauxe && !anninh) {
                    Tim_XE_YCNULL();
                } else if (!maylanh && !maygiat && chodauxe && anninh) {
                    Tim_XE_BV_YCNULL();
                } else if (!maylanh && maygiat && !chodauxe && !anninh) {
                    Tim_MG_YCNULL();
                } else if (!maylanh && maygiat && !chodauxe && anninh) {
                    Tim_MG_BV_YCNULL();
                } else if (!maylanh && maygiat && chodauxe && !anninh) {
                    Tim_MG_XE_YCNULL();
                } else if (!maylanh && maygiat && chodauxe && anninh) {
                    Tim_MG_XE_BV_YCNULL();
                } else if (!maylanh && !maygiat && !chodauxe && !anninh) {
                    Tim_All_TI1_NULL_YCNULL();
                }

        }else if(nauan==true){
            for (int i = 0; i < arrNCTSauKhiSearchTT.size(); i++) {
                if(!arrNCTSauKhiSearchTT.get(i).getYeuCau().getChoNauAn())
                    arrNCTSauKhiSearchTT.remove(i);
            }
            if (maylanh && maygiat && chodauxe && anninh) {
                Tim_ML_MG_XE_BV_YCNULL();
            } else if (maylanh && maygiat && chodauxe && !anninh) {
                Tim_ML_MG_XE_YCNULL();
            } else if (maylanh && maygiat && !chodauxe && anninh) {
                Tim_ML_MG_BV_YCNULL();
            } else if (maylanh && maygiat && !chodauxe && !anninh) {
                Tim_ML_MG_YCNULL();
            } else if (maylanh && !maygiat && chodauxe && anninh) {
                Tim_ML_XE_BV_YCNULL();
            } else if (maylanh && !maygiat && chodauxe && !anninh) {
                Tim_ML_XE_YCNULL();
            } else if (maylanh && !maygiat && !chodauxe && anninh) {
                Tim_ML_BV_YCNULL();
            } else if (maylanh && !maygiat && !chodauxe && !anninh) {
                Tim_ML_YCNULL();
            } else if (!maylanh && !maygiat && !chodauxe && anninh) {
                Tim_BV_YCNULL();
            } else if (!maylanh && !maygiat && chodauxe && !anninh) {
                Tim_XE_YCNULL();
            } else if (!maylanh && !maygiat && chodauxe && anninh) {
                Tim_XE_BV_YCNULL();
            } else if (!maylanh && maygiat && !chodauxe && !anninh) {
                Tim_MG_YCNULL();
            } else if (!maylanh && maygiat && !chodauxe && anninh) {
                Tim_MG_BV_YCNULL();
            } else if (!maylanh && maygiat && chodauxe && !anninh) {
                Tim_MG_XE_YCNULL();
            } else if (!maylanh && maygiat && chodauxe && anninh) {
                Tim_MG_XE_BV_YCNULL();
            } else if (!maylanh && !maygiat && !chodauxe && !anninh) {
                Tim_All_TI1_NULL_YCNULL();
            }
        }
        Log.d("DCMM","KQ SAU KHI SEARCH XONG : "+arrNCTKetQuaSauCung.size());
        arrNCTKetQuaSauCung = new ArrayList<>(new LinkedHashSet<>(arrNCTKetQuaSauCung));
        Log.d("DCMM","KQ SAU KHI SEARCH XONG + hashset : "+arrNCTKetQuaSauCung.size());
        if(progressBar.isShowing())
            progressBar.dismiss();
        MoFormKetQuaSearch();
    }
    private void MoFormKetQuaSearch(){
        if(arrNCTKetQuaSauCung.size()==0){
            ThongBao("Hiện tại không có phòng nào phù hợp với nhu cầu của bạn , vui lòng thử lại sau");
        }else {
            Intent i = new Intent(TrangChinh.this, KetQuaSauKhiSearch.class);
            i.putExtra("listketqua", arrNCTKetQuaSauCung);
            startActivity(i);
        }
    }
    ///////////////////////////
    public void LoadTP() {
        database = Database.initDatabase(this, DATABASE_NAME);
        c = database.rawQuery("SELECT * FROM TinhThanh", null);
        arrThanhPho.clear();
        arrThanhPho.add("--Chọn tỉnh thành--");
        for (int i = 0; i < c.getCount(); i++) {
            c.moveToPosition(i);
            String ten = c.getString(1);
            arrThanhPho.add(ten);
        }
        adapter_TP = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrThanhPho);
        adapter_TP.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spintp.setAdapter(adapter_TP);
        c.close();
        database.close();
    }
    private void LoadLoaiNha() {


        arrLoaiNha.clear();
        arrLoaiNha.add("--Chọn loại nhà--");
        db.collection("LoaiNha").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()) {
                        LoaiNha loai = document.toObject(LoaiNha.class);
                        arrLoaiNha.add(loai.getTenLoai().toString());
                    }
                    adapter_Loai.notifyDataSetChanged();
                }
            }
        });
        adapter_Loai = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrLoaiNha);
        adapter_Loai.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinloai.setAdapter(adapter_Loai);

    }
    private void LayDuLieuQuanHuyenTuTinhThanh(int idTT){
        database = Database.initDatabase(this, DATABASE_NAME);
        c = database.rawQuery("SELECT * FROM QuanHuyen WHERE idTinhThanh="+idTT, null);
        arrQuan.clear();
        arrQuan.add("--Chọn quận huyện--");
        for (int i = 0; i < c.getCount(); i++) {
            c.moveToPosition(i);
            String ten = c.getString(1);
            arrQuan.add(ten);
        }
        adapter_Quan = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrQuan);
        adapter_Quan.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinquan.setAdapter(adapter_Quan);
        c.close();
        database.close();
    }
    private void XuLyKhiChonNgayThue(){
        imgpickngaythue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }
    private void XuLyKhiChonTP() {
        spintp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vitrichonTP=position;
                arrQuan.clear();
                if(position!=0) {
                    strTP=arrTP.get(position-1);
                    LayIDTinhThanh(strTP);
                    LayDuLieuQuanHuyenTuTinhThanh(idTinhThanh);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void XuLyKhiChonQuan(){
        spinquan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vitrichonQuan=position;
                if(position!=0){
                strQuan=arrQuan.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void XuLyKhiChonLoaiNha(){
        spinloai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vitrichonLoaiNha=position;
                if(position!=0) {
                    strLoai = arrLoaiNha.get(position);
                    adapter_Loai.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                    txtngaythue.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    cal.set(year, month, dayOfMonth);
                    ngaythue = cal.getTime();
                    Log.d("TEST",ngaythue.toString());
                } else {
                    ThongBao("Ngày có thể thuê không nhỏ hơn ngày hiện tại!");
                }
            }
        };
        String s = txtngaythue.getText() + "";
        if (txtngaythue.getText().toString().equals("")) {
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
        DatePickerDialog pic = new DatePickerDialog(TrangChinh.this, callback, nam, thang, ngay);
        pic.setTitle("Ngày thuê ");
        pic.show();
    }

    @Override
    public void onBackPressed() {
        ThongBaoThoat("Bạn muốn thoát ứng dụng?");
    }
    public void ThongBaoThoat(String thongbao){
        AlertDialog.Builder b = new AlertDialog.Builder(TrangChinh.this);
        b.setMessage(thongbao);
        b.setPositiveButton("Ở lại", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        b.create().show();
    }
    public void ThongBaoXoaAllTB(){
        AlertDialog.Builder b = new AlertDialog.Builder(TrangChinh.this);
        b.setMessage("Xóa toàn bộ thông báo?");
        b.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                XoaToanBoThongBao();
            }
        });
        b.create().show();
    }
    public void ThongBaoDangXuat(String thongbao){
        AlertDialog.Builder b = new AlertDialog.Builder(TrangChinh.this);
        b.setMessage(thongbao);
        b.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.setNegativeButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TroVeTrangDangNhap();
            }
        });
        b.create().show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        ResetDuLieuSearch();
        if(user!=null){
            LoadUserFromDB();
            DownloadDSTinDK();
        }
    }

    private void ResetDuLieuSearch() {
        vitrichonTP=0;
        vitrichonQuan=0;
        vitrichonLoaiNha=0;
        chungchu=false;
        nauan=false;
        beprieng=false;
        toilet=false;
        chodauxe=false;
        anninh=false;
        maylanh=false;
        maygiat=false;
        tv=false;
        wifi=false;
        san=false;
        santhuong=false;
        strLoai="";
        strTP="";
        strQuan="";
        strDuong="";
        txtngaythue.setText("");
        if(arrQuan!=null){
            arrQuan.clear();
            if(adapter_Quan!=null){
                adapter_Quan.notifyDataSetChanged();
            }
        }
        LoadTP();
        LoadLoaiNha();
        LoadUserFromDB();
        chkchungchu.setChecked(false);
        chknauan.setChecked(false);
        chksan.setChecked(false);
        chksanthuong.setChecked(false);
        chkbeprieng.setChecked(false);
        chktoilet.setChecked(false);
        chktv.setChecked(false);
        chkwifi.setChecked(false);
        chkmaylanh.setChecked(false);
        chkmaygiat.setChecked(false);
        chkdauxe.setChecked(false);
        chkanninh.setChecked(false);
        edtDuong.setText("");
    }
    ////////////////THÔNG BÁO//////THÔNG BÁO//////THÔNG BÁO//////THÔNG BÁO//////THÔNG BÁO//////THÔNG BÁO//////THÔNG BÁO//////THÔNG BÁO//////THÔNG BÁO
    private void XuLyTB(){
        if(user==null){
            txttitledstb.setVisibility(View.GONE);
            imgxoaall.setVisibility(View.GONE);
        }
        if(arrTB.size()==0){
            txtkocotb.setVisibility(View.VISIBLE);
        }else {
            txtkocotb.setVisibility(View.GONE);
        }
        imgxoaall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThongBaoXoaAllTB();
            }
        });
        btnMOFORMDKTIN_TB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TrangChinh.this,FormDKNhanTB.class);
                startActivity(i);
            }
        });
        btnXEMTINDADK_TB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TimTinCuaUser();
                Intent i = new Intent(TrangChinh.this,ListTinDK.class);
                startActivity(i);
            }
        });
        btnDK_tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TrangChinh.this,FormDKNhanTB.class);
                startActivity(i);
            }
        });
        if(user!=null)
        DownloadDSTB();
    }
    private void CapNhatHienThiTinMoi(){
        int sotinchuaxem=0;
        for(int i=0;i<arrTB.size();i++){
            if(arrTB.get(i).isTrangthai()==false){
                sotinchuaxem++;
            }
        }
        if(sotinchuaxem!=0) {
            badge.setText("" + sotinchuaxem);
            badge2.setText("" + sotinchuaxem);
            if(!badge.isShown()) {
                badge.show();
                badge2.show();
            }
        }
        if(sotinchuaxem==0){
            if(badge.isShown()){
                badge.hide();
                badge2.hide();
            }
        }
    }
    private void DownloadDSTB() {
      registrationDSTB =  db.collection("DSThongBao").whereEqualTo("emailUser",user.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            ThongBao tb = dc.getDocument().toObject(ThongBao.class);
                            arrTB.add(tb);
                            final long todayTime = new Date().getTime();
                            Collections.sort(arrTB, new Comparator<ThongBao>() {
                                public int compare(ThongBao tb1, ThongBao tb2) {
                                    Long dist1 = Math.abs(todayTime-tb1.getNgayGuiTB().getTime());
                                    Long dist2 = Math.abs(todayTime-tb2.getNgayGuiTB().getTime());
                                    return dist1.compareTo(dist2);
                                }

                            });
                            CapNhatHienThiTinMoi();
                            if(adapter_tb!=null)
                                adapter_tb.notifyDataSetChanged();
                            if(arrTB.size()==0){
                                txtkocotb.setVisibility(View.VISIBLE);
                            }else {
                                txtkocotb.setVisibility(View.GONE);
                            }
                            break;
                        case MODIFIED:
                            ThongBao tb_sua = dc.getDocument().toObject(ThongBao.class);
                            for(int i=0;i<arrTB.size();i++){
                                if(arrTB.get(i).getId().compareTo(tb_sua.getId())==0)
                                    arrTB.set(i,tb_sua);
                            }
                            CapNhatHienThiTinMoi();
                            if(adapter_tb!=null)
                                adapter_tb.notifyDataSetChanged();
                            break;
                        case REMOVED:
                            ThongBao tb_xoa = dc.getDocument().toObject(ThongBao.class);
                            for(int i=0;i<arrTB.size();i++){
                                if(arrTB.get(i).getId().compareTo(tb_xoa.getId())==0) {
                                    arrTB.remove(i);
                                }
                            }
                            CapNhatHienThiTinMoi();
                            if(adapter_tb!=null)
                                adapter_tb.notifyDataSetChanged();
                            if(arrTB.size()==0){
                                txtkocotb.setVisibility(View.VISIBLE);
                            }else {
                                txtkocotb.setVisibility(View.GONE);
                            }
                            break;
                    }
                }
            }
        });

    }
    public class LoadBaiDangAsync extends AsyncTask<Void,Void,Void>{
    LoadBaiDangAsync(){

    }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            LoadBaiDang();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
        }
    }
    private void XoaToanBoThongBao(){
        for(int i=0;i<arrTB.size();i++){
            db.collection("DSThongBao").document(arrTB.get(i).getId()).delete();
        }
        arrTB.clear();
        adapter_tb.notifyDataSetChanged();
        if(arrTB.size()==0){
            txtkocotb.setVisibility(View.VISIBLE);
        }else {
            txtkocotb.setVisibility(View.GONE);
        }
    }
    private void TimTinCuaUser(){
        for(int i=0;i<arrTINTB.size();i++){
            if(user.getEmail().compareTo(arrTINTB.get(i).getMailuser())==0){
                MoFormXemTinVaGuiKemTin(arrTINTB.get(i));
                break;
            }
        }
    }
    private void AnhXaTB(){
        View target = findViewById(R.id.tabUser);
        View target2=findViewById(R.id.tabThongBao);
        badge = new BadgeView(this,target);
        badge2 = new BadgeView(this,target2);
        badge2.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
        badge.show();
        badge2.show();
        CapNhatHienThiTinMoi();
        imgxoaall= findViewById(R.id.imgXoaAllTB_ThongBao);
        re_ViewTB= findViewById(R.id.reView_ThongBao);
        re_ViewTB.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        re_ViewTB.setLayoutManager(linearLayoutManager);
        adapter_tb = new Adapter_ThongBao(arrTB,this);
        re_ViewTB.setAdapter(adapter_tb);
        txtkocotb= findViewById(R.id.txtKoCoTB_ThongBao);
        btnMOFORMDKTIN_TB= findViewById(R.id.btnDKTin_ThongBao);
        btnXEMTINDADK_TB= findViewById(R.id.btnXemTin_ThongBao);
        btnDK_tb= findViewById(R.id.btnDangKyThem_ThongBao);
        txttitledstb= findViewById(R.id.txtTitleDSTB_ThongBao);
    }
    private void MoFormXemTinVaGuiKemTin(TinDKTB tin) {
        Intent i = new Intent(TrangChinh.this,FormXemTinDaDK.class);
        Bundle b= new Bundle();
        b.putSerializable("tin",tin);
        i.putExtra("bundle",b);
        startActivity(i);
    }

    /////////////////////
    private void LoadAnhNCT(){
        database = Database.initDatabase(this, DATABASE_NAME);
        database.delete("HinhNCT",null,null);
        db.collection("HinhAnhCuaNhaChiTiet").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document : task.getResult()){
                        HinhAnhNhaChiTiet hinh = document.toObject(HinhAnhNhaChiTiet.class);
                        AddHinhVaoSQLite(hinh);
                    }
                    database.close();
                }
            }
        });
    }
    private void AddHinhVaoSQLite(HinhAnhNhaChiTiet hinh) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Link", hinh.getHinhAnh());
        contentValues.put("idNCT", hinh.getIdNhaChiTiet());
        database.insert("HinhNCT", null, contentValues);
    }
    public class DownAnhNCT extends AsyncTask<Void,Void,Void> {
        public DownAnhNCT(){

        }

        @Override
        protected Void doInBackground(Void... params) {
            LoadAnhNCT();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
