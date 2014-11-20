package database.manager;

import database.entity.License;

/**
 * Created by csvanefalk on 18/11/14.
 */
public class LicenseDAO extends AbstractDAO<License> {
    public LicenseDAO() {
        super(License.class);
    }
}
