package answerable;


import javax.annotation.Nullable;

public interface answerable {
    String respond(String message,@Nullable Object o);

    void setReply(String message);
    void restart();

    answerable getInstance();
}
