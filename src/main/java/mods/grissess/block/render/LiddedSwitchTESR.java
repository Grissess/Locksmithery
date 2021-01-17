package mods.grissess.block.render;

import mods.grissess.block.LiddedSwitch;
import mods.grissess.block.te.LiddedSwitchTE;
import mods.grissess.net.Channel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LiddedSwitchTESR extends TileEntitySpecialRenderer<LiddedSwitchTE> {
    public static LiddedSwitchModel MODEL = new LiddedSwitchModel();
    @Override
    public void render(LiddedSwitchTE te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        GlStateManager.pushAttrib();
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.depthMask(true);

        float open = te.open + (te.open - te.prevOpen) * partialTicks;
        float powered = te.powered + (te.powered - te.prevPowered) * partialTicks;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        if(te.hasWorld()) {
            switch(te.getWorld().getBlockState(te.getPos()).getValue(LiddedSwitch.DIRECTION).getHorizontalIndex()) {
                default: break;
                case 1:
                    GlStateManager.translate(0.5f, 0.5f, 0.5f);
                    GlStateManager.rotate(-90f, 0f, 1f, 0f);
                    GlStateManager.translate(-0.5f, -0.5f, -0.5f);
                    break;
                case 2:
                    GlStateManager.translate(0.5f, 0.5f, 0.5f);
                    GlStateManager.rotate(180f, 0f, 1f, 0f);
                    GlStateManager.translate(-0.5f, -0.5f, -0.5f);
                    break;
                case 3:
                    GlStateManager.translate(0.5f, 0.5f, 0.5f);
                    GlStateManager.rotate(90f, 0f, 1f, 0f);
                    GlStateManager.translate(-0.5f, -0.5f, -0.5f);
                    break;
            }
        }
        MODEL.renderAll(rendererDispatcher.renderEngine, ((LiddedSwitch)te.getBlockType()).toggle, open, powered);
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }
}
