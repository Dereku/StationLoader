package net.modificationstation.stationloader;

import net.modificationstation.stationloader.events.SLMainCalledEvent;
import net.modificationstation.stationloader.events.SLPostInitializationEvent;
import net.modificationstation.stationloader.loaders.Loader;

public class StationHooks {
    public static void onMainCalled(String args[]) {
        Loader.INSTANCE.loadMods();
        StationLoader.startEvent(new SLMainCalledEvent(args));
    }
    
    public static void postInit() {
        StationLoader.startEvent(new SLPostInitializationEvent());
    }
}
