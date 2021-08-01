package binary404.mystictools.common.core.util;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.ObjectUtils;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;


public class Utils {

    @Nonnull
    public static Optional<ItemStack> findSmeltingResult(Level world, ItemStack input) {
        RecipeManager mgr = world.getRecipeManager();
        Container inv = new SimpleContainer(input);
        Optional<Recipe<Container>> optRecipe = (Optional<Recipe<Container>>) ObjectUtils.firstNonNull(
                mgr.getRecipeFor(RecipeType.SMELTING, inv, world),
                mgr.getRecipeFor(RecipeType.CAMPFIRE_COOKING, inv, world),
                mgr.getRecipeFor(RecipeType.SMOKING, inv, world),
                Optional.empty());
        return optRecipe.map(recipe -> recipe.assemble(inv).copy());
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

    public static void writeCompoundNBTToBuffer(FriendlyByteBuf bb, CompoundTag nbt) {
        if (nbt == null) {

            bb.writeByte(0);
        } else {

            try {

                NbtIo.write(nbt, (DataOutput) new ByteBufOutputStream(bb));
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

    public static CompoundTag readCompoundNBTFromBuffer(FriendlyByteBuf bb) {
        int i = bb.readerIndex();
        byte b0 = bb.readByte();

        if (b0 == 0) {
            return null;
        }

        bb.readerIndex(i);
        try {
            return NbtIo.read((DataInput) new ByteBufInputStream(bb), new NbtAccounter(2097152L));
        } catch (IOException iOException) {

            return null;
        }
    }

}
