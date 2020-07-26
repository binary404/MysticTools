package binary404.mystictools.common.gamestages;

import binary404.mystictools.common.loot.LootRarity;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModList;
import org.openzen.zencode.java.ZenCodeType;

import java.util.HashMap;
import java.util.Map;

@ZenCodeType.Name("mods.MysticTools.GameStages")
@ZenRegister
public class GameStageHandler {

    public static final Map<LootRarity, String> RARITY_MAP = new HashMap<>();
    public static String openStage;

    public static boolean isRarityAllowed(PlayerEntity playerEntity, LootRarity rarity) {
        if (GameStageHelper.isStageKnown(RARITY_MAP.get(rarity))) {
            return GameStageHelper.hasStage(playerEntity, RARITY_MAP.get(rarity));
        }
        return true;
    }

    @ZenCodeType.Method
    public static void addRarityStage(String stage, String rarity) {
        CraftTweakerAPI.apply(new IRuntimeAction() {
            @Override
            public void apply() {
                LootRarity addRarity = LootRarity.fromId(rarity);
                RARITY_MAP.put(addRarity, stage);
            }

            @Override
            public String describe() {
                return "Adding rarity " + rarity + " to stage " + stage;
            }
        });
    }

    @ZenCodeType.Method
    public static void addOpenStage(String stage) {
        CraftTweakerAPI.apply(new IRuntimeAction() {
            @Override
            public void apply() {
                openStage = stage;
            }

            @Override
            public String describe() {
                return "Added open stage " + stage;
            }
        });
    }

}
