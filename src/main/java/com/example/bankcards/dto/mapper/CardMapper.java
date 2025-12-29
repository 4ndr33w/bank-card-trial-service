package com.example.bankcards.dto.mapper;

import com.example.bankcards.dto.mapper.decorator.CardMapperDecorator;
import com.example.bankcards.dto.projection.CardBalanceProjection;
import com.example.bankcards.dto.request.CardRequestDto;
import com.example.bankcards.dto.response.CardBalanceResponseDto;
import com.example.bankcards.dto.response.CardResponseDto;
import com.example.bankcards.entity.Card;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Mapper(
		componentModel = "spring",
		uses = CardMapperDecorator.class,
		unmappedTargetPolicy = ReportingPolicy.IGNORE,
		unmappedSourcePolicy = ReportingPolicy.IGNORE
)
@DecoratedWith(CardMapperDecorator.class)
public interface CardMapper {
	
	@Mapping(target = "id", ignore = true)
	Card mapRequestToEntity(CardRequestDto request);
	
	CardResponseDto mapEntityToResponse(Card entity);
	
	CardBalanceResponseDto mapBalanceResponse(CardBalanceProjection projection);
}
