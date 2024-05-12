package com.example.jpashop.service;

import com.example.jpashop.domain.Address;
import com.example.jpashop.domain.Member;
import com.example.jpashop.domain.Order;
import com.example.jpashop.domain.OrderStatus;
import com.example.jpashop.domain.item.Book;
import com.example.jpashop.exception.NotEnoughStockException;
import com.example.jpashop.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 상품주문() {
        Member member = createMember("yonghan");
        em.persist(member);
        Book book = createBook("JPA", 10000, 10);
        em.persist(book);

        Long orderId = orderService.order(member.getId(), book.getId(), 2);

        Order order = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, order.getStatus());
        assertEquals(1, order.getOrderItems().size());
        assertEquals(10000 * 2, order.getTotalPrice());
        assertEquals(8, book.getStockQuantity());
    }

    @Test
    public void 주문취소() {
        Member member = createMember("yonghan");
        em.persist(member);
        Book book = createBook("JPA", 10000, 10);
        em.persist(book);

        Long orderId = orderService.order(member.getId(), book.getId(), 2);

        orderService.cancelOrder(orderId);

        assertEquals(10, book.getStockQuantity());
    }

    @Test
    public void 재고수량초과() {
        Member member = createMember("yonghan");
        em.persist(member);
        Book book = createBook("JPA", 10000, 10);
        em.persist(book);

        assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), book.getId(), 12);

        });
    }

    public Member createMember(String name) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address("서울", "서대문구", "12313"));
        return member;
    }
    public Book createBook(String name, int price, int stock) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stock);
        return book;
    }

}