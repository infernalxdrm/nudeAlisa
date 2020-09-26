package parsers.netstalking.validators;

import java.util.List;

public interface validator {
    //validates links
    //and returns array/ string
    public String validate(String link) throws RuntimeException; //throws exception if link isnt valid
    public List<String>validate(List<String> links);
}
