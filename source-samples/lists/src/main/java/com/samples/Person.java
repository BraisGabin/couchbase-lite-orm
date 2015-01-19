package com.samples;

import com.petterfactory.couchbaseliteorm.Entity;
import com.petterfactory.couchbaseliteorm.Field;

import java.util.List;

@Entity("person")
public class Person {

  @Field("emails")
  List<String> emails;

  @Field("address")
  List<Address> address;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Person person = (Person) o;

    if (address != null ? !address.equals(person.address) : person.address != null) return false;
    if (emails != null ? !emails.equals(person.emails) : person.emails != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = emails != null ? emails.hashCode() : 0;
    result = 31 * result + (address != null ? address.hashCode() : 0);
    return result;
  }
}
