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
    public IMask RetriveMask(String status)
    {
        switch (status)
        {
            case "111":
            {
                return new FirstMask();
            }
            case "110":
            {
                return new SecondMask();
            }
            case "101":
            {
                return new ThirdMask();
            }
            case "100":
            {
                return new FourthMask();
            }
            case "011":
            {
                return new FifthMask();
            }
            case "010":
            {
                return new SixthMask();
            }
            case "001":
            {
                return new SeventhMask();
            }
            case "000":
            {
                return new EigthMask();
            }
            default: return null;
        }
    }
}
