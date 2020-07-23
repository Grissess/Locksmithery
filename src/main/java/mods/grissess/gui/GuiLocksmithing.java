package mods.grissess.gui;

import mods.grissess.block.container.LocksmithWorkbenchContainer;
import mods.grissess.block.inv.LocksmithWorkbenchInventory;
import mods.grissess.data.BittingDescriptor;
import mods.grissess.item.Key;
import mods.grissess.registry.Items;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiLocksmithing extends GuiContainer {
    public static final int X_GRID_OFFSET = 40;
    public static final int Y_GRID_OFFSET = 7;
    public static final int GRID_WIDTH = 10;
    public static final int GRID_HEIGHT = 10;

    protected List<List<GuiButton>> bittingButtons = new ArrayList<>(BittingDescriptor.MAX_POSITIONS);

    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("securitycraft:textures/gui/container/locksmith_workbench.png");

    public static int idToPosition(int id) {
        return id / BittingDescriptor.MAX_SETTINGS;
    }
    public static int idToSetting(int id) {
        return id % BittingDescriptor.MAX_SETTINGS;
    }
    public static int positionSettingToID(int pos, int set) {
        return set + pos * BittingDescriptor.MAX_SETTINGS;
    }

    public GuiLocksmithing(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    public LocksmithWorkbenchContainer getContainer() {
        return (LocksmithWorkbenchContainer) inventorySlots;
    }

    @Override
    public void initGui() {
        buttonList.clear();

        for(int position = 0; position < BittingDescriptor.MAX_POSITIONS; position++) {
            List<GuiButton> innerList = new ArrayList<>(BittingDescriptor.MAX_SETTINGS);
            for(int setting = 0; setting < BittingDescriptor.MAX_SETTINGS; setting++) {
                GuiButton button = new GuiButton(
                        positionSettingToID(position, setting),
                        X_GRID_OFFSET + GRID_WIDTH * position,
                        Y_GRID_OFFSET + GRID_HEIGHT * setting,
                        GRID_WIDTH, GRID_HEIGHT,
                        "" + setting
                );
                button.visible = false;
                innerList.add(button);
                addButton(button);
            }
            bittingButtons.add(innerList);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ItemStack input = ((LocksmithWorkbenchInventory) getContainer().tileEntity.inventory).getInputStack();
        BittingDescriptor desc = null;
        if(input != null) {
            if(input.getItem() == Items.key) {
                desc = BittingDescriptor.WOOD;
            }
            if(input.getItem() == Items.cylinder) {
                desc = BittingDescriptor.WOOD;
            }
        }

        for(int position = 0; position < BittingDescriptor.MAX_POSITIONS; position++) {
            for(int setting = 0; setting < BittingDescriptor.MAX_SETTINGS; setting++) {
                bittingButtons.get(position).get(setting).visible =
                        desc != null && position < desc.positions && setting < desc.settings;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        int position = idToPosition(button.id);
        int setting = idToSetting(button.id);
        if(position < 0 || position >= BittingDescriptor.MAX_POSITIONS) return;
        if(setting < 0 || setting >= BittingDescriptor.MAX_SETTINGS) return;

        LocksmithWorkbenchContainer c = getContainer();
        int current = c.tileEntity.inventory.getField(position);
        c.tileEntity.inventory.setField(position, current | (1 << setting));
    }
}
