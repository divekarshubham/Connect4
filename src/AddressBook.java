import java.util.ArrayList;
import java.util.List;

public class AddressBook {

    private List<Contact> storage = new ArrayList<>();

    public void addContact(Contact contact){
        storage.add(contact);
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
}
