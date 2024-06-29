package tradingclone.trading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tradingclone.trading.domain.VerificationType;
import tradingclone.trading.modal.User;
import tradingclone.trading.modal.VerificationCode;
import tradingclone.trading.service.EmailService;
import tradingclone.trading.service.UserService;
import tradingclone.trading.service.VerificationCodeService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJWT(jwt);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOTP(@RequestHeader("Authorization") String jwt,
                                                   @PathVariable VerificationType verificationType) throws Exception {
        User user = userService.findUserProfileByJWT(jwt);
        VerificationCode verificationCode=verificationCodeService.getVerificationCodeByUser(user.getId());

        if(verificationCode!=null){
            verificationCodeService.deleteVerificationCodeById(verificationCode);
        }
        else{
            verificationCode=verificationCodeService.sendVerficationCode(user,verificationType);
        }

        if(verificationType.equals(VerificationType.EMAIL)){
            emailService.sendverificationOtpEmail(user.getEmail(),verificationCode.getOtp());
        }

        return new ResponseEntity<String>("verification otp sent successfully", HttpStatus.OK);
    }

    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(@PathVariable String otp,@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJWT(jwt);

        VerificationCode verificationCode=verificationCodeService.getVerificationCodeByUser(user.getId());

        String sendTo=verificationCode.getVerificationType().equals(VerificationType.EMAIL)?verificationCode.getEmail():verificationCode.getMobile();

        boolean isVerified=verificationCode.getOtp().equals(otp);

        if(isVerified){
            User updatedUser=userService.enableTwoFactorAuthetication(verificationCode.getVerificationType(),sendTo,user);
            verificationCodeService.deleteVerificationCodeById(verificationCode);
            return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
        }

       throw new Exception("wrong otp");
    }


}
