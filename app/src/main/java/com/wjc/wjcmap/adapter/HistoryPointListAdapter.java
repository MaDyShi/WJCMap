package com.wjc.wjcmap.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wjc.wjcmap.R;
import com.wjc.wjcmap.bean.Bean;
import com.wjc.wjcmap.bean.Point;

import java.util.List;

public class HistoryPointListAdapter extends RecyclerView.Adapter<HistoryPointListAdapter.PiontViewHodler> {

    private LayoutInflater inflater;
    private List<Bean> history;
    public HistoryPointListAdapter(LayoutInflater inflater, List<Bean> history) {
        this.inflater = inflater;
        this.history = history;
    }

    @NonNull
    @Override
    public PiontViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PiontViewHodler(inflater.inflate(R.layout.list_pointitem_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PiontViewHodler holder, int position) {
        if (history.get(position) != null) {
            Bean bean = history.get(position);
            holder.add_titletv.setText(TextUtils.isEmpty(bean.getTitle()) ? "" : bean.getTitle());
            holder.add_hinttv.setText(TextUtils.isEmpty(bean.getAddressId()) ? "" : bean.getAddressId());
            holder.add_layout.setOnClickListener(view -> {
                if (listener != null) {
                    listener.clickPointItem(bean);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    class PiontViewHodler extends RecyclerView.ViewHolder{
        TextView add_titletv,add_hinttv;
        View add_layout;
        public PiontViewHodler(View itemView) {
            super(itemView);
            add_hinttv = itemView.findViewById(R.id.add_hinttv);
            add_titletv = itemView.findViewById(R.id.add_titletv);
            add_layout = itemView.findViewById(R.id.add_layout);
        }
    }


    public interface OnPointItemClickListener{
        void clickPointItem(Bean bean);
    }
    OnPointItemClickListener listener;
    public void setOnPointItemClickListener(OnPointItemClickListener listener){
        this.listener = listener;
    }
}
