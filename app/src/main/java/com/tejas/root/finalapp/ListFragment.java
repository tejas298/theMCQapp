package com.tejas.root.finalapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tejas.root.finalapp.entities.PaperInformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 24/12/18.
 */

public class ListFragment extends Fragment {

    List<PaperInformation> list = new ArrayList<PaperInformation>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    CustomRecyclerAdapter adapter;
    MainActivity activity;
    String type;
    Context listContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.test_list,container,false);


        type = getArguments().getString("type");

        if(type==null){
            type="";
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new CustomRecyclerAdapter(view.getContext(),list);

        recyclerView.setAdapter(adapter);
        manager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(manager);
        adapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
        listContext = context;
        //loadData();

    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String temp = type;
        activity.changeString("Home > "+temp.toUpperCase());
        loadData();
    }

    public void loadData(){
        if(list.size()==0) {
            List<PaperInformation> singleTest = new ArrayList<>();
            for(int i=0;i<activity.paperInformations.size();i++) {
                if (activity.paperInformations.get(i).getType().equals(type)){
                    singleTest.add(activity.paperInformations.get(i));
                }

            }/*
            if(activity.paperInformations.size()>0){
                PaperInformation paper = new PaperInformation();
                paper.setTest_name("somedata");
                paper.setVisited("n");
                paper.setPrice(1);
                paper.setLock("n");
                singleTest.add(paper);
            }else{
                PaperInformation paper = new PaperInformation();
                paper.setTest_name("nodata in activity");
                paper.setVisited("n");
                paper.setPrice(1);
                paper.setLock("n");
                singleTest.add(paper);
            }*/

            list.addAll(singleTest);
        }
    }
}
