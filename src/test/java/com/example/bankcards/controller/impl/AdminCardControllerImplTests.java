package com.example.bankcards.controller.impl;

import com.example.bankcards.configuration.TestSecurityConfig;
import com.example.bankcards.dto.request.CardRequestDto;
import com.example.bankcards.dto.response.CardPageViewResponseDto;
import com.example.bankcards.dto.response.CardResponseDto;
import com.example.bankcards.service.AdminCardService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
public class AdminCardControllerImplTests {
	
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MockMvc mockMvc;
	@MockitoBean
	private AdminCardService cardService;
	@MockitoBean
	private AuthenticationManager authenticationManager;
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Успешное создание карты")
	void create_shouldReturnCreatedStatusAndCardResponse() throws Exception {
		CardRequestDto cardRequest = TestUtils.testCardRequestDto();
		CardResponseDto cardResponse = TestUtils.testMaskedCardResponseDto1();
		
		when(cardService.createCard(any(CardRequestDto.class))).thenReturn(cardResponse);

		mockMvc.perform(post("/api/v1/cards")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(cardRequest)))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.cardNumber").value("**** **** **** 3456"))
				.andExpect(jsonPath("$.cardHolder").value("Вася Пупкин"))
				.andExpect(jsonPath("$.balance").value(1000.00))
				.andExpect(jsonPath("$.status").value("ACTIVE"));
		
