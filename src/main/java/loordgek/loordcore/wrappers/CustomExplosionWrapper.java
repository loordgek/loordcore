package loordgek.loordcore.wrappers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class CustomExplosionWrapper extends Explosion {
    @SideOnly(Side.CLIENT)
    public CustomExplosionWrapper(World worldIn, Entity entityIn, double x, double y, double z, float size, List<BlockPos> affectedPositions) {
        super(worldIn, entityIn, x, y, z, size, affectedPositions);
    }

    @SideOnly(Side.CLIENT)
    public CustomExplosionWrapper(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean causesFire, boolean damagesTerrain, List<BlockPos> affectedPositions) {
        super(worldIn, entityIn, x, y, z, size, causesFire, damagesTerrain, affectedPositions);
    }

    public CustomExplosionWrapper(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean flaming, boolean damagesTerrain) {
        super(worldIn, entityIn, x, y, z, size, flaming, damagesTerrain);
    }

    @Override
    public void doExplosionA() {
    }

    @Override
    public void doExplosionB(boolean spawnParticles) {
    }

    @Nullable
    @Override
    public EntityLivingBase getExplosivePlacedBy() {
        return super.getExplosivePlacedBy();
    }

    @Override
    public List<BlockPos> getAffectedBlockPositions() {
        return super.getAffectedBlockPositions();
    }

    @Override
    public Map<EntityPlayer, Vec3d> getPlayerKnockbackMap() {
        return super.getPlayerKnockbackMap();
    }
}
