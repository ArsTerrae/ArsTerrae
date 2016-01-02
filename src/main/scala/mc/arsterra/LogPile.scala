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
    if (handStack == null || handStack.getItem != TFCItems.logs || teLogPile.getNumberOfLogs >= 16) {
      return
    }

    teLogPile.storage.zipWithIndex.foreach {
      case (item, index) if teLogPile.contentsMatch(index, handStack) =>
        val add = math.min(4 - item.stackSize, handStack.stackSize)
        teLogPile.injectContents(index, add)
        handStack.stackSize -= add
        e.setCanceled(true)
      case (null, index) =>
        val add = math.min(handStack.stackSize, 4)
        teLogPile.addContents(index, new ItemStack(handStack.getItem, add, handStack.getItemDamage))
        handStack.stackSize -= add
        e.setCanceled(true)
      case _ => ()
    }
  }
}

