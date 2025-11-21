package interface_adapter;

/**
 * Model for the View Manager. Its state is the name of the View which
 * is currently active. An initial state of "" is used.
 */
public class ViewManagerModel extends ViewModel<String> {
    private static final ViewManagerModel instance = new ViewManagerModel();

    public static ViewManagerModel getInstance() {
        return instance;
    }

    public ViewManagerModel() {
        super("view manager");
        this.setState("");
    }

}
