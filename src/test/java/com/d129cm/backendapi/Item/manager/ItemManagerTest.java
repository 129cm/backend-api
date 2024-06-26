package com.d129cm.backendapi.Item.manager;

import com.d129cm.backendapi.item.domain.Item;
import com.d129cm.backendapi.item.manager.ItemManager;
import com.d129cm.backendapi.item.repository.ItemRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class ItemManagerTest {

    @InjectMocks
    private ItemManager itemManager;

    @Mock
    private ItemRepository itemRepository;

    @Nested
    class saveItem {

        @Test
        void 성공_아이템_저장() {
            // given
            Item mockItem = mock(Item.class);
            when(itemRepository.save(mockItem)).thenReturn(mockItem);

            // when
            itemManager.saveItem(mockItem);

            // then
            verify(itemRepository).save(mockItem);
        }
    }

}
