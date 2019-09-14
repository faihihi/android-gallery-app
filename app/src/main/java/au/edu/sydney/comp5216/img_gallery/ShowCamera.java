package au.edu.sydney.comp5216.img_gallery;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * ShowCamera class using SurfaceHolder
 * */
public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback {

    //Set variables
    Camera camera;
    SurfaceHolder holder;

    /**
     * Instantiate ShowCamera, setting camera and add callback to holder
     * @param camera
     * @param context
     * */
    public ShowCamera(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        holder = getHolder();
        holder.addCallback(this);
    }

    /**
     * When camera surface is created
     * @param holder
     * */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Camera.Parameters params = camera.getParameters();

        //Change the orientation of the cameras
        if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
            params.set("orientation", "portrait");
            camera.setDisplayOrientation(90);
            params.setRotation(90);
        }
        else{
            params.set("orientation", "landscape");
            camera.setDisplayOrientation(0);
            params.setRotation(0);
        }

        camera.setParameters(params);
        try{
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * SurfaceHolder method: surfaceChanged
     * @param holder
     * @param format
     * @param height
     * @param width
     * */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    /**
     * SurfaceHolder method: surfaceDestroyed
     * @param holder
     * */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
    }
}
