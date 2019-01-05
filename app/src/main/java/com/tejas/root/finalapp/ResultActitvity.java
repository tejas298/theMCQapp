package com.tejas.root.finalapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tejas.root.finalapp.databases.DatabaseHandler;
import com.tejas.root.finalapp.entities.CoinsStack;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 24/12/18.
 */

public class ResultActitvity extends AppCompatActivity implements View.OnClickListener{

    TextView attempt,wrong,right,total,percentage,gainedCoins;
    Button see;
    ArrayList<JsonResponse> list = new ArrayList<>();
    ImageView back;
    private int[] yData = new int[]{};
    private String[] xData = {"Attempted","Non-attempted","Wrong","Right","Total"};
    PieChart pieChart;
    int[] array;
    Context context;
    int coinsinresult;
    boolean whetherVisited = false;
    String test;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);

        context = getApplicationContext();
        attempt = findViewById(R.id.attempted_holder);
        wrong = findViewById(R.id.wrong_holder);
        right = findViewById(R.id.right_holder);
        total = findViewById(R.id.total_holder);
        back = findViewById(R.id.backToMainFromresult);
        percentage = findViewById(R.id.percentage);
        gainedCoins = findViewById(R.id.gainedCoins);

        list = (ArrayList<JsonResponse>) getIntent().getSerializableExtra("list");
        String temp = getIntent().getStringExtra("visited");
        //Toast.makeText(context,temp,Toast.LENGTH_LONG).show();
        if(temp.equals("N")){
            whetherVisited = false;
        }else if(temp.equals("Y")){
            whetherVisited = true;
        }

        back.setOnClickListener(this);

        see = findViewById(R.id.see_answer);

        see.setOnClickListener(this);

        pieChart = findViewById(R.id.pie_chart);

        array = getIntent().getIntArrayExtra("array");

        test = getIntent().getStringExtra("name");

        yData = array;



        attempt.setText(String.valueOf(array[0]));
        wrong.setText(String.valueOf(array[2]));
        right.setText(String.valueOf(array[3]));
        total.setText(String.valueOf(array[4]));
        percentage.setText(calculatePercentage()+"%");

        Description description = new Description();
        description.setText("Overview of Single test");
        pieChart.setDescription(description);
        pieChart.setRotationEnabled(true);
        //pieChart.setUsePercentValues(true);
        //pieChart.setHoleColor(Color.BLUE);
        //pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Summary");
        pieChart.setCenterTextSize(10);
        //pieChart.setDrawEntryLabels(true);
        //pieChart.setEntryLabelTextSize(20);
        //More options just check out the documentation!

        addDataSet();


    }

    private void addDataSet() {
        Log.d("tejas", "addDataSet started");
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for(int i = 0; i < yData.length; i++){
            yEntrys.add(new PieEntry(yData[i] , i));
        }

        for(int i = 1; i < xData.length; i++){
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Total Summary");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backToMainFromresult:
                onBackPressed();
                break;
            case R.id.see_answer:
                Intent intent = new Intent(view.getContext(), TestActivity.class);
                intent.putExtra("response",list);
                intent.putExtra("type","result");
                intent.putExtra("address","");
                intent.putExtra("name",test);
                startActivity(intent);
                finish();
                break;
        }
    }

    public String calculatePercentage(){
        float rightAns = array[3];
        float totalAns = array[4];

        float percentage = (rightAns/totalAns)*100;
        addCoins((int)percentage);
        return String.format("%.2f",percentage);
    }

    public void addCoins(int percent){
        if(!whetherVisited) {
            CoinsStack stack = new CoinsStack();
            int coins = DatabaseHandler.getInstance(context).getAppDatabase().getTestDao()
                    .getTotalCoins();
            List<CoinsStack> coinsStacks = DatabaseHandler.getInstance(context).getAppDatabase()
                    .getTestDao().getCoins();

            stack.setId(coinsStacks.get(0).getId());

            if (percent >= 80) {
                stack.setCoins(coins + 50);
                gainedCoins.setText(String.valueOf(50));
            } else if (percent < 80 && percent >= 65) {
                stack.setCoins(coins + 30);
                gainedCoins.setText(String.valueOf(30));
            } else if (percent < 65 && percent >= 50) {
                stack.setCoins(coins + 20);
                gainedCoins.setText(String.valueOf(20));
            } else {
                stack.setCoins(coins + 10);
                gainedCoins.setText(String.valueOf(10));
            }

            DatabaseHandler.getInstance(context).getAppDatabase().getTestDao()
                    .updateCoins(stack);

            coinsinresult = DatabaseHandler.getInstance(context).getAppDatabase().getTestDao()
                    .getTotalCoins();
        }else {

            gainedCoins.setText(String.valueOf(0));
        }
    }
}
