package net.modificationstation.stationloader.loaders;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.modificationstation.stationloader.StationLoader;
import net.modificationstation.stationloader.util.Mod;

public class Loader {
    private Loader() {
    }
	public void loadMods() {
	    File modsFolder = null;
	    try {
	        modsFolder = new File(new File(".").getCanonicalPath() + "/mods/");
	    } catch (Exception e) {}
        if (!modsFolder.exists()) {modsFolder.mkdir();}
        File[] mods = modsFolder.listFiles();
        for (int i = 0; i < mods.length; i++){
            try {
			    File modFile = mods[i];
			    ClassLoader loader = URLClassLoader.newInstance(
			            new URL[] { modFile.toURI().toURL() },
			            getClass().getClassLoader()
			        );
			    JarFile modJar = new JarFile(modFile);
			    Enumeration<JarEntry> modClasses = modJar.entries();
			    while (modClasses.hasMoreElements()) {
			        JarEntry modClass = modClasses.nextElement();
			        try {
			            Class<?> clazz = Class.forName(modClass.getName().replace(".class", "").replace("/", "."), true, loader);
			            if (clazz.isAnnotationPresent(Mod.class)) {
		                    StationLoader.addMod(clazz);
			            }
			        } catch (Exception e) {}
			    }
			    modJar.close();
		    } catch (Exception e) {};
        }
	}
	public final static Loader INSTANCE = new Loader();
}
