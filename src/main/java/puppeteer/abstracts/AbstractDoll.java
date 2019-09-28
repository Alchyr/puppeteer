package puppeteer.abstracts;

import basemod.animations.SpineAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import puppeteer.fields.DollFields;
import puppeteer.util.AnimationData;
import puppeteer.util.HitboxHelper;
import puppeteer.util.MathHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public abstract class AbstractDoll {
    private static final float NUM_Y_OFFSET = 75 * Settings.scale;

    private static final float BOB_SIZE = 4.0f * Settings.scale;
    private static final float BOB_RATE = 0.75f;

    private static final float POWER_ICON_PADDING_X = 48.0F * Settings.scale;

    private static final int RETICLE_W = 36;
    private static final float RETICLE_OFFSET_DIST = 15.0F * Settings.scale;

    public float x;
    public float y;

    private float startX;
    private float startY;
    private float targetX;
    private float targetY;
    private float moveTimeScale;
    private float moveProgress;

    private float bobTime;
    private float bobEffect;

    public Hitbox hb;

    private String name;
    private String[] description;

    public int charge;

    public AbstractMonster closest;

    private HashMap<SpineAnimation, AnimationData> animationMap = new HashMap<>();

    public static SkeletonMeshRenderer sr = new SkeletonMeshRenderer();

    private TextureAtlas atlas;

    protected SpineAnimation animation;

    public SpineAnimation baseAnimation;
    public String baseAnimationName;

    private SpineAnimation nextAnimation;
    private String nextAnimationName;

    public boolean autoReset;

    private Skeleton skeleton;
    private AnimationState state;
    private AnimationStateData stateData;

    private static Color powerColor = Color.WHITE.cpy();

    public boolean autoFace = true; //automatically face nearest enemy
    protected boolean flipHorizontal;
    public float reticleAlpha;
    private Color reticleColor;
    private Color reticleShadowColor;
    public boolean reticleRendered;
    private float reticleOffset;
    private float reticleAnimTimer;

    public ArrayList<AbstractPower> powers;

    public AbstractDoll(String name, String[] description, int charge, TextureAtlas atlas, float x, float y, SpineAnimation baseAnimation, String animationName, float width, float height)
    {
        this.name = name;
        this.description = description;
        this.charge = charge;

        this.powers = new ArrayList<>();

        this.atlas = atlas;
        this.x = x;
        this.y = y;
        startX = x;
        startY = y;
        targetX = x;
        targetY = y;

        moveTimeScale = 1.0f;
        moveProgress = 1.0f;

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

        this.reticleAlpha = 0.0F;
        this.reticleColor = new Color(1.0F, 1.0F, 1.0F, 0.0F);
        this.reticleShadowColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
        this.reticleRendered = false;
        this.reticleOffset = 0.0F;
        this.reticleAnimTimer = 0.0F;

        getClosestMonster();
    }

    public void setPosition(float x, float y)
    {
        this.x = x;
        this.y = y;
        targetX = x;
        targetY = y;

        this.hb.move(x, y);
    }

    public void startMove(float targetX, float targetY, float speed)
    {
        this.startX = x;
        this.startY = y;
        this.targetX = targetX;
        this.targetY = targetY;

        this.moveProgress = 0.0f;

        float dist = MathHelper.dist(x, targetX, y, targetY);
        dist /= speed * Settings.scale; //speed is pixels per second
        //dist is now number of seconds of travel needed
        this.moveTimeScale = 1 / dist;
    }

    public void onEndTurn()
    {

    }

    public float getMoveProgress()
    {
        if (moveProgress >= 1)
        {
            return 1;
        }
        if (startX != targetX)
            return (x - startX) / (targetX - startX);

        if (startY != targetY)
            return (y - startY) / (targetY - startY);

        return 1; //start and end are same??
    }

    public void update()
    {
        if (moveProgress < 1)
        {
            moveProgress += Gdx.graphics.getDeltaTime() * moveTimeScale;

            if (moveProgress > 1.0f)
                moveProgress = 1.0f;

            this.x = Interpolation.exp10In.apply(startX, targetX, moveProgress);
            this.y = Interpolation.exp10In.apply(startY, targetY, moveProgress);

            this.hb.move(x, y);
        }

        hb.update();

        if (this.hb.hovered) {
            TipHelper.renderGenericTip(this.x + 96.0F * Settings.scale, this.y + 64.0F * Settings.scale, this.name, this.description[0] + charge + this.description[1]);
            //TipHelper.queuePowerTips();
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

        updateReticle();
    }

    public void render(SpriteBatch sb)
    {
        this.render(sb, true, Color.WHITE);
    }

    public void render(SpriteBatch sb, boolean showCharge, Color color)
    {
        if (autoFace && closest != null)
        {
            this.flipHorizontal = closest.hb.cX < this.x;
        }
        this.state.update(Gdx.graphics.getDeltaTime());
        this.state.apply(this.skeleton);
        this.skeleton.updateWorldTransform();
        this.skeleton.setPosition(x, y + bobEffect);
        this.skeleton.setColor(color);
        this.skeleton.setFlip(this.flipHorizontal, false);
        sb.end();

        CardCrawlGame.psb.begin();
        sr.draw(CardCrawlGame.psb, this.skeleton);
        CardCrawlGame.psb.end();

        sb.begin();
        sb.setBlendFunction(770, 771);

        if (this.equals(DollFields.targetDoll.get(AbstractDungeon.player)))
        {
            renderReticle(sb);
        }

        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.charge), this.x, this.y + this.bobEffect / 2.0F + NUM_Y_OFFSET, Color.WHITE, Settings.scale);

        this.renderPowerIcons(sb, this.x, this.hb.y + this.hb.height);
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

    public AbstractMonster getClosestMonster()
    {
        closest = HitboxHelper.getClosestMonster(this.x, this.y);
        return closest;
    }

    private void renderPowerIcons(SpriteBatch sb, float x, float y) {
        float offset = 10.0F * Settings.scale;

        Iterator var5;

        for (AbstractPower p : this.powers)
        {
            p.renderIcons(sb, x + offset, y - 48.0F * Settings.scale, powerColor);
            offset += POWER_ICON_PADDING_X;
        }

        offset = 0.0F;

        for (AbstractPower p : this.powers)
        {
            p.renderAmount(sb, x + offset + 32.0F * Settings.scale, y - 66.0F * Settings.scale, powerColor);
            offset += POWER_ICON_PADDING_X;
        }

    }

    public void renderReticle(SpriteBatch sb) {
        this.reticleRendered = true;// 958
        this.renderReticleCorner(sb, -this.hb.width / 2.0F + this.reticleOffset, this.hb.height / 2.0F - this.reticleOffset, false, false);// 961
        this.renderReticleCorner(sb, this.hb.width / 2.0F - this.reticleOffset, this.hb.height / 2.0F - this.reticleOffset, true, false);// 962
        this.renderReticleCorner(sb, -this.hb.width / 2.0F + this.reticleOffset, -this.hb.height / 2.0F + this.reticleOffset, false, true);// 963
        this.renderReticleCorner(sb, this.hb.width / 2.0F - this.reticleOffset, -this.hb.height / 2.0F + this.reticleOffset, true, true);// 964
    }

    protected void updateReticle() {
        if (this.reticleRendered) {
            this.reticleRendered = false;
            this.reticleAlpha += Gdx.graphics.getDeltaTime() * 3.0F;
            if (this.reticleAlpha > 1.0F) {
                this.reticleAlpha = 1.0F;
            }

            this.reticleAnimTimer += Gdx.graphics.getDeltaTime();
            if (this.reticleAnimTimer > 1.0F) {
                this.reticleAnimTimer = 1.0F;
            }

            this.reticleOffset = Interpolation.elasticOut.apply(RETICLE_OFFSET_DIST, 0.0F, this.reticleAnimTimer);
        } else {
            this.reticleAlpha = 0.0F;
            this.reticleAnimTimer = 0.0F;
            this.reticleOffset = RETICLE_OFFSET_DIST;
        }
    }

    private void renderReticleCorner(SpriteBatch sb, float x, float y, boolean flipX, boolean flipY) {
        this.reticleShadowColor.a = this.reticleAlpha / 4.0F;
        sb.setColor(this.reticleShadowColor);
        sb.draw(ImageMaster.RETICLE_CORNER, this.hb.cX + x - 18.0F + 4.0F * Settings.scale, this.hb.cY + y - 18.0F - 4.0F * Settings.scale, 18.0F, 18.0F, 36.0F, 36.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 36, 36, flipX, flipY);
        this.reticleColor.a = this.reticleAlpha;
        sb.setColor(this.reticleColor);
        sb.draw(ImageMaster.RETICLE_CORNER, this.hb.cX + x - 18.0F, this.hb.cY + y - 18.0F, 18.0F, 18.0F, 36.0F, 36.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 36, 36, flipX, flipY);
    }
}
