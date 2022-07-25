package binary404.mystictools.common.core.helper;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Mod.EventBusSubscriber
public class BlockDropCaptureHelper {

    private static final Stack<List<ItemEntity>> capturing = new Stack<>();

    @SubscribeEvent
    public static void onDrop(EntityJoinLevelEvent event) {
        if (event.getLevel() instanceof ServerLevel && event.getEntity() instanceof ItemEntity) {
            ItemStack itemStack = ((ItemEntity) event.getEntity()).getItem();
            if (!capturing.isEmpty()) {
                event.setCanceled(true);

                if (!itemStack.isEmpty() && !capturing.isEmpty()) {
                    ((List<ItemEntity>) capturing.peek()).add((ItemEntity) event.getEntity());
                }

                event.getEntity().remove(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    public static void startCapturing() {
        capturing.push(new ArrayList<>());
    }

    public static List<ItemEntity> getCapturedStacksAndStop() {
        return capturing.pop();
    }
}
