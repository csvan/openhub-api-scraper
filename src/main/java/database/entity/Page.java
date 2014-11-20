package database.entity;

import javax.persistence.Entity;

/**
 * Created by christopher on 18/11/14.
 */
@Entity
public class Page extends AbstractEntity {

    int pageNumber;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
