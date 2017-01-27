package loordgek.loordcore.util.item;


public class UpgradeInv extends InventorySimpleItemhander implements IUpdateItemHander {
    public UpgradeInv(int stacksize, int invsize, String name, IInventoryOnwer onwer) {
        super(stacksize, invsize, name, onwer);
    }

    @Override
    public void UpdateItemHandler() {
        getOnwer().updateItemHandler();

    }
}
