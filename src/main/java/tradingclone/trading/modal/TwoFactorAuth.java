package tradingclone.trading.modal;

import lombok.Data;
import tradingclone.trading.Domain.VerificationType;

@Data
public class TwoFactorAuth {
    private boolean isEnabled = false;
    private VerificationType sendTo;
}
