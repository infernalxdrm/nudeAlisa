package parsers.netstalking.validators;

import network.utils;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class youtubeValidator implements validator {
    private static final String nonvalid_link="<title>www.youtube.com</title>";
    @Override
    public List<String> validate(List<String> links) {
        return null;
    }

    @Override
    public String validate(String link) throws RuntimeException {
        AtomicBoolean isInvalid= new AtomicBoolean(false);
        utils.getHtmlStream(link).forEach(s -> {
            if (s.contains(nonvalid_link)) isInvalid.set(true);
        });
        if (!isInvalid.get())return link;
        throw new RuntimeException("invalid link");
    }
}
