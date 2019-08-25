package puppeteer.abstracts;

import basemod.animations.SpineAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import puppeteer.util.AnimationData;

import java.util.HashMap;

public abstract class AbstractDoll {
    private static final float NUM_Y_OFFSET = 75 * Settings.scale;

    private static final float BOB_SIZE = 4.0f * Settings.scale;
    private static final float BOB_RATE = 0.75f;

    public float x;
    public float y;

    private float bobTime;
    private float bobEffect;

    public Hitbox hb;

    private String name;
    private String[] description;

    public int charge;

    private HashMap<SpineAnimation, AnimationData> animationMap = new HashMap<>();

    public static SkeletonMeshRenderer sr = new SkeletonMeshRenderer();

    private TextureAtlas atlas;

    private SpineAnimation animation;

    private SpineAnimation baseAnimation;
    private String baseAnimationName;

    private SpineAnimation nextAnimation;
    private String nextAnimationName;

    public boolean autoReset;

    private Skeleton skeleton;
    private AnimationState state;
    private AnimationStateData stateData;

    protected boolean flipHorizontal;

    public AbstractDoll(String name, String[] description, int charge, TextureAtlas atlas, float x, float y, SpineAnimation baseAnimation, String animationName, float width, float height)
    {
        this.name = name;
        this.description = description;
        this.charge = charge;

        this.atlas = atlas;
        this.x = x;
        this.y = y;

        this.bobTime = 0;
        this.bobEffect = 0;

        this.hb = new Hitbox(x - width / 2.0f, y - height / 2.0f, width, height);

        this.baseAnimation = baseAnimation;
        this.baseAnimationName = animationName;

        this.nextAnimation = null;
        this.nextAnimationName = "";

        this.autoReset = true;

        loadAnimation(baseAnimation, animationName);
        changeAnimation(baseAnimation, animationName);
    }

    public void onEndTurn()
    {

    }

    public void update()
    {
        hb.update();

        if (this.hb.hovered) {
            TipHelper.renderGenericTip(this.x + 96.0F * Settings.scale, this.y + 64.0F * Settings.scale, this.name, this.description[0] + charge + this.description[1]);
        }

        if (!this.animation.equals(baseAnimation)) {
            bobTime = 0;
            bobEffect = 0;
            if (animationMap.get(this.animation).trackEntry.isComplete()) {
                if (nextAnimation != null) { //if necessary convert this to an animation queue
                    changeAnimation(nextAnimation, nextAnimationName);
                    nextAnimation = null;
                }
                else if (autoReset) {
                    changeAnimation(baseAnimation, baseAnimationName);
                }
            }
        }
        else {
            bobEffect = MathUtils.sin(this.bobTime) * BOB_SIZE;
            bobTime += Gdx.graphics.getDeltaTime() * BOB_RATE;
        }
    }

    public void render(SpriteBatch sb)
    {
        this.render(sb, true);
    }

    public void render(SpriteBatch sb, boolean showCharge)
    {
        this.state.update(Gdx.graphics.getDeltaTime());
        this.state.apply(this.skeleton);
        this.skeleton.updateWorldTransform();
        this.skeleton.setPosition(x, y + bobEffect);
        //this.skeleton.setColor(Color.WHITE); if color is never changed, should be already correct
        this.skeleton.setFlip(this.flipHorizontal, false);
        sb.end();

        CardCrawlGame.psb.begin();
        sr.draw(CardCrawlGame.psb, this.skeleton);
        CardCrawlGame.psb.end();

        sb.begin();
        sb.setBlendFunction(770, 771);

        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.charge), this.x, this.y + this.bobEffect / 2.0F + NUM_Y_OFFSET, Color.WHITE, Settings.scale);
    }

    protected void loadAnimation(SpineAnimation anim, String animationName)
    {
        SkeletonJson json = new SkeletonJson(this.atlas);

        json.setScale(Settings.scale * anim.scale);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(anim.skeletonUrl));
        Skeleton skeleton = new Skeleton(skeletonData);
        skeleton.setColor(Color.WHITE);
        AnimationStateData stateData = new AnimationStateData(skeletonData);
        AnimationState state = new AnimationState(stateData);

        AnimationState.TrackEntry e = state.setAnimation(0, animationName, true);

        animationMap.put(anim, new AnimationData(skeleton, stateData, state, e));
    }

    public void changeAnimation(SpineAnimation next, String animationName)
    {
        this.animation = next;

        if (!animationMap.containsKey(next))
        {
            SkeletonJson json = new SkeletonJson(this.atlas);

            json.setScale(Settings.scale * next.scale);
            SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(next.skeletonUrl));
            this.skeleton = new Skeleton(skeletonData);
            this.skeleton.setColor(Color.WHITE);
            this.stateData = new AnimationStateData(skeletonData);
            this.state = new AnimationState(this.stateData);

            AnimationState.TrackEntry e = state.setAnimation(0, animationName, true);

            animationMap.put(next, new AnimationData(skeleton, stateData, state, e));
        }
        else
        {
            this.skeleton = animationMap.get(next).skeleton;
            this.stateData = animationMap.get(next).stateData;
            this.state = animationMap.get(next).state;
        }

        bobTime = 0;
        bobEffect = 0;

        animationMap.get(next).trackEntry.setTime(0);
    }

    public void setNextAnimation(SpineAnimation next, String nextName)
    {
        this.nextAnimation = next;
        this.nextAnimationName = nextName;
    }
}
