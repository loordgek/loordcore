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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ShapedRecipe implements IRecipe {

    protected int width;
    protected int height;
    protected int recipewidth;
    protected int recipeheight;
    protected int size;
    protected boolean mirrored = true;
    protected final NonNullList<Object> inputs = NonNullList.create();
    protected ItemStack output = ItemStack.EMPTY;
    protected Map<Integer, FluidStack> fluidMap = new HashMap<>();

    public ShapedRecipe(int Recipewidth, int Recipeheight, ItemStack output, Object... recipe) {
        this.recipewidth = Recipewidth;
        this.recipeheight = Recipeheight;
        this.size = width * height;
        this.output = output.copy();


        String shape = "";
        int idx = 0;

        if (recipe[idx] instanceof Boolean) {
            mirrored = (Boolean) recipe[idx];
            if (recipe[idx + 1] instanceof Object[]) {
                recipe = (Object[]) recipe[idx + 1];
            } else {
                idx = 1;
            }
        }

        if (recipe[idx] instanceof String[]) {
            String[] parts = ((String[]) recipe[idx++]);

            for (String s : parts) {
                width = s.length();
                shape += s;
            }

            height = parts.length;
        } else {
            while (recipe[idx] instanceof String) {
                String s = (String) recipe[idx++];
                shape += s;
                width = s.length();
                height++;
            }
        }

        if (width * height != shape.length()) {
            String ret = "Invalid shaped ore recipe: ";
            for (Object tmp : recipe) {
                ret += tmp + ", ";
            }
            ret += output;
            throw new RuntimeException(ret);
        }

        HashMap<Character, Object> itemMap = new HashMap<Character, Object>();

        for (; idx < recipe.length; idx += 2) {
            Character chr = (Character) recipe[idx];
            Object in = recipe[idx + 1];

            if (in instanceof ItemStack) {
                itemMap.put(chr, ((ItemStack) in).copy());
            } else if (in instanceof Item) {
                itemMap.put(chr, new ItemStack((Item) in));
            } else if (in instanceof Block) {
                itemMap.put(chr, new ItemStack((Block) in, 1, OreDictionary.WILDCARD_VALUE));
            } else if (in instanceof String) {
                itemMap.put(chr, OreDictionary.getOres((String) in));
            } else if (in instanceof OreDictStacksizeAware) {
                itemMap.put(chr, ((OreDictStacksizeAware) in).Copy());
            } else if (in instanceof FluidStack) {
                FluidStack fluidStack = (FluidStack) in;
                itemMap.put(chr, fluidStack);
            } else {
                String ret = "Invalid shaped ore recipe: ";
                for (Object tmp : recipe) {
                    ret += tmp + ", ";
                }
                ret += output;
                throw new RuntimeException(ret);
            }
        }


        int x = 0;
        for (char chr : shape.toCharArray()) {
            inputs.set(x++, itemMap.get(chr));
        }

    }

    @Override
    public boolean matches(@Nonnull IRecipeInventory inv, @Nullable World worldIn, @Nullable EntityPlayer player, @Nullable BlockPos pos) {
        for (int x = 0; x <= recipewidth - width; x++) {
            for (int y = 0; y <= recipeheight - height; ++y) {
                if (checkMatch(inv, x, y, false)) {
                    return true;
                }

                if (mirrored && checkMatch(inv, x, y, true)) {
                    return true;
                }
            }
        }
        fluidMap.clear();
        return false;
    }

    @SuppressWarnings("unchecked")
    protected boolean checkMatch(IRecipeInventory inv, int startX, int startY, boolean mirror) {
        for (int x = 0; x < recipewidth; x++) {
            for (int y = 0; y < recipeheight; y++) {
                int subX = x - startX;
                int subY = y - startY;
                Object target = null;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height) {
                    if (mirror) {
                        target = inputs.get(width - subX - 1 + subY * width);
                    } else {
                        target = inputs.get(subX + subY * width);
                    }
                }

                ItemStack slot = inv.getStackInRowAndColumn(x, y);

                if (target instanceof ItemStack) {
                    if (!OreDictionary.itemMatches((ItemStack) target, slot, false)) {
                        return false;
                    }
                } else if (target instanceof List) {
                    boolean matched = false;

                    Iterator<ItemStack> itr = ((List<ItemStack>) target).iterator();
                    while (itr.hasNext() && !matched) {
                        matched = OreDictionary.itemMatches(itr.next(), slot, false);
                    }

                    if (!matched) {
                        return false;
                    }
                } else if (target instanceof OreDictStacksizeAware) {
                    OreDictStacksizeAware.matches(slot, (OreDictStacksizeAware) target);

                } else if (target instanceof FluidStack) {
                    boolean matched = false;

                    IFluidHandler fluidHandler = FluidUtil.getFluidHandler(slot);
                    if (fluidHandler != null) {
                        FluidStack drained = fluidHandler.drain((FluidStack) target, false);
                        if (drained != null) {
                            matched = drained.containsFluid((FluidStack) target);
                        }
                    }

                    if (matched)
                        fluidMap.put(x + y * width, (FluidStack) target);
                    else
                        return false;
                } else if (target == null && !slot.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(IRecipeInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public void onCraft(IRecipeInventory inv, World worldIn, EntityPlayer player, BlockPos pos) {

    }

    @Override
    public int getRecipeSize() {
        return size;
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput() {
        return output;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItemStacks(IRecipeInventory inv) {

        NonNullList<ItemStack> ret = NonNullList.withSize(inv.Size(), ItemStack.EMPTY);
        for (int i = 0; i < ret.size(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                FluidStack fluidStack = fluidMap.get(i);
                if (fluidStack != null) {
                    ItemStack container = stack.copy();
                    container.setCount(1);
                    IFluidHandler fluidHandler = FluidUtil.getFluidHandler(container);
                    if (fluidHandler != null) {
                        fluidHandler.drain(fluidStack, true);
                        if (container.getCount() <= 0) {
                            stack = ForgeHooks.getContainerItem(stack);
                        }
                    }
                } else {
                    stack = ForgeHooks.getContainerItem(stack);
                }
                ret.set(i, stack);
            }
        }
        return ret;


    }


    @Override
    public NonNullList<Object> getInputObjects() {
        return inputs;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
