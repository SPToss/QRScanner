package com.pawlikowski.sebastian.qrscanner.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.pawlikowski.sebastian.qrscanner.R;

import java.io.IOException;

import Domain.ImageService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScannerScreanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScannerScreanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScannerScreanFragment extends Fragment implements SurfaceHolder.Callback
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Camera camera1 = null;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    public static boolean previewing = false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScannerScreanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScannerScreanFragment newInstance(String param1, String param2)
    {
        ScannerScreanFragment fragment = new ScannerScreanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ScannerScreanFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
                if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scanner_screan, container, false);
        surfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);

        //surfaceView = new SurfaceView(getActivity());
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //surfaceView.setBackgroundResource(R.drawable.your_background_image);

        if(!previewing){

            camera1 = Camera.open();
            if (camera1 != null){
                try {
                    camera1.setDisplayOrientation(90);
                    camera1.setPreviewDisplay(surfaceHolder);
                    camera1.startPreview();
                    previewing = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        final Button captureButton = (Button)view.findViewById(R.id.scanButton);
        captureButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(camera1 != null){
                    try{
                        camera1.takePicture(myShutterCallback,myPictureCallback_RAW,myPictureCallback_JPG);
                    }
                    catch (Exception e){

                    }
                }
            }
        });
        return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        if(previewing){
            camera1.stopPreview();
            previewing = false;
        }

        if (camera1 != null){
            try {
                camera1.setPreviewDisplay(surfaceHolder);
                camera1.startPreview();
                previewing = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {

    }

    public void surfaceDestroyed(SurfaceHolder holder) {

        camera1.stopPreview();
        camera1.release();
        camera1 = null;
        previewing = false;

    }

    Camera.ShutterCallback myShutterCallback = new Camera.ShutterCallback(){

        public void onShutter() {
        }};

    Camera.PictureCallback myPictureCallback_RAW = new Camera.PictureCallback(){

        public void onPictureTaken(byte[] arg0, Camera arg1) {
        }};

    Camera.PictureCallback myPictureCallback_JPG = new Camera.PictureCallback(){

        public void onPictureTaken(byte[] arg0, Camera arg1) {
            Bitmap bitmapPicture = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);

            Bitmap correctBmp = Bitmap.createBitmap(bitmapPicture, 0, 0, bitmapPicture.getWidth(), bitmapPicture.getHeight(), null, true);

            BitmapFactory.Options di = new BitmapFactory.Options();
            di.inScaled = false;
            final Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qr3, di);
            final Context c = getActivity().getApplicationContext();
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

            alertDialogBuilder.setMessage("Application will scan ");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            String message = "";
                            ImageService service = ImageService.Initate(myBitmap, c);
                            boolean isQrCode = service.SearchForFinder();
                            if (isQrCode)
                            {
                                try
                                {
                                    message = service.Decode();
                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            final AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(getActivity());

                            alertDialogBuilder1.setMessage("Scan result : \n" + message);
                            alertDialogBuilder1.setCancelable(false);
                            alertDialogBuilder1.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            alertDialogBuilder.create().dismiss();
                                            alertDialogBuilder1.create().dismiss();
                                        }
                                    }
                            );
                            alertDialogBuilder1.show();
                        }

                    }
            );
            alertDialogBuilder.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            alertDialogBuilder.create().dismiss();
                        }
                    }
            );
            alertDialogBuilder.show();
        }};

}
