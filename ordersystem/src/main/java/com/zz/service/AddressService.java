package com.zz.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zz.entity.UserAddress;
import com.zz.repository.AddressRepository;
@Service
public class AddressService {
	 @Resource
	    AddressRepository ar;
	  public UserAddress save(UserAddress Address){
	    	return ar.save(Address);
	    }
	  public List<UserAddress> getByUserid(String Userid){
		  return (List<UserAddress>)ar.findByUserid(Userid);
	  }
	  public void deleteAddress(String id){
	  	ar.deleteById(id);
	  }
}

