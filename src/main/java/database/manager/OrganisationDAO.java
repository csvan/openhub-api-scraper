package database.manager;

import database.entity.Organisation;

/**
 * Created by csvanefalk on 18/11/14.
 */
public class OrganisationDAO extends AbstractDAO<Organisation> {
    public OrganisationDAO() {
        super(Organisation.class);
    }
}
