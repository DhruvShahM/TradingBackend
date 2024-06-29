package tradingclone.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tradingclone.trading.modal.TwoFactorOTP;

public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOTP,String> {

    TwoFactorOTP findByUserId(Long userId);
}
