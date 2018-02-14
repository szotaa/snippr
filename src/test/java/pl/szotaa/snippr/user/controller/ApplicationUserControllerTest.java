package pl.szotaa.snippr.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.szotaa.snippr.user.domain.ApplicationUser;
import pl.szotaa.snippr.user.service.ApplicationUserService;

@WithMockUser
@RunWith(SpringRunner.class)
@WebMvcTest(ApplicationUserController.class)
public class ApplicationUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private ApplicationUserController userController;

    @MockBean
    private ApplicationUserService userService;

    private ApplicationUser exampleUser;

    @Before
    public void init(){
        exampleUser = ApplicationUser.builder()
                .id(1L)
                .username("example_user")
                .build();
    }

    @Test
    public void signUpSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(exampleUser)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    //TODO: add remaining tests

    private static String asJsonString(final Object object) {
        try{
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (Exception e){
            throw new RuntimeException();
        }
    }

}