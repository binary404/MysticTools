package binary404.mystictools.common.tile;

public class TileCreator extends TileInventory {

    public TileCreator() {
        super(ModTileEntities.CREATOR, 3);
        this.syncedSlots = new int[]{0, 1, 2};
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }
}
