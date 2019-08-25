package puppeteer.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import puppeteer.abstracts.AbstractDoll;
import puppeteer.fields.DollFields;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "render",
        paramtypez = { SpriteBatch.class }
)
public class DollRenderPatch {
    @SpirePostfixPatch
    public static void renderDolls(AbstractPlayer __instance, SpriteBatch sb)
    {
        sb.setColor(Color.WHITE);
        for (AbstractDoll d : DollFields.dolls.get(__instance))
        {
            d.render(sb);
        }
    }
}
