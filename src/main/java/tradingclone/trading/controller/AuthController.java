package tradingclone.trading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tradingclone.trading.config.JwtProvider;
import tradingclone.trading.modal.User;
import tradingclone.trading.repository.UserRepository;
import tradingclone.trading.response.AuthResponse;
import tradingclone.trading.service.CustomUserDetailsService;

@RestController
@RequestMapping({"/auth"})
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping({"/signup"})
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {

        User isEmailExist=userRepository.findByEmail(user.getEmail());

        if(isEmailExist!=null){
            throw new Exception("email is already exist with another account");
        }

        User newuser = new User();
        newuser.setEmail(user.getEmail());
        newuser.setPassword(user.getPassword());
        newuser.setFullName(user.getFullName());

        User savedUser = userRepository.save(newuser);

        Authentication auth=new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt= JwtProvider.generateToken(auth);
        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setStatus(true);
        authResponse.setMessage("register success");

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping({"/signin"})
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {

        String userName=user.getEmail();
        String password=user.getPassword();

        Authentication auth=authenticate(userName,password);

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt= JwtProvider.generateToken(auth);
        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setStatus(true);
        authResponse.setMessage("login successfull");

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    private Authentication authenticate(String userName, String password) {
        UserDetails userDetails=customUserDetailsService.loadUserByUsername(userName);

        if(userDetails==null){
            throw new BadCredentialsException("invalid username");
        }

        if(!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,userName,userDetails.getAuthorities());
    }

}
