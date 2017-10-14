package net.ishchenko.bots.crongif;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: Max
 * Date: 10.04.2017
 * Time: 13:12
 */
public class CronGifBot {

    private static final Logger log = LoggerFactory.getLogger(CronGifBot.class);

    private final TelegramBot bot;

    public CronGifBot(String botToken) {
        bot = TelegramBotAdapter.build(botToken);
    }

    public void postScheduledGif(Long chatId, String caption, String url) {
        bot.execute(new SendGiphy(chatId, url).caption(caption), newLoggingCallback());
    }

    private Callback<SendGiphy, SendResponse> newLoggingCallback() {
        return new Callback<SendGiphy, SendResponse>() {
            @Override
            public void onResponse(SendGiphy request, SendResponse response) {
                logOnResponse(request, response);
            }

            @Override
            public void onFailure(SendGiphy request, IOException e) {
                log.warn("Could not send message with parameters" + new TreeMap<>(request.getParameters()), e);
            }
        };
    }

    private void logOnResponse(BaseRequest<?, ?> request, BaseResponse response) {
        log.info(
                "Sent message ok: {}, errorCode: {}, desc: {}, request parameters: {}",
                response.isOk(),
                response.errorCode(),
                response.description(),
                new TreeMap<>(request.getParameters())
        );
    }

}
