package com.example.crud;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactServiceTest {

    @Test
    void canCreateSearchUpdateAndDeleteContact() {
        ContactService service = new ContactService();
        Contact contact = new Contact(null, "Jamie Lee", "jamie@example.com", "555-0199");

        Contact created = service.save(contact);
        assertNotNull(created.getId());
        assertTrue(service.findAll("Jamie").stream()
                .anyMatch(item -> item.getEmail().equals("jamie@example.com")));

        created.setName("Jamie Updated");
        service.save(created);
        assertTrue(service.findAll("Updated").stream()
                .anyMatch(item -> item.getId().equals(created.getId())));

        service.delete(created.getId());
        assertFalse(service.findAll("Jamie Updated").stream()
                .anyMatch(item -> item.getId().equals(created.getId())));
    }
}
