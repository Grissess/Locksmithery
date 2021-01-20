package mods.grissess.ls.item.render;

import mods.grissess.ls.Locksmithery;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class CoreExtractorRender extends TileEntityItemStackRenderer {
    protected IBakedModel flipped_model;
    protected boolean firstFrame = false;
    protected int frames = 0;

    @Override
    public void renderByItem(ItemStack stack, float partialTicks) {
        partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        GlStateManager.translate(0.5f, 0.5f, 0.5f);

        IBakedModel model = Minecraft.getMinecraft()
                .getRenderItem()
                .getItemModelWithOverrides(stack, null, null);

        assert model instanceof TransformCachingModel.Baked;
        TransformCachingModel.Baked mdl = (TransformCachingModel.Baked) model;
        if(
                mdl.transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND ||
                mdl.transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND
        ) {
            if(flipped_model == null) {
                try {
                    IModel flipped = ModelLoaderRegistry.getModel(
                            new ResourceLocation(Locksmithery.MODID, "item/core_extractor_flipped")
                    );
                    flipped_model = flipped.bake(
                            flipped.getDefaultState(),
                            DefaultVertexFormats.ITEM,
                            ModelLoader.defaultTextureGetter()
                    );
                    Locksmithery.logger.debug("CER.rBI: loaded " + flipped + " baked to " + flipped_model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Minecraft.getMinecraft()
                    .getRenderItem()
                    .renderItem(stack, flipped_model);
        } else {
            Minecraft.getMinecraft()
                    .getRenderItem()
                    .renderItem(stack, mdl.delegate);
        }
    }
}
