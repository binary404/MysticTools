package binary404.mystictools.common.core;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketToggle;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;


@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientHandler {

    @SubscribeEvent
    public static void keyboardEvent(InputEvent.KeyInputEvent event) {
        if(KeyBindings.activateEffect.consumeClick()) {
            NetworkHandler.sendToServer(new PacketToggle());
        }
    }

    public static class KeyBindings {
        private static final String KEYBINDING_CATEGORY_ID = "mystictools" + ".key.categories.loot";

        public static final KeyMapping activateEffect = new KeyMapping("mystictools" + ".key.activateeffect", KeyConflictContext.UNIVERSAL, InputConstants.getKey(GLFW.GLFW_KEY_LEFT_ALT, 0), KEYBINDING_CATEGORY_ID);

        public static void init()
        {
            ClientRegistry.registerKeyBinding(activateEffect);
        }
    }

}
