package puppeteer.enums;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;

public class CharacterEnums {
    @SpireEnum
    public static AbstractPlayer.PlayerClass PUPPETEER;
    @SpireEnum(name = "PUPPETEER_COLOR")
    public static AbstractCard.CardColor PUPPETEER_CARD_COLOR;
    @SpireEnum(name = "PUPPETEER_COLOR") @SuppressWarnings("unused")
    public static CardLibrary.LibraryType LIBRARY_COLOR;
}