package com.ryoliveira.ecommerce.image.dao;

import com.ryoliveira.ecommerce.image.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface ImageRepository extends JpaRepository<Image, Integer> {

    public Optional<List<Image>> findByProductId(int productId);

    public Optional<List<Image>> findByProductIdAndIsProductMainImage(int productId, boolean isProductImage);

    @Transactional
    public List<Image> deleteByProductId(int productId);

}
