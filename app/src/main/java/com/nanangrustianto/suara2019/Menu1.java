package com.nanangrustianto.suara2019;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nanangrustianto.suara2019.MainActivity;
import com.nanangrustianto.suara2019.app.AppController;
import com.nanangrustianto.suara2019.app.DataObject.DataObject;
import com.nanangrustianto.suara2019.app.SpinnerAdapter;
import com.nanangrustianto.suara2019.helper.LocationHelper;

import android.content.SharedPreferences;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Spinner;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by nanangrustianto.com on 18/09/17.
 */


public class Menu1 extends Fragment {

    static final String TAG = "Menu1";

    //private Spinner spinner;
    private static final String PATH_TO_SERVER = AppConfig.SERVER_HOME_URL + "/upload_image/kecamatan.php";

    protected List<DataObject> spinnerData;
    private RequestQueue queue;

    private String UPLOAD_URL = "api/android/upload_image/upload.php";

    @BindView(R.id.editText)
    EditText txt_name;
    @BindView(R.id.editQty)
    EditText txt_qty;
    @BindView(R.id.editPreURL)
    EditText txt_preURL;
    //Button btn_choose_image;
    @BindView(R.id.btn_choose_image)
    Button btn_dari_galeri;
    @BindView(R.id.btn_camera)
    Button btnCamera;
    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.buttonUpload)
    FloatingActionButton btnUpload;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.txt_lat)
    TextView txtLat;
    @BindView(R.id.txt_lng)
    TextView txtLng;


    @BindView(R.id.editNIK)
    TextView txtNIK;
    @BindView(R.id.editHP)
    TextView txtHP;
    @BindView(R.id.editLaporan)
    TextView txtLaporan;


    @BindView(R.id.spinner)
    Spinner spinner;


    //String text = spinner.getSelectedItem().toString();
    private String PATH_TO_SERVER_DESA = AppConfig.SERVER_HOME_URL + "/upload_image/kabupaten.php?propinsi=";

    @BindView(R.id.spinnerDesa)
    Spinner spinnerDesa;



    int success;
    int PICK_IMAGE_REQUEST = 1;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    private String KEY_QTY = "qty";
    private String KEY_LAT = "lat";
    private String KEY_LNG = "long";
    private String KEY_NIK = "pemilik";
    private String KEY_HP = "hp";
    private String KEY_LAPORAN = "alamat";
    private String KEY_DESA = "kabupaten";
    private String KEY_KECAMATAN = "propinsi";
    static String tag_json_obj = "json_obj_req";

    Intent intent;
    Uri fileUri;

    Bitmap bitmap, decoded;
    public final int REQUEST_CAMERA = 0;
    public final int SELECT_FILE = 1;

    int bitmap_size = 40; // image quality 1 - 100;
    int max_resolution_image = 800;

    private LocationHelper locationHelper;
    private double lat;
    private double lng;
    ArrayList<String> arrayKec = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        View rootView = inflater.inflate(R.layout.fragment_menu1, container, false);


        ButterKnife.bind(this, rootView);

        txt_preURL.setText("http://suara.jogjaide.web.id/"); //

        // get location when fragment active
        useLocationHelper();





        queue = Volley.newRequestQueue(this.getActivity());
        //requestJsonObject();

        //Log.d("dataLog", getData(PATH_TO_SERVER));
        try {
            JSONArray jsonArray = new JSONArray(getData(PATH_TO_SERVER));
            for (int i = 0; i < jsonArray.length(); i++) {
                arrayKec.add(jsonArray.getJSONObject(i).getString("name"));
            }
            ArrayAdapter<String> adapterKec = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayKec);
            spinner.setAdapter(adapterKec);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinDesa(PATH_TO_SERVER_DESA+arrayKec.get(position).toString().replaceAll(" ", "%20"));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }


    private void requestJsonObject(){
        RequestQueue queue = Volley.newRequestQueue(this.getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PATH_TO_SERVER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                spinnerData = Arrays.asList(mGson.fromJson(response, DataObject[].class));
                //display first question to the user
                if(null != spinnerData){
                    //spinner = (Spinner) findViewById(R.id.spinner);
                    assert spinner != null;
                    spinner.setVisibility(View.VISIBLE);
                    SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity(), spinnerData);
                    spinner.setAdapter(spinnerAdapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            String text = spinner.getSelectedItem().toString();
                            PATH_TO_SERVER_DESA =  AppConfig.SERVER_HOME_URL + "/kabupaten.php?propinsi="+text;
                            requestJsonObjectDesa();
                        }

                        public void onNothingSelected(AdapterView<?> parent) {

                        }

                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);


    }

    private void requestJsonObjectDesa(){
        RequestQueue queue = Volley.newRequestQueue(this.getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PATH_TO_SERVER_DESA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                spinnerData = Arrays.asList(mGson.fromJson(response, DataObject[].class));
                //display first question to the user
                if(null != spinnerData){
                    //spinner = (Spinner) findViewById(R.id.spinner);
                    assert spinnerDesa != null;
                    spinnerDesa.setVisibility(View.VISIBLE);
                    SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity(), spinnerData);
                    spinnerDesa.setAdapter(spinnerAdapter);
                    spinnerAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    private String getData(String url) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
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
        return  result;
    }

    private void spinDesa(String url){
        ArrayList<String> arrayDesa = new ArrayList<>();
        Log.d("dataLogKab", "testing kab...");
        Log.d("dataLogKab", getData(url));
        try {
            JSONArray jsonArray = new JSONArray(getData(url));
            for (int i = 0; i < jsonArray.length(); i++) {
                arrayDesa.add(jsonArray.getJSONObject(i).getString("name"));
            }
            ArrayAdapter<String> adapterDesa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayDesa);
            spinnerDesa.setAdapter(adapterDesa);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @OnClick(R.id.btn_choose_image)
    public void openGalery(View view) {
        intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
    }

    @OnClick(R.id.btn_camera)
    public void takePicture(View view) {
        selectImage();
    }

    @OnClick(R.id.buttonUpload)
    public void doUpload(View view) {
        // exeute asynctask for upload image
        new UploadTask().execute();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles

        btn_dari_galeri.setVisibility(View.INVISIBLE);
        getActivity().setTitle("Suara-2019");
    }

    private void selectImage() {
        imageView.setImageResource(0);
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    fileUri = getOutputMediaFileUri();

                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult", "requestCode " + requestCode + ", resultCode " + resultCode);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                try {
                    Log.e("CAMERA", fileUri.getPath());

                    bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                    setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE && data != null && data.getData() != null) {
                try {
                    // mengambil gambar dari Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Untuk menampilkan bitmap pada ImageView
    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        imageView.setImageBitmap(decoded);

        // get location after load image success
        useLocationHelper();

    }

    // Untuk resize bitmap
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "NaNaNG");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("Monitoring", "Oops! Failed create Monitoring directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_DeKa_" + timeStamp + ".jpg");

        return mediaFile;
    }

    private void uploadImage() {


        //menampilkan progress dialog
        //final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, txt_preURL.getText() + UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.e("v Add", jObj.toString());

                                Toast.makeText(getContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                                // kosong();

                            } else {
                                Toast.makeText(getContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghilangkan progress dialog
                        // loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        //loading.dismiss();

                        //menampilkan toast
                        Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.getMessage().toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();



                //menambah parameter yang di kirim ke web servis
                params.put(KEY_IMAGE, getStringImage(decoded));
                params.put(KEY_NAME, txt_name.getText().toString().trim());
                params.put(KEY_QTY, txt_qty.getText().toString().trim());
                params.put(KEY_LAT, txtLat.getText().toString());
                params.put(KEY_LNG, txtLng.getText().toString());
                params.put(KEY_HP, txtHP.getText().toString());
                params.put(KEY_NIK, AppConfig.emailAktif );
                params.put(KEY_LAPORAN, txtLaporan.getText().toString());
                //untuk mengambil nilai dari teks dari spinner bagaimana
//                params.put(KEY_DESA, spinnerDesa.getSelectedItem().toString());
  //              params.put(KEY_KECAMATAN, spinner.getSelectedItem().toString());

                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };

        new AppController(getContext()).addToRequestQueue(stringRequest, tag_json_obj);
    }



    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private class UploadTask extends AsyncTask<Object, Integer, Long> {

        @Override
        protected Long doInBackground(Object[] params) {
            // upload image to webservice

            uploadImage();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // show progressbar
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);

            // hide progressbar
            progressBar.setVisibility(View.GONE);
        }
    }

    private void useLocationHelper() {
        locationHelper = new LocationHelper(getContext());
        if (locationHelper.isCanGetLocation()) {
            if (locationHelper != null) {
                lat = locationHelper.getLat();
                lng = locationHelper.getLng();

                txtLat.setText(String.valueOf(lat));
                txtLng.setText(String.valueOf(lng));
            }
        }
    }
}

