package com.iprody.userprofile.profileservice.repository;

import com.iprody.userprofile.profileservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
