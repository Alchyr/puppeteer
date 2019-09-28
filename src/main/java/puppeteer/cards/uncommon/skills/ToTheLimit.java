package puppeteer.cards.uncommon.skills;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.powers.DexterityPower;
import puppeteer.abstracts.AbstractDoll;
import puppeteer.abstracts.BaseCard;
import puppeteer.actions.character.BlockChargeAction;
import puppeteer.actions.character.DestroyDollAction;
import puppeteer.enums.CardTargetEnums;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class ToTheLimit extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "ToTheLimit",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DEBUFF = 1;

    public ToTheLimit()
    {
        super(cardInfo, true);

        setMagic(DEBUFF);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        AlwaysRetainField.alwaysRetain.set(this, true);

    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, -this.magicNumber), -this.magicNumber));

        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(2));
    }
}