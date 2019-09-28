package puppeteer.actions.generic;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;

public class DamageSpecificEnemiesAction extends AbstractGameAction {
    public int damage;
    private ArrayList<AbstractMonster> targets;
    private boolean firstFrame;

    public DamageSpecificEnemiesAction(AbstractCreature source, ArrayList<AbstractMonster> targets, int amount, AttackEffect effect, boolean isFast) {
        this.firstFrame = true;
        this.setValues(null, source, amount);
        this.damage = amount;
        this.targets = targets;
        this.actionType = ActionType.DAMAGE;
        this.damageType = DamageInfo.DamageType.THORNS;
        this.attackEffect = effect;
        if (isFast) {
            this.duration = Settings.ACTION_DUR_XFAST;
        } else {
            this.duration = Settings.ACTION_DUR_FAST;
        }
    }

    public DamageSpecificEnemiesAction(AbstractCreature source, ArrayList<AbstractMonster> targets, int amount, AttackEffect effect) {
        this(source, targets, amount, effect, false);
    }// 38

    public void update() {
        int i;
        if (this.firstFrame) {// 42
            boolean playedSfx = false;// 43

            for (AbstractMonster m : targets)
            {
                if (!m.isDying && m.currentHealth > 0 && !m.isEscaping) {
                    if (playedSfx) {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, this.attackEffect, true));
                    } else {
                        playedSfx = true;// 57
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, this.attackEffect));
                    }
                }
            }

            this.firstFrame = false;
        }

        this.tickDuration();
        if (this.isDone) {
            for (AbstractMonster m : targets)
            {
                if (!m.isDeadOrEscaped()) {
                    if (this.attackEffect == AttackEffect.POISON) {
                        m.tint.color.set(Color.CHARTREUSE);
                        m.tint.changeColor(Color.WHITE.cpy());
                    } else if (this.attackEffect == AttackEffect.FIRE) {
                        m.tint.color.set(Color.RED);
                        m.tint.changeColor(Color.WHITE.cpy());
                    }

                    m.damage(new DamageInfo(this.source, this.damage, this.damageType));
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }

            if (!Settings.FAST_MODE) {// 94
                AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
            }
        }

    }
}
