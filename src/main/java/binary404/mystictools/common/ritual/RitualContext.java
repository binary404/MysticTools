package binary404.mystictools.common.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class RitualContext {

    public static final RitualContext EMPTY = new RitualContext((Ritual) null, BlockPos.ZERO, null);

    private Ritual ritual;
    private BlockPos pos;
    private Level level;

    private int ticks = 0;

    //Integer refers to the index of the module
    private Map<Integer, Map<String, Object>> ritualData = new HashMap();

    public RitualContext(Ritual ritual, BlockPos pos, Level level) {
        this.ritual = ritual;
        this.pos = pos;
        this.level = level;
    }

    public RitualContext(ResourceLocation location, BlockPos pos, Level level) {
        this(Ritual.getRitual(location, level), pos, level);
    }

    public Ritual getRitual() {
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
}
