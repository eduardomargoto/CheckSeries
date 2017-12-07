package br.com.etm.checkseries.utils;

import java.util.Comparator;

import br.com.etm.checkseries.deprecated.domains.Serie;

/**
 * Created by EDUARDO_MARGOTO on 31/10/2015.
 */
public class SerieComparator_Name implements Comparator<Serie> {
    @Override
    public int compare(Serie serie1, Serie serie2) {
        return serie1.getName().compareTo(serie2.getName());
    }
}

