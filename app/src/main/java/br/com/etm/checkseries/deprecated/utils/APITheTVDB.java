package br.com.etm.checkseries.deprecated.utils;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.deprecated.domains.EnvironmentConfig;
import br.com.etm.checkseries.deprecated.domains.Episode;
import br.com.etm.checkseries.deprecated.domains.Language;
import br.com.etm.checkseries.deprecated.domains.Serie;

/**
 * Created by EDUARDO_MARGOTO on 22/10/2015.
 */
public class APITheTVDB {

    private final static String APIKey = "18F2D1E6CB166204";
    private final static String PARAM_SERIEID = "<serieid>";
    private final static String PARAM_EPISODEID = "<serieid>";
    private final static String MIRRORPATH = "http://thetvdb.com";
    private final static String PATH_LANGUAGES = MIRRORPATH + "/api/" + APIKey + "/languages.xml";
    private final static String PATH_GETSERIES = MIRRORPATH + "/api/GetSeries.php?seriesname=";
    private final static String PATH_GETSERIEALL = MIRRORPATH + "/api/" + APIKey + "/series/" + PARAM_SERIEID + "/all";
    private final static String PATH_GETSERIE = MIRRORPATH + "/api/" + APIKey + "/series/" + PARAM_SERIEID;
    public final static String PATH_BANNERS = MIRRORPATH + "/banners/";
    public final static String PATH_POSTERS = PATH_BANNERS + "posters/";
    public final static String PATH_POSTERS_SERIE = "posters/";
    public final static String PATH_POSTERS_EPISODE = "episodes/";
    public final static String PATH_EPISODES_IMAGE = PATH_BANNERS + "episodes/" + PARAM_SERIEID + "/" + PARAM_EPISODEID + ".jpg";
    public final static String CURRENT_TIME_SERVER = MIRRORPATH + "/api/Updates.php?type=none";
    private static String path = "";
    private static String xml = "";
    private static String namespace = "";


    public static ArrayList<Serie> getSeries(String name_serie) throws XmlPullParserException, IOException, ParseException, UnknownHostException {
        Log.i("LOG-AMBISERIES", "getSeries(" + name_serie + ")");
        name_serie = name_serie.replaceAll("\\s", "%20");
//        xml = HttpConnection.getDataWeb(path);
        Log.i("LOG-AMBISERIES", "HttpConnection->openConnection");
        InputStream in = HttpConnection.getDataWebIS(PATH_GETSERIES + name_serie + "&language=" + EnvironmentConfig.getInstance().getLanguage().getAbbreviation());

        ArrayList<Serie> list = readSeries(in);
        if (HttpConnection.urlConnection != null)
            HttpConnection.urlConnection.disconnect();
        Log.i("LOG-AMBISERIES", "HttpConnection->closeConnection");
        return list;
    }


/*    public static ListOfUser<Episode> getEpisodes(int serieID) throws XmlPullParserException, IOException, ParseException {
        Log.i("LOG-AMBISERIES", "getEpisodes(" + serieID + ")");
        Log.i("LOG-AMBISERIES", "HttpConnection->openConnection");
        InputStream in = HttpConnection.getDataWebIS(PATH_GETSERIEALL.replace(PARAM_SERIEID, String.valueOf(serieID)));

        ListOfUser<Episode> list = readEpisodes(in);
        HttpConnection.urlConnection.disconnect();
        Log.i("LOG-AMBISERIES", "HttpConnection->closeConnection");
        return list;

    }*/

    public static List<Language> getLanguages() throws XmlPullParserException, IOException, ParseException {
        Log.i("LOG-AMBISERIES", "getLanguages()");
        Log.i("LOG-AMBISERIES", "HttpConnection->openConnection");
        InputStream in = HttpConnection.getDataWebIS(PATH_LANGUAGES);

        List<Language> list = readLanguages(in);
        if (HttpConnection.urlConnection != null)
            HttpConnection.urlConnection.disconnect();
        Log.i("LOG-AMBISERIES", "HttpConnection->closeConnection");
        return list;
    }

    public static Serie getSerie(String seriesID) throws XmlPullParserException, IOException, ParseException {
        Log.i("LOG-AMBISERIES", "getSerie(" + seriesID + ")");
        Log.i("LOG-AMBISERIES", "HttpConnection->openConnection");
        InputStream in = HttpConnection.getDataWebIS(PATH_GETSERIE.replace(PARAM_SERIEID, seriesID) + "/" + EnvironmentConfig.getInstance().getLanguage().getAbbreviation() + ".xml");

        List<Serie> list = readSeries(in);
        if (HttpConnection.urlConnection != null)
            HttpConnection.urlConnection.disconnect();
        Log.i("LOG-AMBISERIES", "HttpConnection->closeConnection");
        return list.get(0);
    }

