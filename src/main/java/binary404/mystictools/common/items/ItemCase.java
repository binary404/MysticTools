package binary404.mystictools.common.items;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.core.UniqueHandler;
import binary404.mystictools.common.gamestages.GameStageHandler;
import binary404.mystictools.common.loot.*;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketOpenCrateFX;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemCase extends Item {

    public ItemCase() {
        super(new Properties().group(MysticTools.tab));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (worldIn.isRemote)
            return super.onItemRightClick(worldIn, playerIn, handIn);
        ServerWorld serverWorld = ServerLifecycleHooks.getCurrentServer().getWorld(worldIn.func_234923_W_());
        ItemStack stack = playerIn.getHeldItem(handIn);

        LootRarity lootRarity = LootRarity.generateRandomRarity(worldIn.rand, playerIn);

        if (ModList.get().isLoaded("gamestages")) {
            if (!GameStageHandler.RARITY_MAP.isEmpty()) {
                for (LootRarity rarity : GameStageHandler.RARITY_MAP.keySet()) {
                    if (lootRarity == rarity && GameStageHandler.isRarityAllowed(playerIn, lootRarity)) {
                        break;
                    } else if (GameStageHandler.isRarityAllowed(playerIn, rarity) && random.nextInt(2) == 0) {
                        lootRarity = rarity;
                        break;
                    } else {
                        lootRarity = LootRarity.generateRandomRarity(worldIn.rand, playerIn);
                    }
                }
            }
            if (!GameStageHandler.isRarityAllowed(playerIn, lootRarity)) {
                lootRarity = null;
            }
        }

        ItemStack loot;
        if (lootRarity != null) {
            if (lootRarity == LootRarity.UNIQUE) {
                loot = UniqueHandler.getRandomUniqueItem(serverWorld);
            } else {
                loot = LootItemHelper.getRandomLoot(random, lootRarity);

                LootSet.LootSetType type = LootItemHelper.getItemType(loot.getItem());

                if (type == null)
                    type = LootSet.LootSetType.SWORD;

                loot = LootItemHelper.generateLoot(lootRarity, type, loot);
            }
            playerIn.dropItem(loot, false, true);
            stack.shrink(1);
            NetworkHandler.sendToNearby(worldIn, playerIn.getPosition(), new PacketOpenCrateFX(playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ()));
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
