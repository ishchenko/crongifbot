package net.ishchenko.bots.crongif;

import at.mukprojects.giphy4j.Giphy;
import net.ishchenko.bots.crongif.job.CronGifJob;
import net.ishchenko.bots.crongif.job.JobDirectoryScanListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.jobs.DirectoryScanJob;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class CronGifMain {

    public static void main(String[] args) throws SchedulerException {

        CronGifBot bot = new CronGifBot(args[0]);

        DirectSchedulerFactory schedulerFactory = DirectSchedulerFactory.getInstance();
        schedulerFactory.createVolatileScheduler(1);

        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.getContext().put(CronGifJob.KEY_BOT, bot);
        scheduler.getContext().put(CronGifJob.KEY_GIPHY, new Giphy("dc6zaTOxFJmzC"));
        scheduler.getContext().put("jobUpdateListener", new JobDirectoryScanListener(scheduler));
        scheduler.scheduleJob(
                newJob(DirectoryScanJob.class)
                        .withIdentity("DirectoryScanJob")
                        .usingJobData(DirectoryScanJob.DIRECTORY_NAME, "jobs")
                        .usingJobData(DirectoryScanJob.DIRECTORY_SCAN_LISTENER_NAME, "jobUpdateListener")
                        .build(),
                newTrigger()
                        .withSchedule(simpleSchedule().withIntervalInMinutes(1).repeatForever())
                        .build()
        );
        scheduler.start();


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                scheduler.shutdown(true);
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }));

    }

}
