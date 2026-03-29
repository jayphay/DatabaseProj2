/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.csx370.models;

/**
 * Extends the User class to include a following status,
 * indicating whether the current session user follows this user.
 */
public class FollowableUser extends User {
    private final boolean isFollowed;
    private final String lastActiveDate;

    // Constructor 1 (with image)
    public FollowableUser(String userId, String firstName, String lastName, String profileImageName,
                          boolean isFollowed, String lastActiveDate) {
        super(userId, firstName, lastName, profileImageName);
        this.isFollowed = isFollowed;

        // PLACE IT HERE: Handle null dates from the database
        this.lastActiveDate = (lastActiveDate != null) ? lastActiveDate : "Never";
    }

    public FollowableUser(String userId, String firstName, String lastName,
                          boolean isFollowed, String lastActiveDate) {
        super(userId, firstName, lastName);
        this.isFollowed = isFollowed;

        // PLACE IT HERE: Handle null dates from the database
        this.lastActiveDate = (lastActiveDate != null) ? lastActiveDate : "Never";
    }

    public boolean isFollowed() { return isFollowed; }

    public String getLastActiveDate() { return lastActiveDate; }
}
