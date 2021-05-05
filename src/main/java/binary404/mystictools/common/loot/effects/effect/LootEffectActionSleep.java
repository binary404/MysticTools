package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.common.loot.effects.IEffectAction;
import com.mojang.datafixers.util.Either;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Unit;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class LootEffectActionSleep implements IEffectAction {

    @Override
    public boolean hasResponseMessage(PlayerEntity player, ItemStack stack) {
        return false;
    }

    @Override
    public ActionResult<ItemStack> handleUse(ActionResult<ItemStack> defaultAction, World world, PlayerEntity player, Hand hand) {
        if (player.isSneaking() || player.world.isRemote) {
            return defaultAction;
        }

        BlockPos pos = player.getPosition();
        trySleep((ServerPlayerEntity) player);

        return defaultAction;
    }

    public Either<PlayerEntity.SleepResult, Unit> trySleep(ServerPlayerEntity player) {
        PlayerEntity.SleepResult ret = ForgeEventFactory.onPlayerSleepInBed((PlayerEntity) player, Optional.empty());
        if (ret != null) {
            return Either.left(ret);
        }

        if (player.isSleeping() || !player.isAlive()) {
            return Either.left(PlayerEntity.SleepResult.OTHER_PROBLEM);
        }
        if (player.world.isDaytime()) {
            return Either.left(PlayerEntity.SleepResult.NOT_POSSIBLE_NOW);
        }

        if (!ForgeEventFactory.fireSleepingTimeCheck((PlayerEntity) player, Optional.empty())) {
            return Either.left(PlayerEntity.SleepResult.NOT_POSSIBLE_NOW);
        }

        if (!player.isCreative()) {
            Vector3d vector3d = player.getPositionVec();
            List<MonsterEntity> list = player.world.getEntitiesWithinAABB(MonsterEntity.class, new AxisAlignedBB(vector3d.getX() - 8.0D, vector3d.getY() - 5.0D, vector3d.getZ() - 8.0D, vector3d.getX() + 8.0D, vector3d.getY() + 5.0D, vector3d.getZ() + 8.0D), entity -> entity.func_230292_f_((PlayerEntity) player));
            if (!list.isEmpty()) {
                return Either.left(PlayerEntity.SleepResult.NOT_SAFE);
            }
        }

        player.takeStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
        if (player.isPassenger()) {
            player.stopRiding();
        }

        try {
            Method setPose1 = ObfuscationReflectionHelper.findMethod(Entity.class, "setPose", new Class[]{Pose.class});
            setPose1.invoke(player, new Object[]{Pose.SLEEPING});
        } catch (Exception exception) {
        }

        player.setBedPosition(player.getPosition());
        player.setMotion(Vector3d.ZERO);
        player.isAirBorne = true;


        try {
            ObfuscationReflectionHelper.setPrivateValue(PlayerEntity.class, player, Integer.valueOf(0), "sleepTimer");
        } catch (net.minecraftforge.fml.common.ObfuscationReflectionHelper.UnableToFindFieldException unableToFindFieldException) {
        }


        if (player.world instanceof ServerWorld) {
            ((ServerWorld) player.world).updateAllPlayersSleepingFlag();
        }


        player.addStat(Stats.SLEEP_IN_BED);
        CriteriaTriggers.SLEPT_IN_BED.trigger(player);

        ((ServerWorld) player.world).updateAllPlayersSleepingFlag();
        return Either.right(Unit.INSTANCE);
    }

    @Override
    public ITextComponent modificationResponseMessage(PlayerEntity player, ItemStack stack) {
        return new StringTextComponent("");
    }

    @Override
    public String getStatusString(ItemStack stack) {
        return "";
    }
}
