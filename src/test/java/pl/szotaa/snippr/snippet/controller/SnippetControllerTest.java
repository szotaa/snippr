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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.szotaa.snippr.snippet.domain.Snippet;
import pl.szotaa.snippr.snippet.service.SnippetService;

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
    public void createTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/snippet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(exampleSnippet)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void getByIdSuccessTest() throws Exception {
        Mockito.when(snippetService.getById(Mockito.anyLong())).thenReturn(exampleSnippet);

        mockMvc.perform(MockMvcRequestBuilders.get("/snippet/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Is.is("Example Title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Is.is("Example content")));
    }

    @Test
    public void getByIdFailureTest() throws Exception {
        Mockito.when(snippetService.getById(Mockito.anyLong())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/snippet/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updateSuccessTest() throws Exception {
        Mockito.when(snippetService.getById(Mockito.anyLong())).thenReturn(exampleSnippet);

        mockMvc.perform(MockMvcRequestBuilders.put("/snippet/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(exampleSnippet)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateFailureTest() throws Exception {
        Mockito.when(snippetService.getById(Mockito.anyLong())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/snippet/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(exampleSnippet)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteSuccessTest() throws Exception {
        Mockito.when(snippetService.getById(Mockito.anyLong())).thenReturn(exampleSnippet);

        mockMvc.perform(MockMvcRequestBuilders.put("/snippet/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(exampleSnippet)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteFailureTest() throws Exception {
        Mockito.when(snippetService.getById(Mockito.anyLong())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/snippet/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(exampleSnippet)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
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