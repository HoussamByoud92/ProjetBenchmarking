package com.benchmark.jersey.resource;

import com.benchmark.dto.ItemDTO;
import com.benchmark.jersey.config.JpaConfig;
import com.benchmark.model.Item;
import com.benchmark.model.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    @GET
    public Response listItems(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Item> query = em.createQuery(
                    "SELECT i FROM Item i ORDER BY i.id", Item.class);
            query.setFirstResult(page * size);
            query.setMaxResults(size);

            List<ItemDTO> dtos = query.getResultList().stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());

            return Response.ok(dtos).build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("{id}")
    public Response getItem(@PathParam("id") Long id) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            Item item = em.find(Item.class, id);
            if (item == null) return Response.status(404).build();
            return Response.ok(toDTO(item)).build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("by-category")
    public Response getItemsByCategory(
            @QueryParam("categoryId") Long categoryId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Item> query = em.createQuery(
                    "SELECT i FROM Item i WHERE i.category.id = :cid ORDER BY i.id", Item.class);
            query.setParameter("cid", categoryId);
            query.setFirstResult(page * size);
            query.setMaxResults(size);

            List<ItemDTO> dtos = query.getResultList().stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());

            return Response.ok(dtos).build();
        } finally {
            em.close();
        }
    }

    @POST
    public Response createItem(ItemDTO dto) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            Category cat = em.find(Category.class, dto.getCategoryId());
            if (cat == null) return Response.status(400).entity("Category not found").build();

            Item item = new Item();
            item.setSku(dto.getSku());
            item.setName(dto.getName());
            item.setPrice(dto.getPrice());
            item.setStock(dto.getStock());
            item.setCategory(cat);
            item.setDescription(dto.getDescription());
            em.persist(item);
            em.getTransaction().commit();
            return Response.status(201).entity(toDTO(item)).build();
        } catch (Exception e) {
            em.getTransaction().rollback();
            return Response.status(400).entity(e.getMessage()).build();
        } finally {
            em.close();
        }
    }

    @PUT
    @Path("{id}")
    public Response updateItem(@PathParam("id") Long id, ItemDTO dto) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            Item item = em.find(Item.class, id);
            if (item == null) return Response.status(404).build();

            item.setSku(dto.getSku());
            item.setName(dto.getName());
            item.setPrice(dto.getPrice());
            item.setStock(dto.getStock());
            item.setDescription(dto.getDescription());

            em.merge(item);
            em.getTransaction().commit();
            return Response.ok(toDTO(item)).build();
        } catch (Exception e) {
            em.getTransaction().rollback();
            return Response.status(400).entity(e.getMessage()).build();
        } finally {
            em.close();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteItem(@PathParam("id") Long id) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            Item item = em.find(Item.class, id);
            if (item == null) return Response.status(404).build();

            em.remove(item);
            em.getTransaction().commit();
            return Response.noContent().build();
        } catch (Exception e) {
            em.getTransaction().rollback();
            return Response.status(400).entity(e.getMessage()).build();
        } finally {
            em.close();
        }
    }

    private ItemDTO toDTO(Item item) {
        ItemDTO dto = new ItemDTO();
        dto.setId(item.getId());
        dto.setSku(item.getSku());
        dto.setName(item.getName());
        dto.setPrice(item.getPrice());
        dto.setStock(item.getStock());
        dto.setCategoryId(item.getCategory().getId());
        dto.setUpdatedAt(item.getUpdatedAt());
        dto.setDescription(item.getDescription());
        return dto;
    }
}
