package org.springframework.security.samples.contacts.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.samples.contacts.entity.Contact;
import org.springframework.security.samples.contacts.service.ContactManager;
import org.springframework.security.samples.contacts.web.model.WebContact;
import org.springframework.security.samples.contacts.web.validator.WebContactValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Luke Taylor
 * @since 3.0
 */
@Controller
public class AddDeleteContactController {
    @Autowired
    private ContactManager contactManager;
    private final Validator validator = new WebContactValidator();

    /**
     * Displays the "add contact" form.
     */
    @RequestMapping(value="/secure/add.htm", method=RequestMethod.GET)
    public ModelAndView addContactDisplay() {
        return new ModelAndView("add", "webContact", new WebContact());
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        System.out.println("A binder for object: " + binder.getObjectName());
    }

    /**
     * Handles the submission of the contact form, creating a new instance if
     * the username and email are valid.
     */
    @RequestMapping(value="/secure/add.htm", method=RequestMethod.POST)
    public String addContact(WebContact form, BindingResult result) {
        validator.validate(form, result);

        if (result.hasErrors()) {
            return "add";
        }

        Contact contact = new Contact();
        contact.setName(form.getName());
        contact.setEmail(form.getEmail());
        contactManager.create(contact);

        return "redirect:/secure/index.htm";
    }

    @RequestMapping(value="/secure/del.htm", method=RequestMethod.GET)
    public ModelAndView handleRequest(@RequestParam("contactId") int contactId) {
        Contact contact = contactManager.getById(Long.valueOf(contactId));
        contactManager.delete(contact);

        return new ModelAndView("deleted", "contact", contact);
    }
}
