package br.com.etm.checkseries.deprecated.utils;

import java.util.Comparator;

import br.com.etm.checkseries.deprecated.domains.Serie;

/**
 * Created by EDUARDO_MARGOTO on 10/19/2016.
 */

public class SerieComparator_Normal implements Comparator<Serie> {

    @Override
    public int compare(Serie o1, Serie o2) {
        //return 1 acima | 0 igual | -1 abaixo
//        if (this.isEnded() && o.isEnded())
//            return 0;
//        else


        if (o1.isFavorite() && o2.isFavorite()) {
            if (o1.getNextEpisode() == null)
                return 1;
            if (o1.getNextEpisode().getFirstAired().before(o2.getNextEpisode().getFirstAired()))
                return -1;
        } else if (o1.isFavorite())
            return -1;

        if (o1.getNextEpisode() == null) // usuário assistiu todos os episódios.
            return 1;

        if (o2.getNextEpisode() == null)
            return -1;

        if(o1.getNextEpisode().getFirstAired() != null && o2.getNextEpisode().getFirstAired() != null) {
            if (o1.getNextEpisode().getFirstAired().before(o2.getNextEpisode().getFirstAired()))
                return -1;
        } else if(o1.getNextEpisode().getFirstAired() == null && o2.getNextEpisode().getFirstAired() != null) {
            return 1;
        } else if(o1.getNextEpisode().getFirstAired() != null && o2.getNextEpisode().getFirstAired() == null) {
            return -1;
        }

        return 1;
    }
}
