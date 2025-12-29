package com.example.bankcards.exception.dto;

import com.example.bankcards.config.openapi.constant.DtoSchemaConstants;
import com.example.bankcards.util.constant.Validation;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public record ErrorResponseDto(
		@Schema(description = DtoSchemaConstants.EXCEPTION_HTTP_STATUS_DESCRIPTION,
				example = DtoSchemaConstants.EXCEPTION_HTTP_STATUS_EXAMPLE)
		Integer httpStatus,
		@Schema(description = DtoSchemaConstants.EXCEPTION_MESSAGE_DESCRIPTION,
				example = DtoSchemaConstants.EXCEPTION_MESSAGE_EXAMPLE)
		String message,
		
		@Schema(description = DtoSchemaConstants.TIMESTAMP_DESCRIPTION,
				example = DtoSchemaConstants.TIMESTAMP_EXAMPLE)
		@JsonFormat(pattern = Validation.JSON_DATE_TIME_FORMAT)
		ZonedDateTime timestamp
) {
}