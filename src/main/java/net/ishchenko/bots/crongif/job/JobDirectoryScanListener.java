package net.ishchenko.bots.crongif.job;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.jobs.DirectoryScanListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.GroupMatcher.groupEquals;

public class JobDirectoryScanListener implements DirectoryScanListener {

    private static final Logger log = LoggerFactory.getLogger(JobDirectoryScanListener.class);

    private final Gson gson = new Gson();
    private final Scheduler scheduler;

    public JobDirectoryScanListener(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void filesUpdatedOrAdded(File[] updatedFiles) {

        for (File updatedFile : updatedFiles) {

            log.info("Processing job config {}", updatedFile);
            List<ChatSettings> chatSettings = loadChatSettings(updatedFile);

            log.info("Found {} settings for job config {}", chatSettings.size());
            String chatId = updatedFile.getName();
            try {

                log.info("Clearing jobs for group {}", chatId);
                scheduler.deleteJobs(new ArrayList<>(scheduler.getJobKeys(groupEquals(chatId))));

                for (int i = 0; i < chatSettings.size(); i++) {
                    log.info("Scheduling for chat {}: {}", chatId, chatSettings);
                    scheduler.scheduleJob(buildNewJob(chatId, i), buildNewTrigger(chatSettings.get(i)));
                }

            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }

        }

    }

    private JobDetail buildNewJob(String chatId, int index) {
        return newJob(CronGifJob.class)
                .withIdentity(chatId + "-" + index, chatId)
                .usingJobData(CronGifJob.KEY_CHAT_ID, Long.valueOf(chatId))
                .build();
    }

    private Trigger buildNewTrigger(ChatSettings settings) {
        return newTrigger()
                .withSchedule(cronSchedule(settings.getCron()))
                .usingJobData(CronGifJob.KEY_CHAT_TAGS, String.join(",", settings.getTags()))
                .build();
    }

    private List<ChatSettings> loadChatSettings(File updatedFile) {
        List<ChatSettings> chatSettings;
        try (FileInputStream in = new FileInputStream(updatedFile)) {
            chatSettings = gson.fromJson(new InputStreamReader(in, "UTF-8"), new TypeToken<List<ChatSettings>>() { }.getType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return chatSettings;
    }

}
