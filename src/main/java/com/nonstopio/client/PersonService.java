package com.nonstopio.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.nonstopio.shared.Person;

import java.util.List;

@RemoteServiceRelativePath("person")
public interface PersonService extends RemoteService {
    Person createPerson(Person person);
    Person updatePerson(Person person);
    void deletePerson(Long id);
    Person getPerson(Long id);
    List<Person> getAllPersons();
}