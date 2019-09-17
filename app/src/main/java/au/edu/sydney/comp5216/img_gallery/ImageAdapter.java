package au.edu.sydney.comp5216.img_gallery;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
* ImageAdapter class for converting image item into view item
*/
public class ImageAdapter extends BaseAdapter {
    // Declare variables
    private Activity context;
    private ArrayList<String> images;

    MarshmallowPermission marshmallowPermission;

    /**
     * Instantiates a new image adapter
     * @param localContext
     */
    public ImageAdapter(Activity localContext){
        // Set context and marshmallowpermission
        context = localContext;
        marshmallowPermission = new MarshmallowPermission(this.context);
        // Get all images path in device storage
        this.images = getAllShownImagesPath(context);
    }

    /**
     * Methods for Adapter: get size of images
     * @return length of images list
     * */
    @Override
    public int getCount() {
        return images.size();
    }

    /**
     * Methods for Adapter: get item by position
     * @return item object
     * */
    @Override
    public Object getItem(int position) {
        return position;
    }

    /**
     * Methods for Adapter: get item ID
     * @return item ID number
     * */
    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * Methods for Adapter: get gallery grid view using Glide
     * @param position
     * @param convertView
     * @param parent
     * @return picturesView of gallery grid
     * */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView picturesView;

        // Create new ImageView and set ScaleType
        if(convertView == null){
            picturesView = new ImageView(context);
            picturesView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else{
            picturesView = (ImageView) convertView;
        }

        // Use Glide to load image from device and put into ImageView
        Glide.with(context).load(images.get(position))
                .placeholder(R.drawable.ic_launcher_background).centerCrop()
                .into(picturesView);

        return picturesView;
    }

    /**
     * Get images list
     * @return list of images' path
     * */
    public ArrayList<String> getImages(){
        return images;
    }

    /**
     * Get arraylist of all images path currently stored in device
     * @param activity
     * @return listOfAllImages all images' path
     * */
    private ArrayList<String> getAllShownImagesPath(Activity activity){
        Uri uri;
        Cursor cursor;
        int column_idx_data;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;

        // Check if permission for reading media file is given
        if (!marshmallowPermission.checkPermissionForReadfiles()) {
            // Request for permission if not given
            marshmallowPermission.requestPermissionForReadfiles();
        } else {
            // Get path to external media storage
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            // Project to primary bucket display name of this media item
            String[] projection = {
                    MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME
            };

            // Get query result and put into cursor
            cursor = activity.getContentResolver().query(uri, projection, null, null, null);

            // Set index of column
            column_idx_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

            // Loop through query result and add all image paths to arraylist
            while(cursor.moveToNext()){
                absolutePathOfImage = cursor.getString(column_idx_data);
                listOfAllImages.add(absolutePathOfImage);
            }
            return listOfAllImages;
        }
        return listOfAllImages;
    }
}
