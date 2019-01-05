package com.tejas.root.finalapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tejas.root.finalapp.databases.DatabaseHandler;

/**
 * Created by root on 26/12/18.
 */

public class SettingsFragment extends Fragment implements View.OnClickListener{

    LinearLayout help,faq,contact,appInfo,email,notify1,mainNotify,sendFeedback,versionId
            ,synchronize,reset;
    Switch aSwitch;
    boolean closed = true;
    boolean isContactHide = true;
    boolean isNotifyHide = true;
    boolean isAppinfoHide = true;
    Context context;
    MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment,container,false);

        help = view.findViewById(R.id.helpLayout);
        faq = view.findViewById(R.id.faqLayout);
        contact = view.findViewById(R.id.contactUsLayout);
        appInfo = view.findViewById(R.id.appInfoLayout);
        aSwitch = view.findViewById(R.id.notification_flag);
        email = view.findViewById(R.id.emailLayout);
        notify1 = view.findViewById(R.id.notify1);
        mainNotify = view.findViewById(R.id.main_notify);
        sendFeedback = view.findViewById(R.id.sendFeedbackLayout);
        versionId = view.findViewById(R.id.versionNo);
        synchronize = view.findViewById(R.id.synchronizeData);
        reset = view.findViewById(R.id.resetData);

        help.setOnClickListener(this);
        faq.setOnClickListener(this);
        contact.setOnClickListener(this);
        appInfo.setOnClickListener(this);
        mainNotify.setOnClickListener(this);
        sendFeedback.setOnClickListener(this);
        synchronize.setOnClickListener(this);
        reset.setOnClickListener(this);


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changePreference(b);
            }
        });

        setaSwitch();

        hideHelp();
        hideContact();
        hideNotify();
        hideAboutUs();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        activity = (MainActivity) this.context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity.changeString("Home > Settings");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.helpLayout:
                hideHelp();
                break;
            case R.id.main_notify:
                hideNotify();
                break;
            case R.id.contactUsLayout:
                hideContact();
                break;
            case R.id.faqLayout:

                break;
            case R.id.appInfoLayout:
                hideAboutUs();
                break;
            case R.id.synchronizeData:
                Toast.makeText(context,"Data successfully syncronized.",Toast.LENGTH_LONG).show();
                break;
            case R.id.resetData:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage("This will delete all progress ?")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseHandler.getInstance(context).getAppDatabase().getTestDao()
                                        .deleteCoins();
                                DatabaseHandler.getInstance(context).getAppDatabase().getTestDao()
                                        .deleteAllRecords();
                                deleteStatusPreference();
                                Toast.makeText(context,"Data deleted.",Toast.LENGTH_LONG).show();
                                MainActivity activity = (MainActivity) context;
                                activity.afterDeletionDataRecovery();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.sendFeedbackLayout:
                final Dialog dialog = new Dialog(context);
                final EditText email,response;
                TextView cancel;
                Button send;
                dialog.setContentView(R.layout.send_feedback);

                email = dialog.findViewById(R.id.senderEmail);
                response = dialog.findViewById(R.id.response);
                send = dialog.findViewById(R.id.sendButton);
                cancel = dialog.findViewById(R.id.cancelFeedback);



                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if((email.getText().toString().equals(""))&&(response.getText().toString().equals("")))
                        {
                            Toast.makeText(context,"Enter details..",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(context,"Thanks for the response.",Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                dialog.show();

                break;
        }
    }

    public void hideHelp(){
        if(closed) {
            faq.setVisibility(View.GONE);
            contact.setVisibility(View.GONE);
            appInfo.setVisibility(View.GONE);
            closed = false;
        }else{
            faq.setVisibility(View.VISIBLE);
            contact.setVisibility(View.VISIBLE);
            appInfo.setVisibility(View.VISIBLE);
            closed = true;
        }
    }

    public void hideContact(){
        if(isContactHide){
            email.setVisibility(View.GONE);
            isContactHide = false;
        }else {
            email.setVisibility(View.VISIBLE);
            isContactHide = true;
        }
    }

    public void hideAboutUs(){
        if(isAppinfoHide){
            versionId.setVisibility(View.GONE);
            isAppinfoHide = false;
        }else {
            versionId.setVisibility(View.VISIBLE);
            isAppinfoHide = true;
        }
    }

    public void hideNotify(){
        if(isNotifyHide){
            notify1.setVisibility(View.GONE);
            isNotifyHide = false;
        }else {
            notify1.setVisibility(View.VISIBLE);
            isNotifyHide = true;
        }
    }

    public void setaSwitch(){
        SharedPreferences pref = context.getSharedPreferences("settings",Context.MODE_PRIVATE);

        String rece = pref.getString("receive",null);

        if(rece==null){
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("receive","Y");
            editor.apply();
            editor.commit();
            rece = "Y";
        }

        if(rece.contains("Y")){
            aSwitch.setChecked(true);
        }else if(rece.contains("N")){
            aSwitch.setChecked(false);
        }


    }

    public void changePreference(boolean flag){
        SharedPreferences pref = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("receive",flag?"Y":"N");
        editor.apply();
        editor.commit();
    }

    public void deleteStatusPreference(){
        SharedPreferences pref = context.getSharedPreferences("loadedvideo", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("date",null);
        editor.putString("count",null);
        editor.apply();
        editor.commit();

    }

}
