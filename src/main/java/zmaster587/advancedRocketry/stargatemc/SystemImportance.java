package zmaster587.advancedRocketry.stargatemc;

public enum SystemImportance {
    // Not visited.
	None(0),
	// Spent 15 minutes of playime on a world.
	VeryLow(15),
	// Spent 1 hour of playtime on a world.
	Low(60),
	// Spent 2 hours of playtime on a world.
	Medium(120),
	// Spent 8 hours of playtime on a world.
	High(480),
	// Spent 1 day of playtime on  world.
	VeryHigh(1440),
	// Spent 7 days of play time on a world.
	Critical(10080);

	private int eventCounterMin = 0;

	SystemImportance(int eventCounter) {
		this.eventCounterMin = eventCounter;
	}

    public int getMinimum() {
        return this.eventCounterMin;
    }

    public SystemImportance getNextHighest() {
        int current = this.getMinimum();
        SystemImportance candidate = null;
        for (SystemImportance imp : SystemImportance.values()) {
            if (imp.getMinimum() > current && (candidate == null || candidate.getMinimum() > imp.getMinimum())) candidate = imp;
        }
        return candidate;
    }

    public static SystemImportance getForCounter(int evc) {
        SystemImportance current = null;
        for (SystemImportance imp : SystemImportance.values()) {
            if (evc < imp.getMinimum()) continue;
            if (current == null && evc >= imp.getMinimum() || current.getMinimum() < imp.getMinimum() && evc >= imp.getMinimum()) current = imp;
        }
        return current;
    }

    public Long getTimeTilDeath(Long sinceLastVisit) {
        return (getLifeTime() - sinceLastVisit);
    }

    public boolean shouldDie(Long sinceLastVisit) {
        return (getLifeTime() - sinceLastVisit <= 0);
    }

    public Long getLifeTime() {
        return Long.valueOf(60 * 1000 * 5);
        /*switch (this) {
            default:
                return Long.valueOf(3600000L); // If the system hasnt had at least 60 minutes of playtime, its life is 1 hour.
            case VeryLow:
                return Long.valueOf(28800000L); // If the system has had 15 minutes of playtime, its life is 8 hours.
            case Low:
                return Long.valueOf(259200000L); // If the system has had 60 minutes of playtime, its life is 1 day.
            case Medium:
                return Long.valueOf(604800000L); // If the system has had 2 hours of playtime, its life is 3 days.
            case High:
                return Long.valueOf(1209600000L); // If the system has had 8 hours of playtime, its life is 7 days.
            case VeryHigh:
                return Long.valueOf(2592000000L); // If the system has had 24 hours of playtime, its life is 14 days.
            case Critical:
                return Long.valueOf(7776000000L); // If the system has had 24 hours of playtime, its life is 30 days.
        }*/
    }
}