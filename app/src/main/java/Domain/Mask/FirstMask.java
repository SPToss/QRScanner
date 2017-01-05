package Domain.Mask;

/**
 * Created by Sebastian on 05.04.2016.
 */
public class FirstMask implements IMask
{
    @Override
    public Boolean Calculate(int i, int j)
    {
        return j % 3 == 0;
    }
}
