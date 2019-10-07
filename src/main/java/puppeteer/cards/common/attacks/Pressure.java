package puppeteer.cards.common.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.BaseCard;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class Pressure extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Pressure",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.COMMON);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 8;
    private static final int UPG_DAMAGE = 2;

    private static final int MAGIC = 12;
    private static final int UPG_MAGIC = 3;

    public Pressure() {
        super(cardInfo, false);

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        int origBase = this.baseDamage;
        int origDmg = this.damage;
        boolean isDmgModified = this.isDamageModified;

        this.baseDamage = this.baseMagicNumber;
        super.applyPowers();

        this.magicNumber = this.damage;
        this.isMagicNumberModified = this.isDamageModified;

        this.baseDamage = origBase;
        this.damage = origDmg;
        this.isDamageModified = isDmgModified;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);

        int origBase = this.baseDamage;
        int origDmg = this.damage;
        boolean isDmgModified = this.isDamageModified;

        this.baseDamage = this.baseMagicNumber;
        super.calculateCardDamage(mo);

        this.magicNumber = this.damage;
        this.isMagicNumberModified = this.isDamageModified;

        this.baseDamage = origBase;
        this.damage = origDmg;
        this.isDamageModified = isDmgModified;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);

        if (this.costForTurn >= 2 && !this.freeToPlayOnce)
        {
            damageSingle(m, this.magicNumber, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        }
    }

    public AbstractCard makeCopy() {
        return new Pressure();
    }
}