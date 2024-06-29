package tradingclone.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tradingclone.trading.modal.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    public VerificationCode findByUserId(Long userId);
}
