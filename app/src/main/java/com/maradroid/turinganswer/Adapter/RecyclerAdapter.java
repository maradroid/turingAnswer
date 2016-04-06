package com.maradroid.turinganswer.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maradroid.turinganswer.DataModel.Rules;
import com.maradroid.turinganswer.R;

import java.util.ArrayList;

/**
 * Created by mara on 4/6/16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ClickListener clickListener;
    private ArrayList<Rules> dataArray;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvTrenutnoStanje;
        private TextView tvProcitanaVrijednost;
        private TextView tvBuduceStanje;
        private TextView tvVrijednostPisanja;
        private TextView tvPomak;

        private LinearLayout llRemove;
        private LinearLayout llEdit;

        public ViewHolder(View v) {
            super(v);

            this.tvTrenutnoStanje = (TextView) v.findViewById(R.id.tv_trenutno_stanje);
            this.tvProcitanaVrijednost = (TextView) v.findViewById(R.id.tv_procitana_vrijednost);
            this.tvBuduceStanje = (TextView) v.findViewById(R.id.tv_buduce_stanje);
            this.tvVrijednostPisanja = (TextView) v.findViewById(R.id.tv_vrijednost_pisanja);
            this.tvPomak = (TextView) v.findViewById(R.id.tv_pomak);

            this.llRemove = (LinearLayout) v.findViewById(R.id.ll_remove_layout);
            this.llEdit = (LinearLayout) v.findViewById(R.id.ll_edit_layout);

            this.llRemove.setOnClickListener(this);
            this.llEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(clickListener != null)
                clickListener.onClick(v, getAdapterPosition());
        }

    }


    public interface ClickListener {
        public void onClick(View v, int position);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public RecyclerAdapter(ArrayList<Rules> data) {
        this.dataArray = data;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rule, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.tvTrenutnoStanje.setText(dataArray.get(position).getTrenutnoStanje());
        viewHolder.tvProcitanaVrijednost.setText(dataArray.get(position).getProcitanaVrijednost());
        viewHolder.tvBuduceStanje.setText(dataArray.get(position).getBuduceStanje());
        viewHolder.tvVrijednostPisanja.setText(dataArray.get(position).getVrijednostPisanja());
        viewHolder.tvPomak.setText(dataArray.get(position).getPomak());
    }

    @Override
    public int getItemCount() {
        return dataArray.size();
    }

}