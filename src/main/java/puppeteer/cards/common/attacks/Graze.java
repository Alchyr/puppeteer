package puppeteer.cards.common.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Cleave;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import puppeteer.abstracts.BaseCard;
import puppeteer.cards.summon.SummonShanghai;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class Graze extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Graze",
            1,
            CardType.ATTACK,
            CardTarget.ALL_ENEMY,
            CardRarity.COMMON);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 5;
    private static final int UPG_DAMAGE = 3;

    public Graze() {
        super(cardInfo, false);

        setDamage(DAMAGE, UPG_DAMAGE);

        this.isMultiDamage = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new CleaveEffect()));
        damageAll(AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        addToBot(new MakeTempCardInHandAction(new SummonShanghai(), 1));
    }

    public AbstractCard makeCopy() {
        return new Graze();
    }
}