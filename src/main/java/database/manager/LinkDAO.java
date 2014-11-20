package database.manager;

import database.entity.Link;

/**
 * Created by csvanefalk on 18/11/14.
 */
public class LinkDAO extends AbstractDAO<Link> {

    public LinkDAO() {
        super(Link.class);
    }
}
