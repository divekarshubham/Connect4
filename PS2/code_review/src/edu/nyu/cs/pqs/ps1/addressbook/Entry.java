package edu.nyu.cs.pqs.ps1.addressbook;

/**
 * An immutable class represents a single {@code Entry} in the {@link AddressBook}.
 */
public final class Entry {

  private final String name;
  private final String address;
  private final String phoneNumber;
  private final String emailAddress;
  private final String note;

  /**
   * A builder class for {@link Entry}.
   * 
   * <p>
   * This class follows the builder pattern. Some example usage is:
   * 
   * {@code builder.name("John").phoneNumber("(123)124-4444")}
   */
  public static final class Builder {
    // Optional parameters
    private String name = "";
    private String address = "";
    private String phoneNumber = "";
    private String emailAddress = "";
    private String note = "";

    public Builder() {}

    /**
     * Set optional parameter {@code name} in the {@link Builder} instance
     * 
     * <p>
     * The leading and tailing whitespace of input {@code String} is removed. {@code null} input is
     * treated as empty string.
     * 
     * @param name a {@code String}
     * @return self-reference to support method chaining
     */
    public Builder name(String name) {
      if (name == null) {
        name = "";
      }
      this.name = name.trim();
      return this;
    }

    /**
     * Set optional parameter {@code address} in the {@link Builder} instance
     * 
     * <p>
     * The leading and tailing whitespace of input {@code String} is removed. {@code null} input is
     * treated as empty string.
     * 
     * @param address a {@code String}
     * @return self-reference to support method chaining
     */
    public Builder address(String address) {
      if (address == null) {
        address = "";
      }
      this.address = address.trim();
      return this;
    }

    /**
     * Set optional parameter {@code phoneNumber} in the {@link Builder} instance
     * 
     * <p>
     * The leading and tailing whitespace of input {@code String} is removed. {@code null} input is
     * treated as empty string.
     * 
     * @param phoneNumber a {@code String}
     * @return self-reference to support method chaining
     */
    public Builder phoneNumber(String phoneNumber) {
      if (phoneNumber == null) {
        phoneNumber = "";
      }
      this.phoneNumber = phoneNumber.trim();
      return this;
    }

    /**
     * Set optional parameter {@code emailAddress} in the {@link Builder} instance
     * 
     * <p>
     * The leading and tailing whitespace of input {@code String} is removed. {@code null} input is
     * treated as empty string.
     * 
     * @param emailAddress a {@code String}
     * @return self-reference to support method chaining
     */
    public Builder emailAddress(String emailAddress) {
      if (emailAddress == null) {
        emailAddress = "";
      }
      this.emailAddress = emailAddress.trim();
      return this;
    }

    /**
     * Set optional parameter {@code note} in the {@link Builder} instance
     * 
     * <p>
     * The leading and tailing whitespace of input {@code String} is removed. {@code null} input is
     * treated as empty string.
     * 
     * @param note a {@code String}
     * @return self-reference to support method chaining
     */
    public Builder note(String note) {
      if (note == null) {
        note = "";
      }
      this.note = note.trim();
      return this;
    }

    /**
     * Build {@link Entry} according to the {@link Builder}
     * 
     * @return a instance of {@link Entry}
     */
    public Entry build() {
      return new Entry(this);
    }
  }

  private Entry(Builder builder) {
    this.name = builder.name;
    this.address = builder.address;
    this.phoneNumber = builder.phoneNumber;
    this.emailAddress = builder.emailAddress;
    this.note = builder.note;
  }

  public String getName() {
    return name;
  }

  public String getAddress() {
    return address;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public String getNote() {
    return note;
  }

  /**
   * Return a new instance of {@link Builder} that can build an identical {@link Entry}.
   * 
   * <p>
   * This function is provided to ease the process of modifying some fields of the immutable
   * {@link Entry} instance. One can achieve similar behavior to the setter on mutable object with
   * the following code:
   * 
   * {@code
   *    Entry modifiedEntry = entry.getBuilder().name("xxxx")
   *    .phoneNumber("newNumber").build();
   * }
   * 
   * @return a {@link Builder} instance that has the field set as this Entry.
   */
  public Builder getBuilder() {
    Builder builder = new Builder();
    builder.name(name).address(address).phoneNumber(phoneNumber).emailAddress(emailAddress)
        .note(note);
    return builder;
  }

  /**
   * Return a string representation.
   * 
   * <p>
   * The format is subjected to changes, but a reasonable format is: (Entry; Name:name;
   * PhoneNumber:phoneNumber; Email:emailAddress; Note:note)
   */
  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("(Entry");
    if (!name.equals("")) {
      stringBuilder.append("; Name: ").append(name);
    }
    if (!address.equals("")) {
      stringBuilder.append("; Address: ").append(address);
    }
    if (!phoneNumber.equals("")) {
      stringBuilder.append("; Phone Number: ").append(phoneNumber);
    }
    if (!emailAddress.equals("")) {
      stringBuilder.append("; Email: ").append(emailAddress);
    }
    if (!note.equals("")) {
      stringBuilder.append("; Note: ").append(note);
    }
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
}
