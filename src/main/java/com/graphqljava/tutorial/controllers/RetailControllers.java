package com.graphqljava.tutorial.controllers;

import com.graphqljava.tutorial.jpa.retail.AccountRepository;
import com.graphqljava.tutorial.jpa.retail.OrderDetailRepository;
import com.graphqljava.tutorial.jpa.retail.OrderRepository;
import com.graphqljava.tutorial.jpa.retail.ProductRepository;
import com.graphqljava.tutorial.jpa.retail.RegionRepository;
import com.graphqljava.tutorial.models.RetailModels;
import com.graphqljava.tutorial.models.input.AccountInput;
import com.graphqljava.tutorial.models.input.OrderDetailInput;
import com.graphqljava.tutorial.models.input.OrderInput;
import com.graphqljava.tutorial.models.input.ProductInput;
import com.graphqljava.tutorial.models.input.RegionInput;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
//import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

public class RetailControllers {

    @Controller
    public static class AccountController {
        private final AccountRepository accountRepository;

        public AccountController(final AccountRepository accountRepository) {
            this.accountRepository = accountRepository;
        }

        @QueryMapping
        public List<RetailModels.account> accounts(@Argument AccountInput input) {
            if (null == input) {
                input = new AccountInput();
            }

            int limit = input.getLimit() > 0 ? input.getLimit() : Integer.MAX_VALUE;
            PageRequest pageRequest = PageRequest.of(0, limit);

            return this.accountRepository.findAll(Example.of(new RetailModels.account(
                    input.getId(),
                    input.getName(),
                    input.getCreated_at(),
                    input.getUpdated_at()
            )), pageRequest).toList();
        }

        @SchemaMapping
        public RetailModels.account account(final RetailModels.order order, @Argument AccountInput input) {
            if (null == input) {
                input = new AccountInput();
            }
            if (null == input.getId()) {
                input.setId(order.getAccount_id());
            }

            return this.accountRepository.findOne(Example.of(new RetailModels.account(
                    input.getId(),
                    input.getName(),
                    input.getCreated_at(),
                    input.getUpdated_at()
            ))).orElse(null);
        }
    }

    @Controller
    public static class OrderController {
        private final OrderRepository orderRepository;

        public OrderController(final OrderRepository orderRepository) {
            this.orderRepository = orderRepository;
        }

        @QueryMapping
        public List<RetailModels.order> orders(@Argument OrderInput input) {
            if (null == input) {
                input = new OrderInput();
            }

            int limit = input.getLimit() > 0 ? input.getLimit() : Integer.MAX_VALUE;
            PageRequest pageRequest = PageRequest.of(0, limit);

            return this.orderRepository.findAll(Example.of(new RetailModels.order(
                    input.getId(),
                    input.getAccount_id(),
                    input.getStatus(),
                    input.getRegion(),
                    input.getCreated_at(),
                    input.getUpdated_at()
            )), pageRequest).toList();
        }

        @SchemaMapping
        public RetailModels.order order(final RetailModels.order_detail order_detail, @Argument OrderInput input) {
            if (null == input) {
                input = new OrderInput();
            }
            if (null == input.getId()) {
                input.setId(order_detail.getOrder_id());
            }

            return this.orderRepository.findOne(Example.of(new RetailModels.order(
                    input.getId(),
                    input.getAccount_id(),
                    input.getStatus(),
                    input.getRegion(),
                    input.getCreated_at(),
                    input.getUpdated_at()
            ))).orElse(null);
        }

        @SchemaMapping
        public List<RetailModels.order> orders(final RetailModels.account account, @Argument OrderInput input) {
            if (null == input) {
                input = new OrderInput();
            }
            if (null == input.getId()) {
                input.setId(account.getId());
            }

            return orders(input);
        }

