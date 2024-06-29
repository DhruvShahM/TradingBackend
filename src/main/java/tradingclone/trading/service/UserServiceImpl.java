package tradingclone.trading.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import tradingclone.trading.domain.VerificationType;
import tradingclone.trading.config.JwtProvider;
import tradingclone.trading.modal.TwoFactorAuth;
import tradingclone.trading.modal.User;
import tradingclone.trading.repository.UserRepository;

import java.util.Optional;

@RestController
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserProfileByJWT(String jwt) throws Exception {
        String email= JwtProvider.getEmailFromToken(jwt);
        User user=userRepository.findByEmail(email);

        if(user==null){
            throw new Exception("user not found");
        }

        return user;
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user=userRepository.findByEmail(email);

        if(user==null){
            throw new Exception("user not found");
        }

        return user;
    }

    @Override
    public User findUserById(Long UserId) throws Exception {
        Optional<User> user=userRepository.findById(UserId);

        if(user.isEmpty()){
            throw new Exception("user not found");
        }
        return user.get();
    }

    @Override
    public User enableTwoFactorAuthetication(VerificationType verificationType,String sendTo, User user) {
        TwoFactorAuth twoFactorAuth=new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);;
        twoFactorAuth.setSendTo(verificationType);

        user.setTwoFactorAuth(twoFactorAuth);
        return userRepository.save(user);
    }

    @Override
    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        return userRepository.save(user);
    }
}
