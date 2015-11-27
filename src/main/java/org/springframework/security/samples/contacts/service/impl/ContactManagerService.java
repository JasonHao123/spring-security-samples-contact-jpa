package org.springframework.security.samples.contacts.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.AclImpl;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.samples.contacts.dao.ContactDao;
import org.springframework.security.samples.contacts.entity.Contact;
import org.springframework.security.samples.contacts.repository.ContactRepository;
import org.springframework.security.samples.contacts.repository.RoleRepository;
import org.springframework.security.samples.contacts.repository.UserRepository;
import org.springframework.security.samples.contacts.service.ContactManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactManagerService implements ContactManager {
    
    @Autowired
    private ContactRepository contactDao;
    
    @Autowired
    private RoleRepository roleDao;
    
    @Autowired
    private UserRepository userDao;
    
    @Autowired
    private MutableAclService mutableAclService;

    @Override
    @PreAuthorize("hasPermission(#contact, admin)")
    public void addPermission(Contact contact, Sid recipient, Permission permission) {
        AclImpl acl = (AclImpl) mutableAclService.readAclById(new ObjectIdentityImpl(Contact.class,
                                                                                     contact.getId()));
        acl.insertAce(acl.getEntries().size(), permission, recipient, true);

        mutableAclService.updateAcl(acl);
        
    }

    @Override
    @PreAuthorize("hasPermission(#contact, admin)")
    public void deletePermission(Contact contact, Sid recipient, Permission permission) {
        AclImpl acl = (AclImpl) mutableAclService.readAclById(new ObjectIdentityImpl(Contact.class,
                                                                                     contact.getId()));
        int index = -1;
        for(int i=0;i<acl.getEntries().size();i++) {
            if(acl.getEntries().get(i).getSid().equals(recipient)) {
                index = i;
                break;
            }
        }
        if(index>0) {
            acl.deleteAce(index);

            mutableAclService.updateAcl(acl);
        }
        
        

    }

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    public void create(Contact contact) {
        // TODO Auto-generated method stub
        contact = contactDao.save(contact);
        
        // Create acl_object_identity rows (and also acl_class rows as needed

        final ObjectIdentity objectIdentity = new ObjectIdentityImpl(Contact.class, contact.getId());
        mutableAclService.createAcl(objectIdentity);

        AclImpl acl = (AclImpl) mutableAclService.readAclById(new ObjectIdentityImpl(Contact.class,
                                                                                     contact.getId()));
        acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, new PrincipalSid(SecurityContextHolder.getContext().getAuthentication().getName()), true);

        mutableAclService.updateAcl(acl);
 
    }

    @Override
    @PreAuthorize("hasPermission(#contact, 'delete') or hasPermission(#contact, admin)")
    public void delete(Contact contact) {
        // TODO Auto-generated method stub
        contactDao.delete(contact.getId());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostFilter("hasPermission(filterObject, read) or hasPermission(filterObject, admin)")
    public List<Contact> getAll() {
        
        return contactDao.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<String> getAllRecipients() {
        return userDao.findAllUsernames();
    }

    @Override
    @PreAuthorize("hasPermission(#id, 'org.springframework.security.samples.contacts.entity.Contact', read) or hasPermission(#id, 'org.springframework.security.samples.contacts.entity.Contact', admin)")
    public Contact getById(Long id) {
        // TODO Auto-generated method stub
        return contactDao.findOne(id);
    }

    @Override
    public Contact getRandomContact() {
        // TODO Auto-generated method stub
        return null;
    }

}
