package zmaster587.advancedRocketry.world.provider;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zmaster587.advancedRocketry.AdvancedRocketry;
import zmaster587.advancedRocketry.api.ARConfiguration;
import zmaster587.advancedRocketry.api.AdvancedRocketryBiomes;
import zmaster587.advancedRocketry.api.stations.ISpaceObject;
import zmaster587.advancedRocketry.client.render.planet.RenderSpaceSky;
import zmaster587.advancedRocketry.client.render.planet.RenderSpaceTravelSky;
import zmaster587.advancedRocketry.dimension.DimensionManager;
import zmaster587.advancedRocketry.dimension.DimensionProperties;
import zmaster587.advancedRocketry.entity.EntityRocket;
import zmaster587.advancedRocketry.stations.SpaceObjectManager;
import zmaster587.advancedRocketry.stations.SpaceStationObject;
import zmaster587.advancedRocketry.util.AstronomicalBodyHelper;
import zmaster587.advancedRocketry.world.ChunkProviderSpace;

public class WorldProviderSpace extends WorldProviderPlanet {
	private IRenderHandler skyRender;
	
	@Override
	public double getHorizon() {
		return 0;
	}
	@Override
	public String getSaveFolder() {		
			switch (this.getDimension()) {
				case -2:
					return "Space";
				case -9000:
					return "RP_Midway";
				case -9001:
					return "RP_Destiny";
				case -9002:
					return "RP_Beliskner";
				case -9003:
					return "RP_OriMotherShip";
				case -9004:
					return "RP_Aurora";
				case -9005:
					return "RP_ReplicatorShip";
				case -9006:
					return "RP_Daedalus";
				case -9007:
					return "RP_Lucian_Alliance_Hatak";
				case -9008:
					return "RP_Hive";
				case -9009:
					return "RP_HubSpawn";
				case -9010:
					return "RP_Apollo";
				case -9011:
					return "RP_Odyssey";
				case -9012:
					return "RP_Tria";
				case -9013:
					return "RP_Tokra_Hatak";
				case -9014:
					return "RP_Free_Jaffa_Hatak";
				case -9015:
					return "RP_Apophis_Hatak";
				case -9016:
					return "RP_Cronus_Hatak";
				case -9017:
					return "RP_Baal_Hatak";
				case -9018:
					return "RP_Anubis_Hatak";
				case -9019:
					return "RP_Lord_Yu_Hatak";
				case -9020:
					return "RP_NakaiShip";
				case -9021:
					return "RP_StructureTemplates";
				default:
					return "ERROR";
							
			}
	}
	//TODO: figure out celestial angle from coords
	
	@Override
	public boolean isPlanet() {
		return false;
	}
	
	
	public int getAverageGroundLevel() {
		return 0;
	}
	
	@Override
	public IChunkGenerator createChunkGenerator() {
		return new ChunkProviderSpace(this.world, this.world.getSeed());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IRenderHandler getSkyRenderer() {
		
		//Maybe a little hacky
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		if(player != null)
		{
			Entity e = player.getRidingEntity();
			if(e instanceof EntityRocket)
			{
				if(((EntityRocket)e).getInSpaceFlight())
				{
					if(!(skyRender instanceof RenderSpaceTravelSky))
						skyRender = new RenderSpaceTravelSky();
					return skyRender;
				}
			}
		}
		
		
		if(ARConfiguration.getCurrentConfig().stationSkyOverride)
			return (skyRender == null || !(skyRender instanceof RenderSpaceSky)) ? skyRender = new RenderSpaceSky() : skyRender;
		
		return super.getSkyRenderer();
	}
	
	@Override
	public float getAtmosphereDensity(BlockPos pos) {
		return 0;
	}
	
	@Override
	public float calculateCelestialAngle(long worldTime, float p_76563_3_) {
		return AdvancedRocketry.proxy.calculateCelestialAngleSpaceStation();
	}

	@Override
	public float getSunBrightness(float partialTicks) {
		DimensionProperties properties = getDimensionProperties(Minecraft.getMinecraft().player.getPosition());
		SpaceStationObject spaceStation = (SpaceStationObject) getSpaceObject(Minecraft.getMinecraft().player.getPosition());

		if (spaceStation != null) {
			//Vary brightness depending upon sun luminosity and planet distance
			//This takes into account how eyes work, that they're not linear in sensing light
			float preWarpBrightnessMultiplier = (float) AstronomicalBodyHelper.getPlanetaryLightLevelMultiplier(AstronomicalBodyHelper.getStellarBrightness(properties.getStar(), properties.getSolarOrbitalDistance()));
			//Warp is no light, because there are no stars
			return (spaceStation.isWarping()) ? (float) 0.0 : preWarpBrightnessMultiplier * world.getSunBrightnessBody(partialTicks);
		}
		return 0;
	}

	@Override
	protected void init() {
		this.hasSkyLight=true;
		world.getWorldInfo().setTerrainType(AdvancedRocketry.spaceWorldType);
		
		this.biomeProvider = new BiomeProviderSingle(AdvancedRocketryBiomes.spaceBiome);//new ChunkManagerPlanet(worldObj, worldObj.getWorldInfo().getGeneratorOptions(), DimensionManager.getInstance().getDimensionProperties(worldObj.provider.getDimension()).getBiomes());
		
	}
	
	public ISpaceObject getSpaceObject(BlockPos pos) {
		return SpaceObjectManager.getSpaceManager().getSpaceStationFromBlockCoords(pos);
	}
	
	@Override
	public DimensionProperties getDimensionProperties(BlockPos pos) {
		ISpaceObject spaceObject = getSpaceObject(pos);
		if(spaceObject != null)
			return (DimensionProperties)spaceObject.getProperties();
		return DimensionManager.defaultSpaceDimensionProperties;
	}
}
