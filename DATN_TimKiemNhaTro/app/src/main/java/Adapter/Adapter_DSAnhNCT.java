package Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tungha.datn_timkiemnhatro.R;

import java.util.ArrayList;

import Classs.NhaChiTiet;

/**
 * Created by Windows 10 TIMT on 08-Nov-17.
 */

public class Adapter_DSAnhNCT extends RecyclerView.Adapter<Adapter_DSAnhNCT.ViewHolder>{
    ArrayList<Bitmap> arrHinh ;
    Context context;

    public Adapter_DSAnhNCT(ArrayList<Bitmap> arrHinh, Context context) {
        this.arrHinh = arrHinh;
        this.context = context;
    }
    @Override
    public Adapter_DSAnhNCT.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_dsanhnct,parent,false);
        return new Adapter_DSAnhNCT.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imgHinh.setImageBitmap(arrHinh.get(position));
    }


    @Override
    public int getItemCount() {
        return arrHinh.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHinh;
        public ViewHolder(View itemView) {
            super(itemView);
            imgHinh= (ImageView) itemView.findViewById(R.id.imgAnh_NCT);

        }
    }
}
