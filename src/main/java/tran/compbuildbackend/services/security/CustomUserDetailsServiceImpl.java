package tran.compbuildbackend.services.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tran.compbuildbackend.domain.user.ApplicationUser;
import tran.compbuildbackend.repositories.users.ApplicationUserRepository;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private ApplicationUserRepository applicationUserRepository;
    private final String USER_NOT_FOUND_MESSAGE = "User not found";

    @Autowired
    public CustomUserDetailsServiceImpl(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
        /*
         * the below occurs if a user tries to log in a username not yet registered.
         */
        if(applicationUser == null) throw new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE);


        return applicationUser;
    }

    @Transactional
    public ApplicationUser loadApplicationUserById(Long id) {
        ApplicationUser applicationUser = applicationUserRepository.getById(id);

        if(applicationUser == null) throw new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE);

        return applicationUser;
    }
}
