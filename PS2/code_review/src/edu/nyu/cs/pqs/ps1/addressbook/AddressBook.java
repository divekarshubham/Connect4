package edu.nyu.cs.pqs.ps1.addressbook;

import edu.nyu.cs.pqs.ps1.addressbook.Entry.Builder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * A general purpose address book that supports add, search and remove.
 */
public final class AddressBook {

  private static final String API_VERSION = "0.1";
  private List<Entry> listOfEntries;

  public AddressBook() {
    listOfEntries = new LinkedList<Entry>();
  }

  /**
   * Clear all {@link Entry} inserted.
   * 
   * <p>
   * This function cleared all the previously inserted in {@code AddressBook}.
   */
  public void clear() {
    listOfEntries.clear();
  }

  /**
   * Insert an unique {@link Entry}.
   * 
   * <p>
   * This function check the inserting {@link Entry} if it exists in the {@link AddressBook}
   * instance. If not, do the insertion.
   * 
   * @param e an {@link Entry} instance
   * @return true if success, false otherwise
   */
  public boolean insertEntry(Entry e) {
    if (!listOfEntries.contains(e)) {
      listOfEntries.add(e);
      return true;
    }
    return false;
  }

  /**
   * Helper function for {@link anyFieldsContainAll}.
   * 
   * <p>
   * Return true if input {@code String} is a substring of any fields
   * 
   * @param str a {@code String} to check if {@link Entry} contains
   * @return true if any field meets the criteria, false otherwise
   */
  private static boolean containsString(Entry e, String str) {
    return e.getName().contains(str) || e.getAddress().contains(str)
        || e.getPhoneNumber().contains(str) || e.getEmailAddress().contains(str)
        || e.getNote().contains(str);
  }

  /**
   * Search for all {@link Entry} instances that any of the fields contains the string
   * 
   * <p>
   * Return a list contains all {@code Entry} that was inserted that any of its field contains the
   * input string.
   * 
   * @param str a {@code String} to be matched
   * @return a list of {@link Entry} that meets the criteria
   */
  public List<Entry> anyFieldsContainAll(String str) {
    List<Entry> returnList = new LinkedList<Entry>();
    for (Entry itr : listOfEntries) {
      if (containsString(itr, str)) {
        returnList.add(itr);
      }
    }
    return returnList;
  }

  /**
   * Search for all {@link Entry} instances that any of the fields contains the string
   * 
   * <p>
   * Return a list contains all {@code Entry} that was inserted whose {@code name} field contains
   * the input string.
   * 
   * @param str a {@code String} to be matched
   * @return a list of {@link Entry} that meets the criteria
   */
  public List<Entry> nameContainAll(String str) {
    List<Entry> returnList = new LinkedList<Entry>();
    for (Entry itr : listOfEntries) {
      if (itr.getName().contains(str)) {
        returnList.add(itr);
      }
    }
    return returnList;
  }

  /**
   * Search for all {@link Entry} instances that any of the fields contains the string
   * 
   * <p>
   * Return a list contains all {@code Entry} that was inserted whose {@code address} field contains
   * the input string.
   * 
   * @param str a {@code String} to be matched
   * @return a list of {@link Entry} that meets the criteria
   */
  public List<Entry> addressContainAll(String str) {
    List<Entry> returnList = new LinkedList<Entry>();
    for (Entry itr : listOfEntries) {
      if (itr.getAddress().contains(str)) {
        returnList.add(itr);
      }
    }
    return returnList;
  }

  /**
   * Search for all {@link Entry} instances that any of the fields contains the string
   * 
   * <p>
   * Return a list contains all {@code Entry} that was inserted whose {@code phoneNumber} field
   * contains the input string.
   * 
   * @param str a {@code String} to be matched
   * @return a list of {@link Entry} that meets the criteria
   */
  public List<Entry> phoneNumberContainAll(String str) {
    List<Entry> returnList = new LinkedList<Entry>();
    for (Entry itr : listOfEntries) {
      if (itr.getPhoneNumber().contains(str)) {
        returnList.add(itr);
      }
    }
    return returnList;
  }

  /**
   * Search for all {@link Entry} instances that any of the fields contains the string
   * 
   * <p>
   * Return a list contains all {@code Entry} that was inserted whose {@code emailAddress} field
   * contains the input string.
   * 
   * @param str a {@code String} to be matched
   * @return a list of {@link Entry} that meets the criteria
   */
  public List<Entry> emailAddressContainAll(String str) {
    List<Entry> returnList = new LinkedList<Entry>();
    for (Entry itr : listOfEntries) {
      if (itr.getEmailAddress().contains(str)) {
        returnList.add(itr);
      }
    }
    return returnList;
  }

