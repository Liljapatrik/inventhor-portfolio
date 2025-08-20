package com.group3.inventhor.repository;

import com.group3.inventhor.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Tatiana Fløisbonn
 *
 * The AddressRepository interface provides methods to interact with the Address entity in the database.
 * It extends JpaRepository, which provides basic CRUD operations.
 *
 * The @Repository annotation indicates that this interface is a Spring Data repository.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    /**
     * @author Nils Patrik Lilja
     */
    Optional<Address> findByCountryAndCityAndStreetAndPostcode(String country, String city, String street, String postcode);
}
