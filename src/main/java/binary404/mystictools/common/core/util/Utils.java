package binary404.mystictools.common.core.util;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.ObjectUtils;
import sun.misc.Unsafe;

import javax.annotation.Nonnull;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;


public class Utils {

    public static Vec3d calculateVelocity(final Vec3d from, final Vec3d to, final double heightGain, final double gravity) {
        final double endGain = to.y - from.y;
        final double horizDist = Math.sqrt(distanceSquared2d(from, to));
        final double gain = heightGain;
        final double maxGain = (gain > endGain + gain) ? gain : (endGain + gain);
        final double a = -horizDist * horizDist / (4.0 * maxGain);
        final double b = horizDist;
        final double c = -endGain;
        final double slope = -b / (2.0 * a) - Math.sqrt(b * b - 4.0 * a * c) / (2.0 * a);
        final double vy = Math.sqrt(maxGain * gravity);
        final double vh = vy / slope;
        final double dx = to.x - from.x;
        final double dz = to.z - from.z;
        final double mag = Math.sqrt(dx * dx + dz * dz);
        final double dirx = dx / mag;
        final double dirz = dz / mag;
        final double vx = vh * dirx;
        final double vz = vh * dirz;
        return new Vec3d(vx, vy, vz);
    }

    public static double distanceSquared2d(final Vec3d from, final Vec3d to) {
        final double dx = to.x - from.x;
        final double dz = to.z - from.z;
        return dx * dx + dz * dz;
    }

    public static double distanceSquared3d(final Vec3d from, final Vec3d to) {
        final double dx = to.x - from.x;
        final double dy = to.y - from.y;
        final double dz = to.z - from.z;
        return dx * dx + dy * dy + dz * dz;
    }

    @Nonnull
    public static Optional<ItemStack> findSmeltingResult(World world, ItemStack input) {
        RecipeManager mgr = world.getRecipeManager();
        IInventory inv = new Inventory(input);
        Optional<IRecipe<IInventory>> optRecipe = (Optional<IRecipe<IInventory>>) ObjectUtils.firstNonNull(
                mgr.getRecipe(IRecipeType.SMELTING, inv, world),
                mgr.getRecipe(IRecipeType.CAMPFIRE_COOKING, inv, world),
                mgr.getRecipe(IRecipeType.SMOKING, inv, world),
                Optional.empty());
        return optRecipe.map(recipe -> recipe.getCraftingResult(inv).copy());
    }

    public static void writeCompoundNBTToBuffer(PacketBuffer bb, CompoundNBT nbt) {
        if (nbt == null) {

            bb.writeByte(0);
        } else {

            try {

                CompressedStreamTools.write(nbt, (DataOutput) new ByteBufOutputStream(bb));
            } catch (IOException ioexception) {

                throw new EncoderException(ioexception);
            }
        }
    }

    public static CompoundNBT readCompoundNBTFromBuffer(PacketBuffer bb) {
        int i = bb.readerIndex();
        byte b0 = bb.readByte();

        if (b0 == 0) {
            return null;
        }

        bb.readerIndex(i);
        try {
            return CompressedStreamTools.read((DataInput) new ByteBufInputStream(bb), new NBTSizeTracker(2097152L));
        } catch (IOException iOException) {

            return null;
        }
    }

}
