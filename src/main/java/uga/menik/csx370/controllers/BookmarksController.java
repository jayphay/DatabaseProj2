/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.csx370.controllers;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uga.menik.csx370.models.Post;
import uga.menik.csx370.models.User;
import uga.menik.csx370.services.BookmarkService;
import uga.menik.csx370.services.PostService;
import uga.menik.csx370.services.UserService;
import uga.menik.csx370.utility.Utility;

/**
 * Handles /bookmarks and its sub URLs.
 * No other URLs at this point.
 * 
 * Learn more about @Controller here: 
 * https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller.html
 */
@Controller
@RequestMapping("/bookmarks")
public class BookmarksController {
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    /**
     * /bookmarks URL itself is handled by this.
     */
    @GetMapping
    public ModelAndView webpage() {
        ModelAndView mv = new ModelAndView("posts_page");

        // Get the current logged-in user
        User currentUser = userService.getLoggedInUser();
        if (currentUser == null) {
            return new ModelAndView("redirect:/login");
        }

        try {
            // Fetch real data from the database
            List<Post> posts = postService.getBookmarkedPosts(currentUser.getUserId());
            mv.addObject("posts", posts);

            // Show "no content" message if list is empty
            if (posts.isEmpty()) {
                mv.addObject("isNoContent", true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mv.addObject("errorMessage", "Failed to load bookmarks.");
        }

        return mv;
    }
    
}
