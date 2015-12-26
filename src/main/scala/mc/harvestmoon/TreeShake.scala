package mc.harvestmoon

import com.bioxx.tfc.api.TFCBlocks
import cpw.mods.fml.common.eventhandler.Event.Result
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.world.World
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import org.apache.logging.log4j.{LogManager, Logger}

import scala.collection.immutable.HashSet

object TreeShake {
  private val log: Logger = LogManager.getLogger("HarvestMoon")

  @SubscribeEvent
  def rightClick(e: PlayerInteractEvent): Unit = {
    if (e.world.isRemote) {
      return
    }
    if (e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && e.getResult != Result.DENY) {
      if (e.world.getBlock(e.x, e.y, e.z) == TFCBlocks.fruitTreeWood) {
        (findFruits(e.world, (e.x, e.y, e.z))
          foreach { case (x, y, z) =>
          e.world.getBlock(x, y, z).onBlockActivated(e.world, x, y, z, e.entityPlayer, e.face, 0, 0, 0)
        })
      }
    }
  }

  private def fruitRange(pos: (Int, Int, Int)) = {
    val (x, y, z) = pos
    ((for (xs <- x - 1 to x + 1; ys <- y - 1 to y + 1; zs <- z - 1 to z + 1) yield (xs, ys, zs))
      filter { pos2 => pos != pos2 })
  }

  def findFruits(world: World, pos: (Int, Int, Int)): Set[(Int, Int, Int)] = {
    val (x, y, z) = pos
    ((HashSet((x + 1, y, z), (x - 1, y, z), (x, y, z + 1), (x, y, z - 1), (x, y + 1, z))
      filter { case (x, y, z) => world.getBlock(x, y, z) == TFCBlocks.fruitTreeWood }
      flatMap { pos => findFruits(world, pos) })
      | (fruitRange(pos)
      filter {
      case (x, y, z) => {
        val b = world.getBlock(x, y, z)
        (b == TFCBlocks.fruitTreeLeaves || b == TFCBlocks.fruitTreeLeaves2) && ((world.getBlockMetadata(x, y, z) & 8) == 8)
      }
    }).toSet)
  }
}
