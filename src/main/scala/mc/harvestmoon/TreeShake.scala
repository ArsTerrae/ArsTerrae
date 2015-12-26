package mc.harvestmoon

import com.bioxx.tfc.api.TFCBlocks
import cpw.mods.fml.common.eventhandler.Event.Result
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.world.World
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import org.apache.logging.log4j.{LogManager, Logger}

object TreeShake {
  private val log: Logger = LogManager.getLogger("HarvestMoon")

  @SubscribeEvent
  def rightClick(e: PlayerInteractEvent): Unit = {
    if (e.world.isRemote) {
      return
    }
    log.info(s"${e.action} @ ${e.x} ${e.y} ${e.z}")
    if (e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && e.getResult != Result.DENY) {
      val block = e.world.getBlock(e.x, e.y, e.z)
      if (block == TFCBlocks.fruitTreeWood) {
        val topCoords = scanForTrunkTop(e.world, e.x, e.y, e.z)
        log.info(s"${topCoords._1} ${topCoords._2} ${topCoords._3}")
      }
    }
  }

  def scanForTrunkTop(world: World, initialX: Int, initialY: Int, initialZ: Int): (Int, Int, Int) = {
    (1 to 3).map(logCount => (initialX, initialY + logCount, initialZ)).takeWhile({ case (x, y, z) =>
      world.getBlock(x, y, z) != TFCBlocks.fruitTreeLeaves && world.getBlock(x, y, z) != TFCBlocks.fruitTreeLeaves2 && !world.isAirBlock(x, y, z)
    }).last
  }
}
