package com.tejas.root.finalapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 24/12/18.
 */

public class TestActivity extends AppCompatActivity implements View.OnClickListener,FragmentListener{

    TextView time,submit,totalQuestionsSign,attemptedSign,testName;
    LinearLayout quit,inputLayout;
    Button previous,save,next,exit;
    MyAsyncTask asyncTask;
    CircleImageView imageView;
    QuestionsFragment questionsFragment;
    OptionsFragment optionsFragment;
    FragmentTransaction tx;
    boolean flag = false;
    ArrayList<JsonResponse> responseList;
    int counter;
    String fileName;
    String indi;
    String visited;
    String test;
    boolean isResult = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        fileName = getIntent().getStringExtra("address");

        time = findViewById(R.id.timestamp);
        submit = findViewById(R.id.submitTest);

        previous = findViewById(R.id.previousbutton);
        next = findViewById(R.id.nextbutton);

        imageView = findViewById(R.id.user_image_test);

        totalQuestionsSign = findViewById(R.id.totalQuestions);
        attemptedSign = findViewById(R.id.attemptedQuestions);
        quit = findViewById(R.id.quit_layout);
        inputLayout = findViewById(R.id.inputActivity);
        exit = findViewById(R.id.exitWindowbutton);
        testName = findViewById(R.id.testName);

        indi = getIntent().getStringExtra("type");
        visited = getIntent().getStringExtra("visited");
        test = getIntent().getStringExtra("name");

        loadImage();
        if(!indi.equals("result")) {
            JsonFileReader reader = new JsonFileReader();
            responseList = (ArrayList<JsonResponse>) reader.doProcessing(getApplicationContext(), fileName);
        }else{
            responseList = (ArrayList<JsonResponse>) getIntent().getSerializableExtra("response");
        }


        if(responseList.size()>0){
            counter=0;
        }

        totalQuestionsSign.setText("Total Questions : "+responseList.size());

        submit.setOnClickListener(this);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        previous.setVisibility(View.INVISIBLE);
        quit.setOnClickListener(this);
        exit.setOnClickListener(this);

        testName.setText(test);

        if(indi.equals("result")){
            inputLayout.setVisibility(View.GONE);
            exit.setVisibility(View.VISIBLE);
            time.setText("00:00");
            isResult = true;
        }else{
            inputLayout.setVisibility(View.VISIBLE);
            exit.setVisibility(View.GONE);

            asyncTask = new MyAsyncTask();
            flag = true;
            asyncTask.execute(0);
        }
/*
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asyncTask = new MyAsyncTask();
                flag = true;
                asyncTask.execute(0);

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = false;
                asyncTask.cancel(true);
            }
        });
*/
        optionsFragment = new OptionsFragment();

        questionsFragment = new QuestionsFragment();


