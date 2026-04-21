package com.Mandal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        // 1. Validation Logic
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new RuntimeException("Invalid email format");
        }

        // 2. Duplicate Check
        int count = userRepository.existsByEmailOrMobile(user.getEmail(), user.getMobileNo());
        if (count > 0) {
            throw new UserAlreadyExistsException("User with this email or mobile number already exists!");
        }

        // 3. Insert
        userRepository.registerUser(
            user.getName(), user.getAddress(), user.getMobileNo(), 
            user.getEmail(), user.getPassword(), user.getDob()
        );
    }
    public Page<User> getAllUsers(int page, int size) {
        return userRepository.findAllUsers(PageRequest.of(page, size));
    }
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        
        // Case 1: Email not found
        if (user == null) {
            throw new UnauthorizedException("Invalid email or password");
        }
        
        // Case 2: Password mismatch
        if (!user.getPassword().equals(password)) {
            throw new UnauthorizedException("Invalid email or password");
        }
        
        return user;
    }

    public void forgotPassword(String email, String newPassword) {
        if (newPassword == null) {
            throw new RuntimeException("Password cannot be null");
        }
        
        int updatedRows = userRepository.updatePassword(email, newPassword);
        
        if (updatedRows == 0) {
            throw new UserAlreadyExistsException("No account found with email: " + email);
        }
    }
    
 // Get User by ID logic
    public User getUserById(Long id) {
        User user = userRepository.findByUserId(id);
        if (user == null) {
            throw new UserAlreadyExistsException("User with ID " + id + " not found");
        }
        return user;
    }

    // Delete User logic
    public void deleteUser(Long id) {
        int deletedRows = userRepository.deleteUserById(id);
        if (deletedRows == 0) {
            throw new UserAlreadyExistsException("Cannot delete. User with ID " + id + " does not exist");
        }
    }
    
}