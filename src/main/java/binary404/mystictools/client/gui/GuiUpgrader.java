package binary404.mystictools.client.gui;

import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketUpgrader;
import binary404.mystictools.common.tile.TileEntityUpgrader;
import binary404.mystictools.common.tile.UpgraderContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.system.CallbackI;

public class GuiUpgrader extends ContainerScreen<UpgraderContainer> {

    private UpgraderContainer container;

    public GuiUpgrader(UpgraderContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.container = screenContainer;
        this.xSize = 176;
        this.ySize = 168;
    }


    @Override
    protected void init() {
        super.init();
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.addButton(new ButtonBasic(this::reroll, x + 113, y + 18, "", 0, 244, 32, 12));
        this.addButton(new ButtonBasic(this::upgrade, x + 113, y + 62, "", 0, 228, 32, 12));
    }

    public void reroll(Button button) {
        TileEntityUpgrader tileEntity = this.container.tileEntity;

        NetworkHandler.sendToServer(new PacketUpgrader(tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ(), 0));
    }

    public void upgrade(Button button) {
        TileEntityUpgrader tileEntity = this.container.tileEntity;

        NetworkHandler.sendToServer(new PacketUpgrader(tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ(), 1));
    }

    @Override
    public void render(int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.renderBackground();
        super.render(p_230430_2_, p_230430_3_, p_230430_4_);
        this.renderHoveredToolTip(p_230430_2_, p_230430_3_);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        this.minecraft.getTextureManager().bindTexture(new ResourceLocation("mystictools", "textures/gui/upgrader.png"));
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        blit(x, y, 0, 0, 176, 168);
    }

    private class ButtonBasic extends Button {

        int tx, ty;

        public ButtonBasic(IPressable onPress, int x, int y, String text, int tx, int ty, int tw, int th) {
            super(x, y, tw, th, text, onPress);
            this.tx = tx;
            this.ty = ty;
        }

        @Override
        public void render(int p_230431_2_, int p_230431_3_, float p_230431_4_) {
            minecraft.getTextureManager().bindTexture(new ResourceLocation("mystictools", "textures/gui/upgrader.png"));
            this.blit(this.x, this.y, this.tx, this.ty, this.width, this.height);
        }
    }

}
