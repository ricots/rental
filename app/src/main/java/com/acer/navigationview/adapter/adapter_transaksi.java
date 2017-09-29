package com.acer.navigationview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acer.navigationview.R;
import com.acer.navigationview.oop.Item;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

public class adapter_transaksi extends RecyclerView.Adapter<adapter_transaksi.ViewHolder> {
    private ImageLoader imageLoader;
    private Context context;
    List<Item> transaksi;

    public adapter_transaksi(List<Item> transaksi, Context context){
        super();
        //Getting all the superheroes
        this.transaksi = transaksi;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_transaksi, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Item trans =  transaksi.get(position);
        holder.list_pinjam.setText(trans.getTgl_pinjam());
        holder.list_kembali.setText(trans.getTgl_kembali());
        holder.list_lama.setText(trans.getLama());
        holder.list_total.setText(trans.getTotal_bayar());
        holder.item = trans;
    }

    @Override
    public int getItemCount() {
        return transaksi.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView list_pinjam,list_kembali,list_lama,list_total;
        ImageView imageView_brg;
        Item item;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView_brg = (ImageView) itemView.findViewById(R.id.imageView_brg);
            list_pinjam = (TextView) itemView.findViewById(R.id.list_pinjam);
            list_kembali = (TextView) itemView.findViewById(R.id.list_kembali);
            list_lama = (TextView) itemView.findViewById(R.id.list_lama);
            list_total = (TextView) itemView.findViewById(R.id.list_total);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}