// func_1238_a reimplemented for use in mod_oldCustomLogo
// Has been tweaked slightly to allow metadata

package net.rek.oldlogo.gui.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;

// Referenced classes of package net.minecraft.src:
//            RenderBlocks, Block, Tessellator

public class RenderBlocksLogoFunc extends RenderBlocks
{

    public RenderBlocksLogoFunc()
    {
    }

    public void func_1238_a(Block block, int i, float f)
    {
        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        float f1 = 0.5F;
        float f2 = 1.0F;
        float f3 = 0.8F;
        float f4 = 0.6F;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f2, f2, f2, f);
        renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(0, i));
        tessellator.setColorRGBA_F(f1, f1, f1, f);
        renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(1, i));
        tessellator.setColorRGBA_F(f3, f3, f3, f);
        renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(2, i));
        renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(3, i));
        tessellator.setColorRGBA_F(f4, f4, f4, f);
        renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(4, i));
        renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(5, i));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }
}
