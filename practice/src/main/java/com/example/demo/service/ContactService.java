package com.example.demo.service;

import com.example.demo.entity.Contact;
import com.example.demo.form.ContactForm;

import java.util.List;

public interface ContactService {

	List<Contact> findAllContacts();

	Contact findContactById(Long id);

	Contact updateContact(Contact updatedContact);

	void deleteContactById(Long id);

	void saveContact(ContactForm contactForm);

	Contact updateContact1(Contact updatedContact);
}
