package Domain.Contracts;

import java.util.ArrayList;

import DataTransferObject.QrCodeImageDto;

/**
 * Created by Sebastian on 05.04.2016.
 */
public interface IQrService
{
    ArrayList<Double[]> Decode();
}
