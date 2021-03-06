package com.vaadin.nayan.ems.ui.views.list;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import com.vaadin.nayan.ems.backend.entity.Company;
import com.vaadin.nayan.ems.backend.entity.Contact;

import java.util.List;

public class ContactForm extends FormLayout {

    TextField firstName = new TextField("Company Code");
    TextField lastName = new TextField("Company Id");
    EmailField email = new EmailField("Email");
    ComboBox<Contact.Status> status = new ComboBox<>("Status");
    ComboBox<Company> company = new ComboBox<>("Company");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Contact> binder = new BeanValidationBinder<>(Contact.class);
    private Contact contact;
    
    //notification
    Notification notification = new Notification();
    

    public ContactForm(List<Company> companies) {
        addClassName("contact-form");

        binder.bindInstanceFields(this);
        status.setItems(Contact.Status.values());
        company.setItems(companies);
        company.setItemLabelGenerator(Company::getName);

        add(
        	firstName,
            lastName,
            email,
            status,
            company,
            createButtonsLayout()
        ); //email,
    }

    public void setContact(Contact contact) {
        this.contact = contact;
        binder.readBean(contact);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, contact)));
        close.addClickListener(click -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {

      try {
        binder.writeBean(contact);
        fireEvent(new SaveEvent(this, contact));
      } catch (ValidationException e) {
        e.printStackTrace();
      }
      Notification.show(
              "Company and Details Saved Sucessfully");
    }

    // Events
    public static abstract class ContactFormEvent extends ComponentEvent<ContactForm> {
      private Contact contact;

      protected ContactFormEvent(ContactForm source, Contact contact) {
        super(source, false);
        this.contact = contact;
      }

      public Contact getContact() {
        return contact;
      }
    }

    public static class SaveEvent extends ContactFormEvent {
      SaveEvent(ContactForm source, Contact contact) {
        super(source, contact);
      }
    }

    public static class DeleteEvent extends ContactFormEvent {
      DeleteEvent(ContactForm source, Contact contact) {
        super(source, contact);
        Notification.show(
                "Company and Details Deleated Sucessfully");
      }

    }

    public static class CloseEvent extends ContactFormEvent {
      CloseEvent(ContactForm source) {
        super(source, null);
      }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
      return getEventBus().addListener(eventType, listener);
    }
}
