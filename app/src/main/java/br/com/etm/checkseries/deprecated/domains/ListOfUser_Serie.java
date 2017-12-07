package br.com.etm.checkseries.deprecated.domains;

/**
 * Created by EDUARDO_MARGOTO on 05/11/2015.
 */
public class ListOfUser_Serie {
    private Integer id;
    private Serie serie;
    private ListOfUser listOfUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public ListOfUser getListOfUser() {
        return listOfUser;
    }

    public void setListOfUser(ListOfUser listOfUser) {
        this.listOfUser = listOfUser;
    }
}
