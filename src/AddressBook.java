import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//TODO:change the remove methods, make remove by field or remove contact only
//TODO: comply with the stylesheet
//TODO: add comments and javadocs and readme.md
//TODO: append to existing file
public class AddressBook {

    private Set<Contact> storage;
    private String filepath;
    public AddressBook(){
        storage = new TreeSet<>();
    }

    public AddressBook(String filepath){
        storage = new TreeSet<>(importFromFile(filepath));
        this.filepath = filepath;
    }

    public void saveState(){
        if(filepath == null || filepath.equals("")) {
            String filepath = "AddressBook-"+ LocalDate.now();
            System.out.println(filepath);
            exportToFile(filepath);
        }
        else
            exportToFile(this.filepath);
    }

    private void exportToFile(String filepath){
        JSONArray addressBook = new JSONArray();
        for(Contact contact : storage){
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

        try(FileWriter file = new FileWriter(filepath)){
            file.write(addressBook.toJSONString());
            file.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    private List<Contact> importFromFile(String filepath){
        JSONParser jsonParser = new JSONParser();
        List<Contact> contacts = new ArrayList<>();
        try (FileReader reader = new FileReader(filepath)){
            JSONArray addressBook = (JSONArray) jsonParser.parse(reader);
            addressBook.forEach( c -> contacts.add(parseContacts((JSONObject) c)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    private Contact parseContacts(JSONObject contact){
        JSONObject contactObject = (JSONObject) contact.get("Contact");
        return new Contact.Builder((String) contactObject.get("Name"))
                .phoneNumber((String) contactObject.get("Phone"))
                .address((String) contactObject.get("Adress"))
                .email((String) contactObject.get("E-Mail"))
                .note((String) contactObject.get("Note"))
                .build();
    }

    public void addContact(Contact contact){
        storage.add(contact);
    }

    public void addMultipleContacts(Contact[] contacts){
        Collections.addAll(storage, contacts);
    }

    public int removeContact(String name){
        List<Contact> contactsToDelete = new ArrayList<>();

        for(Contact c : storage){
            if(c.getName().equals(name)) {
                contactsToDelete.add(c);
            }
        }

        for(Contact c : contactsToDelete){
            storage.remove(c);
        }

        return contactsToDelete.size();
    }

    public int removeContact(Contact contact){
        if(storage.contains(contact)){
            storage.remove(contact);
            return 1;
        }
        return 0;
    }

    public List<Contact> search(String query){
        query = query.toLowerCase();
        List<Contact> searchResults = new ArrayList<>();
        for (Contact contact : storage){
            if (contact.toString().toLowerCase().contains(query))
                searchResults.add(contact);
        }
        return searchResults;
    }

    public List<Contact> searchByField(ContactFields parameter, String query){
        query = query.toLowerCase();
        List<Contact> searchResults = new ArrayList<>();
        for(Contact contact : storage) {
            String field = "";
            switch (parameter) {
                case NAME: field = contact.getName().toLowerCase();
                break;
                case PHONE: field = contact.getPhone().toLowerCase();
                    break;
                case ADDR: field = contact.getAddress().toLowerCase();
                    break;
                case EMAIL: field = contact.getEmail().toLowerCase();
                    break;
                case NOTE: field = contact.getNote().toLowerCase();
                    break;
            }
            if(field.contains(query))
                searchResults.add(contact);
        }
        return searchResults;
    }

}
