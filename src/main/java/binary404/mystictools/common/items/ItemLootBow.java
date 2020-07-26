package binary404.mystictools.common.items;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.loot.effects.PotionEffect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLootBow extends BowItem implements ILootItem {

    public ItemLootBow() {
        super(new Item.Properties().group(MysticTools.tab));
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

    public float getPull(ItemStack stack, World world, LivingEntity entity) {
        float pull = 0.0F;

        if (entity == null) {
            return pull;
        } else {
            ItemStack itemstack = entity.getActiveItemStack();
            pull = itemstack != null && itemstack.getItem() == ModItems.loot_bow ? (float) (stack.getUseDuration() - entity.getItemInUseCount()) / 20.0F : 0.0F;

            return pull;
        }
    }

    public float getPulling(ItemStack stack, World world, LivingEntity entity) {
        return entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F;
    }


    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return new StringTextComponent(LootItemHelper.getLootName(stack, super.getDisplayName(stack).getString()));
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return LootItemHelper.getMaxDamage(stack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getHeldItem(hand);

        boolean flag = !player.findAmmo(itemStack).isEmpty();

        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemStack, world, player, hand, flag);
        if (ret != null) return ret;

        if (!player.abilities.isCreativeMode && !flag) {
            return !flag ? ActionResult.resultFail(itemStack) : ActionResult.resultPass(itemStack);
        } else {
            player.setActiveHand(hand);
            return ActionResult.resultSuccess(itemStack);
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity entityplayer = (PlayerEntity) entityLiving;
            boolean flag = entityplayer.abilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemstack = entityplayer.findAmmo(stack);

            int time = this.getUseDuration(stack) - timeLeft;
            time = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, world, entityplayer, time, itemstack != null || flag);
            if (time < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack == null) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float f = getArrowVelocity(time);
                float draw_factor = LootNbtHelper.getLootFloatValue(stack, LootTags.LOOT_TAG_DRAWSPEED);
                if (draw_factor > 0)
                    f *= draw_factor;

                if ((double) f < 0.1D) {
                    return;
                }

                if (f > 1.0F) {
                    f = 1.0F;
                }

                int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
                int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
                int m = EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack);

                List<PotionEffect> effects = PotionEffect.getPotionlist(stack);

                int s = 1;

                boolean flag1 = entityplayer.abilities.isCreativeMode || (itemstack.getItem() instanceof ArrowItem ? ((ArrowItem) itemstack.getItem()).isInfinite(itemstack, stack, entityplayer) : false);
                ArrowItem itemarrow = ((ArrowItem) (itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW));
                ArrowEntity entityarrow = (ArrowEntity) itemarrow.createArrow(world, itemstack, entityplayer);

                if (effects.size() > 0) {
                    for (PotionEffect effect : effects) {
                        EffectInstance potionEffect = effect.getPotionEffect(PotionEffect.getDurationFromStack(stack, effect.getId()), PotionEffect.getAmplifierFromStack(stack, effect.getId()));
                        if (potionEffect != null)
                            entityarrow.addEffect(potionEffect);
                    }

                    entityarrow.pickupStatus = ArrowEntity.PickupStatus.CREATIVE_ONLY;
                }

                entityarrow.func_234612_a_(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * 3.0F, 1.0F);

                if (f == 1.0F)
                    entityarrow.setIsCritical(true);
                if (j > 0)
                    entityarrow.setDamage(entityarrow.getDamage() + (double) j * 0.5D + 0.5D);
                if (k > 0)
                    entityarrow.setKnockbackStrength(k);
                if (m > 0)
                    entityarrow.setFire(100);

                entityarrow.setDamage(entityarrow.getDamage() * LootNbtHelper.getLootFloatValue(stack, LootTags.LOOT_TAG_POWER));

                stack.damageItem(1, entityplayer, (p_220009_1_) -> {
                    p_220009_1_.sendBreakAnimation(entityplayer.getActiveHand());
                });

                CompoundNBT nbt = stack.getTag().getCompound(LootTags.LOOT_TAG).getCompound(LootTags.LOOT_TAG_UNIQUE);
                if (nbt != null)
                    entityarrow.getPersistentData().put("unique", nbt);

                world.addEntity(entityarrow);

                world.playSound(null,
                        entityplayer.getPosX(),
                        entityplayer.getPosY(),
                        entityplayer.getPosZ(),
                        SoundEvents.ENTITY_ARROW_SHOOT,
                        SoundCategory.NEUTRAL,
                        1.0F,
                        1.0F / (entityplayer.world.rand.nextFloat() * 0.4F + 1.2F) * 0.5F);
                if (!flag1)
                    itemstack.shrink(1);

                if (itemstack.isEmpty()) {
                    entityplayer.inventory.deleteStack(itemstack);
                }
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (Screen.hasShiftDown()) {
            LootItemHelper.addInformation(stack, tooltip);
        }

        tooltip.add(new StringTextComponent(TextFormatting.RESET + "" + "Bow"));

        tooltip.add(new StringTextComponent(TextFormatting.GRAY + "Draw speed modifier " + TextFormatting.BOLD + "" + ItemStack.DECIMALFORMAT.format(LootNbtHelper.getLootFloatValue(stack, LootTags.LOOT_TAG_DRAWSPEED))));
        tooltip.add(new StringTextComponent("Power multiplier " + TextFormatting.BOLD + "" + ItemStack.DECIMALFORMAT.format(LootNbtHelper.getLootFloatValue(stack, LootTags.LOOT_TAG_POWER))));
    }
}
