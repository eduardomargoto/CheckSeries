package br.com.etm.checkseries.deprecated.utils;

import java.util.Comparator;

import br.com.etm.checkseries.deprecated.domains.Serie;

/**
 * Created by EDUARDO_MARGOTO on 28/12/2015.
 */
public class SerieComparator_NextEpisode implements Comparator<Serie> {
    @Override
    public int compare(Serie s1, Serie s2) {
        if((s1.isFavorite() && s2.isFavorite()) || (!s1.isFavorite() && !s2.isFavorite())) {
            if (s1.getNextEpisode() != null && s2.getNextEpisode() != null) {
                if (s1.getNextEpisode().getFirstAired() != null && s2.getNextEpisode().getFirstAired() != null) {
                    if (s1.getNextEpisode().getFirstAired().before(s2.getNextEpisode().getFirstAired())) {
                        return -1;
                    } else return 1;
                } else if (s1.getNextEpisode().getFirstAired() != null && s2.getNextEpisode().getFirstAired() == null) {
                    return -1;
                } else if (s1.getNextEpisode().getFirstAired() == null && s2.getNextEpisode().getFirstAired() != null) {
                    return 1;
                } else return 0;
            } else if (s1.getNextEpisode() == null && s2.getNextEpisode() == null) {
                return 0;
            } else if (s1.getNextEpisode() != null && s2.getNextEpisode() == null) {
                return -1;
            } else if (s1.getNextEpisode() == null && s2.getNextEpisode() != null) {
                return 1;
            } else return 0;
        } else {
            if(s1.isFavorite())
                return -1;
            else return 1;
        }
    }
}
