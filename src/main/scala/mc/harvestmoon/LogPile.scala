// Copyright (c) 2015 Contributors to Harvest Moon
//
// Permission is hereby granted, free of charge, to any person obtaining a copy of
// this software and associated documentation files (the "Software"), to deal in
// the Software without restriction, including without limitation the rights to
// use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
// the Software, and to permit persons to whom the Software is furnished to do so,
// subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
// FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
// COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
// IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
// CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

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
    if (handStack == null || handStack.getItem != TFCItems.logs || teLogPile.getNumberOfLogs >= 16) {
      return
    }

    teLogPile.storage.zipWithIndex.foreach {
      case (item, index) if (teLogPile.contentsMatch(index, handStack)) => {
        val add = math.min(4 - item.stackSize, handStack.stackSize)
        teLogPile.injectContents(index, add)
        handStack.stackSize -= add
        e.setCanceled(true)
      }
      case (null, index) => {
        val add = math.min(handStack.stackSize, 4)
        teLogPile.addContents(index, new ItemStack(handStack.getItem, add, handStack.getItemDamage))
        handStack.stackSize -= add
        e.setCanceled(true)
      }
      case _ => ()
    }
  }
}

