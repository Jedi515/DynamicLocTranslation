package code.patches;

import code.ModFile;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static code.ModFile.replaceDict;

@SpirePatch(clz = AbstractCard.class, method = "initializeDescription") //TODO: put it after cardmods
public class LocsPatch {
    public static void Prefix(AbstractCard __instance)
    {
        if (replaceDict.isEmpty()) return;
        Pattern pattern;
        Matcher matcher;
        String retVal = __instance.rawDescription;
        for (String key : replaceDict.keySet())
        {
            retVal = retVal.replaceAll(key.replaceAll("%", "\\"), replaceDict.get(key));
        }
        __instance.rawDescription = retVal;
    }
}
