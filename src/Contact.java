/**
 *  The contact class stores individual entries of the address book. It consists of the details of a
 *  contact which are Name (mandatory), phone-number, address, email-address and a note. The class
 *  is constructed using the Builder pattern and the object created is immutable. A class is
 *  instantiated with a name and you can add additional details with the use of functions like
 *  .email(), .phone() etc.
 *
 * The class overrides the equals() and hashCode() functions to use the class with generics.
 * toString() is implemented to make the contact information readable. The class also creates a
 * COMPARATOR to make it usable by SortedSets and similar Collections.
 *
 * The modify method constructs and returns a new Object with one field changed to preserve
 * immutability
 *
 * @author Shubham Divekar (sjd451)
 */

import java.util.Comparator;

//Assumption: cannot add a contact without a name
public class Contact implements Comparable<Contact> {
    private final String name;
    private final String address;
    private final String phoneNumber;
    private final String note;
    private final String emailAddress;
    /** Create a string lazily to be returned when toString is invoked*/
    private String contactInfo = "";

    private Contact(Builder builder) {
        this.name = builder.name;
        this.address = builder.address;
        this.phoneNumber = builder.phoneNumber;
        this.note = builder.note;
        this.emailAddress = builder.emailAddress;
    }

    /** Generic functions to get the details of the contact*/
    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return isNull(phoneNumber) ? "" : this.phoneNumber;
    }

    public String getAddress() {
        return isNull(address) ? "" : this.address;
    }

    public String getEmail() {
        return isNull(emailAddress) ? "" : this.emailAddress;
    }

    public String getNote() {
        return isNull(note) ? "" : this.note;
    }

    private boolean isNull(Object s) {
        return s == null;
    }

    /**
     * Since the Contact is immutable we create a new Object and return it with the changed field.
     * This method is package-private and can only be called by using the modifyContact() method
     * in the AddressBook class
     *
     * @param parameter The field to be changed
     * @param newVal The value to be changed to
     * @return New Contact with changed parameter
     */
    Contact modify(ContactFields parameter, String newVal) {
        return new Builder(parameter == ContactFields.NAME ? newVal : this.name)
            .phoneNumber(parameter == ContactFields.PHONE ? newVal : this.phoneNumber)
            .address(parameter == ContactFields.ADDR ? newVal : this.address)
            .email(parameter == ContactFields.EMAIL ? newVal : this.emailAddress)
            .note(parameter == ContactFields.NOTE ? newVal : this.note).build();
    }

    /**
     * Returns the comparison between two contacts and compares all the field values. For
     * equivalency of contacts '+()-' characters are ignored from phone-numbers.
     *
     * @param o Contact to compare
     * @return true if two contacts are equal
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Contact))
            return false;
        Contact contact = (Contact) o;
        if (contact.name.equals(name)) {
            if (contact.getPhone().replaceAll("[^\\d]", "").equals(this.getPhone()
                .replaceAll("[^\\d]", ""))) {
                if (contact.getAddress().equals(this.getAddress())) {
                    if (contact.getEmail().equals(this.getEmail())) {
                        if (contact.getNote().equals(this.getNote())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;

    }

    /**
     * Generates hashCode based on all the parameters
     *
     * @return hashCode of the contact
     */
    @Override
    public int hashCode() {
        int hash = name.hashCode();
        hash += isNull(phoneNumber) ? 0 : hash * 31 + phoneNumber.hashCode();
        hash += isNull(address) ? 0 : hash * 31 + address.hashCode();
        hash += isNull(emailAddress) ? 0 : hash * 31 + emailAddress.hashCode();
        hash += isNull(note) ? 0 : hash * 31 + note.hashCode();
        return hash;
    }

    /**
     * Creates a string of contact information based on all available(not null) fields. It follows
     * lazy initialization and created the string the first time the toString() is called since the
     * class is immutable
     *
     * @return Contact information
     */
    @Override
    public String toString() {
        if (contactInfo.equals("")) {
            StringBuilder contactInfo = new StringBuilder("Contact Info = { Name:" + this.name);
            contactInfo.append(isNull(phoneNumber) ? "" : ", Phone:" + this.phoneNumber);
            contactInfo.append(isNull(address) ? "" : ", Address:" + this.address);
            contactInfo.append(isNull(emailAddress) ? "" : ", Email:" + this.emailAddress);
            contactInfo.append(isNull(note) ? "" : ", Note:" + this.note);
            contactInfo.append(" }");
            this.contactInfo = contactInfo.toString();
        }

        return contactInfo;
    }

    /**
     *  Initialize a comparator to make the class be used efficiently with generics and Collections
     */
    private static final Comparator<Contact> COMPARATOR = Comparator.comparing((Contact c) -> c.name)
        .thenComparing(c -> (c.phoneNumber == null) ? "" : c.phoneNumber.replaceAll("[^\\d]", ""))
        .thenComparing(c -> (c.address == null) ? "" : c.address)
        .thenComparing(c -> (c.emailAddress == null) ? "" : c.emailAddress)
        .thenComparing(c -> (c.note == null) ? "" : c.note);

    public int compareTo(Contact c) {
        return COMPARATOR.compare(this, c);
    }

    /**
     * Builder subclass to implement the Builder Pattern, wherein the name is passed to the Builder
     * constructor and further parameters can be added using the email(), address() etc. functions,
     * where the null or blank values are omitted from the object.
     * All validations are performed inside the builder functions and throw IllegalArgumentException
     * with different messages according to the validation and the field
     */
    public static class Builder {
        private final String name;
        private String address;
        private String phoneNumber;
        private String note;
        private String emailAddress;

        public Builder(String name) {
            if (name.length() > 100)
                throw new IllegalArgumentException("Name " + name + " cannot be greater than 100 characters");
            if (name.length() == 0)
                throw new IllegalArgumentException("Name cannot be blank");
            this.name = name;
        }

        public Builder address(String val) {
            if (val != null && !val.equals(""))
                this.address = val;
            return this;
        }

        public Builder phoneNumber(String val) {
            /**
             * Use Regular expression to evaluate the phone number
             * (\+[0-9][0-9]?)? matches international codes +1 or +91
             * \d{10} matches 1234567890
             * (?:\d{3}-){2}\d{4} matches 123-456-7890
             * \(\d{3}\)\d{3}-?\d{4} matches (123)456-7890 or (123)4567890
             */
            if (val != null && !val.equals("")) {
                String regex = "(\\+[0-9][0-9]?)?(\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4})";
                if (!val.matches(regex)) {
                    throw new IllegalArgumentException("Phone number " + val + " is invalid");
                }
                this.phoneNumber = val;
            }
            return this;
        }

        public Builder note(String val) {
            if (val != null && !val.equals(""))
                this.note = val;
            return this;
        }

        public Builder email(String val) {
            /**
             * Use Regular expression to evaluate the email
             * [\w-_.+]*[\w-_.]@1 or more characters with -_and . followed by @
             * ([\w]+\.)+ matches 1 or more domains like cs.nyu. or pqs.cs.nyu.
             * [\w]+[\w] ends with an org like edu or com
             */
            if (val != null && !val.equals("")) {
                String regex = "^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$";
                if (!val.matches(regex)) {
                    throw new IllegalArgumentException("Email " + val + " is invalid");
                }
                this.emailAddress = val;
            }
            return this;
        }

        /**
         * Build and return the Contact by passing in the builder object created from the methods
         * above
         *
         * @return Contact object
         */
        public Contact build() {
            return new Contact(this);
        }
    }
}
