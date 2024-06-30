package tradingclone.trading.request;

import lombok.Data;
import tradingclone.trading.domain.VerificationType;

@Data
public class ForgotPasswordTokenRequest {
    private String sendTo;
    private VerificationType verificationType;
}
