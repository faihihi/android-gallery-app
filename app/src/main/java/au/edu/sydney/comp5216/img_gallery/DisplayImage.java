package au.edu.sydney.comp5216.img_gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DisplayImage extends AppCompatActivity {
    ImageView iv;
    CropImageView cropImageView;
    String imgPath;
    Uri croppedUri;

    private static final int CROPPING = 1;

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


        Button cropBtn = (Button) findViewById(R.id.cropping);
        cropBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri imgUri = Uri.fromFile(new File(imgPath));

                if(iv.getTag() == imgPath){
                    //Start CropImage activity
                    CropImage.activity(imgUri)
                            .start(DisplayImage.this);
                }
                else{
                    //Start CropImage activity
                    CropImage.activity(croppedUri)
                            .start(DisplayImage.this);
                }

            }
        });
    }

    public void displayMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        if(iv.getTag().toString() == imgPath){
            Toast.makeText(
                    getApplicationContext(),
                    "Saved Unchange",
                    Toast.LENGTH_SHORT).show();
        }
        else if(iv.getTag().toString() == croppedUri.toString()){
            File imgFile = new File(imgPath);

            if(imgFile != null){
                Log.d("NOT NULL", imgFile.getAbsolutePath());
                try{
                    InputStream iStream =   getContentResolver().openInputStream(croppedUri);
                    byte[] croppedData = getBytes(iStream);

//                    FileOutputStream fos = new FileOutputStream(imgFile);
//                    fos.write(croppedData);
//                    fos.close();

                    Bitmap croppedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), croppedUri);

                    MediaStore.Images.Media.insertImage(getContentResolver(), croppedBitmap, null , null);


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

    public void undoCrop(View v){
        iv.setImageURI(Uri.parse(imgPath));
        iv.setTag(imgPath);
        Toast.makeText(
                getApplicationContext(),
                "Undo cropping",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                croppedUri = result.getUri();
                iv.setImageURI(croppedUri);
                iv.setTag(croppedUri.toString());

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }




}
