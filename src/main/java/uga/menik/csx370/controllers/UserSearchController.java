package uga.menik.csx370.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uga.menik.csx370.models.Post;
import uga.menik.csx370.utility.Utility;

/**
 * Handles /usersearch URL and possibly others.
 * At this point no other URLs.
 */
@Controller
@RequestMapping("/usersearch")
public class UserSearchController {

    /**
     * This function handles the /usersearch URL itself.
     * This URL can process a request parameter with name users.
     * In the browser the URL will look something like below:
     */
    @GetMapping
    public ModelAndView webpage(@RequestParam(name = "users") String users) {
        System.out.println("User is searching: " + users);

        // See notes on ModelAndView in BookmarksController.java.
        ModelAndView mv = new ModelAndView("posts_page");

        // Following line populates sample data.
        // You should replace it with actual data from the database.
        List<Post> posts = Utility.createSamplePostsListWithoutComments();
        mv.addObject("posts", posts);

        // If an error occured, you can set the following property with the
        // error message to show the error message to the user.
        // String errorMessage = "Some error occured!";
        // mv.addObject("errorMessage", errorMessage);

        // Enable the following line if you want to show no content message.
        // Do that if your content list is empty.
        // mv.addObject("isNoContent", true);

        return mv;
    }

}