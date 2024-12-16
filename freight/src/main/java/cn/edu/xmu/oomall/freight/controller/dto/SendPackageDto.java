package cn.edu.xmu.oomall.freight.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 2023-dgn3-009
 *
 * @author huangzian
 */
@NoArgsConstructor
@AllArgsConstructor
public class SendPackageDto {
    @NotNull(message = "上门开始时间不能为空")
    private LocalDateTime startTime;
    @NotNull(message = "上门结束时间不能为空")
    private LocalDateTime endTime;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
