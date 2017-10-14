package net.ishchenko.bots.crongif;

import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.entity.search.SearchGiphy;
import at.mukprojects.giphy4j.exception.GiphyException;

/**
 * Created with IntelliJ IDEA.
 * User: Max
 * Date: 13.04.2017
 * Time: 13:18
 */
public class SamplePostMain {
    public static void main(String[] args) throws GiphyException {

        CronGifBot bot = new CronGifBot(args[0]);
        Giphy giphy = new Giphy("dc6zaTOxFJmzC");
        String tag = "eat";
        SearchGiphy gifDetails = giphy.searchByID(giphy.searchRandom(tag).getData().getId());

        bot.postScheduledGif(Long.valueOf(args[1]), tag, gifDetails.getData().getImages().getFixedHeight().getMp4());

    }
}
