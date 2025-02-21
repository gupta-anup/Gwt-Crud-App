package com.nonstopio.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.nonstopio.client.PersonService;
import com.nonstopio.shared.Person;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class PersonServiceImpl extends RemoteServiceServlet implements PersonService {
    private static final long serialVersionUID = 1L;

    @Override
    public Person createPerson(Person person) {
        Transaction transaction = null;
        try (Session session = DatabaseConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(person);
            transaction.commit();
            return person;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error creating person: " + e.getMessage());
        }
    }

    @Override
    public Person updatePerson(Person person) {
        Transaction transaction = null;
        try (Session session = DatabaseConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(person);
            transaction.commit();
            return person;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error updating person: " + e.getMessage());
        }
    }

    @Override
    public void deletePerson(Long id) {
        Transaction transaction = null;
        try (Session session = DatabaseConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Person person = session.get(Person.class, id);
            if (person != null) {
                session.delete(person);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error deleting person: " + e.getMessage());
        }
    }

    @Override
    public Person getPerson(Long id) {
        try (Session session = DatabaseConfig.getSessionFactory().openSession()) {
            return session.get(Person.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting person: " + e.getMessage());
        }
    }

    @Override
    public List<Person> getAllPersons() {
        try (Session session = DatabaseConfig.getSessionFactory().openSession()) {
            Query<Person> query = session.createQuery("FROM Person", Person.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting all persons: " + e.getMessage());
        }
    }
}