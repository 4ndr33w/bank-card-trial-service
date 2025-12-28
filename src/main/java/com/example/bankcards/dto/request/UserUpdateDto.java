package com.example.bankcards.dto.request;

import com.example.bankcards.config.openapi.constant.DtoSchemaConstants;
import com.example.bankcards.util.constant.RegexpPatterns;
import com.example.bankcards.util.constant.Validation;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public record UserUpdateDto(
		
		@Size(min = 1, max = 30)
		@Pattern(regexp = RegexpPatterns.REGEXP_NAME_PATTERN, message = RegexpPatterns.REGEXP_NAME_MESSAGE)
		@Schema(description = DtoSchemaConstants.NAME_DESCRIPTION, example = DtoSchemaConstants.NAME_EXAMPLE)
		String name,
		
		@Schema(description = DtoSchemaConstants.LAST_NAME_DESCRIPTION, example = DtoSchemaConstants.LAST_NAME_EXAMPLE)
		String lastName,
		
		@JsonFormat(pattern = Validation.JSON_DATE_FORMAT)
		@Past(message = Validation.VALIDATION_PAST_MESSAGE)
		@Schema(description = DtoSchemaConstants.BIRTH_DATE_DESCRIPTION, example = DtoSchemaConstants.BIRTH_DATE_EXAMPLE)
		LocalDate birthDate
) {
}