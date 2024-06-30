package tradingclone.trading.service;

import org.springframework.beans.factory.annotation.Autowired;
import tradingclone.trading.domain.VerificationType;
import tradingclone.trading.modal.ForgotPasswordToken;
import tradingclone.trading.modal.User;
import tradingclone.trading.repository.ForgotPasswordRepository;

import java.util.Optional;

public class ForgotPasswordServiceImpl implements ForgotPasswordService   {
    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;


    @Override
    public ForgotPasswordToken createToken(User user,
                                           String id,
                                           String otp,
                                           VerificationType verificationType,
                                           String sendTo) {
        ForgotPasswordToken token=new ForgotPasswordToken();
        token.setUser(user);
        token.setSendTo(sendTo);
        token.setOtp(otp);
        token.setId(id);
        return forgotPasswordRepository.save(token);
    }

    @Override
    public ForgotPasswordToken findById(String id) {
        Optional<ForgotPasswordToken> token=forgotPasswordRepository.findById(id);
        return token.orElse(null);
    }

    @Override
    public ForgotPasswordToken findByUser(Long userId) {
        return forgotPasswordRepository.findByUserId(userId);
    }

    @Override
    public void deleteToken(ForgotPasswordToken token) {
        forgotPasswordRepository.delete(token);
    }
}
