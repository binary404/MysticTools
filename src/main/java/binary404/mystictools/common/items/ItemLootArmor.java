package binary404.mystictools.common.items;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.loot.effects.LootEffect;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketJump;
import com.blamejared.crafttweaker.impl.network.PacketHandler;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class ItemLootArmor extends ArmorItem implements ILootItem {

    String type;

    public ItemLootArmor(EquipmentSlotType type, String pieceType) {
        super(ArmorMaterial.DIAMOND, type, new Item.Properties().group(MysticTools.tab));
        this.type = pieceType;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = LootItemHelper.modifiersForStack(slot, this.slot, stack, HashMultimap.create(), "Armor modifier");

        return modifiers;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return new StringTextComponent(LootItemHelper.getLootName(stack, super.getDisplayName(stack).getString()));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (Screen.hasShiftDown()) {
            LootItemHelper.addInformation(stack, tooltip, false);
        } else {
            EquipmentSlotType type = this.slot;
            tooltip.add(new StringTextComponent(TextFormatting.RESET + "" + this.type));
            Multimap<Attribute, AttributeModifier> multimap = stack.getAttributeModifiers(type);
            if (!multimap.isEmpty() && type != EquipmentSlotType.MAINHAND) {
                for (Map.Entry<Attribute, AttributeModifier> entry : multimap.entries()) {
                    if (entry.getKey().equals(Attributes.ARMOR_TOUGHNESS) || entry.getKey().equals(Attributes.ARMOR)) {
                        AttributeModifier modifier = entry.getValue();
                        double d0 = modifier.getAmount();

                        double d1;

                        if (modifier.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && modifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL)
                            d1 = d0;
                        else
                            d1 = d0 * 100.0D;

                        if (d0 > 0.0D) {
                            tooltip.add(new StringTextComponent(TextFormatting.BLUE + " " + I18n.format("attribute.modifier.plus." + modifier.getOperation().getId(), ItemStack.DECIMALFORMAT.format(d1), I18n.format(entry.getKey().getAttributeName()))));
                        } else if (d0 < 0.0D) {
                            d1 = d1 * -1.0D;
                            tooltip.add(new StringTextComponent(TextFormatting.RED + " " + I18n.format("attribute.modifier.take." + modifier.getOperation().getId(), ItemStack.DECIMALFORMAT.format(d1), I18n.format(entry.getKey().getAttributeName()))));
                        }
                    }
                }
            }
        }

    }

    private static int timesJumped;
    private static boolean jumpDown;

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        LootItemHelper.handlePotionEffects(stack, null, (LivingEntity) player);

        List<LootEffect> effects = LootEffect.getEffectList(stack);

        if (effects.contains(LootEffect.getById("jump"))) {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                if (player == Minecraft.getInstance().player) {
                    ClientPlayerEntity playerSp = (ClientPlayerEntity) player;

                    if (playerSp.isOnGround()) {
                        timesJumped = 0;
                    } else {
                        if (playerSp.movementInput.jump) {
                            if (!jumpDown && timesJumped < LootEffect.getAmplifierFromStack(stack, "jump")) {
                                playerSp.jump();
                                NetworkHandler.sendToServer(new PacketJump());
                                timesJumped++;
                            }
                            jumpDown = true;
                        } else {
                            jumpDown = false;
                        }
                    }
                }
            });
        }

        for(LootEffect effect : effects) {
            if(effect.getAction() != null) {
                effect.getAction().handleUpdate(stack, world, player, 0, false);
            }
        }
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        int id = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_MODEL);
        String texture = "mystictools:textures/models/armor/1.png";

        if (id > 0)
            texture = "mystictools:textures/models/armor/" + id;

        if (stack.getItem() == ModItems.loot_leggings)
            texture = texture + "_2";

        texture += ".png";

        return texture;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return LootItemHelper.getMaxDamage(stack);
    }
}
