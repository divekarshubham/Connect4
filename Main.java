//Test file to validate the functionality of the AddressBook

import edu.nyu.pqs.ps1.AddressBook;
import edu.nyu.pqs.ps1.Contact;
import edu.nyu.pqs.ps1.ContactFields;

public class Main {
  public static void main(String[] args) {
  
    //Creating some contacts
    Contact c1 = new Contact.Builder("Shubham Divekar").address("Street9211").email("sd@nyu.edu").note("This is me!").build();
    Contact c2 = new Contact.Builder("Someone Random").phoneNumber("+009889008765").build();
    Contact c3 = new Contact.Builder("Someone Random").phoneNumber("+00(988)9008765").build();
    
    //Testing the equals functionality
    System.out.println(c2.equals(c3));
    
    //Creating an addressbook and adding the contacts
    AddressBook a = new AddressBook();
    a.addContact(c1).addContact(c2).addContact(c3);
    System.out.println(a);
    
    //Modifying the contact information
    a.modifyContact(c1, ContactFields.EMAIL, "newEmail@sd.com");
    System.out.println(a.searchByField(ContactFields.EMAIL, "newEmail"));
    //Removing the contact based on a query
    a.removeContactByField(ContactFields.EMAIL, "newEmail");
    System.out.println(a.searchByField(ContactFields.EMAIL, "newEmail"));
    
    //Persisting the addressbook
    a.saveState();
    
    //Testing the newly created addressbook, please change the filepath and date here
    AddressBook b = new AddressBook("[filepath]AddressBook-[Date].json");
    System.out.println(a.equals(b));
  }
}
