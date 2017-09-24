package loordgek.loordcore.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class TileUtil {

    @Nullable
    public static <T> T getTileEntity(IBlockAccess world, BlockPos pos, Class<T> tClass) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return tClass.isInstance(tileEntity) ? (T) tileEntity : null;
    }

    public static <T> void tileConsumer(Consumer<T> consumer, IBlockAccess world, BlockPos pos, Class<T> tClass){
        T tile = getTileEntity(world, pos, tClass);
        if (tile != null){
            consumer.accept(tile);
        }
    }
}
