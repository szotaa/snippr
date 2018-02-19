package pl.szotaa.snippr.user.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import pl.szotaa.snippr.user.domain.ApplicationUser;
import pl.szotaa.snippr.user.domain.Role;
import pl.szotaa.snippr.user.repository.ApplicationUserRepository;
import pl.szotaa.snippr.user.repository.RoleRepository;

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
                .build();
    }

    @Test
    public void save_UsernameNotExistent_UserSaved() throws Exception {
        Mockito.when(applicationUserRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        Mockito.when(roleRepository.findOne(Mockito.anyLong())).thenReturn(exampleRole);
        Mockito.when(bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn("ENCODED_PASSWORD");

        applicationUserService.save(applicationUser);

        Mockito.verify(bCryptPasswordEncoder, Mockito.times(1)).encode(Mockito.anyString());
        Mockito.verify(roleRepository, Mockito.times(1)).findOne(Mockito.anyLong());
        Mockito.verify(applicationUserRepository, Mockito.times(1)).save(Mockito.any(ApplicationUser.class));

    }
    //TODO: add remaining tests
}