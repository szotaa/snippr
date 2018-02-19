package pl.szotaa.snippr.snippet.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import pl.szotaa.snippr.snippet.domain.Snippet;
import pl.szotaa.snippr.snippet.exception.SnippetExpiredException;
import pl.szotaa.snippr.snippet.exception.SnippetNotFoundException;
import pl.szotaa.snippr.snippet.repostiory.SnippetRepository;
import pl.szotaa.snippr.user.service.ApplicationUserService;

import java.time.Instant;

@RunWith(SpringRunner.class)
public class SnippetServiceTest {

    @InjectMocks
    private SnippetService snippetService;

    @Mock
    private SnippetRepository snippetRepository;

    @Mock
    private ApplicationUserService applicationUserService;

    private Snippet snippet;

    @Before
    public void init(){
        snippet = Snippet.builder()
                .title("example title")
                .content("example content")
                .build();
    }

    @Test
    @WithAnonymousUser
    public void save_AnonymousUser_SnippetGetsSaved() throws Exception {
        snippetService.save(snippet);
        Mockito.verify(applicationUserService, Mockito.never()).getByUsername(Mockito.anyString());
        Mockito.verify(snippetRepository, Mockito.times(1)).save(snippet);
    }

    @Test
    @WithMockUser
    public void save_Authenticated_SnippetGetsSaved() throws Exception {
        snippetService.save(snippet);
        Mockito.verify(applicationUserService, Mockito.times(1)).getByUsername(Mockito.anyString());
        Mockito.verify(snippetRepository, Mockito.times(1)).save(snippet);
    }

    @Test
    public void getById_ExistingId_SnippetGetsReturned() throws Exception {
        Mockito.when(snippetRepository.findOne(Mockito.anyLong())).thenReturn(snippet);
        Snippet found = snippetService.getById(1L);
        Assert.assertEquals(snippet, found);
    }

    @Test(expected = SnippetNotFoundException.class)
    public void getById_NonExistentId_SnippetNotFoundExceptionThrown() throws Exception {
        Mockito.when(snippetRepository.findOne(Mockito.anyLong())).thenReturn(null);
        snippetService.getById(1L);
    }

    @Test(expected = SnippetExpiredException.class)
    public void getById_ExistentIdExpiredSnippet_SnippetExpiredException() throws Exception {
        snippet.setExpiryDate(Instant.now().minusSeconds(1));
        Mockito.when(snippetRepository.findOne(Mockito.anyLong())).thenReturn(snippet);
        snippetService.getById(1L);
    }

    @Test
    public void update_ExistentSnippet_SnippetGetsSaved() throws Exception {
        snippet.setId(1L);
        Mockito.when(snippetRepository.exists(Mockito.anyLong())).thenReturn(true);
        snippetService.update(snippet);
        Mockito.verify(snippetRepository, Mockito.times(1)).save(snippet);
    }

    @Test(expected = SnippetNotFoundException.class)
    public void update_NonExistentSnippet_SnippetNotFoundExceptionThrown() throws Exception {
        snippet.setId(1L);
        Mockito.when(snippetRepository.exists(Mockito.anyLong())).thenReturn(false);
        snippetService.update(snippet);
        Mockito.verify(snippetRepository, Mockito.never()).save(snippet);
    }

    @Test
    public void delete_ExistentSnippet_SnippetGetsSaved() throws Exception {
        Mockito.when(snippetRepository.exists(Mockito.anyLong())).thenReturn(true);
        snippetService.delete(1L);
        Mockito.verify(snippetRepository, Mockito.times(1)).delete(1L);
    }

    @Test(expected = SnippetNotFoundException.class)
    public void delete_NonExistentSnippet_SnippetNotFoundExceptionThrown() throws Exception {
        Mockito.when(snippetRepository.exists(Mockito.anyLong())).thenReturn(false);
        snippetService.delete(1L);
        Mockito.verify(snippetRepository, Mockito.never()).delete(1L);
    }
}