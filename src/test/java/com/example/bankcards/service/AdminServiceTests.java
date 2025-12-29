package com.example.bankcards.service;

import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.dto.request.CardRequestDto;
import com.example.bankcards.dto.response.CardResponseDto;
import com.example.bankcards.dto.response.UserResponseDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.impl.AdminCardServiceImpl;
import com.example.bankcards.service.impl.UtilService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class AdminServiceTests {

}
