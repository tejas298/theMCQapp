package com.tejas.root.finalapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by root on 24/12/18.
 */

public class OptionsFragment extends Fragment implements View.OnClickListener {

    RadioButton a,b,c,d;
    RadioGroup group;
    FragmentListener listener;
    TestActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.options_fragment,container,false);

        a = view.findViewById(R.id.a);
        b = view.findViewById(R.id.b);
        c = view.findViewById(R.id.c);
        d = view.findViewById(R.id.d);

        group = view.findViewById(R.id.radioGroup);

        //group.setOnCheckedChangeListener(this);

        a.setOnClickListener(this);
        b.setOnClickListener(this);
        c.setOnClickListener(this);
        d.setOnClickListener(this);
        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (TestActivity) getActivity();
        if(activity.isResult){
            doViewForResult();
        }
    }

    private void doViewForResult() {

    }

    public void setRadioButtonsForResult(JsonResponse.Options options,String ans,String userAns){
        a.setEnabled(false);
        b.setEnabled(false);
        c.setEnabled(false);
        d.setEnabled(false);

        a.setText(options.getA());
        b.setText(options.getB());
        c.setText(options.getC());
        d.setText(options.getD());

        a.setBackgroundColor(Color.parseColor("#f5f5f0"));
        b.setBackgroundColor(Color.parseColor("#f5f5f0"));
        c.setBackgroundColor(Color.parseColor("#f5f5f0"));
        d.setBackgroundColor(Color.parseColor("#f5f5f0"));

        if(userAns.contains("a")){
            a.setBackgroundColor(Color.RED);
            a.setChecked(true);
        }else if(userAns.contains("b")){
            b.setBackgroundColor(Color.RED);
            b.setChecked(true);
        }else if(userAns.contains("c")){
            c.setBackgroundColor(Color.RED);
            c.setChecked(true);
        }else if(userAns.contains("d")){
            d.setBackgroundColor(Color.RED);
            d.setChecked(true);
        }

        if(ans.contains("a")){
            a.setBackgroundColor(Color.GREEN);
        }else if(ans.contains("b")){
            b.setBackgroundColor(Color.GREEN);
        }else if(ans.contains("c")){
            c.setBackgroundColor(Color.GREEN);
        }else if(ans.contains("d")){
            d.setBackgroundColor(Color.GREEN);
        }



    }

    public void setRadioButtons(JsonResponse.Options options){
        a.setText(options.getA());
        b.setText(options.getB());
        c.setText(options.getC());
        d.setText(options.getD());
    }

    public void checkWhetherHadResponse(char ch){

        if(ch == 'x'){
            group.clearCheck();
        }else{
            switch (ch){
                case 'a':
                    a.setChecked(true);
                    break;
                case 'b':
                    b.setChecked(true);
                    break;
                case 'c':
                    c.setChecked(true);
                    break;
                case 'd':
                    d.setChecked(true);
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        char ch = 0;
        switch(view.getId()){
            case R.id.a:
                ch = 'a';
                break;
            case R.id.b:
                ch = 'b';
                break;
            case R.id.c:
                ch = 'c';
                break;
            case R.id.d:
                ch = 'd';
                break;
        }
        activity.onRadioButtonChangedListener(ch);
    }
/*
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        int id = radioGroup.getCheckedRadioButtonId();
        char ch = 0;
        switch(id){
            case R.id.a:
                ch = 'a';
                break;
            case R.id.b:
                ch = 'b';
                break;
            case R.id.c:
                ch = 'c';
                break;
            case R.id.d:
                ch = 'd';
                break;
        }
        activity.onRadioButtonChangedListener(ch);
    }*/
}
