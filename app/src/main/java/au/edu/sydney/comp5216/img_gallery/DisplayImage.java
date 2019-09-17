package au.edu.sydney.comp5216.img_gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;


/**
 * This activity will be started when an image item
 * on the gallery is clicked
 */
public class DisplayImage extends AppCompatActivity {
    // Declare variables
    ImageView iv;
    String imgPath;
    Uri croppedUri;

    /**
     * Display image from the image URI intent passed from previous activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        iv = (ImageView) findViewById(R.id.imageView);

        // Get image URI string from passed intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imgPath = extras.getString("Image");
        }

        // Set view and tag
        iv.setImageURI(Uri.parse(imgPath));
        iv.setTag(imgPath);


        /**
         * When click Crop, start new activity CropImage
         * CropImage activity is from android-image-cropper API
         * @param v this view
         */
        Button cropBtn = (Button) findViewById(R.id.cropping);
        cropBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Convert image path to URI
                Uri imgUri = Uri.fromFile(new File(imgPath));

                // Start CropImage activity with original image
                if (iv.getTag() == imgPath) {
                    CropImage.activity(imgUri)
                            .start(DisplayImage.this);
                } else { // Start CropImage activity with cropped image
                    CropImage.activity(croppedUri)
                            .start(DisplayImage.this);
                }
            }
        });
    }

    /**
     * Method for going back to Grid view gallery by starting MainActivity
     * @param v this view
     */
    public void displayMain(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        if (iv.getTag().toString() == imgPath) { //If no change has been made to the image
            // Show toast when no change is made
            Toast.makeText(
                    getApplicationContext(),
                    "Saved Unchange",
                    Toast.LENGTH_SHORT).show();
        } else if (iv.getTag().toString() == croppedUri.toString()) { //If the image has been cropped
            File imgFile = new File(imgPath);

            if (imgFile != null) {
                try {
                    // Convert byte data of image to bitmap and save image to storage
                    Bitmap croppedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), croppedUri);
                    MediaStore.Images.Media.insertImage(getContentResolver(), croppedBitmap, null, null);

                    // Show toast that image has been saved
                    Toast.makeText(
                            getApplicationContext(),
                            "Saved Cropped Image as a separate file",
                            Toast.LENGTH_SHORT).show();
                } catch (IOException e) { // Handle exception
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Method for undoing changes
     * @param v this view
     */
    public void undoCrop(View v) {
        // Display original image and set tag
        iv.setImageURI(Uri.parse(imgPath));
        iv.setTag(imgPath);

        // Show toast
        Toast.makeText(
                getApplicationContext(),
                "Undo cropping",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Method for getting result from CropImage Activity
     * @param requestCode
     * @param resultCode
     * @param data result of activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Get result after CropImage activity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            // Display cropped image on preview
            if (resultCode == RESULT_OK) {
                croppedUri = result.getUri();
                iv.setImageURI(croppedUri);
                iv.setTag(croppedUri.toString());

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
