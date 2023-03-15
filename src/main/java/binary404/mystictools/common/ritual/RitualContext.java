package binary404.mystictools.common.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

import java.util.HashMap;
import java.util.Map;

public class RitualContext {

    public static final RitualContext EMPTY = new RitualContext(LazyOptional.empty(), BlockPos.ZERO, null);

    private LazyOptional<Ritual> ritual;
    private BlockPos pos;
    private Level level;

    private int ticks = 0;

    //Integer refers to the index of the module
    private Map<Integer, Map<String, Object>> ritualData = new HashMap();

    public RitualContext(Ritual ritual, BlockPos pos, Level level) {
        this(LazyOptional.of(() -> ritual), pos, level);
    }

    public RitualContext(ResourceLocation ritual, BlockPos pos, Level level) {
        this(LazyOptional.of(() -> Ritual.getRitual(ritual, level)), pos, level);
    }

    public RitualContext(LazyOptional<Ritual> ritual, BlockPos pos, Level level) {
        this.ritual = ritual;
        this.pos = pos;
        this.level = level;
    }

    public LazyOptional<Ritual> getRitual() {
        return ritual;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Level getLevel() {
        return level;
    }

    public Map<Integer, Map<String, Object>> getRitualData() {
        return ritualData;
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        if(ritual.isPresent()) {
            tag.putString("ritual", Ritual.getRegistryId(ritual.orElse(Ritual.EMPTY), level).toString());
        }
        tag.putInt("posX", pos.getX());
        tag.putInt("posY", pos.getY());
        tag.putInt("posZ", pos.getZ());
        tag.putInt("ticks", ticks);

        return tag;
    }

    public static RitualContext deserialize(CompoundTag tag, Level level) {
        BlockPos pos = new BlockPos(tag.getInt("posX"), tag.getInt("posY"), tag.getInt("posZ"));
        int ticks = tag.getInt("ticks");
        LazyOptional<Ritual> ritual = LazyOptional.empty();
        if(tag.contains("ritual")) {
            ritual = LazyOptional.of(() -> Ritual.getRitual(new ResourceLocation(tag.getString("ritual")), level));
        }
        RitualContext result = new RitualContext(ritual, pos, level);
        result.ticks = ticks;
        return result;
    }

}
