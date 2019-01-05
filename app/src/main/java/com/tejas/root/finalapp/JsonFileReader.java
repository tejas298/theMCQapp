package com.tejas.root.finalapp;

import android.content.Context;
import android.util.Log;

import com.tejas.root.finalapp.entities.PaperInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 24/12/18.
 */

public class JsonFileReader {

    List<JsonResponse> responseList;

    List<PaperInformation> paperList;

    JsonFileReader(){
        responseList = new ArrayList<>();
        paperList = new ArrayList<>();
    }

    public List<JsonResponse> doProcessing(Context context,String fileName){

        try {/*
            String fileName = String.valueOf(context.getResources().getIdentifier("java","raw",context.getPackageName()));

            System.out.print(fileName);
*/
            String json = null;

            InputStream is = context.getAssets().open(fileName);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


            //FileReader reader = new FileReader(fileName);

            //JsonReader jsonReader = new JsonReader(reader);

            JSONObject object = new JSONObject(json);

            JSONArray array = object.getJSONArray("questions");

            for (int i=0;i<array.length();i++){
                JSONObject question = array.getJSONObject(i);

                JsonResponse response = new JsonResponse();
                JsonResponse.Options options = response.new Options();
                response.setId(question.getInt("id"));
                response.setQuestion(question.getString("ques"));

                JSONObject answer = question.getJSONObject("options");
                options.setA(answer.getString("a"));
                options.setB(answer.getString("b"));
                options.setC(answer.getString("c"));
                options.setD(answer.getString("d"));

                response.setOptions(options);
                response.setAns(question.getString("ans"));

                responseList.add(response);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseList;
    }


    public List<PaperInformation> getPapers(Context context,String fileName){

        try {/*
            String fileName = String.valueOf(context.getResources().getIdentifier("java","raw",context.getPackageName()));

            System.out.print(fileName);
*/
            String json = null;

            InputStream is = context.getAssets().open(fileName);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


            //FileReader reader = new FileReader(fileName);

            //JsonReader jsonReader = new JsonReader(reader);

            JSONObject object = new JSONObject(json);

            JSONArray array = object.getJSONArray("list");

            for (int i=0;i<array.length();i++){
                PaperInformation information = new PaperInformation();
                JSONObject question = array.getJSONObject(i);

                information.setFilename(question.getString("filename"));
                information.setId(Integer.parseInt(question.getString("id")));
                information.setLock(question.getString("lock"));
                information.setOrder(Integer.parseInt(question.getString("order")));
                information.setPrice(Integer.parseInt(question.getString("price")));
                information.setTest_name(question.getString("name"));
                information.setType(question.getString("type"));
                information.setVisited(question.getString("visited"));

                paperList.add(information);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(paperList.size()>1){
            Log.d("tejas","Data found");
        }

        return paperList;
    }
}
