package com.nonstopio.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.nonstopio.shared.Person;

import java.util.List;

public class PersonListPanel extends Composite {
    private FlexTable personTable;
    private Button addButton;
    private VerticalPanel mainPanel;
    private PersonServiceAsync personService = GWT.create(PersonService.class);
    private PersonForm personForm;
    private DeckPanel deckPanel;

    public PersonListPanel() {
        deckPanel = new DeckPanel();

        // List Panel
        mainPanel = new VerticalPanel();
        mainPanel.addStyleName("person-list-panel");

        HorizontalPanel headerPanel = new HorizontalPanel();
        headerPanel.setWidth("100%");
        headerPanel.add(new Label("Person List"));

        addButton = new Button("Add New Person");
        headerPanel.add(addButton);
        headerPanel.setCellHorizontalAlignment(addButton, HasHorizontalAlignment.ALIGN_RIGHT);

        mainPanel.add(headerPanel);

        personTable = new FlexTable();
        personTable.addStyleName("person-table");
        personTable.getRowFormatter().addStyleName(0, "person-table-header");

        personTable.setText(0, 0, "ID");
        personTable.setText(0, 1, "First Name");
        personTable.setText(0, 2, "Last Name");
        personTable.setText(0, 3, "Email");
        personTable.setText(0, 4, "Actions");

        mainPanel.add(personTable);

        // Form Panel
        personForm = new PersonForm(this);

        deckPanel.add(mainPanel);
        deckPanel.add(personForm);
        deckPanel.showWidget(0);

        initWidget(deckPanel);

        // Event handlers
        addButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                personForm.clearForm();
                showPersonForm();
            }
        });

        refreshPersonList();
    }

    public void refreshPersonList() {
        personService.getAllPersons(new AsyncCallback<List<Person>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error loading persons: " + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Person> result) {
                displayPersons(result);
            }
        });
    }

    private void displayPersons(List<Person> persons) {
        // Clear existing data rows
        while (personTable.getRowCount() > 1) {
            personTable.removeRow(1);
        }

        if (persons.isEmpty()) {
            personTable.setText(1, 0, "No records found");
            personTable.getFlexCellFormatter().setColSpan(1, 0, 5);
            return;
        }

        int row = 1;
        for (final Person person : persons) {
            personTable.setText(row, 0, person.getId().toString());
            personTable.setText(row, 1, person.getFirstName());
            personTable.setText(row, 2, person.getLastName());
            personTable.setText(row, 3, person.getEmail());

            HorizontalPanel actionPanel = new HorizontalPanel();
            Button editButton = new Button("Edit");
            Button deleteButton = new Button("Delete");

            actionPanel.add(editButton);
            actionPanel.add(deleteButton);
            personTable.setWidget(row, 4, actionPanel);

            editButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    editPerson(person);
                }
            });

            deleteButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (Window.confirm("Are you sure you want to delete this person?")) {
                        deletePerson(person.getId());
                    }
                }
            });

            row++;
        }
    }

    private void editPerson(Person person) {
        personForm.setPersonForEdit(person);
        showPersonForm();
    }

    private void deletePerson(Long id) {
        personService.deletePerson(id, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error deleting person: " + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                refreshPersonList();
            }
        });
    }

    public void showPersonList() {
        deckPanel.showWidget(0);
    }

    public void showPersonForm() {
        deckPanel.showWidget(1);
    }
}