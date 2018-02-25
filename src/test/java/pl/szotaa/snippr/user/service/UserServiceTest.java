package pl.szotaa.snippr.user.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import pl.szotaa.snippr.user.domain.User;
import pl.szotaa.snippr.user.domain.Role;
import pl.szotaa.snippr.user.exception.UserAlreadyExistsException;
import pl.szotaa.snippr.user.exception.UserNotFoundException;
import pl.szotaa.snippr.user.repository.UserRepository;
import pl.szotaa.snippr.user.repository.RoleRepository;

import java.util.Collections;
import java.util.HashSet;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private Role exampleRole;
    private User exampleUser;

    @Before
    public void init(){
        exampleRole = new Role();
        exampleRole.setId(2L);
        exampleRole.setRoleName("ROLE_USER");

        exampleUser = User.builder()
                .id(1L)
                .username("example_username")
                .password("example_password")
                .roles(new HashSet<>(Collections.singleton(exampleRole)))
                .build();
    }

    @Test
    public void loadByUsername_UsernameExistent_UserReturned() {
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(exampleUser);

        org.springframework.security.core.userdetails.User expected = new org.springframework.security.core.userdetails.User(exampleUser.getUsername(), exampleUser.getPassword(), exampleUser.getRoles());
        org.springframework.security.core.userdetails.User actual = (org.springframework.security.core.userdetails.User) userService.loadUserByUsername("example_username");

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadByUsername_UsernameNonExistent_UsernameNotFoundExceptionThrown() {
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(null);
        userService.loadUserByUsername("example_username");
    }

    @Test
    public void save_UsernameNotExistent_ApplicationUserSaved() throws Exception {
        Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        Mockito.when(roleRepository.findOne(Mockito.anyLong())).thenReturn(exampleRole);
        Mockito.when(bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn("ENCODED_PASSWORD");

        userService.save(exampleUser);

        Mockito.verify(bCryptPasswordEncoder, Mockito.times(1)).encode(Mockito.anyString());
        Mockito.verify(roleRepository, Mockito.times(1)).findOne(Mockito.anyLong());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));

    }


    @Test(expected = UserAlreadyExistsException.class)
    public void save_UsernameExistent_ApplicationUserAlreadyExistsExceptionThrown() throws Exception {
        Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(true);

        userService.save(exampleUser);

        Mockito.verify(roleRepository, Mockito.never()).findOne(Mockito.anyLong());
        Mockito.verify(bCryptPasswordEncoder, Mockito.never()).encode(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
    }

    @Test
    public void getByUsername_UserExistent_ApplicationUserReturned() throws Exception {
        Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(true);
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(exampleUser);

        User found = userService.getByUsername("example_username");

        Assert.assertEquals(exampleUser, found);

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
    }

    @Test(expected = UserNotFoundException.class)
    public void getByUsername_UserNonExistent_ApplicationUserNotFoundExceptionThrown() throws Exception {
        Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(false);

        userService.getByUsername("example_username");

        Mockito.verify(userRepository, Mockito.never()).findByUsername(Mockito.anyString());
    }

    @Test
    public void getById_UserExistent_ApplicationUserReturned() throws Exception {
        Mockito.when(userRepository.exists(Mockito.anyLong())).thenReturn(true);
        Mockito.when(userRepository.findOne(Mockito.anyLong())).thenReturn(exampleUser);

        User found = userService.getById(1L);

        Assert.assertEquals(exampleUser, found);

        Mockito.verify(userRepository, Mockito.times(1)).findOne(Mockito.anyLong());
    }

    @Test(expected = UserNotFoundException.class)
    public void getById_UserNonExistent_ApplicationUserNotFoundExceptionThrown() throws Exception {
        Mockito.when(userRepository.exists(Mockito.anyLong())).thenReturn(false);

        userService.getById(1L);

        Mockito.verify(userRepository, Mockito.never()).findOne(Mockito.anyLong());
    }

    @Test
    public void update_UserExistent_ApplicationUserUpdated() throws Exception {
        User updateData = User.builder()
                .id(1L)
                .username("updated_username")
                .password("updated_password")
                .build();

        Mockito.when(userRepository.findOne(Mockito.anyLong())).thenReturn(exampleUser);

        userService.update(updateData);

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test(expected = UserNotFoundException.class)
    public void update_UserNonExistent_ApplicationUserNotFoundExceptionThrown() throws Exception {
        Mockito.when(userRepository.exists(Mockito.anyLong())).thenReturn(false);

        userService.update(exampleUser);

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
    }

    @Test
    public void delete_UserExistent_ApplicationUserDeleted() throws Exception {
        Mockito.when(userRepository.exists(Mockito.anyLong())).thenReturn(true);

        userService.delete(1L);

        Mockito.verify(userRepository, Mockito.times(1)).delete(Mockito.anyLong());
    }

    @Test(expected = UserNotFoundException.class)
    public void delete_UserNonExistent_ApplicationUserNotFoundExceptionThrown() throws Exception {
        Mockito.when(userRepository.exists(Mockito.anyLong())).thenReturn(false);

        userService.delete(1L);

        Mockito.verify(userRepository, Mockito.never()).delete(Mockito.anyLong());
    }
}
