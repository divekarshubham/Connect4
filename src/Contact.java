import java.util.Comparator;

//Assumption: cannot add a contact without a name
//TODO: implement clone and write modify
public class Contact implements Comparable<Contact>{
    private final String name;
    private final String address;
    private final String phoneNumber;
    private final String note;
    private final String emailAddress;
    private String contactInfo = "";

    private Contact(Builder builder){
        this.name = builder.name;
        this.address = builder.address;
        this.phoneNumber = builder.phoneNumber;
        this.note = builder.note;
        this.emailAddress = builder.emailAddress;
    }

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

    private boolean isNull(Object s){
        return s == null;
    }

    Contact modify(ContactFields parameter, String newVal){
        return new Builder(parameter == ContactFields.NAME ? newVal : this.name)
            .phoneNumber(parameter == ContactFields.PHONE ? newVal : this.phoneNumber)
            .address(parameter == ContactFields.ADDR ? newVal : this.address)
            .email(parameter == ContactFields.EMAIL ? newVal : this.emailAddress)
            .note(parameter == ContactFields.NOTE ? newVal : this.note)
            .build();
    }

    @Override public boolean equals(Object o){
        if (o == this)
            return true;
        if (!(o instanceof Contact))
            return false;
        Contact contact = (Contact) o;
        return contact.name.equals(name) && contact.phoneNumber.equals(phoneNumber) &&
                contact.emailAddress.equals(emailAddress) && contact.address.equals(address);

    }

    @Override public int hashCode(){
        int hash = name.hashCode();
        hash += isNull(phoneNumber) ? 0 : hash * 31 + phoneNumber.hashCode();
        hash += isNull(address) ? 0 : hash * 31 + address.hashCode();
        hash += isNull(emailAddress) ? 0 : hash * 31 + emailAddress.hashCode();
        hash += isNull(note) ? 0 : hash * 31 + note.hashCode();
        return hash;
    }

    @Override public String toString(){
        /* Lazy initialization of contactInfo*/
        if(contactInfo.equals("")) {
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

    private static final Comparator<Contact> COMPARATOR = Comparator.comparing((Contact c) -> c.name)
            .thenComparing(c -> (c.phoneNumber == null) ? "" : c.phoneNumber)
            .thenComparing(c -> (c.address == null) ? "" : c.address)
            .thenComparing(c -> (c.emailAddress == null) ? "" : c.emailAddress)
            .thenComparing(c -> (c.note == null) ? "" : c.note);

    public int compareTo(Contact c) {
        return COMPARATOR.compare(this, c);
    }

    public static class Builder{
        private final String name;
        private String address;
        private String phoneNumber;
        private String note;
        private String emailAddress;

        public Builder(String name){
            if (name.length() > 100)
                throw new IllegalArgumentException("Name " + name + " cannot be greater than 100 characters");
            if (name.length() == 0)
                throw new IllegalArgumentException("Name cannot be blank");
            this.name = name;
        }

        public Builder address(String val){
            if(val != null && !val.equals(""))
                this.address = val;
            return this;
        }

        public Builder phoneNumber(String val){
            //\d{10} matches 1234567890
            //(?:\d{3}-){2}\d{4} matches 123-456-7890
            //\(\d{3}\)\d{3}-?\d{4} matches (123)456-7890 or (123)4567890
            if(val != null && !val.equals("")) {
                String regex = "(\\+[0-9][0-9]?)?(\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4})";
                if (!val.matches(regex)) {
                    throw new IllegalArgumentException("Phone number " + val + " is invalid");
                }
                this.phoneNumber = val;
            }
            return this;
        }

        public Builder note(String val){
            if(val != null && !val.equals(""))
                this.note = val;
            return this;
        }

        public Builder email(String val){
            if(val != null && !val.equals("")) {
                String regex = "^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$";
                if (!val.matches(regex)) {
                    throw new IllegalArgumentException("Email " + val + " is invalid");
                }
                this.emailAddress = val;
            }
            return this;
        }

        public Contact build(){
            return new Contact(this);
        }
    }
}
