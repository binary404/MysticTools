package binary404.mystictools.common.items;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.core.UniqueHandler;
import binary404.mystictools.common.items.attribute.ModAttributes;
import binary404.mystictools.common.loot.*;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketFX;
import binary404.mystictools.common.ritual.Ritual;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;


import net.minecraft.world.item.Item.Properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ItemCase extends Item {

    public ItemCase() {
        super(new Properties().tab(MysticTools.tab));
    }

    public List<LootRarity> getRarities(ItemStack stack) {
        List<LootRarity> rarities = new ArrayList<>();
        List<String> strings = ModAttributes.LOOT_CASE_RARITY.getOrCreate(stack, Collections.emptyList()).getValue(stack);
        for (String string : strings) {
            LootRarity rarity;
            if ((rarity = LootRarity.fromId(string)) != null) rarities.add(rarity);
        }
        return rarities;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (worldIn.isClientSide)
            return super.use(worldIn, playerIn, handIn);
        ServerLevel serverWorld = ServerLifecycleHooks.getCurrentServer().getLevel(worldIn.dimension());
        ItemStack stack = playerIn.getItemInHand(handIn);

        LootRarity lootRarity;
        if (getRarities((stack)).isEmpty()) {
            lootRarity = LootRarity.generateRandomRarity(worldIn.random, playerIn);
        } else {
            lootRarity = getRarities(stack).get(new Random().nextInt(getRarities(stack).size()));
        }

        ItemStack loot;
        if (lootRarity != null) {
            if (lootRarity.getId().equals("unique")) {
                loot = UniqueHandler.getRandomUniqueItem(serverWorld, playerIn);
            } else {
                loot = LootItemHelper.getRandomLoot(worldIn.random);

                if (loot.getItem() instanceof ILootItem) {
                    LootSet.LootSetType type = LootItemHelper.getItemType(loot.getItem());

                    if (type == null)
                        type = LootSet.LootSetType.SWORD;

                    loot = LootItemHelper.generateLoot(lootRarity, type, loot);
                }
            }
            playerIn.drop(loot, false, true);
            stack.shrink(1);
            NetworkHandler.sendToNearby(worldIn, playerIn.blockPosition(), new PacketFX(playerIn.getX(), playerIn.getY(), playerIn.getZ(), 0));
            return super.use(worldIn, playerIn, handIn);
        }
        return super.use(worldIn, playerIn, handIn);
    }
}
