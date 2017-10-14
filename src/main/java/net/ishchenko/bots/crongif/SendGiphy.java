package net.ishchenko.bots.crongif;

import com.pengrad.telegrambot.request.AbstractMultipartRequest;

public class SendGiphy extends AbstractMultipartRequest<SendGiphy> {

    public SendGiphy(Object chatId, String url) {
        super(chatId, url);
    }

    public SendGiphy caption(String caption) {
        return add("caption", caption);
    }

    @Override
    public String getMethod() {
        return "sendDocument";
    }

    @Override
    protected String getFileParamName() {
        return "document";
    }

    @Override
    public String getDefaultFileName() {
        return "giphy.mp4";
    }

    @Override
    public String getContentType() {
        return "image/mp4";
    }

}
