package com.tungha.datn_timkiemnhatro;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import Classs.Database;
import Classs.HinhAnhNhaChiTiet;

import static com.tungha.datn_timkiemnhatro.MainActivity.database;
import static com.tungha.datn_timkiemnhatro.MainActivity.db;

public class LoadingScreen extends AppCompatActivity {
    ProgressDialog progressBar;
    String DATABASE_NAME = "TimKiemNhaTro3.sqlite";
    SQLiteDatabase database;
    Cursor c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        database = Database.initDatabase(this, DATABASE_NAME);
        database.delete("HinhNCT",null,null);
        loading();
    }
    private void loading() {
        progressBar = new ProgressDialog(LoadingScreen.this);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.hide();
        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;
                try {
                    sleep(0);
                    jumpTime += 5;
                    LoadAnhNCT();
                    progressBar.setProgress(jumpTime);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
    private void LoadAnhNCT(){
        db.collection("HinhAnhCuaNhaChiTiet").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document : task.getResult()){
                        HinhAnhNhaChiTiet hinh = document.toObject(HinhAnhNhaChiTiet.class);
                        AddHinhVaoSQLite(hinh);
                    }
                    database.close();
                    progressBar.dismiss();
                    QuaTrangDangNhap();

                }
            }
        });
    }

    private void QuaTrangDangNhap() {
        Intent i = new Intent(LoadingScreen.this,MainActivity.class);
        startActivity(i);
        finish();
    }

    private void AddHinhVaoSQLite(HinhAnhNhaChiTiet hinh) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Link", hinh.getHinhAnh());
        contentValues.put("idNCT", hinh.getIdNhaChiTiet());
        database.insert("HinhNCT", null, contentValues);
    }
}
