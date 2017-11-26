package de.uniReddit.uniReddit.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniReddit.uniReddit.Models.*;
import de.uniReddit.uniReddit.Repositories.*;
import de.uniReddit.uniReddit.UniRedditApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Sokol on 28.09.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UniRedditApplication.class)
@WebAppConfiguration
public class UniversityControllerTest{
    private MockMvc mockMvc;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private UniSubjectRepository uniSubjectRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private PostContentRepository postContentRepository;

    static private User user;
    static private University uni;
    static private PostContent content;
    static private UniSubject uniSubject;
    static private UniThread thread;
    private String email = "s.makolli@aol.de";
    private String username = "sokol";
    private String password = "password";
    private static boolean setUpIsDone = false;


    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() {
        if (setUpIsDone) {
            return;
        }

        uni = new University();
        uni.setLocation("Stuttgart");
        uni.setName("Uni Stuttgart");
        content = new PostContent("Hallo");
        uniSubject = new UniSubject(uni);
        user = new User.UserBuilder().email(email).username(username).password(password).university(uni).build();
        thread = new UniThread(content.getId(),user, "Test",  uniSubject);
        this.universityRepository.save(uni);
        this.uniSubjectRepository.save(uniSubject);
        this.userRepository.save(user);
        user.subscribe(uniSubject);
        this.userRepository.save(user);
        this.postContentRepository.save(content);
        this.threadRepository.save(thread);
        setUpIsDone = true;
    }

    @Test
    public void getUniversities(){

    }
}
