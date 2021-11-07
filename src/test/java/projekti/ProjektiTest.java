package projekti;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProjektiTest extends org.fluentlenium.adapter.junit.FluentTest {

    @LocalServerPort
    private Integer port;

    @Before
    public void register() {
        goTo("http://localhost:" + port + "/registration");

        //Fill regform
        find("#name").fill().with("Teemu Testaaja2");
        find("#username").fill().with("temppu2");
        find("#password").fill().with("salasana123");
        find("form").first().submit();
    }

    public void login() {
        //Fill log in form
        goTo("http://localhost:" + port + "/login");
        find("#username").fill().with("temppu2");
        find("#password").fill().with("salasana123");
        find("form").first().submit();
    }

    public void registerFriend() {
        goTo("http://localhost:" + port + "/registration");

        //Fill regform
        find("#name").fill().with("Friend");
        find("#username").fill().with("friend1");
        find("#password").fill().with("salasana123");
        find("form").first().submit();
    }

    @Test
    public void ableToSignUpAndLogIn() {

        goTo("http://localhost:" + port + "/registration");

        //Fill regform
        find("#name").fill().with("Teemu Testaaja");
        find("#username").fill().with("temppu");
        find("#password").fill().with("salasana123");
        find("form").first().submit();

        //Check we are in login page
        assertTrue(pageSource().contains("Please sign in"));

        //Fill log in form
        find("#username").fill().with("temppu");
        find("#password").fill().with("salasana123");
        find("form").first().submit();

        //Check we are in
        assertTrue(pageSource().contains("My wall"));

    }

    @Test
    public void ableToSeeMyWall() {
        goTo("http://localhost:" + port + "/profile");
        if (!pageSource().contains("People you follow:")) {
            this.login();
            goTo("http://localhost:" + port + "/profile");
        }
        assertTrue(pageSource().contains("People you follow:"));
    }

    @Test
    public void addingImageErrorReturnsToMyWall() {
        goTo("http://localhost:" + port + "/profile");
        if (!pageSource().contains("People you follow:")) {
            this.login();
            goTo("http://localhost:" + port + "/profile");
        }
        assertTrue(pageSource().contains("People you follow:"));
        //Submit empty imageform
        find("#imageform").submit();
        //Check we are on My wall page
        assertTrue(pageSource().contains("People you follow:"));
    }

    @Test
    public void seekFindsMe() {
        goTo("http://localhost:" + port + "/seek");
        if (!pageSource().contains("Seek users")) {
            this.login();
            goTo("http://localhost:" + port + "/seek");
        }
        assertTrue(pageSource().contains("Seek users"));
        find("#word").fill().with("Te");
        find("#seekform").submit();
        assertTrue(pageSource().contains("Teemu Testaaja2"));
    }

    @Test
    public void ableToSendMessage() {
        goTo("http://localhost:" + port + "/profile");
        if (!pageSource().contains("People you follow:")) {
            this.login();
            goTo("http://localhost:" + port + "/profile");
        }
        assertTrue(pageSource().contains("People you follow:"));
        //Submit a message
        find("#msgtext").fill().with("Testiviesti!");
        find("#messageform").submit();
        //Check we are still on My wall page
        assertTrue(pageSource().contains("People you follow:"));
        //And see the msg
        assertTrue(pageSource().contains("Testiviesti!"));
        //And we see it also in public profiles page
        goTo("http://localhost:" + port + "/profiles/temppu2");
        //Check we are on profiles page
        assertTrue(pageSource().contains("This is how your followers see your wall"));
        assertTrue(pageSource().contains("Testiviesti!"));
    }

    @Test
    public void followingChangesView() {
        this.registerFriend();
        goTo("http://localhost:" + port + "/profiles/friend1");
        if (!pageSource().contains("Friend's wall")) {
            this.login();
            goTo("http://localhost:" + port + "/profiles/friend1");
        }
        //We are on Friends page
        assertTrue(pageSource().contains("Friend's wall"));
        //We don't see the messages or pictures
        assertTrue(pageSource().contains("Start to follow to see the wall."));
        goTo("http://localhost:" + port + "/profiles/friend1/pics");
        assertTrue(pageSource().contains("Start to follow to see pictures."));
        //Start to follow
        goTo("http://localhost:" + port + "/profiles/friend1");
        //We are on Friends page
        assertTrue(pageSource().contains("Friend's wall"));
        //Start to follow
        find("#followform").submit();
        //See the wall
        assertFalse(pageSource().contains("Start to follow to see the wall."));
        assertTrue(pageSource().contains("No messages yet."));
        //See pictures
        goTo("http://localhost:" + port + "/profiles/friend1/pics");
        assertFalse(pageSource().contains("Start to follow to see pictures."));
        assertTrue(pageSource().contains("No pictures yet."));
    }

    @Test
    public void cannotFollowIfBlocked() {
        this.registerFriend();
        goTo("http://localhost:" + port + "/profiles/friend1");
        if (!pageSource().contains("Friend's wall")) {
            this.login();
            goTo("http://localhost:" + port + "/profiles/friend1");
        }
        //We are on Friends page
        assertTrue(pageSource().contains("Friend's wall"));
        //Block
        find("#blockform").submit();
        //We don't see the messages
        assertTrue(pageSource().contains("Start to follow to see the wall."));
        //logout
        find("#logoutform").submit();
        //log in Friend
        goTo("http://localhost:" + port + "/login");
        find("#username").fill().with("friend1");
        find("#password").fill().with("salasana123");
        find("form").first().submit();
        //see Teemu's page
        goTo("http://localhost:" + port + "/profiles/temppu2");
        assertTrue(pageSource().contains("Testaaja2's wall"));
        //We don't see the messages
        assertTrue(pageSource().contains("Start to follow to see the wall."));
        //Try to follow
        find("#followform").submit();
        //No success, we don't see the messages
        assertTrue(pageSource().contains("Start to follow to see the wall."));
        //logout
        find("#logoutform").submit();

        //Unblock
        this.login();
        goTo("http://localhost:" + port + "/profiles/friend1");
        assertTrue(pageSource().contains("Friend's wall"));
        find("#blockform").submit();
    }
}
