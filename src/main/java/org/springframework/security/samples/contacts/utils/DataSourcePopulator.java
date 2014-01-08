/* Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.samples.contacts.utils;

import java.util.Random;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.acls.domain.AclImpl;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.samples.contacts.entity.Contact;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;


/**
 * Populates the Contacts in-memory database with contact and ACL information.
 *
 * @author Ben Alex
 */
public class DataSourcePopulator implements InitializingBean {
    //~ Instance fields ================================================================================================

    JdbcTemplate template;
    private MutableAclService mutableAclService;
    final Random rnd = new Random();
    TransactionTemplate tt;
    final String[] firstNames = {
            "Bob", "Mary", "James", "Jane", "Kristy", "Kirsty", "Kate", "Jeni", "Angela", "Melanie", "Kent", "William",
            "Geoff", "Jeff", "Adrian", "Amanda", "Lisa", "Elizabeth", "Prue", "Richard", "Darin", "Phillip", "Michael",
            "Belinda", "Samantha", "Brian", "Greg", "Matthew"
        };
    final String[] lastNames = {
            "Smith", "Williams", "Jackson", "Rictor", "Nelson", "Fitzgerald", "McAlpine", "Sutherland", "Abbott", "Hall",
            "Edwards", "Gates", "Black", "Brown", "Gray", "Marwell", "Booch", "Johnson", "McTaggart", "Parklin",
            "Findlay", "Robinson", "Giugni", "Lang", "Chi", "Carmichael"
        };
    private int createEntities = 50;

    //~ Methods ========================================================================================================
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(template, "dataSource required");
        // Set a user account that will initially own all the created data
        Authentication authRequest = new UsernamePasswordAuthenticationToken("rod", "koala",
                AuthorityUtils.createAuthorityList("ROLE_IGNORED"));
        SecurityContextHolder.getContext().setAuthentication(authRequest);
        
