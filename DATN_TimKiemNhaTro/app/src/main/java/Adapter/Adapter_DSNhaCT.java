package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tungha.datn_timkiemnhatro.ChiTietBaiDang;
import com.tungha.datn_timkiemnhatro.ChiTietPhong_Viewer;
import com.tungha.datn_timkiemnhatro.FormThemNhaChiTiet_SuaNha;
import com.tungha.datn_timkiemnhatro.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import Classs.*;

import static com.tungha.datn_timkiemnhatro.ChiTietBaiDang.emailuser_static;
import static com.tungha.datn_timkiemnhatro.MainActivity.db;
import static com.tungha.datn_timkiemnhatro.TrangChinh.InfoUser;

/**
 * Created by Windows 10 TIMT on 08-Nov-17.
 */

public class Adapter_DSNhaCT extends RecyclerView.Adapter<Adapter_DSNhaCT.ViewHolder> {
    ArrayList<NhaChiTiet> arrNCT ;
    ArrayList<NhaChiTiet> arrNCTofNha = new ArrayList<>();
    Context context;
    Activity parent;
    ArrayList<Bitmap> arrBitmap ;
    NhaChiTiet NCT;
    int slphong;
    int mingiamoi=0;
    int slphongmoi=0;

    public Adapter_DSNhaCT(ArrayList<Bitmap> arrBitmap, ArrayList<NhaChiTiet> arrNCT, Context context, Activity parent) {
        this.arrNCT = arrNCT;
        this.context = context;
        this.arrBitmap = arrBitmap;
        this.parent = parent;
    }
    @Override
    public Adapter_DSNhaCT.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_nhachitiet,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Adapter_DSNhaCT.ViewHolder holder, int position) {
        String giathue = NumberFormat.getNumberInstance(Locale.US).format(arrNCT.get(position).getGiaThue());
        holder.txtGiaTien.setText(giathue);
        holder.txtSoLoaiPhong.setText(String.valueOf(position+1));
        if(arrBitmap.size()!=0) {
            if(arrBitmap.size()>=position)
            holder.imgHinh.setImageBitmap(arrBitmap.get(position));
        }
    }
    private void ThongBao(String thongbao){
        AlertDialog.Builder b = new AlertDialog.Builder(parent);
        b.setMessage(thongbao);
        b.setPositiveButton("Không xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new XoaNha().execute();
                parent.finish();
            }
        });
        b.create().show();
    }
    private void LayDSNCTofNha(){
        for(int i=0;i<arrNCT.size();i++){
            if(arrNCT.get(i).getIdNha().compareTo(NCT.getIdNha())==0)
                arrNCTofNha.add(arrNCT.get(i));
        }
        Log.d("DEBUG","size nct of nha :"+arrNCTofNha.size());
    }
    private void LayMinGiaMoi(){
        if(arrNCTofNha.size()!=0) {
            mingiamoi = arrNCTofNha.get(0).getGiaThue();
            Log.d("DEBUG", "min gia moi truoc khi duyet :" + mingiamoi);
            for (int i = 1; i < arrNCTofNha.size(); i++) {
                if (arrNCTofNha.get(i).getGiaThue() < mingiamoi) {
                    mingiamoi = arrNCTofNha.get(i).getGiaThue();
                }
            }
        }
        Log.d("DEBUG","min gia moi sau duyet :"+mingiamoi);
    }
    private void LaySLPhongMoi(){
        for(int i=1;i<arrNCTofNha.size();i++){
            slphongmoi+=arrNCTofNha.get(i).getSoLuongPhong();
        }
    }
    private void XoaTBofNCTBiXoa(){
        Log.d("DEBUG","chạy hàm xóa tb khi xóa nct");
        Log.d("DEBUG","id nct : "+NCT.getId());
        db.collection("DSThongBao").whereEqualTo("idNhaChiTiet",NCT.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Log.d("DEBUG","tìm thấy nct");
                    for(DocumentSnapshot doc : task.getResult()){
                        ThongBao tb = doc.toObject(ThongBao.class);
                        db.collection("DSThongBao").document(tb.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d("DEBUG","xóa thành công");
                                }
                            }
                        });
                    }
                }
            }
        });
    }
    private void UpdateNha() {
        db.collection("Nha").document(NCT.getIdNha()).update(
                "giaThapNhat",mingiamoi,
                "soLuongPhong",slphongmoi);
    }
    private void XoaNhaDaDang(){
        db.collection("Nha").document(NCT.getIdNha()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void XoaBLofNha(){
        db.collection("DSBinhLuan").whereEqualTo("idNha",NCT.getIdNha()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
                        BinhLuan bl = doc.toObject(BinhLuan.class);
                        db.collection("DSBinhLuan").document(bl.getId()).delete();
                    }
                }
            }
        });
    }
    private void XoaDGofNha(){
        db.collection("DSDanhGiaChiTiet").whereEqualTo("idNha",NCT.getIdNha()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
                        DanhGiaChiTiet dgct = doc.toObject(DanhGiaChiTiet.class);
                        db.collection("DSDanhGiaChiTiet").document(dgct.getId()).delete();
                    }
                }
            }
        });
    }
    private void MoFormCTPhong(){
        Intent i = new Intent(context, ChiTietPhong_Viewer.class);
        Bundle b= new Bundle();
        b.putSerializable("dulieunct",NCT);
        i.putExtra("bundle",b);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
    private void KiemTraPhongCuoi(){
        for(int i=0;i<arrNCT.size();i++){
            if(arrNCT.get(i).getIdNha().compareTo(NCT.getIdNha())==0)
                slphong++;
        }
    }
    private void MoFormSuaNhaCT(){
        Intent i = new Intent(context, FormThemNhaChiTiet_SuaNha.class);
        Bundle b= new Bundle();
        b.putSerializable("dulieunct",NCT);
        i.putExtra("bundle",b);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
    private void XoaNCT(String idNCT){
        this.notifyDataSetChanged();
        db.collection("DSNhaChiTiet").document(idNCT).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                UpdateNha();
                Toast.makeText(context, "Đã xóa!", Toast.LENGTH_SHORT).show();
            }
        });
        db.collection("HinhAnhCuaNhaChiTiet").whereEqualTo("idNhaChiTiet",idNCT).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
                        String idHinh = doc.getId();
                        db.collection("HinhAnhCuaNhaChiTiet").document(idHinh).delete();
                    }
                }
            }
        });
    }
    class XoaNha extends AsyncTask<Void,Void,Void>{
    XoaNha(){

    }
        @Override
        protected Void doInBackground(Void... params) {
            XoaNhaDaDang();
            XoaBLofNha();
            XoaDGofNha();
            XoaTBofNCTBiXoa();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    @Override
    public int getItemCount() {
        return arrNCT.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnCreateContextMenuListener {
        ImageView imgHinh;
        TextView txtSoLoaiPhong,txtGiaTien;
        public ViewHolder(View itemView) {
            super(itemView);
            imgHinh= (ImageView) itemView.findViewById(R.id.imgHinh_DSNhaCT);
            txtSoLoaiPhong= (TextView) itemView.findViewById(R.id.txtPhongLoai_DSNhaCT);
            txtGiaTien= (TextView) itemView.findViewById(R.id.txtGiaTien_DSNhaCT);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NCT = arrNCT.get(getAdapterPosition());
                    MoFormCTPhong();
                }
            });
            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);
            if(InfoUser!=null && emailuser_static!=null) {
                if (InfoUser.getEmail().compareTo(emailuser_static) == 0) {
                    itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                            menu.add(Menu.NONE, 0, Menu.NONE, "Sửa").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    NCT = arrNCT.get(getAdapterPosition());
                                    MoFormSuaNhaCT();
                                    return false;
                                }
                            });
                            menu.add(Menu.NONE, 1, Menu.NONE, "Xóa").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    slphong=0;
                                    NCT = arrNCT.get(getAdapterPosition());
                                    KiemTraPhongCuoi();
                                    if(slphong>1) {
                                        arrNCT.remove(getAdapterPosition());
                                        LayDSNCTofNha();
                                        LayMinGiaMoi();
                                        LaySLPhongMoi();
                                        XoaTBofNCTBiXoa();
                                        XoaNCT(NCT.getId());
                                    }else if(slphong==1){
                                        ThongBao("Đây là phòng cuối của nhà, nếu xóa sẽ tự động xóa bài đăng , tiếp tục?");
                                    }
                                    return false;
                                }
                            });
                        }
                    });
                }
            }

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
