package parsers.netstalking.validators;

import utils.httpUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class screenshootsValidator implements validator {
    private static final String nonvalid_link="<img class=\"no-click screenshot-image\" src=\"//st.prntscr.com/2020/08/01/0537/img/0_173a7b_211be8ff.png\"";
    public String validate(String link) {
        AtomicBoolean isInvalid= new AtomicBoolean(false);
        httpUtil.getHtmlStream(link).forEach(s -> {
            if (s.contains(nonvalid_link)) isInvalid.set(true);
        });
       //isInvalid=utils.getHtmlStream(link).allMatch(n->(n.contains(nonvalid_link)));
       if (!isInvalid.get())return link;
       throw new RuntimeException("invalid link");
    }

    public List<String> validate(List<String> links) {
        return null;
    }
}
