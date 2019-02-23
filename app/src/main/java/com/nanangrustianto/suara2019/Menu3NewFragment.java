package com.nanangrustianto.suara2019;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Menu3NewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match


    public Menu3NewFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Menu3NewFragment newInstance() {
        Menu3NewFragment fragment = new Menu3NewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    ListView listMenu3;
    ListView mList;
    String url = "http://eaduan.terasdata.com/android/upload_image/getandroidosnames.php";
    ArrayList<HashMap<String, String>> arrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu3_new, container, false);
        listMenu3 = (ListView)view.findViewById(R.id.listMenu3);

       /* AdView mAdView =(AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);*/

        /*funsi ini untuk pindah kelas dan memindahkan data dari kelas A ke kelas B menurut user yang di klik*/
        listMenu3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), Menu3DetailActivity.class);
                intent.putExtra("id", arrayList.get(position).get("id"));
                intent.putExtra("title", arrayList.get(position).get("name"));
                intent.putExtra("qty", arrayList.get(position).get("qty"));
                intent.putExtra("lat", arrayList.get(position).get("lat"));
                intent.putExtra("lng", arrayList.get(position).get("lng"));
                intent.putExtra("image", arrayList.get(position).get("image"));
                intent.putExtra("status", arrayList.get(position).get("status"));
                intent.putExtra("laporan", arrayList.get(position).get("laporan"));
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        arrayList = new ArrayList<>();
        new sync().execute();
    }

    /*fungsi ini untuk load data dari server dan menampilkan nya ke listview melalui adapter*/
    class sync extends AsyncTask<Void, String, String>{
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("PLease wait....!!!");
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";

            try {
                HttpParams myParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(myParams, 15000);
                HttpConnectionParams.setSoTimeout(myParams, 15000);
                DefaultHttpClient httpClient= new DefaultHttpClient(myParams);

                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);

                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject objItem = jsonArray.getJSONObject(i);
                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put("id", objItem.getString("id"));
                    hashMap.put("name", objItem.getString("AndroidNames"));
                    hashMap.put("qty", objItem.getString("qty"));
                    hashMap.put("lat", objItem.getString("lat"));
                    hashMap.put("lng", objItem.getString("lng"));
                    hashMap.put("image", objItem.getString("ImagePath"));
                    hashMap.put("status", objItem.getString("status"));
                    hashMap.put("laporan", objItem.getString("laporan"));

                    arrayList.add(hashMap);
                }
                Menu3Adapter adapter = new Menu3Adapter(getActivity(), arrayList);
                listMenu3.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
