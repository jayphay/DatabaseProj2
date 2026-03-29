ugrd 18

Shachin Chakraborty: Created Bookmarkibility and implemented the bookmark Page.
Implemented hashtag search feature, and created user search UI and implemented it.

Jack Phalen: Created likability, home page, expanded post page. Also made the entity 
diagram and created GitHub repository to allow for easier collaboration.

James Shanks:
Shresta Jha:

Grant Moore: Worked on the comment functionality, added comments to all of the SQL Queries,
made the final video. Tweaked the like funcationality to make it live update. Added data as SQL Insertion statements.

New Feature: User search is a search bar in addition to the hashtag search bar which
allows for the individual to search up everyone that contains the inputted sequence in their username.
It is accessed via the top bar next to the hashtag search.

    UI: top_bar.mustache
    controller: UserSearchController
    service: PeopleService

How to run:
1. Start the MySQL docker container and get the mysql prompt.

2. Create the database and the user table using the given database_setup.sql file. This is needed only if this is the first time running the web app.
    i.e. docker exec -i my-mysql mysql -u root -pmysqlpass < database_setup.sql

3. Navigate to the directory with the pom.xml using the terminal in your local machine and run the following command:
    On unix like machines:
        mvn spring-boot:run -Dspring-boot.run.jvmArguments='-Dserver.port=8081'
    On windows command line:
        mvn spring-boot:run -D"spring-boot.run.arguments=--server.port=8081"
    On windows power shell:
        mvn spring-boot:run --% -Dspring-boot.run.arguments="--server.port=8081"

4. Open the browser and navigate to the following URL:
    http://localhost:8081/

5. Create an account and login.