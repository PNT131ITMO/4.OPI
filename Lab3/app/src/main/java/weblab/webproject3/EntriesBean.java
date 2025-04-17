package weblab.webproject3;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.inject.Named;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A managed bean responsible for handling and storing {@link Entry} entities.
 * This bean is application-scoped and used in JSF views to access entry data and perform database operations.
 * <p>
 * It connects to the database using JPA with the persistence unit {@code StudsPU},
 * providing functionality to add new entries, clear all entries, and retrieve entry lists.
 * </p>
 *
 * @author [Choi Soo Jong]
 */
@Named("entries")
@ApplicationScoped
public class EntriesBean implements Serializable {

    /** The name of the persistence unit used for JPA configuration. */
    private static final String persistenceUnit = "StudsPU";

    /** The current {@link Entry} object being processed. */
    private Entry entry;

    /** The list of all stored {@link Entry} objects. */
    private List<Entry> entries;

    /** The factory for creating {@link EntityManager} instances. */
    private EntityManagerFactory entityManagerFactory;

    /** The JPA entity manager for database operations, tied to the {@code StudsPU} persistence unit. */
    @PersistenceContext(unitName = "StudsPU")
    private EntityManager entityManager;

    /** The transaction object for managing database operations. */
    private EntityTransaction transaction;

    /**
     * Constructs a new {@code EntriesBean} instance, initializing the JPA connection and entry list.
     */
    public EntriesBean() {
        entry = new Entry();
        entries = new ArrayList<>();
        connect();
        loadEntries();
    }

    /**
     * Initializes the JPA {@link EntityManagerFactory}, {@link EntityManager}, and {@link EntityTransaction}.
     */
    private void connect() {
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
        entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
    }

    /**
     * Loads all existing {@link Entry} objects from the database into the {@code entries} list.
     */
    private void loadEntries() {
        try {
            transaction.begin();
            Query query = entityManager.createQuery("SELECT e FROM Entry e");
            entries = query.getResultList();
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    /**
     * Persists the current {@link Entry} object to the database, reinitializes the {@code entry} field,
     * and updates the in-memory {@code entries} list.
     *
     * @return a redirect string ("redirect") used by JSF for navigation
     */
    public String addEntry() {
        try {
            transaction.begin();
            entry.checkHit(); // Custom logic to evaluate the entry
            entityManager.persist(entry);
            entries.add(entry);
            entry = new Entry();
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
        return "redirect";
    }

    /**
     * Deletes all {@link Entry} records from the database and clears the in-memory {@code entries} list.
     *
     * @return a redirect string ("redirect") used by JSF for navigation
     */
    public String clearEntries() {
        try {
            transaction.begin();
            Query query = entityManager.createQuery("DELETE FROM Entry");
            query.executeUpdate();
            entries.clear();
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
        return "redirect";
    }

    /**
     * Returns the current {@link Entry} object being processed.
     *
     * @return the current {@code entry}
     */
    public Entry getEntry() {
        return entry;
    }

    /**
     * Sets the current {@link Entry} object to be processed.
     *
     * @param entry the {@code Entry} object to set
     */
    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    /**
     * Returns the list of all stored {@link Entry} objects.
     *
     * @return the {@code entries} list
     */
    public List<Entry> getEntries() {
        return entries;
    }

    /**
     * Sets the list of stored {@link Entry} objects.
     *
     * @param entries the list of {@code Entry} objects to set
     */
    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }
}