package puppeteer.patches;
/*
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.RenderCustomDynamicVariable;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;

import java.util.regex.Pattern;

public class NoSpaceDynamicVariables {
    private static Pattern noSpacePattern = Pattern.compile("!(.+)\\+(.*) ");

    @SpirePatch(
            clz = RenderCustomDynamicVariable.Inner.class,
            method = "myRenderDynamicVariable"
    )
    public static class RenderNoSpaceVariable {

    }
}

Hmm... in card description rendering:

        if (tmp.length() == 4) { such as '!D! '
            start_x += this.renderDynamicVariable(tmp.charAt(1), start_x, draw_y, i, font, sb, (Character)null);
        } else if (tmp.length() == 5) { ??? '!D!. ' like this?
            start_x += this.renderDynamicVariable(tmp.charAt(1), start_x, draw_y, i, font, sb, tmp.charAt(3));
        }


*/
