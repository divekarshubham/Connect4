public class Main {
    public static void main(String[] args) {
        Contact c1 = new Contact.Builder("Bunty").address("YOLO street").email("haggandas@haha.com").note("").build();
        Contact c2 = new Contact.Builder("Bumty haha").address("YOLO street").phoneNumber("+009889008765").build();
        //System.out.println("Hello "+c1.hashCode()+"Hello2 "+c2.hashCode()+"Eq "+c1.equals(c2)+ "Hello "+c1.compareTo(c2) + "Hello "+c1);
        AddressBook a = new AddressBook();
        a.addContact(c1);
        a.addContact(c2);
        //a.saveState();
        AddressBook b = new AddressBook("C:\\Users\\Shubham Divekar\\IdeaProjects\\AddressBook\\AddressBook-2020-02-17");
        System.out.println(b.search("haha"));
        System.out.println(b.searchByField(ContactFields.EMAIL, "haha"));
    }
}
