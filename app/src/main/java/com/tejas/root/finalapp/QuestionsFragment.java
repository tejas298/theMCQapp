package com.tejas.root.finalapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by root on 24/12/18.
 */

public class QuestionsFragment extends Fragment {

    TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.question_fragment,container,false);

        textView = view.findViewById(R.id.question);

        return view;
    }

    public void setQuestion(String question){
        textView.setText(question);
    }
}
