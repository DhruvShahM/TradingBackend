package tradingclone.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tradingclone.trading.modal.ForgotPasswordToken;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordToken, String> {

    ForgotPasswordToken findByUserId(Long userId);
}
