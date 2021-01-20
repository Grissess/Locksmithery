package mods.grissess.ls.block.te;

import mods.grissess.ls.block.LiddedSwitch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ITickable;

public class LiddedSwitchTE extends BaseBittedTE implements ITickable {
    public float open = 0f;
    public float powered = 0f;
    public float prevOpen = 0f;
    public float prevPowered = 0f;

    @Override
    public void update() {
        prevOpen = open;
        prevPowered = powered;
        IBlockState state = hasWorld() ? getWorld().getBlockState(getPos()) : null;
        open += state != null && state.getValue(LiddedSwitch.OPEN) ? 0.1f : -0.1f;
        powered += state != null && state.getValue(LiddedSwitch.POWERED) ? 0.25f : -0.25f;
        open = Math.max(0f, Math.min(1f, open));
        powered = Math.max(0f, Math.min(1f, powered));
    }
}
