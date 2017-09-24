package loordgek.loordcore.util.item;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtil {

    public static NonNullList<ItemStack> getStacks(IItemHandler itemHandler) {
        NonNullList<ItemStack> itemStacks = NonNullList.withSize(itemHandler.getSlots(), ItemStack.EMPTY);
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            itemStacks.set(i, itemHandler.getStackInSlot(i));
        }
        return itemStacks;
    }

    public static boolean hasItemHanderItems(IItemHandler itemHandler) {
        boolean[] array = new boolean[itemHandler.getSlots()];
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (itemHandler.getStackInSlot(i) == ItemStack.EMPTY) {
                array[i] = true;
            }
        }
        for (boolean anArray : array) {
            if (!anArray)
                return true;
        }
        return false;
    }
    /*
    public static boolean removeSets(IInventory inventory, int count, ItemStack[] set, boolean oreDictionary, boolean doRemove) {
        ItemStack[] stock = getStacks(inventory);

        if (doRemove) {
            ItemStack[] removed = removeSets(inventory, count, set, oreDictionary);
            return removed != null && removed.length >= count;
        } else {
            return containsSets(set, stock, oreDictionary) >= count;
        }
    }

    public static ItemStack[] removeSets(IInventory inventory, int count, ItemStack[] set, boolean oreDictionary) {
        ItemStack[] removed = new ItemStack[set.length];
        ItemStack[] stock = getStacks(inventory);

        if (containsSets(set, stock, oreDictionary) < count) {
            return null;
        }

        for (int i = 0; i < set.length; i++) {
            if (set[i] == null) {
                continue;
            }
            ItemStack stackToRemove = set[i].copy();
            stackToRemove.stackSize *= count;

            // try to remove the exact stack first
            ItemStack removedStack = removeStack(inventory, stackToRemove, false);
            if (removedStack == null) {
                // remove crafting equivalents next
                removedStack = removeStack(inventory, stackToRemove, oreDictionary);
            }

            removed[i] = removedStack;
        }
        return removed;
    }

    private static ItemStack removeStack(IInventory inventory, ItemStack stackToRemove, boolean oreDictionary) {
        for (int j = 0; j < inventory.getSizeInventory(); j++) {
            ItemStack stackInSlot = inventory.getStackInSlot(j);
            ItemStack removed = inventory.decrStackSize(j, stackToRemove.stackSize);
            stackToRemove.stackSize -= removed.stackSize;

            if (stackToRemove.stackSize == 0) {
                return removed;
            }


        }
        return null;
    }*/
    public static ItemStack[] condenseStacks(ItemStack[] stacks) {
    List<ItemStack> condensed = new ArrayList<ItemStack>();

    for (ItemStack stack : stacks) {
        if (stack == null) {
            continue;
        }
        if (stack.stackSize <= 0) {
            continue;
        }

        boolean matched = false;
        for (ItemStack cached : condensed) {
            if (cached.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(cached, stack)) {
                cached.stackSize += stack.stackSize;
                matched = true;
            }
        }

        if (!matched) {
            ItemStack cached = stack.copy();
            condensed.add(cached);
        }
    }

    return condensed.toArray(new ItemStack[condensed.size()]);
    }

    public static int containsSets(ItemStack[] set, ItemStack[] stock, boolean oreDictionary) {
        int totalSets = 0;

        ItemStack[] condensedRequired = condenseStacks(set);
        ItemStack[] condensedOffered = condenseStacks(stock);

        for (ItemStack req : condensedRequired) {

            int reqCount = 0;
            for (ItemStack offer : condensedOffered) {
                if (IsItemStackEqual(req, offer)) {
                    int stackCount = (int) Math.floor(offer.stackSize / req.stackSize);
                    reqCount = Math.max(reqCount, stackCount);
                }
            }

            if (reqCount == 0) {
                return 0;
            } else if (totalSets == 0) {
                totalSets = reqCount;
            } else if (totalSets > reqCount) {
                totalSets = reqCount;
            }
        }

        return totalSets;
    }
    public static boolean IsItemStackEqual(ItemStack stack1, ItemStack stack2) {
        return !(stack1 == null || stack2 == null) && stack1.isItemEqual(stack2) && stack1.getTagCompound() == stack2.getTagCompound();

    }


    public static boolean isBucket(ItemStack stack)
    {
        return stack != null && stack.getItem() != null && stack.getItem() == Items.BUCKET;
    }
}