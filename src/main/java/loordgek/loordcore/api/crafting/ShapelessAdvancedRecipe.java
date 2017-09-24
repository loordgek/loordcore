package loordgek.loordcore.api.crafting;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ShapelessRecipe implements IRecipe {

    @Nonnull
    protected ItemStack output = ItemStack.EMPTY;
    protected NonNullList<Object> input = NonNullList.create();

    public ShapelessRecipe(ItemStack result, Object... recipe) {
        {
            output = result.copy();
            for (Object in : recipe) {
                if (in instanceof ItemStack) {
                    input.add(((ItemStack) in).copy());
                } else if (in instanceof Item) {
                    input.add(new ItemStack((Item) in));
                } else if (in instanceof Block) {
                    input.add(new ItemStack((Block) in));
                } else if (in instanceof String) {
                    input.add(OreDictionary.getOres((String) in));
                } else if (in instanceof FluidStack) {
                    input.add(((FluidStack) in).copy());
                } else if (in instanceof OreDictStacksizeAware) {
                    input.add(((OreDictStacksizeAware) in).Copy());
                } else {


                    String ret = "Invalid shapeless ore recipe: ";
                    for (Object tmp : recipe) {
                        ret += tmp + ", ";
                    }
                    ret += output;
                    throw new RuntimeException(ret);
                }
            }
        }
    }


    @Override
    public boolean matches(@Nonnull IRecipeInventory inv, @Nullable World worldIn, @Nullable EntityPlayer player, @Nullable BlockPos pos) {

        NonNullList<Object> required = NonNullList.create();
        required.addAll(input);

        for (int x = 0; x < inv.Size(); x++) {
            ItemStack slot = inv.getStackInSlot(x);

            if (!slot.isEmpty()) {
                boolean inRecipe = false;

                for (Object next : required) {
                    boolean match = false;

                    if (next instanceof ItemStack) {
                        match = OreDictionary.itemMatches((ItemStack) next, slot, false);
                    } else if (next instanceof List) {
                        Iterator<ItemStack> itr = ((List<ItemStack>) next).iterator();
                        while (itr.hasNext() && !match) {
                            match = OreDictionary.itemMatches(itr.next(), slot, false);
                        }
                    }
                    if (next instanceof FluidStack) {

                        IFluidHandler fluidHandler = FluidUtil.getFluidHandler(slot);
                        if (fluidHandler != null) {
                            FluidStack drained = fluidHandler.drain((FluidStack) next, false);
                            if (drained != null) {
                                match = drained.containsFluid((FluidStack) next);
                            }
                        }
                    }

                    if (next instanceof OreDictStacksizeAware) {
                        match = OreDictStacksizeAware.matches(slot, (OreDictStacksizeAware) next);
                    }

                    if (match) {
                        inRecipe = true;
                        required.remove(next);
                        break;
                    }
                }
                if (!inRecipe) {
                    return false;
                }
            }
        }
        return required.isEmpty();
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(IRecipeInventory inv) {
        return getRecipeOutput();
    }

    @Override
    public void onCraft(IRecipeInventory inv, World worldIn, EntityPlayer player, BlockPos pos) {}

    @Override
    public int getRecipeSize() {
        return input.size();
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput() {
        return output.copy();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItemStacks(IRecipeInventory inv) {
        NonNullList<ItemStack> ret = NonNullList.withSize(inv.Size(), ItemStack.EMPTY);
        for (int i = 0; i < ret.size(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                stack = ForgeHooks.getContainerItem(stack);
            }
            ret.set(i, stack);
        }
        return ret;
    }

    @Override
    public List<Object> getInputObjects() {
        return Collections.unmodifiableList(input);
    }

    @Override
    public int getWidth() {
        return -1;
    }

    @Override
    public int getHeight() {
        return -1;
    }

}
