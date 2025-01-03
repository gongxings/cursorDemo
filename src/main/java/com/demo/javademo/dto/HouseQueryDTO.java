package com.demo.javademo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "房源查询条件")
public class HouseQueryDTO {
    @Schema(description = "区域")
    private String district;

    @DecimalMin(value = "0", message = "最小价格不能小于0")
    @Schema(description = "最小价格")
    private BigDecimal minPrice;

    @DecimalMin(value = "0", message = "最大价格不能小于0")
    @Schema(description = "最大价格")
    private BigDecimal maxPrice;

    @DecimalMin(value = "0", message = "最小面积不能小于0")
    @Schema(description = "最小面积")
    private BigDecimal minArea;

    @DecimalMin(value = "0", message = "最大面积不能小于0")
    @Schema(description = "最大面积")
    private BigDecimal maxArea;

    @Schema(description = "房间数")
    private Integer roomCount;

    @Schema(description = "房源类型")
    private String type;

    @Schema(description = "房源状态")
    private String status;
} 