  /**
   * Search for all {@link Entry} instances that any of the fields contains the string
   * 
   * <p>
   * Return a list contains all {@code Entry} that was inserted whose {@code note} field contains
   * the input string.
   * 
   * @param str a {@code String} to be matched
   * @return a list of {@link Entry} that meets the criteria
   */
  public List<Entry> noteContainAll(String str) {
    List<Entry> returnList = new LinkedList<Entry>();
    for (Entry itr : listOfEntries) {
      if (itr.getNote().contains(str)) {
        returnList.add(itr);
      }
    }
    return returnList;
  }

  /**
   * Remove {@code Entry} reference in the {@code AddressBook}
   * 
   * <p>
   * This function remove the input {@link Entry} reference in the address book and return true if
   * it existed. There is only one {@link Entry} within one {@link AddressBook} instance. This is
   * guaranteed by the {@link insertEntry} function.
   * 
   * @param e a reference of the entry to be removed
   * @return true if success, false otherwise.
   */
  public boolean remove(Entry e) {
    return listOfEntries.remove(e);
  }

  /***
   * Return a string representation.
   * 
   * <p>
   * Return a readable string representation of this address book. If the number of {@link Entry}
   * exceeds 4, only the first 3 entries and the last entry are presented.
   * 
   * <p>
   * One of the possible format is:
   * 
   * {@code
   * [AddressBook
   *   entry0.toString()
   *   entry1.toString()
   *   entry2.toString()
   *   ...
   *   entryN.toString()
   * ]
   * }
   * 
   * @return a {@code String} representation of this {@link AddressBook}
   */
  @Override
  public String toString() {
    StringBuilder rep = new StringBuilder();
    rep.append("[Address Book\n");
    for (int i = 0; i < listOfEntries.size() && i < 3; i++) {
      Entry e = listOfEntries.get(i);
      rep.append("  ").append(e.toString()).append("\n");
    }

    if (listOfEntries.size() > 2) {
      rep.append("  ...\n");
      Entry e = listOfEntries.get(listOfEntries.size() - 1);
      rep.append("  ").append(e.toString()).append("\n");
    }

    rep.append("]\n");
    return rep.toString();
  }


  /**
   * Helper function to construct JSON representation of an {@link Entry}
   * 
   * @param e an {@link Entry} to construct JSON
   * @return a {@code JSONObject} storing the info of an {@link Entry} instance.
   */
  private JSONObject entryToJsonObject(Entry e) {
    JSONObject obj = new JSONObject();
    obj.put("name", e.getName());
    obj.put("address", e.getAddress());
    obj.put("phoneNumber", e.getPhoneNumber());
    obj.put("emailAddress", e.getEmailAddress());
    obj.put("note", e.getNote());
    return obj;
  }

  /**
   * Save {@link AddressBook} instance to a file in JSON form
   * 
   * <p>
   * This function save the {@link AddressBook} to project root directory in JSON form:
   * 
   * {@code
   *    {
   *        "ApiVersion": "0.1",
   *        "entries":[
   *            {
   *                "name": ..., "address": ..., "phoneNumber": ..., "emailAddress": ..., "note":
   * ... }, ... ] } }
   * 
   * @param fileName a string of the file name to store
   * @return true if saving success, otherwise return false
   */
  public boolean save(String fileName) {
    JSONObject obj = new JSONObject();
    obj.put("ApiVersion", API_VERSION);
    JSONArray array = new JSONArray();
    for (Entry e : listOfEntries) {
      array.add(entryToJsonObject(e));
    }
    obj.put("Entries", array);
    try (FileWriter file = new FileWriter(fileName)) {
      file.write(obj.toJSONString());
      file.flush();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Load {@link AddressBook} from a file
   * 
   * <p>
   * This function will clear the current {@link AddressBook} and load the {@link Entry} stored in
   * {@code fileName} at project root directory.
   * 
   * @param fileName a string of the file name to load
   * @return true if loading success, otherwise return false
   */
  public boolean load(String fileName) {
    JSONParser parser = new JSONParser();
    clear();
    try (FileReader file = new FileReader(fileName)) {
      Object obj = parser.parse(file);
      JSONObject jobj = (JSONObject) obj;
      String apiVersion = (String) jobj.get("ApiVersion");
      if (!apiVersion.equals(API_VERSION)) {
        System.err.println("API Version unmatched");
        return false;
      }
      JSONArray array = (JSONArray) jobj.get("Entries");
      for (int i = 0; i < array.size(); i++) {
        Builder builder = new Builder();
        JSONObject jsonEntry = (JSONObject) array.get(i);
        builder.name((String) jsonEntry.get("name")).address((String) jsonEntry.get("address"))
            .phoneNumber((String) jsonEntry.get("phoneNumber"))
            .emailAddress((String) jsonEntry.get("emailAddress"))
            .note((String) jsonEntry.get("note"));
        insertEntry(builder.build());
      }
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    } catch (ParseException e) {
      e.printStackTrace();
      return false;
    }
  }
}
