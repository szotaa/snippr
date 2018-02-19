package pl.szotaa.snippr.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
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
    public void create_201Created() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(exampleUser)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Mockito.verify(userService, Mockito.times(1)).save(Mockito.any(ApplicationUser.class));
    }

    @Test
    public void getById_200Ok() throws Exception {
        Mockito.when(userService.getById(Mockito.anyLong())).thenReturn(exampleUser);
        mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Is.is("example_user")));

                //TODO: more jsonPath assertions

        Mockito.verify(userService, Mockito.times(1)).getById(Mockito.anyLong());
    }

    @Test
    public void updateExisting_200Ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(exampleUser)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(userService, Mockito.times(1)).update(Mockito.any(ApplicationUser.class));
    }

    @Test
    public void deleteExisting_200Ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(userService, Mockito.times(1)).delete(Mockito.anyLong());
    }

    private static String asJsonString(final Object object) {
        try{
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (Exception e){
            throw new RuntimeException();
        }
    }

}