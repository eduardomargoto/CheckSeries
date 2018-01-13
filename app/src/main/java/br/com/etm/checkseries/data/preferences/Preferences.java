package br.com.etm.checkseries.data.preferences;

/**
 * Created by eduardo on 13/01/18.
 */

public interface Preferences {

    void setFilterUnfinished(boolean notFinished);
    boolean isFilterUnfinished();

    void setFilterFavourite(boolean favourite);
    boolean isFilterFavourite();

    void setFilterHidden(boolean hidden);
    boolean isFilterHidden();

    void setFilterNextEpisodes(boolean nextEpisodes);
    boolean isFilterNextEpisodes();

    void setOrderName(boolean orderName);
    boolean isOrderName();

    void setOrderNextEpisode(boolean orderNextEpisode);
    boolean isOrderNextEpisode();

    void clearFilter();
}