        /*
        @BatchMapping
        public Map<RetailModels.account, List<RetailModels.order>> ordersMulti(final List<RetailModels.account> accounts) {
            Map<UUID, RetailModels.account> accountMap = accounts
                    .stream()
                    .collect(Collectors.toMap(RetailModels.account::id, Function.identity()));
            Set<UUID> ids = accountMap.keySet();
            Map<UUID, List<RetailModels.order>> orders = this.orderRepository
                    .findAllByAccount_idIn(ids)
                    .stream()
                    .collect(Collectors.groupingBy(RetailModels.order::account_id));

            return orders
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(entry -> accountMap.get(entry.getKey()), Map.Entry::getValue));
        }
        */
    }

    @Controller
    public static class OrderDetailController {
        private final OrderDetailRepository orderDetailRepository;

        public OrderDetailController(final OrderDetailRepository orderDetailRepository) {
            this.orderDetailRepository = orderDetailRepository;
        }

        @QueryMapping
        public List<RetailModels.order_detail> order_details(@Argument OrderDetailInput input) {
            if (null == input) {
                input = new OrderDetailInput();
            }

            int limit = input.getLimit() > 0 ? input.getLimit() : Integer.MAX_VALUE;
            PageRequest pageRequest = PageRequest.of(0, limit);

            return this.orderDetailRepository.findAll(Example.of(new RetailModels.order_detail(
                    input.getId(),
                    input.getOrder_id(),
                    input.getProduct_id(),
                    input.getUnits(),
                    input.getCreated_at(),
                    input.getUpdated_at()
            )), pageRequest).toList();
        }

        @SchemaMapping
        public List<RetailModels.order_detail> order_details(final RetailModels.order order, @Argument OrderDetailInput input) {
            if (null == input) {
                input = new OrderDetailInput();
            }
            if (null == input.getOrder_id()) {
                input.setOrder_id(order.getId());
            }

            return order_details(input);
        }

        @SchemaMapping
        public List<RetailModels.order_detail> order_details(final RetailModels.product product, @Argument OrderDetailInput input) {
            if (null == input) {
                input = new OrderDetailInput();
            }
            if (null == input.getProduct_id()) {
                input.setProduct_id(product.getId());
            }

            return order_details(input);
        }
    }

    @Controller
    public static class ProductController {
        private final ProductRepository productRepository;

        public ProductController(final ProductRepository productRepository) {
            this.productRepository = productRepository;
        }

        @QueryMapping
        public List<RetailModels.product> products(@Argument ProductInput input) {
            if (null == input) {
                input = new ProductInput();
            }

            return this.productRepository.findAll(Example.of(new RetailModels.product(
                    input.getId(),
                    input.getName(),
                    input.getPrice(),
                    input.getCreated_at(),
                    input.getUpdated_at()
            )));
        }

        @SchemaMapping
        public RetailModels.product product(final RetailModels.order_detail order_detail, @Argument ProductInput input) {
            if (null == input) {
                input = new ProductInput();
            }
            if (null == input.getId()) {
                input.setId(order_detail.getProduct_id());
            }

            return this.productRepository.findOne(Example.of(new RetailModels.product(
                    input.getId(),
                    input.getName(),
                    input.getPrice(),
                    input.getCreated_at(),
                    input.getUpdated_at()
            ))).orElse(null);
        }
    }

    @Controller
    public static class RegionController {
        private final RegionRepository regionRepository;

        public RegionController(final RegionRepository regionRepository) {
            this.regionRepository = regionRepository;
        }

        @QueryMapping
        public List<RetailModels.region> regions(@Argument RegionInput input) {
            if (null == input) {
                input = new RegionInput();
            }

            int limit = input.getLimit() > 0 ? input.getLimit() : Integer.MAX_VALUE;
            PageRequest pageRequest = PageRequest.of(0, limit);

            return this.regionRepository.findAll(Example.of(new RetailModels.region(
                    input.getValue(),
                    input.getDescription()
            )), pageRequest).toList();
        }
    }
}
