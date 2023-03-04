package binary404.mystictools.common.items;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.core.UniqueHandler;
import binary404.mystictools.common.core.config.ModConfigs;
import binary404.mystictools.common.core.helper.util.WeightedList;
import binary404.mystictools.common.items.attribute.ModAttributes;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootRarity;
import binary404.mystictools.common.loot.LootSet;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketFX;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemSelectRarityCase extends Item {

    public ItemSelectRarityCase() {
        super(new Properties());
    }

    public static LootRarity getLootRarity(ItemStack stack) {
        return LootRarity.fromId(ModAttributes.LOOT_RARITY.getOrDefault(stack, "common").getValue(stack));
    }

    public static void setLootRarity(LootRarity rarity, ItemStack stack) {
        ModAttributes.LOOT_RARITY.getOrCreate(stack, rarity.getId()).setBaseValue(rarity.getId());
    }

    @Override
    public Component getName(ItemStack pStack) {
        LootRarity rarity = getLootRarity(pStack);
        return Component.literal(rarity.getColor() + I18n.get(rarity.getId()) + " " + "Loot Case");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (worldIn.isClientSide)
            return super.use(worldIn, playerIn, handIn);

        ServerLevel world = (ServerLevel) worldIn;
        ItemStack stack = playerIn.getItemInHand(handIn);

        ItemStack loot;

        LootRarity lootRarity = getLootRarity(stack);

        if (lootRarity == LootRarity.UNIQUE) {
            loot = UniqueHandler.getRandomUniqueItem(world, playerIn);
        } else {
            loot = LootItemHelper.getRandomLoot(worldIn.random);

            if(loot.getItem() instanceof ILootItem) {
                LootSet.LootSetType type = LootItemHelper.getItemType(loot.getItem());

                if (type == null)
                    type = LootSet.LootSetType.SWORD;

                loot = LootItemHelper.generateLoot(lootRarity, type, loot);
            }
        }
        playerIn.drop(loot, false, true);
        stack.shrink(1);
        NetworkHandler.sendToNearby(worldIn, playerIn, new PacketFX(playerIn.getX(), playerIn.getY(), playerIn.getZ(), 0));
        return super.use(worldIn, playerIn, handIn);
    }
}
