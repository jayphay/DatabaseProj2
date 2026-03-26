package uga.menik.csx370.controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uga.menik.csx370.models.FollowableUser;
import uga.menik.csx370.services.PeopleService;
import uga.menik.csx370.services.UserService;

/**
 * Handles /hashtagsearch URL and possibly others.
 * At this point no other URLs.
 */
@Controller
@RequestMapping("/usersearch")
public class UserSearchController {

    private final PeopleService peopleService;
    private final UserService userService;

    @Autowired
    public UserSearchController(PeopleService peopleService, UserService userService) {
        this.peopleService = peopleService;
        this.userService = userService;
    }

    /**
     * This function handles the /hashtagsearch URL itself.
     * This URL can process a request parameter with name hashtags.
     * In the browser the URL will look something like below:
     * http://localhost:8081/hashtagsearch?hashtags=%23amazing+%23fireworks
     * Note: the value of the hashtags is URL encoded.
     */
    @GetMapping
    public ModelAndView webpage(@RequestParam(name = "users") String users) {
        System.out.println("User is searching: " + users);

        ModelAndView mv = new ModelAndView("people_page");
        try {
            String loggedInUserId = userService.getLoggedInUser().getUserId();
            List<FollowableUser> found = peopleService.searchUsers(users, loggedInUserId);
            mv.addObject("users", found);
            if (found.isEmpty()) {
                mv.addObject("isNoContent", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String message = URLEncoder.encode("Failed to search users. Please try again.", StandardCharsets.UTF_8);
            mv.addObject("errorMessage", message);
        }
        return mv;
    }

}