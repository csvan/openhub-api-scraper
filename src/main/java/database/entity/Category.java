package database.entity;

import javax.persistence.Entity;

/**
 * Created by csvanefalk on 18/11/14.
 */
@Entity
public class Category extends AbstractEntity {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
