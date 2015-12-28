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

import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.{Mod, SidedProxy}
import net.minecraftforge.common.MinecraftForge
import org.apache.logging.log4j.{LogManager, Logger}

@Mod(modid = "harvestmoon", name = "Harvest Moon", version = "0.0.2", modLanguage = "scala")
object HarvestMoon {
  private val log: Logger = LogManager.getLogger("HarvestMoon")

  @SidedProxy(clientSide = "mc.harvestmoon.HarvestMoon$ClientProxy", serverSide = "mc.harvestmoon.HarvestMoon$ServerProxy")
  var proxy: CommonProxy = null

  @EventHandler
  def init(e: FMLInitializationEvent): Unit = {
    proxy.init(e)
  }

  class CommonProxy {
    @EventHandler
    def init(e: FMLInitializationEvent): Unit = {
      log.info("Harvest Moon loading")
      MinecraftForge.EVENT_BUS.register(TreeShake)
      MinecraftForge.EVENT_BUS.register(LogPile)
    }
  }

  class ClientProxy extends CommonProxy {
    @EventHandler
    override def init(e: FMLInitializationEvent): Unit = super.init(e)
  }

  class ServerProxy extends CommonProxy {
    @EventHandler
    override def init(e: FMLInitializationEvent): Unit = super.init(e)
  }

}
