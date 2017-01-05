package Domain.Contracts;

import DataTransferObject.ImageDto;

/**
 * Created by Sebastian on 26.01.2016.
 */
public interface IImageService{

    ImageDto TurnIntoGrayScale(ImageDto colorPhoto);
}
