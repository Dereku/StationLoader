//Straight outta 1.3
//Used in mod_oldCustomLogo

package net.rek.oldlogo.gui.util;

import net.rek.oldlogo.gui.GuiMainMenu;

// Referenced classes of package net.minecraft.src:
//            GuiMainMenu

public class LogoEffectRandomizer
{

    public LogoEffectRandomizer(GuiMainMenu guimainmenu, int i, int j)
    {
        mainMenu = guimainmenu;
        field_1312_a = field_1311_b = (double)(10 + j) + GuiMainMenu.getRand().nextDouble() * 32D + (double)i;
    }

    public void func_875_a()
    {
        field_1311_b = field_1312_a;
        if(field_1312_a > 0.0D)
        {
            field_1314_c -= 0.59999999999999998D;
        }
        field_1312_a += field_1314_c;
        field_1314_c *= 0.90000000000000002D;
        if(field_1312_a < 0.0D)
        {
            field_1312_a = 0.0D;
            field_1314_c = 0.0D;
        }
    }

    public double field_1312_a;
    public double field_1311_b;
    public double field_1314_c;
    final GuiMainMenu mainMenu;
}
