/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zmaster587.advancedRocketry.stargatemc;

import net.minecraft.util.math.BlockPos;
import scala.xml.dtd.impl.WordExp.Letter;
import zmaster587.advancedRocketry.dimension.DimensionManager;
import zmaster587.advancedRocketry.dimension.DimensionProperties;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;


public enum Galaxy {

	MilkyWay("PL", 0,0,15000),
	Pegasus("ML", 50000,32000,6000),
	Ida("IL", 75000,-16000,5200),
	Othala("OL", 65000,-42000,5500),
	Destiny("1D", 900000,-97000,4200),
	Alterran("HO", 980000,93000,5200),
	GalacticVoid("XL", 0,0,1000000);
	
	String identifier;
	int centerX;
	int centerZ;
	int diameter;
	
	Galaxy(String identifier, int centerX, int centerZ, int diameter) {
		this.identifier = identifier;
		this.diameter = diameter;
		this.centerX = centerX;
		this.centerZ = centerZ;
	}
	
	public String getIdentifier() {
		return this.identifier;
	}

	public int getDiameter() {
		return this.diameter;
	}

	public int getRadius() {
		return getDiameter()/2;
	}
	public int getCenterX() {
		return this.centerX;
	}

	public int getCenterZ() {
		return this.centerZ;
	}

	public static Galaxy getRandom() {
   		int pick = new Random().nextInt(Galaxy.values().length);
		while (Galaxy.values()[pick].equals(Galaxy.GalacticVoid)) {
			pick = new Random().nextInt(Galaxy.values().length);
		}
    	return Galaxy.values()[pick];
	}

	public ArrayList<Integer> getRandomStellarCoordinates() {
		Random r = new Random();
		int x = r.nextInt(getRadius());
		int z = r.nextInt(getRadius());
		if (r.nextBoolean()) {
			x *= -1;
		}
		if (r.nextBoolean()) {
			z *= -1;
		}
		x = getCenterX() + x;
		z = getCenterZ() + z;
		ArrayList<Integer> coords = new ArrayList<>();
		coords.add(x);
		coords.add(z);
		return coords;
	}

	public String getRandomStarName() {
		return DimensionManager.getInstance().getAlphaNumericString(8);
	}

	public static Galaxy forStellarCoordinates(int x, int z) {
		for (Galaxy g : Galaxy.values()) {
			if (g.equals(Galaxy.GalacticVoid)) continue;
			if (x < g.getCenterX()-g.getRadius()) continue;
			if (x > g.getCenterX()+g.getRadius()) continue;
			if (z < g.getCenterZ()-g.getRadius()) continue;
			if (z > g.getCenterZ()+g.getRadius()) continue;
			return g;
		}
		return Galaxy.GalacticVoid;
	}

 
	public static Galaxy forIdentifier(String s) {
		for (Galaxy g : Galaxy.values()) {
			if (s.equals(g.getIdentifier())) return g;
		}
		return Galaxy.GalacticVoid;
	}
	
	public static Galaxy forAddress(String s) {
		for (Galaxy g : Galaxy.values()) {
			if (s.endsWith(g.getIdentifier())) return g;
		}
		return Galaxy.GalacticVoid;
	}
	
    public static String getString() {
        String string = "";
        for (Galaxy race : Galaxy.values()) {
            if (string.equals("")) {
                string += race.name();
            } else {
                string += ("," + race.name());
            }
        }
        return string;
    }
	public static Galaxy forDimensionId(int id, BlockPos pos) {
		try {
			DimensionProperties props = DimensionManager.getEffectiveDimId(id,  pos);
			if (props != null) return forStellarCoordinates(props.getStar().getPosX(), props.getStar().getPosZ());
			return Galaxy.GalacticVoid;
		} catch (Exception e) {
			return Galaxy.GalacticVoid;
		}
	}
	public static Galaxy forDimProps (DimensionProperties props) {
		try {
			if (props != null) return forStellarCoordinates(props.getStar().getPosX(), props.getStar().getPosZ());
			return Galaxy.GalacticVoid;
		} catch (Exception e) {
			return Galaxy.GalacticVoid;
		}
	}
}
