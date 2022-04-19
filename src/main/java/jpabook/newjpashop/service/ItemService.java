package jpabook.newjpashop.service;

import jpabook.newjpashop.controller.BookForm;
import jpabook.newjpashop.domain.item.Book;
import jpabook.newjpashop.domain.item.Item;
import jpabook.newjpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, BookForm parm) {
        Book findItem = (Book) itemRepository.findOne(itemId);
        // 예시 이므로 업데이트 대상은 Book 하나로만 잡는다.
        findItem.changeBookInfo(parm.getName(), parm.getPrice(), parm.getAuthor(), parm.getIsbn());
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
