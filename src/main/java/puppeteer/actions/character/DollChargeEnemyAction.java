package puppeteer.actions.character;

import com.badlogic.gdx.tools.particleeditor.Chart;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import javafx.util.Pair;
import puppeteer.abstracts.AbstractDoll;
import puppeteer.abstracts.AbstractMagic;
import puppeteer.patches.DollAndMagicRenderPatch;
import puppeteer.util.HitboxHelper;
import puppeteer.util.MathHelper;

import java.util.ArrayList;


public class DollChargeEnemyAction extends AbstractGameAction {
    private static final float CHARGE_DIST = 250.0f * Settings.scale; //will go this many pixels past the target
    private static final float SPEED = 2300.0f;

    public enum DollDamageType {
        MULTIPLIER,
        BONUS
    }

    private AbstractDoll doll;
    private AbstractMonster m;
    private DollDamageType type;
    private AbstractMagic attack;

    private boolean first;

    private ArrayList<Pair<AbstractMonster, Float>> targetPositions;

    public DollChargeEnemyAction(AbstractDoll d, AbstractMonster target, AbstractMagic magic, DollDamageType type, int amount)
    {
        targetPositions = new ArrayList<>();

        this.m = target;
        this.amount = amount;
        this.type = type;

        this.attack = magic;

        this.doll = d;

        first = true;

        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (first)
        {
            first = false;

            if (m.isDeadOrEscaped())
            {
                this.m = HitboxHelper.getClosestMonster(doll.hb.cX, doll.hb.cY);
            }
            if (m == null || m.isDeadOrEscaped())
            {
                this.isDone = true;
                return;
            }

            attack.setOrigin(doll.hb.cX, doll.hb.cY);
            Chart.Point target = getTargetPoint(doll, m);
            attack.setTarget(target.x, target.y);

            targetPositions.clear();
            float totalDist = MathHelper.dist(target.x, doll.hb.cX, target.y, doll.hb.cY);

            for (AbstractMonster m : attack.getTargets())
            {
                float targetDist = MathHelper.dist(m.hb.cX, doll.hb.cX, m.hb.cY, doll.hb.cY);
                targetPositions.add(new Pair<>(m, targetDist / totalDist));
            }

            doll.startMove(target.x, target.y, SPEED);
        }

        DollAndMagicRenderPatch.renderMagic(attack);

        targetPositions.removeIf((p)->
        {
            if (p.getValue() < doll.getMoveProgress())
            {
                return false;
            }

            AbstractMonster m = p.getKey();
            m.damageFlash = true;
            m.damageFlashFrames = 3;
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.SLASH_HORIZONTAL, false));

            m.damage(new DamageInfo(AbstractDungeon.player, getDamage(), DamageInfo.DamageType.THORNS));

            return true;
        });

        if (doll.getMoveProgress() >= 1)
        {
            //damage any leftover targets
            for (Pair<AbstractMonster, Float> p : targetPositions)
            {
                AbstractMonster m = p.getKey();
                m.damageFlash = true;
                m.damageFlashFrames = 3;
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.SLASH_HORIZONTAL, false));

                m.damage(new DamageInfo(AbstractDungeon.player, getDamage(), DamageInfo.DamageType.THORNS));
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }

            doll.changeAnimation(doll.baseAnimation, doll.baseAnimationName);
            doll.autoFace = true;
            this.isDone = true;
        }
    }

    private int getDamage()
    {
        switch (type)
        {
            case BONUS:
               return doll.charge + this.amount;
            case MULTIPLIER:
                return doll.charge * this.amount;
        }
        return 0;
    }

    public static Chart.Point getTargetPoint(AbstractDoll d, AbstractMonster m)
    {
        float tX = m.hb.cX;
        float tY = m.hb.cY;

        float dX = tX - d.hb.cX;
        float dY = tY - d.hb.cY;

        float dist = MathHelper.dist(tX, d.hb.cX, tY, d.hb.cY);

        //logger.info("Spearhead distance: " + dist);

        //normalize
        dX /= dist;
        dY /= dist;

        //dX, dY is now a vector with length of 1 in direction of target
        tX = d.hb.cX + dX * (dist + CHARGE_DIST);
        tY = d.hb.cY + dY * (dist + CHARGE_DIST);

        //logger.info("Final distance: " + MathHelper.dist(tX, d.hb.cX, tY, d.hb.cY));

        tX = Math.max(0, Math.min(Settings.WIDTH, tX));
        tY = Math.max(250.0F * Settings.scale, Math.min(Settings.CARD_DROP_END_Y, tY));

        return new Chart.Point(tX, tY);
    }
}