        template.execute("INSERT INTO USERS(ID,USERNAME,PASSWORD,ENABLED) VALUES(1,'rod','$2a$10$75pBjapg4Nl8Pzd.3JRnUe7PDJmk9qBGwNEJDAlA3V.dEJxcDKn5O',1)");
        template.execute("INSERT INTO USERS(ID,USERNAME,PASSWORD,ENABLED) VALUES(2,'dianne','$2a$04$bCMEyxrdF/7sgfUiUJ6Ose2vh9DAMaVBldS1Bw2fhi1jgutZrr9zm',1)");
        template.execute("INSERT INTO USERS(ID,USERNAME,PASSWORD,ENABLED) VALUES(3,'scott','$2a$06$eChwvzAu3TSexnC3ynw4LOSw1qiEbtNItNeYv5uI40w1i3paoSfLu',1)");
        template.execute("INSERT INTO USERS(ID,USERNAME,PASSWORD,ENABLED) VALUES(4,'peter','$2a$04$8.H8bCMROLF4CIgd7IpeQ.tcBXLP5w8iplO0n.kCIkISwrIgX28Ii',0)");
        template.execute("INSERT INTO USERS(ID,USERNAME,PASSWORD,ENABLED) VALUES(5,'bill','$2a$04$8.H8bCMROLF4CIgd7IpeQ.3khQlPVNWbp8kzSQqidQHGFurim7P8O',1)");
        template.execute("INSERT INTO USERS(ID,USERNAME,PASSWORD,ENABLED) VALUES(6,'bob','$2a$06$zMgxlMf01SfYNcdx7n4NpeFlAGU8apCETz/i2C7VlYWu6IcNyn4Ay',1)");
        template.execute("INSERT INTO USERS(ID,USERNAME,PASSWORD,ENABLED) VALUES(7,'jane','$2a$05$ZrdS7yMhCZ1J.AAidXZhCOxdjD8LO/dhlv4FJzkXA6xh9gdEbBT/u',1)");
        template.execute("INSERT INTO ROLE(ID,NAME) VALUES(1,'ROLE_USER')");
        template.execute("INSERT INTO ROLE(ID,NAME) VALUES(2,'ROLE_SUPERVISOR')");
        template.execute("INSERT INTO USER_ROLES(USER_ID,ROLE_ID) VALUES(1,1)");
        template.execute("INSERT INTO USER_ROLES(USER_ID,ROLE_ID) VALUES(1,2)");

//        template.execute("INSERT INTO contacts VALUES (1, 'John Smith', 'john@somewhere.com');");
//        template.execute("INSERT INTO contacts VALUES (2, 'Michael Citizen', 'michael@xyz.com');");
//        template.execute("INSERT INTO contacts VALUES (3, 'Joe Bloggs', 'joe@demo.com');");
//        template.execute("INSERT INTO contacts VALUES (4, 'Karen Sutherland', 'karen@sutherland.com');");
//        template.execute("INSERT INTO contacts VALUES (5, 'Mitchell Howard', 'mitchell@abcdef.com');");
//        template.execute("INSERT INTO contacts VALUES (6, 'Rose Costas', 'rose@xyz.com');");
//        template.execute("INSERT INTO contacts VALUES (7, 'Amanda Smith', 'amanda@abcdef.com');");
//        template.execute("INSERT INTO contacts VALUES (8, 'Cindy Smith', 'cindy@smith.com');");
//        template.execute("INSERT INTO contacts VALUES (9, 'Jonathan Citizen', 'jonathan@xyz.com');");
        SecurityContextHolder.clearContext();
    }
    public void afterPropertiesSet2() throws Exception {
        Assert.notNull(mutableAclService, "mutableAclService required");
        Assert.notNull(template, "dataSource required");
        Assert.notNull(tt, "platformTransactionManager required");

        // Set a user account that will initially own all the created data
        Authentication authRequest = new UsernamePasswordAuthenticationToken("rod", "koala",
                AuthorityUtils.createAuthorityList("ROLE_IGNORED"));
        SecurityContextHolder.getContext().setAuthentication(authRequest);

        try {
            template.execute("DROP TABLE CONTACTS");
            template.execute("DROP TABLE AUTHORITIES");
            template.execute("DROP TABLE USERS");
            template.execute("DROP TABLE ACL_ENTRY");
            template.execute("DROP TABLE ACL_OBJECT_IDENTITY");
            template.execute("DROP TABLE ACL_CLASS");
            template.execute("DROP TABLE ACL_SID");
        } catch(Exception e) {
            System.out.println("Failed to drop tables: " + e.getMessage());
        }
/**
        template.execute(
            "CREATE TABLE ACL_SID(" +
                    "ID BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 100) NOT NULL PRIMARY KEY," +
                    "PRINCIPAL BOOLEAN NOT NULL," +
                    "SID VARCHAR_IGNORECASE(100) NOT NULL," +
                    "CONSTRAINT UNIQUE_UK_1 UNIQUE(SID,PRINCIPAL));");
        template.execute(
            "CREATE TABLE ACL_CLASS(" +
                    "ID BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 100) NOT NULL PRIMARY KEY," +
                    "CLASS VARCHAR_IGNORECASE(100) NOT NULL," +
                    "CONSTRAINT UNIQUE_UK_2 UNIQUE(CLASS));");
        template.execute(
            "CREATE TABLE ACL_OBJECT_IDENTITY(" +
                    "ID BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 100) NOT NULL PRIMARY KEY," +
                    "OBJECT_ID_CLASS BIGINT NOT NULL," +
                    "OBJECT_ID_IDENTITY BIGINT NOT NULL," +
                    "PARENT_OBJECT BIGINT," +
                    "OWNER_SID BIGINT," +
                    "ENTRIES_INHERITING BOOLEAN NOT NULL," +
                    "CONSTRAINT UNIQUE_UK_3 UNIQUE(OBJECT_ID_CLASS,OBJECT_ID_IDENTITY)," +
                    "CONSTRAINT FOREIGN_FK_1 FOREIGN KEY(PARENT_OBJECT)REFERENCES ACL_OBJECT_IDENTITY(ID)," +
                    "CONSTRAINT FOREIGN_FK_2 FOREIGN KEY(OBJECT_ID_CLASS)REFERENCES ACL_CLASS(ID)," +
                    "CONSTRAINT FOREIGN_FK_3 FOREIGN KEY(OWNER_SID)REFERENCES ACL_SID(ID));");
        template.execute(
            "CREATE TABLE ACL_ENTRY(" +
                    "ID BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 100) NOT NULL PRIMARY KEY," +
                    "ACL_OBJECT_IDENTITY BIGINT NOT NULL,ACE_ORDER INT NOT NULL,SID BIGINT NOT NULL," +
                    "MASK INTEGER NOT NULL,GRANTING BOOLEAN NOT NULL,AUDIT_SUCCESS BOOLEAN NOT NULL," +
                    "AUDIT_FAILURE BOOLEAN NOT NULL,CONSTRAINT UNIQUE_UK_4 UNIQUE(ACL_OBJECT_IDENTITY,ACE_ORDER)," +
                    "CONSTRAINT FOREIGN_FK_4 FOREIGN KEY(ACL_OBJECT_IDENTITY) REFERENCES ACL_OBJECT_IDENTITY(ID)," +
                    "CONSTRAINT FOREIGN_FK_5 FOREIGN KEY(SID) REFERENCES ACL_SID(ID));");

        template.execute(
            "CREATE TABLE USERS(USERNAME VARCHAR_IGNORECASE(50) NOT NULL PRIMARY KEY,PASSWORD VARCHAR_IGNORECASE(500) NOT NULL,ENABLED BOOLEAN NOT NULL);");
        template.execute(
            "CREATE TABLE AUTHORITIES(USERNAME VARCHAR_IGNORECASE(50) NOT NULL,AUTHORITY VARCHAR_IGNORECASE(50) NOT NULL,CONSTRAINT FK_AUTHORITIES_USERS FOREIGN KEY(USERNAME) REFERENCES USERS(USERNAME));");
        template.execute("CREATE UNIQUE INDEX IX_AUTH_USERNAME ON AUTHORITIES(USERNAME,AUTHORITY);");

        template.execute(
            "CREATE TABLE CONTACTS(ID BIGINT NOT NULL PRIMARY KEY, CONTACT_NAME VARCHAR_IGNORECASE(50) NOT NULL, EMAIL VARCHAR_IGNORECASE(50) NOT NULL)");
*/
        /*
           Passwords encoded using MD5, NOT in Base64 format, with null as salt
           Encoded password for rod is "koala"
           Encoded password for dianne is "emu"
           Encoded password for scott is "wombat"
           Encoded password for peter is "opal" (but user is disabled)
           Encoded password for bill is "wombat"
           Encoded password for bob is "wombat"
           Encoded password for jane is "wombat"

         */
        template.execute("INSERT INTO USERS VALUES('rod','$2a$10$75pBjapg4Nl8Pzd.3JRnUe7PDJmk9qBGwNEJDAlA3V.dEJxcDKn5O',TRUE);");
        template.execute("INSERT INTO USERS VALUES('dianne','$2a$04$bCMEyxrdF/7sgfUiUJ6Ose2vh9DAMaVBldS1Bw2fhi1jgutZrr9zm',TRUE);");
        template.execute("INSERT INTO USERS VALUES('scott','$2a$06$eChwvzAu3TSexnC3ynw4LOSw1qiEbtNItNeYv5uI40w1i3paoSfLu',TRUE);");
        template.execute("INSERT INTO USERS VALUES('peter','$2a$04$8.H8bCMROLF4CIgd7IpeQ.tcBXLP5w8iplO0n.kCIkISwrIgX28Ii',FALSE);");
        template.execute("INSERT INTO USERS VALUES('bill','$2a$04$8.H8bCMROLF4CIgd7IpeQ.3khQlPVNWbp8kzSQqidQHGFurim7P8O',TRUE);");
        template.execute("INSERT INTO USERS VALUES('bob','$2a$06$zMgxlMf01SfYNcdx7n4NpeFlAGU8apCETz/i2C7VlYWu6IcNyn4Ay',TRUE);");
        template.execute("INSERT INTO USERS VALUES('jane','$2a$05$ZrdS7yMhCZ1J.AAidXZhCOxdjD8LO/dhlv4FJzkXA6xh9gdEbBT/u',TRUE);");
        template.execute("INSERT INTO AUTHORITIES VALUES('rod','ROLE_USER');");
        template.execute("INSERT INTO AUTHORITIES VALUES('rod','ROLE_SUPERVISOR');");
        template.execute("INSERT INTO AUTHORITIES VALUES('dianne','ROLE_USER');");
        template.execute("INSERT INTO AUTHORITIES VALUES('scott','ROLE_USER');");
        template.execute("INSERT INTO AUTHORITIES VALUES('peter','ROLE_USER');");
        template.execute("INSERT INTO AUTHORITIES VALUES('bill','ROLE_USER');");
        template.execute("INSERT INTO AUTHORITIES VALUES('bob','ROLE_USER');");
        template.execute("INSERT INTO AUTHORITIES VALUES('jane','ROLE_USER');");

        template.execute("INSERT INTO contacts VALUES (1, 'John Smith', 'john@somewhere.com');");
        template.execute("INSERT INTO contacts VALUES (2, 'Michael Citizen', 'michael@xyz.com');");
        template.execute("INSERT INTO contacts VALUES (3, 'Joe Bloggs', 'joe@demo.com');");
        template.execute("INSERT INTO contacts VALUES (4, 'Karen Sutherland', 'karen@sutherland.com');");
        template.execute("INSERT INTO contacts VALUES (5, 'Mitchell Howard', 'mitchell@abcdef.com');");
        template.execute("INSERT INTO contacts VALUES (6, 'Rose Costas', 'rose@xyz.com');");
        template.execute("INSERT INTO contacts VALUES (7, 'Amanda Smith', 'amanda@abcdef.com');");
        template.execute("INSERT INTO contacts VALUES (8, 'Cindy Smith', 'cindy@smith.com');");
        template.execute("INSERT INTO contacts VALUES (9, 'Jonathan Citizen', 'jonathan@xyz.com');");

        for (int i = 10; i < createEntities; i++) {
            String[] person = selectPerson();
            template.execute("INSERT INTO contacts VALUES (" + i + ", '" + person[2] + "', '" + person[0].toLowerCase()
                + "@" + person[1].toLowerCase() + ".com');");
        }

        // Create acl_object_identity rows (and also acl_class rows as needed
        for (int i = 1; i < createEntities; i++) {
            final ObjectIdentity objectIdentity = new ObjectIdentityImpl(Contact.class, new Long(i));
            tt.execute(new TransactionCallback<Object>() {
                    public Object doInTransaction(TransactionStatus arg0) {
                        mutableAclService.createAcl(objectIdentity);

                        return null;
                    }
                });
        }

        // Now grant some permissions
        grantPermissions(1, "rod", BasePermission.ADMINISTRATION);
        grantPermissions(2, "rod", BasePermission.READ);
        grantPermissions(3, "rod", BasePermission.READ);
        grantPermissions(3, "rod", BasePermission.WRITE);
        grantPermissions(3, "rod", BasePermission.DELETE);
        grantPermissions(4, "rod", BasePermission.ADMINISTRATION);
        grantPermissions(4, "dianne", BasePermission.ADMINISTRATION);
        grantPermissions(4, "scott", BasePermission.READ);
        grantPermissions(5, "dianne", BasePermission.ADMINISTRATION);
        grantPermissions(5, "dianne", BasePermission.READ);
        grantPermissions(6, "dianne", BasePermission.READ);
        grantPermissions(6, "dianne", BasePermission.WRITE);
        grantPermissions(6, "dianne", BasePermission.DELETE);
        grantPermissions(6, "scott", BasePermission.READ);
        grantPermissions(7, "scott", BasePermission.ADMINISTRATION);
        grantPermissions(8, "dianne", BasePermission.ADMINISTRATION);
        grantPermissions(8, "dianne", BasePermission.READ);
        grantPermissions(8, "scott", BasePermission.READ);
        grantPermissions(9, "scott", BasePermission.ADMINISTRATION);
        grantPermissions(9, "scott", BasePermission.READ);
        grantPermissions(9, "scott", BasePermission.WRITE);
        grantPermissions(9, "scott", BasePermission.DELETE);

        // Now expressly change the owner of the first ten contacts
        // We have to do this last, because "rod" owns all of them (doing it sooner would prevent ACL updates)
        // Note that ownership has no impact on permissions - they're separate (ownership only allows ACl editing)
        changeOwner(5, "dianne");
        changeOwner(6, "dianne");
        changeOwner(7, "scott");
        changeOwner(8, "dianne");
        changeOwner(9, "scott");

        String[] users = {"bill", "bob", "jane"}; // don't want to mess around with consistent sample data
        Permission[] permissions = {BasePermission.ADMINISTRATION, BasePermission.READ, BasePermission.DELETE};

        for (int i = 10; i < createEntities; i++) {
            String user = users[rnd.nextInt(users.length)];
            Permission permission = permissions[rnd.nextInt(permissions.length)];
            grantPermissions(i, user, permission);

            String user2 = users[rnd.nextInt(users.length)];
            Permission permission2 = permissions[rnd.nextInt(permissions.length)];
            grantPermissions(i, user2, permission2);
        }

        SecurityContextHolder.clearContext();
    }

    private void changeOwner(int contactNumber, String newOwnerUsername) {
        AclImpl acl = (AclImpl) mutableAclService.readAclById(new ObjectIdentityImpl(Contact.class,
                    new Long(contactNumber)));
        acl.setOwner(new PrincipalSid(newOwnerUsername));
        updateAclInTransaction(acl);
    }

    public int getCreateEntities() {
        return createEntities;
    }

    private void grantPermissions(int contactNumber, String recipientUsername, Permission permission) {
        AclImpl acl = (AclImpl) mutableAclService.readAclById(new ObjectIdentityImpl(Contact.class,
                    new Long(contactNumber)));
        acl.insertAce(acl.getEntries().size(), permission, new PrincipalSid(recipientUsername), true);
        updateAclInTransaction(acl);
    }

    private String[] selectPerson() {
        String firstName = firstNames[rnd.nextInt(firstNames.length)];
        String lastName = lastNames[rnd.nextInt(lastNames.length)];

        return new String[] {firstName, lastName, firstName + " " + lastName};
    }

    public void setCreateEntities(int createEntities) {
        this.createEntities = createEntities;
    }

    public void setDataSource(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public void setMutableAclService(MutableAclService mutableAclService) {
        this.mutableAclService = mutableAclService;
    }

    public void setPlatformTransactionManager(PlatformTransactionManager platformTransactionManager) {
        this.tt = new TransactionTemplate(platformTransactionManager);
    }

    private void updateAclInTransaction(final MutableAcl acl) {
        tt.execute(new TransactionCallback<Object>() {
                public Object doInTransaction(TransactionStatus arg0) {
                    mutableAclService.updateAcl(acl);

                    return null;
                }
            });
    }
}
