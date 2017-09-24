package loordgek.loordcore.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy implements IProxy {

    @Override
    public EntityPlayer getclientplayer() {
        return getMinecraft().player;
    }

    @Override
    public void setCustomModelResourceLocationitem(Item item, int meta, ModelResourceLocation location) {
        ModelLoader.setCustomModelResourceLocation(item, meta, location);
    }

    @Override
    public Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

}