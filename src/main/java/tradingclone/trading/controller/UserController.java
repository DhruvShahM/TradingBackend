package tradingclone.trading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tradingclone.trading.request.ForgotPasswordTokenRequest;
import tradingclone.trading.domain.VerificationType;
import tradingclone.trading.modal.ForgotPasswordToken;
import tradingclone.trading.modal.User;
import tradingclone.trading.modal.VerificationCode;
import tradingclone.trading.request.ResetPasswordRequest;
import tradingclone.trading.response.ApiResponse;
import tradingclone.trading.response.AuthResponse;
import tradingclone.trading.service.EmailService;
import tradingclone.trading.service.ForgotPasswordService;
import tradingclone.trading.service.UserService;
import tradingclone.trading.service.VerificationCodeService;
import tradingclone.trading.utils.OtpUtils;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

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

    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOTP(@RequestHeader("Authorization") String jwt, @RequestBody ForgotPasswordTokenRequest req
                                                        ) throws Exception {
        User user=userService.findUserByEmail(req.getSendTo());
        String otp= OtpUtils.generateOTP();
        UUID uuid=UUID.randomUUID();
        String id=uuid.toString();

        ForgotPasswordToken token=forgotPasswordService.findByUser(user.getId());

        if(token==null){
            token=forgotPasswordService.createToken(user,id, otp,req.getVerificationType(), req.getSendTo());
        }

        if(req.getVerificationType().equals(VerificationType.EMAIL)){
            emailService.sendverificationOtpEmail(user.getEmail(),token.getOtp());
        }

        AuthResponse response=new AuthResponse();
        response.setSession(token.getId());
        response.setMessage("Password reset otp sent successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam String id,
                                              @RequestHeader("Authorization") String jwt, @RequestBody ResetPasswordRequest req) throws Exception {


        ForgotPasswordToken forgotPasswordToken=forgotPasswordService.findById(id);


        boolean isVerified=forgotPasswordToken.getOtp().equals(req.getOtp());

        if(isVerified){
            userService.updatePassword(forgotPasswordToken.getUser(),req.getPassword());
            ApiResponse response=new ApiResponse();
            response.setMessage("password update successfully");
            return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
        }

        throw new Exception("wrong otp");
    }

}
