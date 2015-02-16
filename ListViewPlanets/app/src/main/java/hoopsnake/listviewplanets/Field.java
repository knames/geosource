package hoopsnake.listviewplanets;

/**
 * Created by Anam on 2015-02-09.
 */

public class Field {

    private String title;
    private String description;
    private int idImg;



    public Field(String title) {
        this.title = title;
    }

    public Field(String title, String descr) {
        this.title = title;
        this.description = descr;
    }

    public Field(String title, Integer distance, String descr, int idImg) {
        this.title = title;
        this.description = descr;
        this.idImg = idImg;
    }


    public String getTitle() {
        return title;
    }
    public void setTitle(String name) {
        this.title = name;
    }
    public int getIdImg() {
        return idImg;
    }
    public void setIdImg(int idImg) {
        this.idImg = idImg;
    }


    public String getDescription() {
        return description;
    }
}