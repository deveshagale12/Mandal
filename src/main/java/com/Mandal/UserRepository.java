package com.Mandal;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO users (name, address, mobile_no, email, password, dob) VALUES (:name, :address, :mobileNo, :email, :password, :dob)", nativeQuery = true)
    void registerUser(@Param("name") String name, 
                      @Param("address") String address, 
                      @Param("mobileNo") String mobileNo, 
                      @Param("email") String email, 
                      @Param("password") String password, 
                      @Param("dob") java.time.LocalDate dob);

    @Query(value = "SELECT * FROM users", 
           countQuery = "SELECT count(*) FROM users", 
           nativeQuery = true)
    Page<User> findAllUsers(Pageable pageable);
    
    @Query(value = "SELECT COUNT(*) FROM users WHERE email = :email OR mobile_no = :mobileNo", nativeQuery = true)
    int existsByEmailOrMobile(@Param("email") String email, @Param("mobileNo") String mobileNo);
    
    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    User findByEmail(@Param("email") String email);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET password = :newPassword WHERE email = :email", nativeQuery = true)
    int updatePassword(@Param("email") String email, @Param("newPassword") String newPassword);
    
 // Find User by ID
    @Query(value = "SELECT * FROM users WHERE id = :userId", nativeQuery = true)
    User findByUserId(@Param("userId") Long userId);

    // Delete User by ID
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM users WHERE id = :userId", nativeQuery = true)
    int deleteUserById(@Param("userId") Long userId);
}