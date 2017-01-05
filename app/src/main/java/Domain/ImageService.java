package Domain;

import android.graphics.Bitmap;

import DataTransferObject.ImageDto;
import DataTransferObject.QrCodeImageDto;
import Domain.Contracts.IImageService;
import Domain.Contracts.IQrService;

/**
 * Created by Sebastian on 26.01.2016.
 */
public final class ImageService implements IImageService
{
    private static ImageService _imageService;
    private static ImageDto _imageDto;
    private QrCodeImageDto _finalImage;
    private ImageService()
    {
    }


    // Declaration of singleton
    public static ImageService Initate(Bitmap bitmap)
    {
        if (_imageService == null){
            _imageService = new ImageService();
            _imageDto = new ImageDto();
            _imageDto.SetBitmap(bitmap);
        }
        return _imageService;
    }


    public boolean SearchForFinder()
    {
        _finalImage = new FinderSearcher().FinderBase(_imageDto);
        if(_finalImage == null){
            return false;
        }
        else return true;
    }

    public ImageDto TurnIntoGrayScale(ImageDto colorPhoto)
    {
        ImageDto newImageDto = colorPhoto;
        int width = colorPhoto.GetWidth();
        int height = colorPhoto.GetHeight();

        Bitmap grayScaleBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);

        newImageDto.SetBitmap(grayScaleBitmap);
        return newImageDto;
    }

    public char Decode(){
        String type = Analise();
        IQrService service;
        switch (type){
            case "first":
            {
                service = new FirstVersion(_finalImage);
            }
            case "second":
            {
                service = new SecondVersion();
            }
            case "third":
            {
                service = new ThirdVersion();
            }

            char test = service.Decode();
        }
        return '0';
    }

    private String Analise(){
        return "first";
    }
}