package zmaster587.advancedRocketry.dimension;

import zmaster587.libVulpes.util.HashedBlockPosition;

public class watersourcelocked{
    public HashedBlockPosition pos;
    public int timer;
    watersourcelocked(HashedBlockPosition pos){
        this.pos = pos;
        this.reset_timer();
    }
    public void reset_timer(){
        this.timer = 20*180;
    }
}
