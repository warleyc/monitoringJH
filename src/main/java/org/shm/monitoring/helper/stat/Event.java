package org.shm.monitoring.helper.stat;

public class Event {

    static final String PAGEVIEW_EVENT_CATEGORY = "__##GOOGLEPAGEVIEW##__";
    static final String INSTALL_EVENT_CATEGORY = "__##GOOGLEINSTALL##__";
    private final long eventId;
    private final int userId;
    private final String accountId;
    private final int randomVal;
    private final int timestampFirst;
    private final int timestampPrevious;
    private final int timestampCurrent;
    private final int visits;
    private final String category;
    private final String action;
    private final String label;
    private final int value;
    private final int screenWidth;
    private final int screenHeight;

    public Event(long eventId, int userId, String accountId, int randomVal,
                 int timestampFirst, int timestampPrevious, int timestampCurrent,
                 int visits, String category, String action, String label,
                 int value, int screenWidth, int screenHeight) {
        this.eventId = eventId;
        this.userId = userId;
        this.accountId = accountId;
        this.randomVal = randomVal;
        this.timestampFirst = timestampFirst;
        this.timestampPrevious = timestampPrevious;
        this.timestampCurrent = timestampCurrent;
        this.visits = visits;
        this.category = category;
        this.action = action;
        this.label = label;
        this.value = value;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    public Event(int userId, String accountId, String category, String action,
                 String label, int value, int screenWidth, int screenHeight) {
        this(-1L, userId, accountId, -1, -1, -1, -1, -1, category, action,
                label, value, screenWidth, screenHeight);
    }

    public String toString() {
        return (new StringBuilder()).append("id:").append(eventId).append(" ")
                .append("random:").append(randomVal).append(" ")
                .append("timestampCurrent:").append(timestampCurrent)
                .append(" ").append("timestampPrevious:")
                .append(timestampPrevious).append(" ")
                .append("timestampFirst:").append(timestampFirst).append(" ")
                .append("visits:").append(visits).append(" ").append("value:")
                .append(value).append(" ").append("category:").append(category)
                .append(" ").append("action:").append(action).append(" ")
                .append("label:").append(label).append(" ").append("width:")
                .append(screenWidth).append(" ").append("height:")
                .append(screenHeight).toString();
    }

    public long getEventId() {
        return eventId;
    }

    public int getUserId() {
        return userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public int getRandomVal() {
        return randomVal;
    }

    public int getTimestampFirst() {
        return timestampFirst;
    }

    public int getTimestampPrevious() {
        return timestampPrevious;
    }

    public int getTimestampCurrent() {
        return timestampCurrent;
    }

    public int getVisits() {
        return visits;
    }

    public String getCategory() {
        return category;
    }

    public String getAction() {
        return action;
    }

    public String getLabel() {
        return label;
    }

    public int getValue() {
        return value;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

}
