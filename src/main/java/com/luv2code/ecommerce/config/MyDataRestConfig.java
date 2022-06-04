package com.luv2code.ecommerce.config;

import com.luv2code.ecommerce.entity.Country;
import com.luv2code.ecommerce.entity.Product;
import com.luv2code.ecommerce.entity.ProductCategory;
import com.luv2code.ecommerce.entity.State;
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
        // for Product entity
        disableHttpMethods(config, Product.class);

        // for ProductCategory entity
        disableHttpMethods(config, ProductCategory.class);

        // for Country entity
        disableHttpMethods(config, Country.class);

        // for State entity
        disableHttpMethods(config, State.class);

        exposeIds(config);
    }

    private void disableHttpMethods(RepositoryRestConfiguration config,
                                        Class domainType) {
        config.getExposureConfiguration()
                .forDomainType(domainType)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(DISABLED_HTTP_METHODS))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(DISABLED_HTTP_METHODS));
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
