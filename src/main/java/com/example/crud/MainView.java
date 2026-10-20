package com.example.crud;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {
    private final ContactService contactService;
    private final Grid<Contact> grid = new Grid<>(Contact.class, false);
    private final TextField searchField = new TextField();
    private final Dialog editorDialog = new Dialog();

    public MainView(ContactService contactService) {
        this.contactService = contactService;

        setSizeFull();
        setMaxWidth("1100px");
        getStyle().set("margin", "0 auto");

        H2 title = new H2("Contact Manager");
        Paragraph subtitle = new Paragraph("A simple Java CRUD application: create, view, edit, and delete contacts.");

        searchField.setPlaceholder("Search by name, email, or phone...");
        searchField.setClearButtonVisible(true);
        searchField.setWidthFull();
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(event -> refreshGrid());

        Button addButton = new Button("Add contact", event -> openEditor(new Contact()));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout toolbar = new HorizontalLayout(searchField, addButton);
        toolbar.setWidthFull();
        toolbar.setAlignItems(Alignment.BASELINE);
        toolbar.expand(searchField);

        grid.addColumn(Contact::getName).setHeader("Name").setAutoWidth(true).setSortable(true);
        grid.addColumn(Contact::getEmail).setHeader("Email").setAutoWidth(true);
        grid.addColumn(Contact::getPhone).setHeader("Phone").setAutoWidth(true);
        grid.addComponentColumn(contact -> {
            Button editButton = new Button("Edit", event -> openEditor(contact));
            Button deleteButton = new Button("Delete", event -> {
                contactService.delete(contact.getId());
                refreshGrid();
                Notification.show("Contact deleted");
            });
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            return new HorizontalLayout(editButton, deleteButton);
        }).setHeader("Actions").setAutoWidth(true);

        grid.setSizeFull();
        editorDialog.setHeaderTitle("Contact details");

        add(title, subtitle, toolbar, grid);
        expand(grid);
        refreshGrid();
    }

    private void openEditor(Contact selected) {
        Contact contact = new Contact(
                selected.getId(), selected.getName(), selected.getEmail(), selected.getPhone());

        TextField nameField = new TextField("Name");
        nameField.setRequired(true);
        nameField.setMaxLength(100);
        nameField.setWidthFull();
        nameField.setValue(contact.getName() == null ? "" : contact.getName());

        EmailField emailField = new EmailField("Email");
        emailField.setRequired(true);
        emailField.setErrorMessage("Enter a valid email address");
        emailField.setWidthFull();
        emailField.setValue(contact.getEmail() == null ? "" : contact.getEmail());

        TextField phoneField = new TextField("Phone");
        phoneField.setMaxLength(30);
        phoneField.setWidthFull();
        phoneField.setValue(contact.getPhone() == null ? "" : contact.getPhone());

        FormLayout form = new FormLayout(nameField, emailField, phoneField);
        form.setWidth("min(440px, 85vw)");

        Button saveButton = new Button("Save", event -> {
            if (nameField.isEmpty() || emailField.isEmpty()
                    || !emailField.getValue().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                nameField.setInvalid(nameField.isEmpty());
                emailField.setInvalid(emailField.isEmpty()
                        || !emailField.getValue().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"));
                Notification.show("Please enter a name and a valid email");
                return;
            }

            contact.setName(nameField.getValue().trim());
            contact.setEmail(emailField.getValue().trim());
            contact.setPhone(phoneField.getValue().trim());
            contactService.save(contact);
            editorDialog.close();
            refreshGrid();
            Notification.show("Contact saved");
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel", event -> editorDialog.close());
        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);

        editorDialog.removeAll();
        editorDialog.add(form, buttons);
        editorDialog.open();
    }

    private void refreshGrid() {
        grid.setItems(contactService.findAll(searchField.getValue()));
    }
}
