package com.paycore.backend.repositories;

import com.paycore.backend.entities.Customer;
import com.paycore.backend.entities.Merchant;
import com.paycore.backend.enums.CustomerStatus;
import com.paycore.backend.enums.MerchantCategory;
import com.paycore.backend.enums.MerchantStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MerchantRepository  extends JpaRepository<Merchant, UUID> {

    public List<Merchant> findByStatus(MerchantStatus status);
    public List<Merchant> findByCategory(MerchantCategory category);
    public List<Merchant> findByStatusAndCategory(MerchantStatus status, MerchantCategory category);

}
