/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.csx370.controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uga.menik.csx370.models.Post;
import uga.menik.csx370.services.HomeService;
import uga.menik.csx370.services.PostService;
import uga.menik.csx370.services.UserService;

/**
 * This controller handles the home page and some of it's sub URLs.
 */
@Controller
@RequestMapping
public class HomeController {
    // private final DataSource dataSource;
    private UserService userService;
    private HomeService homeService;
    private PostService postService;

    public HomeController(UserService userService, HomeService homeService, PostService postService) {
        this.userService = userService;
        this.homeService = homeService;
        this.postService = postService;
    }

    /**
     * This is the specific function that handles the root URL itself.
     * 
     * Note that this accepts a URL parameter called error.
     * The value to this parameter can be shown to the user as an error message.
     * See notes in HashtagSearchController.java regarding URL parameters.
     */
    @GetMapping
    public ModelAndView webpage(@RequestParam(name = "error", required = false) String error) {
        // See notes on ModelAndView in BookmarksController.java.
        ModelAndView mv = new ModelAndView("home_page");


        // gets posts from users the logged in user follows and sort with newest at top
        // also get the count of comments, likes
        String loggedInUserId = userService.getLoggedInUser().getUserId();
        try {
            List<Post> posts = homeService.getFollowingPosts(loggedInUserId);
            mv.addObject("posts", posts);
        } catch (SQLException e) {
            // Log the error or handle it appropriately
            e.printStackTrace();
            String message = "Failed to load users. Please try again.";
            mv.addObject("errorMessage", message);
        }
        // mv.addObject("posts", posts);

        // If an error occured, you can set the following property with the
        // error message to show the error message to the user.
        // An error message can be optionally specified with a url query parameter too.
        // String errorMessage = error;
        // mv.addObject("errorMessage", errorMessage);

        // Enable the following line if you want to show no content message.
        // Do that if your content list is empty.
        // mv.addObject("isNoContent", true);

        return mv;
    }

    /**
     * This function handles the /createpost URL.
     * This handles a post request that is going to be a form submission.
     * The form for this can be found in the home page. The form has a
     * input field with name = posttext. Note that the @RequestParam
     * annotation has the same name. This makes it possible to access the value
     * from the input from the form after it is submitted.
     */
    @PostMapping("/createpost")
    public String createPost(@RequestParam(name = "posttext") String postText) {
        System.out.println("User is creating post: " + postText);

        String trimmed = postText == null ? "" : postText.trim();
        if (trimmed.isEmpty()) {
            String message = URLEncoder.encode("Post content cannot be empty.",
                    StandardCharsets.UTF_8);
            return "redirect:/?error=" + message;
        }

        try {
            String loggedInUserId = userService.getLoggedInUser().getUserId();
            postService.createPost(loggedInUserId, trimmed);
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Redirect the user with an error message if there was an error.
        String message = URLEncoder.encode("Failed to create the post. Please try again.",
                StandardCharsets.UTF_8);
        return "redirect:/?error=" + message;
    }

}
