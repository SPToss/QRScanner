package DataTransferObject;

import android.media.Image;

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

}
