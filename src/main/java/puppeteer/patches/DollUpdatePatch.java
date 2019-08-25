package puppeteer.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import puppeteer.abstracts.AbstractDoll;
import puppeteer.fields.DollFields;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "combatUpdate"
)
public class DollUpdatePatch {
    @SpirePostfixPatch
    public static void updateDolls(AbstractPlayer __instance)
    {
        for (AbstractDoll d : DollFields.dolls.get(__instance))
        {
            d.update();
        }
    }
}
