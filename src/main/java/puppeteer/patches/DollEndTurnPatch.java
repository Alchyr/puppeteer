package puppeteer.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;
import puppeteer.abstracts.AbstractDoll;
import puppeteer.actions.character.EndTurnDollAction;
import puppeteer.fields.DollFields;

@SpirePatch(
        clz = GameActionManager.class,
        method = "callEndOfTurnActions"
)
public class DollEndTurnPatch {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void dollEndTurn(GameActionManager __instance)
    {
        for (AbstractDoll d : DollFields.dolls.get(AbstractDungeon.player))
        {
            AbstractDungeon.actionManager.addToBottom(new EndTurnDollAction(d));
        }
    }



    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "hand");
            return LineFinder.findInOrder(ctBehavior, finalMatcher);
        }
    }
}
