package au.edu.sydney.comp5216.img_gallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * This activity start after the an imaged is captured and saved
 * to preview the image
 * */
public class ImagePreviewActivity extends Activity {
    // Declare variable
    byte[] data;

    /**
     * Display Image Preview
     * @param savedInstanceState
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view);
        ImageView iv = (ImageView)findViewById(R.id.previewImage);

        // Get image data from previous activity
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            data = extras.getByteArray("Image");
        }

        // Convert bytes array to Bitmap
        Bitmap takenImage = BitmapFactory.decodeByteArray(data, 0, data.length);

        // Load the taken image into a preview
        iv.setImageBitmap(takenImage);
        iv.setVisibility(View.VISIBLE);
    }


    /**
     * Method for going back to Grid View by starting MainActivity
     * @param v this view
     * */
    public void displayMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
