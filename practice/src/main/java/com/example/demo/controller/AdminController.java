package com.example.demo.controller;

import com.example.demo.entity.Admin;
import com.example.demo.entity.Contact;
import com.example.demo.service.AdminService;
import com.example.demo.repository.ContactRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final ContactRepository contactRepository;

    public AdminController(AdminService adminService, ContactRepository contactRepository) {
        this.adminService = adminService;
        this.contactRepository = contactRepository;
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute Admin admin) {
        adminService.saveAdmin(admin); // PasswordEncoderでパスワードは自動的にエンコードされる
        return "redirect:/admin/signin";
    }

    @GetMapping("/signin")
    public String showSigninForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin/signin";
    }

    @GetMapping("/contacts")
    public String showContactList(Model model) {
        Iterable<Contact> contactsIterable = contactRepository.findAll();
        List<Contact> contacts = StreamSupport.stream(contactsIterable.spliterator(), false)
                .collect(Collectors.toList());
        model.addAttribute("contacts", contacts);
        return "admin/contacts-list";
    }

    @GetMapping("/contacts/{id}")
    public String showContactDetail(@PathVariable("id") Long id, Model model) {
        Optional<Contact> contact = contactRepository.findById(id);
        if (contact.isPresent()) {
            model.addAttribute("contact", contact.get());
            return "admin/contacts-detail";
        } else {
            return "redirect:/admin/contacts";
        }
    }

    @GetMapping("/contacts/{id}/edit")
    public String showContactEditForm(@PathVariable("id") Long id, Model model) {
        Optional<Contact> contact = contactRepository.findById(id);
        if (contact.isPresent()) {
            model.addAttribute("contact", contact.get());
            return "admin/contacts-edit";
        } else {
            return "redirect:/admin/contacts";
        }
    }

    @PostMapping("/contacts/{id}/edit")
    public String updateContact(@PathVariable Long id, @ModelAttribute("contact") Contact updatedContact, RedirectAttributes attributes) {
        Contact existingContact = contactRepository.findById(id).orElse(null);
        if (existingContact == null) {
            return "redirect:/admin/contacts";
        }

        existingContact.setLastName(updatedContact.getLastName());
        existingContact.setFirstName(updatedContact.getFirstName());
        existingContact.setEmail(updatedContact.getEmail());
        existingContact.setPhone(updatedContact.getPhone());
        existingContact.setZipCode(updatedContact.getZipCode());
        existingContact.setAddress(updatedContact.getAddress());
        existingContact.setBuildingName(updatedContact.getBuildingName());
        existingContact.setContactType(updatedContact.getContactType());
        existingContact.setBody(updatedContact.getBody());
        existingContact.setUpdatedAt(LocalDateTime.now());

        contactRepository.save(existingContact);

        // Flash属性に更新したお問い合わせのIDを渡す
        attributes.addFlashAttribute("updatedContactId", existingContact.getId());

        return "redirect:/admin/contacts";
    }

    @PostMapping("/contacts/{id}/delete")
    public String deleteContact(@PathVariable("id") Long id) {
        contactRepository.deleteById(id);
        return "redirect:/admin/contacts";
    }
}

