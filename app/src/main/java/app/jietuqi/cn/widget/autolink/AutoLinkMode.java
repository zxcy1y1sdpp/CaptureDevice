package app.jietuqi.cn.widget.autolink;

/**
 * Created by Chatikyan on 25.09.2016-22:02.
 */

public enum AutoLinkMode {

    MODE_HASHTAG("Hashtag"),
    MODE_MENTION("Mention"),
    MODE_URL("Url"),
    MODE_PHONE("Phone"),
    MODE_Number_7("Number_7"),
    MODE_EMAIL("Email"),
    MODE_CUSTOM("Custom");

    private String name;

    AutoLinkMode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
