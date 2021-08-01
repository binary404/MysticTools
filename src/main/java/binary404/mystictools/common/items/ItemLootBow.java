package binary404.mystictools.common.items;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootRarity;
import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.loot.effects.PotionEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLootBow extends BowItem implements ILootItem {

    public ItemLootBow() {
        super(new Item.Properties().tab(MysticTools.tab));
        /*
        this.addPropertyOverride(new ResourceLocation("ml_pull"), new IItemPropertyGetter() {
            public float call(ItemStack stack, @Nullable World world, @Nullable LivingEntity entity) {
                float pull = 0.0F;

                if (entity == null) {
                    return pull;
                } else {
                    ItemStack itemstack = entity.getActiveItemStack();
                    pull = itemstack != null && itemstack.getItem() == ModItems.loot_bow ? (float) (stack.getUseDuration() - entity.getItemInUseCount()) / 20.0F : 0.0F;

                    return pull;
                }
            }
        });
        this.addPropertyOverride(new ResourceLocation("ml_pulling"), new IItemPropertyGetter() {
            public float call(ItemStack stack, @Nullable World world, @Nullable LivingEntity entity) {
                return entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
         */
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        LootRarity rarity = LootRarity.fromId(LootNbtHelper.getLootStringValue(stack, LootTags.LOOT_TAG_RARITY));

        return rarity == LootRarity.UNIQUE;
    }

    public float getPull(ItemStack stack, Level world, LivingEntity entity) {
        float pull = 0.0F;

        if (entity == null) {
            return pull;
        } else {
            ItemStack itemstack = entity.getUseItem();
            pull = itemstack != null && itemstack.getItem() == ModItems.loot_bow ? (float) (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F : 0.0F;

            return pull;
        }
    }

    public float getPulling(ItemStack stack, Level world, LivingEntity entity) {
        return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
    }


    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return true;
    }

    @Override
    public Component getName(ItemStack stack) {
        return new TextComponent(LootItemHelper.getLootName(stack, super.getName(stack).getString()));
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return LootItemHelper.getMaxDamage(stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        boolean flag = !player.getProjectile(itemStack).isEmpty();

        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemStack, world, player, hand, flag);
        if (ret != null) return ret;

        if (!player.getAbilities().instabuild && !flag) {
            return !flag ? InteractionResultHolder.fail(itemStack) : InteractionResultHolder.pass(itemStack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.success(itemStack);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player) {
            Player entityplayer = (Player) entityLiving;
            boolean flag = entityplayer.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
            ItemStack itemstack = entityplayer.getProjectile(stack);

            int time = this.getUseDuration(stack) - timeLeft;
            time = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, world, entityplayer, time, itemstack != null || flag);
            if (time < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack == null) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float f = getPowerForTime(time);
                float draw_factor = LootNbtHelper.getLootFloatValue(stack, LootTags.LOOT_TAG_DRAWSPEED);
                if (draw_factor > 0)
                    f *= draw_factor;

                if ((double) f < 0.1D) {
                    return;
                }

                if (f > 1.0F) {
                    f = 1.0F;
                }

                int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
                int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
                int m = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack);

                List<PotionEffect> effects = PotionEffect.getPotionlist(stack);

                int s = 1;

                boolean flag1 = entityplayer.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem ? ((ArrowItem) itemstack.getItem()).isInfinite(itemstack, stack, entityplayer) : false);
                ArrowItem itemarrow = ((ArrowItem) (itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW));
                AbstractArrow entityarrow = itemarrow.createArrow(world, itemstack, entityplayer);


                if (effects.size() > 0) {
                    for (PotionEffect effect : effects) {
                        MobEffectInstance potionEffect = effect.getPotionEffect(PotionEffect.getDurationFromStack(stack, effect.getId()), PotionEffect.getAmplifierFromStack(stack, effect.getId()));
                        if (potionEffect != null && entityarrow instanceof Arrow) {
                            ((Arrow) entityarrow).addEffect(potionEffect);
                        }
                    }

                    entityarrow.pickup = Arrow.Pickup.CREATIVE_ONLY;
                }

                entityarrow.shootFromRotation(entityplayer, entityplayer.getXRot(), entityplayer.getYRot(), 0.0F, f * 3.0F, 1.0F);

                if (f == 1.0F)
                    entityarrow.setCritArrow(true);
                if (j > 0)
                    entityarrow.setBaseDamage(entityarrow.getBaseDamage() + (double) j * 0.5D + 0.5D);
                if (k > 0)
                    entityarrow.setKnockback(k);
                if (m > 0)
                    entityarrow.setSecondsOnFire(100);

                entityarrow.setBaseDamage(entityarrow.getBaseDamage() * LootNbtHelper.getLootFloatValue(stack, LootTags.LOOT_TAG_POWER));

                stack.hurtAndBreak(1, entityplayer, (p_220009_1_) -> {
                    p_220009_1_.broadcastBreakEvent(entityplayer.getUsedItemHand());
                });

                CompoundTag nbt = stack.getTag().getCompound(LootTags.LOOT_TAG).getCompound(LootTags.LOOT_TAG_UNIQUE);
                if (nbt != null)
                    entityarrow.getPersistentData().put("unique", nbt);

                world.addFreshEntity(entityarrow);

                world.playSound(null,
                        entityplayer.getX(),
                        entityplayer.getY(),
                        entityplayer.getZ(),
                        SoundEvents.ARROW_SHOOT,
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F / (entityplayer.level.random.nextFloat() * 0.4F + 1.2F) * 0.5F);
                if (!flag1)
                    itemstack.shrink(1);

                if (itemstack.isEmpty()) {
                    entityplayer.getInventory().removeItem(itemstack);
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (Screen.hasShiftDown()) {
            LootItemHelper.addInformation(stack, tooltip);
        }

        tooltip.add(new TextComponent(ChatFormatting.RESET + "" + "Bow"));

        tooltip.add(new TextComponent(ChatFormatting.GRAY + "Draw speed modifier " + ChatFormatting.BOLD + "" + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(LootNbtHelper.getLootFloatValue(stack, LootTags.LOOT_TAG_DRAWSPEED))));
        tooltip.add(new TextComponent("Power multiplier " + ChatFormatting.BOLD + "" + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(LootNbtHelper.getLootFloatValue(stack, LootTags.LOOT_TAG_POWER))));
    }
}
