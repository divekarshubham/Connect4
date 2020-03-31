package edu.nyu.cs.pqs.ps1.testing;

import edu.nyu.cs.pqs.ps1.addressbook.AddressBook;
import edu.nyu.cs.pqs.ps1.addressbook.Entry;

import java.util.List;

public class TestDriver {
  public static void main(String[] args) {
    
    AddressBook book = new AddressBook();
    
    Entry.Builder b = new Entry.Builder();
//    b.name("John").address("444, 16th Street, New York, 63572")
//     .note("Some place good");
//    book.insertEntry(b.build());
//
//    b = new Entry.Builder();
//    b.name("Mick").phoneNumber("123-451-4598")
//      .emailAddress("asdf@ocoe.com");
//    book.insertEntry(b.build());
//
//    b = new Entry.Builder();
//    b.name("Emily").phoneNumber("725-567-9999")
//      .emailAddress("sss@ggg.com");
//    book.insertEntry(b.build());
//
//    b = new Entry.Builder();
//    b.name("Claire").phoneNumber("456-999-8777")
//      .emailAddress("asdf@ocoe.com");
//    book.insertEntry(b.build());
//
    b = new Entry.Builder();
    b.name("Thomas").phoneNumber("123-999-1111")
      .emailAddress("ttt@aaa.com");
    Entry etr = b.build();
    book.insertEntry(etr);

    b = new Entry.Builder();
    b.name("Thomas").phoneNumber("123-999-1111")
            .emailAddress("ttt@aaa.com");
    etr = b.build();

    // check duplicate insertion
    if(book.insertEntry(etr)) {
      System.out.print("Inserted duplicated entry.");
      b = new Entry.Builder();
      b.name("Thomas").phoneNumber("123-999-1111")
              .emailAddress("ttt@aaa.com");
      etr = b.build();
      System.out.println(book.remove(etr));
    }
    System.out.print(book);

    b = new Entry.Builder();
    b.name("Rick").phoneNumber("111-222-1111")
      .emailAddress("rrr@aaa.com");
    book.insertEntry(b.build());
    
    b = new Entry.Builder();
    b.name("Sandy").phoneNumber("123-999-1111")
      .emailAddress("sad@aaa.com");
    book.insertEntry(b.build());
    
    // Test save
    book.save("test.json");

    
    System.out.print(book);
    
    List<Entry> result = book.anyFieldsContainAll("John");
    
    for(Entry e : result) {
      System.out.println(e);
    }
    
    System.out.println("");
    
    result = book.phoneNumberContainAll("123-999");
    
    for(Entry e : result) {
      System.out.println(e);
    }
    
    book.remove(result.get(0));
    System.out.println(book);
    
    // Test loading
    AddressBook book2 = new AddressBook();
    System.out.println(book2);
    if(book2.load("test.json")) {
      System.out.println(book2);
    }

    
    // Save & load empty
    AddressBook book3 = new AddressBook();
    book3.save("empty.json");

    System.out.println(book3);
    if(book2.load("empty.json")) {
      System.out.println(book2);
    }
  }
}