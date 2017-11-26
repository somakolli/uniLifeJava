package de.uniReddit.uniReddit.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniReddit.uniReddit.Models.*;
import de.uniReddit.uniReddit.Repositories.*;
import de.uniReddit.uniReddit.UniRedditApplication;
import org.junit.Assert;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;


import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Sokol on 25.09.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UniRedditApplication.class)
@WebAppConfiguration

public class UserControllerTest {
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
    private String email = "s.m@aol.de";
    private String username = "s";
    private String password = "p";
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
        uni.setLocation("Stuttgart1");
        uni.setName("Uni Stuttgart1");
        content = new PostContent("Hallo1");
        this.postContentRepository.save(content);
        uniSubject = new UniSubject(uni);
        uniSubject.setName("Architektur von Anwendungssysteme11");
        user = new User.UserBuilder().email(email).username(username).password(password).university(uni).build();
        thread = new UniThread(content.getId(),user, "Test",  uniSubject);
        this.universityRepository.save(uni);
        this.uniSubjectRepository.save(uniSubject);
        this.userRepository.save(user);
        user.subscribe(uniSubject);
        this.userRepository.save(user);
        this.threadRepository.save(thread);
        setUpIsDone = true;
    }

    @Test
    public void createUserTest() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", "sokol1");
        params.add("email", "s.makolli1@aol.de");
        params.add("universityId", "1");
        params.add("password", "password");
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        MvcResult response = mockMvc.perform(post("/api/users/sign-up").params(params)).
                andExpect(status().isCreated()).andReturn();
        Assert.assertEquals("s.makolli1@aol.de", userRepository.findByUsername("sokol1").getEmail());
    }

    @Test
    public void usernameAlreadyExists() throws Exception{
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        params.add("email", "s.makolli1@aol.de");
        params.add("universityId", "1");
        params.add("password", "password");
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        MvcResult response = mockMvc.perform(post("/api/users/sign-up").params(params)).
                andExpect(status().isConflict()).andReturn();
        Assert.assertEquals("username exists", response.getResponse().getContentAsString());
    }
    @Test
    public void emailAlreadyExists() throws Exception{
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", "sokol12");
        params.add("email", email);
        params.add("universityId", "1");
        params.add("password", "password");
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        MvcResult response = mockMvc.perform(post("/api/users/sign-up").params(params)).
                andExpect(status().isConflict()).andReturn();
        Assert.assertEquals("email exists", response.getResponse().getContentAsString());
    }

    @Test
    public void getTest() throws Exception{
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        MvcResult  result = mockMvc.perform(get("/api/users/sokol")).andExpect(status().isOk()).andReturn();
    }


    @Test
    public void subscribe() throws Exception{
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        String subscribe = "";
        MvcResult result = mockMvc.perform(get("/api/users/"+ subscribe + "subscribe?username=sokol&uniSubjectId=1")).
                andExpect(status().isAccepted()).andReturn();

        MvcResult result1 = mockMvc.perform(get("/api/users/"+ subscribe + "subscribe?username=sokol&uniSubjectId=3123")).
                andExpect(status().isNotFound()).andReturn();
        Assert.assertEquals("uniSubject not found", result1.getResponse().getContentAsString());
    }

    @Test
    public void unsubscribe() throws Exception{
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        String subscribe = "un";
        MvcResult result = mockMvc.perform(get("/api/users/"+ subscribe + "subscribe?username=sokol&uniSubjectId=1")).
                andExpect(status().isAccepted()).andReturn();

        MvcResult result1 = mockMvc.perform(get("/api/users/"+ subscribe + "subscribe?username=sokol&uniSubjectId=2234")).
                andExpect(status().isNotFound()).andReturn();
        Assert.assertEquals("uniSubject not found", result1.getResponse().getContentAsString());
    }

    @Test
    public void createUniversityTest() throws Exception{
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "Universität Stuttgart");
        params.add("location", "Stuttgart");
        mockMvc.perform(post("/api/universities/").params(params)).andExpect(status().isCreated());
    }
    @Test
    public void getUniversitiesTest() throws Exception{
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        MvcResult  result = mockMvc.perform(get("/api/universities")).andExpect(status().isOk()).andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void createSubjectTest() throws Exception{
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "AAS");
        params.add("universityId", "1");
        mockMvc.perform(post("/api/unisubjects/").params(params)).andExpect(status().isCreated());
    }

    @Test
    public void getSubjectsTest() throws Exception{
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        MvcResult  result = mockMvc.perform(get("/api/unisubjects?universityId=1")).andExpect(status().isOk()).andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }



    @Test
    public void createThreadTest() throws Exception{
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("title", "test");
        params.add("contentString", "testContent");
        params.add("subjectId", "1");
        params.add("authorId", "1");
        mockMvc.perform(post("/api/unithreads").params(params)).andExpect(status().isCreated());
        Long contentId = threadRepository.findOne((long)2).getContentId();
        Assert.assertTrue(contentId==2);
    }

    @Test
    public void getThreadTest() throws Exception{
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        MvcResult result = mockMvc.perform(get("/api/unithreads/1")).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void createCommentTest() throws Exception{
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("content", "Test");
        params.add("username", "s");
        params.add("parentId", "1");
        mockMvc.perform(post("/api/comments").params(params)).andExpect(status().isCreated());
    }

}
