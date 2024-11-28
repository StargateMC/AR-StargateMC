// Generic Integration class for things we want AR to do that arent 'stock standard'
package zmaster587.advancedRocketry.stargatemc;


import zmaster587.advancedRocketry.AdvancedRocketry;
import zmaster587.advancedRocketry.api.dimension.solar.StellarBody;
import zmaster587.advancedRocketry.dimension.DimensionManager;
import zmaster587.advancedRocketry.api.dimension.IDimensionProperties;
import java.util.ArrayList;
import java.util.Random;


public class ARIntegration {

    // Cleanup Star System

    public static boolean cleanupStarSystem(int StarID) {

        StellarBody star = DimensionManager.getInstance().getStar(StarID);
        if (star == null) {
            AdvancedRocketry.logger.error("Attempting to cleanup star: " + StarID + " which no longer exists.");
            return false;
        }

        // Deletes planets, using existing logic to recurse to moons.
        for (IDimensionProperties prop  : star.getPlanets()) {
            AdvancedRocketry.logger.info("Deleting planet: " + prop.getId() + " for star: " + star.getId() + " and name: " + star.getName());
            DimensionManager.getInstance().deleteDimension(prop.getId());
        }
            AdvancedRocketry.logger.info("Deleting star: " + StarID + " with time left: " + star.getSystemImportance().getTimeTilDeath(star.timeSinceLastVisit()));
            DimensionManager.getInstance().removeStar(StarID);
        return true;
    }


    public static StellarBody generateStarSystem(Galaxy galaxy, int numPlanets, int numGasGiants) {
        StellarBody star = new StellarBody();
        Random r = new Random();
        star.setTemperature(r.nextInt(199) + 1);
        // Calculate a random float between 0.1 and 10.0
        float minValue = 0.1f;
        float maxValue = 10.0f;
        // The formula to calculate the random float is:
        // randomFloat = minValue + (randomValue * (maxValue - minValue))
        float randomValue = r.nextFloat();
        float randomFloat = minValue + (randomValue * (maxValue - minValue));
        star.setSize(randomFloat);
        ArrayList<Integer> coords = galaxy.getRandomStellarCoordinates();
        star.setPosX(coords.get(0));
        star.setPosZ(coords.get(1));
        star.setId(DimensionManager.getInstance().getNextFreeStarId());
        star.setBlackHole((r.nextFloat() == 0.5));
        if (r.nextFloat() > 0.7) { // 30% chance to spawn a non-single-star system.
            int extraStars = r.nextInt(3); // Systems will be either Single, binary or trinary.
            while (extraStars > 0) {
                StellarBody substar = new StellarBody();
                substar.setTemperature(r.nextInt(199) + 1);
                substar.setStarSeparation((extraStars * 10) + ((r.nextInt(10))));
                substar.setBlackHole((r.nextFloat() == 0.5));
                randomValue = r.nextFloat();
                randomFloat = minValue + (randomValue * (maxValue - minValue));
                star.setSize(randomFloat);
                star.addSubStar(substar);
                extraStars--;
            }
        }
        star.isRandomGenerated = true;
        String sys = null;
        String name = null;
        while (name == null || DimensionManager.getInstance().getStar(name) != null) {
            sys = DimensionManager.getInstance().getAlphaNumericString(2);
            name = galaxy.getIdentifier().substring(0,1) + sys + "SOL0" + galaxy.getIdentifier();
        }
        star.setName(name);
        DimensionManager.getInstance().addStar(star);
        DimensionManager.getInstance().generateRandomPlanets(DimensionManager.getInstance().getStar(name), numPlanets,numGasGiants);
        return DimensionManager.getInstance().getStar(name);
    }

}