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

//TODO: comply with the stylesheet
//TODO: add comments and javadocs and readme.md
//TODO: implement tostring etc
public class AddressBook {

    private Set<Contact> storage;
    private String filePath;

    public AddressBook() {
        storage = new TreeSet<>();
    }

    public AddressBook(String filePath) {
        storage = new TreeSet<>(importFromFile(filePath));
        this.filePath = filePath;
    }

    public void saveState() {
        if (filePath == null || filePath.equals("")) {
            String filePath = "AddressBook-" + LocalDate.now();
            System.out.println(filePath);
            exportToFile(filePath);
            this.filePath = filePath;
        } else
            exportToFile(this.filePath);
    }

    @SuppressWarnings("unchecked")
    private void exportToFile(String filePath) {
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

    private Contact parseContacts(JSONObject contact) {
        JSONObject contactObject = (JSONObject) contact.get("Contact");
        return new Contact.Builder((String) contactObject.get("Name")).phoneNumber((String) contactObject.get("Phone"))
                .address((String) contactObject.get("Address")).email((String) contactObject.get("E-Mail"))
                .note((String) contactObject.get("Note")).build();
    }

    public void addContact(Contact contact) {
        storage.add(contact);
    }

    public void addMultipleContacts(List<Contact> contacts) {
        storage.addAll(contacts);
    }

    public void addContactsFromFile(String filePath){
        List<Contact> contacts = importFromFile(filePath);
        storage.addAll(contacts);
    }

    public int removeContactByField(ContactFields parameter, String query) {
        List<Contact> contacts = searchByField(parameter, query);
        storage.removeAll(contacts);
        return contacts.size();
    }

    public int removeMultipleContacts(List<Contact> contacts){
        if(storage.removeAll(contacts))
            return contacts.size();
        return 0;
    }

    public int removeContact(Contact contact) {
        if (storage.contains(contact)) {
            storage.remove(contact);
            return 1;
        }
        return 0;
    }

    public List<Contact> search(String query) {
        query = query.toLowerCase();
        List<Contact> searchResults = new ArrayList<>();
        for (Contact contact : storage) {
            if (contact.toString().toLowerCase().contains(query))
                searchResults.add(contact);
        }
        return searchResults;
    }

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

    public void modifyContact(Contact contact, ContactFields field, String newVal){
        if(storage.contains(contact)) {
            System.out.println("hi");
            storage.remove(contact);
            contact = contact.modify(field, newVal);
            System.out.println(contact);
            storage.add(contact);
        }
        else
            throw new IllegalArgumentException("Contact not present in AddressBook");
    }

}
