package database.entity;

import javax.persistence.Entity;

/**
 * Created by csvanefalk on 18/11/14.
 */
@Entity
public class License extends AbstractEntity {

    private String name;

    private String niceName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNiceName() {
        return niceName;
    }

    public void setNiceName(String niceName) {
        this.niceName = niceName;
    }
}
