package binary404.mystictools.common.core.util;

import com.sun.javafx.geom.Vec3d;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import net.minecraft.entity.Entity;
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
import net.minecraft.world.World;
import org.apache.commons.lang3.ObjectUtils;
import sun.misc.Unsafe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
import java.util.function.Function;
import java.util.function.Predicate;


public class Utils {

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

    public static Predicate<? super Entity> selectEntities(Class<? extends Entity>... entities) {
        return (Predicate<Entity>) entity -> {
            if (entity == null || !entity.isAlive()) return false;
            Class<? extends Entity> clazz = entity.getClass();
            for (Class<? extends Entity> test : entities) {
                if (test.isAssignableFrom(clazz)) return true;
            }
            return false;
        };
    }

    @Nullable
    public static <T> T selectClosest(Collection<T> elements, Function<T, Double> dstFunc) {
        if (elements.isEmpty()) return null;

        double dstClosest = Double.MAX_VALUE;
        T closestElement = null;
        for (T element : elements) {
            double dst = dstFunc.apply(element);
            if (dst < dstClosest) {
                closestElement = element;
                dstClosest = dst;
            }
        }
        return closestElement;
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

    public static ItemStack copyStackWithAmount(ItemStack stack, int amount) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;
        ItemStack s2 = stack.copy();
        s2.setCount(amount);
        return s2;
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
