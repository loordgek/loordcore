package loordgek.loordcore.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public interface IProxy
{
    EntityPlayer getclientplayer();
    void setCustomModelResourceLocationitem(Item item, int meta, ModelResourceLocation location);
    Minecraft getMinecraft();

}
