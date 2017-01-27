package loordgek.loordcore.util.item;

import loordgek.extragenarators.enums.EnumInvFlow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

public class InventorySimpleItemhander implements IItemHandler, IItemHandlerModifiable, INBTSerializable<NBTTagCompound> {


    private final int stacksize;
    private final int invsize;
    private final String name;
    private final IInventoryOnwer onwer;
    private ItemStack[] stacks;

    public InventorySimpleItemhander(int stacksize, int invsize, String name, IInventoryOnwer onwer) {
        this.stacksize = stacksize;
        this.invsize = invsize;
        this.name = name;
        this.onwer = onwer;
        this.stacks = new ItemStack[invsize];
    }

    @Override
    public int getSlots() {
        return invsize;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return stacks[slot];
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack == null)
            return null;

        ItemStack stackinslot = getStackInSlot(slot);

        int m;
        if (stackinslot != null) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, stackinslot))
                return stack;

            m = Math.min(stack.getMaxStackSize(), stacksize - stackinslot.stackSize);
            if (stack.stackSize <= m) {
                if (!simulate) {
                    ItemStack copy = stack.copy();
                    copy.stackSize += stackinslot.stackSize;
                    stacks[slot] = stack;
                    onwer.OnInventoryChanged(stack, slot, name, EnumInvFlow.INSERT);
                    onwer.markDirty();
                }
                return null;

            } else {
                stack = stack.copy();
                if (!simulate) {
                    ItemStack copy = stack.splitStack(m);
                    copy.stackSize += stackinslot.stackSize;
                    stacks[slot] = stack;
                    onwer.OnInventoryChanged(stack, slot, name, EnumInvFlow.INSERT);
                    onwer.markDirty();
                }
            }
        } else {
            m = Math.min(stack.getMaxStackSize(), stacksize);
            if (m < stack.stackSize) {
                stack = stack.copy();
                if (!simulate) {
                    stacks[slot] = stack;
                    onwer.OnInventoryChanged(stack, slot, name, EnumInvFlow.INSERT);
                    onwer.markDirty();
                    return stack;
                } else {
                    stack.stackSize -= m;
                    return stack;
                }
            } else {
                if (!simulate) {
                    stacks[slot] = stack;
                    onwer.OnInventoryChanged(stack, slot, name, EnumInvFlow.INSERT);
                    onwer.markDirty();
                }
                return null;
            }
        }
        return null;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return null;
        ItemStack stackInSlot = stacks[slot];

        if (stackInSlot == null)
            return null;

        if (simulate)
            if (stackInSlot.stackSize < amount)
                return stackInSlot.copy();
            else {
                ItemStack copy = stackInSlot.copy();
                copy.stackSize = amount;
                return copy;
            }
        else {
            int m = Math.min(stackInSlot.stackSize, amount);

            ItemStack decrStackSize = decrStackSize(slot, amount);
            onwer.OnInventoryChanged(decrStackSize, slot, name, EnumInvFlow.EXTRACT);
            onwer.markDirty();
            return decrStackSize;
        }
    }

    private ItemStack decrStackSize(int slot, int count) {
        if (stacks[slot].stackSize <= count) {

            ItemStack stack = this.stacks[slot];
            stacks[slot] = null;
            return stack;
        } else {
            ItemStack stack = stacks[slot].splitStack(count);
            if (stacks[slot].stackSize == 0) {
                stacks[slot] = null;
            }
            return stack;
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < stacks.length; i++) {
            if (stacks[i] != null) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                stacks[i].writeToNBT(itemTag);
                nbtTagList.appendTag(itemTag);
            }
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("items", nbtTagList);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        NBTTagList tagList = nbt.getTagList("items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
            int slot = itemTags.getInteger("Slot");

            if (slot >= 0 && slot <= stacks.length) {
                stacks[slot] = ItemStack.loadItemStackFromNBT(itemTags);

            }
        }
        onwer.markDirty();
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        stacks[slot] = stack;
        onwer.markDirty();
    }

    protected IInventoryOnwer getOnwer() {
        return onwer;
    }
}