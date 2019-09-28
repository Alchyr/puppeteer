package puppeteer.util;

import com.badlogic.gdx.math.MathUtils;

public class MathHelper {
    public static float dist(float x1, float x2, float y1, float y2)
    {
        return (float) Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }

    public static float angle(float x1, float x2, float y1, float y2)
    {
        return MathUtils.atan2(y2 - y1, x2 - x1) * 180.0f / MathUtils.PI;
    }
}
