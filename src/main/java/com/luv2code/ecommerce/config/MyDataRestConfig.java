package com.luv2code.ecommerce.config;

import com.luv2code.ecommerce.entity.Product;
import com.luv2code.ecommerce.entity.ProductCategory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Type;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private static final HttpMethod[] DISABLED_HTTP_METHODS = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};

    private final EntityManager entityManager;

    public MyDataRestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(DISABLED_HTTP_METHODS))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(DISABLED_HTTP_METHODS));

        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(DISABLED_HTTP_METHODS))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(DISABLED_HTTP_METHODS));

        exposeIds(config);
    }

    private void exposeIds(RepositoryRestConfiguration config) {

        // get the list of EntityTypes from the EntityManager
        Set<EntityType<?>> entityTypes = entityManager.getMetamodel()
                .getEntities();

        // map EntityType to Class
        List<? extends Class<?>> entityClasses = entityTypes.stream()
                .map(Type::getJavaType)
                .toList();
        // expose entity ids for the array of entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }


}
