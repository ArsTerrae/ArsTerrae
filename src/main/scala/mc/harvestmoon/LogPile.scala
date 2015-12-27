package mc.harvestmoon

import com.bioxx.tfc.TileEntities.TELogPile
import com.bioxx.tfc.api.TFCBlocks
import com.bioxx.tfc.api.TFCItems
import cpw.mods.fml.common.eventhandler.Event.Result
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.item.ItemStack
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import org.apache.logging.log4j.{LogManager, Logger}

object LogPile {
  @SubscribeEvent
  def rightClick(e: PlayerInteractEvent): Unit = {
    if (!e.world.isRemote
      && e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK
      && e.getResult != Result.DENY
      && e.world.getBlock(e.x, e.y, e.z) == TFCBlocks.logPile
      && e.entityPlayer.getCurrentEquippedItem != null
      && e.entityPlayer.getCurrentEquippedItem.getItem == TFCItems.logs
      && e.world.getTileEntity(e.x, e.y, e.z).asInstanceOf[TELogPile].getNumberOfLogs < 16) {
        e.world.getTileEntity(e.x, e.y, e.z).asInstanceOf[TELogPile].storage.zipWithIndex.foreach { case (item, index) => {
          if(item != null && e.world.getTileEntity(e.x, e.y, e.z).asInstanceOf[TELogPile].contentsMatch(index, item)) {
            e.world.getTileEntity(e.x, e.y, e.z).asInstanceOf[TELogPile].injectContents(index, math.min(4 - item.stackSize, e.entityPlayer.getCurrentEquippedItem.stackSize))
            e.entityPlayer.getCurrentEquippedItem.stackSize -= math.min(4 - item.stackSize, e.entityPlayer.getCurrentEquippedItem.stackSize)
          }
          if(item == null) {
            e.world.getTileEntity(e.x, e.y, e.z).asInstanceOf[TELogPile].addContents(index, new ItemStack(e.entityPlayer.getCurrentEquippedItem.getItem, math.min(e.entityPlayer.getCurrentEquippedItem.stackSize, 4), e.entityPlayer.getCurrentEquippedItem.getItemDamage))
            e.entityPlayer.getCurrentEquippedItem.stackSize -= math.min(e.entityPlayer.getCurrentEquippedItem.stackSize, 4)
          }
        }}

    }
  }
}
