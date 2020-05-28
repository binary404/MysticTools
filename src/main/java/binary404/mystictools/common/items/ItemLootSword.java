package binary404.mystictools.common.items;

import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.loot.effects.BasicEffect;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.antlr.v4.runtime.misc.MultiMap;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLootSword extends SwordItem implements ILootItem {

    public ItemLootSword() {
        super(ItemTier.DIAMOND, 3, -2.4F, new Item.Properties());

        this.addPropertyOverride(new ResourceLocation("model"), new IItemPropertyGetter() {
            @Override
            public float call(ItemStack p_call_1_, @Nullable World p_call_2_, @Nullable LivingEntity p_call_3_) {
                float model = 1.0F;

                model = LootNbtHelper.getLootIntValue(p_call_1_, LootTags.LOOT_TAG_MODEL);

                return model;
            }
        });
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
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean hit = super.hitEntity(stack, target, attacker);

        LootItemHelper.handleBasicEffectsAfterHit(stack, target, attacker);

        return hit;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {

        Multimap<String, AttributeModifier> multiMap = super.getAttributeModifiers(slot, stack);

        return LootItemHelper.modifiersForStack(slot, stack, multiMap, "Weapon modifier");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        int attackDamage = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_DAMAGE);

        if (Screen.hasShiftDown()) {
            tooltip.add(new StringTextComponent(TextFormatting.WHITE + ItemStack.DECIMALFORMAT.format(attackDamage)));

            List<BasicEffect> basicEffects = BasicEffect.getEffectList(stack);
            for (BasicEffect effect : basicEffects) {
                tooltip.add(new StringTextComponent(TextFormatting.RESET + "- " + effect.getType().getColor() + effect.getId()));
            }
        }
    }
}
