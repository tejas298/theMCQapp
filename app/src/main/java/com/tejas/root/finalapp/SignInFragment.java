package com.tejas.root.finalapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 25/12/18.
 */

public class SignInFragment extends Fragment implements View.OnClickListener{

    GoogleSignInClient client;
    CallbackManager callbackManager;
    AccessTokenTracker tracker;
    SignInButton signInButton;
    LoginButton fbloginButton;
    EditText name,email,phone;
    CircleImageView photo;
    Button save,googleLogout;
    private static final int RC_SIGN_IN = 9001;

    private static final String EMAIL = "email";
    private static final String profileImg = "public_profile";
    Context context;
    MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_in_page,container,false);

        //Google Code
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(view.getContext(),gso);

        //Facebook Code
        callbackManager = CallbackManager.Factory.create();




        fbloginButton = (LoginButton) view.findViewById(R.id.fb_login_button);
        fbloginButton.setReadPermissions(Arrays.asList(EMAIL,profileImg));
        // If you are using in a fragment, call loginButton.setFragment(this);
        fbloginButton.setFragment(this);

        // Callback registration

        //View initialization

        signInButton = view.findViewById(R.id.google_sign_in_button);
        googleLogout = view.findViewById(R.id.google_logout);

        name = view.findViewById(R.id.name_google);
        email = view.findViewById(R.id.email_google);
        save = view.findViewById(R.id.logout_button);
        phone = view.findViewById(R.id.phone);
        photo = view.findViewById(R.id.photo);
        fbloginButton = view.findViewById(R.id.fb_login_button);

        if(isSharedPreferenceNUll()){
            hideContent();
        }else if(isDataFromGoogle()) {
            signInButton.setVisibility(View.GONE);
            googleLogout.setVisibility(View.VISIBLE);
            fbloginButton.setVisibility(View.GONE);
            setValuesForTextViews(getUserInformation());
        }else if(isDataFromFb()){
            signInButton.setVisibility(View.GONE);
            googleLogout.setVisibility(View.GONE);
            setValuesForTextViews(getUserInformation());
        }
        else{
                showContent();
                setValuesForTextViews(getUserInformation());
                googleLogout.setVisibility(View.GONE);
            }


        signInButton.setOnClickListener(this);
        googleLogout.setOnClickListener(this);
        fbloginButton.setOnClickListener(this);
        save.setOnClickListener(this);

        return view;
    }

    private boolean isDataFromFb() {
        Map<String,String> map = getUserInformation();
        String mode = map.get("mode");
        return mode != null && mode.equals("facebook");
    }

    private boolean isDataFromGoogle() {
        Map<String, String> map = getUserInformation();
        String mode = map.get("mode");
        return mode != null && mode.equals("google");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.google_sign_in_button:
                signIn();
                break;
            case R.id.logout_button:
                saveFromInputs();
                break;
            case R.id.fb_login_button:
                fbCallBackRegisteration();
                break;
            case R.id.google_logout:
                signOut();
                break;
        }
    }

    public Map<String,String> getUserInformation(){
        SharedPreferences pref = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        Map<String,String> map = new HashMap<>();
        map.put("name",pref.getString("name",null));
        map.put("email",pref.getString("email",null));
        map.put("img",pref.getString("img",null));
        map.put("id",pref.getString("id",null));
        map.put("phone",pref.getString("phone",null));
        map.put("mode",pref.getString("mode",null));
        return map;
    }

    public void saveFromInputs(){
        String temp_name = name.getText().toString();
        String temp_mail = email.getText().toString();
        String temp_phone = phone.getText().toString();

        if(temp_name.equals("")&&
                temp_mail.equals("") &&
                temp_phone.equals("")){

        }/*else if(temp_name.equals("")||
                temp_mail.equals("") ||
                temp_phone.equals("")){
           */ Map<String,String> map = getUserInformation();
            String temp_id = map.get("id");
            String temp_img = map.get("img");
            String temp_mode = map.get("mode");
            storeInSharedPreference(new String[]{temp_name,temp_mail,temp_img,temp_id,temp_phone,temp_mode});
        //}

    }

    private void signIn() {
        Intent signInIntent = client.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        client.signOut()
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        removeDataFromSharedPref();
                        setValuesForTextViews(getUserInformation());
                        googleLogout.setVisibility(View.GONE);
                        signInButton.setVisibility(View.VISIBLE);
                        fbloginButton.setVisibility(View.VISIBLE);
                    }
                });
    }

    public void updateUI(GoogleSignInAccount account){

        storeInSharedPreference(new String[]{
                account.getDisplayName(),account.getEmail(),account.getPhotoUrl().toString(),
                account.getId(),"","google"
        });
        signInButton.setVisibility(View.GONE);
        googleLogout.setVisibility(View.VISIBLE);
        fbloginButton.setVisibility(View.GONE);
        showContent();
        setValuesForTextViews(getUserInformation());

    }

    //Check whether data is inserted manually
    public boolean isInsertedManually(){
        Map<String,String> data = getUserInformation();

        return data.get("name") != null;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context= context;
        activity = (MainActivity) this.context;
        tracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    checkFBStatus();
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tracker.stopTracking();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity.changeString("Home > Sign In");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("tejas", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    public void storeInSharedPreference(String[] strings){
        SharedPreferences pref = context.getSharedPreferences("login", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.putString("name",strings[0]);
        editor.putString("email",strings[1]);
        editor.putString("img",strings[2]);
        editor.putString("id",strings[3]);
        editor.putString("phone",strings[4]);
        editor.putString("mode",strings[5]);
        editor.apply();
        editor.commit();
    }

    public boolean isSharedPreferenceNUll(){
        Map<String,String> map = getUserInformation();

        return (map.get("name") == null) || (map.get("name").equals(""));
    }

    public void hideContent(){
/*
        name.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        save.setVisibility(View.GONE);
        photo.setVisibility(View.GONE);
        */
        googleLogout.setVisibility(View.GONE);

        //signInButton.setVisibility(View.VISIBLE);
        //fbloginButton.setVisibility(View.VISIBLE);
    }

    public void showContent(){
/*
        name.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        save.setVisibility(View.VISIBLE);
        photo.setVisibility(View.VISIBLE);

        signInButton.setVisibility(View.GONE);
        fbloginButton.setVisibility(View.GONE);*/
    }

    public void setValuesForTextViews(Map<String,String> map){
        name.setText(map.get("name"));
        email.setText(map.get("email"));
        phone.setText(map.get("phone"));
        String temp_mobile=null,temp_name=null,temp_email=null,temp_img=null;
        boolean flag_mobile = ((temp_mobile = map.get("phone"))==null);
        boolean flag_name = ((temp_name = map.get("name"))==null);
        boolean flag_email = ((temp_email = map.get("email"))==null);
        boolean flag_img = ((temp_img = map.get("img"))==null);

        if(!flag_name){name.setText(temp_name);}else{name.setHint("Name");}

        if(!flag_email){email.setText(temp_email);}else{email.setHint("Email Id");}

        if(!flag_mobile){phone.setText(temp_mobile);}else{phone.setHint("Mobile Number");}

        if(!flag_img){Glide.with(this).load(temp_img).into(photo);
        }else{Glide.with(this).load(R.drawable.profile_default).into(photo);}

    }

    public void removeDataFromSharedPref(){
        SharedPreferences pref = context.getSharedPreferences("login", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.putString("name",null);
        editor.putString("email",null);
        editor.putString("img",null);
        editor.putString("id",null);
        editor.putString("phone",null);
        editor.putString("mode",null);
        editor.commit();

        hideContent();
    }


    public void fbCallBackRegisteration(){
        fbloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                if(object!=null){


                                try
                                {
                                    System.out.println(object.toString());
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    //String email = object.getString("email")==null?"Not found":object.getString("email");
                                    //String gender = object.getString("gender");
                                    //String birthday = object.getString("birthday");
                                    String picture = object.getJSONObject("picture").getJSONObject("data").getString("url")==null?"Image not found":object.getJSONObject("picture").getJSONObject("data").getString("url");
                                    System.out.println(id + ", " + name + ", " + "No mail" + ", "+picture);

                                    storeInSharedPreference(new String[]{name,"No mail",picture,id,"","facebook"});
                                    signInButton.setVisibility(View.GONE);
                                    googleLogout.setVisibility(View.GONE);
                                    showContent();
                                    setValuesForTextViews(getUserInformation());
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                                }
                            }
                        }
                );

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


    }



    public void checkFBStatus(){
            signInButton.setVisibility(View.VISIBLE);
            removeDataFromSharedPref();
            setValuesForTextViews(getUserInformation());
    }
}
