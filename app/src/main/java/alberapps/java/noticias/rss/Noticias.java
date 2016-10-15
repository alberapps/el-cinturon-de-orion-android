package alberapps.java.noticias.rss;

import java.util.List;

/**
 * Created by albert on 26/02/16.
 */
public class Noticias {

    private String title;

    private String description;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private List<NoticiaRss> noticiasList;

    public List<NoticiaRss> getNoticiasList() {
        return noticiasList;
    }

    public void setNoticiasList(List<NoticiaRss> noticiasList) {
        this.noticiasList = noticiasList;
    }
}
