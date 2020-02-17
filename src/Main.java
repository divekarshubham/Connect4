public class Main {
    public static void main(String[] args) {
        Contact c1 = new Contact.Builder("Bunty").address("YOLO street").email("haggandas@haha.com").build();
        Contact c2 = new Contact.Builder("Bunty").address("YOLO street").phoneNumber("+009889008765").build();
        //System.out.println("Hello "+c1.hashCode()+"Hello2 "+c2.hashCode()+"Eq "+c1.equals(c2));
        System.out.println("Hello "+c1);
        System.out.println("Hello "+c1.compareTo(c2));
    }
}
