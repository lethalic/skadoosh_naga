package com.example.edgepoint.skadoosh_naga;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class CustomAdapter extends BaseAdapter {
    Context context;
    String[] questionsList, listArr;
    LayoutInflater inflter;
    public static ArrayList<String> selectedAnswers;

    public CustomAdapter(Context applicationContext, String[] questionsList, String[]  listArr) {
        this.context = context;
        this.questionsList = questionsList;
        this.listArr = listArr;

        selectedAnswers = new ArrayList<>();
        for (int i = 0; i < questionsList.length; i++) {
            selectedAnswers.add("");
        }
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return questionsList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        view = inflter.inflate(R.layout.list_items, null);

        TextView question = (TextView) view.findViewById(R.id.question);

        RadioButton atin = (RadioButton) view.findViewById(R.id.atin_id);
        RadioButton baka = (RadioButton) view.findViewById(R.id.baka_id);
        RadioButton kabila = (RadioButton) view.findViewById(R.id.kabila_id);

            String ch_question = questionsList[i].toString();
            String ch_listarr = listArr[i].toString();

            if(ch_listarr.equals("Atin")){
                atin.setChecked(true);
                selectedAnswers.set(i, "Atin");
            }
            else if(ch_listarr.equals("Baka Sakali")){
                baka.setChecked(true);
                selectedAnswers.set(i, "Baka Sakali");
            }
            else if(ch_listarr.equals("Kabila")){
                kabila.setChecked(true);
                selectedAnswers.set(i, "Kabila");
            }

            atin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        selectedAnswers.set(i, "Atin");
                }
            });

            baka.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        selectedAnswers.set(i, "Baka Sakali");
                }
            });

            kabila.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        selectedAnswers.set(i, "Kabila");
                }
            });

        question.setText(questionsList[i]);
        return view;
    }
}
