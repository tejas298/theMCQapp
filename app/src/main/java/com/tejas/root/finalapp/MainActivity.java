package com.tejas.root.finalapp;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tejas.root.finalapp.databases.DatabaseHandler;
import com.tejas.root.finalapp.entities.CoinsStack;
import com.tejas.root.finalapp.entities.PaperInformation;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements StringChanger,View.OnClickListener,RewardedVideoAdListener {

    Fragment main_fragment;
    FragmentManager manager;
    FragmentTransaction tx;
    Context context;
    TextView indexText,totalCoinsTextView;
    ImageView addCoins;
    InterstitialAd interstitialAd;
    RewardedVideoAd rewardedVideoAd;
    int totalCoins;
    ScheduledExecutorService executorService;
    List<PaperInformation> paperInformations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        indexText = findViewById(R.id.index_text);
        totalCoinsTextView = findViewById(R.id.totalCoins);

        addCoins = findViewById(R.id.addCoins);

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-7385091305730363~4823817621");//original
        //MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");//dummy

        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(this);
        loadAwardedVideo();


        if(savedInstanceState!=null){
            main_fragment = getSupportFragmentManager().getFragment(savedInstanceState,"saved") ;
        }else{
            main_fragment = new BlocksFragment();
        }

        if(main_fragment==null){
            main_fragment = new BlocksFragment();
        }

        manager = getSupportFragmentManager();
        tx = manager.beginTransaction();
        tx.replace(R.id.below_fragment,main_fragment).addToBackStack(null).commit();



        context = getApplicationContext();
        checkWhetherDataPresent();
        getCoinsStatus();

        prepareAd();
        scheduleAd();

        getloadVideoStatus();

        totalCoinsTextView.setText(String.valueOf(totalCoins));
        totalCoinsTextView.setOnClickListener(this);
        addCoins.setOnClickListener(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        List<Fragment> i = getSupportFragmentManager().getFragments();
        Fragment savedFragment = i.get(i.size()-1);

        if(savedFragment!=null){
            getSupportFragmentManager().putFragment(outState,"saved",savedFragment);
        }
    }

    public void getloadVideoStatus(){
        storedate();
        storeDateIntoSharedPreference();
    }

    public void updateStatus(){
        storeDateIntoSharedPreference();
    }

    public int getCounterStatus(){
        SharedPreferences pref = context.getSharedPreferences("loadedvideo", Context.MODE_PRIVATE);
        String count = pref.getString("count",String.valueOf(0));

        return Integer.valueOf(count);

    }

    public void storedate(){
        SharedPreferences pref = context.getSharedPreferences("loadedvideo", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateString = formatter.format(date).substring(0,2);
        editor.putString("date",dateString);
        editor.apply();
        editor.commit();
    }

    public void storeDateIntoSharedPreference(){
        SharedPreferences pref = context.getSharedPreferences("loadedvideo", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateString = formatter.format(date).substring(0,2);

        String[] array = loadDataIntoSharedPreference();

        String storedDate = array[0];
        int count = Integer.valueOf(array[1]);

        if(storedDate==null){
            storedDate="";
        }


        if(dateString.equals(storedDate)){
            count++;
        }else{
            count=0;
        }

        if(count>5){
            count--;
        }

        editor.putString("date",dateString);
        editor.putString("count",String.valueOf(count));
        editor.apply();
        editor.commit();

        //Toast.makeText(context,storedDate+" "+String.valueOf(count),Toast.LENGTH_SHORT).show();


    }

    public String[] loadDataIntoSharedPreference(){
        SharedPreferences pref = context.getSharedPreferences("loadedvideo", Context.MODE_PRIVATE);
        String[] array = {pref.getString("date",null),pref.getString("count",String.valueOf(0))};
        return array;
    }

    @Override
    protected void onResume() {
        if(executorService.isShutdown()){
            scheduleAd();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        stopad();
        super.onPause();
    }

    public void prepareAd(){
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-7385091305730363/7464161510");//original
        //interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");//dummy
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public void loadAwardedVideo(){
        rewardedVideoAd.loadAd("ca-app-pub-7385091305730363/6397480097",
                new AdRequest.Builder().build());//original

        //rewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                //new AdRequest.Builder().build());//dummy
    }

    public void scheduleAd(){
        executorService =
                Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(interstitialAd.isLoaded()){
                            interstitialAd.show();
                        }else{
                            Log.d("ad","Not loaded");
                        }

                        prepareAd();
                    }
                });
            }
        },30,30, TimeUnit.SECONDS);

    }

    public void stopad(){
        executorService.shutdown();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCoinsStatus();
        loadDataFromDatabase();
        totalCoinsTextView.setText(String.valueOf(totalCoins));

        if(executorService.isShutdown()){
            scheduleAd();
        }
    }

    public void afterDeletionDataRecovery(){
        getCoinsStatus();
        readDataFromFile();
        totalCoinsTextView.setText(String.valueOf(totalCoins));
    }


    @Override
    public void onBackPressed() {
        List<Fragment> list = getSupportFragmentManager().getFragments();
        if(list.get(list.size()-1) instanceof BlocksFragment) {
            finish();
        } if(list.get(list.size()-1) instanceof SignInFragment){
            super.onBackPressed();
            changeString("Home > ");
            main_fragment = new BlocksFragment();
            tx = manager.beginTransaction();
            tx.replace(R.id.below_fragment,main_fragment).addToBackStack(null).commit();
        } if(list.get(list.size()-1) instanceof ListFragment) {
            super.onBackPressed();
            changeString("Home > ");
            main_fragment = new BlocksFragment();
            tx = manager.beginTransaction();
            tx.replace(R.id.below_fragment,main_fragment).addToBackStack(null).commit();
        } if(list.get(list.size()-1) instanceof SettingsFragment) {
            super.onBackPressed();
            changeString("Home > ");
            main_fragment = new BlocksFragment();
            tx = manager.beginTransaction();
            tx.replace(R.id.below_fragment,main_fragment).addToBackStack(null).commit();
        }/*else{

                super.onBackPressed();
                changeString("Home > ");
            }*/

    }

    @Override
    public void changeString(String string) {
        indexText.setText(string);
    }

    public void checkWhetherDataPresent(){

        loadDataFromDatabase();

        if(paperInformations.size()==0){
            readDataFromFile();
        }

    }

    public void readDataFromFile(){

        JsonFileReader reader = new JsonFileReader();
        paperInformations = reader.getPapers(context,"data.json");

        DatabaseHandler.getInstance(context).getAppDatabase()
                .getTestDao().insertPapers(paperInformations);
    }

    public void loadDataFromDatabase(){
        paperInformations.clear();
        paperInformations = DatabaseHandler.getInstance(context).getAppDatabase()
                .getTestDao().getAllPapers();

    }

    public void getCoinsStatus(){
        //DatabaseHandler.getInstance(context).getAppDatabase().getTestDao().deleteCoins();

        List<CoinsStack> coinsStacks = DatabaseHandler.getInstance(context).getAppDatabase()
                .getTestDao().getCoins();

        if(coinsStacks.size()==0){
            CoinsStack stack = new CoinsStack();
            stack.setCoins(0);
            DatabaseHandler.getInstance(context).getAppDatabase()
                    .getTestDao().insertCoin(stack);


        }

        totalCoins = DatabaseHandler.getInstance(context).getAppDatabase().getTestDao().getTotalCoins();
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.addCoins){
            final Dialog dialog = new Dialog(this);
            TextView cancelTextWhite,summary;
            Button watchVideoButton;
            dialog.setContentView(R.layout.coins_dialog);
            cancelTextWhite = dialog.findViewById(R.id.closeCoinsDialog);
            summary = dialog.findViewById(R.id.collectedCoins);
            watchVideoButton = dialog.findViewById(R.id.watchVideoButton);

            watchVideoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int status = getCounterStatus();
                    if(status <5){

                    if(rewardedVideoAd.isLoaded()){
                        dialog.cancel();
                        rewardedVideoAd.show();
                    }{
                        Toast.makeText(context,"Failed to load ad.",Toast.LENGTH_SHORT).show();
                    }}
                    else{
                        Toast.makeText(context,"You reach the max limit for today.",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            cancelTextWhite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });

            summary.setText(String.valueOf(totalCoins));

            dialog.show();
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {
        getloadVideoStatus();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadAwardedVideo();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        CoinsStack stack = new CoinsStack();
        int coins = DatabaseHandler.getInstance(context).getAppDatabase().getTestDao()
                .getTotalCoins();
        List<CoinsStack> coinsStacks = DatabaseHandler.getInstance(context).getAppDatabase()
                .getTestDao().getCoins();

        stack.setId(coinsStacks.get(0).getId());
        stack.setCoins(coins+100);
        DatabaseHandler.getInstance(context).getAppDatabase().getTestDao()
                .updateCoins(stack);
        totalCoins = DatabaseHandler.getInstance(context).getAppDatabase().getTestDao()
                .getTotalCoins();
        totalCoinsTextView.setText(String.valueOf(totalCoins));
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        //Toast.makeText(this,"Failed to load Video.",Toast.LENGTH_LONG).show();
        loadAwardedVideo();
    }

    @Override
    public void onRewardedVideoCompleted() {
        loadAwardedVideo();
    }
}
