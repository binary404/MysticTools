package binary404.mystictools.common.core;

import binary404.mystictools.client.fx.FXHelper;
import binary404.mystictools.common.core.helper.ActiveFlags;
import binary404.mystictools.common.core.helper.BlockDropCaptureHelper;
import binary404.mystictools.common.core.helper.BlockHelper;
import binary404.mystictools.common.core.helper.OverLevelEnchantmentHelper;
import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.loot.effects.IEffectAction;
import binary404.mystictools.common.loot.effects.LootEffect;
import binary404.mystictools.common.loot.effects.LootEffectInstance;
import binary404.mystictools.common.loot.effects.UniqueEffect;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketSparkle;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = "mystictools")
public class EntityHandler {

    @SubscribeEvent
    public static void arrowHit(ProjectileImpactEvent event) {
        if (event.getProjectile() instanceof Arrow) {
            if (event.getProjectile().getPersistentData().contains("unique")) {
                CompoundTag compound = event.getProjectile().getPersistentData().getCompound("unique");
                String id = compound.getString("id");
                UniqueEffect effect = UniqueEffect.getById(id);
                if (effect != null) {
                    Entity entity = event.getProjectile().getOwner();
                    effect.arrowImpact(entity, event.getProjectile());
                }
            }
        }
    }

    @SubscribeEvent
    public static void sleepingCheck(SleepingLocationCheckEvent event) {
        if (event.getEntityLiving().isSleeping() && !event.getEntityLiving().level.isDay()) {
            ItemStack stack = event.getEntityLiving().getMainHandItem();
            if (LootItemHelper.hasEffect(stack, LootEffect.getById("sleep"))) {
                event.setResult(Event.Result.ALLOW);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityAttacked(LivingDamageEvent event) {
        Entity sourceEntity = event.getSource().getEntity();

        if (sourceEntity != null && !sourceEntity.level.isClientSide) {
            if (sourceEntity instanceof Player) {
                Player player = (Player) sourceEntity;

                if (player.getMainHandItem().getItem() == ModItems.loot_sword) {
                    ItemStack stack = player.getMainHandItem();

                    if (LootItemHelper.hasEffect(stack, LootEffect.getById("leech"))) {
                        float damageInflicted = Math.min(event.getAmount(), event.getEntityLiving().getHealth());
                        double amplifier = LootEffect.getAmplifierFromStack(stack, "leech");
                        float leech = damageInflicted * ((float) amplifier / 100.0F);
                        player.heal(leech);
                        NetworkHandler.sendToNearby(player.level, player, new PacketSparkle(event.getEntityLiving().getX() + 0.5, event.getEntityLiving().getY() + 0.5, event.getEntityLiving().getZ() + 0.5, 0.92F, 0.0F, 0.08F));
                    }
                }
            }
            if (event.getEntityLiving() instanceof Player && event.getSource().getEntity() instanceof LivingEntity) {
                Player player = (Player) event.getEntityLiving();
                for (ItemStack stack : player.getInventory().armor) {
                    List<LootEffectInstance> effects = LootEffect.getEffectList(stack);
                    for (LootEffectInstance effect : effects) {
                        if (effect.getEffect().getAction() != null)
                            effect.getEffect().getAction().handleArmorHit(stack, player, (LivingEntity) event.getSource().getEntity());
                    }
                }
            }

            if (event.getEntityLiving() instanceof Player) {
                Player playerEntity = (Player) event.getEntityLiving();
                double parry = 0;
                for (ItemStack stack : playerEntity.getInventory().armor) {
                    if (LootItemHelper.hasEffect(stack, LootEffect.getById("reflect"))) {
                        float damage = event.getAmount() / 2;
                        event.setAmount(damage);
                        if (event.getSource().getEntity() != null) {
                            event.getSource().getEntity().hurt(new DamageSource("reflect"), damage);
                        }
                    }
                    if (LootItemHelper.hasEffect(stack, LootEffect.getById("parry"))) {
                        double chance = LootEffect.getAmplifierFromStack(stack, "parry");
                        parry += chance;
                    }
                }
                if (playerEntity.level.random.nextInt(100) <= parry) {
                    playerEntity.level.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                            SoundEvents.SHIELD_BLOCK,
                            SoundSource.MASTER,
                            1F, 1F);
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onOrbPickup(PlayerXpEvent.PickupXp event) {
        Player player = event.getPlayer();
        for (ItemStack stack : player.getInventory().armor) {
            if (LootItemHelper.hasEffect(stack, LootEffect.getById("insight"))) {
                double level = LootEffect.getAmplifierFromStack(stack, "insight");
                ExperienceOrb orb = event.getOrb();
                orb.value *= (1 + level);
            }
        }
    }

    @SubscribeEvent
    public static void addFortune(BlockEvent.BreakEvent event) {
        ServerPlayer player = (ServerPlayer) event.getPlayer();
        ItemStack heldStack = player.getMainHandItem();
        if (LootItemHelper.hasEffect(heldStack, LootEffect.getById("lucky"))) {
            ActiveFlags.IS_LUCKY_MINING.runIfNotSet(() -> {
                int fortuneLevelToAdd = (int) LootEffect.getAmplifierFromStack(heldStack, "lucky");
                ServerLevel level = (ServerLevel) event.getWorld();
                ItemStack miningStack = OverLevelEnchantmentHelper.addFortune(heldStack.copy(), fortuneLevelToAdd);
                BlockPos pos = event.getPos();

                BlockDropCaptureHelper.startCapturing();
                try {
                    BlockHelper.breakBlock(level, player, pos, level.getBlockState(pos), miningStack, true, true);
                    BlockHelper.damageMiningItem(heldStack, player, 1);
                } finally {
                    BlockDropCaptureHelper.getCapturedStacksAndStop()
                            .forEach((item) -> Block.popResource((Level) level, (BlockPos) item.blockPosition(), (ItemStack) item.getItem()));
                }
                event.setCanceled(true);
            });
        }
    }

    @SubscribeEvent
    public static void direct(BlockEvent.BreakEvent event) {
        ServerPlayer player = (ServerPlayer) event.getPlayer();
        ItemStack heldStack = player.getMainHandItem();
        if (LootItemHelper.hasEffect(heldStack, LootEffect.getById("direct"))) {
            ActiveFlags.IS_DIRECT_MINING.runIfNotSet(() -> {
                ServerLevel level = (ServerLevel) event.getWorld();
                BlockPos pos = event.getPos();

                BlockDropCaptureHelper.startCapturing();
                try {
                    BlockHelper.breakBlock(level, player, pos, level.getBlockState(pos), heldStack.copy(), true, true);
                    BlockHelper.damageMiningItem(heldStack, player, 1);
                } finally {
                    BlockDropCaptureHelper.getCapturedStacksAndStop()
                            .forEach((item) -> {
                                boolean flag = player.addItem(item.getItem());
                                if (!flag) {
                                    Block.popResource(level, item.blockPosition(), item.getItem());
                                }
                            });
                }
                event.setCanceled(true);
            });
        }
    }
}