		verify(cardService).createCard(any(CardRequestDto.class));
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Создание карты без тела запроса - Bad Request 400")
	void create_withoutBody_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(post("/api/v1/cards")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		
		verify(cardService, never()).createCard(any());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Создание карты с неверным Content-Type - Unsupported Media Type 415")
	void create_withWrongContentType_shouldReturnUnsupportedMediaType() throws Exception {
		CardRequestDto cardRequest = TestUtils.testCardRequestDto();
		
		mockMvc.perform(post("/api/v1/cards")
						.contentType(MediaType.TEXT_PLAIN)
						.content(objectMapper.writeValueAsString(cardRequest)))
				.andExpect(status().isUnsupportedMediaType());
		
		verify(cardService, never()).createCard(any());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Успешное получение всех карт клиента по ID")
	void getAllCardsByClientId_shouldReturnOkStatusAndPageResponse() throws Exception {
		CardPageViewResponseDto cardPageResponse = TestUtils.testCardPageViewResponseDto();
		UUID testClientId = TestUtils.testUser().getId();
		String cardHolder1Name = TestUtils.testCard1().getCardHolder();
		String cardHolder2Name = TestUtils.testCard2().getCardHolder();
		Integer page = 1;
		
		when(cardService.getAllCardsByClientId(testClientId, page)).thenReturn(cardPageResponse);
		
		mockMvc.perform(post("/api/v1/cards/{clientId}", testClientId)
						.param("page", page.toString()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.cards").isArray())
				.andExpect(jsonPath("$.cards[0].cardHolder").value(cardHolder1Name))
				.andExpect(jsonPath("$.cards[1].cardHolder").value(cardHolder2Name))
				.andExpect(jsonPath("$.currentPage").value(1));
		
		verify(cardService).getAllCardsByClientId(testClientId, page);
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Получение карт клиента без параметра page - Bad Request 400")
	void getAllCardsByClientId_withoutPageParam_shouldReturnBadRequest() throws Exception {
		UUID testClientId = TestUtils.testUser().getId();
		
		mockMvc.perform(post("/api/v1/cards/{clientId}", testClientId))
				.andExpect(status().isBadRequest());
		
		verify(cardService, never()).getAllCardsByClientId(any(), any());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Получение карт клиента с некорректным UUID - Bad Request 400")
	void getAllCardsByClientId_withInvalidUUID_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(post("/api/v1/cards/{clientId}", "invalid-uuid")
						.param("page", "1"))
				.andExpect(status().isBadRequest());
		
		verify(cardService, never()).getAllCardsByClientId(any(), any());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Успешное получение всех карт постранично")
	void getAllCardsByPage_shouldReturnOkStatusAndPageResponse() throws Exception {
		CardPageViewResponseDto cardPageResponse = TestUtils.testCardPageViewResponseDto();
		String cardHolder1Name = TestUtils.testCard1().getCardHolder();
		String cardHolder2Name = TestUtils.testCard2().getCardHolder();
		Integer page = 1;
		Integer limit = 10;
		when(cardService.getAllCardsByPage(page, limit)).thenReturn(cardPageResponse);

		mockMvc.perform(post("/api/v1/cards/page/{page}", page)
						.param("limit", limit.toString()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.cards").isArray())
				.andExpect(jsonPath("$.cards[0].cardHolder").value(cardHolder1Name))
				.andExpect(jsonPath("$.cards[1].cardHolder").value(cardHolder2Name))
				.andExpect(jsonPath("$.currentPage").value(1))
				.andExpect(jsonPath("$.limit").value(10));
		
		verify(cardService).getAllCardsByPage(page, limit);
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Получение всех карт без параметра limit - Bad Request 400")
	void getAllCardsByPage_withoutLimitParam_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(post("/api/v1/cards/page/{page}", 1))
				.andExpect(status().isBadRequest());
		
		verify(cardService, never()).getAllCardsByPage(any(), any());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Получение всех карт с некорректным номером страницы - Bad Request 400")
	void getAllCardsByPage_withInvalidPage_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(post("/api/v1/cards/page/{page}", "not-a-number")
						.param("limit", "10"))
				.andExpect(status().isBadRequest());
		
		verify(cardService, never()).getAllCardsByPage(any(), any());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Успешное удаление карты")
	void deleteCard_shouldReturnOkStatus() throws Exception {
		UUID testCardId = TestUtils.testCard1().getId();
		doNothing().when(cardService).deleteCard(testCardId);

		mockMvc.perform(delete("/api/v1/cards/{cardId}", testCardId))
				.andExpect(status().isOk());
		
		verify(cardService).deleteCard(testCardId);
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Удаление карты с некорректным UUID - Bad Request 400")
	void deleteCard_withInvalidUUID_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(delete("/api/v1/cards/{cardId}", "invalid-uuid"))
				.andExpect(status().isBadRequest());
		
		verify(cardService, never()).deleteCard(any());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Успешная блокировка карты")
	void blockCard_shouldReturnOkStatusAndTrue() throws Exception {
		UUID testCardId = TestUtils.testCard1().getId();
		
		when(cardService.blockCard(testCardId)).thenReturn(true);

		mockMvc.perform(get("/api/v1/cards/block/{cardId}", testCardId))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string("true"));
		
		verify(cardService).blockCard(testCardId);
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Блокировка карты с некорректным UUID - Bad Request 400")
	void blockCard_withInvalidUUID_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(get("/api/v1/cards/block/{cardId}", "invalid-uuid"))
				.andExpect(status().isBadRequest());
		
		verify(cardService, never()).blockCard(any());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Успешная активация карты")
	void activateCard_shouldReturnOkStatusAndTrue() throws Exception {
		UUID testCardId = TestUtils.testCard1().getId();
		
		when(cardService.activateCard(testCardId)).thenReturn(true);

		mockMvc.perform(get("/api/v1/cards/activate/{cardId}", testCardId))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string("true"));
		
		verify(cardService).activateCard(testCardId);
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Активация карты с некорректным UUID - Bad Request 400")
	void activateCard_withInvalidUUID_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(get("/api/v1/cards/activate/{cardId}", "invalid-uuid"))
				.andExpect(status().isBadRequest());
		
		verify(cardService, never()).activateCard(any());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Блокировка карты должна возвращать false при неудачной операции")
	void blockCard_whenServiceReturnsFalse_shouldReturnOkWithFalse() throws Exception {
		UUID testCardId = TestUtils.testCard1().getId();
		
		when(cardService.blockCard(testCardId)).thenReturn(false);

		mockMvc.perform(get("/api/v1/cards/block/{cardId}", testCardId))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string("false"));
		
		verify(cardService).blockCard(testCardId);
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Активация карты должна возвращать false при неудачной операции")
	void activateCard_whenServiceReturnsFalse_shouldReturnOkWithFalse() throws Exception {
		UUID testCardId = TestUtils.testCard1().getId();
		
		when(cardService.activateCard(testCardId)).thenReturn(false);

		mockMvc.perform(get("/api/v1/cards/activate/{cardId}", testCardId))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string("false"));
		
		verify(cardService).activateCard(testCardId);
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Создание карты с валидацией полей DTO")
	void create_withDtoValidation_shouldValidateAllFields() throws Exception {
		UUID testClientId = TestUtils.testUser().getId();
		CardRequestDto validRequest = new CardRequestDto(testClientId);
		CardResponseDto cardResponse = TestUtils.testMaskedCardResponseDto1();
		
		when(cardService.createCard(any(CardRequestDto.class))).thenReturn(cardResponse);

		mockMvc.perform(post("/api/v1/cards")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(validRequest)))
				.andExpect(status().isCreated());
		
		verify(cardService).createCard(any(CardRequestDto.class));
	}
}