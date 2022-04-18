package com.jacob.ui.auth;

import com.jacob.database.game_data.PastGame;
import com.jacob.database.user.User;
import com.jacob.database.user.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserAuthState {
    private User user;
    private final UserService userService;

    public UserAuthState(UserService userService) {
        this.userService = userService;
    }

    public void loginUser(User user) {
        this.user = user;
    }

    public User getLoggedInUser() {
        return user;
    }

    public void updateUserDetails(PastGame pastGame) {
        user.getPastGamesPlayed().add(pastGame);
        user = userService.saveUser(user);
    }
}
