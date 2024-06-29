package tradingclone.trading.modal;

import jakarta.persistence.*;
import lombok.Data;
import tradingclone.trading.domain.VerificationType;

@Entity
@Data
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String otp;

    @OneToOne
    private User user;

    private String email;

    private String mobile;

    private VerificationType verificationType;

}
