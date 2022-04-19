package jpabook.newjpashop.controller;

import jpabook.newjpashop.domain.item.Book;
import jpabook.newjpashop.domain.item.Item;
import jpabook.newjpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {
        Book book = Book.builder()
                .name(form.getName())
                .price(form.getPrice())
                .stockQuantity(form.getStockQuantity())
                .author(form.getAuthor())
                .isbn(form.getIsbn())
                .build();
        itemService.saveItem(book);
        return "redirect:/items";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId);

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setAuthor(item.getAuthor());
        form.setPrice(item.getPrice());
        form.setIsbn(item.getIsbn());
        form.setStockQuantity(item.getStockQuantity());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    /**
     * 권장 코드
     */
    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute BookForm form) {
        itemService.updateItem(itemId, form);
        return "redirect:/items";
    }

/**
 * 비권장 코드
 */
//    @PostMapping("/items/{itemId}/edit")
//    public String updateItem(@PathVariable String itemId, @ModelAttribute BookForm form) {
//        Book book = Book.builder()
//                .id(form.getId())
//                .name(form.getName())
//                .price(form.getPrice())
//                .stockQuantity(form.getStockQuantity())
//                .author(form.getAuthor())
//                .isbn(form.getIsbn())
//                .build();
//        itemService.saveItem(book);
//        return "redirect:/items";
//    }
}
