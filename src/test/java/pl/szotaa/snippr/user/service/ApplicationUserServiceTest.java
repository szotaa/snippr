package pl.szotaa.snippr.user.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import pl.szotaa.snippr.user.domain.ApplicationUser;
import pl.szotaa.snippr.user.domain.Role;
import pl.szotaa.snippr.user.exception.ApplicationUserAlreadyExistsException;
import pl.szotaa.snippr.user.exception.ApplicationUserNotFoundException;
import pl.szotaa.snippr.user.repository.ApplicationUserRepository;
import pl.szotaa.snippr.user.repository.RoleRepository;

import java.util.Collections;
import java.util.HashSet;

@RunWith(SpringRunner.class)
public class ApplicationUserServiceTest {

    @InjectMocks
    private ApplicationUserService applicationUserService;

    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private Role exampleRole;
    private ApplicationUser applicationUser;

    @Before
    public void init(){
        exampleRole = new Role();
        exampleRole.setId(2L);
        exampleRole.setRoleName("ROLE_USER");

        applicationUser = ApplicationUser.builder()
                .id(1L)
                .username("example_username")
                .password("example_password")
                .roles(new HashSet<>(Collections.singleton(exampleRole)))
                .build();
    }

    @Test
    public void loadByUsername_UsernameExistent_UserReturned() throws Exception {
        Mockito.when(applicationUserRepository.findByUsername(Mockito.anyString())).thenReturn(applicationUser);

        User expected = new User(applicationUser.getUsername(), applicationUser.getPassword(), applicationUser.getRoles());
        User actual = (User) applicationUserService.loadUserByUsername("example_username");

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadByUsername_UsernameNonExistent_UsernameNotFoundExceptionThrown() throws Exception {
        Mockito.when(applicationUserRepository.findByUsername(Mockito.anyString())).thenReturn(null);
        applicationUserService.loadUserByUsername("example_username");
    }

    @Test
    public void save_UsernameNotExistent_ApplicationUserSaved() throws Exception {
        Mockito.when(applicationUserRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        Mockito.when(roleRepository.findOne(Mockito.anyLong())).thenReturn(exampleRole);
        Mockito.when(bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn("ENCODED_PASSWORD");

        applicationUserService.save(applicationUser);

        Mockito.verify(bCryptPasswordEncoder, Mockito.times(1)).encode(Mockito.anyString());
        Mockito.verify(roleRepository, Mockito.times(1)).findOne(Mockito.anyLong());
        Mockito.verify(applicationUserRepository, Mockito.times(1)).save(Mockito.any(ApplicationUser.class));

    }


    @Test(expected = ApplicationUserAlreadyExistsException.class)
    public void save_UsernameExistent_ApplicationUserAlreadyExistsExceptionThrown() throws Exception {
        Mockito.when(applicationUserRepository.existsByUsername(Mockito.anyString())).thenReturn(true);

        applicationUserService.save(applicationUser);

        Mockito.verify(roleRepository, Mockito.never()).findOne(Mockito.anyLong());
        Mockito.verify(bCryptPasswordEncoder, Mockito.never()).encode(Mockito.anyString());
        Mockito.verify(applicationUserRepository, Mockito.never()).save(Mockito.any(ApplicationUser.class));
    }

    @Test
    public void getByUsername_UserExistent_ApplicationUserReturned() throws Exception {
        Mockito.when(applicationUserRepository.existsByUsername(Mockito.anyString())).thenReturn(true);
        Mockito.when(applicationUserRepository.findByUsername(Mockito.anyString())).thenReturn(applicationUser);

        ApplicationUser found = applicationUserService.getByUsername("example_username");

        Assert.assertEquals(applicationUser, found);

        Mockito.verify(applicationUserRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
    }

    @Test(expected = ApplicationUserNotFoundException.class)
    public void getByUsername_UserNonExistent_ApplicationUserNotFoundExceptionThrown() throws Exception {
        Mockito.when(applicationUserRepository.existsByUsername(Mockito.anyString())).thenReturn(false);

        applicationUserService.getByUsername("example_username");

        Mockito.verify(applicationUserRepository, Mockito.never()).findByUsername(Mockito.anyString());
    }

    @Test
    public void getById_UserExistent_ApplicationUserReturned() throws Exception {
        Mockito.when(applicationUserRepository.exists(Mockito.anyLong())).thenReturn(true);
        Mockito.when(applicationUserRepository.findOne(Mockito.anyLong())).thenReturn(applicationUser);

        ApplicationUser found = applicationUserService.getById(1L);

        Assert.assertEquals(applicationUser, found);

        Mockito.verify(applicationUserRepository, Mockito.times(1)).findOne(Mockito.anyLong());
    }

    @Test(expected = ApplicationUserNotFoundException.class)
    public void getById_UserNonExistent_ApplicationUserNotFoundExceptionThrown() throws Exception {
        Mockito.when(applicationUserRepository.exists(Mockito.anyLong())).thenReturn(false);

        applicationUserService.getById(1L);

        Mockito.verify(applicationUserRepository, Mockito.never()).findOne(Mockito.anyLong());
    }

    @Test
    public void update_UserExistent_ApplicationUserUpdated() throws Exception {
        Mockito.when(applicationUserRepository.exists(Mockito.anyLong())).thenReturn(true);

        applicationUserService.update(applicationUser);

        Mockito.verify(applicationUserRepository, Mockito.times(1)).save(Mockito.any(ApplicationUser.class));
    }

    @Test(expected = ApplicationUserNotFoundException.class)
    public void update_UserNonExistent_ApplicationUserNotFoundExceptionThrown() throws Exception {
        Mockito.when(applicationUserRepository.exists(Mockito.anyLong())).thenReturn(false);

        applicationUserService.update(applicationUser);

        Mockito.verify(applicationUserRepository, Mockito.never()).save(Mockito.any(ApplicationUser.class));
    }

    @Test
    public void delete_UserExistent_ApplicationUserDeleted() throws Exception {
        Mockito.when(applicationUserRepository.exists(Mockito.anyLong())).thenReturn(true);

        applicationUserService.delete(1L);

        Mockito.verify(applicationUserRepository, Mockito.times(1)).delete(Mockito.anyLong());
    }

    @Test(expected = ApplicationUserNotFoundException.class)
    public void delete_UserNonExistent_ApplicationUserNotFoundExceptionThrown() throws Exception {
        Mockito.when(applicationUserRepository.exists(Mockito.anyLong())).thenReturn(false);

        applicationUserService.delete(1L);

        Mockito.verify(applicationUserRepository, Mockito.never()).delete(Mockito.anyLong());
    }
}
