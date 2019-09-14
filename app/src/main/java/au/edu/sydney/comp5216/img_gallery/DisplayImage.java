package au.edu.sydney.comp5216.img_gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;


/**
 * Display Image Activity
 * */
public class DisplayImage extends AppCompatActivity {
    ImageView iv;
    String imgPath;
    Uri croppedUri;

    /**
     * Display image view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        iv = (ImageView) findViewById(R.id.imageView);
        Bundle extras = getIntent().getExtras();

        if(extras != null){
            imgPath = extras.getString("Image");
        }

        iv.setImageURI(Uri.parse(imgPath));
        iv.setTag(imgPath);


        /**
         * When click Crop
         * @param v view
         */
        Button cropBtn = (Button) findViewById(R.id.cropping);
        cropBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri imgUri = Uri.fromFile(new File(imgPath));

                //Start CropImage activity with original image
                if(iv.getTag() == imgPath){
                    CropImage.activity(imgUri)
                            .start(DisplayImage.this);
                }
                //Start CropImage activity with cropped image
                else{
                    CropImage.activity(croppedUri)
                            .start(DisplayImage.this);
                }

            }
        });
    }

    /**
     * Method for going back to Grid view gallery
     * @param v
     */
    public void displayMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        //Show toast when no change is made
        if(iv.getTag().toString() == imgPath){
            Toast.makeText(
                    getApplicationContext(),
                    "Saved Unchange",
                    Toast.LENGTH_SHORT).show();
        }
        //Save cropped image as a separate file and show toast that it has been saved
        else if(iv.getTag().toString() == croppedUri.toString()){
            File imgFile = new File(imgPath);

            if(imgFile != null){
                try{
                    //Convert byte data to bitmap and save image to storage
                    Bitmap croppedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), croppedUri);
                    MediaStore.Images.Media.insertImage(getContentResolver(), croppedBitmap, null , null);

                    //Show toast that image has been saved
                    Toast.makeText(
                            getApplicationContext(),
                            "Saved Cropped Image as a separate file",
                            Toast.LENGTH_SHORT).show();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Method for undoing changes
     * @param v view
     */
    public void undoCrop(View v){
        //Display original image
        iv.setImageURI(Uri.parse(imgPath));
        iv.setTag(imgPath);

        //Show toast
        Toast.makeText(
                getApplicationContext(),
                "Undo cropping",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * On activity result after CropImage Activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Get result after CropImage activity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            //Display cropped image on preview
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
