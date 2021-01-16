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

        boolean open = true, powered = false;
        if(te.hasWorld()) {
            IBlockState state = te.getWorld().getBlockState(te.getPos());
            open = state.getValue(LiddedSwitch.OPEN);
            powered = state.getValue(LiddedSwitch.POWERED);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        MODEL.renderAll(rendererDispatcher.renderEngine, ((LiddedSwitch)te.getBlockType()).toggle, open, powered);
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }
}
