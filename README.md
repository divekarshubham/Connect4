# Production Quality Software - Assignment 1

This is the library implementation of the AddressBook library. The user can add, modify and delete contacts using this module. The AddressBook is a collection of such contacts. With this the user can search the entire storage for a keyword  or narrow down the search to a particular field. Please refer to the javadocs for the implementation details.

### Assumptions

A user cannot add a contact without a name

## Features:

- Automatic removal of **duplicate** items because of `TreeSet` and implementation of Comparable in `Contact class`

- Builder Pattern style to create a new contact. Use `Contact c1 = new Contact.Builder("Your_name").address("Address").email("name@company.domain").note("Something").build();`. This makes the creation robust and can be modified later using the `modifyContact()` method in the `AddressBook class`

- Can export the contents to a JSON file using simple-json

- Can read from external JSON file

- **Input Validations** performed inside the module:
- Phone Number- Accepted forms are
    - 9703512652
    - 970-351-2652
    - (970)351-2652 or (970)3512652
    - +19703512652 or +919703512652
  - Name should not be blank or contain >100 characters
  - Email should be of the format [alphanumeric combination with `-_.`] @ [list of domains separated by `.`] . [organization] like shubham.d@nyu.edu

## Format of the JSON output/input:

```json
[
    { "Contact":{
        		"E-Mail":"name@domain.org",
        		"Address":"some other street",
        		"Note":"Not important",
        		"PhoneNumber":"+009889008765",
        		"Name":"Name Surname"
    	}},
    { "Contact":{
        		"E-Mail":"name2@domain.org",
        		"Address":"some street",
        		"Note":" Very important",
        		"PhoneNumber":"+00(988)9008765",
        		"Name":"Name Surname"
    }}]
```

## How to install dependencies

You will require google simple-json package to successfully compile the code. You can either download the jar from the web and include it in the project or follow these instructions for installing it in IntelliJ

1. Please open the project in IntelliJ
2. Navigate to File > Project Structure > Modules
3. Now, click on the `Dependencies` tab
4. Click on the `+` icon
5. Select Library > From Maven
6. Search for `com.googlecode.json-simple` Select the package that comes up.
7. Hit `Ok` and `Apply`



## Testing the code

You can use the following file to test the functionality of the Library

```java
import edu.nyu.pqs.ps1.AddressBook;
import edu.nyu.pqs.ps1.Contact;
import edu.nyu.pqs.ps1.ContactFields;

public class Main {
  public static void main(String[] args) {
    Contact c1 = new Contact.Builder("Shubham Divekar").address("Street9211").email("sd@nyu.edu").note("This is me!").build();
    Contact c2 = new Contact.Builder("Someone Random").phoneNumber("+009889008765").build();
    Contact c3 = new Contact.Builder("Someone Random").phoneNumber("+00(988)9008765").build();
    System.out.println(c2.equals(c3));
    AddressBook a = new AddressBook();
    a.addContact(c1).addContact(c2).addContact(c3);
    System.out.println(a);
    a.modifyContact(c1, ContactFields.EMAIL, "newEmail@sd.com");
    System.out.println(a.searchByField(ContactFields.EMAIL, "newEmail"));
    a.removeContactByField(ContactFields.EMAIL, "newEmail");
    System.out.println(a.searchByField(ContactFields.EMAIL, "newEmail"));
    a.saveState();
    AddressBook b = new AddressBook("[filepath]AddressBook-[Date].json");
    System.out.println(a.equals(b));
  }
}
```
