package com.nanangrustianto.suara2019;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Menu3DetailActivity extends AppCompatActivity {
    String id, title, image, qty, lat, lng, laporan;
    ImageView img;
    TextView txtTitle, txtQty, txtLokasi, txtLaporan;
    Button btnDel, btnEdit;

    LinearLayout lineDetail, lineEdit;

    /*EDIT*/
    EditText edtName, edtQty;
    ImageView imgFrom;
    Button btnGallery, btnCamera, btnUpload;
    Bitmap yourSelectedImage;
    String imageName;
    MagicalCamera magicalCamera;
    int RESIZE_PHOTO_PIXELS_PERCENTAGE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu3_detail);
        magicalCamera = new MagicalCamera(this,RESIZE_PHOTO_PIXELS_PERCENTAGE);
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        qty = getIntent().getStringExtra("status");
        lat = getIntent().getStringExtra("lat");
        lng = getIntent().getStringExtra("lng");
        image = getIntent().getStringExtra("image");
        laporan = getIntent().getStringExtra("laporan");

        lineDetail = (LinearLayout)findViewById(R.id.lineDetail);
        img = (ImageView)findViewById(R.id.img);
        txtTitle = (TextView)findViewById(R.id.txtTitle);
        txtQty = (TextView)findViewById(R.id.txtQty);
        txtLokasi = (TextView)findViewById(R.id.txtLokasi);
        txtLaporan = (TextView) findViewById(R.id.txtLaporan);
        btnDel = (Button)findViewById(R.id.btnDelete);
        btnEdit = (Button)findViewById(R.id.btnEdit);

        btnDel.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);


        txtTitle.setText("Nama : "+title);
        txtLaporan.setText("Laporan : "+laporan);
        txtQty.setText("Status : "+qty);
        txtLokasi.setText("Latitude : "+lat+"\nLongitude : "+lng);


        /*fungsi ini untuk menampilkan gambar dari server*/
        Glide.with(getApplicationContext()).load(image)
                .thumbnail(0.5f)
                .override(500,500)
                .crossFade()
                .into(img);

        /*fungsi ini untuk mengklik button*/
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new delete().execute();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineDetail.setVisibility(View.GONE);
                lineEdit.setVisibility(View.VISIBLE);
            }
        });

        /*EDIT*/
        lineEdit = (LinearLayout)findViewById(R.id.lineEdit);
        edtName = (EditText)findViewById(R.id.edtName);
        edtQty = (EditText)findViewById(R.id.edtQty);
        imgFrom = (ImageView)findViewById(R.id.imgFrom);
        btnCamera = (Button)findViewById(R.id.btnCamera);
        btnGallery = (Button)findViewById(R.id.btnGallery);
        btnUpload = (Button)findViewById(R.id.btnUpload);

        edtName.setText(title);
        edtQty.setText(qty);

        Glide.with(getApplicationContext()).load(image)
                .thumbnail(0.5f)
                .override(500,500)
                .crossFade()
                .into(imgFrom);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                magicalCamera.takePhoto();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*fungsi ini untuk mengambil gambar dari gallery*/
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 100);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new update(edtName.getText().toString(), edtQty.getText().toString()).execute();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100){
            try {
                Uri selectedImage = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                //imgProfile.setImageBitmap(yourSelectedImage);
                Glide.with(getApplicationContext())
                        .load(selectedImage)
                        .override(400, 400)
                        .thumbnail(0.5f)
                        .into(imgFrom);
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(
                        selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String pathImage = cursor.getString(columnIndex);
                cursor.close();
                int temp = (pathImage.split("/").length)-1;
                imageName = pathImage.split("/")[temp];

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), "Image is Empty", Toast.LENGTH_LONG).show();
            }
        }else {
            magicalCamera.resultPhoto(requestCode, resultCode, data);
            yourSelectedImage = magicalCamera.getMyPhoto();
            String filePath = magicalCamera.savePhotoInMemoryDevice(magicalCamera.getMyPhoto(), "image", MagicalCamera.JPEG, true);
            int temp = (filePath.split("/").length)-1;
            imageName = filePath.split("/")[temp];

        }
    }
    /*fungsi ini untuk mengubah image(bitmap) menjadi Base64*/
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    /*fungsi ini untuk mengubah data dan di kirim ke server*/
    class update extends AsyncTask<Void, Void, String>{
        String upName, upQty;
        ProgressDialog dialog;
        public update(String upName, String upQty){
            this.upName = upName;
            this.upQty = upQty;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Menu3DetailActivity.this);
            dialog.setMessage("PLease wait....!!!");
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            try {
                HttpParams myParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(myParams, 5000);
                HttpConnectionParams.setSoTimeout(myParams, 5000);
                HttpClient client = new DefaultHttpClient(myParams);
                HttpPost post = new HttpPost("http://eaduan.terasdata.com/android/upload_image/edit.php");

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();

                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);


                builder.addTextBody("id", id);
                builder.addTextBody("image", getStringImage(yourSelectedImage));
                builder.addTextBody("image_name", imageName);
                builder.addTextBody("name", upName);
                builder.addTextBody("qty", upQty);
                builder.addTextBody("lat", lat);
                builder.addTextBody("long", lng);

                post.setEntity(builder.build());
                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();
                result = EntityUtils.toString(resEntity);
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
                if (jsonObject.getInt("success")==1){
                    Toast.makeText(Menu3DetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(Menu3DetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    /*fungsi ini untuk menghapus data di server*/
    class delete extends AsyncTask<Void, Void, String>{
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Menu3DetailActivity.this);
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
                DefaultHttpClient httpClient = new DefaultHttpClient(myParams);

                HttpPost httpPost = new HttpPost("http://eaduan.terasdata.com/android/upload_image/delete.php");

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();

                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                //Log.d("dataLog", "check 1");
                builder.addTextBody("id", id);
                //Log.d("dataLog", "check 2");
                httpPost.setEntity(builder.build());
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity resEntity = response.getEntity();
                result = EntityUtils.toString(resEntity);
            }  catch (IOException e) {
                e.printStackTrace();
            }
            //Log.d("dataLog", "check 3");
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d("dataLog", s);
            dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getInt("success")==1){
                    Toast.makeText(Menu3DetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(Menu3DetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
