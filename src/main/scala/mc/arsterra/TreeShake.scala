// Copyright (c) 2015 Contributors to Ars Terra
//
// This file is part of Ars Terrae.
//
// Ars Terrae is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Ars Terrae is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Ars Terrae. If not, see <http://www.gnu.org/licenses/>.

package mc.arsterra

import com.bioxx.tfc.TileEntities.TEFruitTreeWood
import scala.util.Random
import com.bioxx.tfc.api.TFCBlocks
import cpw.mods.fml.common.eventhandler.Event.Result
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.world.World
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import org.apache.logging.log4j.{LogManager, Logger}

import scala.collection.immutable.HashSet

object TreeShake {
  private val log: Logger = LogManager.getLogger("ArsTerra")

  @SubscribeEvent
  def rightClick(e: PlayerInteractEvent): Unit = {
    if (!(!e.world.isRemote
      && e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK
      && e.getResult != Result.DENY
      && e.world.getBlock(e.x, e.y, e.z) == TFCBlocks.fruitTreeWood) || !e.world.getTileEntity(e.x, e.y, e.z).asInstanceOf[TEFruitTreeWood].isTrunk) {
      return
    }

    Random.shuffle(findFruits(e.world, (e.x, e.y, e.z)).toSeq).take(Random.nextInt(3) + 1).foreach {
      case (x, y, z) => e.world.getBlock(x, y, z)
        .onBlockActivated(e.world, x, y, z, e.entityPlayer, e.face, 0, 0, 0)
    }
  }

  private def adjacentWood(world: World, pos: Point): Set[Point] = pos match {
    case (x, y, z) => HashSet((x + 1, y, z), (x - 1, y, z), (x, y, z + 1), (x, y, z - 1), (x, y + 1, z)).filter {
      case (x, y, z) => world.getBlock(x, y, z) == TFCBlocks.fruitTreeWood
    }
  }

  private def linkedWood(world: World, pos: Point, trv: Set[Point] = HashSet()): Set[Point] = pos match {
    case (x, y, z) => adjacentWood(world, pos).diff(trv).flatMap(p => linkedWood(world, p, trv + pos)) + pos
  }

  private def fruitRange(pos: Point) = pos match {
    case (x, y, z) => for {
      xs <- x - 1 to x + 1
      ys <- y - 1 to y + 1
      zs <- z - 1 to z + 1
      if (xs, ys, zs) != pos
    } yield (xs, ys, zs)
  }

  private def fruitsSupportedByWood(world: World, pos: Point): Set[Point] =
    fruitRange(pos).filter {
      case (x, y, z) =>
        val b = world.getBlock(x, y, z)
        (b == TFCBlocks.fruitTreeLeaves || b == TFCBlocks.fruitTreeLeaves2) && ((world.getBlockMetadata(x, y, z) & 8) == 8)
    }.toSet

  private def findFruits(world: World, pos: Point): Set[Point] =
    linkedWood(world, pos).flatMap(p => fruitsSupportedByWood(world, p))
}
