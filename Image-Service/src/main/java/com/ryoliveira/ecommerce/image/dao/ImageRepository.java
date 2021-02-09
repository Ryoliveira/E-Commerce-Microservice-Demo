package com.ryoliveira.ecommerce.image.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ryoliveira.ecommerce.image.model.Image;


public interface ImageRepository extends JpaRepository<Image, Integer> {

}
