package com.tungha.datn_timkiemnhatro;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;

import Adapter.Adapter_DSNha_DaLuu;
import Adapter.Adapter_DSNha_SearchDone;
import Classs.Nha;
import Classs.NhaChiTiet;

import static com.tungha.datn_timkiemnhatro.MainActivity.db;

public class KetQuaSauKhiSearch extends AppCompatActivity {
    RecyclerView reView_KQ;
    Adapter_DSNha_SearchDone adapter;
    public static ArrayList<NhaChiTiet> arrnct_da_nhan = new ArrayList<>();
    ArrayList<Nha> arrNha = new ArrayList<>();
    ArrayList<String> arrIDNHA= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ket_qua_sau_khi_search);
        AnhXa();
        XuLy();
        NhanNCT();


    }

    private void XuLy() {

    }

    private void NhanNCT() {
        arrnct_da_nhan.clear();
        Intent i = getIntent();
        arrnct_da_nhan= (ArrayList<NhaChiTiet>) i.getSerializableExtra("listketqua");
        DownloadNhaFromNCT();
    }

    private void DownloadNhaFromNCT() {
        arrNha.clear();
        arrnct_da_nhan = new ArrayList<>(new LinkedHashSet<>(arrnct_da_nhan));
        for(int k=0;k<arrnct_da_nhan.size();k++){
            arrIDNHA.add(arrnct_da_nhan.get(k).getIdNha());
        }
        arrIDNHA = new ArrayList<>(new LinkedHashSet<>(arrIDNHA));
        for(int i=0;i<arrIDNHA.size();i++){
            db.collection("Nha")
                    .document(arrIDNHA.get(i))
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.getResult().exists()) {
                        Nha nha = task.getResult().toObject(Nha.class);
                        arrNha.add(nha);
                        adapter.notifyDataSetChanged();
                    }
                    final long todayTime3 = new Date().getTime();
                    Collections.sort(arrNha, new Comparator<Nha>() {
                        public int compare(Nha nha1, Nha nha2) {
                            Long dist1 = Math.abs(todayTime3 - nha1.getNgayDang().getTime());
                            Long dist2 = Math.abs(todayTime3 - nha2.getNgayDang().getTime());
                            return dist1.compareTo(dist2);
                        }

                    });
                }
            });
        }



    }

    private void AnhXa() {
        reView_KQ= (RecyclerView) findViewById(R.id.reView_KQSEARCH);
        reView_KQ.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reView_KQ.setLayoutManager(linearLayoutManager);
        adapter = new Adapter_DSNha_SearchDone(arrNha, getApplicationContext());
        reView_KQ.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
