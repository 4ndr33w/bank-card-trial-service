package com.example.bankcards.exception.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public record ErrorResponseDto(
//		@Schema(description = DtoSchemaMessages.EXCEPTION_HTTP_STATUS_DESCRIPTION,
//				example = DtoSchemaMessages.EXCEPTION_HTTP_STATUS_EXAMPLE)
		Integer httpStatus,
//		@Schema(description = DtoSchemaMessages.EXCEPTION_MESSAGE_DESCRIPTION,
//				example = DtoSchemaMessages.EXCEPTION_MESSAGE_EXAMPLE)
		String message,
		
//		@Schema(description = DtoSchemaMessages.TIMESTAMP_DESCRIPTION,
//				example = DtoSchemaMessages.TIMESTAMP_EXAMPLE)
//		@JsonFormat(pattern = Validation.JSON_DATE_TIME_FORMAT)
		ZonedDateTime timestamp
) {
}
