package Domain.Mask;

/**
 * Created by Sebastian on 05.04.2016.
 */
public class EigthMask implements IMask
{
    @Override
    public Boolean Calculate(int i, int j)
    {
        return  (i*j)% 2 + (i*j)%3 == 0;
    }
}
