package Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tungha.datn_timkiemnhatro.FormXemTinDaDK;
import com.tungha.datn_timkiemnhatro.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import Classs.TinDKTB;

/**
 * Created by Windows 10 TIMT on 08-Nov-17.
 */

public class Adapter_ListTinDKTB extends RecyclerView.Adapter<Adapter_ListTinDKTB.ViewHolder>{
    ArrayList<TinDKTB> arrTin ;
    Context context;
    TinDKTB tin;
    public Adapter_ListTinDKTB(ArrayList<TinDKTB> arrTin, Context context) {
        this.arrTin = arrTin;
        this.context = context;
    }
    @Override
    public Adapter_ListTinDKTB.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_listtindktb,parent,false);
        return new Adapter_ListTinDKTB.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtstt.setText(String.valueOf(position+1));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String ngaydk = sdf.format(arrTin.get(position).getNgaydktin());
        holder.txtngaydang.setText(ngaydk);
    }

    private void MoTTCTTin(){
        Intent i = new Intent(context,FormXemTinDaDK.class);
        Bundle b= new Bundle();
        b.putSerializable("tin",tin);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("bundle",b);
        context.startActivity(i);
    }
    @Override
    public int getItemCount() {
        return arrTin.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtstt,txtngaydang;
        public ViewHolder(View itemView) {
            super(itemView);
            txtstt= (TextView) itemView.findViewById(R.id.txtSTT_ItemTin);
            txtngaydang= (TextView) itemView.findViewById(R.id.txtNgayDK_ItemTin);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tin = arrTin.get(getAdapterPosition());
                    MoTTCTTin();
                }
            });
        }
    }
}
