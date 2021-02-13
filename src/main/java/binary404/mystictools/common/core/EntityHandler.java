package binary404.mystictools.common.core;

import binary404.mystictools.client.fx.FXHelper;
import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.effects.IEffectAction;
import binary404.mystictools.common.loot.effects.LootEffect;
import binary404.mystictools.common.loot.effects.UniqueEffect;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketSparkle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = "mystictools")
public class EntityHandler {

    @SubscribeEvent
    public static void arrowHit(ProjectileImpactEvent.Arrow event) {
        if (event.getArrow().getPersistentData().contains("unique")) {
            CompoundNBT compound = event.getArrow().getPersistentData().getCompound("unique");
            String id = compound.getString("id");
            UniqueEffect effect = UniqueEffect.getById(id);
            if (effect != null)
                effect.arrowImpact(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void sleepingCheck(SleepingLocationCheckEvent event) {
        if (event.getEntityLiving().isSleeping() && !event.getEntityLiving().world.isDaytime()) {
            ItemStack stack = event.getEntityLiving().getHeldItemMainhand();
            if (LootItemHelper.hasEffect(stack, LootEffect.SLEEP)) {
                event.setResult(Event.Result.ALLOW);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityAttacked(LivingDamageEvent event) {
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
                        NetworkHandler.sendToNearby(player.world, player, new PacketSparkle(event.getEntityLiving().getPosX() + 0.5, event.getEntityLiving().getPosY() + 0.5, event.getEntityLiving().getPosZ() + 0.5, 0.92F, 0.0F, 0.08F));
                    }
                }
            }
            if (event.getEntityLiving() instanceof PlayerEntity && event.getSource().getTrueSource() instanceof LivingEntity) {
                PlayerEntity player = (PlayerEntity) event.getEntityLiving();
                for (ItemStack stack : player.inventory.armorInventory) {
                    List<LootEffect> effects = LootEffect.getEffectList(stack);
                    for (LootEffect effect : effects) {
                        if (effect.getAction() != null)
                            effect.getAction().handleArmorHit(stack, player, (LivingEntity) event.getSource().getTrueSource());
                    }
                }
            }
            if (event.getEntityLiving() instanceof PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity) event.getEntityLiving();
                for (ItemStack stack : playerEntity.inventory.armorInventory) {
                    List<LootEffect> effects = LootEffect.getEffectList(stack);
                    if (effects.contains(LootEffect.REFLECT)) {
                        float damage = event.getAmount() / 2;
                        event.setAmount(damage);
                        if (event.getSource().getTrueSource() != null) {
                            event.getSource().getTrueSource().attackEntityFrom(new DamageSource("reflect"), damage);
                        }
                    }
                }
            }
        }
    }
}
