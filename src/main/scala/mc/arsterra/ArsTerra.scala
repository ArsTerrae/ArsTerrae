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

import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.{Mod, SidedProxy}
import net.minecraftforge.common.MinecraftForge
import org.apache.logging.log4j.{LogManager, Logger}

@Mod(modid = "arsterra", name = "Ars Terra", version = "0.0.2", modLanguage = "scala")
object ArsTerra {
  private val log: Logger = LogManager.getLogger("ArsTerra")

  @SidedProxy(clientSide = "mc.arsterra.ArsTerra$ClientProxy", serverSide = "mc.arsterra.ArsTerra$ServerProxy")
  var proxy: CommonProxy = null

  @EventHandler
  def init(e: FMLInitializationEvent): Unit = {
    proxy.init(e)
  }

  class CommonProxy {
    @EventHandler
    def init(e: FMLInitializationEvent): Unit = {
      log.info("Ars Terra loading")
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
