package Domain;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Sebastian on 13.02.2017.
 */
public class PropertyReader
{
    public static Properties properties = null;

    public static String GetPropertyByName(String key, Context context) throws IOException
    {
        if(properties == null){
            properties = new Properties();
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("config.properties");
            properties.load(inputStream);
        }
        return properties.getProperty(key);
    }
}
