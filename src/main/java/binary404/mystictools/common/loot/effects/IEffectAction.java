package binary404.mystictools.common.loot.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.util.List;

public interface IEffectAction {
    public void toggleAction(PlayerEntity player, ItemStack stack);

    public void handleHarvest(PlayerEntity player, ItemStack stack, List<ItemStack> drops, BlockPos pos);

    public void handleUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected);

    public ActionResult<ItemStack> handleUse(ActionResult<ItemStack> defaultAction, World world, PlayerEntity player, Hand hand);

    public ITextComponent modificationResponseMessage(PlayerEntity player, ItemStack stack);

    public String getStatusString(ItemStack stack);
}
