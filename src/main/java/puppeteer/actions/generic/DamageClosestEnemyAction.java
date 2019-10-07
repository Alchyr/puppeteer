package puppeteer.actions.generic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.util.HitboxHelper;

public class DamageClosestEnemyAction extends AbstractGameAction {
    private AbstractCard card;

    public DamageClosestEnemyAction(AbstractPlayer p, AbstractCard c)
    {
        this.source = p;
        this.card = c;
    }

    @Override
    public void update() {
        AbstractMonster m = HitboxHelper.getClosestMonster(this.source.hb.cX, this.source.hb.cY);
        if (m != null)
        {
            this.card.calculateCardDamage(m);
            AbstractDungeon.actionManager.addToTop(new DamageAction(m, new DamageInfo(AbstractDungeon.player, this.card.damage, this.card.damageTypeForTurn), AttackEffect.FIRE));
        }
        this.isDone = true;
    }
}
