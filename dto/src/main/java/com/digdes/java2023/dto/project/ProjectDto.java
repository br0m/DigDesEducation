package com.digdes.java2023.dto.project;

import com.digdes.java2023.dto.enums.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Schema(description = "DTO проекта")
public class ProjectDto {

    @Schema(description = "Уникальный ID проекта")
    private Integer id;

    @Schema(description = "Уникальное кодовое название")
    @NotBlank
    private String codename;

    @Schema(description = "Наименование")
    @NotBlank
    private String title;

    @Schema(description = "Описание")
    private String description;

    @Schema(description = "Статус: DRAFT, DEVELOPING, TESTING, FINISHED")
    private ProjectStatus status;
}
