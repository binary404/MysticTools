package binary404.mystictools.common.core;

import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.loot.effects.IEffectAction;
import binary404.mystictools.common.loot.effects.LootEffect;
import binary404.mystictools.common.loot.effects.UniqueEffect;
import com.tfar.additionalevents.event.DropLootEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = "mystictools")
public class EntityHandler {

    @SubscribeEvent
    public static void entityHit(ProjectileImpactEvent.Arrow event) {
        if (event.getArrow().getPersistentData().contains("unique")) {
            CompoundNBT compound = event.getArrow().getPersistentData().getCompound("unique");
            String id = compound.getString("id");
            UniqueEffect effect = UniqueEffect.getById(id);
            if (effect != null)
                effect.arrowImpact(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        Entity sourceEntity = event.getSource().getTrueSource();

        if (sourceEntity != null && !sourceEntity.world.isRemote) {
            if (sourceEntity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) sourceEntity;

                if (player.getHeldItemMainhand().getItem() == ModItems.loot_sword) {
                    ItemStack stack = player.getHeldItemMainhand();

                    List<LootEffect> effects = LootEffect.getEffectList(stack);

                    if (effects.contains(LootEffect.getById("leech"))) {
                        float damageInflicted = Math.min(event.getAmount(), event.getEntityLiving().getHealth());
                        int amplifier = LootEffect.getAmplifierFromStack(stack, "leech");
                        float leech = damageInflicted * ((float) amplifier / 100.0F);
                        player.heal(leech);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onHarvest(DropLootEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player != null) {
            List<Item> tools = new ArrayList<Item>();
            tools.add(ModItems.loot_axe);
            tools.add(ModItems.loot_pickaxe);
            tools.add(ModItems.loot_shovel);

            ItemStack tool = player.getHeldItemMainhand();

            if (tools.contains(tool.getItem())) {
                List<LootEffect> effects = LootEffect.getEffectList(tool);

                for (LootEffect effect : effects) {
                    IEffectAction action = effect.getAction();
                    if (action != null) {
                        action.handleHarvest(player, tool, event.getDrops());
                    }
                }
            }
        }
    }

}
