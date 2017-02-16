package br.com.etm.checkseries.domains;

/**
 * Created by EDUARDO_MARGOTO on 30/10/2015.
 */
public class EnvironmentConfig {

    public final static String[] typesFormat = {"1x02", "S1E02", "s1e02"};
    public final static int[] timesOffset = {-12, -11, -10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22};
    static EnvironmentConfig environmentConfig = new EnvironmentConfig();

    private Integer id;
    private boolean filter_hidden = false;
    private boolean filter_favorite = false;
    private boolean filter_comingsoon = false;
    private boolean filter_notfinalized = false;
    private boolean order_name = false;
    private boolean order_nextEpisode = false;
    private boolean imageOnlyWifi = true;
    private Language language;
    private String formatNumber = EnvironmentConfig.typesFormat[0];
    private boolean updateAutomatic = true;
    private boolean hiddenEpisodesSpecials = true;
    private int timeOffset = 0;
    private boolean notification = true;
    private boolean notification_only_favorite = true;
    private boolean checkmain = true;
    private boolean layoutCompat = false;
    private long nextUpdate = -1;


    private EnvironmentConfig() {}

    public boolean isNotification_only_favorite() {
        return environmentConfig.notification_only_favorite;
    }

    public void setNotification_only_favorite(boolean notification_only_favorite) {
        environmentConfig.notification_only_favorite = notification_only_favorite;
    }

    public boolean isLayoutCompat() {
        return environmentConfig.layoutCompat;
    }

    public void setLayoutCompat(boolean layoutCompat) {
        environmentConfig.layoutCompat = layoutCompat;
    }

    public long getNextUpdate() {
        return environmentConfig.nextUpdate;
    }

    public void setNextUpdate(long nextUpdate) {
        environmentConfig.nextUpdate = nextUpdate;
    }

    public boolean isCheckmain() {
        return environmentConfig.checkmain;
    }

    public void setCheckmain(boolean checkmain) {
        environmentConfig.checkmain = checkmain;
    }

    public boolean isOrder_nextEpisode() {
        return environmentConfig.order_nextEpisode;
    }

    public void setOrder_nextEpisode(boolean order_nextEpisode) {
        environmentConfig.order_nextEpisode = order_nextEpisode;
    }

    public static EnvironmentConfig getInstance() {
        if (environmentConfig == null) return new EnvironmentConfig();
        else return environmentConfig;
    }

    public boolean isNotification() {
        return environmentConfig.notification;
    }

    public void setNotification(boolean notification) {
        environmentConfig.notification = notification;
    }

    public int getTimeOffset() {
        return environmentConfig.timeOffset;
    }

    public String getTimeOffsetFormatted() {
        if (environmentConfig.timeOffset > 0)
            return "+" + environmentConfig.timeOffset;
        else return String.valueOf(environmentConfig.timeOffset);
    }

    public void setTimeOffset(int timeOffset) {
        environmentConfig.timeOffset = timeOffset;
    }

    public boolean isHiddenEpisodesSpecials() {
        return environmentConfig.hiddenEpisodesSpecials;
    }

    public void setHiddenEpisodesSpecials(boolean hiddenEpisodesSpecials) {
        environmentConfig.hiddenEpisodesSpecials = hiddenEpisodesSpecials;
    }

    public boolean isUpdateAutomatic() {
        return environmentConfig.updateAutomatic;
    }

    public void setUpdateAutomatic(boolean updateAutomatic) {
        environmentConfig.updateAutomatic = updateAutomatic;
    }

    public String getFormatNumber() {
        return environmentConfig.formatNumber;
    }

    public void setFormatNumber(String formatNumber) {
        environmentConfig.formatNumber = formatNumber;
    }

    public Language getLanguage() {
        return environmentConfig.language;
    }

    public void setLanguage(Language language) {
        environmentConfig.language = language;
    }

    public boolean isImageOnlyWifi() {
        return environmentConfig.imageOnlyWifi;
    }

    public void setImageOnlyWifi(boolean imageOnlyWifi) {
        environmentConfig.imageOnlyWifi = imageOnlyWifi;
    }

    public boolean isOrder_name() {
        return environmentConfig.order_name;
    }

    public void setOrder_name(boolean order_name) {
        environmentConfig.order_name = order_name;
    }

    public Integer getId() {
        return environmentConfig.id;
    }

    public void setId(Integer id) {
        environmentConfig.id = id;
    }

    public boolean isFilter_hidden() {
        return environmentConfig.filter_hidden;
    }

    public void setFilter_hidden(boolean filter_hidden) {
        environmentConfig.filter_hidden = filter_hidden;
    }

    public boolean isFilter_favorite() {
        return environmentConfig.filter_favorite;
    }

    public void setFilter_favorite(boolean filter_favorite) {
        environmentConfig.filter_favorite = filter_favorite;
    }

    public boolean isFilter_comingsoon() {
        return environmentConfig.filter_comingsoon;
    }

    public void setFilter_comingsoon(boolean filter_comingsoon) {
        environmentConfig.filter_comingsoon = filter_comingsoon;
    }

    public boolean isFilter_notfinalized() {
        return environmentConfig.filter_notfinalized;
    }

    public void setFilter_notfinalized(boolean filter_notfinalized) {
        environmentConfig.filter_notfinalized = filter_notfinalized;
    }

    public void alterOrderName() {
        if (environmentConfig.order_name)
            environmentConfig.order_name = false;
        else environmentConfig.order_name = true;
    }

    public void alterFilterNotFinalized() {
        if (environmentConfig.filter_notfinalized)
            environmentConfig.filter_notfinalized = false;
        else environmentConfig.filter_notfinalized = true;
    }

    public void alterFilterFavorite() {
        if (environmentConfig.filter_favorite)
            environmentConfig.filter_favorite = false;
        else environmentConfig.filter_favorite = true;
    }

    public void alterFilterComingsoon() {
        if (environmentConfig.filter_comingsoon)
            environmentConfig.filter_comingsoon = false;
        else environmentConfig.filter_comingsoon = true;
    }

    public void alterFilterHidden() {
        if (environmentConfig.filter_hidden)
            environmentConfig.filter_hidden = false;
        else environmentConfig.filter_hidden = true;
    }

    public void removeAllFilters() {
        environmentConfig.filter_favorite = false;
        environmentConfig.filter_hidden = false;
        environmentConfig.filter_comingsoon = false;
        environmentConfig.filter_notfinalized = false;
    }
}
