package com.tejas.root.finalapp;

import android.content.Context;
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
import android.widget.Toast;

import com.tejas.root.finalapp.entities.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 28/12/18.
 */

public class PHPFragment extends Fragment implements View.OnClickListener{

    Button add,remove;
    Context context;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.php_fragment,container,false);

        add = view.findViewById(R.id.addUser);
        remove = view.findViewById(R.id.removeUser);

        add.setOnClickListener(this);
        remove.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        FirebaseApp.initializeApp(this.context);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addUser:

                //String id = myRef.push().getKey();
                //storeInSharedPreference(id);
                User user = new User();
                Map<String,String> map = getUserInformation();
                String id = map.get("firebaseId");
                user.setId(map.get("id"));
                user.setEmail("123");
                user.setName(map.get("name"));
                user.setPack(map.get("pack"));
                if(id!=null)
                myRef.child(id).setValue(user);

                break;
            case R.id.removeUser:


                myRef.child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String,String> map = getUserInformation();
                        String id = map.get("firebaseId");
                        Log.d("tejas",id);
                        User user1 = dataSnapshot.child("users").child(id).getValue(User.class);
                        Toast.makeText(context,user1.getEmail(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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
        map.put("firebaseId",pref.getString("firebaseId",null));
        return map;
    }
    public void storeInSharedPreference(String firebaseId){
        SharedPreferences pref = context.getSharedPreferences("login", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("firebaseId",firebaseId);
        editor.commit();
    }
}
