package com.SAMProject.CarSharing.dao;


import com.SAMProject.CarSharing.Entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRepository {
    List<User> userList = new ArrayList<>();
    private int id;

    public User saveOrUpdate(User user) {
        if (user.getId() == 0) {
            user.setId(++id);
            userList.add(user);
            return user;
        } else {
            int index = findIndexById(user.getId());
            if (index != -1) {
                userList.set(index, user);
                return user;
            }
            return null;

        }
    }

    public int findIndexById(int id) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    public User findById(int id) {
        for (User user : userList) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }


    public User findByUsername(String username) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public List<User> allUsers() {
        List<User> allUsers = new ArrayList<>(userList);
        return allUsers;
    }
}
