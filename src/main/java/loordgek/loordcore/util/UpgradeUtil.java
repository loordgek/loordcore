package loordgek.loordcore.util;

import loordgek.extragenarators.api.IUpgradeItem;
import net.minecraft.item.ItemStack;

public class UpgradeUtil {
    public static int getSpeedBoost(ItemStack[] stacks) {
        int speedboost = 1;
        for (ItemStack stack : stacks) {
            if (stack != null && stack.getItem() instanceof IUpgradeItem) {
                speedboost += ((IUpgradeItem) stack.getItem()).getspeedboost(stack) * stack.stackSize;
            }
        }
        return speedboost;
    }

    public static int getPowerstoreBoost(ItemStack[] stacks) {
        int speedboost = 1;
        for (ItemStack stack : stacks) {
            if (stack != null && stack.getItem() instanceof IUpgradeItem) {
                speedboost += ((IUpgradeItem) stack.getItem()).getpowerstoreboost(stack) * stack.stackSize;
            }
        }
        return speedboost;
    }

    public static int getMultiplierBoost(ItemStack[] stacks) {
        int speedboost = 1;
        for (ItemStack stack : stacks) {
            if (stack != null && stack.getItem() instanceof IUpgradeItem) {
                speedboost += ((IUpgradeItem) stack.getItem()).getmultiplierboost(stack) * stack.stackSize;
            }
        }
        return speedboost;
    }
}
