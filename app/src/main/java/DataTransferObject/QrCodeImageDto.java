package DataTransferObject;

import android.graphics.Bitmap;
import android.media.Image;

import Domain.Mask.IMask;
import Domain.VersionBase;

/**
 * Created by Sebastian on 05.02.2016.
 */
public class QrCodeImageDto
{
    public QrCodeImageDto(ImageDto image, int marker)
    {
        Marker = marker;
        Image = image;
    }

    private ImageDto Image;
    public ImageDto GetImage()
    {
        return Image;
    }
    public void SetImage(ImageDto image)
    {
        Image = image;
    }

    private int Marker;
    public int GetMarker()
    {
        return Marker;
    }
    public void SetMarker(int marker)
    {
        Marker = marker;
    }

    public IMask GetMask(){

        ImageDto processImage = new ImageDto();
        processImage.SetBitmap(Bitmap.createBitmap(Image.GetBitmap(), 2 * Marker, 8 * Marker, 3 * Marker, Marker ));
        processImage.setBinaryTable();
        int[][] bitTable = processImage.getBinaryTable();
        String result = "";
        int white = 0;
        int black = 0;
        for (int i = 0; i< Marker; i++)
        {
            for (int j =  0; j<  Marker ; j++)
            {
                byte color = (byte)bitTable[i][j];
                if(color == 1){
                    black++;
                }
                else {
                    white++;
                }
            }
        }
        if((double)black/((double)black + (double)white) > 0.50){
            result += "1";
        }
        else {
            result += "0";
        }
        white = 0;
        black = 0;
        for (int i = 0; i<  Marker; i++)
        {
            for (int j =  Marker; j<  2 * Marker ; j++)
            {
                byte color = (byte)bitTable[i][j];
                if(color == 1){
                    black++;
                }
                else {
                    white++;
                }
            }
        }
        if((double)black/((double)black + (double)white) > 0.50){
            result += "1";
        }
        else {
            result += "0";
        }
        white = 0;
        black = 0;
        for (int i = 0; i< Marker; i++)
        {
            for (int j =  2 * Marker; j<  3 * Marker ; j++)
            {
                byte color = (byte)bitTable[i][j];
                if(color == 1){
                    black++;
                }
                else {
                    white++;
                }
            }
        }
        if((double)black/((double)black + (double)white) > 0.50){
            result += "1";
        }
        else {
            result += "0";
        }
        return new VersionBase().RetriveMask(result);

    }

}
