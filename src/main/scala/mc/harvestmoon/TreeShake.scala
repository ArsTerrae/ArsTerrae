package mc.harvestmoon

import com.bioxx.tfc.api.TFCBlocks
import cpw.mods.fml.common.eventhandler.Event.Result
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import org.apache.logging.log4j.{LogManager, Logger}

object TreeShake {
  private val log: Logger = LogManager getLogger "HarvestMoon"

  @SubscribeEvent
  def rightClick(e: PlayerInteractEvent): Unit = {
    log info s"${e.action} @ ${e.x} ${e.y} ${e.z}"
    if (e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && e.getResult != Result.DENY) {
      val block = e.world.getBlock(e.x, e.y, e.z)
      if (block == TFCBlocks.fruitTreeWood) {
        // TODO
      }
    }
  }
}
