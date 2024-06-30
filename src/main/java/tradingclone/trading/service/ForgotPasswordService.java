package tradingclone.trading.service;

import tradingclone.trading.domain.VerificationType;
import tradingclone.trading.modal.ForgotPasswordToken;
import tradingclone.trading.modal.User;

public interface ForgotPasswordService {

    ForgotPasswordToken createToken(User user,
                                    String id,
                                    String otp,
                                    VerificationType verificationType,
                                    String sendTo);

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToken(ForgotPasswordToken token);

}
