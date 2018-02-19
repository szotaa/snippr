//TODO: rework tests

package pl.szotaa.snippr.snippet.controller;

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
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.szotaa.snippr.snippet.domain.Snippet;
import pl.szotaa.snippr.snippet.service.SnippetService;

//TODO: fix this

@RunWith(SpringRunner.class)
@WebMvcTest(SnippetController.class)
public class SnippetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private SnippetController snippetController;

    @MockBean
    private SnippetService snippetService;

    private Snippet exampleSnippet;

    @Before
    public void init(){
        exampleSnippet = Snippet.builder()
                .id(1L)
                .title("Example Title")
                .content("Example content")
                .build();
    }

    @Test
    @WithAnonymousUser
    public void getById_AnonymousUser_200Success() throws Exception {
        Mockito.when(snippetService.getById(Mockito.anyLong())).thenReturn(exampleSnippet);
        mockMvc.perform(MockMvcRequestBuilders.get("/snippet/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Is.is("Example Title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Is.is("Example content")));

        Mockito.verify(snippetService, Mockito.times(1)).getById(1L);
    }

    @Test
    @WithAnonymousUser
    public void updateExisting_AnonymousUser_401Unauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/snippet/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(exampleSnippet)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        Mockito.verify(snippetService, Mockito.never()).update(exampleSnippet);
    }

    @Test
    @WithMockUser
    public void updateExisting_Authenticated_200Ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/snippet/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(exampleSnippet)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(snippetService, Mockito.times(1)).update(exampleSnippet);
    }

    @Test
    @WithAnonymousUser
    public void deleteExisting_AnonymousUser_401Unauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/snippet/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        Mockito.verify(snippetService, Mockito.never()).delete(1L);
    }

    @Test
    @WithMockUser
    public void deleteExisting_Authenticated_200Ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/snippet/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(snippetService, Mockito.times(1)).delete(1L);
    }

    private static String asJsonString(Object object) {
        try{
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (Exception e){
            throw new RuntimeException();
        }
    }

}