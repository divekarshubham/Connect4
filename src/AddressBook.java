import java.util.*;

public class AddressBook {

    private Set<Contact> storage = new TreeSet<>();

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
