package com.example.jpashop;

import com.example.jpashop.domain.*;
import com.example.jpashop.domain.item.Book;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }
    @Component
    @RequiredArgsConstructor
    @Transactional
    static class InitService {
        private final EntityManager em;
        public void dbInit1() {
            Member member = CreateMember("member1", "서울", "123", "123");
            em.persist(member);

            Book book1 = createBook("JPA1", 10000, 100);
            em.persist(book1);

            Book book2 = createBook("JPA2", 20000, 200);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 10);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 20);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }
        public void dbInit2() {
            Member member = CreateMember("member2", "부산", "321", "312");
            em.persist(member);

            Book book1 = createBook("SPRING1", 100000, 1000);
            em.persist(book1);

            Book book2 = createBook("SPRING2", 200000, 2000);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 100000, 100);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 200000, 200);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }
        private Book createBook(String name, int price, int stockQuantity) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
            return book;
        }
        private static Member CreateMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }


    }
}
