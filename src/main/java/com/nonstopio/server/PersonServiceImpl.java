package com.nonstopio.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.nonstopio.client.PersonService;
import com.nonstopio.shared.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonServiceImpl extends RemoteServiceServlet implements PersonService {
    private static final long serialVersionUID = 1L;

    // In-memory storage for this example
    private static final Map<Long, Person> persons = new HashMap<>();
    private static Long nextId = 1L;

    @Override
    public Person createPerson(Person person) {
        // Assign a new ID
        person.setId(nextId++);
        persons.put(person.getId(), person);
        return person;
    }

    @Override
    public Person updatePerson(Person person) {
        if (person.getId() == null || !persons.containsKey(person.getId())) {
            throw new IllegalArgumentException("Person not found with id: " + person.getId());
        }
        persons.put(person.getId(), person);
        return person;
    }

    @Override
    public void deletePerson(Long id) {
        if (!persons.containsKey(id)) {
            throw new IllegalArgumentException("Person not found with id: " + id);
        }
        persons.remove(id);
    }

    @Override
    public Person getPerson(Long id) {
        Person person = persons.get(id);
        if (person == null) {
            throw new IllegalArgumentException("Person not found with id: " + id);
        }
        return person;
    }

    @Override
    public List<Person> getAllPersons() {
        return new ArrayList<>(persons.values());
    }
}