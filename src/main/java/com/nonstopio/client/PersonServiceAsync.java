package com.nonstopio.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nonstopio.shared.Person;

import java.util.List;

public interface PersonServiceAsync {
    void createPerson(Person person, AsyncCallback<Person> callback);
    void updatePerson(Person person, AsyncCallback<Person> callback);
    void deletePerson(Long id, AsyncCallback<Void> callback);
    void getPerson(Long id, AsyncCallback<Person> callback);
    void getAllPersons(AsyncCallback<List<Person>> callback);
}