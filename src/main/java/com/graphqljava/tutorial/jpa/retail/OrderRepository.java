package com.graphqljava.tutorial.jpa.retail;

import com.graphqljava.tutorial.models.RetailModels;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<RetailModels.order, UUID> {
    //List<RetailModels.order> findAllByAccount_idIn(final Set<UUID> ids);
}
