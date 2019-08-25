package puppeteer.util;

import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;

public class AnimationData {
    public Skeleton skeleton;
    public AnimationStateData stateData;
    public AnimationState state;
    public AnimationState.TrackEntry trackEntry;

    public AnimationData(Skeleton s, AnimationStateData d, AnimationState state, AnimationState.TrackEntry e)
    {
        this.skeleton = s;
        this.stateData = d;
        this.state = state;
        this.trackEntry = e;
    }
}
