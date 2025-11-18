package interface_adapter.filter;

import Entities.Listing;
import use_case.filter.FilterListingsOutputData;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class FilterListingsViewModel {

    public static final String PROPERTY_RESULTS = "results";
    public static final String PROPERTY_ERROR = "error";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private List<Listing> results;
    private String errorMessage;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public List<Listing> getResults() {
        return results;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setResults(List<Listing> results) {
        this.results = results;
        support.firePropertyChange(PROPERTY_RESULTS, null, results);
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        support.firePropertyChange(PROPERTY_ERROR, null, errorMessage);
    }
}
