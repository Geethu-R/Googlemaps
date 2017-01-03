package com.tringapps.googlemaps;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tringapps.googlemaps.model.Result;
import java.util.List;

/**
 * Created by geethu on 28/12/16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<Result> mresults;
    Callback callback;

    public RecyclerAdapter(List<Result> result, Callback callback) {
        mresults = result;
        this.callback = callback;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txt;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.search_list);
            txt.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           int position = getAdapterPosition();
            double a = mresults.get(position).getGeometry().getLocation().getLat();
            double b = mresults.get(position).getGeometry().getLocation().getLng();
            StringBuilder sb = new StringBuilder(mresults.get(position).getName());
            callback.listener(a,b,sb);
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.txt.setText(mresults.get(position).getName());
    }


    @Override
    public int getItemCount() {
       return mresults.size();

    }

    public interface Callback{
        void listener(double a, double b, StringBuilder sb);
    }


}
