package com.Tekarch.DataStoreService.Repositories;

import com.Tekarch.DataStoreService.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
