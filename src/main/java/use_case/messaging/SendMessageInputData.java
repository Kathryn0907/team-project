package use_case.messaging;

public class SendMessageInputData {
    private final String fromUsername;
    private final String toUsername;
    private final String listingId;
    private final String content;

    public SendMessageInputData(String fromUsername, String toUsername,
                                String listingId, String content) {
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.listingId = listingId;
        this.content = content;
    }

    public String getFromUsername() { return fromUsername; }
    public String getToUsername() { return toUsername; }
    public String getListingId() { return listingId; }
    public String getContent() { return content; }
}