        getSupportFragmentManager().beginTransaction().
                replace(R.id.questionFragment,questionsFragment,"questionTag")
                .commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.radioAnswerFragemnt,optionsFragment,"answerTag")
                .commit();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(!isResult) {
            optionsFragment.setRadioButtons(responseList.get(0).getOptions());
            questionsFragment.setQuestion(responseList.get(0).getQuestion());
        }else{
            optionsFragment.setRadioButtonsForResult(responseList.get(0).getOptions(),
                    responseList.get(0).getAns(), String.valueOf(responseList.get(0).getUser_ans()));
            questionsFragment.setQuestion(responseList.get(0).getQuestion());
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.previousbutton :
                if(!(counter==0)){
                    previous.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                    counter--;

                    if(isResult){
                        optionsFragment.setRadioButtonsForResult(responseList.get(counter).getOptions(),
                                responseList.get(counter).getAns(), String.valueOf(responseList.get(counter).getUser_ans()));
                    }else{

                        optionsFragment.setRadioButtons(responseList.get(counter).getOptions());
                    }

                    questionsFragment.setQuestion(responseList.get(counter).getQuestion());
                    //Toast.makeText(getApplicationContext(),responseList.get(counter).getUser_ans()==0?"null":"value",Toast.LENGTH_SHORT).show();
                    optionsFragment.checkWhetherHadResponse(responseList.get(counter).getUser_ans()==0?'x':responseList.get(counter).getUser_ans());
                    if(counter == 0){
                        previous.setVisibility(View.INVISIBLE);
                    }
                }else {
                    previous.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.nextbutton:
                if(!(counter==responseList.size()-1)){
                    next.setVisibility(View.VISIBLE);
                    previous.setVisibility(View.VISIBLE);
                    counter++;

                    if(isResult){
                        optionsFragment.setRadioButtonsForResult(responseList.get(counter).getOptions(),
                                responseList.get(counter).getAns(), String.valueOf(responseList.get(counter).getUser_ans()));
                    }else{

                        optionsFragment.setRadioButtons(responseList.get(counter).getOptions());
                    }

                    questionsFragment.setQuestion(responseList.get(counter).getQuestion());
                    //Toast.makeText(getApplicationContext(),responseList.get(counter).getUser_ans()==0?"null":"value",Toast.LENGTH_SHORT).show();
                    optionsFragment.checkWhetherHadResponse(responseList.get(counter).getUser_ans()==0?'x':responseList.get(counter).getUser_ans());
                    if(counter == responseList.size()-1){
                        next.setVisibility(View.INVISIBLE);
                    }
                }else{
                    next.setVisibility(View.INVISIBLE);
                    previous.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.submitTest :
                final AlertDialog.Builder submitAlert = new AlertDialog.Builder(TestActivity.this);

                submitAlert.setMessage("Are you sure ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int[] array = calculateResult();

                                Intent intent = new Intent(getApplicationContext(),ResultActitvity.class);
                                asyncTask.cancel(true);
                                intent.putExtra("list",responseList);
                                intent.putExtra("array",array);
                                intent.putExtra("visited",visited);
                                intent.putExtra("name",test);
                                startActivity(intent);

                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                final AlertDialog dialogSubmit = submitAlert.create();

                dialogSubmit.show();


                break;
            case R.id.quit_layout:
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TestActivity.this);

                alertDialog.setMessage("This will end your Exam session ?")
                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int[] array = calculateResult();

                                Intent intent = new Intent(getApplicationContext(),ResultActitvity.class);
                                asyncTask.cancel(true);
                                intent.putExtra("list",responseList);
                                intent.putExtra("array",array);
                                intent.putExtra("visited",visited);
                                intent.putExtra("name",test);
                                startActivity(intent);

                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                final AlertDialog dialog = alertDialog.create();

                dialog.show();



                break;

            case R.id.exitWindowbutton:
                finish();
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Quit Test ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(asyncTask!=null) {
                                asyncTask.cancel(true);
                            }
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();


        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRadioButtonChangedListener(char ch) {
        //Toast.makeText(getApplicationContext()," pointer at "+ch+" for counter "+counter,Toast.LENGTH_SHORT).show();
        responseList.get(counter).setUser_ans(ch);
        int attemCounter = 0;
        /*for(JsonResponse res : responseList){
            if(res.getUser_ans()!=0){
                attemCounter++;
            }
        }*/

        int[] result = calculateResult();

        attemptedSign.setText("Attempted : "+result[0]);
    }


    public int[] calculateResult(){
        int attempted = 0;
        int non_attempted = 0;
        int wrong=0,right=0,total=0;
        total = responseList.size();
        for(int i=0;i<responseList.size();i++){
            if(responseList.get(i).getUser_ans()==0){
                non_attempted++;
            }
            else{
                attempted++;
                char[] ch = responseList.get(i).getAns().toCharArray();

                if(ch[0] == responseList.get(i).getUser_ans()){
                    right++;
                }else{
                    wrong++;
                }
            }



        }

        return new int[]{attempted,non_attempted,wrong,right,total};

    }


    private class MyAsyncTask extends AsyncTask<Integer,Integer,Integer>{

        int count;
        int min,sec;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            count = 0;
            min = 4;
            sec = 60;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            count = integers[0];
            while(flag) {
                try {
                    Thread.sleep(1000);
                    count++;
                    sec--;
                    publishProgress(sec);
                } catch (Exception e) {
                    Log.d("tejas", e.getMessage());

                }
            }
            return count;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            if(min==0 && sec==0){
                flag = true;

                int[] array = calculateResult();
                Intent intent = new Intent(getApplicationContext(),ResultActitvity.class);

                intent.putExtra("list",responseList);
                intent.putExtra("array",array);
                intent.putExtra("visited",visited);
                intent.putExtra("name",test);
                startActivity(intent);

                finish();
                asyncTask.cancel(true);
            }

            if(sec == 0){

                time.setText(String.format("%02d",min)+":"+String.format("%02d",sec));
                min--;
                sec = 60;
            }else{
                time.setText(String.format("%02d",min)+":"+String.format("%02d",sec));
            }

            //time.setText(values[0].toString());
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            time.setText(integer.toString());
        }
    }

    public void loadImage(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String url = pref.getString("img",null);
        if(url==null){
            Glide.with(this).load(R.drawable.profile_default).into(imageView);
        }else{
            Glide.with(this).load(url).into(imageView);
        }

    }

}
