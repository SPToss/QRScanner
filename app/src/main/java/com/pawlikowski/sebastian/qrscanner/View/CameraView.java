package com.pawlikowski.sebastian.qrscanner.View;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by Sebastian on 22.01.2016.
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback
{

    private SurfaceHolder _surfaceHolder;
    private android.hardware.Camera _camera;
    public CameraView(Context context, android.hardware.Camera camera)
    {
        super(context);

        _camera = camera;
        _camera.setDisplayOrientation(90);
        _surfaceHolder = getHolder();
        _surfaceHolder.addCallback(this);
        _surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        try{
            _camera.setPreviewDisplay(_surfaceHolder);
            _camera.startPreview();
        }
        catch (IOException e){
            // Some sort of shit
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        if(_surfaceHolder.getSurface() == null)
        {
            return;
        }
        try
        {
            _camera.stopPreview();
        } catch (Exception e)
        {
            // Shit happen
        }

        try
        {
            _camera.setPreviewDisplay(_surfaceHolder);
        }catch (IOException e){
            // Shit happen again
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        _camera.startPreview();
        _camera.release();
    }
}
