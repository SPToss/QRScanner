package Domain.Mask;

import Domain.ImageService;

/**
 * Created by Sebastian on 05.04.2016.
 */
public class FourthMask implements IMask
{
    @Override
    public Boolean Calculate(int i, int j)
    {
        return i % 2 == 0;
    }
}
