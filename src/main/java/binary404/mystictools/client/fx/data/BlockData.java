package binary404.mystictools.client.fx.data;

import binary404.mystictools.client.fx.ModParticles;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

import java.util.Locale;

public class BlockData implements IParticleData {

    public boolean depth;
    public int maxAge;
    public Block block;

    public BlockData(boolean depth, int maxAge, Block block) {
        this.depth = depth;
        this.maxAge = maxAge;
        this.block = block;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeBoolean(this.depth);
        buffer.writeInt(maxAge);
        buffer.writeItemStack(new ItemStack(this.block));
    }

    @Override
    public ParticleType<?> getType() {
        return ModParticles.BLOCK;
    }

    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%b %i %bl", this.getType().getRegistryName(), this.depth, this.maxAge, this.block);
    }

    public static final IDeserializer<BlockData> DESERIALIZER = new IDeserializer<BlockData>() {
        @Override
        public BlockData deserialize(ParticleType<BlockData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            boolean depth = reader.readBoolean();
            reader.expect(' ');
            Block block = Blocks.STONE;
            int maxAge = reader.readInt();

            return new BlockData(depth, maxAge, block);
        }

        @Override
        public BlockData read(ParticleType<BlockData> particleTypeIn, PacketBuffer buffer) {
            return new BlockData(buffer.readBoolean(), buffer.readInt(), Block.getBlockFromItem(buffer.readItemStack().getItem()));
        }
    };
}
