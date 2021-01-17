package mods.grissess.item.render;

import mods.grissess.SecurityCraft;
import mods.grissess.block.SecureDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;
import java.util.function.Function;

public class TransformCachingModel implements IModel {
    public IModel delegate;

    public TransformCachingModel(IModel del) {
        delegate = del;
    }
    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        IBakedModel baked = delegate.bake(state, format, bakedTextureGetter);
        System.out.println("TCM.b: baked " + delegate + " to " + baked);
        return new Baked(baked);
    }

    public static class Baked implements IBakedModel {
        public IBakedModel delegate;
        public ItemCameraTransforms.TransformType transformType;

        public Baked(IBakedModel del) {
            delegate = del;
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
            return delegate.getQuads(state, side, rand);
        }

        @Override
        public boolean isAmbientOcclusion() {
            return delegate.isAmbientOcclusion();
        }

        @Override
        public boolean isGui3d() {
            return delegate.isGui3d();
        }

        @Override
        public boolean isBuiltInRenderer() {
            return true;  // Needed to activate TEISR
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return delegate.getParticleTexture();
        }

        @Override
        public ItemOverrideList getOverrides() {
            return delegate.getOverrides();
        }

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
            transformType = cameraTransformType;
            Pair<? extends IBakedModel, Matrix4f> result = delegate.handlePerspective(cameraTransformType);
            return Pair.of(new Baked(result.getLeft()), result.getRight());
        }
    }

    public enum Loader implements ICustomModelLoader {
        INSTANCE;

        public static final String PREFIX = "models/item/teisr_";
        public static final String PATH_ROOT = "item/";

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager) {

        }

        @Override
        public boolean accepts(ResourceLocation modelLocation) {
            boolean result = modelLocation.getResourceDomain().equals(SecurityCraft.MODID) &&
                    modelLocation.getResourcePath().startsWith(PREFIX);
            if(result)
                System.out.println("TCM.L.a: overriding models returned for " + modelLocation);
            return result;
        }

        @Override
        public IModel loadModel(ResourceLocation modelLocation) throws Exception {
            ResourceLocation rl = new ResourceLocation(
                    modelLocation.getResourceDomain(),
                    PATH_ROOT + modelLocation.getResourcePath().substring(PREFIX.length())
            );
            IModel result = ModelLoaderRegistry.getModel(rl);
            System.out.println("TCM.L.lM: mapped " + modelLocation + " to " + rl + " load result " + result);
            return new TransformCachingModel(result);
        }
    }
}
