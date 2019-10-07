package puppeteer.cards.rare.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.GoldenSlashEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import puppeteer.abstracts.BaseCard;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class GrandGuignol extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "GrandGuignol",
            1,
            CardType.ATTACK,
            CardTarget.NONE,
            CardRarity.RARE);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 0;
    private static final int MAGIC = 11;
    private static final int UPG_MAGIC = 4;

    public GrandGuignol() {
        super(cardInfo, false);

        setDamage(DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public boolean hasEnoughEnergy() {
        boolean hasEnergy = super.hasEnoughEnergy();
        //why does hasEnoughEnergy check so many things other than energy >.>
        //this will be my temporary solution. If this has issues, I'll put in more effort.
        if (!hasEnergy && TEXT[11].equals(this.cantUseMessage) && EnergyPanel.totalCount >= 1)
            return true;

        return hasEnergy;
    }

    @Override
    public void applyPowers() {
        this.baseDamage = this.magicNumber * (freeToPlayOnce ? 0 : this.costForTurn);
        if (this.baseDamage < 0) //I don't trust people :)
            this.baseDamage = 0;

        super.applyPowers();

        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.baseDamage = this.magicNumber * (freeToPlayOnce ? 0 : this.costForTurn);
        if (this.baseDamage < 0) //I don't trust people :)
            this.baseDamage = 0;

        super.calculateCardDamage(mo);

        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            if (this.costForTurn >= 5)
            {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new WeightyImpactEffect(m.hb.cX, m.hb.cY)));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
            }
            if (this.costForTurn >= 9)
            {
                addToBot(new VFXAction(new ExplosionSmallEffect(m.hb.cX, m.hb.cY)));
            }
            if (this.costForTurn >= 4)
            {
                addToBot(new VFXAction(new GoldenSlashEffect(m.hb.cX + 30.0f, m.hb.cY, true)));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
                addToBot(new VFXAction(new GoldenSlashEffect(m.hb.cX, m.hb.cY + 30.0f, false)));
            }
            else if (this.costForTurn >= 2)
            {
                addToBot(new VFXAction(new GoldenSlashEffect(m.hb.cX, m.hb.cY, false)));
            }
        }
        damageSingle(m, AbstractGameAction.AttackEffect.FIRE);
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    public AbstractCard makeCopy() {
        return new GrandGuignol();
    }
}