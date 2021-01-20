package mods.grissess.ls.net;

import io.netty.buffer.ByteBuf;
import mods.grissess.ls.block.te.LocksmithWorkbenchTE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class Channel {
    public enum PacketKind {
        SET_CUTS,
        TAKE_RESULT,
    }

    public abstract static class Packet implements IMessage {
        public abstract PacketKind getKind();
    }
    public abstract static class Handler<T extends IMessage> implements IMessageHandler<T, IMessage> {
        public abstract PacketKind getKind();
    }

    public static class PacketSetCuts extends Packet {
        public World world;
        public BlockPos pos;
        public int[] cuts;

        @Override
        public PacketKind getKind() {
            return PacketKind.SET_CUTS;
        }

        public PacketSetCuts() {}

        public PacketSetCuts(World world, BlockPos pos, int[] cuts) {
            this.world = world;
            this.pos = pos;
            this.cuts = cuts;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            world = DimensionManager.getWorld(buf.readInt());
            int x = buf.readInt();
            int y = buf.readInt();
            int z = buf.readInt();
            pos = new BlockPos(x, y, z);
            int len = buf.readInt();
            cuts = new int[len];
            for(int i = 0; i < len; i++) {
                cuts[i] = buf.readInt();
            }
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(world.provider.getDimension());
            buf.writeInt(pos.getX());
            buf.writeInt(pos.getY());
            buf.writeInt(pos.getZ());
            buf.writeInt(cuts.length);
            for(int i = 0; i < cuts.length; i++) {
                buf.writeInt(cuts[i]);
            }
        }
    }

    public static class HandlerSetCuts extends Handler<PacketSetCuts> {

        @Override
        public PacketKind getKind() {
            return PacketKind.SET_CUTS;
        }

        @Override
        public IMessage onMessage(PacketSetCuts pkt, MessageContext ctx) {
            ((WorldServer) ctx.getServerHandler().player.world).addScheduledTask(() -> {
                if (!pkt.world.isBlockLoaded(pkt.pos)) return;

                TileEntity te = pkt.world.getTileEntity(pkt.pos);
                if (!(te instanceof LocksmithWorkbenchTE)) return;

                LocksmithWorkbenchTE lwte = (LocksmithWorkbenchTE) te;
                lwte.setAllCuts(pkt.cuts);
                lwte.markDirty();
                IBlockState state = pkt.world.getBlockState(pkt.pos);
                pkt.world.notifyBlockUpdate(pkt.pos, state, state, 2);
            });
            return null;
        }
    }

    public static class PacketTakeResult extends Packet {
        public World world;
        public BlockPos pos;

        public PacketTakeResult() {}

        public PacketTakeResult(World world, BlockPos pos) {
            this.world = world;
            this.pos = pos;
        }

        @Override
        public PacketKind getKind() {
            return PacketKind.TAKE_RESULT;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            world = DimensionManager.getWorld(buf.readInt());
            int x = buf.readInt();
            int y = buf.readInt();
            int z = buf.readInt();
            pos = new BlockPos(x, y, z);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(world.provider.getDimension());
            buf.writeInt(pos.getX());
            buf.writeInt(pos.getY());
            buf.writeInt(pos.getZ());
        }
    }

    public static class HandlerTakeResult extends Handler<PacketTakeResult> {

        @Override
        public PacketKind getKind() {
            return PacketKind.TAKE_RESULT;
        }

        @Override
        public IMessage onMessage(PacketTakeResult pkt, MessageContext ctx) {
            ((WorldServer) ctx.getServerHandler().player.world).addScheduledTask(() -> {
                if(!pkt.world.isBlockLoaded(pkt.pos)) return;

                TileEntity te = pkt.world.getTileEntity(pkt.pos);
                if(!(te instanceof LocksmithWorkbenchTE)) return;
                LocksmithWorkbenchTE lwte = (LocksmithWorkbenchTE) te;

                ctx.getServerHandler().player.inventory.setItemStack(
                        lwte.getStackInSlot(1).copy()
                );
                lwte.setInventorySlotContents(0, ItemStack.EMPTY);
                lwte.markDirty();
            });
            return null;
        }
    }
}
