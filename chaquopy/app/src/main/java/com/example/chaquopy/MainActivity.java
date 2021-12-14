package com.example.chaquopy;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";

    Button button;

    ImageView image_face;

    BitmapDrawable drawable;
    Bitmap bitmap;

    String imageString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textView = (TextView) findViewById(R.id.textview);

        button = (Button) findViewById(R.id.submit);
        /*imageViewColor = (ImageView) findViewById(R.id.image_view_color);
        imageViewGrey = (ImageView) findViewById(R.id.image_view_grey);*/

        image_face = (ImageView) findViewById(R.id.image_face);

        if (!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }

        final Python py = Python.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on click over button ..get image from image view

                drawable = (BitmapDrawable) image_face.getDrawable();
                Log.d(TAG, "traaaal");


                bitmap = drawable.getBitmap();


                imageString = getStringImage(bitmap);

                Log.d(TAG, imageString);

                PyObject pyObject = py.getModule("myscript");

                PyObject obj = pyObject.callAttr("main", imageString);

                String str = obj.toString();

                byte data[] = android.util.Base64.decode(str, Base64.DEFAULT);

                //convert to bitmap

                Bitmap conv_bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                //set this bitmap to image_view_gray

                image_face.setImageBitmap(conv_bitmap);
            }
        });

        /*Python py = Python.getInstance();

        PyObject pyobj = py.getModule("myscript");

        PyObject obj = pyobj.callAttr("main");

        textView.setText(obj.toString());*/
    }

    private String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        //store in byte array
        byte[] imageBytes = baos.toByteArray();

        //finally encode to string
        String encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }
}