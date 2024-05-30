package com.example.demo.service;

import com.example.demo.entity.Contact;
import com.example.demo.form.ContactForm;
import com.example.demo.repository.ContactRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public List<Contact> findAllContacts() {
        return contactRepository.findAll();
    }

    @Override
    public Contact findContactById(Long id) {
        Optional<Contact> contact = contactRepository.findById(id);
        return contact.orElse(null);
    }

    @Transactional
    @Override
    public Contact updateContact(Contact updatedContact) {
        Contact existingContact = contactRepository.findById(updatedContact.getId())
                                             .orElseThrow(() -> new EntityNotFoundException("Contact not found"));
        existingContact.setLastName(updatedContact.getLastName());
        existingContact.setFirstName(updatedContact.getFirstName());
        existingContact.setEmail(updatedContact.getEmail());
        existingContact.setPhone(updatedContact.getPhone());
        existingContact.setZipCode(updatedContact.getZipCode());
        existingContact.setAddress(updatedContact.getAddress());
        existingContact.setBuildingName(updatedContact.getBuildingName());
        existingContact.setContactType(updatedContact.getContactType());
        existingContact.setBody(updatedContact.getBody());

        // 更新日時を設定
        existingContact.setUpdatedAt(LocalDateTime.now());

        return contactRepository.save(existingContact);
    }

    @Override
    public void deleteContactById(Long id) {
        contactRepository.deleteById(id);
    }

    @Override
    public void saveContact(ContactForm contactForm) {
        Contact contact = new Contact();
        contact.setLastName(contactForm.getLastName());
        contact.setFirstName(contactForm.getFirstName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhone(contactForm.getPhone());
        contact.setZipCode(contactForm.getZipCode());
        contact.setAddress(contactForm.getAddress());
        contact.setBuildingName(contactForm.getBuildingName());
        contact.setContactType(contactForm.getContactType());
        contact.setBody(contactForm.getBody());

        // 作成日時と更新日時を設定
        LocalDateTime now = LocalDateTime.now();
        contact.setCreatedAt(now);
        contact.setUpdatedAt(now);

        contactRepository.save(contact);
    }

    @Override
    public Contact updateContact1(Contact updatedContact) {
        return updateContact(updatedContact);
    }
}

