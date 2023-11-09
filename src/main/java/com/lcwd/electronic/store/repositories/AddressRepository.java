package com.lcwd.electronic.store.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lcwd.electronic.store.entities.Address;
import com.lcwd.electronic.store.entities.User;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
	public Set<Address> getAddressByUser(User user);
}
