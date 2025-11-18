package use_case.check_favorite;

/**
 * Input Data for the Check Favorite Use Case.
 */
public class CheckFavoriteInputData {
    private final String username;

    public CheckFavoriteInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}