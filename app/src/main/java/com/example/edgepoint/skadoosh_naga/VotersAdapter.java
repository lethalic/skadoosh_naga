package com.example.edgepoint.skadoosh_naga;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VotersAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> votersList = new ArrayList<>();
    private List<String> indicatorList = new ArrayList<>();
    private List<String> deceasedList = new ArrayList<>();


    public VotersAdapter(Context context, List<String> list, List<String> indicator, List<String> deceased) {
        super(context, 0 , list);
        mContext = context;
        votersList = list;
        indicatorList = indicator;
        deceasedList = deceased;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.voters_item,parent,false);

        String currentMovie = votersList.get(position).toString();

        TextView name = (TextView) listItem.findViewById(R.id.textView_name);
        name.setText(currentMovie);

        for (int i = 0; i < votersList.size(); i++) {
            if (position == i){
                String indicator_status = "";
                String deceased_status = "";
                try {
                    indicator_status = indicatorList.get(position).toString();
                    deceased_status = deceasedList.get(position).toString();
                } catch (Exception e) {
                    e.toString();
                }

                if (indicator_status.equals("on") && deceased_status.equals("no")) {
                    listItem.setBackgroundColor(Color.parseColor("#FFBB33"));
                }
                else if(indicator_status.equals("on") && deceased_status.equals("yes"))
                {
                    listItem.setBackgroundColor(Color.parseColor("#F44256"));
                }
                else if(indicator_status.equals("off"))
                {
                    listItem.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        }

        return listItem;
    }
}
