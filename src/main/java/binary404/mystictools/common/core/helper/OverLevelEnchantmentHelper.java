package binary404.mystictools.common.core.helper;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Map;

public class OverLevelEnchantmentHelper {

    public static ItemStack enableSilkTouch(ItemStack stack) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        enchantments.put(Enchantments.SILK_TOUCH, 1);
        enchantments.remove(Enchantments.BLOCK_FORTUNE);
        EnchantmentHelper.setEnchantments(enchantments, stack);
        return stack;
    }

    public static ItemStack addFortune(ItemStack stack, int addLevels) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        int fortune = enchantments.getOrDefault(Enchantments.BLOCK_FORTUNE, 0);
        enchantments.put(Enchantments.BLOCK_FORTUNE, fortune + addLevels);
        EnchantmentHelper.setEnchantments(enchantments, stack);
        return stack;
    }

}
