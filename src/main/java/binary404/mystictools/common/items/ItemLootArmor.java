package binary404.mystictools.common.items;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.items.attribute.ModAttributes;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.loot.effects.LootEffect;
import binary404.mystictools.common.loot.effects.LootEffectInstance;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketJump;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ItemLootArmor extends ArmorItem implements ILootItem {

    String type;

    public ItemLootArmor(EquipmentSlot type, String pieceType) {
        super(ArmorMaterials.DIAMOND, type, new Item.Properties().tab(MysticTools.tab));
        this.type = pieceType;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = LootItemHelper.modifiersForStack(slot, this.slot, stack, super.getAttributeModifiers(equipmentSlot, stack));
        return modifiers;
    }

    @Override
    public Component getName(ItemStack stack) {
        return new TextComponent(LootItemHelper.getLootName(stack, super.getName(stack).getString()));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (Screen.hasShiftDown()) {
            LootItemHelper.addInformation(stack, tooltip, false);
        } else {
            EquipmentSlot type = this.slot;
            tooltip.add(new TextComponent(ChatFormatting.RESET + "" + this.type));
            Multimap<Attribute, AttributeModifier> multimap = getAttributeModifiers(type, stack);
            if (!multimap.isEmpty() && type != EquipmentSlot.MAINHAND) {
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
                            tooltip.add(new TextComponent(ChatFormatting.BLUE + " " + I18n.get("attribute.modifier.plus." + modifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), I18n.get(entry.getKey().getDescriptionId()))));
                        } else if (d0 < 0.0D) {
                            d1 = d1 * -1.0D;
                            tooltip.add(new TextComponent(ChatFormatting.RED + " " + I18n.get("attribute.modifier.take." + modifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), I18n.get(entry.getKey().getDescriptionId()))));
                        }
                    }
                }
            }
        }

    }

    private static int timesJumped;
    private static boolean jumpDown;

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        LootItemHelper.handlePotionEffects(stack, null, (LivingEntity) player);

        if (LootItemHelper.hasEffect(stack, LootEffect.getById("jump"))) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                if (player == Minecraft.getInstance().player) {
                    LocalPlayer playerSp = (LocalPlayer) player;

                    if (playerSp.isOnGround()) {
                        timesJumped = 0;
                    } else {
                        if (playerSp.input.jumping) {
                            if (!jumpDown && timesJumped < LootEffect.getAmplifierFromStack(stack, "jump")) {
                                playerSp.jumpFromGround();
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

        List<LootEffectInstance> effects = LootEffect.getEffectList(stack);

        for (LootEffectInstance effect : effects) {
            if (effect.getEffect().getAction() != null) {
                effect.getEffect().getAction().handleUpdate(stack, world, player, 0, false);
            }
        }
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        int id = ModAttributes.LOOT_MODEL.getOrDefault(stack, 0).getValue(stack);

        String texture = "mystictools:textures/models/armor/1";

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
