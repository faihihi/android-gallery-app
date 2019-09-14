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

public class CameraActivity extends AppCompatActivity {
    private Camera camera;
    private ShowCamera showCamera;
    private FrameLayout frameLayout;

    private Context myContext;

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
         * Attach onClick listener to Gallery button, when click, move back to MainActivity
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

        Button capturebutton = (Button) findViewById(R.id.capturebutton);
        capturebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                camera.takePicture(null, null, mPicture);

            }
        });
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //Convert byte array to Bitmap, rotate image, and store to device
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            bitmap = rotateImage(90, bitmap);
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null , null);

            //Convert back to byte array for passing byte array intent
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            data = stream.toByteArray();

            Intent i = new Intent(CameraActivity.this, ImagePreviewActivity.class);
            i.putExtra("Image", data);
            startActivity(i);

        }
    };

    public Bitmap rotateImage(int angle, Bitmap bitmapSrc) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmapSrc, 0, 0,
                bitmapSrc.getWidth(), bitmapSrc.getHeight(), matrix, true);
    }

}
