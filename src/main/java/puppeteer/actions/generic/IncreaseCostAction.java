package puppeteer.actions.generic;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

import static puppeteer.PuppeteerMod.makeID;

public class IncreaseCostAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("CostIncrease"));
    public static final String[] TEXT = uiStrings.TEXT;
    private AbstractPlayer p;
    private ArrayList<AbstractCard> cannotIncrease = new ArrayList();

    private int numCards;

    public IncreaseCostAction(int numCards, int amount) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;

        this.numCards = numCards;
        this.amount = amount;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.numCards >= p.hand.size()) {
                for (AbstractCard c : p.hand.group)
                {
                    if (canIncrease(c)) {
                        c.cost += this.amount;
                        c.costForTurn += this.amount;
                        c.isCostModified = true;
                        c.superFlash(Color.SLATE);
                    }
                }

                this.isDone = true;
                return;
            }



            for (AbstractCard c : p.hand.group) {
                if (!canIncrease(c)) {
                    cannotIncrease.add(c);
                }
            }

            if (cannotIncrease.size() >= p.hand.group.size()) {
                this.isDone = true;
                return;
            }

            if (this.p.hand.group.size() - this.cannotIncrease.size() <= this.numCards) {
                for (AbstractCard c : p.hand.group) {
                    if (canIncrease(c)) {
                        c.cost += this.amount;
                        c.costForTurn += this.amount;
                        c.isCostModified = true;
                        c.superFlash(Color.SLATE);
                    }
                }
            }

            this.p.hand.group.removeAll(this.cannotIncrease);

            if (this.p.hand.group.size() > this.numCards) {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.numCards, false, false, false, false);
                this.tickDuration();
                return;
            }

            if (this.p.hand.group.size() > 0) {
                for (AbstractCard c : p.hand.group) {
                    if (canIncrease(c)) {
                        c.cost += this.amount;
                        c.costForTurn += this.amount;
                        c.isCostModified = true;
                        c.superFlash(Color.SLATE);
                    }
                }
                this.returnCards();
                this.isDone = true;
            }
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group)
            {
                if (canIncrease(c)) {
                    c.cost += this.amount;
                    c.costForTurn += this.amount;
                    c.isCostModified = true;
                    c.superFlash(Color.SLATE);
                }

                this.p.hand.addToTop(c);
            }

            this.returnCards();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.isDone = true;
        }

        this.tickDuration();
    }

    private void returnCards() {
        for (AbstractCard c : this.cannotIncrease)
        {
            this.p.hand.addToTop(c);
        }

        this.p.hand.refreshHandLayout();
    }

    private boolean canIncrease(AbstractCard c) {
        return c.costForTurn >= 0 || c.cost >= 0;
    }
}