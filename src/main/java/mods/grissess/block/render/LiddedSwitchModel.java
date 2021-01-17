package mods.grissess.block.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LiddedSwitchModel extends ModelBase {
    public ModelRenderer base = new ModelRenderer(this, 0, 0).setTextureSize(16, 16);
    public ModelRenderer lid = new ModelRenderer(this, 4, 0).setTextureSize(12, 8);
    public ModelRenderer button = new ModelRenderer(this, 0, 0).setTextureSize(16, 16);
    public ModelRenderer rocker = new ModelRenderer(this, 0, 0).setTextureSize(16, 16);

    public static final ResourceLocation TEX_BASE = new ResourceLocation("securitycraft", "textures/entity/liddedswitch/base.png");
    public static final ResourceLocation TEX_LID = new ResourceLocation("securitycraft", "textures/entity/liddedswitch/lid.png");
    public static final ResourceLocation TEX_SWITCH = new ResourceLocation("securitycraft", "textures/entity/liddedswitch/switch.png");

    public static final float QUARTER_PI = (float)Math.PI / 4f;

    public LiddedSwitchModel() {
        base.addBox(2f, 0f, 4f, 12, 1, 8);
        lid.addBox(0f, 0f, -8f, 12, 5, 8);
        lid.setRotationPoint(2f, 1f, 12f);
        button.addBox(6f, 1f, 7f, 4, 2, 2);
        rocker.addBox(-1f, 0f, 0f, 2, 4, 2);
        rocker.setRotationPoint(8f, 1f, 8f);
    }

    public void renderAll(TextureManager textureManager, boolean toggle, float open, float powered) {
        textureManager.bindTexture(TEX_BASE);
        base.render(0.0625f);

        textureManager.bindTexture(TEX_SWITCH);
        if(toggle) {
            rocker.rotateAngleZ = (-1f + 2f * powered) * QUARTER_PI;
            rocker.render(0.0625f);
        } else {
            button.offsetY = powered * powered * -0.03125f;
            button.render(0.0625f);
        }

        textureManager.bindTexture(TEX_LID);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
        lid.rotateAngleX = (1f - (1f - open * open * open)) * QUARTER_PI;
        lid.render(0.0625f);
        GlStateManager.disableBlend();
    }
}
