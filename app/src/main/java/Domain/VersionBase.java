package Domain;

import DataTransferObject.Enums.MaskStatus;
import Domain.Mask.EigthMask;
import Domain.Mask.FifthMask;
import Domain.Mask.FirstMask;
import Domain.Mask.FourthMask;
import Domain.Mask.IMask;
import Domain.Mask.SecondMask;
import Domain.Mask.SeventhMask;
import Domain.Mask.SixthMask;
import Domain.Mask.ThirdMask;

/**
 * Created by Sebastian on 06.04.2016.
 */
public class VersionBase
{
    public IMask RetriveMask(MaskStatus status)
    {
        switch (status)
        {
            case First:
            {
                return new FirstMask();
            }
            case Second:
            {
                return new SecondMask();
            }
            case Third:
            {
                return new ThirdMask();
            }
            case Fourth:
            {
                return new FourthMask();
            }
            case Fifth:
            {
                return new FifthMask();
            }
            case Sixth:
            {
                return new SixthMask();
            }
            case Seventh:
            {
                return new SeventhMask();
            }
            case Eight:
            {
                return new EigthMask();
            }
            default: return null;
        }
    }
}
