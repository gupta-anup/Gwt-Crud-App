package com.nonstopio.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.nonstopio.shared.Person;

public class PersonForm extends Composite {
    private TextBox firstName;
    private TextBox lastName;
    private TextBox email;
    private Button saveButton;
    private Button cancelButton;

    private Person currentPerson;
    private PersonServiceAsync personService = GWT.create(PersonService.class);
    private PersonListPanel parentPanel;

    public PersonForm(PersonListPanel parent) {
        this.parentPanel = parent;

        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.addStyleName("person-form");

        Grid form = new Grid(3, 2);
        form.setWidget(0, 0, new Label("First Name:"));
        firstName = new TextBox();
        form.setWidget(0, 1, firstName);

        form.setWidget(1, 0, new Label("Last Name:"));
        lastName = new TextBox();
        form.setWidget(1, 1, lastName);

        form.setWidget(2, 0, new Label("Email:"));
        email = new TextBox();
        form.setWidget(2, 1, email);

        mainPanel.add(form);

        HorizontalPanel buttonPanel = new HorizontalPanel();
        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel);

        initWidget(mainPanel);

        saveButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                savePerson();
            }
        });

        cancelButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                clearForm();
                parentPanel.showPersonList();
            }
        });
    }

    public void setPersonForEdit(Person person) {
        this.currentPerson = person;
        if (person != null) {
            firstName.setText(person.getFirstName());
            lastName.setText(person.getLastName());
            email.setText(person.getEmail());
        }
    }

    private void savePerson() {
        if (!validateForm()) {
            Window.alert("Please fill all fields");
            return;
        }

        if (currentPerson == null) {
            currentPerson = new Person();
        }

        currentPerson.setFirstName(firstName.getText());
        currentPerson.setLastName(lastName.getText());
        currentPerson.setEmail(email.getText());

        AsyncCallback<Person> callback = new AsyncCallback<Person>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error saving person: " + caught.getMessage());
            }

            @Override
            public void onSuccess(Person result) {
                clearForm();
                parentPanel.refreshPersonList();
                parentPanel.showPersonList();
            }
        };

        if (currentPerson.getId() == null) {
            personService.createPerson(currentPerson, callback);
        } else {
            personService.updatePerson(currentPerson, callback);
        }
    }

    private boolean validateForm() {
        return !firstName.getText().trim().isEmpty() &&
                !lastName.getText().trim().isEmpty() &&
                !email.getText().trim().isEmpty();
    }

    public void clearForm() {
        firstName.setText("");
        lastName.setText("");
        email.setText("");
        currentPerson = null;
    }
}