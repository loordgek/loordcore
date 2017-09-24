package loordgek.loordcore.util.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class SimpleExtendedItemHandler implements IExtendedItemHandler, INBTSerializable<NBTTagCompound> {

    private final int stacksize;
    private final int invsize;
    private final String name;
    private final IInventoryOwner onwer;
    protected NonNullList<ItemStack> stacks;

    public SimpleExtendedItemHandler(int stacksize, int invsize, String name, IInventoryOwner onwer) {
        this.stacksize = stacksize;
        this.invsize = invsize;
        this.name = name;
        this.onwer = onwer;
        this.stacks = NonNullList.create();
    }

    @Override
    public int getSlots() {
        return invsize;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return stacks.get(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot,@Nonnull ItemStack stack, boolean simulate) {
        if (stack == ItemStack.EMPTY)
            return ItemStack.EMPTY;

        ItemStack stackinslot = getStackInSlot(slot);

        int m;
        if (stackinslot != ItemStack.EMPTY) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, stackinslot))
                return stack;

            m = Math.min(stack.getMaxStackSize(), stacksize - stackinslot.getCount());
            if (stack.getCount() <= m) {
                if (!simulate) {
                    ItemStack copy = stack.copy();
                    copy.grow(stackinslot.getCount());
                    stacks.set(slot, stack);
                    onwer.OnInventoryChanged(stack, slot, name, InvFlow.INSERT);
                    onwer.markDirty();
                }
                return ItemStack.EMPTY;

            } else {
                stack = stack.copy();
                if (!simulate) {
                    ItemStack copy = stack.splitStack(m);
                    copy.grow(stackinslot.getCount());
                    stacks.set(slot, stack);
                    onwer.OnInventoryChanged(stack, slot, name, InvFlow.INSERT);
                    onwer.markDirty();
                }
            }
        } else {
            m = Math.min(stack.getMaxStackSize(), stacksize);
            if (m < stack.getCount()) {
                stack = stack.copy();
                if (!simulate) {
                    stacks.set(slot, stack);
                    onwer.OnInventoryChanged(stack, slot, name, InvFlow.INSERT);
                    onwer.markDirty();
                    return stack;
                } else {
                    stack.grow(m);
                    return stack;
                }
            } else {
                if (!simulate) {
                    stacks.set(slot, stack);
                    onwer.OnInventoryChanged(stack, slot, name, InvFlow.INSERT);
                    onwer.markDirty();
                }
                return ItemStack.EMPTY;
            }
        }
        return ItemStack.EMPTY;
    }
    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;

        ItemStack stackInSlot = stacks.get(slot);

        if (stackInSlot == ItemStack.EMPTY)
            return ItemStack.EMPTY;

        if (simulate)
            if (stackInSlot.getCount() < amount)
                return stackInSlot.copy();
            else {
                ItemStack copy = stackInSlot.copy();
                copy.shrink(amount);
                return copy;
            }
        else {
            int m = Math.min(stackInSlot.getCount(), amount);

            ItemStack stack = decrStackSize(slot, amount);
            onwer.OnInventoryChanged(stack, slot, name, InvFlow.EXTRACT);
            onwer.markDirty();
            return stack;
        }
    }

    @Nonnull
    @Override
    public ItemStack insertItemInternal(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return null;
    }

    @Nonnull
    @Override
    public ItemStack extractItemInternal(int slot, int amount, boolean simulate) {
        return null;
    }

    @Override
    public int getSlotLimit(int slot) {
        return stacksize;
    }

    @Nonnull
    private ItemStack decrStackSize(int slot, int count) {
        if (stacks.get(slot).getCount() <= count) {

            ItemStack stack = this.stacks.get(slot);
            stacks.set(slot, ItemStack.EMPTY);
            return stack;
        } else {
            ItemStack stack = stacks.get(slot).splitStack(count);
            if (stacks.get(slot).getCount() == 0) {
                stacks.set(slot, ItemStack.EMPTY);
            }
            return stack;
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < stacks.size(); i++) {
            if (stacks.get(i) != ItemStack.EMPTY) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                stacks.get(i).writeToNBT(itemTag);
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

            if (slot >= 0 && slot <= stacks.size()) {
                stacks.set(slot, new ItemStack(itemTags));

            }
        }
        onwer.markDirty();
    }

    @Nonnull
    protected IInventoryOwner getOwner() {
        return onwer;
    }


    public int getStacksize() {
        return stacksize;
    }

    public int getInvsize() {
        return invsize;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean canInsertItem(ItemStack stack, int slot) {
        return true;
    }

    @Override
    public boolean canExtractItem(int slot) {
        return true;
    }

    @Override
    public boolean canExtractStackFromSlot(ItemStack stack, int slot) {
        return false;
    }

    @Override
    public void putStack(@Nonnull ItemStack stack, int slot) {
        stacks.set(slot, stack);
    }

    @Override
    public NonNullList<ItemStack> getInv() {
        return stacks;
    }
}