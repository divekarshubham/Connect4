//Assumption: cannot add a contact without a name
//TODO: add input validations to the entries
public class Contact {
    private final String name;
    private final String address;
    private final int phoneNumber;
    private final String note;
    private final String emailAddress;

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

    public static class Builder{
        private final String name;
        private String address;
        private int phoneNumber;
        private String note;
        private String emailAddress;

        public Builder(String name){
            this.name = name;
        }

        public Builder address(String val){
            this.address = val;
            return this;
        }

        public Builder phoneNumber(int val){
            this.phoneNumber = val;
            return this;
        }

        public Builder note(String val){
            this.note = val;
            return this;
        }

        public Builder email(String val){
            this.emailAddress = val;
            return this;
        }

        public Contact build(){
            return new Contact(this);
        }
    }
}
