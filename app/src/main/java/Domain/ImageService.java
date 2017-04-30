package Domain;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.Cipher;

import DataTransferObject.Enums.CodeVersion;
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
    private static Network _network;
    private ImageService()
    {
    }


    // Declaration of singleton
    public static ImageService Initate(Bitmap bitmap, Context context)
    {
        if (_imageService == null){
            _imageService = new ImageService();;
            _network = new Network(context);
        }
        _imageDto = new ImageDto();
        _imageDto.SetBitmap(bitmap);
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

    public String Decode() throws Exception
    {
        CodeVersion type = Analise();
        IQrService service;
        switch (type){
            case First:
            {
                service = new FirstVersion(_finalImage);
                break;
            }
            case Second:
            {
                service = new SecondVersion();
                break;
            }
            case Third:
            {
                service = new ThirdVersion();
                break;
            }
            case NotSupported:
             default:
                 throw new Exception("No Supported QR Code Found ");
        }
        ArrayList<Double[]> result = service.Decode();
        String message = "Sieć neuronowa : ";
        for(Double[] element : result){
            message += _network.GetCharFormSegment(new ArrayList<>(Arrays.asList(element)));
           }
        message +=  "\n Normalna Metoda : ";
        for(Double[] element : result){
            String lenh = "";
            for (Double e:element)
            {
                if (e > 0.5)
                {
                    lenh += "1";
                }
                else {
                    lenh += "0";
                }
            }
            int code = Integer.parseInt(lenh, 2);

            message += Character.toString((char)  ( + code));
        }

        return message;
    }

    private CodeVersion Analise()
    {
        if(_finalImage == null){
            return CodeVersion.NotSupported;
        }
        double ratio =  (double)_finalImage.GetImage().GetHeight() / (double)_finalImage.GetMarker();
        if(ratio > 20 && ratio < 22){
            return  CodeVersion.First;
        }

        if (ratio > 24 && ratio < 26){
            return CodeVersion.Second;
        }

        if(ratio > 28 && ratio < 30){
            return CodeVersion.Third;
        }

        return CodeVersion.NotSupported;
    }
}