    public static Serie getSerieAndEpisodes(String seriesID) throws XmlPullParserException, IOException, ParseException {
        Log.i("LOG-AMBISERIES", "getSerie(" + seriesID + ")");
        Log.i("LOG-AMBISERIES", "HttpConnection->openConnection");
        InputStream in = HttpConnection.getDataWebIS(PATH_GETSERIEALL.replace(PARAM_SERIEID, seriesID) + "/" + EnvironmentConfig.getInstance().getLanguage().getAbbreviation() + ".xml");
        if (in != null) {
            Serie serie = read(in);
            Log.i("LOG-AMBISERIES", "Serie -> " + serie.getName());
            Log.i("LOG-AMBISERIES", "Serie -> Episodes.size() " + serie.getEpisodeList().size());
        /*Serie serie = serieList.get(0);
        serie.setEpisodeList(episodeList);*/
            if (HttpConnection.urlConnection != null)
                HttpConnection.urlConnection.disconnect();
            Log.i("LOG-AMBISERIES", "HttpConnection->closeConnection");
            return serie;
        } else return null;

    }

    private static Serie read(InputStream in) throws XmlPullParserException, IOException, ParseException {
        Serie serie = new Serie();
        serie.setEpisodeList(new ArrayList<Episode>());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf_hour = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();
        int count = 1;
        parser.require(XmlPullParser.START_TAG, namespace, "Data");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Series")) {
                readSerie(parser, serie);
            } else if (name.equals("Episode")) {
                readEpisodes(parser, serie.getEpisodeList());
                if (serie.getEpisodeList().get(serie.getEpisodeList().size() - 1).getSeasonNumber() != 0) {
                    serie.getEpisodeList().get(serie.getEpisodeList().size() - 1).setTotalEpisodeNumber(count);
                    count++;
                }
            }
        }

        return serie;
    }

    private static void readSerie(XmlPullParser parser, Serie serie) throws XmlPullParserException, IOException, ParseException {
        Log.i("LOG-AMBISERIES", "readSeries()");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        parser.require(XmlPullParser.START_TAG, namespace, "Series");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String aux = "";
            String tagName = parser.getName();

            if (tagName.equalsIgnoreCase("id")) {
                aux = parser.nextText();
                if (!aux.equals(""))
                    serie.setId(Integer.parseInt(aux));
            } else if (tagName.equalsIgnoreCase("SeriesID")) {
                aux = parser.nextText();
                if (!aux.equals(""))
                    serie.setSerieid(Integer.parseInt(aux));
            } else if (tagName.equalsIgnoreCase("Status")) {
                serie.setStatus(parser.nextText());
            } else if (tagName.equalsIgnoreCase("Actors")) {
                serie.setActors(parser.nextText());
            } else if (tagName.equalsIgnoreCase("Airs_DayOfWeek")) {
                serie.setAirs_DayOfWeek(parser.nextText());
            } else if (tagName.equalsIgnoreCase("Airs_Time")) {
                serie.setAirs_Time(parser.nextText());
            } else if (tagName.equalsIgnoreCase("Genre")) {
                serie.setGenre(parser.nextText());
            } else if (tagName.equalsIgnoreCase("SeriesName")) {
                serie.setName(parser.nextText());
            } else if (tagName.equalsIgnoreCase("Overview")) {
                serie.setOverview(parser.nextText());
            } else if (tagName.equalsIgnoreCase("AliasName")) {
                serie.setAlias_names(parser.nextText());
            } else if (tagName.equalsIgnoreCase("FirstAired")) {
                aux = parser.nextText();
                if (!aux.equals(""))
                    serie.setFirst_aired(sdf.parse(aux));
            } else if (tagName.equalsIgnoreCase("Network")) {
                serie.setNetwork(parser.nextText());
            } else if (tagName.equalsIgnoreCase("IMDB_ID")) {
                serie.setImdb_id(parser.nextText());
            } else if (tagName.equalsIgnoreCase("banner")) {
                serie.setBanner(parser.nextText());
            } else if (tagName.equalsIgnoreCase("fanart")) {
                serie.setFanArt(parser.nextText());
            } else if (tagName.equalsIgnoreCase("language")) {
                serie.setLanguage(parser.nextText());
            } else if (tagName.equalsIgnoreCase("lastupdated")) {
                serie.setLast_updated(parser.nextText());
            } else if (tagName.equalsIgnoreCase("Runtime")) {
                serie.setRuntime(parser.nextText());
            } else if (tagName.equalsIgnoreCase("poster")) {
                serie.setPoster(parser.nextText());
            } else if (tagName.equalsIgnoreCase("zap2it_id")) {
                serie.setZap2it_id(parser.nextText());
            } else {
                skip(parser);
            }
            aux = "";
        }
    }

    private static void readEpisodes(XmlPullParser parser, List<Episode> episodeList) throws XmlPullParserException, IOException, ParseException {
        Log.i("LOG-AMBISERIES", "readEpisodes()");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf_hour = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Episode episode = new Episode();
        String aux = "";
        parser.require(XmlPullParser.START_TAG, namespace, "Episode");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tagName = parser.getName();

            if (tagName.equalsIgnoreCase("id")) {
                episode.setId(Integer.parseInt(parser.nextText()));
            } else if (tagName.equalsIgnoreCase("EpisodeNumber")) {
                episode.setEpisodeNumber(Integer.parseInt(parser.nextText()));
            } else if (tagName.equalsIgnoreCase("SeasonNumber")) {
                episode.setSeasonNumber(Integer.parseInt(parser.nextText()));
            } else if (tagName.equalsIgnoreCase("Director")) {
                episode.setDirector(parser.nextText());
            } else if (tagName.equalsIgnoreCase("EpImgFlag")) {
                episode.setEpImgFlag(parser.nextText());
            } else if (tagName.equalsIgnoreCase("EpisodeName")) {
                episode.setEpisodeName(parser.nextText());
            } else if (tagName.equalsIgnoreCase("FirstAired")) {
                aux = parser.nextText();
                if (!aux.equals(""))
                    episode.setFirstAired(sdf.parse(aux));
            } else if (tagName.equalsIgnoreCase("IMDB_ID")) {
                episode.setImdb_id(parser.nextText());
            } else if (tagName.equalsIgnoreCase("Language")) {
                episode.setLanguage(parser.nextText());
            } else if (tagName.equalsIgnoreCase("Overview")) {
                episode.setOverview(parser.nextText());
            } else if (tagName.equalsIgnoreCase("Writer")) {
                episode.setWriter(parser.nextText());
            } else if (tagName.equalsIgnoreCase("airsafter_season")) {
                episode.setAirAfterSeason(parser.nextText());
            } else if (tagName.equalsIgnoreCase("airsbefore_episode")) {
                episode.setAirBeforeEpisode(parser.nextText());
            } else if (tagName.equalsIgnoreCase("airsbefore_season")) {
                episode.setAirBeforeSeason(parser.nextText());
            } else if (tagName.equalsIgnoreCase("filename")) {
                episode.setFilename(parser.nextText());
            } else if (tagName.equalsIgnoreCase("lastupdated")) {
                episode.setLastUpdated(parser.nextText());
            } else if (tagName.equalsIgnoreCase("seriesid")) {
                episode.setSerieId(Integer.parseInt(parser.nextText()));
            } else if (tagName.equalsIgnoreCase("seasonid")) {
                episode.setSeasonId(parser.nextText());
            } else if (tagName.equalsIgnoreCase("thumb_added")) {
                aux = parser.nextText();
                if (!aux.equals(""))
                    episode.setThumbAdded(sdf.parse(aux));
            } else if (tagName.equalsIgnoreCase("thumb_height")) {
                episode.setThumbHight(parser.nextText());
            } else if (tagName.equalsIgnoreCase("thumb_width")) {
                episode.setThumbWidth(parser.nextText());
            } else {
                skip(parser);
            }
            aux = "";
        }

        episodeList.add(episode);
    }

   /* private static ArrayList<Episode> readEpisodes(InputStream in) throws XmlPullParserException, IOException, ParseException {
        Log.i("LOG-AMBISERIES", "readEpisodes()");
        Episode episode = null;
        ArrayList<Episode> episodeList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf_hour = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();

        parser.require(XmlPullParser.START_TAG, namespace, "Data");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Episode")) {
                episode = new Episode();
                parser.require(XmlPullParser.START_TAG, namespace, "Episode");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String tagName = parser.getName();

                    if (tagName.equalsIgnoreCase("id")) {
                        episode.setId(Integer.parseInt(parser.nextText()));
                    } else if (tagName.equalsIgnoreCase("EpisodeNumber")) {
                        episode.setEpisodeNumber(Integer.parseInt(parser.nextText()));
                    } else if (tagName.equalsIgnoreCase("SeasonNumber")) {
                        episode.setSeasonNumber(Integer.parseInt(parser.nextText()));
                    } else if (tagName.equalsIgnoreCase("Director")) {
                        episode.setDirector(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("EpImgFlag")) {
                        episode.setEpImgFlag(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("EpisodeName")) {
                        episode.setEpisodeName(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("FirstAired")) {
                        episode.setFirstAired(sdf.parse(parser.nextText()));
                    } else if (tagName.equalsIgnoreCase("IMDB_ID")) {
                        episode.setImdb_id(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("Language")) {
                        episode.setLanguage(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("Overview")) {
                        episode.setOverview(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("Writer")) {
                        episode.setWriter(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("airsafter_season")) {
                        episode.setAirAfterSeason(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("airsbefore_episode")) {
                        episode.setAirBeforeEpisode(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("airsbefore_season")) {
                        episode.setAirBeforeSeason(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("filename")) {
                        episode.setFilename(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("lastupdated")) {
                        episode.setLastUpdated(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("seriesid")) {
                        episode.setSerieId(Integer.parseInt(parser.nextText()));
                    } else if (tagName.equalsIgnoreCase("seasonid")) {
                        episode.setSerieId(Integer.parseInt(parser.nextText()));
                    } else if (tagName.equalsIgnoreCase("thumb_added")) {
                        episode.setThumbAdded(sdf_hour.parse(parser.nextText()));
                    } else if (tagName.equalsIgnoreCase("thumb_height")) {
                        episode.setThumbHight(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("thumb_width")) {
                        episode.setThumbWidth(parser.nextText());
                    } else {
                        skip(parser);
                    }
                }
                episodeList.add(episode);
            }
        }
        return episodeList;
    }*/

    private static ArrayList<Serie> readSeries(InputStream in) throws XmlPullParserException, IOException, ParseException {
        Log.i("LOG-AMBISERIES", "readSeries()");
        Serie serie = null;
        int id_last = -1;
        ArrayList<Serie> serieList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        Reader reader = new InputStreamReader(in);
        parser.setInput(reader);
        parser.nextTag();

        parser.require(XmlPullParser.START_TAG, namespace, "Data");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Series")) {
                serie = new Serie();
                parser.require(XmlPullParser.START_TAG, namespace, "Series");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String aux = "";
                    String tagName = parser.getName();

                    if (tagName.equalsIgnoreCase("id")) {
                        aux = parser.nextText();
                        if (!aux.equals(""))
                            serie.setId(Integer.parseInt(aux));
                    } else if (tagName.equalsIgnoreCase("SeriesID")) {
                        aux = parser.nextText();
                        if (!aux.equals(""))
                            serie.setSerieid(Integer.parseInt(aux));
                    } else if (tagName.equalsIgnoreCase("Status")) {
                        serie.setStatus(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("Actors")) {
                        serie.setActors(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("Airs_DayOfWeek")) {
                        serie.setAirs_DayOfWeek(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("Airs_Time")) {
                        serie.setAirs_Time(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("Genre")) {
                        serie.setGenre(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("SeriesName")) {
                        serie.setName(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("Overview")) {
                        serie.setOverview(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("AliasNames")) {
                        serie.setAlias_names(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("FirstAired")) {
                        aux = parser.nextText();
                        if (!aux.equals(""))
                            serie.setFirst_aired(sdf.parse(aux));
                    } else if (tagName.equalsIgnoreCase("Network")) {
                        serie.setNetwork(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("IMDB_ID")) {
                        serie.setImdb_id(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("banner")) {
                        serie.setBanner(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("fanart")) {
                        serie.setFanArt(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("language")) {
                        serie.setLanguage(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("lastupdated")) {
                        serie.setLast_updated(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("Runtime")) {
                        serie.setRuntime(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("poster")) {
                        serie.setPoster(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("zap2it_id")) {
                        serie.setZap2it_id(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("Rating")) {
                        serie.setRating(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("RatingCount")) {
                        serie.setRatingCount(Integer.parseInt(parser.nextText()));
                    } else {
                        skip(parser);
                    }
                    aux = "";
                }
                if (!serie.getId().equals(id_last)) {
                    id_last = serie.getId();
                    serieList.add(serie);
                } else {
                    if (serie.getName().equals(serieList.get(serieList.size() - 1).getName()))
                        serieList.get(serieList.size() - 1).setAlias_names(serie.getAlias_names());
                    else
                        serieList.get(serieList.size() - 1).setAlias_names(serie.getAlias_names() + "|" + serie.getName());
                }
            }
        }
        return serieList;
    }

    private static ArrayList<Language> readLanguages(InputStream in) throws XmlPullParserException, IOException, ParseException {
        Log.i("LOG-AMBISERIES", "readLanguages()");
        Language language = null;
        ArrayList<Language> languages = new ArrayList<>();

        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();

        parser.require(XmlPullParser.START_TAG, namespace, "Languages");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Language")) {
                language = new Language();
//                parser.require(XmlPullParser.START_TAG, namespace, "Language");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String tagName = parser.getName();

                    if (tagName.equalsIgnoreCase("name")) {
                        language.setLanguage(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("abbreviation")) {
                        language.setAbbreviation(parser.nextText());
                    } else if (tagName.equalsIgnoreCase("id")) {
                        language.setId(Integer.parseInt(parser.nextText()));
                    } else {
                        skip(parser);
                    }
                }
                languages.add(language);
            }
        }
        return languages;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }


}
