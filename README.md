# Github analyst web application

### Description
This is demo restful app for analysis of GitHub repositories activities

### Implemented features
1. Search repo by name or description. - done
2. Committer (users) list for selected repo. - done
   - Getting list of unique committer for selected repo. 
3. User - commits count - 100 commits. - done
4. Commit - date - 100 commits. - done
5. Page bookmark-able ability. - not implemented

Caching and pagination have been implemented to decrease requests delay. 

### Using technology
- J2EE technology in Spring Boot implementation. 

### Running the app
#### Pre-requirement
 - This application using embedded Tomcat as Web server and by default use `8080` port.
   Please make sure that `8080` port is not used by other app in running host. 
 - `java 1.8` should be added in your host machine PATH.
#### Build and run
1. There is `git-hub-analyst/src/main/resources/static` dir. Spring boot recognizes it as folder for
   static resources like `.html, .js, img, .css` files and adds them to the build artifact as is. If you
   want to run front and back part of this app on the same server you should copy front-end files 
   from `git-hub-analyst-web-gui/dist/git-hub-analyst-web-gui` to here.  
2. To build and package app go to `git-hub-analyst` dir and run `mvnw package`.
3. To run app in `git-hub-analyst` dir execute command `java -jar target/demo-0.0.1-SNAPSHOT.jar`.
   App will start up on Tomcat server `8080` port.

#### Using
For the development purposes [API Documentation]("http://localhost:8080/swagger-ui.html") is available.
