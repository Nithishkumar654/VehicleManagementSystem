package com.project.DemoApp.repository;

import com.project.DemoApp.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRepository extends JpaRepository<Product, Integer> {
}
