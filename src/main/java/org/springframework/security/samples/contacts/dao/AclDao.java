package org.springframework.security.samples.contacts.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.samples.contacts.entity.AclClass;
import org.springframework.security.samples.contacts.entity.AclEntry;
import org.springframework.security.samples.contacts.entity.AclObjectIdentity;
import org.springframework.security.samples.contacts.entity.AclSid;

public interface AclDao {

    List<ObjectIdentity> findChildren(Serializable identifier, String type);

    AclObjectIdentity getObjectIdentity(String type, Serializable identifier);

    void createObjectIdentity(AclObjectIdentity identity);

    List<AclSid> findAclSidList(Boolean valueOf, String sidName);

    AclSid createAclSid(AclSid sid2);

    List<AclClass> findAclClassList(String type);

    AclClass createAclClass(AclClass clazz);

    void deleteEntries(AclObjectIdentity oidPrimaryKey);

    void deleteObjectIdentity(AclObjectIdentity oidPrimaryKey);

    void createEntries(List<AclEntry> entries);

    boolean updateObjectIdentity(AclObjectIdentity aclObject);

    AclSid findAclSid(String principal);

}
