package loordgek.loordcore.api.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface IRecipe {

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    boolean matches(@Nonnull IRecipeInventory inv, @Nullable World worldIn, @Nullable EntityPlayer player, @Nullable BlockPos pos);

    /**
     * Returns an Item that is the result of this recipe
     */
    @Nonnull
    ItemStack getCraftingResult(IRecipeInventory inv);

    void onCraft(IRecipeInventory inv, World worldIn, EntityPlayer player, BlockPos pos);

    /**
     * Returns the size of the recipe area
     */
    int getRecipeSize();

    @Nonnull
    ItemStack getRecipeOutput();

    NonNullList<ItemStack> getRemainingItemStacks(IRecipeInventory inv);

    List<Object> getInputObjects();

    /**
     * @return width of the crafting ingredients in the crafting table
     */
    int getWidth();

    /**
     * @return height of the crafting ingredients in the crafting table
     */
    int getHeight();
}
