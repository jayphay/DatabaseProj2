/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/

// Make sure the dom is loaded.
document.addEventListener('DOMContentLoaded', function () {
    // submittable is expected to be text fields.
    var submittables = document.getElementsByClassName('submittable');

    for (var submittable of submittables) {
        // Make text field submit the enclosing form when the enter key is pressed.
        submittable.addEventListener('keydown', function (e) {
            // Check if Enter was pressed without the Shift key
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault(); // Prevent new lines.
                this.form.submit(); // Submit the form.
                console.log(this.form + ' was submitted.');
            }
        });
    }
});

// AJAX logic to handle Bookmark and Like toggles without page refresh.
document.addEventListener('DOMContentLoaded', () => {
    // Select all bookmark and heart buttons
    const actionButtons = document.querySelectorAll('.bookmark-btn, .heart-btn');

    actionButtons.forEach(btn => {
        // Remove any existing click event to avoid double binding
        btn.replaceWith(btn.cloneNode(true));
    });

    // Re-select after cloning
    const freshButtons = document.querySelectorAll('.bookmark-btn, .heart-btn');
    freshButtons.forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault(); // Stop standard navigation

            const postId = this.getAttribute('data-postid');
            const isAdd = this.getAttribute('data-isadd');
            // Determine if we are calling /bookmark/ or /heart/
            const actionType = this.classList.contains('bookmark-btn') ? 'bookmark' : 'heart';

            // Send request to your Java Controller in the background
            fetch(`/post/${postId}/${actionType}/${isAdd}`)
                .then(response => {
                    if (response.ok) {
                        // 1. Toggle visual state (Solid vs Outline)
                        this.classList.toggle('fa');
                        this.classList.toggle('far');

                        // 2. Flip the state for the next click
                        const nextState = (isAdd === 'true') ? 'false' : 'true';
                        this.setAttribute('data-isadd', nextState);

                        // 3. Update the like count in the DOM
                        if (actionType === 'heart') {
                            const countSpan = document.getElementById(`hearts-count-${postId}`);
                            if (countSpan) {
                                let count = parseInt(countSpan.textContent, 10) || 0;
                                count = (isAdd === 'true') ? count + 1 : count - 1;
                                countSpan.textContent = count;
                            }
                        }
                    } else {
                        alert("Action failed. Are you logged in?");
                    }
                })
                .catch(err => console.error("Request failed:", err));
        });
    });
});
