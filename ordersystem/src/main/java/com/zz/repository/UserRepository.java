package com.zz.repository;

import com.zz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,String> {
     List<User> findByUsername(String username);
     List<User> findByEmail(String email);

     User findByUsernameAndPassword(String username, String password);


}
