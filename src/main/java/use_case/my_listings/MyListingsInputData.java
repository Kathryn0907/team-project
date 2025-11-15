package use_case.my_listings;

public class MyListingsInputData {
    private final String username;

    public MyListingsInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}