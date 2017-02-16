package br.com.etm.checkseries.domains;

/**
 * Created by EDUARDO_MARGOTO on 05/11/2015.
 */
public class ListOfUser {

    private Integer id;
    private Integer weight;
    private String name;

    public ListOfUser() {
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public ListOfUser(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
