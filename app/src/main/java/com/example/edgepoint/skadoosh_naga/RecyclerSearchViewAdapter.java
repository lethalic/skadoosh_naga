package com.example.edgepoint.skadoosh_naga;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecyclerSearchViewAdapter extends RecyclerView.Adapter<RecyclerSearchViewAdapter.SearchViewHolder> implements Filterable {

    private ArrayList<ViewInfoRecycler> exampleList;
    private ArrayList<ViewInfoRecycler> exampleListFull;

    private OnItemCLickListener rListener;

    public interface OnItemCLickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener (OnItemCLickListener listener){
        rListener = listener;
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder{

        public TextView dTextView1;
        public TextView dTextView2;

        public SearchViewHolder(@NonNull View itemView, final OnItemCLickListener listener) {
            super(itemView);
            dTextView1 = itemView.findViewById(R.id.textView);
            dTextView2 = itemView.findViewById(R.id.textView2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);

                        }
                    }

                }
            });
        }
    }

    public  RecyclerSearchViewAdapter(ArrayList<ViewInfoRecycler> exampleList) {
        this.exampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewinfosearch, parent, false);
        SearchViewHolder evh = new SearchViewHolder(v, rListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int position) {
        ViewInfoRecycler currentItem = exampleList.get(position);
        searchViewHolder.dTextView1.setText(currentItem.getName());


    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }


    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ViewInfoRecycler> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ViewInfoRecycler item : exampleListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            exampleList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
}
