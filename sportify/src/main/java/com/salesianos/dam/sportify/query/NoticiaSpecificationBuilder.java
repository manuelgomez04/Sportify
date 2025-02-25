package com.salesianos.dam.sportify.query;

import com.salesianos.dam.sportify.noticia.model.Noticia;
import com.salesianos.dam.sportify.user.model.User;
import com.salesianos.dam.sportify.util.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


import java.util.List;

public class NoticiaSpecificationBuilder  extends GenericSpecificationBuilder<Noticia>{
    public NoticiaSpecificationBuilder(List<SearchCriteria> params) {
        super(params);
    }

   
    }
