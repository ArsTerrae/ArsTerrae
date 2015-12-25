package mc.harvestmoon

import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.{Mod, SidedProxy}
import net.minecraftforge.common.MinecraftForge
import org.apache.logging.log4j.{LogManager, Logger}

@Mod(modid = "harvestmoon", name = "Harvest Moon", version = "0.0.1", modLanguage = "scala")
object HarvestMoon {
  private val log: Logger = LogManager getLogger "HarvestMoon"

  @SidedProxy(clientSide = "mc.harvestmoon.HarvestMoon$ClientProxy", serverSide = "mc.harvestmoon.HarvestMoon$ServerProxy")
  var proxy: CommonProxy = null

  @EventHandler
  def init(e: FMLInitializationEvent): Unit = {
    proxy init e
  }

  class CommonProxy {
    @EventHandler
    def init(e: FMLInitializationEvent): Unit = {
      log info "Harvest Moon loading"
    }
  }

  class ClientProxy extends CommonProxy {
    @EventHandler
    override def init(e: FMLInitializationEvent): Unit = super.init(e)
  }

  class ServerProxy extends CommonProxy {
    @EventHandler
    override def init(e: FMLInitializationEvent): Unit = {
      MinecraftForge.EVENT_BUS register TreeShake
    }
  }

}
