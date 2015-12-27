package mc.harvestmoon

import com.bioxx.tfc.TileEntities.TELogPile
import com.bioxx.tfc.api.{TFCBlocks, TFCItems}
import cpw.mods.fml.common.eventhandler.Event.Result
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.item.ItemStack
import net.minecraftforge.event.entity.player.PlayerInteractEvent

object LogPile {
  @SubscribeEvent
  def rightClick(e: PlayerInteractEvent): Unit = {
    if (e.world.isRemote
      || e.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK
      || e.getResult == Result.DENY
      || e.world.getBlock(e.x, e.y, e.z) != TFCBlocks.logPile) {
      return
    }

    val handStack = e.entityPlayer.getCurrentEquippedItem
    val teLogPile = e.world.getTileEntity(e.x, e.y, e.z).asInstanceOf[TELogPile]
    if (handStack == null || handStack.getItem != TFCItems.logs
      || teLogPile.getNumberOfLogs >= 16) {
      return
    }

    teLogPile.storage.zipWithIndex foreach {
      case (item, index) =>
        if (teLogPile.contentsMatch(index, handStack)) {
          val add = math.min(4 - item.stackSize, handStack.stackSize)
          teLogPile.injectContents(index, add)
          handStack.stackSize -= add
          e.setCanceled(true)
        } else if (item == null) {
          val add = math.min(handStack.stackSize, 4)
          teLogPile.addContents(index, new ItemStack(handStack.getItem, add, handStack.getItemDamage))
          handStack.stackSize -= add
          e.setCanceled(true)
        }
    }
  }
}

