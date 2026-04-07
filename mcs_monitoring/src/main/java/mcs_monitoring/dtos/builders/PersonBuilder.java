//package mcs_monitoring.dtos.builders;
//
//import mcs_monitoring.dtos.PersonDTO;
//import mcs_monitoring.dtos.PersonDetailsDTO;
//import mcs_monitoring.entities.Person;
//
//public class PersonBuilder {
//
//    private PersonBuilder() {
//    }
//
//    public static PersonDTO toPersonDTO(Person person) {
//        return new PersonDTO(person.getId(), person.getName(), person.getAge());
//    }
//
//    public static PersonDetailsDTO toPersonDetailsDTO(Person person) {
//        return new PersonDetailsDTO(person.getId(), person.getName(), person.getAddress(), person.getAge());
//    }
//
//    public static Person toEntity(PersonDetailsDTO personDetailsDTO) {
//        return new Person(personDetailsDTO.getName(),
//                personDetailsDTO.getAddress(),
//                personDetailsDTO.getAge());
//    }
//}
