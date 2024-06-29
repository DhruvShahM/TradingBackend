package tradingclone.trading.modal;

import lombok.Data;
import tradingclone.trading.domain.VerificationType;

@Data
public class TwoFactorAuth {
    private boolean isEnabled = false;
    private VerificationType sendTo;
}
