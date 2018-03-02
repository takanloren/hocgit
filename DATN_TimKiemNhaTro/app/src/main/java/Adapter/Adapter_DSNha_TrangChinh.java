package Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.DrawableCrossFadeTransition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.squareup.picasso.Picasso;
import com.tungha.datn_timkiemnhatro.ChiTietBaiDang;
import com.tungha.datn_timkiemnhatro.R;
import com.tungha.datn_timkiemnhatro.TrangChinh;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Classs.LuuBai;
import Classs.Nha;
import Classs.NhaChiTiet;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.tungha.datn_timkiemnhatro.MainActivity.db;
import static com.tungha.datn_timkiemnhatro.MainActivity.mAuth;
import static com.tungha.datn_timkiemnhatro.TrangChinh.arrLB;
import static com.tungha.datn_timkiemnhatro.TrangChinh.switchTabs;

/**
 * Created by Windows 10 TIMT on 08-Nov-17.
 */

public class Adapter_DSNha_TrangChinh extends RecyclerView.Adapter<Adapter_DSNha_TrangChinh.ViewHolder> {
    ArrayList<Nha> arrNha ;
    ArrayList<Nha> arrNhaDaLuu;
    Context context;
    String ngaydang,so,duong,phuong,quan,tinh,strdiemtb;
    float diemtb=0;
    Nha nhaduocchon;
    LuuBai save = new LuuBai();
    FirebaseUser user = mAuth.getCurrentUser();
    boolean check = false;
    private GestureDetectorCompat mDetector;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    SimpleDateFormat iftoday = new SimpleDateFormat("'Hôm nay, lúc 'HH:mm");
    SimpleDateFormat ifinyear = new SimpleDateFormat("dd' tháng 'MM' lúc 'HH:mm");
    SimpleDateFormat ifpastyear = new SimpleDateFormat("dd' tháng 'MM' năm 'yyyy' lúc 'HH:mm");
    ///////
    SimpleDateFormat today = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat year = new SimpleDateFormat("yyyy");
    public Adapter_DSNha_TrangChinh(ArrayList<Nha> arrNha,Context context) {
        this.arrNha = arrNha;
        this.context = context;
        this.arrNhaDaLuu=arrNhaDaLuu;
    }
    @Override
    public Adapter_DSNha_TrangChinh.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_bai_dang_trang_chinh,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Adapter_DSNha_TrangChinh.ViewHolder holder, int position) {
        mDetector = new GestureDetectorCompat(context, new MyGestureListener());

        if(user!=null){
            if(arrNha.get(position).isDaluu()==true){
                holder.imgLuuBai.setImageResource(R.drawable.dsdaluu2);
                holder.imgLuuBai.setTag(1);
            }else {
                holder.imgLuuBai.setImageResource(R.drawable.dsdaluu2_unselected);
                holder.imgLuuBai.setTag(2);
            }

        }else
            holder.imgLuuBai.setVisibility(View.GONE);
        Glide.with(context)
                .load(arrNha.get(position).getUser().getHinhAnh())
                .into(holder.imgHinhUser);
        Glide.with(context)
                .load(arrNha.get(position).getHinhAnh())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.progress_image_loading))
                        .transition(DrawableTransitionOptions.withCrossFade(1222))
                .into(holder.imgHinhNha);
        holder.txtTenUser.setText(arrNha.get(position).getUser().getHoTen());

        ///////
        String layngaycuanha = today.format(arrNha.get(position).getNgayDang());
        String laynamcuanha = year.format(arrNha.get(position).getNgayDang());
        ///////
        Date date_today = new Date();
        String ngayhientai = today.format(date_today);
        String namhientai = year.format(date_today);
        if(layngaycuanha.compareTo(ngayhientai)==0){
            ngaydang = iftoday.format(arrNha.get(position).getNgayDang());
        }else if(laynamcuanha.compareTo(namhientai)==0){
            ngaydang=ifinyear.format(arrNha.get(position).getNgayDang());
        }else {
            ngaydang=ifpastyear.format(arrNha.get(position).getNgayDang());
        }
        holder.txtNgayDang.setText(ngaydang);
        String giathapnhat = NumberFormat.getNumberInstance(Locale.US).format(arrNha.get(position).getGiaThapNhat());
        holder.txtGiaTu.setText(giathapnhat+" VNĐ");
        holder.txtSoPhong.setText(String.valueOf(arrNha.get(position).getSoLuongPhong()));
        so = arrNha.get(position).getDiaChi().getSoNha();
        duong = arrNha.get(position).getDiaChi().getDuong();
        phuong = arrNha.get(position).getDiaChi().getPhuong();
        quan =arrNha.get(position).getDiaChi().getQuan();
        tinh = arrNha.get(position).getDiaChi().getThanhPho();
        holder.txtDiaChi.setText(so+" "+duong+","+phuong+","+quan+","+tinh);
        holder.rating.setRating(arrNha.get(position).getDanhGia().getDiemTrungBinh());
        diemtb=arrNha.get(position).getDanhGia().getDiemTrungBinh();
        strdiemtb = String.format("%.01f", diemtb);
        holder.txtDiemTB.setText(strdiemtb+"/5");


    }
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(DEBUG_TAG,"onDown: " + event.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            if(event1.getX() - event2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
                switchTabs(false);
            }else if(event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
                switchTabs(true);
            }
            return true;
        }
    }
    private void MoFormXemCTBai(){
        Intent i = new Intent(context, ChiTietBaiDang.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("dulieunha",nhaduocchon);
        i.putExtra("data",bundle);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
    private void HuySaveBaiLuu(){
        db.collection("NhaDaLuu").document(user.getEmail()).collection("DSNhaDaLuu").document(nhaduocchon.getId()).delete();
    }
    private void SaveBaiLuu(){
        save.setIdNha(nhaduocchon.getId());
        db.collection("NhaDaLuu").document(user.getEmail()).set(save);
        db.collection("NhaDaLuu").document(user.getEmail()).collection("DSNhaDaLuu").document(nhaduocchon.getId()).set(save).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(context, "Lưu bài thất bại! Kiểm tra lại kết nối", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrNha.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgHinhNha,imgLuuBai;
        CircleImageView imgHinhUser;
        TextView txtTenUser,txtNgayDang,txtDiaChi,txtSoPhong,txtGiaTu,txtDiemTB;
        RatingBar rating;
        public ViewHolder(View itemView) {
            super(itemView);
            txtDiemTB= (TextView) itemView.findViewById(R.id.txtDiemTB_ItemDSBai);
            imgLuuBai= (ImageView) itemView.findViewById(R.id.imgLuuBai_ItemDSBai);
            imgHinhNha= (ImageView) itemView.findViewById(R.id.imgNha_ItemDSBai);
            imgHinhUser= (CircleImageView) itemView.findViewById(R.id.imgAvatarUser_ItemDSBai);
            txtDiaChi= (TextView) itemView.findViewById(R.id.txtDiaChi_ItemDSBai);
            txtGiaTu= (TextView) itemView.findViewById(R.id.txtGiaTu_ItemDSBai);
            txtTenUser= (TextView) itemView.findViewById(R.id.txtTenUser_ItemDSBai);
            txtNgayDang= (TextView) itemView.findViewById(R.id.txtNgayDang_ItemDSBai);
            txtSoPhong= (TextView) itemView.findViewById(R.id.txtTongSoPhong_ItemDSBai);
            rating= (RatingBar) itemView.findViewById(R.id.ratingBar_ItemDSBai);
            imgLuuBai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(user!=null) {
                        nhaduocchon = arrNha.get(getAdapterPosition());
                        if(imgLuuBai.getTag().equals(2)) {
                            SaveBaiLuu();
                            imgLuuBai.setImageResource(R.drawable.dsdaluu2);
                            imgLuuBai.setTag(1);
                        }else if(imgLuuBai.getTag().equals(1)){
                            HuySaveBaiLuu();
                            imgLuuBai.setImageResource(R.drawable.dsdaluu2_unselected);
                            imgLuuBai.setTag(2);
                        }
                    }
                }
            });
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                   mDetector.onTouchEvent(event);
                    return false;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nhaduocchon = arrNha.get(getAdapterPosition());
                    MoFormXemCTBai();
                }
            });
        }
    }
}
