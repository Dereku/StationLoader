package net.modificationstation.classloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import net.modificationstation.stationloader.coremod.EventsInjectorTransformer;
import net.modificationstation.stationloader.coremod.SideTransformer;

public class MCClassLoader extends URLClassLoader
{
    private static String[] excludedPackages = {
        //"java.", "sun.", "javax.",
        "net.modificationstation.classloader."//, "net.minecraftforge.classloading."
    };

    private static String[] transformerExclusions =
    {
        "org.objectweb.asm."//, "com.google.common.", "cpw.mods.fml."
    };
    private List<URL> sources;
    private ClassLoader parent;

    private List<IClassTransformer> transformers;
    private Map<String, Class<?>> cachedClasses;

    public MCClassLoader(URL[] sources)
    {
        super(sources, null);
        this.sources = new ArrayList<URL>(Arrays.asList(sources));
        this.parent = getClass().getClassLoader();
        this.cachedClasses = new HashMap<String,Class<?>>(1000);
        this.transformers = new ArrayList<IClassTransformer>(2);
        
        registerTransformer(EventsInjectorTransformer.class.getName());
        registerTransformer(SideTransformer.class.getName());
        
        ReflectionHelper.setPrivateValue(ClassLoader.class, null, this, "scl");
        Thread.currentThread().setContextClassLoader(this);
    }

    public void registerTransformer(String transformerClassName)
    {
        try
        {
            transformers.add((IClassTransformer) loadClass(transformerClassName).newInstance());
        }
        catch (Exception e)
        {
            Log.log(Level.SEVERE, e, "A critical problem occured registering the ASM transformer class %s", transformerClassName);
        }
    }
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException
    {
        for (String st : excludedPackages)
        {
            if (name.startsWith(st))
            {
                return parent.loadClass(name);
            }
        }

        if (cachedClasses.containsKey(name))
        {
            return cachedClasses.get(name);
        }

        for (String st : transformerExclusions)
        {
            if (name.startsWith(st))
            {
                Class<?> cl = super.findClass(name);
                cachedClasses.put(name, cl);
                return cl;
            }
        }

        try
        {
            int lastDot = name.lastIndexOf('.');
            if (lastDot > -1)
            {
                String pkgname = name.substring(0, lastDot);
                if (getPackage(pkgname)==null)
                {
                    definePackage(pkgname, null, null, null, null, null, null, null);
                }
            }
            byte[] basicClass = getClassBytes(name);
            byte[] transformedClass = runTransformers(name, basicClass);
            Class<?> cl = defineClass(name, transformedClass, 0, transformedClass.length);
            cachedClasses.put(name, cl);
            return cl;
        }
        catch (Throwable e)
        {
            throw new ClassNotFoundException(name, e);
        }
    }

    /**
     * @param name
     * @return
     * @throws IOException
     */
    public byte[] getClassBytes(String name) throws IOException
    {
        InputStream classStream = null;
        try
        {
            URL classResource = findResource(name.replace('.', '/').concat(".class"));
            if (classResource == null)
            {
                return null;
            }
            classStream = classResource.openStream();
            return readFully(classStream);
        }
        finally
        {
            if (classStream != null)
            {
                try
                {
                    classStream.close();
                }
                catch (IOException e)
                {
                    // Swallow the close exception
                }
            }
        }
    }

    private byte[] runTransformers(String name, byte[] basicClass)
    {
        for (IClassTransformer transformer : transformers)
        {
            basicClass = transformer.transform(name, basicClass);
        }
        return basicClass;
    }

    @Override
    public void addURL(URL url)
    {
        super.addURL(url);
        sources.add(url);
    }

    public List<URL> getSources()
    {
        return sources;
    }


    private byte[] readFully(InputStream stream)
    {
        try
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(stream.available());
            int r;
            while ((r = stream.read()) != -1)
            {
                bos.write(r);
            }

            return bos.toByteArray();
        }
        catch (Throwable t)
        {
            /// HMMM
            return new byte[0];
        }
    }
}
