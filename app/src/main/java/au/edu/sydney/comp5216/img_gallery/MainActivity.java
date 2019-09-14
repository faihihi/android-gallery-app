package au.edu.sydney.comp5216.img_gallery;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity of Image Gallery app
 */
public class MainActivity extends AppCompatActivity {
    //Set variables
    ImageAdapter imageAdapter;
    public static ArrayList<String> images = new ArrayList<String>();

    //Instantiate marshmallowpermission object
    MarshmallowPermission marshmallowPermission = new MarshmallowPermission(this);

    /**
     * Display grid view
     * @param savedInstanceState
     * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gallery = (GridView) findViewById(R.id.galleryGridView);

        //Create new adapter
        imageAdapter = new ImageAdapter(this);

        //Set adapter for gallery
        gallery.setAdapter(imageAdapter);

        //Get list of images path
        images = imageAdapter.getImages();

        /**
         * When an image on Gallery grid is clicked, start DisplayImage activity
         * intent with clicked image item is passed
         * @param arg0
         * @param arg1
         * @param arg3
         * @param position
         * */
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (null != images && !images.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, DisplayImage.class);
                    intent.putExtra("Image", images.get(position));
                    startActivity(intent);
                }
            }
        });

        /**
         * When click Take Photo button
         * Get permission for using camera and start Camera Activity
         * @param v view
         * */
        Button takePicture = (Button) findViewById(R.id.takePicture);
        takePicture.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Check for camera permission
                if (!marshmallowPermission.checkPermissionForCamera()) {
                    marshmallowPermission.requestPermissionForCamera();
                } else {
                    //Create intent and pass to CameraActivity
                    Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}