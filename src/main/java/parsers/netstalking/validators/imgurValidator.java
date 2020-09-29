package parsers.netstalking.validators;

import utils.httpUtil;

import java.util.List;

public class imgurValidator implements validator {

    public String validate(String link) {
        String redirect= httpUtil.getRedirect(link);
        if (redirect==null)return link;
        throw new RuntimeException("invalid link");
    }

    public List<String> validate(List<String> links) {
        return null;
    }
}
