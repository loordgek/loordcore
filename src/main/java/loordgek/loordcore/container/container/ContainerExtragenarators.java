package loordgek.loordcore.container.container;

import com.google.common.collect.Sets;
import loordgek.extragenarators.network.*;
import loordgek.extragenarators.util.item.IUpdateItemHander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/*
*    PneumaticCraft code. author = MineMaarten
*    https://github.com/MineMaarten/PneumaticCraft
*/
public abstract class ContainerExtragenarators<Tile extends IContainerGuiSync> extends Container {
    public Tile te;

    /**
     * The current drag mode (0 : evenly split, 1 : one item by slot, 2 : not used ?)
     */
    private int dragMode = -1;
    /**
     * The current drag event (0 : start, 1 : add slot : 2 : end)
     */
    private int dragEvent;
    private final Set<Slot> dragSlots = Sets.<Slot>newHashSet();

    private final List<SyncField> syncFields = new ArrayList<SyncField>();

    public ContainerExtragenarators(Tile te) {
        this.te = te;
        if (te != null) addSyncFields(te);
    }

    protected void addPlayerSlots(InventoryPlayer playerInventory, int x, int y) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, x + j * 18, y + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            addSlotToContainer(new Slot(playerInventory, i, x + i * 18, y + 58));
        }
    }

    protected void addSlotListToContainer(IItemHandler handler, int x, int y, int rows, int collums){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < collums; j++) {
                addSlotToContainer(new SlotItemHandler(handler, j + i + rows, x + j * 18, y + i *18));
            }
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < syncFields.size(); i++) {
            if (syncFields.get(i).update()) {
                sendToCrafters(new GuiSyncPacket(i, syncFields.get(i)));
            }
        }
    }

    private void sendToCrafters(IMessage message) {
        for (IContainerListener crafter : listeners) {
            if (crafter instanceof EntityPlayerMP) {
                NetworkHandler.SentTo(message, (EntityPlayerMP) crafter);
            }
        }
    }

    private void addSyncField(SyncField field) {
        syncFields.add(field);
        field.setLazy(false);
    }

    private void addSyncFields(Object annotatedObject) {
        List<SyncField> fields = NetworkUtils.getSyncFields(annotatedObject, GuiSync.class);
        for (SyncField field : fields)
            addSyncField(field);
    }

    @SuppressWarnings("unchecked")
    public void Updatefield(int index, Object value) {
        syncFields.get(index).setValue(value);
        if (te != null) te.onGuiUpdate();
    }

    @Override
    protected void resetDrag() {
        this.dragEvent = 0;
        this.dragSlots.clear();
    }

    @Nullable
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {

        ItemStack itemstack = null;
        InventoryPlayer inventoryplayer = player.inventory;

        if (clickTypeIn == ClickType.QUICK_CRAFT) {
            int i = this.dragEvent;
            this.dragEvent = getDragEvent(dragType);

            if ((i != 1 || this.dragEvent != 2) && i != this.dragEvent) {
                this.resetDrag();
            } else if (inventoryplayer.getItemStack() == null) {
                this.resetDrag();
            } else if (this.dragEvent == 0) {
                this.dragMode = extractDragMode(dragType);

                if (isValidDragMode(this.dragMode, player)) {
                    this.dragEvent = 1;
                    this.dragSlots.clear();
                } else {
                    this.resetDrag();
                }
            } else if (this.dragEvent == 1) {
                Slot slot = this.inventorySlots.get(slotId);

                if (slot != null && canAddItemToSlot(slot, inventoryplayer.getItemStack(), true) && slot.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize > this.dragSlots.size() && this.canDragIntoSlot(slot)) {
                    this.dragSlots.add(slot);
                }
            } else if (this.dragEvent == 2) {
                if (!this.dragSlots.isEmpty()) {
                    ItemStack itemstack3 = inventoryplayer.getItemStack().copy();
                    int j = inventoryplayer.getItemStack().stackSize;

                    for (Slot slot1 : this.dragSlots) {
                        if (slot1 != null && canAddItemToSlot(slot1, inventoryplayer.getItemStack(), true) && slot1.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize >= this.dragSlots.size() && this.canDragIntoSlot(slot1)) {
                            ItemStack itemstack1 = itemstack3.copy();
                            int k = slot1.getHasStack() ? slot1.getStack().stackSize : 0;
                            computeStackSize(this.dragSlots, this.dragMode, itemstack1, k);

                            if (itemstack1.stackSize > itemstack1.getMaxStackSize()) {
                                itemstack1.stackSize = itemstack1.getMaxStackSize();
                            }

                            if (itemstack1.stackSize > slot1.getItemStackLimit(itemstack1)) {
                                itemstack1.stackSize = slot1.getItemStackLimit(itemstack1);
                            }

                            j -= itemstack1.stackSize - k;
                            slot1.putStack(itemstack1);
                            if (slot1 instanceof SlotItemHandler) {
                                if (((SlotItemHandler) slot1).getItemHandler() instanceof IUpdateItemHander) {
                                    IItemHandler itemHandler = ((SlotItemHandler) slot1).getItemHandler();
                                    ((IUpdateItemHander) itemHandler).UpdateItemHandler();

                                }
                            }
                        }
                    }

                    itemstack3.stackSize = j;

                    if (itemstack3.stackSize <= 0) {
                        itemstack3 = null;
                    }

                    inventoryplayer.setItemStack(itemstack3);
                }

                this.resetDrag();
            } else {
                this.resetDrag();
            }
        } else if (this.dragEvent != 0) {
            this.resetDrag();
        } else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
            if (slotId == -999) {
                if (inventoryplayer.getItemStack() != null) {
                    if (dragType == 0) {
                        player.dropItem(inventoryplayer.getItemStack(), true);
                        inventoryplayer.setItemStack((ItemStack) null);
                    }

                    if (dragType == 1) {
                        player.dropItem(inventoryplayer.getItemStack().splitStack(1), true);

                        if (inventoryplayer.getItemStack().stackSize == 0) {
                            inventoryplayer.setItemStack((ItemStack) null);
                        }
                    }
                }
            } else if (clickTypeIn == ClickType.QUICK_MOVE) {
                if (slotId < 0) {
                    return null;
                }

                Slot slot6 = (Slot) this.inventorySlots.get(slotId);

                if (slot6 != null && slot6.canTakeStack(player)) {
                    ItemStack itemstack8 = slot6.getStack();

                    if (itemstack8 != null && itemstack8.stackSize <= 0) {
                        itemstack = itemstack8.copy();
                        slot6.putStack((ItemStack) null);
                    }

                    ItemStack itemstack11 = this.transferStackInSlot(player, slotId);

                    if (itemstack11 != null) {
                        Item item = itemstack11.getItem();
                        itemstack = itemstack11.copy();

                        if (slot6.getStack() != null && slot6.getStack().getItem() == item) {
                            this.retrySlotClick(slotId, dragType, true, player);
                        }
                    }
                }
            } else {
                if (slotId < 0) {
                    return null;
                }

                Slot slot7 = (Slot) this.inventorySlots.get(slotId);

                if (slot7 != null) {
                    ItemStack itemstack9 = slot7.getStack();
                    ItemStack itemstack12 = inventoryplayer.getItemStack();

                    if (itemstack9 != null) {
                        itemstack = itemstack9.copy();
                    }

                    if (itemstack9 == null) {
                        if (itemstack12 != null && slot7.isItemValid(itemstack12)) {
                            int l2 = dragType == 0 ? itemstack12.stackSize : 1;

                            if (l2 > slot7.getItemStackLimit(itemstack12)) {
                                l2 = slot7.getItemStackLimit(itemstack12);
                            }

                            slot7.putStack(itemstack12.splitStack(l2));
                            if (slot7 instanceof SlotItemHandler) {
                                if (((SlotItemHandler) slot7).getItemHandler() instanceof IUpdateItemHander) {
                                    IItemHandler itemHandler = ((SlotItemHandler) slot7).getItemHandler();
                                    ((IUpdateItemHander) itemHandler).UpdateItemHandler();

                                }
                            }
                        }
                        {
                            if (itemstack12 != null) {
                                if (itemstack12.stackSize == 0) {
                                    inventoryplayer.setItemStack((ItemStack) null);
                                }
                            }

                        }
                    } else if (slot7.canTakeStack(player)) {
                        if (itemstack12 == null) {
                            if (itemstack9.stackSize > 0) {
                                int k2 = dragType == 0 ? itemstack9.stackSize : (itemstack9.stackSize + 1) / 2;
                                inventoryplayer.setItemStack(slot7.decrStackSize(k2));

                                if (itemstack9.stackSize <= 0) {
                                    slot7.putStack((ItemStack) null);
                                }

                                slot7.onPickupFromSlot(player, inventoryplayer.getItemStack());
                                if (slot7 instanceof SlotItemHandler) {
                                    boolean update = true;
                                    if (update) {
                                        if (((SlotItemHandler) slot7).getItemHandler() instanceof IUpdateItemHander) {
                                            IItemHandler itemHandler = ((SlotItemHandler) slot7).getItemHandler();
                                            ((IUpdateItemHander) itemHandler).UpdateItemHandler();

                                        }
                                        update = false;
                                    }
                                }
                            } else {
                                slot7.putStack((ItemStack) null);
                                inventoryplayer.setItemStack((ItemStack) null);
                            }
                        } else if (slot7.isItemValid(itemstack12)) {
                            if (itemstack9.getItem() == itemstack12.getItem() && itemstack9.getMetadata() == itemstack12.getMetadata() && ItemStack.areItemStackTagsEqual(itemstack9, itemstack12)) {
                                int j2 = dragType == 0 ? itemstack12.stackSize : 1;

                                if (j2 > slot7.getItemStackLimit(itemstack12) - itemstack9.stackSize) {
                                    j2 = slot7.getItemStackLimit(itemstack12) - itemstack9.stackSize;
                                }

                                if (j2 > itemstack12.getMaxStackSize() - itemstack9.stackSize) {
                                    j2 = itemstack12.getMaxStackSize() - itemstack9.stackSize;
                                }

                                itemstack12.splitStack(j2);

                                if (itemstack12.stackSize == 0) {
                                    inventoryplayer.setItemStack((ItemStack) null);
                                }

                                itemstack9.stackSize += j2;
                                if (slot7 instanceof SlotItemHandler) {
                                    if (((SlotItemHandler) slot7).getItemHandler() instanceof IUpdateItemHander) {
                                        IItemHandler itemHandler = ((SlotItemHandler) slot7).getItemHandler();
                                        ((IUpdateItemHander) itemHandler).UpdateItemHandler();

                                    }
                                }
                            } else if (itemstack12.stackSize <= slot7.getItemStackLimit(itemstack12)) {
                                slot7.putStack(itemstack12);
                                inventoryplayer.setItemStack(itemstack9);
                                if (slot7 instanceof SlotItemHandler) {
                                    boolean update = true;
                                    if (update) {
                                        if (((SlotItemHandler) slot7).getItemHandler() instanceof IUpdateItemHander) {
                                            IItemHandler itemHandler = ((SlotItemHandler) slot7).getItemHandler();
                                            ((IUpdateItemHander) itemHandler).UpdateItemHandler();

                                        }
                                        update = false;
                                    }
                                }
                            }
                        } else if (itemstack9.getItem() == itemstack12.getItem() && itemstack12.getMaxStackSize() > 1 && (!itemstack9.getHasSubtypes() || itemstack9.getMetadata() == itemstack12.getMetadata()) && ItemStack.areItemStackTagsEqual(itemstack9, itemstack12)) {
                            int i2 = itemstack9.stackSize;

                            if (i2 > 0 && i2 + itemstack12.stackSize <= itemstack12.getMaxStackSize()) {
                                itemstack12.stackSize += i2;
                                itemstack9 = slot7.decrStackSize(i2);

                                if (itemstack9.stackSize == 0) {
                                    slot7.putStack((ItemStack) null);
                                }

                                slot7.onPickupFromSlot(player, inventoryplayer.getItemStack());
                                if (slot7 instanceof SlotItemHandler) {
                                    boolean update = true;
                                    if (update) {
                                        if (((SlotItemHandler) slot7).getItemHandler() instanceof IUpdateItemHander) {
                                            IItemHandler itemHandler = ((SlotItemHandler) slot7).getItemHandler();
                                            ((IUpdateItemHander) itemHandler).UpdateItemHandler();

                                        }
                                        update = false;
                                    }
                                }
                            }
                        }
                    }

                    slot7.onSlotChanged();
                }
            }
        } else if (clickTypeIn == ClickType.SWAP && dragType >= 0 && dragType < 9) {
            Slot slot5 = (Slot) this.inventorySlots.get(slotId);
            ItemStack itemstack7 = inventoryplayer.getStackInSlot(dragType);

            if (itemstack7 != null && itemstack7.stackSize <= 0) {
                itemstack7 = null;
                inventoryplayer.setInventorySlotContents(dragType, (ItemStack) null);
            }

            ItemStack itemstack10 = slot5.getStack();

            if (itemstack7 != null || itemstack10 != null) {
                if (itemstack7 == null) {
                    if (slot5.canTakeStack(player)) {
                        inventoryplayer.setInventorySlotContents(dragType, itemstack10);
                        slot5.putStack((ItemStack) null);
                        slot5.onPickupFromSlot(player, itemstack10);
                        if (slot5 instanceof SlotItemHandler) {
                            if (((SlotItemHandler) slot5).getItemHandler() instanceof IUpdateItemHander) {
                                IItemHandler itemHandler = ((SlotItemHandler) slot5).getItemHandler();
                                ((IUpdateItemHander) itemHandler).UpdateItemHandler();

                            }
                        }
                    }
                } else if (itemstack10 == null) {
                    if (slot5.isItemValid(itemstack7)) {
                        int k1 = slot5.getItemStackLimit(itemstack7);

                        if (itemstack7.stackSize > k1) {
                            slot5.putStack(itemstack7.splitStack(k1));
                            if (slot5 instanceof SlotItemHandler) {
                                if (((SlotItemHandler) slot5).getItemHandler() instanceof IUpdateItemHander) {
                                    IItemHandler itemHandler = ((SlotItemHandler) slot5).getItemHandler();
                                    ((IUpdateItemHander) itemHandler).UpdateItemHandler();
                                }
                            }
                        } else {
                            slot5.putStack(itemstack7);
                            inventoryplayer.setInventorySlotContents(dragType, (ItemStack) null);
                            if (slot5 instanceof SlotItemHandler) {
                                if (((SlotItemHandler) slot5).getItemHandler() instanceof IUpdateItemHander) {
                                    IItemHandler itemHandler = ((SlotItemHandler) slot5).getItemHandler();
                                    ((IUpdateItemHander) itemHandler).UpdateItemHandler();
                                }
                            }
                        }
                    }
                } else if (slot5.canTakeStack(player) && slot5.isItemValid(itemstack7)) {
                    int l1 = slot5.getItemStackLimit(itemstack7);

                    if (itemstack7.stackSize > l1) {
                        slot5.putStack(itemstack7.splitStack(l1));
                        slot5.onPickupFromSlot(player, itemstack10);

                        if (!inventoryplayer.addItemStackToInventory(itemstack10)) {
                            player.dropItem(itemstack10, true);
                        }
                        if (slot5 instanceof SlotItemHandler) {
                            if (((SlotItemHandler) slot5).getItemHandler() instanceof IUpdateItemHander) {
                                IItemHandler itemHandler = ((SlotItemHandler) slot5).getItemHandler();
                                ((IUpdateItemHander) itemHandler).UpdateItemHandler();
                            }
                        }
                    } else {
                        slot5.putStack(itemstack7);
                        inventoryplayer.setInventorySlotContents(dragType, itemstack10);
                        slot5.onPickupFromSlot(player, itemstack10);
                        if (slot5 instanceof SlotItemHandler) {
                            if (((SlotItemHandler) slot5).getItemHandler() instanceof IUpdateItemHander) {
                                IItemHandler itemHandler = ((SlotItemHandler) slot5).getItemHandler();
                                ((IUpdateItemHander) itemHandler).UpdateItemHandler();
                            }
                        }
                    }
                }
            }
        } else if (clickTypeIn == ClickType.CLONE && player.capabilities.isCreativeMode && inventoryplayer.getItemStack() == null && slotId >= 0) {
            Slot slot4 = (Slot) this.inventorySlots.get(slotId);

            if (slot4 != null && slot4.getHasStack()) {
                if (slot4.getStack().stackSize > 0) {
                    ItemStack itemstack6 = slot4.getStack().copy();
                    itemstack6.stackSize = itemstack6.getMaxStackSize();
                    inventoryplayer.setItemStack(itemstack6);
                } else {
                    slot4.putStack((ItemStack) null);
                }
            }
        } else if (clickTypeIn == ClickType.THROW && inventoryplayer.getItemStack() == null && slotId >= 0) {
            Slot slot3 = (Slot) this.inventorySlots.get(slotId);

            if (slot3 != null && slot3.getHasStack() && slot3.canTakeStack(player)) {
                ItemStack itemstack5 = slot3.decrStackSize(dragType == 0 ? 1 : slot3.getStack().stackSize);
                slot3.onPickupFromSlot(player, itemstack5);
                player.dropItem(itemstack5, true);
                if (slot3 instanceof SlotItemHandler) {
                    if (((SlotItemHandler) slot3).getItemHandler() instanceof IUpdateItemHander) {
                        IItemHandler itemHandler = ((SlotItemHandler) slot3).getItemHandler();
                        ((IUpdateItemHander) itemHandler).UpdateItemHandler();

                    }
                }
            }
        } else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0) {
            Slot slot2 = (Slot) this.inventorySlots.get(slotId);
            ItemStack itemstack4 = inventoryplayer.getItemStack();

            if (itemstack4 != null && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(player))) {
                int i1 = dragType == 0 ? 0 : this.inventorySlots.size() - 1;
                int j1 = dragType == 0 ? 1 : -1;

                for (int i3 = 0; i3 < 2; ++i3) {
                    for (int j3 = i1; j3 >= 0 && j3 < this.inventorySlots.size() && itemstack4.stackSize < itemstack4.getMaxStackSize(); j3 += j1) {
                        Slot slot8 = (Slot) this.inventorySlots.get(j3);

                        if (slot8.getHasStack() && canAddItemToSlot(slot8, itemstack4, true) && slot8.canTakeStack(player) && this.canMergeSlot(itemstack4, slot8) && (i3 != 0 || slot8.getStack().stackSize != slot8.getStack().getMaxStackSize())) {
                            int l = Math.min(itemstack4.getMaxStackSize() - itemstack4.stackSize, slot8.getStack().stackSize);
                            ItemStack itemstack2 = slot8.decrStackSize(l);
                            itemstack4.stackSize += l;

                            if (itemstack2.stackSize <= 0) {
                                slot8.putStack((ItemStack) null);
                            }
                            slot8.onPickupFromSlot(player, itemstack2);
                        }
                    }
                }
                if (slot2 instanceof SlotItemHandler) {
                    if (((SlotItemHandler) slot2).getItemHandler() instanceof IUpdateItemHander) {
                        IItemHandler itemHandler = ((SlotItemHandler) slot2).getItemHandler();
                        ((IUpdateItemHander) itemHandler).UpdateItemHandler();
                    }
                }
            }
        }
        this.detectAndSendChanges();
        return itemstack;
    }
}


