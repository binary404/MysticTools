package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.common.loot.effects.IEffectAction;
import com.mojang.datafixers.util.Either;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class LootEffectActionSleep implements IEffectAction {

    @Override
    public boolean hasResponseMessage(Player player, ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> handleUse(InteractionResultHolder<ItemStack> defaultAction, Level world, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown() || player.level.isClientSide) {
            return defaultAction;
        }

        BlockPos pos = player.blockPosition();
        trySleep((ServerPlayer) player);

        return defaultAction;
    }

    public Either<Player.BedSleepingProblem, Unit> trySleep(ServerPlayer player) {
        Player.BedSleepingProblem ret = ForgeEventFactory.onPlayerSleepInBed((Player) player, Optional.empty());
        if (ret != null) {
            return Either.left(ret);
        }

        if (player.isSleeping() || !player.isAlive()) {
            return Either.left(Player.BedSleepingProblem.OTHER_PROBLEM);
        }
        if (player.level.isDay()) {
            return Either.left(Player.BedSleepingProblem.NOT_POSSIBLE_NOW);
        }

        if (!ForgeEventFactory.fireSleepingTimeCheck((Player) player, Optional.empty())) {
            return Either.left(Player.BedSleepingProblem.NOT_POSSIBLE_NOW);
        }

        if (!player.isCreative()) {
            Vec3 vector3d = player.position();
            List<Monster> list = player.level.getEntitiesOfClass(Monster.class, new AABB(vector3d.x() - 8.0D, vector3d.y() - 5.0D, vector3d.z() - 8.0D, vector3d.x() + 8.0D, vector3d.y() + 5.0D, vector3d.z() + 8.0D), entity -> entity.isPreventingPlayerRest((Player) player));
            if (!list.isEmpty()) {
                return Either.left(Player.BedSleepingProblem.NOT_SAFE);
            }
        }

        player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
        if (player.isPassenger()) {
            player.stopRiding();
        }

        try {
            Method setPose1 = ObfuscationReflectionHelper.findMethod(Entity.class, "setPose", new Class[]{Pose.class});
            setPose1.invoke(player, new Object[]{Pose.SLEEPING});
        } catch (Exception exception) {
        }

        player.setSleepingPos(player.blockPosition());
        player.setDeltaMovement(Vec3.ZERO);
        player.hasImpulse = true;


        try {
            ObfuscationReflectionHelper.setPrivateValue(Player.class, player, Integer.valueOf(0), "sleepCounter");
        } catch (ObfuscationReflectionHelper.UnableToFindFieldException unableToFindFieldException) {
        }


        if (player.level instanceof ServerLevel) {
            ((ServerLevel) player.level).updateSleepingPlayerList();
        }


        player.awardStat(Stats.SLEEP_IN_BED);
        CriteriaTriggers.SLEPT_IN_BED.trigger(player);

        ((ServerLevel) player.level).updateSleepingPlayerList();
        return Either.right(Unit.INSTANCE);
    }

    @Override
    public Component modificationResponseMessage(Player player, ItemStack stack) {
        return Component.literal("");
    }

    @Override
    public String getStatusString(ItemStack stack) {
        return "";
    }
}
