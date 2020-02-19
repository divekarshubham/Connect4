package edu.nyu.pqs.ps1;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * AddressBook class represents the collection of contacts stored in a TreeSet
 * and the operation performed to add, modify and delete contacts. Contacts can
 * be added using a Contact object, List of Contacts and from the file that
 * stores contacts in a specific JSON format.
 *
 * You can initialize an empty AddressBook with the parameterless constructor
 * and the contents are stored to a new file (AddressBook-Date.json) or from a
 * stored AddressBook with the filename. All changes made to the AddressBook are
 * then saved to the same file(using the saveState() function.
 *
 * Duplicates are automatically eliminated out of the box with the use of
 * TreeSet and the use of Comparator in the Contacts class
 *
 * Class also overrides equals(), hashCode(), and toString() for better
 * readability and usability
 *
 * @author Shubham Divekar (sjd451)
 */
public class AddressBook {

    /** Storing all the contacts as a TreeSet to maintain order */
    private Set<Contact> storage;

    /** Location to store/read the addressbook from */
    private String filePath;

    public AddressBook() {
        storage = new TreeSet<>();
    }

    public AddressBook(String filePath) {
        storage = new TreeSet<>(importFromFile(filePath));
        this.filePath = filePath;
    }

    /**
     * If filePath is not specified create one of the format AddressBook-[Date].json
     */
    public void saveState() {
        if (filePath == null || filePath.equals("")) {
            String filePath = "AddressBook-" + LocalDate.now() + ".json";
            exportToFile(filePath);
            this.filePath = filePath;
        } else
            exportToFile(this.filePath);
    }

    /**
     * Use the simple-json library to create JSONObjects and serialize the
     * addressbook and write it to the specified path.
     *
     * @param filePath Path to write the storage
     */
    @SuppressWarnings("unchecked")
    private void exportToFile(String filePath) {
        /** Create an array of all the contacts and populate it with the contacts in the storage */
        JSONArray addressBook = new JSONArray();
        for (Contact contact : storage) {
            JSONObject contactDetails = new JSONObject();
            contactDetails.put("Name", contact.getName());
            contactDetails.put("PhoneNumber", contact.getPhone());
            contactDetails.put("Address", contact.getAddress());
            contactDetails.put("E-Mail", contact.getEmail());
            contactDetails.put("Note", contact.getNote());

            JSONObject contactObject = new JSONObject();
            contactObject.put("Contact", contactDetails);
            addressBook.add(contactObject);
        }

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(addressBook.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Read the entire dictionary and parse individual contacts to get the details
     * and then add them to the list. This may throw a FileNotFoundException if
     * filePath is incorrect or an IOException or a ParseException is the JSON is
     * not in the desired format.
     *
     * @param filePath file where the dictionary is stored
     * @return List of contacts from the JSON file
     */
    @SuppressWarnings("unchecked")
    private List<Contact> importFromFile(String filePath) {
        JSONParser jsonParser = new JSONParser();
        List<Contact> contacts = new ArrayList<>();
        try (FileReader reader = new FileReader(filePath)) {
            JSONArray addressBook = (JSONArray) jsonParser.parse(reader);
            addressBook.forEach(c -> contacts.add(parseContacts((JSONObject) c)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    /**
     * Parse each Contact fields from the JSON input file and extract the details
     * into an Contact
     *
     * @param contact Contact derived from the JSONObject array
     * @return Contact object from the builder
     */
    private Contact parseContacts(JSONObject contact) {
        JSONObject contactObject = (JSONObject) contact.get("Contact");
        return new Contact.Builder((String) contactObject.get("Name"))
            .phoneNumber((String) contactObject.get("PhoneNumber"))
            .address((String) contactObject.get("Address"))
            .email((String) contactObject.get("E-Mail"))
            .note((String) contactObject.get("Note")).build();
    }

    /**
     * Add individual contacts from Contact objects and they can be chained
     *
     * @param contact
     * @return AddressBook reference
     */
    public AddressBook addContact(Contact contact) {
        storage.add(contact);
        return this;
    }

    /**
     * Add multiple contacts from a List of Contact objects and they can be chained
     *
     * @param contacts
     * @return AddressBook reference
     */
    public AddressBook addMultipleContacts(List<Contact> contacts) {
        storage.addAll(contacts);
        return this;
    }

    /**
     * Add multiple contacts from a a new file and they can be chained
     *
     * @param filePath
     * @return AddressBook reference
     */
    public AddressBook addContactsFromFile(String filePath) {
        List<Contact> contacts = importFromFile(filePath);
        storage.addAll(contacts);
        return this;
    }

    /**
     * Remove contacts that satisfy a particular query in the specified field
     *
     * @param parameter The field you want to search in
     * @param query     Remove a contact that contains the following query
     * @return Number of contacts removed
     */
    public int removeContactByField(ContactFields parameter, String query) {
        List<Contact> contacts = searchByField(parameter, query);
        storage.removeAll(contacts);
        return contacts.size();
    }

    /**
     * Remove multiple contacts from the AddressBook
     *
     * @param contacts List of contacts to be removed
     * @return Number of contacts removed
     */
    public int removeMultipleContacts(List<Contact> contacts) {
        if (storage.removeAll(contacts))
            return contacts.size();
        return 0;
    }

    /**
     * Remove a single contact from the AddressBook
     *
     * @param contact Contact to be removed
     * @return 1 if contact found and removed else 0
     */
    public int removeContact(Contact contact) {
        if (storage.contains(contact)) {
            storage.remove(contact);
            return 1;
        }
        return 0;
    }

    /**
     * Universal search returns a contact if the given query is found as a substring
     * within any of fields of a contact. The toString() of the contact is used to
     * generate the summary and search
     *
     * @param query Substring to search
     * @return List of contacts containing the substring
     */
    public List<Contact> search(String query) {
        query = query.toLowerCase();
        List<Contact> searchResults = new ArrayList<>();
        for (Contact contact : storage) {
            if (contact.toString().toLowerCase().contains(query))
                searchResults.add(contact);
        }
        return searchResults;
    }

    /**
     * Search a specific field of every contact for the subquery.
     *
     * @param parameter The ContactFields enum represents the parameter to search
     *                  on.
     * @param query     Substring to search
     * @return List of contacts containing the substring
     */
    public List<Contact> searchByField(ContactFields parameter, String query) {
        query = query.toLowerCase();
        List<Contact> searchResults = new ArrayList<>();
        for (Contact contact : storage) {
            String field = "";
            switch (parameter) {
                case NAME:
                    field = contact.getName().toLowerCase();
                    break;
                case PHONE:
                    field = contact.getPhone().toLowerCase();
                    break;
                case ADDR:
                    field = contact.getAddress().toLowerCase();
                    break;
                case EMAIL:
                    field = contact.getEmail().toLowerCase();
                    break;
                case NOTE:
                    field = contact.getNote().toLowerCase();
                    break;
            }
            if (field.contains(query))
                searchResults.add(contact);
        }
        return searchResults;
    }

    /**
     * This uses the modify method of the contact to generate a clone with new
     * parameter as specified by the user. If the specified contact is not present
     * in the AddressBook an IllegalArgumentException is thrown else the contact is
     * replaced with the modified version.
     *
     * @param contact Contact to modify
     * @param field The ContactFields enum represents the parameter to modify
     * @param newVal Changed value
     */
    public void modifyContact(Contact contact, ContactFields field, String newVal) {
        if (storage.contains(contact)) {
            storage.remove(contact);
            contact = contact.modify(field, newVal);
            storage.add(contact);
        } else
            throw new IllegalArgumentException("Contact not present in AddressBook");
    }

    /**
     * Get a list of all the contacts in the AddressBook for viewing
     *
     * @return List of all Contacts
     */
    public List<Contact> getContacts() {
        return new ArrayList<>(storage);
    }

    @Override
    public String toString() {
        return storage.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AddressBook))
            return false;
        AddressBook addressBook = (AddressBook) o;
        return getContacts().equals(addressBook.getContacts());
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (Contact contact : storage) {
            hash += hash * 31 + contact.hashCode();
        }
        return hash;
    }

}
