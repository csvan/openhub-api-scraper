package database.manager;

import database.entity.Tag;

/**
 * Created by csvanefalk on 18/11/14.
 */
public class TagDAO extends AbstractDAO<Tag> {

    public TagDAO() {
        super(Tag.class);
    }
}
