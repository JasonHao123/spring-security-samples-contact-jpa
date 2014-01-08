package org.springframework.security.samples.contacts.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.UnloadedSidException;

@Entity
@Table(name="ACL_OBJECT_IDENTITY")
public class AclObjectIdentity implements ObjectIdentity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name="OBJECT_ID_CLASS")
    private AclClass objIdClass;
    
    @Column(name="OBJECT_ID_IDENTITY")
    private Long objIdIdentity;
    
    @ManyToOne
    @JoinColumn(name="PARENT_OBJECT")
    private AclObjectIdentity parentObject;
    
    @ManyToOne
    @JoinColumn(name="OWNER_SID")
    private AclSid owner;
    
    @Column(name="ENTRIES_INHERITING")
    private Boolean entriesInheriting;
    
    @OneToMany(mappedBy="aclObjectIdentity")
    private List<AclEntry> aclEntries;

    public List<AclEntry> getAclEntries() {
        return aclEntries;
    }

    public void setAclEntries(List<AclEntry> aclEntries) {
        this.aclEntries = aclEntries;
    }

    public List<AccessControlEntry> getEntries() {
        if(aclEntries!=null) {
            return Arrays.asList(aclEntries.toArray(new AccessControlEntry[0]));
        }
        return null;
    }

    public void setEntries(List<AclEntry> entries) {
        this.aclEntries = entries;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AclClass getObjIdClass() {
        return objIdClass;
    }

    public void setObjIdClass(AclClass objIdClass) {
        this.objIdClass = objIdClass;
    }

    public Long getObjIdIdentity() {
        return objIdIdentity;
    }

    public void setObjIdIdentity(Long objIdIdentity) {
        this.objIdIdentity = objIdIdentity;
    }

    public AclObjectIdentity getParentObject() {
        return parentObject;
    }

    public void setParentObject(AclObjectIdentity parentObject) {
        this.parentObject = parentObject;
    }

    public AclSid getOwner() {
        return owner;
    }

    public void setOwner(AclSid owner) {
        this.owner = owner;
    }

    public Boolean getEntriesInheriting() {
        return entriesInheriting;
    }

    public void setEntriesInheriting(Boolean entriesInheriting) {
        this.entriesInheriting = entriesInheriting;
    }

    @Override
    public Serializable getIdentifier() {
        
        return this.objIdIdentity;
    }

    @Override
    public String getType() {

        return objIdClass.getClazz();
    }

}
