package au.edu.sydney.comp5216.img_gallery;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
* ImageAdapter class for converting image item into view item
*
*/
public class ImageAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<String> images;

    MarshmallowPermission marshmallowPermission;

    /**
     * Instantiates a new image adapter
     * @param localContext activity
     */
    public ImageAdapter(Activity localContext){
        context = localContext;
        marshmallowPermission = new MarshmallowPermission(this.context);
        this.images = getAllShownImagesPath(context);

    }

    /**
     * Getter and Setters for Adapter
     * */
    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView picturesView;

        //Create new ImageView and set ScaleType
        if(convertView == null){
            picturesView = new ImageView(context);
            picturesView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        else{
            picturesView = (ImageView) convertView;
        }


        //Use Glide to load image from device and put into ImageView
        Glide.with(context).load(images.get(position))
                .placeholder(R.drawable.ic_launcher_background).centerCrop()
                .into(picturesView);

        return picturesView;
    }

    public ArrayList<String> getImages(){
        return images;
    }

    /**
     * Get arraylist of all images path
     *
     * @param activity
     * @return ArrayList<String> of all images path
     * */
    private ArrayList<String> getAllShownImagesPath(Activity activity){
        Log.d("getAllShownImagesPath","IS RANNNNN");
        Uri uri;
        Cursor cursor;
        int column_idx_data;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;

        //Check for permission for read media file
        if (!marshmallowPermission.checkPermissionForReadfiles()) {
            marshmallowPermission.requestPermissionForReadfiles();
        } else {
            //Get path to media storage
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            //project to primary bucket display name of this media item
            String[] projection = {
                    MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME
            };

            //Get query result and put into cursor
            cursor = activity.getContentResolver().query(uri, projection, null, null, null);

            //Set index of column
            column_idx_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

            //Loop through query result and add all image paths to arraylist
            while(cursor.moveToNext()){
                absolutePathOfImage = cursor.getString(column_idx_data);
                listOfAllImages.add(absolutePathOfImage);
            }

            return listOfAllImages;
        }
        return listOfAllImages;
    }
}
