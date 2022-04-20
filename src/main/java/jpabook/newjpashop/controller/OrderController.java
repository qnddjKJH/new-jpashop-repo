package jpabook.newjpashop.controller;

import jpabook.newjpashop.domain.Member;
import jpabook.newjpashop.domain.Order;
import jpabook.newjpashop.domain.item.Item;
import jpabook.newjpashop.repository.OrderSearch;
import jpabook.newjpashop.service.ItemService;
import jpabook.newjpashop.service.MemberService;
import jpabook.newjpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);
        return "orders/orderList";
    }

    @GetMapping("/order")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "orders/orderForm";
    }

    @PostMapping("/order")
    public String order(
            @RequestParam("memberId") Long memberId,
            @RequestParam("itemId") Long itemId,
            @RequestParam("count") int count) {
        // 컨트롤러에서는 식별자만 넘기고 영속성 컨텍스트가 존재 하는 곳에서
        // 작업을 하도록 설계하는것이 좋다 밑에 처럼
        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }

    @PostMapping("/order/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
