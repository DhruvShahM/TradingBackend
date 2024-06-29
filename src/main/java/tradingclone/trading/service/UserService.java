package tradingclone.trading.service;

import tradingclone.trading.domain.VerificationType;
import tradingclone.trading.modal.User;

public interface UserService {

    public User findUserProfileByJWT(String jwt) throws Exception;

    public User findUserByEmail(String email) throws Exception;

    public User findUserById(Long UserId) throws Exception;

    public User enableTwoFactorAuthetication(VerificationType verificationType, String sendTo, User user);

    User updatePassword(User user,String newPassword);
}
