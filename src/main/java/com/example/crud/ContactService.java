package com.example.crud;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ContactService {
    private final List<Contact> contacts = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public ContactService() {
        save(new Contact(null, "Alex Johnson", "alex@example.com", "555-0101"));
        save(new Contact(null, "Sam Taylor", "sam@example.com", "555-0102"));
    }

    public synchronized List<Contact> findAll(String searchTerm) {
        String query = searchTerm == null ? "" : searchTerm.trim().toLowerCase(Locale.ROOT);

        return contacts.stream()
                .filter(contact -> query.isEmpty()
                        || contact.getName().toLowerCase(Locale.ROOT).contains(query)
                        || contact.getEmail().toLowerCase(Locale.ROOT).contains(query)
                        || contact.getPhone().toLowerCase(Locale.ROOT).contains(query))
                .sorted(Comparator.comparing(Contact::getName, String.CASE_INSENSITIVE_ORDER))
                .map(contact -> new Contact(
                        contact.getId(), contact.getName(), contact.getEmail(), contact.getPhone()))
                .collect(Collectors.toList());
    }

    public synchronized Contact save(Contact contact) {
        if (contact.getId() == null) {
            contact.setId(nextId.getAndIncrement());
            contacts.add(copy(contact));
            return copy(contact);
        }

        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getId().equals(contact.getId())) {
                contacts.set(i, copy(contact));
                return copy(contact);
            }
        }

        contacts.add(copy(contact));
        return copy(contact);
    }

    public synchronized void delete(Long id) {
        contacts.removeIf(contact -> contact.getId().equals(id));
    }

    private Contact copy(Contact contact) {
        return new Contact(contact.getId(), contact.getName(), contact.getEmail(), contact.getPhone());
    }
}
