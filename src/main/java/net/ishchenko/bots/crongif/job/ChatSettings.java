package net.ishchenko.bots.crongif.job;

import java.util.List;

public class ChatSettings {

    private String cron;
    private List<String> tags;

    public void setCron(String cron) {
        this.cron = cron;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCron() {
        return cron;
    }

    public List<String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "ChatSettings{" +
                "cron='" + cron + '\'' +
                ", tags=" + tags +
                '}';
    }
}
