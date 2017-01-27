package loordgek.loordcore.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class ServerProxy implements IProxy {
    @Override
    public EntityPlayer getclientplayer() {return null;}

    @Override
    public void setCustomModelResourceLocationitem(Item item, int meta, ModelResourceLocation location) {}

    @Override
    public Minecraft getMinecraft() {
        return null;
    }
}


