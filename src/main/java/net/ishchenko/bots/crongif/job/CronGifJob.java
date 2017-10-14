package net.ishchenko.bots.crongif.job;

import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.entity.search.SearchGiphy;
import at.mukprojects.giphy4j.exception.GiphyException;
import net.ishchenko.bots.crongif.CronGifBot;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class CronGifJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(CronGifJob.class);

    public static final String KEY_CHAT_ID = CronGifJob.class.getName() + "_KEY_CHAT_ID";
    public static final String KEY_CHAT_TAGS = CronGifJob.class.getName() + "_KEY_CHAT_SETTINGS";
    public static final String KEY_BOT = CronGifJob.class.getName() + "_KEY_BOT";
    public static final String KEY_GIPHY = CronGifJob.class.getName() + "_KEY_GIPHY";
    private final Random random = new Random();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        Long chatId = (Long) context.getMergedJobDataMap().get(KEY_CHAT_ID);
        String[] tags = ((String) context.getMergedJobDataMap().get(KEY_CHAT_TAGS)).split(",");

        log.info("Processing chatId {} using tags {}", chatId, tags);

        String randomTag = tags[random.nextInt(tags.length)];
        try {

            log.info("Searching giphy for {}", randomTag);
            Giphy giphy = (Giphy) context.getScheduler().getContext().get(KEY_GIPHY);
            SearchGiphy gifDetails = giphy.searchByID(giphy.searchRandom(randomTag).getData().getId());

            log.info("Post scheduled gif. chatId:{}, tag:{}", chatId, randomTag);
            CronGifBot bot = (CronGifBot) context.getScheduler().getContext().get(KEY_BOT);
            bot.postScheduledGif(chatId, randomTag, gifDetails.getData().getImages().getFixedHeight().getMp4());

        } catch (GiphyException | SchedulerException e) {
            throw new JobExecutionException(e);
        }

    }

}
