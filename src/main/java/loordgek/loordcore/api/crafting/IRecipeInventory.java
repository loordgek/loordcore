package loordgek.loordcore.api.crafting;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IRecipeInventory {
    @Nonnull
    ItemStack getStackInSlot(int index);

    /**
     * Gets the ItemStack in the slot specified.
     */
    @Nonnull
    default ItemStack getStackInRowAndColumn(int row, int column)
    {
        return row >= 0 && row < this.Width() && column >= 0 && column <= this.Height() ? this.getStackInSlot(row + column * this.Width()) : ItemStack.EMPTY;
    }
    int Width();
    int Height();
}
