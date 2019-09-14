package au.edu.sydney.comp5216.img_gallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ImagePreviewActivity extends Activity {
    byte[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view);

        ImageView iv = (ImageView)findViewById(R.id.previewImage);
        Bundle extras = getIntent().getExtras();

        if(extras != null){
            data = extras.getByteArray("Image");
        }

        Bitmap takenImage = BitmapFactory.decodeByteArray(data, 0, data.length);

        // Load the taken image into a preview
        iv.setImageBitmap(takenImage);
        iv.setVisibility(View.VISIBLE);
    }


    public void displayMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
