package puppeteer.actions.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.green.PiercingWail;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;
import puppeteer.abstracts.AbstractDoll;
import puppeteer.fields.DollFields;

public class LuminousDollsAction extends AbstractGameAction {
    private static final float MAX_OFFSET = 20 * Settings.scale;


    public LuminousDollsAction(int amt)
    {
        this.amount = amt;
    }

    @Override
    public void update() {
        int totalLoss = 0;
        int initial;

        for (AbstractDoll d : DollFields.dolls.get(AbstractDungeon.player))
        {
            initial = d.charge;
            d.charge -= this.amount;

            if (d.charge < 0)
                d.charge = 0;

            initial -= d.charge;

            totalLoss += initial;

            if (initial > 0)
            {
                for (int i = 0; i < 10; ++i)
                    AbstractDungeon.effectList.add(new LightFlareParticleEffect(d.x + MathUtils.random(-MAX_OFFSET, MAX_OFFSET), d.y + MathUtils.random(-MAX_OFFSET, MAX_OFFSET), Color.WHITE.cpy()));
            }
        }

        AbstractPlayer p = AbstractDungeon.player;

        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.hasPower(ArtifactPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(m, p, new GainStrengthPower(m, totalLoss), totalLoss, true, AttackEffect.NONE));
            }
        }

        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(m, p, new StrengthPower(m, -totalLoss), -totalLoss, true, AttackEffect.NONE));
        }


        this.isDone = true;
    }
}