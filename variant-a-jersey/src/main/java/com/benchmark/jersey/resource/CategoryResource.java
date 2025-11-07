package com.benchmark.jersey.resource;

import com.benchmark.dto.CategoryDTO;
import com.benchmark.jersey.config.JpaConfig;
import com.benchmark.model.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    @GET
    public Response listCategories(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Category> query = em.createQuery(
                    "SELECT c FROM Category c ORDER BY c.id", Category.class);
            query.setFirstResult(page * size);
            query.setMaxResults(size);

            List<CategoryDTO> dtos = query.getResultList().stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());

            return Response.ok(dtos).build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("{id}")
    public Response getCategory(@PathParam("id") Long id) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            Category cat = em.find(Category.class, id);
            if (cat == null) return Response.status(404).build();
            return Response.ok(toDTO(cat)).build();
        } finally {
            em.close();
        }
    }

    @POST
    public Response createCategory(CategoryDTO dto) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            Category cat = new Category();
            cat.setCode(dto.getCode());
            cat.setName(dto.getName());
            em.persist(cat);
            em.getTransaction().commit();
            return Response.status(201).entity(toDTO(cat)).build();
        } catch (Exception e) {
            em.getTransaction().rollback();
            return Response.status(400).entity(e.getMessage()).build();
        } finally {
            em.close();
        }
    }

    @PUT
    @Path("{id}")
    public Response updateCategory(@PathParam("id") Long id, CategoryDTO dto) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            Category cat = em.find(Category.class, id);
            if (cat == null) return Response.status(404).build();

            cat.setCode(dto.getCode());
            cat.setName(dto.getName());
            em.merge(cat);
            em.getTransaction().commit();
            return Response.ok(toDTO(cat)).build();
        } catch (Exception e) {
            em.getTransaction().rollback();
            return Response.status(400).entity(e.getMessage()).build();
        } finally {
            em.close();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteCategory(@PathParam("id") Long id) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            Category cat = em.find(Category.class, id);
            if (cat == null) return Response.status(404).build();

            em.remove(cat);
            em.getTransaction().commit();
            return Response.noContent().build();
        } catch (Exception e) {
            em.getTransaction().rollback();
            return Response.status(400).entity(e.getMessage()).build();
        } finally {
            em.close();
        }
    }

    private CategoryDTO toDTO(Category cat) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(cat.getId());
        dto.setCode(cat.getCode());
        dto.setName(cat.getName());
        dto.setUpdatedAt(cat.getUpdatedAt());
        return dto;
    }
}
