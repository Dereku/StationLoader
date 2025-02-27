package net.modificationstation.classloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

public class FMLInjectionData
{
    static File minecraftHome;
    static String major;
    static String minor;
    static String rev;
    static String build;
    static String mccversion;
    static String mcsversion;
    public static List<String> containers = new ArrayList<String>();

    static void build(File mcHome, MCClassLoader classLoader)
    {
        minecraftHome = mcHome;
        InputStream stream = classLoader.getResourceAsStream("fmlversion.properties");
        Properties properties = new Properties();

        if (stream != null)
        {
            try
            {
                properties.load(stream);
            }
            catch (IOException ex)
            {
                Log.log(Level.SEVERE, ex, "Could not get FML version information - corrupted installation detected!");
            }
        }

        major = properties.getProperty("fmlbuild.major.number", "missing");
        minor = properties.getProperty("fmlbuild.minor.number", "missing");
        rev = properties.getProperty("fmlbuild.revision.number", "missing");
        build = properties.getProperty("fmlbuild.build.number", "missing");
        mccversion = properties.getProperty("fmlbuild.mcclientversion", "missing");
        mcsversion = properties.getProperty("fmlbuild.mcserverversion", "missing");


    }

    public static Object[] data()
    {
        return new Object[] { major, minor, rev, build, mccversion, mcsversion, minecraftHome, containers };
    }
}
