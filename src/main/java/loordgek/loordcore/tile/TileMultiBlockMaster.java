package loordgek.loordcore.tile;

import loordgek.loordcore.network.DescSync;
import loordgek.loordcore.network.NetworkHandler;
import loordgek.loordcore.network.PacketDescription;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class TileMultiBlockMaster<M extends TileMultiBlockMaster> extends TileMultiBlock<M> {

    protected final List<? extends MultiBlockPattern> patterns;
    @DescSync
    private int currentPatternindex;
    public final List<TileMultiBlock> components = new LinkedList<>();

    protected TileMultiBlockMaster(List<? extends MultiBlockPattern> patterns) {
        super();
        this.patterns = patterns;
    }

    public List<TileMultiBlock> getComponents() {
        return Collections.unmodifiableList(components);
    }

    @Nullable
    public MultiBlockPattern getCurrentPattern() {
        return patterns.get(currentPatternindex);
    }

    @Nullable
    public <T> T getMasterCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing, TileMultiBlock tileMultiBlock) {
        return null;
    }

    public boolean hasMasterCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing, TileMultiBlock tileMultiBlock) {
        return false;
    }

    public boolean testMultiBlock() {
        for (int i = 0; i < patterns.size(); i++) {
            if (patterns.get(i).isValidMultiBlock(this)) {
                currentPatternindex = i;
                NetworkHandler.sendNetworkPacketTile(new PacketDescription(this), this);
                for (TileMultiBlock tileMultiBlock : components) {
                    tileMultiBlock.setMaster(this);
                    tileMultiBlock.setHasMaster(true);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void onNeighborChange(BlockPos neighbor) {
        testMultiBlock();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return hasMasterCapability(capability, facing, this);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return getMasterCapability(capability, facing, this);
    }

    protected void inValidateMultiBlock() {
        for (TileMultiBlock tileMultiBlock : components) {
            tileMultiBlock.setMaster(null);
            tileMultiBlock.setHasMaster(false);
        }
        components.clear();
    }

    public boolean ExtraValidation(MultiBlockPattern multiBlockPattern) {
        return true;
    }
}

