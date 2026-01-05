package com.example.bankcards.controller.impl;

import com.example.bankcards.configuration.TestSecurityConfig;
import com.example.bankcards.dto.request.UserUpdateDto;
import com.example.bankcards.dto.response.UserResponseDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.UserRole;
import com.example.bankcards.service.AdminService;
import com.example.bankcards.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
public class AdminControllerImplTests {
	
	@Autowired
	private ObjectMapper objectMapper;
	@MockitoBean
	private AuthenticationManager authenticationManager;
	@Autowired
	private MockMvc mockMvc;
	@MockitoBean
	private AdminService adminService;

	@Test
	@WithMockUser(username = "user", authorities = {"USER"})
	@DisplayName("Попытка доступа к методам админа с ролью USER - Access Denied 403")
	void adminEndpoints_withUserRole_shouldReturnForbidden() throws Exception {
		UUID testUserId = UUID.randomUUID();

		mockMvc.perform(get("/api/v1/admin/block/{clientId}", testUserId))
				.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Успешное обновление пользователя по ID")
	void updateByClientId_shouldReturnAcceptedStatusAndUserResponseDto() throws Exception {
		UserUpdateDto validUpdateDto = TestUtils.testUserUpdateDto();
		User testUser = TestUtils.testUser();
		UUID testUserId = testUser.getId();
		UserResponseDto expectedResponse = TestUtils.testUpdatedUserResponseDto();

		when(adminService.updateByClientId(any(UserUpdateDto.class), eq(testUserId)))
				.thenReturn(expectedResponse);

		mockMvc.perform(patch("/api/v1/admin/{clientId}", testUserId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(validUpdateDto)))
				.andExpect(status().isAccepted())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(testUserId.toString()))
				.andExpect(jsonPath("$.name").value(expectedResponse.name()))
				.andExpect(jsonPath("$.lastName").value(expectedResponse.lastName()));
		
		verify(adminService).updateByClientId(any(UserUpdateDto.class), eq(testUserId));
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Успешное удаление пользователя по ID")
	void deleteByClientId_shouldReturnOkStatus() throws Exception {
		UUID testUserId = UUID.randomUUID();
		
		when(adminService.deleteByClientId(testUserId)).thenReturn(true);

		mockMvc.perform(delete("/api/v1/admin/{clientId}", testUserId))
				.andExpect(status().isOk());
		
		verify(adminService).deleteByClientId(testUserId);
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Удаление пользователя с некорректным UUID - Bad Request 400")
	void deleteByClientId_withInvalidUUID_shouldReturnBadRequest() throws Exception {

		mockMvc.perform(delete("/api/v1/admin/{clientId}", "invalid-uuid"))
				.andExpect(status().isBadRequest());
		
		verify(adminService, never()).deleteByClientId(any());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Успешная блокировка аккаунта пользователя по ID")
	void blockAccountByClientId_shouldReturnOkStatusAndTrue() throws Exception {
		UUID testUserId = UUID.randomUUID();
		
		when(adminService.blockByClientId(testUserId)).thenReturn(true);

		mockMvc.perform(get("/api/v1/admin/block/{clientId}", testUserId))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string("true"));
		
		verify(adminService).blockByClientId(testUserId);
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Успешная деактивация аккаунта пользователя по ID")
	void deactivateAccountByClientId_shouldReturnOkStatusAndTrue() throws Exception {
		UUID testUserId = UUID.randomUUID();
		
		when(adminService.deactivateByClientId(testUserId)).thenReturn(true);

		mockMvc.perform(get("/api/v1/admin/deactivate/{clientId}", testUserId))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string("true"));
		
		verify(adminService).deactivateByClientId(testUserId);
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Успешная разблокировка аккаунта пользователя по ID")
	void unblockAccountByClientId_shouldReturnOkStatusAndTrue() throws Exception {
		UUID testUserId = UUID.randomUUID();
		
		when(adminService.unblockByClientId(testUserId)).thenReturn(true);

		mockMvc.perform(get("/api/v1/admin/unblock/{clientId}", testUserId))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string("true"));
		
		verify(adminService).unblockByClientId(testUserId);
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Успешная активация аккаунта пользователя по ID")
	void activateAccountByClientId_shouldReturnOkStatusAndTrue() throws Exception {
		UUID testUserId = UUID.randomUUID();
		
		when(adminService.activateByClientId(testUserId)).thenReturn(true);

		mockMvc.perform(get("/api/v1/admin/activate/{clientId}", testUserId))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string("true"));
		
		verify(adminService).activateByClientId(testUserId);
	}
	
	@ParameterizedTest
	@EnumSource(UserRole.class)
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Успешное добавление роли пользователю для всех типов ролей")
	void addRoleToUser_withDifferentRoles_shouldReturnOkStatusAndTrue(UserRole role) throws Exception {
		UUID testUserId = UUID.randomUUID();
		
		when(adminService.addRoleToUser(eq(role), eq(testUserId))).thenReturn(true);

		mockMvc.perform(post("/api/v1/admin/role/add")
						.param("role", role.name())
						.param("userId", testUserId.toString()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string("true"));
		
		verify(adminService).addRoleToUser(role, testUserId);
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Добавление роли с некорректным UUID - Bad Request 400")
	void addRoleToUser_withInvalidUUID_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(post("/api/v1/admin/role/add")
						.param("role", UserRole.ADMIN.name())
						.param("userId", "invalid-uuid"))
				.andExpect(status().isBadRequest());
		
		verify(adminService, never()).addRoleToUser(any(), any());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Добавление роли без указания роли - Bad Request 400")
	void addRoleToUser_withoutRoleParam_shouldReturnBadRequest() throws Exception {
		UUID testUserId = UUID.randomUUID();
		
		mockMvc.perform(post("/api/v1/admin/role/add")
						.param("userId", testUserId.toString()))
				.andExpect(status().isBadRequest());
		
		verify(adminService, never()).addRoleToUser(any(), any());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Добавление роли без указания userId - Bad Request 400")
	void addRoleToUser_withoutUserIdParam_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(post("/api/v1/admin/role/add")
						.param("role", UserRole.ADMIN.name()))
				.andExpect(status().isBadRequest());
		
		verify(adminService, never()).addRoleToUser(any(), any());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Добавление некорректной роли - Bad Request 400")
	void addRoleToUser_withInvalidRole_shouldReturnBadRequest() throws Exception {
		UUID testUserId = UUID.randomUUID();
		
		mockMvc.perform(post("/api/v1/admin/role/add")
						.param("role", "INVALID_ROLE")
						.param("userId", testUserId.toString()))
				.andExpect(status().isBadRequest());
		
		verify(adminService, never()).addRoleToUser(any(), any());
	}

	@ParameterizedTest
	@EnumSource(UserRole.class)
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Успешное удаление роли у пользователя для всех типов ролей")
	void removeRoleFromUser_withDifferentRoles_shouldReturnOkStatusAndTrue(UserRole role) throws Exception {
		UUID testUserId = UUID.randomUUID();
		
		when(adminService.removeRoleFromUser(eq(role), eq(testUserId))).thenReturn(true);

		mockMvc.perform(post("/api/v1/admin/role/remove")
						.param("role", role.name())
						.param("userId", testUserId.toString()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string("true"));
		
		verify(adminService).removeRoleFromUser(role, testUserId);
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Удаление роли с некорректным UUID - Bad Request 400")
	void removeRoleFromUser_withInvalidUUID_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(post("/api/v1/admin/role/remove")
						.param("role", UserRole.ADMIN.name())
						.param("userId", "invalid-uuid"))
				.andExpect(status().isBadRequest());
		
		verify(adminService, never()).removeRoleFromUser(any(), any());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Обновление пользователя без тела запроса - Bad Request 400")
	void updateByClientId_withoutBody_shouldReturnBadRequest() throws Exception {
		UUID testUserId = UUID.randomUUID();
		
		mockMvc.perform(patch("/api/v1/admin/{clientId}", testUserId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		
		verify(adminService, never()).updateByClientId(any(), any());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Добавление роли - false при неудачной операции")
	void addRoleToUser_whenServiceReturnsFalse_shouldReturnOkWithFalse() throws Exception {
		UUID testUserId = UUID.randomUUID();
		
		when(adminService.addRoleToUser(eq(UserRole.ADMIN), eq(testUserId))).thenReturn(false);

		mockMvc.perform(post("/api/v1/admin/role/add")
						.param("role", UserRole.ADMIN.name())
						.param("userId", testUserId.toString()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string("false"));
		
		verify(adminService).addRoleToUser(UserRole.ADMIN, testUserId);
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	@DisplayName("Удаление роли - false при неудачной операции")
	void removeRoleFromUser_whenServiceReturnsFalse_shouldReturnOkWithFalse() throws Exception {
		UUID testUserId = UUID.randomUUID();
		
		when(adminService.removeRoleFromUser(eq(UserRole.USER), eq(testUserId))).thenReturn(false);

		mockMvc.perform(post("/api/v1/admin/role/remove")
						.param("role", UserRole.USER.name())
						.param("userId", testUserId.toString()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string("false"));
		
		verify(adminService).removeRoleFromUser(UserRole.USER, testUserId);
	}
}