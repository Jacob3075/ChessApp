package com.jacob.ui.auth;

import com.jacob.database.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserAuthState {
    private User user;

    public void loginUser(User user) {
        this.user = user;
    }

    public User getLoggedInUser() {
        return user;
    }
}
