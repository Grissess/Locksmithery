package mods.grissess.ls.gui;

import mods.grissess.ls.Locksmithery;
import mods.grissess.ls.block.container.LocksmithWorkbenchContainer;
import mods.grissess.ls.block.te.LocksmithWorkbenchTE;
import mods.grissess.ls.data.BittingDescriptor;
import mods.grissess.ls.item.Key;
import mods.grissess.ls.item.Lockset;
import mods.grissess.ls.registry.Items;
import mods.grissess.ls.net.Channel;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class GuiLocksmithing extends GuiContainer {
    public static final int X_GRID_OFFSET = 40;
    public static final int Y_GRID_OFFSET = 7;
    public static final int GRID_WIDTH = 10;
    public static final int GRID_HEIGHT = 10;

    protected List<List<GuiButton>> bittingButtons = new ArrayList<>(BittingDescriptor.MAX_POSITIONS);

    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(Locksmithery.MODID, "textures/gui/container/locksmith_workbench.png");

    public static int idToPosition(int id) {
        return id / BittingDescriptor.MAX_SETTINGS;
    }
    public static int idToSetting(int id) {
        return id % BittingDescriptor.MAX_SETTINGS;
    }
    public static int positionSettingToID(int pos, int set) {
        return set + pos * BittingDescriptor.MAX_SETTINGS;
    }

    public GuiLocksmithing(InventoryPlayer playerInv, LocksmithWorkbenchTE te) {
        super(new LocksmithWorkbenchContainer(playerInv, te));
    }

    public LocksmithWorkbenchContainer getContainer() {
        return (LocksmithWorkbenchContainer) inventorySlots;
    }

    @Override
    public void initGui() {
        super.initGui();

        buttonList.clear();
        bittingButtons.clear();

        for(int position = 0; position < BittingDescriptor.MAX_POSITIONS; position++) {
            List<GuiButton> innerList = new ArrayList<>(BittingDescriptor.MAX_SETTINGS);
            for(int setting = 0; setting < BittingDescriptor.MAX_SETTINGS; setting++) {
                GuiButton button = new GuiButton(
                        positionSettingToID(position, setting),
                        guiLeft + X_GRID_OFFSET + GRID_WIDTH * position,
                        guiTop + Y_GRID_OFFSET + GRID_HEIGHT * setting,
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
        ItemStack input = getContainer().tileEntity.getInputStack();
        BittingDescriptor desc = null;
        if(input != null) {
            if(input.getItem() == Items.key) {
                desc = Key.getBittingDescriptor(input);
            }
            if(input.getItem() == Items.lockset) {
                desc = Lockset.getBittingDescriptor(input);
            }
        }

        LocksmithWorkbenchTE te = getContainer().tileEntity;
        for(int position = 0; position < BittingDescriptor.MAX_POSITIONS; position++) {
            Set<Integer> cuts = te.getAllCuts(position);
            for(int setting = 0; setting < BittingDescriptor.MAX_SETTINGS; setting++) {
                GuiButton button = bittingButtons.get(position).get(setting);
                button.visible =
                        desc != null && position < desc.positions && setting < desc.settings;
                button.packedFGColour =
                        cuts.contains(setting) ? 0xff00 : 0;

            }
        }

        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        int position = idToPosition(button.id);
        int setting = idToSetting(button.id);
        if(position < 0 || position >= BittingDescriptor.MAX_POSITIONS) return;
        if(setting < 0 || setting >= BittingDescriptor.MAX_SETTINGS) return;

        LocksmithWorkbenchContainer c = getContainer();
        ItemStack inputStack = c.tileEntity.getInputStack();
        if(inputStack == null) return;

        if(inputStack.getItem() == Items.lockset) {
            int current = c.tileEntity.getField(position);
            c.tileEntity.setField(position, current ^ (1 << setting));
        }
        if(inputStack.getItem() == Items.key) {
            c.tileEntity.setField(position, 1 << setting);
        }

        Locksmithery.proxy.channel.sendToServer(
                new Channel.PacketSetCuts(
                        c.world,
                        c.position,
                        c.tileEntity.getCutsAsArray()
                )
        );
    }
}
