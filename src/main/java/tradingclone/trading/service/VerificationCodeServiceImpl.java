package tradingclone.trading.service;

import org.springframework.beans.factory.annotation.Autowired;
import tradingclone.trading.domain.VerificationType;
import tradingclone.trading.modal.User;
import tradingclone.trading.modal.VerificationCode;
import tradingclone.trading.repository.VerificationCodeRepository;
import tradingclone.trading.utils.OtpUtils;

import javax.swing.text.html.Option;
import java.util.Optional;

public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Override
    public VerificationCode sendVerficationCode(User user, VerificationType verificationType) {
        VerificationCode verificationCode1=new VerificationCode();
        verificationCode1.setOtp(OtpUtils.generateOTP());
        verificationCode1.setVerificationType(verificationType);
        verificationCode1.setUser(user);
        return verificationCodeRepository.save(verificationCode1);
    }

    @Override
    public VerificationCode getVerificationCodebyId(Long id) throws Exception {
        Optional<VerificationCode> verificationCode=verificationCodeRepository.findById(id);

        if(verificationCode.isPresent()){
            return verificationCode.get();
        }

        throw new Exception("verification code not found");
    }

    @Override
    public VerificationCode getVerificationCodeByUser(Long userId) {
        return verificationCodeRepository.findByUserId(userId);
    }

    @Override
    public void deleteVerificationCodeById(VerificationCode verificationCode) {
        verificationCodeRepository.delete(verificationCode);
    }
}
