package mc.harvestmoon

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPreInitializationEvent}
import org.apache.logging.log4j.{LogManager, Logger}

@Mod(modid = "harvestmoon", name = "Harvest Moon", version = "0.0.1", modLanguage = "scala")
object HarvestMoon {
  val log: Logger = LogManager.getLogger("HarvestMoon")

  @EventHandler
  def init(e: FMLInitializationEvent): Unit = {
    log info "Hello world from Harvest Moon"
  }
}
