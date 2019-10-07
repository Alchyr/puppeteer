package puppeteer.cards.uncommon.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.defect.RecycleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.Blizzard;
import com.megacrit.cardcrawl.cards.colorless.MindBlast;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import puppeteer.abstracts.BaseCard;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class DevilryLightRay extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "DevilryLightRay",
            2,
            CardType.ATTACK,
            CardTarget.ALL_ENEMY,
            CardRarity.UNCOMMON);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int UPG_COST = 1;

    private static final int DAMAGE = 0;

    public DevilryLightRay() {
        super(cardInfo, false);

        setCostUpgrade(UPG_COST);
        setDamage(DAMAGE);

        this.isMultiDamage = true;
    }

    @Override
    public void applyPowers() {
        this.baseDamage = 0;
        for (AbstractCard c : AbstractDungeon.player.hand.group)
        {
            if (c.freeToPlayOnce || c.cost < -1)
            {
                continue;
            }
            if (c.costForTurn >= 0)
            {
                this.baseDamage += c.costForTurn;
            }
            else if (c.cost == -1) {
                this.baseDamage += EnergyPanel.getCurrentEnergy();
            }
        }
        super.applyPowers();

        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);

        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new MindblastEffect(p.dialogX, p.dialogY, p.flipHorizontal)));
        damageAll(AbstractGameAction.AttackEffect.FIRE);
    }

    public AbstractCard makeCopy() {
        return new DevilryLightRay();
    }
}