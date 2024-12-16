package com.croche.lariharumi.models.Category;

import java.util.List;

import com.croche.lariharumi.models.Product.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    
    @Id
    private Long id;
    private String name;
    private String description;
    private String image;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

}
