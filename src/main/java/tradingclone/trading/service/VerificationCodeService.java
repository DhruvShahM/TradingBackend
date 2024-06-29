package tradingclone.trading.service;

import tradingclone.trading.domain.VerificationType;
import tradingclone.trading.modal.User;
import tradingclone.trading.modal.VerificationCode;

public interface VerificationCodeService {

    VerificationCode sendVerficationCode(User user, VerificationType verificationType);

    VerificationCode getVerificationCodebyId(Long id) throws Exception;

    VerificationCode getVerificationCodeByUser(Long userId);

    void deleteVerificationCodeById(VerificationCode verificationCode);
}
