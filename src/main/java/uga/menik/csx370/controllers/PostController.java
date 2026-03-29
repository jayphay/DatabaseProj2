/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.csx370.controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import uga.menik.csx370.models.ExpandedPost;
import uga.menik.csx370.models.User;
import uga.menik.csx370.services.BookmarkService;
import uga.menik.csx370.services.PostService;
import uga.menik.csx370.services.LikeService;
import uga.menik.csx370.services.PeopleService;
import uga.menik.csx370.services.UserService;
import uga.menik.csx370.utility.Utility;

/**
 * Handles /post URL and its sub urls.
 */
@Controller
@RequestMapping("/post")
public class PostController {
    @Autowired
    private UserService userService;

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private PostService postService;


    /**
     * This function handles the /post/{postId} URL.
     * This handlers serves the web page for a specific post.
     * Note there is a path variable {postId}.
     * An example URL handled by this function looks like below:
     * http://localhost:8081/post/1
     * The above URL assigns 1 to postId.
     * 
     * See notes from HomeController.java regardig error URL parameter.
     */
    @GetMapping("/{postId}")
    public ModelAndView webpage(@PathVariable("postId") String postId,
            @RequestParam(name = "error", required = false) String error) {
        System.out.println("The user is attempting to view post with id: " + postId);
        // See notes on ModelAndView in BookmarksController.java.
        ModelAndView mv = new ModelAndView("posts_page");

        // Following line populates sample data.
        // You should replace it with actual data from the database.
        
        String loggedInUserId = userService.getLoggedInUser().getUserId();
        try {
            ExpandedPost post = postService.getExpandedPost(loggedInUserId, postId);
            mv.addObject("posts", List.of(post));
        } catch (SQLException e) {
            // Log the error or handle it appropriately
            e.printStackTrace();
            String message = "Failed to load users. Please try again.";
            mv.addObject("errorMessage", message);
        }

        // Enable the following line if you want to show no content message.
        // Do that if your content list is empty.
        // mv.addObject("isNoContent", true);

        return mv;
    }

    /**
     * Handles comments added on posts.
     * See comments on webpage function to see how path variables work here.
     * This function handles form posts.
     * See comments in HomeController.java regarding form submissions.
     */
    @PostMapping("/{postId}/comment")
    public String postComment(@PathVariable("postId") int postId,
            @RequestParam(name = "comment") String comment) {
        System.out.println("The user is attempting add a comment:");
        System.out.println("\tpostId: " + postId);
        System.out.println("\tcomment: " + comment);

        // Redirect the user if the comment adding is a success.
        // return "redirect:/post/" + postId;
        
        try {
            User currentUser = userService.getLoggedInUser();

            int userId = Integer.parseInt(currentUser.getUserId());

            postService.addComment(userId, postId, comment);

            // Redirect the user if the comment successfully added.
            return "redirect:/post/" + postId ;

        } catch (Exception e) {
            e.printStackTrace();
            // Redirect the user with an error message if there was an error.
            String message = URLEncoder.encode("Failed to post the comment. Please try again.",
                    StandardCharsets.UTF_8);
            return "redirect:/post/" + postId + "?error=" + message;
        }        
    }

    /**
     * Handles likes added on posts.
     * See comments on webpage function to see how path variables work here.
     * See comments in PeopleController.java in followUnfollowUser function regarding 
     * get type form submissions and how path variables work.
     */
    @GetMapping("/{postId}/heart/{isAdd}")
    public ResponseEntity<Void> addOrRemoveHeart(@PathVariable("postId") int postId,
            @PathVariable("isAdd") Boolean isAdd) {
        try {
            // 1. Manually get the userId or use a fixed ID for testing
            // Example: If you still want the current user but don't want to "check" auth
            User currentUser = userService.getLoggedInUser();

            // If no one is logged in, you can default to a 'Guest' ID (e.g., 1)
            int userId = (currentUser != null) ? Integer.parseInt(currentUser.getUserId()) : 1;

            // 2. Perform the database action
            if (isAdd) {
                likeService.addLike(userId, postId);
            } else {
                likeService.removeLike(userId, postId);
            }

            // 3. Return 200 OK to the AJAX script
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Handles bookmarking posts.
     * See comments on webpage function to see how path variables work here.
     * See comments in PeopleController.java in followUnfollowUser function regarding 
     * get type form submissions.
     */
    @GetMapping("/{postId}/bookmark/{isAdd}")
    @ResponseBody
    public ResponseEntity<Void> addOrRemoveBookmark(@PathVariable("postId") int postId,
                                                    @PathVariable("isAdd") boolean isAdd) {
        try {
            // 1. Manually get the userId or use a fixed ID for testing
            // Example: If you still want the current user but don't want to "check" auth
            User currentUser = userService.getLoggedInUser();

            // If no one is logged in, you can default to a 'Guest' ID (e.g., 1)
            int userId = (currentUser != null) ? Integer.parseInt(currentUser.getUserId()) : 1;

            // 2. Perform the database action
            if (isAdd) {
                bookmarkService.addBookmark(userId, postId);
            } else {
                bookmarkService.removeBookmark(userId, postId);
            }

            // 3. Return 200 OK to the AJAX script
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

}
