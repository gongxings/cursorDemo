package com.rental.dataAnalysis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "房源信息")
public class HouseDTO {
    @Schema(description = "房源ID（创建时不需要传）")
    private Long id;

    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200个字符")
    @Schema(description = "房源标题", required = true)
    private String title;

    @NotBlank(message = "地址不能为空")
    @Size(max = 500, message = "地址长度不能超过500个字符")
    @Schema(description = "详细地址", required = true)
    private String address;

    @NotBlank(message = "区域不能为空")
    @Schema(description = "所在区域", required = true)
    private String district;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0", message = "价格不能小于0")
    @Schema(description = "价格", required = true)
    private BigDecimal price;

    @NotNull(message = "面积不能为空")
    @DecimalMin(value = "0", message = "面积不能小于0")
    @Schema(description = "面积", required = true)
    private BigDecimal area;

    @NotNull(message = "房间数不能为空")
    @Min(value = 1, message = "房间数不能小于1")
    @Schema(description = "房间数", required = true)
    private Integer roomCount;

    @NotNull(message = "楼层不能为空")
    @Min(value = 1, message = "楼层不能小于1")
    @Schema(description = "楼层", required = true)
    private Integer floor;

    @NotNull(message = "总楼层不能为空")
    @Min(value = 1, message = "总楼层不能小于1")
    @Schema(description = "总楼层", required = true)
    private Integer totalFloor;

    @Schema(description = "建造年份")
    private String buildYear;

    @NotBlank(message = "房源类型不能为空")
    @Schema(description = "房源类型", required = true)
    private String type;

    @Schema(description = "房源状态")
    private String status;

    @Schema(description = "特征标签")
    private List<String> features;

    @Schema(description = "配套设施")
    private List<String> facilities;

    @Size(max = 2000, message = "描述长度不能超过2000个字符")
    @Schema(description = "详细描述")
    private String description;
} 