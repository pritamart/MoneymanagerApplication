package in.bushansigur.moneymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncomeDTO {
    private Long id;
    private String name;
    private String icon;
    private LocalDateTime date;
    private BigDecimal amount;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
