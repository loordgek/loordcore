package loordgek.loordcore.util.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class SimpleItemhandersDeepStore extends SimpleExtendedItemHandler {
    public SimpleItemhandersDeepStore(int stacksize, int invsize, String name, IInventoryOwner onwer) {
        super(stacksize, invsize, name, onwer);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack == ItemStack.EMPTY)
            return ItemStack.EMPTY;

        ItemStack stackinslot = getStackInSlot(slot);

        int m;
        if (stackinslot != ItemStack.EMPTY) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, stackinslot))
                return stack;

            m = Math.min(stack.getMaxStackSize(), getStacksize() - stackinslot.getCount());
            if (stack.getCount() <= m) {
                if (!simulate) {
                    ItemStack copy = stack.copy();
                    copy.grow(stackinslot.getCount());
                    stacks.set(slot, stack);
                    getOwner().OnInventoryChanged(stack, slot, getName(), InvFlow.INSERT);
                    getOwner().markDirty();
                }
                return ItemStack.EMPTY;

            } else {
                stack = stack.copy();
                if (!simulate) {
                    ItemStack copy = stack.splitStack(m);
                    copy.grow(stackinslot.getCount());
                    stacks.set(slot, stack);
                    getOwner().OnInventoryChanged(stack, slot, getName(), InvFlow.INSERT);
                    getOwner().markDirty();
                }
            }
        } else {
            m = Math.min(stack.getMaxStackSize(), getStacksize());
            if (m < stack.getCount()) {
                stack = stack.copy();
                if (!simulate) {
                    stacks.set(slot, stack);
                    getOwner().OnInventoryChanged(stack, slot, getName(), InvFlow.INSERT);
                    getOwner().markDirty();
                    return stack;
                } else {
                    stack.grow(m);
                    return stack;
                }
            } else {
                if (!simulate) {
                    stacks.set(slot, stack);
                    getOwner().OnInventoryChanged(stack, slot, getName(), InvFlow.INSERT);
                    getOwner().markDirty();
                }
                return ItemStack.EMPTY;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < stacks.size(); i++) {
            if (stacks.get(i) != ItemStack.EMPTY) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                itemTag.setInteger("stacksize", stacks.get(i).getCount());
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
                ItemStack stack = new ItemStack(itemTags);
                stack.setCount(itemTags.getInteger("stacksize"));
                stacks.set(slot, stack);
            }
        }
        getOwner().markDirty();
    }
}


