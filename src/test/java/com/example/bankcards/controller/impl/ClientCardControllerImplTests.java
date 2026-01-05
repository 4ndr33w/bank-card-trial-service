package com.example.bankcards.controller.impl;

import com.example.bankcards.configuration.TestSecurityConfig;
import com.example.bankcards.dto.response.CardPageViewResponseDto;
import com.example.bankcards.dto.response.CardResponseDto;
import com.example.bankcards.service.ClientCardService;
import com.example.bankcards.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import({TestSecurityConfig.class})
@ActiveProfiles("test")
public class ClientCardControllerImplTests {
	
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MockMvc mockMvc;
	@MockitoBean
	private ClientCardService cardService;
	@MockitoBean
	private AuthenticationManager authenticationManager;
	
	@Test
	@DisplayName("Получение баланса с некорректным UUID должно вернуть 400")
	void getBalance_withInvalidUUID_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(get("/api/v1/clients/cards/balance/{cardId}", "invalid-uuid"))
				.andExpect(status().isBadRequest());
		
		verify(cardService, never()).getCardBalance(any());
	}
	
	@Test
	@DisplayName("Успешное получение списка карт с пагинацией")
	void getAllCardsByPage_shouldReturnOkStatusAndPageResponse() throws Exception {
		CardPageViewResponseDto cardPageResponse = TestUtils.testCardPageViewResponseDto();
		String cardHolder1 = TestUtils.testMaskedCardResponseDto1().cardHolder();
		String cardHolder2 = TestUtils.testMaskedCardResponseDto2().cardHolder();
		Integer page = 1;
		Integer limit = 10;
		when(cardService.getAllCardsByPage(page, limit)).thenReturn(cardPageResponse);

		mockMvc.perform(post("/api/v1/clients/cards/page/{page}", page)
						.param("limit", limit.toString()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.cards").isArray())
				.andExpect(jsonPath("$.currentPage").value(1))
				.andExpect(jsonPath("$.limit").value(10))
				.andExpect(jsonPath("$.cards[0].cardHolder").value(cardHolder1))
				.andExpect(jsonPath("$.cards[1].cardHolder").value(cardHolder2));

		verify(cardService).getAllCardsByPage(page, limit);
	}
	
	@Test
	@DisplayName("Получение списка карт без параметра limit должно вернуть 400")
	void getAllCardsByPage_withoutLimitParam_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(post("/api/v1/clients/cards/page/{page}", 1))
				.andExpect(status().isBadRequest());
		
		verify(cardService, never()).getAllCardsByPage(any(), any());
	}
	
	@Test
	@DisplayName("Получение списка карт с некорректным номером страницы должно вернуть 400")
	void getAllCardsByPage_withInvalidPage_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(post("/api/v1/clients/cards/page/{page}", "not-a-number")
						.param("limit", "10"))
				.andExpect(status().isBadRequest());
		
		verify(cardService, never()).getAllCardsByPage(any(), any());
	}
	
	@Test
	@DisplayName("Успешное получение данных карты по ID")
	void getCardById_shouldReturnOkStatusAndCardData() throws Exception {
		CardResponseDto cardResponse = TestUtils.testMaskedCardResponseDto1();
		UUID testCardId = UUID.randomUUID();
		
		when(cardService.findCardById(testCardId)).thenReturn(cardResponse);

		mockMvc.perform(get("/api/v1/clients/cards/{cardId}", testCardId))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.cardNumber").value("**** **** **** 3456"))
				.andExpect(jsonPath("$.cardHolder").value("Вася Пупкин"))
				.andExpect(jsonPath("$.balance").value(1000.00))
				.andExpect(jsonPath("$.status").value("ACTIVE"));
		
		verify(cardService).findCardById(testCardId);
	}
	
	@Test
	@DisplayName("Получение данных карты с некорректным UUID должно вернуть 400")
	void getCardById_withInvalidUUID_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(get("/api/v1/clients/cards/{cardId}", "invalid-uuid"))
				.andExpect(status().isBadRequest());
		
		verify(cardService, never()).findCardById(any());
	}
	
	@Test
	@DisplayName("Успешная отправка запроса на блокировку карты")
	void blockCardRequest_shouldReturnOkStatusAndTrue() throws Exception {
		UUID testCardId = UUID.randomUUID();
		
		when(cardService.blockCardRequest(testCardId)).thenReturn(true);

		mockMvc.perform(get("/api/v1/clients/cards/block/{cardId}", testCardId))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string("true"));
		
		verify(cardService).blockCardRequest(testCardId);
	}
	
	@Test
	@DisplayName("Запрос на блокировку карты с некорректным UUID должно вернуть 400")
	void blockCardRequest_withInvalidUUID_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(get("/api/v1/clients/cards/block/{cardId}", "invalid-uuid"))
				.andExpect(status().isBadRequest());
		
		verify(cardService, never()).blockCardRequest(any());
	}
	
	@Test
	@DisplayName("Успешный перевод средств между картами")
	void transferMoney_shouldReturnOkStatusAndTrue() throws Exception {
		UUID testCardIdFrom = UUID.randomUUID();
		UUID testCardIdTo = UUID.randomUUID();
		
		BigDecimal amount = new BigDecimal("100.00");
		when(cardService.transferMoney(eq(amount), eq(testCardIdFrom), eq(testCardIdTo)))
				.thenReturn(true);

		mockMvc.perform(post("/api/v1/clients/cards/transfer")
						.param("amount", amount.toString())
						.param("cardIdFrom", testCardIdFrom.toString())
						.param("cardIdTo", testCardIdTo.toString()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string("true"));
		
		verify(cardService).transferMoney(amount, testCardIdFrom, testCardIdTo);
	}
	
	@Test
	@DisplayName("Перевод средств без указания суммы должен вернуть 400")
	void transferMoney_withoutAmount_shouldReturnBadRequest() throws Exception {
		UUID testCardIdFrom = UUID.randomUUID();
		UUID testCardIdTo = UUID.randomUUID();
		
		mockMvc.perform(post("/api/v1/clients/cards/transfer")
						.param("cardIdFrom", testCardIdFrom.toString())
						.param("cardIdTo", testCardIdTo.toString()))
				.andExpect(status().isBadRequest());
		
		verify(cardService, never()).transferMoney(any(), any(), any());
	}
	
	@Test
	@DisplayName("Перевод средств без указания карты-отправителя должен вернуть 400")
	void transferMoney_withoutCardIdFrom_shouldReturnBadRequest() throws Exception {
		UUID testCardIdTo = UUID.randomUUID();
		
		mockMvc.perform(post("/api/v1/clients/cards/transfer")
						.param("amount", "100.00")
						.param("cardIdTo", testCardIdTo.toString()))
				.andExpect(status().isBadRequest());
		
		verify(cardService, never()).transferMoney(any(), any(), any());
	}
	
	@Test
	@DisplayName("Перевод средств без указания карты-получателя должен вернуть 400")
	void transferMoney_withoutCardIdTo_shouldReturnBadRequest() throws Exception {
		UUID testCardIdFrom = UUID.randomUUID();
		
		mockMvc.perform(post("/api/v1/clients/cards/transfer")
						.param("amount", "100.00")
						.param("cardIdFrom", testCardIdFrom.toString()))
				.andExpect(status().isBadRequest());
		
		verify(cardService, never()).transferMoney(any(), any(), any());
	}
	
	@Test
	@DisplayName("Перевод средств с некорректным UUID карты-отправителя должен вернуть 400")
	void transferMoney_withInvalidCardIdFrom_shouldReturnBadRequest() throws Exception {
		UUID testCardIdTo = UUID.randomUUID();
		
		mockMvc.perform(post("/api/v1/clients/cards/transfer")
						.param("amount", "100.00")
						.param("cardIdFrom", "invalid-uuid")
						.param("cardIdTo", testCardIdTo.toString()))
				.andExpect(status().isBadRequest());
		
		verify(cardService, never()).transferMoney(any(), any(), any());
	}
	
	@Test
	@DisplayName("Перевод средств с некорректным форматом суммы должен вернуть 400")
	void transferMoney_withInvalidAmountFormat_shouldReturnBadRequest() throws Exception {
		UUID testCardIdFrom = UUID.randomUUID();
		UUID testCardIdTo = UUID.randomUUID();
		
		mockMvc.perform(post("/api/v1/clients/cards/transfer")
						.param("amount", "not-a-number")
						.param("cardIdFrom", testCardIdFrom.toString())
						.param("cardIdTo", testCardIdTo.toString()))
				.andExpect(status().isBadRequest());
		
		verify(cardService, never()).transferMoney(any(), any(), any());
	}
	
	@Test
	@DisplayName("Успешный перевод средств должен возвращать false при неудачной операции")
	void transferMoney_whenServiceReturnsFalse_shouldReturnOkWithFalse() throws Exception {
		UUID testCardIdFrom = UUID.randomUUID();
		UUID testCardIdTo = UUID.randomUUID();
		BigDecimal amount = new BigDecimal("100.00");
		
		when(cardService.transferMoney(eq(amount), eq(testCardIdFrom), eq(testCardIdTo)))
				.thenReturn(false);

		mockMvc.perform(post("/api/v1/clients/cards/transfer")
						.param("amount", amount.toString())
						.param("cardIdFrom", testCardIdFrom.toString())
						.param("cardIdTo", testCardIdTo.toString()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string("false"));
		
		verify(cardService).transferMoney(amount, testCardIdFrom, testCardIdTo);
	}
	
	@Test
	@DisplayName("Успешный запрос на блокировку должен возвращать false при неудачной операции")
	void blockCardRequest_whenServiceReturnsFalse_shouldReturnOkWithFalse() throws Exception {
		UUID testCardId = UUID.randomUUID();
		
		when(cardService.blockCardRequest(testCardId)).thenReturn(false);

		mockMvc.perform(get("/api/v1/clients/cards/block/{cardId}", testCardId))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string("false"));
		
		verify(cardService).blockCardRequest(testCardId);
	}
}