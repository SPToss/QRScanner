package Domain;

import DataTransferObject.Enums.MaskStatus;
import DataTransferObject.QrCodeImageDto;
import Domain.Contracts.IQrService;
import Domain.Mask.IMask;

/**
 * Created by Sebastian on 05.04.2016.
 */
public class FirstVersion extends VersionBase implements IQrService
{
    private QrCodeImageDto _image;
    public FirstVersion(QrCodeImageDto image){
        _image = image;
    }

    @Override
    public char Decode()
    {
        return 0;
    }

}
