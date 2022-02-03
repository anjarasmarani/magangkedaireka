package com.grocery.food.Activity;

import static android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.grocery.food.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class BuktiTf extends AppCompatActivity

    //implements View.OnClickListener
    {
//        private static final String UPLOAD_URL = "";
//        private static final int IMAGE_REQUEST_CODE = 3;
//        private static final int STORAGE_PERMISSION_CODE = 123;
//        private ImageView imageview;
//        private TextView etHarga;
//        private TextView tvPath;
//        private Button upload;
//        private Bitmap bitmap;
//        private Uri filepath;

        TextView t1;
        TextView t2;
        String a;
        String b;
        Button browse,upload;
        ImageView img;
        Bitmap bitmap;
        String encodeImageString;
        private static final String url="http://192.168.56.1/webfrsh/api/fileupload.php";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bukti_tf);

        Intent intent = getIntent();
        TextView harga = findViewById(R.id.harga);
        TextView oid = findViewById(R.id.oid);
        TextView uid = findViewById(R.id.uid);
        a =intent.getStringExtra("oid");
        b =intent.getStringExtra("uid");

            harga.setText("Total Transfer = "+ intent.getStringExtra("harga"));
            oid.setText("ORDER NUMBER : "+ a);
            uid.setText("ID USER : "+ b);

            img=(ImageView)findViewById(R.id.image);
            upload=(Button)findViewById(R.id.upload);
            browse=(Button)findViewById(R.id.kirim);

            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dexter.withActivity(BuktiTf.this)
                            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response)
                                {
                                    Intent intent=new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    startActivityForResult(Intent.createChooser(intent,"Browse Image"),1);
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {

                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }).check();
                }
            });

            browse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploaddatatodb();
                    Intent a = new Intent(BuktiTf.this,HomeActivity.class);
                    startActivity(a);
                }
            });

//        upload = findViewById(R.id.upload);
//        tvPath = findViewById(R.id.tvPath);
//        imageview = findViewById(R.id.image);
//        requestStoragePermission();
//
//        imageview.setOnClickListener(this);
//        upload.setOnClickListener(this);
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
        {
            if(requestCode==1 && resultCode==RESULT_OK)
            {
                Uri filepath=data.getData();
                try
                {
                    InputStream inputStream=getContentResolver().openInputStream(filepath);
                    bitmap= BitmapFactory.decodeStream(inputStream);
                    img.setImageBitmap(bitmap);
                    encodeBitmapImage(bitmap);
                }catch (Exception ex)
                {

                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

        private void encodeBitmapImage(Bitmap bitmap)
        {
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte[] bytesofimage=byteArrayOutputStream.toByteArray();
            encodeImageString=android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
        }

        private void uploaddatatodb()
        {
            t1= (TextView) findViewById(R.id.uid);
            t2=(TextView)findViewById(R.id.oid);
            final String uid=t1.getText().toString().trim();
            final String oid=t2.getText().toString().trim();

            StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    t1.setText("");
                    t2.setText("");
                    img.setImageResource(R.drawable.ic_photo_camera_black_48dp);
                    Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError
                {
                    Map<String,String> map=new HashMap<String, String>();
                    map.put("uid",a);
                    map.put("oid",b);
                    map.put("image",encodeImageString);
                    map.put("harga","1000");
                    return map;
                }
            };


            RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
            queue.add(request);
        }
//    private void requestStoragePermission() {
//            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//                return;
//            }
//
//
//    }
//
//    @Override
//    public void onClick(View view) {
//        if(view == imageview){
//            Intent intent = new Intent();
//            intent.setType("img/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(intent,"Complete action using"),IMAGE_REQUEST_CODE);
//        }
//        else if(view == upload){
//            uploadMultipart();
//        }
//    }
//
//    @Override
//    protected  void onActivityResult(int requestcode,int resultcode,Intent data) {
//        super.onActivityResult(requestcode, resultcode, data);
//        if (requestcode == IMAGE_REQUEST_CODE && resultcode == RESULT_OK && data != null && data.getData() != null) {
//            filepath = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
//                tvPath.setText("Path : ".concat(getPath(filepath)));
//                imageview.setImageBitmap(bitmap);
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private String getPath(Uri uri) {
//        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
//        cursor.moveToFirst();
//        String document_id = cursor.getString(0);
//        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
//        cursor.close();
//
//        cursor = getContentResolver().query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Images.Media._ID+" = ? ",new String[]{document_id},null);
//        cursor.moveToFirst();
//        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//        cursor.close();
//        return  path;
//    }
//
//    private void uploadMultipart() {
//    }
}
