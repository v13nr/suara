package com.nanangrustianto.suara2019;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;



/**
 * Created by nanangrustianto.com on 18/09/17.
 */


public class Menu3 extends Fragment {

    private static final String newurl = "http://192.168.0.101/android/android/upload_image/getandroidosnames.php";

    private String json;
    private JSONArray urls;
    public  Getjson getjsonobj;
    Customadapter customadapter;

    @BindView(R.id.lst)
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        //listView = (ListView) findViewById(R.id.lst);

        return inflater.inflate(R.layout.layout, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        //getActivity().setTitle("Menu 3");
        getURLs();
    }

    //Get FoodTYpe
    private void getImages() {
        class GetImages extends AsyncTask<Void, Void, Void> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Loading Menu", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);
                loading.dismiss();


                customadapter = new Customadapter(getActivity(), getjsonobj.Android_Name ,getjsonobj.bitmaps );

                listView.setAdapter(customadapter);


            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    getjsonobj.getAllImages();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        GetImages getImages = new GetImages();
        getImages.execute();
    }

    private void getURLs() {

        class GetURLs extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Loading...", "Please Wait...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                getjsonobj = new Getjson(s);
                getImages();
            }

            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader = null;
                try {

                    URL url = new URL(strings[0]);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;

                }
            }
        }
        GetURLs gu = new GetURLs();


        gu.execute(newurl);


    }
}