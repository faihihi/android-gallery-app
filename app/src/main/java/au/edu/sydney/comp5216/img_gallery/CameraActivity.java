package au.edu.sydney.comp5216.img_gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.ByteArrayOutputStream;

/**
 * This activity start when camera view is open
 */
public class CameraActivity extends AppCompatActivity {
    // Declare variables
    private Camera camera;
    private ShowCamera showCamera;
    private FrameLayout frameLayout;

    private Context myContext;

    /**
     * Open camera and add show camera view to FrameLayout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_view);
        frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        myContext = this;

        camera = Camera.open();
        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);


        /**
         * Attach onClick listener to Gallery button, when click, start MainActivity
         * @param v view
         * */
        Button gallerybutton = (Button) findViewById(R.id.gallerybutton);
        gallerybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CameraActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
            }
        });

        /**
         * When click capture, callback mPicture
         * @param v this view
         */
        Button capturebutton = (Button) findViewById(R.id.capturebutton);
        capturebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                camera.takePicture(null, null, mPicture);
            }
        });
    }

    /**
     * Callback function for saving captured image and start Preview activity
     * @param data captured image data in byte array
     * @param camera
     */
    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // Convert byte array of image to Bitmap and rotate image
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            bitmap = rotateImage(90, bitmap);
            // Store image bitmap to device storage
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null , null);

            // Convert rotated image back to byte array for passing byte array intent
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            data = stream.toByteArray();

            // Start new intent with image data and start ImagePreview activity
            Intent i = new Intent(CameraActivity.this, ImagePreviewActivity.class);
            i.putExtra("Image", data);
            startActivity(i);
        }
    };

    /**
     * Method for rotating image
     * @param angle rotating angle
     * @param bitmapSrc image source in bitmap
     * @return rotated image bitmap
     */
    public Bitmap rotateImage(int angle, Bitmap bitmapSrc) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmapSrc, 0, 0,
                bitmapSrc.getWidth(), bitmapSrc.getHeight(), matrix, true);
    }

}
