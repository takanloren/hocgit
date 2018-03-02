package com.tungha.datn_timkiemnhatro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import Adapter.Adapter_ListTinDKTB;
import Classs.TinDKTB;

import static com.tungha.datn_timkiemnhatro.MainActivity.mAuth;
import static com.tungha.datn_timkiemnhatro.TrangChinh.arrTINTB;

public class ListTinDK extends AppCompatActivity {
    RecyclerView reView_listtin;
    Button btnback;
    Adapter_ListTinDKTB adapter_listtin;
    public static ArrayList<TinDKTB> arrTinCuaUser = new ArrayList<>();
    FirebaseUser user = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tin_dk);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        AnhXa();
        XuLy();
    }

    private void XuLy() {
        LocTinCuaUser();
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void LocTinCuaUser() {
        arrTinCuaUser.clear();
        for(int i=0;i<arrTINTB.size();i++){
            if(user.getEmail().compareTo(arrTINTB.get(i).getMailuser())==0){
                arrTinCuaUser.add(arrTINTB.get(i));
            }
        }
        final long todayTime = new Date().getTime();
        Collections.sort(arrTinCuaUser, new Comparator<TinDKTB>() {
            public int compare(TinDKTB tin1, TinDKTB tin2) {
                Long dist1 = Math.abs(todayTime - tin1.getNgaydktin().getTime());
                Long dist2 = Math.abs(todayTime - tin2.getNgaydktin().getTime());
                return dist1.compareTo(dist2);
            }

        });
        adapter_listtin.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(arrTinCuaUser.size()==0){
            finish();
        }
        adapter_listtin.notifyDataSetChanged();
    }

    private void AnhXa() {
        reView_listtin= (RecyclerView) findViewById(R.id.reView_ListTinDK);
        reView_listtin.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reView_listtin.setLayoutManager(linearLayoutManager);
        adapter_listtin = new Adapter_ListTinDKTB(arrTinCuaUser,getApplicationContext());
        reView_listtin.setAdapter(adapter_listtin);
        ////
        btnback= (Button) findViewById(R.id.btnTroVe_ListTinDK);
    }
}
