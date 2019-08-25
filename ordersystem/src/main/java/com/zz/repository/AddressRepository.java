
package com.zz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zz.entity.UserAddress;

public interface AddressRepository extends JpaRepository<UserAddress,String>{
	List<UserAddress> findByUserid(String Userid);
	List<UserAddress> getById(String id);
	List<UserAddress> findAllById(String id);

	void deleteById(String id);
}