public class Main {
    public static void main(String[] args) {
        Contact c1 = new Contact.Builder("Bunty").address("YOLO street").email("haggandas@haha.com").build();
        Contact c2 = new Contact.Builder("Bumty haha").address("YOLO street").phoneNumber("+009889008765").build();
        //System.out.println("Hello "+c1.hashCode()+"Hello2 "+c2.hashCode()+"Eq "+c1.equals(c2)+ "Hello "+c1.compareTo(c2) + "Hello "+c1);
        AddressBook a = new AddressBook();
        a.addContact(c1);
        a.addContact(c2);
        System.out.println(a.search("haha"));
        System.out.println(a.searchByField(ContactFields.EMAIL, "haha"));
    }
}
